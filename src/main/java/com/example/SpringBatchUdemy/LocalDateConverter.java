package com.example.SpringBatchUdemy;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter extends AbstractSingleValueConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    @Override
    public boolean canConvert(Class type) {
        return LocalDate.class.equals(type);
    }

    @Override
    public String toString(Object obj) {
        return ((LocalDate) obj).format(FORMATTER);
    }

    @Override
    public Object fromString(String str) {
        return LocalDate.parse(str, FORMATTER);
    }
}