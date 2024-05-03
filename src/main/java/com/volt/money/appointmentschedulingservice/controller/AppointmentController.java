package com.volt.money.appointmentschedulingservice.controller;

import com.volt.money.appointmentschedulingservice.dto.AppointmentDTO;
import com.volt.money.appointmentschedulingservice.model.Appointment;
import com.volt.money.appointmentschedulingservice.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/schedule")
    public ResponseEntity<Appointment> scheduleAppointment(@Validated @RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.scheduleAppointment(appointmentDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable("id") String appointmentId) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(appointmentId));
    }

    @GetMapping("/service-operator/{serviceOperatorId}")
    public ResponseEntity<List<Appointment>> getAppointmentByServiceOperatorId(@PathVariable("serviceOperatorId") String serviceOperatorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentByServiceOperatorId(serviceOperatorId));
    }

    @PutMapping("/")
    public ResponseEntity<Appointment> updateAppointment(@Validated @RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.updateAppointment(appointmentDTO));
    }

    @PostMapping("/cancel")
    public ResponseEntity<Appointment> cancelAppointment(@Validated @RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentDTO));
    }


    @DeleteMapping("/")
    public ResponseEntity<Appointment> deleteAppointment(@Validated @RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.deleteAppointment(appointmentDTO));
    }
}