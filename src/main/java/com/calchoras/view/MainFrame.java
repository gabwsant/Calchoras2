package com.calchoras.view;

import com.calchoras.model.Employee;
import com.calchoras.model.TimeEntry;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class MainFrame extends JFrame {
    // Funcionários
    private DefaultListModel<String> employeeListModel;
    private JList<String> employeeList;
    private JButton addEmployeeButton;
    private JButton removeEmployeeButton;
    // Campos de informação do funcionário
    private JTextField companyField;
    private JTextField shiftInField;
    private JTextField shiftOutField;
    private JTextField lunchBreakMinutesField;
    // Campos de batida de ponto
    private JFormattedTextField dateField;
    private JTextField clockInField;
    private JTextField lunchInField;
    private JTextField lunchOutField;
    private JTextField clockOutField;
    // Botões de ação
    private JButton nextEntryButton;
    private JButton previousEntryButton;
    private JButton addTimeEntryButton;
    private JButton removeTimeEntryButton;
    private JButton resetCalculationButton;
    private JButton calculateButton;
    private JButton printReportButton;
    // Área de resultado
    private JTextArea resultArea;

    public MainFrame() {
        super("Calchoras - Cálculadora de Horas Extras");

        initFrame();
        initComponents();
        layoutComponents();
    }

    private void initFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(475, 400);
        setMinimumSize(new Dimension(475, 400));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        employeeListModel = new DefaultListModel<>();
        employeeList = new JList<>(employeeListModel);
        addEmployeeButton = new JButton("Adicionar");
        removeEmployeeButton = new JButton("Remover");
        companyField = new JTextField();
        shiftInField = new JTextField();
        shiftOutField = new JTextField();
        lunchBreakMinutesField = new JTextField();
        dateField = new JFormattedTextField();
        clockInField = new JTextField();
        lunchInField = new JTextField();
        lunchOutField = new JTextField();
        clockOutField = new JTextField();
        nextEntryButton = new JButton("Próximo");
        previousEntryButton = new JButton("Anterior");
        addTimeEntryButton = new JButton("Adicionar");
        removeTimeEntryButton = new JButton("Remover");
        resetCalculationButton = new JButton("Reiniciar");
        calculateButton = new JButton("Calcular");
        printReportButton = new JButton("Gerar Relatório");
        resultArea = new JTextArea();
    }

    private void layoutComponents() {
        // Define o layout principal da janela como BorderLayout, com 10 pixels de espaço entre as áreas.
        this.setLayout(new BorderLayout(10, 10));
        // Adiciona uma borda interna para dar um respiro aos componentes.
        ((JPanel) this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- PAINEL DA ESQUERDA (GERENCIAMENTO DE FUNCIONÁRIOS) ---
        JPanel employeePanel = new JPanel(new BorderLayout(5, 5));
        employeePanel.setBorder(BorderFactory.createTitledBorder("Funcionários"));

        // Painel para os botões de adicionar/remover funcionários
        JPanel employeeButtonsPanel = new JPanel(new GridLayout(1, 2, 5, 5)); // 1 linha, 2 colunas
        employeeButtonsPanel.add(addEmployeeButton);
        employeeButtonsPanel.add(removeEmployeeButton);

        // Adiciona a lista (com barra de rolagem) e os botões ao painel de funcionários
        employeePanel.add(new JScrollPane(employeeList), BorderLayout.CENTER);
        employeePanel.add(employeeButtonsPanel, BorderLayout.SOUTH);


        // --- PAINEL CENTRAL (DADOS E AÇÕES) ---
        JPanel centerPanel = new JPanel();
        // BoxLayout empilha os componentes verticalmente.
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Sub-painel com as informações do funcionário selecionado
        JPanel employeeInfoPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        employeeInfoPanel.setBorder(BorderFactory.createTitledBorder("Informações do Funcionário"));
        employeeInfoPanel.add(new JLabel("Empresa:"));
        employeeInfoPanel.add(companyField);
        employeeInfoPanel.add(new JLabel("Início Jornada:"));
        employeeInfoPanel.add(shiftInField);
        employeeInfoPanel.add(new JLabel("Fim Jornada:"));
        employeeInfoPanel.add(shiftOutField);
        employeeInfoPanel.add(new JLabel("Almoço (min):"));
        employeeInfoPanel.add(lunchBreakMinutesField);

        // Sub-painel com as batidas de ponto
        JPanel timeEntryPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        timeEntryPanel.setBorder(BorderFactory.createTitledBorder("Registro de Ponto"));
        timeEntryPanel.add(new JLabel("Data:"));
        timeEntryPanel.add(dateField);
        timeEntryPanel.add(new JLabel("Entrada:"));
        timeEntryPanel.add(clockInField);
        timeEntryPanel.add(new JLabel("Saída Almoço:"));
        timeEntryPanel.add(lunchInField);
        timeEntryPanel.add(new JLabel("Volta Almoço:"));
        timeEntryPanel.add(lunchOutField);
        timeEntryPanel.add(new JLabel("Saída:"));
        timeEntryPanel.add(clockOutField);

        // Sub-painel com botões de navegação e ações de batida
        JPanel entryActionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        entryActionsPanel.add(previousEntryButton);
        entryActionsPanel.add(addTimeEntryButton);
        entryActionsPanel.add(removeTimeEntryButton);
        entryActionsPanel.add(nextEntryButton);

        // Adiciona os sub-painéis ao painel central
        centerPanel.add(employeeInfoPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento vertical
        centerPanel.add(timeEntryPanel);
        centerPanel.add(entryActionsPanel);

        // --- PAINEL INFERIOR (AÇÕES PRINCIPAIS E RESULTADO) ---
        JPanel southPanel = new JPanel(new BorderLayout(5, 5));

        // Painel com os botões de cálculo
        JPanel mainActionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainActionsPanel.add(resetCalculationButton);
        mainActionsPanel.add(calculateButton);
        mainActionsPanel.add(printReportButton);

        // Área de resultado com barra de rolagem
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder("Resultados / Relatório"));
        resultScrollPane.setPreferredSize(new Dimension(100, 120)); // Define uma altura preferida

        southPanel.add(mainActionsPanel, BorderLayout.NORTH);
        southPanel.add(resultScrollPane, BorderLayout.CENTER);

        // --- MONTAGEM FINAL ---
        // Adiciona os painéis principais ao JFrame
        this.add(employeePanel, BorderLayout.WEST);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);

        // Ajusta o tamanho da janela para caber os componentes de forma organizada
        this.pack();
        // Redefine o tamanho mínimo para o tamanho calculado pelo pack()
        this.setMinimumSize(this.getSize());
    }

    public void updateEmployeeList(List<String> employeeNames) {
        // Limpa a lista de qualquer nome que estivesse lá antes
        employeeListModel.clear();
        // Adiciona todos os novos nomes ao modelo. A JList se atualiza sozinha.
        employeeNames.forEach(employeeListModel::addElement);
    }

    public void displayEmployeeInfo(Employee employee) {
        // Usamos o padrão do Java.time para formatar LocalTime para String "HH:mm"
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        companyField.setText(employee.getCompany());
        shiftInField.setText(employee.getShiftIn().format(timeFormatter));
        shiftOutField.setText(employee.getShiftOut().format(timeFormatter));
        lunchBreakMinutesField.setText(String.valueOf(employee.getLunchBreakMinutes()));
    }

    public void displayTimeEntry(TimeEntry timeEntry) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dateField.setValue(timeEntry.getEntryDate().format(dateFormatter));
        clockInField.setText(timeEntry.getClockIn().format(timeFormatter));
        lunchOutField.setText(timeEntry.getLunchOut().format(timeFormatter));
        lunchInField.setText(timeEntry.getLunchIn().format(timeFormatter));
        clockOutField.setText(timeEntry.getClockOut().format(timeFormatter));
    }

    public void clearTimeEntryFields() {
        // Deixa o campo de data com a data atual, pronto para um novo registro
        dateField.setValue(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        clockInField.setText("");
        lunchOutField.setText("");
        lunchInField.setText("");
        clockOutField.setText("");
    }

    public void clearEmployeeInfoFields() {
        companyField.setText("");
        shiftInField.setText("");
        shiftOutField.setText("");
        lunchBreakMinutesField.setText("");
    }
}
