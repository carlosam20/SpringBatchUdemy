package com.example.SpringBatchUdemy.mapper;

import com.example.SpringBatchUdemy.dto.InputSensorDataDTO;
import lombok.NonNull;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class SensorDataFieldSetMapper implements FieldSetMapper<InputSensorDataDTO> {

    @Override
    @NonNull
    public InputSensorDataDTO mapFieldSet(FieldSet fieldSet) {
        String dateString = fieldSet.readString(0);
        LocalDate date = LocalDate.parse(dateString);
        List<Double> temps = new ArrayList<>();
        String [] tempsTokenized = fieldSet.readString(0).split(",");

        for (int i = 1; i < tempsTokenized.length; i++) {
            temps.add(Double.parseDouble(tempsTokenized[i]));
        }
        return new InputSensorDataDTO(date, temps);
    }
}
