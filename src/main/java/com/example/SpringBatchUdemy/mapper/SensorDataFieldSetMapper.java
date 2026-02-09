package com.example.SpringBatchUdemy.mapper;

import com.example.SpringBatchUdemy.dto.InputSensorDataDTO;
import lombok.NonNull;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SensorDataFieldSetMapper implements FieldSetMapper<InputSensorDataDTO> {

    @Override
    @NonNull
    public InputSensorDataDTO mapFieldSet(FieldSet fieldSet) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String fieldDateSensor = fieldSet.readString("date");
        LocalDate resultDate = LocalDate.from(dateFormat.parse(fieldDateSensor));

        List<Double> listTemps = new ArrayList<>();
        String [] tempsTokenized = fieldSet.readString("temps").split(",");

        for (int i = 1; i < tempsTokenized.length; i++) {
            listTemps.add(Double.parseDouble(tempsTokenized[i]));
        }
        return new InputSensorDataDTO(resultDate, listTemps);
    }
}
