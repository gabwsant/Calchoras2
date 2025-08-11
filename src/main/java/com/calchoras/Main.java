package com.calchoras;

import com.calchoras.controller.PontoController;
import com.calchoras.service.*;
import com.calchoras.service.interfaces.*;
import com.calchoras.view.MainFrame;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		// Garante que todo o código da interface gráfica
		// rode na sua própria thread (a Event Dispatch Thread - EDT).
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// --- 1. CRIAÇÃO DOS SERVIÇOS ---
				IEmployeeService employeeService = new EmployeeService();
				ITimeEntryService timeEntryService = new TimeEntryService();
				IDailyCalculationService dailyCalculationService = new DailyCalculationService();
				IReportService reportService = new ReportService(dailyCalculationService);

				// --- 2. CRIAÇÃO DA VIEW---
				MainFrame view = new MainFrame();

				// --- 3. CRIAÇÃO DO CONTROLLER ---
				// Aqui acontece a "Injeção de Dependência": nós entregamos todas as peças
				// que o Controller precisa para trabalhar.
				new PontoController(view, dailyCalculationService, employeeService, reportService, timeEntryService);
			}
		});
	}
}
