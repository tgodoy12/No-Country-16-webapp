package com.healthtech.controller;

import com.healthtech.dto.VideoCallSessionDto;
import com.healthtech.service.VideoCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @Autowired
    private VideoCallService videoCallService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Endpoint para probar videollamada rápida
    @PostMapping("/video-call/quick-start")
    public ResponseEntity<Map<String, String>> quickStartVideoCall() {
        try {
            // Crear sesión de prueba
            VideoCallSessionDto session = videoCallService.startVideoCall(999L);

            // Enviar mensaje de prueba al chat
            Map<String, Object> chatMessage = new HashMap<>();
            chatMessage.put("type", "CHAT_MESSAGE");
            chatMessage.put("fromUser", "Sistema");
            chatMessage.put("message", "✅ Videollamada de prueba iniciada correctamente");
            chatMessage.put("timestamp", System.currentTimeMillis());

            messagingTemplate.convertAndSend("/topic/chat/" + session.getMeetingId(), chatMessage);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("meetingId", session.getMeetingId());
            response.put("message", "Videollamada de prueba creada - Revisa logs del backend");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Probar envío de señal WebRTC
    @PostMapping("/video-call/{meetingId}/test-signal")
    public ResponseEntity<Map<String, String>> testWebRTCSignal(@PathVariable String meetingId) {
        try {
            // Simular señal WebRTC
            Map<String, Object> signal = new HashMap<>();
            signal.put("type", "offer");
            signal.put("sdp", "v=0\\r\\no=- 123456 2 IN IP4 127.0.0.1\\r\\n...");

            videoCallService.handleWebRTCSignal(meetingId, 1L, signal);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Señal WebRTC enviada al meeting: " + meetingId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Ver estado del WebSocket
    @GetMapping("/websocket/status")
    public ResponseEntity<Map<String, String>> websocketStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "active");
        response.put("endpoint", "ws://localhost:8080/ws");
        response.put("message", "WebSocket configurado correctamente");
        return ResponseEntity.ok(response);
    }

    // Probar chat directo
    @PostMapping("/chat/{meetingId}/send")
    public ResponseEntity<Map<String, String>> testChatMessage(
            @PathVariable String meetingId,
            @RequestParam String message) {
        try {
            Map<String, Object> chatMessage = new HashMap<>();
            chatMessage.put("type", "CHAT_MESSAGE");
            chatMessage.put("fromUser", "Test User");
            chatMessage.put("message", message);
            chatMessage.put("timestamp", System.currentTimeMillis());

            messagingTemplate.convertAndSend("/topic/chat/" + meetingId, chatMessage);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Mensaje enviado al meeting: " + meetingId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}