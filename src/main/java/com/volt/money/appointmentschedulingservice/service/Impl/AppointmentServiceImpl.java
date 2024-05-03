package com.volt.money.appointmentschedulingservice.service.Impl;

import com.volt.money.appointmentschedulingservice.dto.AppointmentDTO;
import com.volt.money.appointmentschedulingservice.model.Appointment;
import com.volt.money.appointmentschedulingservice.model.ServiceOperator;
import com.volt.money.appointmentschedulingservice.repository.AppointmentRepository;
import com.volt.money.appointmentschedulingservice.service.AppointmentService;
import com.volt.money.appointmentschedulingservice.service.ServiceOperatorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.volt.money.appointmentschedulingservice.util.TimeUtil.getEpoch;
import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ServiceOperatorService serviceOperatorService;

    @Override
    public Appointment scheduleAppointment(AppointmentDTO appointmentDTO) {

        ServiceOperator serviceOperator = serviceOperatorService.getServiceOperatorById(appointmentDTO.getServiceOperatorId());
        if(isNull(serviceOperator)) {
            throw new RuntimeException("Service Operator Not Found for the appointment.");
        }

        if(!checkSlotAvailability(appointmentDTO, serviceOperator)) {
            throw new RuntimeException("Slot not available!");
        }
        Appointment appointment = appointmentDTO.apply(new Appointment());
        appointment.setId(UUID.randomUUID().toString());
        serviceOperatorService.addAppointment(appointment.getServiceOperatorId(), appointment.getId());
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment cancelAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentRepository.findNonDeletedById(appointmentDTO.getId()).orElseThrow();
        appointment.setCanceled(Boolean.TRUE);
        serviceOperatorService.removeAppointment(appointment.getServiceOperatorId(), appointment.getId());
        return appointmentRepository.save(appointment);
    }

    private boolean checkSlotAvailability(AppointmentDTO appointmentDTO, ServiceOperator serviceOperator) {
        // https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html
        long startTime = getEpoch(appointmentDTO.getStartTime(), appointmentDTO.getDate());
        long endTime = getEpoch(appointmentDTO.getEndTime(), appointmentDTO.getDate());
        if(startTime<System.currentTimeMillis()) {
            throw new RuntimeException("Slot time has passed");
        }
        if(endTime<=startTime) {
            throw new RuntimeException("End Time cannot be before Start Time");
        }
        endTime = startTime+3600000; // assuming slot should be exactly 1 hour long;
        List<Appointment> appointments =
                getNonCanceledAppointmentsByServiceOperatorIdAndLocalDate(serviceOperator.getId(),
                appointmentDTO.getDate());
        for (Appointment appointment : appointments) {
            if (appointment.getSlot().getStartTime()<endTime && appointment.getSlot().getEndTime()>(startTime) &&
                !Objects.equals(appointment.getId(), appointmentDTO.getId())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Appointment getAppointmentById(String id) {
        return appointmentRepository.findNonDeletedById(id).orElse(null);
    }

    @Override
    public List<Appointment> getAppointmentByServiceOperatorIdAndLocalDate(String serviceOperatorId, LocalDate localDate) {
        return appointmentRepository.findNonDeletedAppointmentsByServiceOperatorIdAndLocalDate(serviceOperatorId, localDate.toString());
    }

    private List<Appointment> getNonCanceledAppointmentsByServiceOperatorIdAndLocalDate(String serviceOperatorId,
                                                                                        LocalDate localDate) {
        return appointmentRepository.findNonDeletedNonCanceledAppointmentsByServiceOperatorIdAndLocalDate(serviceOperatorId, localDate.toString());
    }

    @Override
    public List<Appointment> getAppointmentByServiceOperatorId(String serviceOperatorId) {
        return appointmentRepository.findNonDeletedAppointmentsByServiceOperatorId(serviceOperatorId);
    }

    @Override
    public Appointment updateAppointment(AppointmentDTO appointmentDTO) {
        appointmentRepository.findById(appointmentDTO.getId()).orElseThrow();
        return scheduleAppointment(appointmentDTO);
    }

    @Override
    public Appointment deleteAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentRepository.findNonDeletedById(appointmentDTO.getId()).orElseThrow();
        appointment.setDeleted(true);
        return appointmentRepository.save(appointment);
    }
}