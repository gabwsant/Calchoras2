package com.calchoras.model;

import java.time.Duration;
import java.util.List;

/**
 * Guarda o resultado consolidado de um período de cálculo (ex: um mês).
 * @param dailyResults Uma lista com os detalhes de cada dia, para o relatório.
 * @param totalOvertime O total de horas extras no período.
 * @param totalNegativeHours O total de horas negativas no período.
 */
public record MonthCalculationResult(
        List<DailyCalculationResult> dailyResults,
        Duration totalOvertime,
        Duration totalNegativeHours
) {}
