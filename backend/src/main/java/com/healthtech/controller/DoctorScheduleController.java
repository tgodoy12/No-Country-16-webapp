package com.healthtech.controller;

import com.healthtech.entity.Doctor;
import com.healthtech.entity.DoctorSchedule;
import com.healthtech.repository.DoctorRepository;
import com.healthtech.repository.DoctorScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-schedules")
@CrossOrigin(origins = "*")
public class DoctorScheduleController {

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorSchedule>> getDoctorSchedules(@PathVariable Long doctorId) {
        List<DoctorSchedule> schedules = doctorScheduleRepository.findByDoctorId(doctorId);
        return ResponseEntity.ok(schedules);
    }

    @PostMapping
    public ResponseEntity<DoctorSchedule> createSchedule(@RequestBody ScheduleRequest request) {
        // Validar que el doctor existe
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctor(doctor);
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());

        DoctorSchedule savedSchedule = doctorScheduleRepository.save(schedule);
        return ResponseEntity.ok(savedSchedule);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        doctorScheduleRepository.deleteById(scheduleId);
        return ResponseEntity.ok().build();
    }

    // DTO interno para el request
    public static class ScheduleRequest {
        private Long doctorId;
        private java.time.DayOfWeek dayOfWeek;
        private java.time.LocalTime startTime;
        private java.time.LocalTime endTime;

        // Getters y Setters
        public Long getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(Long doctorId) {
            this.doctorId = doctorId;
        }

        public java.time.DayOfWeek getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(java.time.DayOfWeek dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public java.time.LocalTime getStartTime() {
            return startTime;
        }

        public void setStartTime(java.time.LocalTime startTime) {
            this.startTime = startTime;
        }

        public java.time.LocalTime getEndTime() {
            return endTime;
        }

        public void setEndTime(java.time.LocalTime endTime) {
            this.endTime = endTime;
        }
    }
}