package com.volt.money.appointmentschedulingservice.repository;

import com.volt.money.appointmentschedulingservice.model.Appointment;
import com.volt.money.appointmentschedulingservice.model.ServiceOperator;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends CrudRepository<Appointment, String> {
    @Query("{ 'deleted' : false, 'serviceOperatorId' : ?0 }")
    List<Appointment> findNonDeletedAppointmentsByServiceOperatorId(String serviceOperatorId);
    @Query("{ 'deleted' : false, 'serviceOperatorId' : ?0, 'date': ?1 }")
    List<Appointment> findNonDeletedAppointmentsByServiceOperatorIdAndLocalDate(String serviceOperatorId, String date);

    @Query("{ 'canceled': false, 'deleted' : false, 'serviceOperatorId' : ?0, 'date': ?1 }")
    List<Appointment> findNonDeletedNonCanceledAppointmentsByServiceOperatorIdAndLocalDate(String serviceOperatorId,
                                                                                  String date);
    @Query("{ 'deleted' : false, 'id' : ?0 }")
    Optional<Appointment> findNonDeletedById(String id);
}
