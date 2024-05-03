package com.volt.money.appointmentschedulingservice.util;

import lombok.experimental.UtilityClass;

import java.time.*;

@UtilityClass
public class TimeUtil {

    public static ZoneOffset getLocalZone() {
        return ZoneOffset.ofHoursMinutes(5, 30);
    }

    public static long getEpoch(LocalTime localTime, LocalDate localDate) {
        return localTime.toEpochSecond(localDate, getLocalZone())*1000;
    }

}
