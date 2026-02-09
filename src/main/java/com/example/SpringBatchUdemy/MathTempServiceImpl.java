package com.example.SpringBatchUdemy;

import com.example.SpringBatchUdemy.dto.InputSensorDataDTO;
import com.example.SpringBatchUdemy.dto.XMLSensorDataStructure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Collections;

public class MathTempServiceImpl {

    Log log = LogFactory.getLog(MathTempServiceImpl.class);

    public XMLSensorDataStructure tempOperation(InputSensorDataDTO inputSensorDataDTO){

        if (inputSensorDataDTO.temps() == null || inputSensorDataDTO.temps().isEmpty()) {
            throw new IllegalArgumentException("Sensor data contains no temperatures.");
        }

            double min = conversionToCelsius(Collections.min(inputSensorDataDTO.temps()));
            log.info("Min temp done");
            double max = conversionToCelsius(Collections.max(inputSensorDataDTO.temps()));
            log.info("Max temp done");
            double avg = avgOperation(inputSensorDataDTO);
            log.info("Temperature processing successful for date: {}");

            return new XMLSensorDataStructure(inputSensorDataDTO.localDate() ,min,avg,max);

    }

    double avgOperation(InputSensorDataDTO inputSensorDataDTO){

        if (inputSensorDataDTO.temps() == null || inputSensorDataDTO.temps().isEmpty()) {
            ArithmeticException error = new ArithmeticException("Cannot calculate average: Division by zero");
            log.error("Average Operation failed", error);
            throw error;
        }
            double avg = conversionToCelsius((inputSensorDataDTO.temps().stream()
                    .mapToDouble(Double::doubleValue)
                    .sum()) / inputSensorDataDTO.temps().size());
            log.info("Avg temp done");

            return avg;

    }

    double conversionToCelsius(double tempFahrenheit){
        return (tempFahrenheit - 32) * 5 / 9;
    }


}
