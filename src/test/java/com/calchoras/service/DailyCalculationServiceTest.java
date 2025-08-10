package com.calchoras.service;

import com.calchoras.model.Employee;
import com.calchoras.model.TimeEntry;
import com.calchoras.model.DailyCalculationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test; // Importação do JUnit
import static org.junit.jupiter.api.Assertions.*; // Importação para asserções

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@DisplayName("Teste para o DailyCalculationService")
class DailyCalculationServiceTest {

    @Test // Anotação que marca este método como um caso de teste
    @DisplayName("Deve gerar um resultado de cálculo para um dia com 1 hora extra")
    void testCalculate_WithOvertime_ShouldReturnCorrectValues() {

        // --- 1. Arrange (Preparação) ---
        // Preparamos todos os dados de entrada para o nosso teste.
        Employee employee = new Employee(
                "Empresa de Teste",
                "Funcionario Teste",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                60);

        TimeEntry timeEntry = new TimeEntry(
                1,
                LocalDate.now(),
                LocalTime.parse("08:00"),
                LocalTime.parse("12:00"),
                LocalTime.parse("13:00"),
                LocalTime.parse("18:00")
        );

        DailyCalculationService service = new DailyCalculationService();

        // --- 2. Act (Ação) ---
        // Executamos o método que queremos testar.
        DailyCalculationResult result = service.calculate(timeEntry, employee);

        // --- 3. Assert (Verificação) ---
        // Verificamos se o resultado foi o esperado.
        assertEquals(Duration.ofHours(9), result.workedHours(), "As horas trabalhadas deveriam ser 9.");
        assertEquals(Duration.ofHours(8), result.expectedHours(), "As horas esperadas deveriam ser 8.");
        assertEquals(Duration.ofHours(1), result.overtimeHours(), "Deveria ter 1 hora extra.");
        assertEquals(Duration.ZERO, result.negativeHours(), "Não deveria ter horas negativas.");
    }
}