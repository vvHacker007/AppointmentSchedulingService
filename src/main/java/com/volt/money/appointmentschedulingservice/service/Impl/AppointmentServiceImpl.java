package com.volt.money.appointmentschedulingservice.service.Impl;

import com.volt.money.appointmentschedulingservice.dto.AppointmentDTO;
import com.volt.money.appointmentschedulingservice.dto.Slot;
import com.volt.money.appointmentschedulingservice.model.Appointment;
import com.volt.money.appointmentschedulingservice.model.ServiceOperator;
import com.volt.money.appointmentschedulingservice.repository.AppointmentRepository;
import com.volt.money.appointmentschedulingservice.service.AppointmentService;
import com.volt.money.appointmentschedulingservice.service.ServiceOperatorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.volt.money.appointmentschedulingservice.util.TimeUtil.getEpoch;
import static com.volt.money.appointmentschedulingservice.util.TimeUtil.getLocalZone;
import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ServiceOperatorService serviceOperatorService;

    @Override
    public Appointment scheduleAppointment(AppointmentDTO appointmentDTO) {

        ServiceOperator serviceOperator = serviceOperatorService.getServiceOperatorById(
                appointmentDTO.getServiceOperatorId());
        if (isNull(serviceOperator)) {
            throw new RuntimeException("Service Operator Not Found for the appointment.");
        }

        if (!checkSlotAvailability(appointmentDTO, serviceOperator)) {
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
        if (startTime < System.currentTimeMillis()) {
            throw new RuntimeException("Slot time has passed");
        }
        if (endTime <= startTime) {
            throw new RuntimeException("End Time cannot be before Start Time");
        }
        endTime = startTime + 3600000; // assuming slot should be exactly 1 hour long;
        List<Appointment> appointments = getNonCanceledAppointmentsByServiceOperatorIdAndLocalDate(
                serviceOperator.getId(), appointmentDTO.getDate());
        for (Appointment appointment : appointments) {
            if (appointment.getSlot().getStartTime() < endTime && appointment.getSlot().getEndTime() > startTime &&
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
    public List<Appointment> getAppointmentByServiceOperatorIdAndLocalDate(String serviceOperatorId,
                                                                           LocalDate localDate) {
        return appointmentRepository.findNonDeletedAppointmentsByServiceOperatorIdAndLocalDate(serviceOperatorId,
                localDate.toString());
    }

    private List<Appointment> getNonCanceledAppointmentsByServiceOperatorIdAndLocalDate(String serviceOperatorId,
                                                                                        LocalDate localDate) {
        return appointmentRepository.findNonDeletedNonCanceledAppointmentsByServiceOperatorIdAndLocalDate(
                serviceOperatorId, localDate.toString());
    }

    @Override
    public List<Appointment> getAppointmentByServiceOperatorId(String serviceOperatorId) {
        return appointmentRepository.findNonDeletedAppointmentsByServiceOperatorId(serviceOperatorId);
    }

    @Override
    public List<Appointment> getOpenSlotsForAppointmentByServiceOperatorId(AppointmentDTO appointmentDTO) {

        ServiceOperator serviceOperator = serviceOperatorService.getServiceOperatorById(
                appointmentDTO.getServiceOperatorId());
        if (isNull(serviceOperator)) {
            throw new RuntimeException("Service Operator Not Found for the appointment.");
        }

        List<Slot> openAppointmentSlots = getOpenSlots(appointmentDTO, serviceOperator);

        // DEBUG
        // Booked Slots ->
        // {
        // 07:59 -> 08:59 (open slot -> 00:00 -> 07:59)
        // 09:30 -> 10:30 (open slot -> 08:59 -> 09:30) (not 1 hr long so discard)
        // 11:20 -> 12:20 (open slot -> 10:30 -> 11:20) (not 1 hr long so discard)
        // 19:20 -> 20:20 (open slot -> 12:20 -> 19:20)
        // 22:10 -> 23:10 (open slot -> 20:20 -> 22:10)
        // (last open slot -> 23:10 -> 23:59) (not 1 hr long so discard) (considered slots to be booked in one day only)
        //}
        for(Slot slot : openAppointmentSlots) {
            System.out.println("Start TIME:{}" + Instant.ofEpochMilli(slot.getStartTime()).atOffset(getLocalZone()).toLocalTime());
            System.out.println("End TIME:{}" + Instant.ofEpochMilli(slot.getEndTime()).atOffset(getLocalZone()).toLocalTime());
        }

        appointmentDTO.setId(null);

        return openAppointmentSlots.stream().map(slot -> {
            Appointment appointment = new Appointment();
            appointment.setSlot(slot);
            appointment.setServiceOperatorId(appointment.getServiceOperatorId());
            appointment.setDate(appointment.getDate());
            return appointment;
        }).collect(Collectors.toList());
    }

    public List<Slot> getOpenSlots(AppointmentDTO appointmentDTO, ServiceOperator serviceOperator) {
        List<Slot> openSlots = new ArrayList<>();
        List<Slot> bookedAppointmentSlots = appointmentRepository
                .findNonDeletedNonCanceledAppointmentsByServiceOperatorIdAndLocalDate(
                        appointmentDTO.getServiceOperatorId(), appointmentDTO.getDate().toString()).stream()
                .map(Appointment::getSlot).sorted(Comparator.comparingLong(Slot::getStartTime)).collect(Collectors.toList());

        // https://www.baeldung.com/java-day-start-end
        long startTime = getEpoch(LocalTime.MIN, appointmentDTO.getDate());
        long endTime = getEpoch(LocalTime.MAX, appointmentDTO.getDate());

        while (startTime<endTime) {
            for(Slot bookedSlot : bookedAppointmentSlots) {
                if (bookedSlot.getStartTime() - startTime >= 3600000) {
                    Slot slot = new Slot();
                    slot.setStartTime(startTime);
                    slot.setEndTime(bookedSlot.getStartTime());
                    openSlots.add(slot);
                }
                startTime = bookedSlot.getEndTime();
            }
        }

        return openSlots;
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
        serviceOperatorService.removeAppointment(appointment.getServiceOperatorId(), appointment.getId());
        return appointmentRepository.save(appointment);
    }
}
