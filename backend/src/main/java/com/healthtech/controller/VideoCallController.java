package com.healthtech.controller;

import com.healthtech.dto.VideoCallSessionDto;
import com.healthtech.service.VideoCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/video-call")
@CrossOrigin(origins = "*")
public class VideoCallController {

    @Autowired
    private VideoCallService videoCallService;

    @PostMapping("/{appointmentId}/start")
    public ResponseEntity<VideoCallSessionDto> startVideoCall(@PathVariable Long appointmentId) {
        VideoCallSessionDto session = videoCallService.startVideoCall(appointmentId);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/{meetingId}/end")
    public ResponseEntity<Void> endVideoCall(@PathVariable String meetingId) {
        videoCallService.endVideoCall(meetingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{meetingId}/chat/history")
    public ResponseEntity<?> getChatHistory(@PathVariable String meetingId) {
        return ResponseEntity.ok(videoCallService.getChatHistory(meetingId));
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        return ResponseEntity.ok(Map.of("status", "VideoCall API funcionando"));
    }
}