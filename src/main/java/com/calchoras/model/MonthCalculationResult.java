package com.calchoras.model;

import java.time.Duration;
import java.time.YearMonth;

public record MonthCalculationResult (
    YearMonth month,
    Duration totalWorkedHours,
    Duration totalExpectedHours,
    Duration totalOvertimeHours,
    Duration totalNegativeHours
) {}
