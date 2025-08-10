package com.calchoras.util; // ou outro pacote de sua escolha

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends TypeAdapter<LocalTime> {

    // Define um formato padrão para consistência.
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public void write(JsonWriter out, LocalTime value) throws IOException {
        // Como escrever o objeto LocalTime no arquivo JSON
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.format(FORMATTER));
        }
    }

    @Override
    public LocalTime read(JsonReader in) throws IOException {
        // Como ler a string do JSON e transformar de volta em um objeto LocalTime
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            return LocalTime.parse(in.nextString(), FORMATTER);
        }
    }
}