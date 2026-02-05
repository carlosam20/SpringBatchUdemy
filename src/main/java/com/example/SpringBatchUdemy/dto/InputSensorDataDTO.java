package com.example.SpringBatchUdemy.dto;

import java.time.LocalDate;

public record InputSensorDataDTO (
        LocalDate date, double minTemp, double avgTemp, double maxTemp
){
}
