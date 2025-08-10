package com.calchoras.service.interfaces;

import com.calchoras.model.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Define o contrato para o serviço que gerencia os funcionários.
 */
public interface IEmployeeService {

    /**
     * Retorna uma lista de todos os funcionários atualmente cadastrados.
     * @return Uma lista de Employees, que pode estar vazia.
     */
    List<Employee> getAllEmployees();

    /**
     * Busca um funcionário específico pelo seu ID único.
     * @param employeeId O ID do funcionário a ser encontrado.
     * @return Um Optional contendo o funcionário se encontrado, ou um Optional vazio caso contrário.
     */
    Optional<Employee> getEmployeeById(int employeeId);

    /**
     * Adiciona um novo funcionário ao sistema e persiste a alteração.
     * O ID do funcionário será gerado automaticamente.
     * @param employee O novo funcionário a ser adicionado (ainda sem ID).
     * @return O funcionário com o ID atribuído.
     */
    Employee addEmployee(Employee employee);

    /**
     * Atualiza os dados de um funcionário existente no sistema e persiste a alteração.
     * @param employee O funcionário com os dados atualizados (deve ter um ID válido).
     */
    void updateEmployee(Employee employee);

    /**
     * Remove um funcionário do sistema, baseado no seu ID.
     * @param employeeId O ID do funcionário a ser removido.
     */
    void deleteEmployee(int employeeId);
}