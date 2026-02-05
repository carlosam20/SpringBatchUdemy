package com.example.SpringBatchUdemy.dto;

import java.time.LocalDate;
import java.util.List;

public record InputSensorDataDTO (LocalDate localDate, List<Double> temps
){
}
