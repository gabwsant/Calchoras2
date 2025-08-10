package com.calchoras.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class TimeEntry {
    private LocalDate date;
    private LocalTime clockIn;
    private LocalTime lunchIn;
    private LocalTime lunchOut;
    private LocalTime clockOut;
    private boolean isDayOff;
}
