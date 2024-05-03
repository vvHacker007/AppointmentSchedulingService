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
        appointmentDTO.setRescheduled(false);
        appointmentDTO.setCanceled(false);
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

    @PostMapping("/service-operator/open-slots")
    public ResponseEntity<List<Appointment>> getOpenSlotsForAppointmentByServiceOperatorId(@Validated @RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.getOpenSlotsForAppointmentByServiceOperatorId(appointmentDTO));
    }

    @PutMapping("/")
    public ResponseEntity<Appointment> updateAppointment(@Validated @RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.updateAppointment(appointmentDTO));
    }

    @PostMapping("/cancel")
    public ResponseEntity<Appointment> cancelAppointment(@Validated @RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentDTO));
    }

    @PostMapping("/reschedule")
    public ResponseEntity<Appointment> rescheduleAppointment(@Validated @RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.rescheduleAppointment(appointmentDTO));
    }


    @DeleteMapping("/")
    public ResponseEntity<Appointment> deleteAppointment(@Validated @RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.deleteAppointment(appointmentDTO));
    }

    @PostMapping("/service-operator/date/open-slots")
    public ResponseEntity<List<Appointment>> getAppointmentByServiceOperatorIdAndLocalDate(@Validated @RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.getAppointmentByServiceOperatorIdAndLocalDate(appointmentDTO.getServiceOperatorId(), appointmentDTO.getDate()));
    }
}
