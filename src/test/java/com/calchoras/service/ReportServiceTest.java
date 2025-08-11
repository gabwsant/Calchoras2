package com.calchoras.service;

import com.calchoras.model.DailyCalculationResult;
import com.calchoras.model.Employee;
import com.calchoras.model.MonthCalculationResult;
import com.calchoras.model.TimeEntry;
import com.calchoras.service.interfaces.IDailyCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para o ReportService")
class ReportServiceTest {

    private ReportService reportService;
    private Employee sampleEmployee;

    /**
     * Implementação "falsa" do nosso serviço de cálculo diário, apenas para este teste.
     * Não importa qual batida de ponto ele receba, ele SEMPRE retorna o mesmo resultado:
     * 10 minutos de horas extras e 5 minutos de horas negativas.
     */
    private static class MockDailyCalculationService implements IDailyCalculationService {
        @Override
        public DailyCalculationResult calculate(TimeEntry timeEntry, Employee employee) {
            return new DailyCalculationResult(
                    timeEntry.getEntryDate(),
                    Duration.ofHours(8),
                    Duration.ofHours(8).plusMinutes(5),
                    Duration.ofMinutes(10),
                    Duration.ofMinutes(15) // <--- O valor que acreditamos estar correto
            );
        }
    }

    /**
     * Método de setup, executado antes de cada teste.
     */
    @BeforeEach
    void setUp() {
        // 1. Criamos nossa implementação falsa.
        IDailyCalculationService mockDailyCalculator = new MockDailyCalculationService();

        // 2. Criamos o ReportService, injetando o nosso serviço falso.
        reportService = new ReportService(mockDailyCalculator);

        // 3. Criamos um funcionário de exemplo para usar nos testes.
        sampleEmployee = new Employee("Empresa Teste", "Funcionário Mock", LocalTime.of(8,0), LocalTime.of(17,0), 60);
    }

    @Test
    @DisplayName("Deve agregar corretamente os totais de múltiplas batidas de ponto")
    void calculateMonthlyBalance_withMultipleEntries_shouldAggregateTotals() {
        // Arrange (Preparação)
        // Criamos uma lista com 3 batidas de ponto. Não importa o conteúdo, pois o mock sempre retorna o mesmo resultado.
        List<TimeEntry> entries = List.of(
                new TimeEntry(1, LocalDate.of(2025, 8, 1), LocalTime.of(8,0), LocalTime.of(12,0), LocalTime.of(13,0), LocalTime.of(17,10)),
                new TimeEntry(1, LocalDate.of(2025, 8, 2), LocalTime.of(8,0), LocalTime.of(12,0), LocalTime.of(13,0), LocalTime.of(16,45)),
                new TimeEntry(1, LocalDate.of(2025, 8, 3), LocalTime.of(8,0), LocalTime.of(12,0), LocalTime.of(13,0), LocalTime.of(17,0))
        );

        // Act (Ação)
        MonthCalculationResult result = reportService.calculateMonthlyBalance(sampleEmployee, entries);

        // Assert (Verificação)
        // O mock retorna 10min de extra e 15min negativas para CADA UMA das 3 batidas.
        // Portanto, os totais devem ser 3 * 10 = 30min e 3 * 15 = 45min.
        Duration expectedOvertime = Duration.ofMinutes(30);
        Duration expectedNegative = Duration.ofMinutes(45);

        assertNotNull(result, "O resultado não deveria ser nulo.");
        assertEquals(3, result.dailyResults().size(), "A lista de resultados diários deveria conter 3 itens.");
        assertEquals(expectedOvertime, result.totalOvertime(), "O total de horas extras está incorreto.");
        assertEquals(expectedNegative, result.totalNegativeHours(), "O total de horas negativas está incorreto.");
    }

    @Test
    @DisplayName("Deve retornar totais zerados para uma lista de batidas vazia")
    void calculateMonthlyBalance_withEmptyEntryList_shouldReturnZeroDurations() {
        // Arrange
        // Criamos uma lista vazia.
        List<TimeEntry> emptyList = Collections.emptyList();

        // Act
        MonthCalculationResult result = reportService.calculateMonthlyBalance(sampleEmployee, emptyList);

        // Assert
        assertNotNull(result);
        assertTrue(result.dailyResults().isEmpty(), "A lista de resultados diários deveria estar vazia.");
        assertEquals(Duration.ZERO, result.totalOvertime(), "O total de horas extras deveria ser zero.");
        assertEquals(Duration.ZERO, result.totalNegativeHours(), "O total de horas negativas deveria ser zero.");
    }
}