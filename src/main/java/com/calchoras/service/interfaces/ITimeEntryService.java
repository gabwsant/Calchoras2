package com.calchoras.service.interfaces;

import com.calchoras.model.TimeEntry;
import java.util.List;

/**
 * Define o contrato para o serviço que gerencia as batidas de ponto.
 */
public interface ITimeEntryService {

    /**
     * Retorna uma lista de todas as batidas de um funcionário específico.
     * @param employeeId O ID do funcionário.
     * @return Uma lista de TimeEntries.
     */
    List<TimeEntry> getTimeEntriesForEmployee(int employeeId);

    /**
     * Adiciona uma nova batida de ponto e persiste a alteração.
     * @param timeEntry A nova batida a ser adicionada.
     */
    void addTimeEntry(TimeEntry timeEntry);

    /**
     * Remove todas as batidas de ponto associadas a um funcionário.
     * Útil quando um funcionário é removido do sistema.
     * @param employeeId O ID do funcionário cujas batidas serão removidas.
     */
    void deleteEntriesForEmployee(int employeeId);

    // Você pode adicionar métodos de update e delete para uma única batida se precisar
    // void updateTimeEntry(TimeEntry timeEntry);
    // void deleteTimeEntry(int entryId);
}