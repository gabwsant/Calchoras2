package com.calchoras.service;

import com.calchoras.model.DailyCalculationResult;
import com.calchoras.model.Employee;
import com.calchoras.model.MonthCalculationResult;
import com.calchoras.model.TimeEntry;
import com.calchoras.service.interfaces.IDailyCalculationService;
import com.calchoras.service.interfaces.IReportService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ReportService implements IReportService {

    private final IDailyCalculationService dailyCalculationService;

    public ReportService(IDailyCalculationService dailyCalculationService) {
        this.dailyCalculationService = dailyCalculationService;
    }

    @Override
    public MonthCalculationResult calculateMonthlyBalance(Employee employee, List<TimeEntry> entries){
        List<DailyCalculationResult> dailyResults = new ArrayList<>();
        Duration totalOvertime = Duration.ZERO;
        Duration totalNegativeHours = Duration.ZERO;

        for (TimeEntry entry : entries) {
            DailyCalculationResult dailyResult = dailyCalculationService.calculate(entry, employee);
            dailyResults.add(dailyResult);
            totalOvertime = totalOvertime.plus(dailyResult.overtimeHours());
            totalNegativeHours = totalNegativeHours.plus(dailyResult.negativeHours());
        }
        return new MonthCalculationResult(dailyResults, totalOvertime, totalNegativeHours);
    }
}
