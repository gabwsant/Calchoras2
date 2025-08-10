package com.calchoras.model;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Um record para armazenar os resultados do cálculo de um dia.
 * Records são ideais para objetos que apenas guardam dados (imutáveis).
 */

public record DailyCalculationResult(
        LocalDate date,
        Duration workedHours,
        Duration expectedHours,
        Duration overtimeHours,
        Duration negativeHours
) {}