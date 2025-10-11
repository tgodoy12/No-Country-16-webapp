package com.healthtech.service;

import com.healthtech.dto.ChatMessageDto;
import com.healthtech.dto.VideoCallSessionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VideoCallService {

    // Almacenamiento en memoria para el MVP
    private final Map<String, VideoCallSession> activeSessions = new ConcurrentHashMap<>();
    private final Map<String, List<ChatMessageDto>> chatHistories = new ConcurrentHashMap<>();
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public VideoCallSessionDto startVideoCall(Long appointmentId) {
        String meetingId = UUID.randomUUID().toString();
        String meetingUrl = "https://meet.healthtech.com/" + meetingId;

        // Crear sesión
        VideoCallSession session = new VideoCallSession(meetingId, appointmentId);
        activeSessions.put(meetingId, session);

        // Notificar inicio de llamada
        Map<String, Object> message = new HashMap<>();
        message.put("type", "CALL_STARTED");
        message.put("meetingId", meetingId);
        message.put("meetingUrl", meetingUrl);
        message.put("appointmentId", appointmentId);

        messagingTemplate.convertAndSend("/topic/video-call/" + meetingId, message);

        System.out.println("🎥 Videollamada iniciada: " + meetingId);

        return convertToDto(session);
    }

    public void joinVideoCall(String meetingId, Long userId, String userName) {
        VideoCallSession session = activeSessions.get(meetingId);
        if (session != null) {
            session.addParticipant(userId, userName);

            // Notificar que alguien se unió
            Map<String, Object> message = new HashMap<>();
            message.put("type", "USER_JOINED");
            message.put("userId", userId);
            message.put("userName", userName);
            message.put("meetingId", meetingId);
            message.put("participants", session.getParticipants());

            messagingTemplate.convertAndSend("/topic/video-call/" + meetingId, message);

            System.out.println("👤 Usuario " + userName + " se unió a: " + meetingId);
        }
    }

    public void handleWebRTCSignal(String meetingId, Long fromUserId, Object signal) {
        // Reenviar señal WebRTC a otros participantes
        Map<String, Object> message = new HashMap<>();
        message.put("type", "WEBRTC_SIGNAL");
        message.put("fromUserId", fromUserId);
        message.put("signal", signal);
        message.put("meetingId", meetingId);

        messagingTemplate.convertAndSend("/topic/video-call/" + meetingId, message);
    }

    public void sendChatMessage(String meetingId, Long senderId, String senderName, String messageText) {
        // Crear mensaje
        ChatMessageDto chatMessage = new ChatMessageDto();
        chatMessage.setMeetingId(meetingId);
        chatMessage.setSenderId(senderId);
        chatMessage.setSenderName(senderName);
        chatMessage.setMessage(messageText);
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setType(ChatMessageDto.MessageType.TEXT);

        // Guardar en historial (memoria)
        chatHistories.computeIfAbsent(meetingId, k -> new ArrayList<>()).add(chatMessage);

        // Enviar a través de WebSocket
        Map<String, Object> message = new HashMap<>();
        message.put("type", "CHAT_MESSAGE");
        message.put("message", chatMessage);

        messagingTemplate.convertAndSend("/topic/chat/" + meetingId, message);

        System.out.println("💬 Chat: " + senderName + ": " + messageText);
    }

    public List<ChatMessageDto> getChatHistory(String meetingId) {
        return chatHistories.getOrDefault(meetingId, new ArrayList<>());
    }

    public void endVideoCall(String meetingId) {
        VideoCallSession session = activeSessions.remove(meetingId);
        if (session != null) {
            // Notificar fin de llamada
            Map<String, Object> message = new HashMap<>();
            message.put("type", "CALL_ENDED");
            message.put("meetingId", meetingId);

            messagingTemplate.convertAndSend("/topic/video-call/" + meetingId, message);

            System.out.println("🛑 Videollamada finalizada: " + meetingId);
        }
    }

    private VideoCallSessionDto convertToDto(VideoCallSession session) {
        VideoCallSessionDto dto = new VideoCallSessionDto();
        dto.setMeetingId(session.getMeetingId());
        dto.setAppointmentId(session.getAppointmentId());
        dto.setParticipants(session.getParticipants().keySet());
        dto.setActive(true);
        return dto;
    }

    // Clase interna para manejo de sesiones
    private static class VideoCallSession {
        private final String meetingId;
        private final Long appointmentId;
        private final Map<Long, String> participants; // userId -> userName
        private final LocalDateTime startTime;

        public VideoCallSession(String meetingId, Long appointmentId) {
            this.meetingId = meetingId;
            this.appointmentId = appointmentId;
            this.participants = new ConcurrentHashMap<>();
            this.startTime = LocalDateTime.now();
        }

        public void addParticipant(Long userId, String userName) {
            participants.put(userId, userName);
        }

        public void removeParticipant(Long userId) {
            participants.remove(userId);
        }

        public Map<Long, String> getParticipants() {
            return new HashMap<>(participants);
        }

        public String getMeetingId() {
            return meetingId;
        }

        public Long getAppointmentId() {
            return appointmentId;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }
    }
}