package com.healthtech.controller;

import com.healthtech.service.VideoCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class WebSocketController {

    @Autowired
    private VideoCallService videoCallService;

    @MessageMapping("/video-call.{meetingId}.signal")
    public void handleWebRTCSignal(
            @DestinationVariable String meetingId,
            @Payload Map<String, Object> payload) {

        Long fromUserId = Long.valueOf(payload.get("fromUserId").toString());
        Object signal = payload.get("signal");

        videoCallService.handleWebRTCSignal(meetingId, fromUserId, signal);
    }

    @MessageMapping("/video-call.{meetingId}.chat")
    public void handleChatMessage(
            @DestinationVariable String meetingId,
            @Payload Map<String, Object> payload) {

        Long senderId = Long.valueOf(payload.get("senderId").toString());
        String senderName = payload.get("senderName").toString();
        String message = payload.get("message").toString();

        videoCallService.sendChatMessage(meetingId, senderId, senderName, message);
    }

    @MessageMapping("/video-call.{meetingId}.join")
    public void handleUserJoin(
            @DestinationVariable String meetingId,
            @Payload Map<String, Object> payload) {

        Long userId = Long.valueOf(payload.get("userId").toString());
        String userName = payload.get("userName").toString();

        videoCallService.joinVideoCall(meetingId, userId, userName);
    }
}