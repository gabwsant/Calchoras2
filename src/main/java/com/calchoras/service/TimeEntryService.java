package com.calchoras.service;

import com.calchoras.model.TimeEntry;
import com.calchoras.service.interfaces.ITimeEntryService;
import com.calchoras.util.LocalDateAdapter;
import com.calchoras.util.LocalTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TimeEntryService implements ITimeEntryService {

    private final String filePath;
    private List<TimeEntry> timeEntryList;
    private final Gson gson;

    public TimeEntryService(){
        this("batidas.json");
    }

    public TimeEntryService(String filePath) {
        this.filePath = filePath;
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());

        this.gson = gsonBuilder.setPrettyPrinting().create();
        loadTimeEntriesFromFile();
    }

    @Override
    public List<TimeEntry> getTimeEntriesForEmployee(int employeeId) {
        // Filtra a lista principal para retornar apenas as batidas do funcionário solicitado
        return this.timeEntryList.stream()
                .filter(entry -> entry.getEmployeeId() == employeeId)
                .collect(Collectors.toList());
    }

    @Override
    public void addTimeEntry(TimeEntry timeEntry) {
        int nextId = timeEntryList.stream()
                .mapToInt(TimeEntry::getId)
                .max()
                .orElse(0) + 1;
        timeEntry.setId(nextId);

        this.timeEntryList.add(timeEntry);
        saveTimeEntriesToFile();
    }

    @Override
    public void deleteEntriesForEmployee(int employeeId) {
        // Remove da lista todas as batidas que pertencem ao funcionário especificado
        boolean removed = this.timeEntryList.removeIf(entry -> entry.getEmployeeId() == employeeId);
        if (removed) {
            saveTimeEntriesToFile(); // Salva apenas se algo foi realmente removido
        }
    }

    // --- Métodos Privados de Persistência ---

    private void loadTimeEntriesFromFile() {
        try (Reader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<ArrayList<TimeEntry>>() {}.getType();
            this.timeEntryList = gson.fromJson(reader, listType);
            if (this.timeEntryList == null) {
                this.timeEntryList = new ArrayList<>();
            }
        } catch (IOException e) {
            this.timeEntryList = new ArrayList<>();
            System.out.println("Arquivo de batidas não encontrado. Uma nova lista será criada.");
        }
    }

    private void saveTimeEntriesToFile() {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(this.timeEntryList, writer);
        } catch (IOException e) {
            System.err.println("Falha ao salvar o arquivo de batidas.");
            e.printStackTrace();
        }
    }
}