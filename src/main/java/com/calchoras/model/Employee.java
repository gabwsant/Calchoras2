package com.calchoras.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class Employee {
    private String company;
    private String name;
    private LocalTime shiftIn;
    private LocalTime shiftOut;
    private long lunchBreakMinutes;
}
