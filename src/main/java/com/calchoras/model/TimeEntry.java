package com.calchoras.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class TimeEntry {
    private int id;
    private int employeeId;
    private LocalDate entryDate;
    private LocalTime clockIn;
    private LocalTime lunchIn;
    private LocalTime lunchOut;
    private LocalTime clockOut;
    private boolean isDayOff = false;

    public TimeEntry(
            int employeeId,
            LocalDate entryDate,
            LocalTime clockIn,
            LocalTime lunchIn,
            LocalTime lunchOut,
            LocalTime clockOut) {
        this.employeeId = employeeId;
        this.entryDate = entryDate;
        this.clockIn = clockIn;
        this.lunchIn = lunchIn;
        this.lunchOut = lunchOut;
        this.clockOut = clockOut;
    }
}
