package com.calchoras;

import com.calchoras.model.DailyCalculationResult;
import com.calchoras.model.Employee;
import com.calchoras.model.TimeEntry;
import com.calchoras.service.DailyCalculationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootApplication
public class CalchorasApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalchorasApplication.class, args);

		TimeEntry batida = new TimeEntry(
				LocalDate.now(),
				LocalTime.of(22, 0),
				LocalTime.of(0, 0),
				LocalTime.of(1, 0),
				LocalTime.of(5, 0),
				false
		);

//		TimeEntry batida = new TimeEntry(
//				LocalDate.now(),
//				null,
//				null,
//				null,
//				null,
//				true
//		);

		Employee employee = new Employee(
				"Teste",
				"Gabriel",
				LocalTime.of(22, 0),
				LocalTime.of(6, 0),
				60
		);

		DailyCalculationService calculadora = new DailyCalculationService();
		DailyCalculationResult resultado = calculadora.calculate(batida, employee);

		System.out.println(resultado.date() + " " + resultado.expectedHours().toHours() + " " +
				resultado.workedHours().toHours() + " " + resultado.overtimeHours().toHours() +
				" " + resultado.negativeHours().toHours());
	}
}
