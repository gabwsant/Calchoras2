package com.calchoras.service.interfaces;

import com.calchoras.model.DailyCalculationResult;
import com.calchoras.model.Employee;
import com.calchoras.model.TimeEntry;

public interface ICalculationService {

    /**
     * Contrato: qualquer classe que implementar esta interface
     * deve fornecer um método 'calculate' com esta assinatura.
     */
    DailyCalculationResult calculate(TimeEntry timeEntry, Employee employee);
}