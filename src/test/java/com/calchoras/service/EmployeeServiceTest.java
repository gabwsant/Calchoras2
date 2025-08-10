package com.calchoras.service;

import com.calchoras.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para o EmployeeService")
class EmployeeServiceTest {

    private final String TEST_FILE_PATH = "funcionarios_teste.json";
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        // Apaga o arquivo de teste antigo, se ele existir
        deleteTestFile();
        // Cria uma NOVA instância do serviço para cada teste, forçando-o a começar do zero.
        // Como o arquivo não existe, ele vai criar uma lista em memória vazia.
        employeeService = new EmployeeService(TEST_FILE_PATH);
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
    @DisplayName("Deve adicionar um funcionário, atribuir um ID e aumentar a lista")
    void addEmployee_ShouldAssignIdAndIncreaseListSize() {
        // Arrange (Preparação)
        assertEquals(0, employeeService.getAllEmployees().size(), "A lista deveria começar vazia.");
        Employee newEmployee = new Employee(
                "Empresa Teste",
                "João da Silva",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                60
        );

        // Act (Ação)
        Employee addedEmployee = employeeService.addEmployee(newEmployee);

        // Assert (Verificação)
        assertNotNull(addedEmployee, "O funcionário retornado não deveria ser nulo.");
        assertEquals(1, addedEmployee.getId(), "O ID do primeiro funcionário deveria ser 1.");
        assertEquals("João da Silva", addedEmployee.getName());
        assertEquals(1, employeeService.getAllEmployees().size(), "A lista deveria ter 1 funcionário após a adição.");
    }

    @Test
    @DisplayName("Deve persistir o funcionário no arquivo após adicionar")
    void addEmployee_ShouldPersistDataToFile() {
        // Arrange
        Employee newEmployee = new Employee(
                "Empresa Teste",
                "Maria Oliveira",
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                60
        );
        employeeService.addEmployee(newEmployee);

        // Act
        // Criamos uma NOVA instância do serviço. Isso força o construtor a LER o arquivo que acabamos de salvar.
        EmployeeService newInstanceService = new EmployeeService(TEST_FILE_PATH);
        List<Employee> loadedEmployees = newInstanceService.getAllEmployees();

        // Assert
        assertEquals(1, loadedEmployees.size(), "Deveria ter carregado 1 funcionário do arquivo.");
        assertEquals("Maria Oliveira", loadedEmployees.get(0).getName(), "O nome do funcionário carregado deve ser o correto.");
    }

    @Test
    @DisplayName("Deve atualizar os dados de um funcionário existente")
    void updateEmployee_ShouldModifyExistingEmployee() {
        // Arrange
        Employee originalEmployee = new Employee(
                "Empresa Original",
                "Carlos Pereira",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                60
        );
        Employee addedEmployee = employeeService.addEmployee(originalEmployee);

        // Modificamos os dados do funcionário que foi adicionado
        addedEmployee.setName("Carlos Pereira (Editado)");
        addedEmployee.setShiftOut(LocalTime.of(17, 30));

        // Act
        employeeService.updateEmployee(addedEmployee);
        Optional<Employee> updatedEmployeeOpt = employeeService.getEmployeeById(addedEmployee.getId());

        // Assert
        assertTrue(updatedEmployeeOpt.isPresent(), "O funcionário atualizado deveria ser encontrado.");
        assertEquals("Carlos Pereira (Editado)", updatedEmployeeOpt.get().getName());
        assertEquals(LocalTime.of(17, 30), updatedEmployeeOpt.get().getShiftOut());
    }

    @Test
    @DisplayName("Deve remover um funcionário da lista e do arquivo")
    void deleteEmployee_ShouldRemoveEmployee() {
        // Arrange
        Employee employee1 = employeeService.addEmployee(
                new Employee(
                        "Empresa",
                        "Ana",
                        LocalTime.of(8,0),
                        LocalTime.of(17,0),
                        60
                )
        );
        Employee employee2 = employeeService.addEmployee(
                new Employee(
                        "Empresa",
                        "Beto",
                        LocalTime.of(8,0),
                        LocalTime.of(17,0),
                        60
                )
        );
        assertEquals(2, employeeService.getAllEmployees().size());

        // Act
        employeeService.deleteEmployee(employee1.getId());

        // Assert (na instância atual)
        assertEquals(1, employeeService.getAllEmployees().size(), "A lista deveria ter apenas 1 funcionário.");
        assertFalse(employeeService.getEmployeeById(employee1.getId()).isPresent(), "O funcionário 1 não deveria mais existir.");
        assertTrue(employeeService.getEmployeeById(employee2.getId()).isPresent(), "O funcionário 2 deveria continuar existindo.");

        // Assert (verificando a persistência)
        EmployeeService newInstanceService = new EmployeeService(TEST_FILE_PATH);
        assertEquals(1, newInstanceService.getAllEmployees().size(), "A lista carregada do arquivo também deveria ter apenas 1 funcionário.");
    }
}