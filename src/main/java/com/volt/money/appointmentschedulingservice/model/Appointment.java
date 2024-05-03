package com.volt.money.appointmentschedulingservice.model;

import com.volt.money.appointmentschedulingservice.dto.Slot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(name = "serviceOperatorId_date", def = "{'serviceOperatorId': 1, 'date': 1, 'slot.startTime': 1}")
// https://docs.spring.io/spring-data/mongodb/docs/current/api/org/springframework/data/mongodb/core/index/CompoundIndex.html
public class Appointment {
    @Id
    @Field(value = "_id")
    private String id;
    @Indexed
    private String serviceOperatorId;
    @Indexed
    private String date;
    private Slot slot;
    private boolean rescheduled;
    private boolean canceled;
    private Long createdTime;
    private Long modifiedTime;
    private boolean deleted;
}
