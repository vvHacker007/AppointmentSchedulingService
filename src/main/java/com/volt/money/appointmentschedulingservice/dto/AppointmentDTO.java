package com.volt.money.appointmentschedulingservice.dto;


import com.volt.money.appointmentschedulingservice.model.Appointment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.volt.money.appointmentschedulingservice.util.TimeUtil.getEpoch;

@Data
@NoArgsConstructor
public class AppointmentDTO {
    private String id;
    private String serviceOperatorId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean rescheduled;
    private Boolean canceled;

    public Appointment apply(Appointment appointment) {
        appointment.setId(id);
        appointment.setServiceOperatorId(serviceOperatorId);
        Slot slot = new Slot();
        slot.setStartTime(getEpoch(startTime, date));
        slot.setEndTime(getEpoch(startTime, date)+3600000);
        appointment.setSlot(slot);
        appointment.setDate(date.toString());
        appointment.setCreatedTime(System.currentTimeMillis());
        appointment.setModifiedTime(System.currentTimeMillis());
        return appointment;
    }
}
