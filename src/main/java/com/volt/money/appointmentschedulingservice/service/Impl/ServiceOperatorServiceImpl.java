package com.volt.money.appointmentschedulingservice.service.Impl;

import com.volt.money.appointmentschedulingservice.dto.ServiceOperatorDTO;
import com.volt.money.appointmentschedulingservice.model.ServiceOperator;
import com.volt.money.appointmentschedulingservice.repository.ServiceOperatorRepository;
import com.volt.money.appointmentschedulingservice.service.ServiceOperatorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ServiceOperatorServiceImpl implements ServiceOperatorService {

    private final ServiceOperatorRepository serviceOperatorRepository;

    @Override
    public ServiceOperator createServiceOperator(ServiceOperatorDTO serviceOperatorDTO) {
        ServiceOperator serviceOperator = new ServiceOperator();
        serviceOperator.setName(serviceOperatorDTO.getName());
        serviceOperator.setAppointments(new ArrayList<>());
        serviceOperator.setId(UUID.randomUUID().toString());
        serviceOperator.setCreatedTime(System.currentTimeMillis());
        serviceOperator.setModifiedTime(System.currentTimeMillis());
        return serviceOperatorRepository.save(serviceOperator);
    }

    @Override
    public ServiceOperator getServiceOperatorById(String id) {
        return serviceOperatorRepository.findNonDeletedById(id).orElse(null);
    }

    @Override
    public ServiceOperator updateServiceOperator(ServiceOperatorDTO serviceOperatorDTO) {
        ServiceOperator serviceOperator =
                serviceOperatorRepository.findNonDeletedById(serviceOperatorDTO.getId()).orElseThrow();
        serviceOperator.setName(serviceOperatorDTO.getName());
        serviceOperator.setModifiedTime(System.currentTimeMillis());
        return serviceOperatorRepository.save(serviceOperator);
    }

    @Override
    public ServiceOperator deleteServiceOperator(ServiceOperatorDTO serviceOperatorDTO) {
        ServiceOperator serviceOperator =
                serviceOperatorRepository.findNonDeletedById(serviceOperatorDTO.getId()).orElseThrow();
        serviceOperator.setDeleted(true);
        return serviceOperatorRepository.save(serviceOperator);
    }

    @Override
    public void addAppointment(String serviceOperatorId, String appointmentId) {
        ServiceOperator serviceOperator =
                serviceOperatorRepository.findNonDeletedById(serviceOperatorId).orElseThrow();
        List<String> appointments = serviceOperator.getAppointments();
        appointments.add(appointmentId);
        serviceOperator.setAppointments(appointments);
        serviceOperatorRepository.save(serviceOperator);
    }

    @Override
    public void removeAppointment(String serviceOperatorId, String appointmentId) {
        ServiceOperator serviceOperator =
                serviceOperatorRepository.findNonDeletedById(serviceOperatorId).orElseThrow();
        List<String> appointments = serviceOperator.getAppointments();
        appointments.remove(appointmentId);
        serviceOperator.setAppointments(appointments);
        serviceOperatorRepository.save(serviceOperator);
    }
}
