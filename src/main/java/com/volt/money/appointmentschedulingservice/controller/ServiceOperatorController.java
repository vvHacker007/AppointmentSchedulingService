package com.volt.money.appointmentschedulingservice.controller;

import com.volt.money.appointmentschedulingservice.dto.ServiceOperatorDTO;
import com.volt.money.appointmentschedulingservice.model.ServiceOperator;
import com.volt.money.appointmentschedulingservice.service.ServiceOperatorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/serviceOperator")
public class ServiceOperatorController {

    private final ServiceOperatorService serviceOperatorService;

    @PostMapping("/")
    public ResponseEntity<ServiceOperator> createServiceOperator(@Validated @RequestBody
                                                                 ServiceOperatorDTO serviceOperatorDTO) {
        return ResponseEntity.ok(serviceOperatorService.createServiceOperator(serviceOperatorDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOperator> getServiceOperatorById(@PathVariable("id") String serviceOperatorId) {
        return ResponseEntity.ok(serviceOperatorService.getServiceOperatorById(serviceOperatorId));
    }

    @PutMapping("/")
    public ResponseEntity<ServiceOperator> updateServiceOperator(@Validated @RequestBody ServiceOperatorDTO serviceOperatorDTO) {
        return ResponseEntity.ok(serviceOperatorService.updateServiceOperator(serviceOperatorDTO));
    }

    @DeleteMapping("/")
    public ResponseEntity<ServiceOperator> deleteServiceOperator(@Validated @RequestBody ServiceOperatorDTO serviceOperatorDTO) {
        return ResponseEntity.ok(serviceOperatorService.deleteServiceOperator(serviceOperatorDTO));
    }
}
