package com.volt.money.appointmentschedulingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOperator {

    @Id
    @Field(value = "_id")
    private String id;
    @NonNull
    private String name;
    private List<String> appointments;
    private Long createdTime;
    private Long modifiedTime;
    private boolean deleted;
}
