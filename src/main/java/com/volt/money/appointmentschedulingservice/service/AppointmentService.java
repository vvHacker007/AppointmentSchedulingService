package com.volt.money.appointmentschedulingservice.service;

import com.volt.money.appointmentschedulingservice.dto.AppointmentDTO;
import com.volt.money.appointmentschedulingservice.model.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    Appointment scheduleAppointment(AppointmentDTO appointmentDTO);
    Appointment cancelAppointment(AppointmentDTO appointmentDTO);
    Appointment getAppointmentById(String id);
    List<Appointment> getAppointmentByServiceOperatorIdAndLocalDate(String id, LocalDate localDate);
    List<Appointment> getAppointmentByServiceOperatorId(String id);
    List<Appointment> getOpenSlotsForAppointmentByServiceOperatorId(AppointmentDTO appointmentDTO);
    Appointment updateAppointment(AppointmentDTO appointmentDTO);
    Appointment deleteAppointment(AppointmentDTO appointmentDTO);

}
