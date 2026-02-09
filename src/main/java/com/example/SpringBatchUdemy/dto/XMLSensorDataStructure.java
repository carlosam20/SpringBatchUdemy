package com.example.SpringBatchUdemy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.time.LocalDateConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class XMLSensorDataStructure {
    @XStreamConverter(LocalDateConverter.class)
    private LocalDate date;
    private double min;
    private double avg;
    private double max;
}
