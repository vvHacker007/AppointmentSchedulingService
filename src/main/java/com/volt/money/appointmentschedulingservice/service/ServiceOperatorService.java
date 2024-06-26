package com.volt.money.appointmentschedulingservice.service;

import com.volt.money.appointmentschedulingservice.dto.ServiceOperatorDTO;
import com.volt.money.appointmentschedulingservice.model.ServiceOperator;

public interface ServiceOperatorService {
    ServiceOperator createServiceOperator(ServiceOperatorDTO serviceOperatorDTO);
    ServiceOperator getServiceOperatorById(String id);
    ServiceOperator updateServiceOperator(ServiceOperatorDTO serviceOperatorDTO);
    ServiceOperator deleteServiceOperator(ServiceOperatorDTO serviceOperatorDTO);
    void addAppointment(String serviceOperatorId, String appointmentId);
    void removeAppointment(String serviceOperatorId, String appointmentId);

}
