package com.volt.money.appointmentschedulingservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ServiceOperatorDTO {
    private String id;
    @NonNull
    private String name;
}
