package com.healthtech.controller;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDto {
    private String meetingId;
    private Long senderId;
    private String senderName;
    private String message;
    private LocalDateTime timestamp;
    private MessageType type;

    public enum MessageType {
        TEXT, SYSTEM, PRESCRIPTION
    }
}