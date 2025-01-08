package com.shahla.ema;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        out.value(value != null ? value.format(formatter) : null);
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        // We have date in two different format one is YYYY-MM-DD and the second is RFC_1123_DATE_TIME
        String date = in.nextString();
        try {
            return LocalDate.parse(date, formatter);
        } catch (Exception e) {
            return LocalDate.parse(date, DateTimeFormatter.RFC_1123_DATE_TIME);
        }
//        return in != null ? LocalDate.parse(in.nextString(), formatter) : null;
    }
}