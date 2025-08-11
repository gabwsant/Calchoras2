package com.calchoras.service.interfaces;

import com.calchoras.model.Employee;
import com.calchoras.model.MonthCalculationResult;
import com.calchoras.model.TimeEntry;

import java.util.List;

public interface IReportService {

    /**
     * Calcula o balanço de horas consolidado para um funcionário em um dado período.
     * @param employee O funcionário para o qual o cálculo será feito.
     * @param entries A lista de batidas de ponto do período.
     * @return Um objeto MonthCalculationResult com os totais e os detalhes diários.
     */
    MonthCalculationResult calculateMonthlyBalance(Employee employee, List<TimeEntry> entries);
}