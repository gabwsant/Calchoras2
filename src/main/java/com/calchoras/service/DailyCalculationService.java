package com.calchoras.service;

import com.calchoras.model.DailyCalculationResult;
import com.calchoras.model.Employee;
import com.calchoras.model.TimeEntry;
import java.time.Duration;
import java.time.LocalTime;

/**
 * Um serviço stateless para calcular os resultados de um único dia de trabalho.
 * "Stateless" significa que a classe não armazena nenhuma informação em seus campos.
 * Todos os dados necessários são passados como parâmetros para os métodos.
 */
public class DailyCalculationService implements ICalculationService {

    // Como esta classe "assinou o contrato", ela é OBRIGADA a ter este método.
    // O @Override indica que estamos cumprindo uma promessa da interface.
    @Override
    public DailyCalculationResult calculate(TimeEntry timeEntry, Employee employee) {

        // --- 1. Calcular a Duração Efetivamente Trabalhada ---

        Duration workedHours;

        if (timeEntry.getClockIn() == null) {
            workedHours = Duration.ZERO;
        } else {
            LocalTime clockIn = timeEntry.getClockIn();
            LocalTime lunchIn = timeEntry.getLunchIn();
            LocalTime lunchOut = timeEntry.getLunchOut();
            LocalTime clockOut = timeEntry.getClockOut();

            Duration morningShift = Duration.between(clockIn, lunchIn);
            if (lunchIn.isBefore(clockIn)) {
                morningShift = morningShift.plusDays(1);
            }

            Duration afternoonShift = Duration.between(lunchOut, clockOut);
            if (clockOut.isBefore(lunchOut)) {
                afternoonShift = afternoonShift.plusDays(1);
            }

            workedHours = morningShift.plus(afternoonShift);
        }

        // --- 2. Calcular a Duração Esperada da Jornada ---

        Duration expectedHours;

        LocalTime shiftIn = employee.getShiftIn();
        LocalTime shiftOut = employee.getShiftOut();

        Duration shiftDuration = Duration.between(shiftIn, shiftOut);
        if (shiftOut.isBefore(shiftIn)) {
            shiftDuration = shiftDuration.plusDays(1);
        }

        expectedHours = shiftDuration.minusMinutes(employee.getLunchBreakMinutes());

        if(timeEntry.isDayOff()){
            expectedHours = Duration.ZERO;
        }

        // --- 3. Calcular o Saldo (Horas Extras e Negativas) ---

        Duration balance = workedHours.minus(expectedHours);

        Duration overtimeHours;
        Duration negativeHours;

        if (balance.isPositive()) {
            overtimeHours = balance;
            negativeHours = Duration.ZERO;
        } else {
            overtimeHours = Duration.ZERO;
            negativeHours = balance.abs();
        }

        // --- 4. Retornar o Objeto de Resultado Completo ---

        return new DailyCalculationResult(
                timeEntry.getDate(),
                workedHours,
                expectedHours,
                overtimeHours,
                negativeHours
        );
    }
}