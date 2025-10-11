package com.healthtech.dto;

import lombok.Data;

import java.util.Set;

@Data
public class VideoCallSessionDto {
    private String meetingId;
    private Long appointmentId;
    private Set<Long> participants;
    private boolean active;
}