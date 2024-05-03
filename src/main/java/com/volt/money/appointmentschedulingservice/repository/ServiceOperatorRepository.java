package com.volt.money.appointmentschedulingservice.repository;

import com.volt.money.appointmentschedulingservice.model.ServiceOperator;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ServiceOperatorRepository extends CrudRepository<ServiceOperator, String> {
    @Query("{ 'deleted' : false, 'id' : ?0 }")
    Optional<ServiceOperator> findNonDeletedById(String id);
}
