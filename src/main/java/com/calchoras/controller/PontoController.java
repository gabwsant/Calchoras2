package com.calchoras.controller;

import com.calchoras.service.interfaces.IDailyCalculationService;
import com.calchoras.service.interfaces.IEmployeeService;
import com.calchoras.service.interfaces.IReportService;
import com.calchoras.service.interfaces.ITimeEntryService;
import com.calchoras.view.MainFrame;

import java.util.ArrayList;

public class PontoController {
    private final MainFrame view;
    private final IDailyCalculationService dailyCalculationService;
    private final IEmployeeService employeeService;
    private final IReportService reportService;
    private final ITimeEntryService timeEntryService;

    public PontoController(
            MainFrame view,
            IDailyCalculationService dailyCalculationService,
            IEmployeeService employeeService,
            IReportService reportService,
            ITimeEntryService timeEntryService
    ) {
        this.view = view;
        this.dailyCalculationService = dailyCalculationService;
        this.employeeService = employeeService;
        this.reportService = reportService;
        this.timeEntryService = timeEntryService;
    }
}
