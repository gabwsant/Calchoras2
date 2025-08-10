package com.calchoras.service;

import com.calchoras.model.TimeEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para o TimeEntryService")
class TimeEntryServiceTest {

    // Usaremos um arquivo separado para não interferir com os dados de produção.
    private static final String TEST_FILE_PATH = "batidas_teste.json";
    private TimeEntryService timeEntryService;

    @BeforeEach
    void setUp() {
        deleteTestFile();
        // Passamos o caminho do arquivo de teste para o construtor.
        timeEntryService = new TimeEntryService(TEST_FILE_PATH);
    }


    @AfterEach
    void tearDown() {
        deleteTestFile();
    }

    private void deleteTestFile() {
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
        } catch (IOException e) {
            System.err.println("Falha ao deletar arquivo de teste: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Deve adicionar uma batida de ponto, atribuir ID e persistir no arquivo")
    void addTimeEntry_shouldAssignIdAndPersist() {
        // Arrange (Preparação)
        // O serviço deve começar com zero batidas.
        assertTrue(timeEntryService.getTimeEntriesForEmployee(1).isEmpty(), "A lista de batidas deveria começar vazia.");

        TimeEntry newEntry = new TimeEntry(1, LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(12, 0), LocalTime.of(13, 0), LocalTime.of(17, 0));

        // Act (Ação)
        timeEntryService.addTimeEntry(newEntry);

        // Assert (Verificação)
        // 1. Verifica se a batida foi adicionada na instância atual do serviço.
        List<TimeEntry> entries = timeEntryService.getTimeEntriesForEmployee(1);
        assertEquals(1, entries.size());
        assertEquals(1, entries.get(0).getId(), "O ID da primeira batida deveria ser 1.");
        assertEquals(1, entries.get(0).getEmployeeId());

        // 2. Verifica se a batida foi salva no arquivo, criando uma nova instância do serviço para forçar a leitura.
        TimeEntryService newInstanceService = new TimeEntryService(TEST_FILE_PATH);
        List<TimeEntry> loadedEntries = newInstanceService.getTimeEntriesForEmployee(1);
        assertEquals(1, loadedEntries.size(), "Deveria carregar 1 batida do arquivo.");
        assertEquals(1, loadedEntries.get(0).getId());
    }

    @Test
    @DisplayName("Deve retornar apenas as batidas do funcionário solicitado")
    void getTimeEntriesForEmployee_shouldReturnOnlyMatchingEntries() {
        // Arrange
        // Adiciona batidas para dois funcionários diferentes.
        timeEntryService.addTimeEntry(new TimeEntry(1, LocalDate.now(), LocalTime.of(8, 0), null, null, null));
        timeEntryService.addTimeEntry(new TimeEntry(2, LocalDate.now(), LocalTime.of(9, 0), null, null, null));
        timeEntryService.addTimeEntry(new TimeEntry(1, LocalDate.now().plusDays(1), LocalTime.of(8, 5), null, null, null));

        // Act
        List<TimeEntry> employee1Entries = timeEntryService.getTimeEntriesForEmployee(1);
        List<TimeEntry> employee2Entries = timeEntryService.getTimeEntriesForEmployee(2);
        List<TimeEntry> employee3Entries = timeEntryService.getTimeEntriesForEmployee(3); // Um funcionário sem batidas

        // Assert
        assertEquals(2, employee1Entries.size(), "Funcionário 1 deveria ter 2 batidas.");
        assertEquals(1, employee2Entries.size(), "Funcionário 2 deveria ter 1 batida.");
        assertTrue(employee3Entries.isEmpty(), "Funcionário 3 não deveria ter batidas.");
    }

    @Test
    @DisplayName("Deve remover todas as batidas de um funcionário específico")
    void deleteEntriesForEmployee_shouldRemoveEntriesAndPersist() {
        // Arrange
        timeEntryService.addTimeEntry(new TimeEntry(1, LocalDate.now(), LocalTime.of(8, 0), null, null, null));
        timeEntryService.addTimeEntry(new TimeEntry(2, LocalDate.now(), LocalTime.of(9, 0), null, null, null));
        timeEntryService.addTimeEntry(new TimeEntry(1, LocalDate.now().plusDays(1), LocalTime.of(8, 5), null, null, null));

        // Act
        timeEntryService.deleteEntriesForEmployee(1);

        // Assert
        // 1. Verifica na instância atual.
        assertTrue(timeEntryService.getTimeEntriesForEmployee(1).isEmpty(), "As batidas do funcionário 1 deveriam ter sido removidas.");
        assertFalse(timeEntryService.getTimeEntriesForEmployee(2).isEmpty(), "As batidas do funcionário 2 deveriam permanecer.");
        assertEquals(1, timeEntryService.getTimeEntriesForEmployee(2).size());

        // 2. Verifica a persistência criando uma nova instância.
        TimeEntryService newInstanceService = new TimeEntryService(TEST_FILE_PATH);
        assertTrue(newInstanceService.getTimeEntriesForEmployee(1).isEmpty(), "As batidas do funcionário 1 também deveriam ter sumido do arquivo.");
        assertEquals(1, newInstanceService.getTimeEntriesForEmployee(2).size(), "As batidas do funcionário 2 deveriam ter sido carregadas do arquivo.");
    }
}