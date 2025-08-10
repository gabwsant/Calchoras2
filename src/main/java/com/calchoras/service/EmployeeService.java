package com.calchoras.service;

import com.calchoras.model.Employee;
import com.calchoras.service.interfaces.IEmployeeService;
import com.calchoras.util.LocalDateAdapter;
import com.calchoras.util.LocalTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeService implements IEmployeeService {

    private final String filePath;
    private List<Employee> employeeList;
    private final Gson gson;

    /**
     * Construtor principal que usa o caminho padrão do arquivo.
     */
    public EmployeeService() {
        this("funcionarios.json"); // Chama o outro construtor com o nome padrão
    }

    /**
     * Construtor para testes, permitindo especificar um arquivo diferente.
     * @param filePath O caminho para o arquivo JSON a ser usado.
     */
    public EmployeeService(String filePath) {
        this.filePath = filePath;
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());

        this.gson = gsonBuilder.setPrettyPrinting().create();
        loadEmployeesFromFile();
    }

    @Override
    public List<Employee> getAllEmployees() {
        // Retorna uma cópia da lista para garantir que a lista interna do serviço
        // não seja modificada por outras partes do código (princípio do encapsulamento).
        return new ArrayList<>(this.employeeList);
    }

    @Override
    public Optional<Employee> getEmployeeById(int employeeId) {
        return employeeList.stream()
                .filter(e -> e.getId() == employeeId)
                .findFirst();
    }

    @Override
    public Employee addEmployee(Employee employee) {
        // Gera um ID novo e seguro, pegando o maior ID existente e somando 1.
        int nextId = employeeList.stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0) + 1;

        employee.setId(nextId);

        employeeList.add(employee);
        saveEmployeesToFile(); // Salva a lista inteira no arquivo após a adição.
        return employee;
    }

    @Override
    public void updateEmployee(Employee updatedEmployee) {
        // Procura pelo funcionário com o mesmo ID e o substitui na lista.
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getId() == updatedEmployee.getId()) {
                employeeList.set(i, updatedEmployee);
                break; // Para a busca assim que encontrar e substituir
            }
        }
        saveEmployeesToFile(); // Salva as alterações no arquivo.
    }

    @Override
    public void deleteEmployee(int employeeId) {
        // Remove da lista o funcionário que corresponde ao ID fornecido.
        employeeList.removeIf(employee -> employee.getId() == employeeId);
        saveEmployeesToFile(); // Salva as alterações no arquivo.
    }

    // --- Métodos Privados de Persistência ---

    private void loadEmployeesFromFile() {
        File file = new File(filePath);

        // 1. Verificamos primeiro se o arquivo não existe OU se ele existe mas está vazio.
        // Em qualquer um desses casos, simplesmente começamos com uma lista nova.
        if (!file.exists() || file.length() == 0) {
            this.employeeList = new ArrayList<>();
            System.out.println("Arquivo de funcionários não encontrado ou vazio. Iniciando com lista nova.");
            return;
        }

        // 2. Se o arquivo existe e tem conteúdo, aí sim tentamos processá-lo.
        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Employee>>() {}.getType();
            this.employeeList = gson.fromJson(reader, listType);

            // Segurança extra: se o JSON for inválido (ex: "null"), inicializa uma lista vazia.
            if (this.employeeList == null) {
                this.employeeList = new ArrayList<>();
            }
        } catch (IOException | JsonSyntaxException e) { // Agora captura erros de sintaxe também
            // Se o arquivo estiver corrompido, é mais seguro começar com uma lista vazia
            // do que travar a aplicação.
            this.employeeList = new ArrayList<>();
            System.err.println("ERRO: Falha ao ler ou interpretar o arquivo de funcionários. Iniciando com lista vazia para segurança.");
            e.printStackTrace();
        }
    }

    private void saveEmployeesToFile() {
        try (Writer writer = new FileWriter(filePath)) {
            // Converte a lista inteira de funcionários para formato JSON e escreve no arquivo.
            gson.toJson(this.employeeList, writer);
        } catch (IOException e) {
            System.err.println("ERRO: Falha ao salvar o arquivo de funcionários.");
            e.printStackTrace();
            // Em uma aplicação real, você poderia exibir uma mensagem de erro na UI.
        }
    }
}