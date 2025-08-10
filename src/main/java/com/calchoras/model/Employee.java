package com.calchoras.model;

import lombok.*;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class Employee {
    private int id;
    private String company;
    private String name;
    private LocalTime shiftIn;
    private LocalTime shiftOut;
    private long lunchBreakMinutes;

    public Employee(
            String company,
            String name,
            LocalTime shiftIn,
            LocalTime shiftOut,
            long lunchBreakMinutes) {
        this.company = company;
        this.name = name;
        this.shiftIn = shiftIn;
        this.shiftOut = shiftOut;
        this.lunchBreakMinutes = lunchBreakMinutes;
    }
}
