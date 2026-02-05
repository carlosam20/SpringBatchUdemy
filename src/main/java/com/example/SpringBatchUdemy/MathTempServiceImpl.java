package com.example.SpringBatchUdemy;

import com.example.SpringBatchUdemy.dto.InputSensorDataDTO;
import com.example.SpringBatchUdemy.dto.OutputXMLSensorDataDTO;
import com.example.SpringBatchUdemy.mapper.TemperatureMappingConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Collections;

public class MathTempServiceImpl {

    Log log = LogFactory.getLog(TemperatureMappingConverter.class);

    public OutputXMLSensorDataDTO tempOperation(InputSensorDataDTO inputSensorDataDTO){

        double min =conversionToCelsius(Collections.min(inputSensorDataDTO.temps()));
        log.info("Min temp done");
        double max = conversionToCelsius(Collections.max(inputSensorDataDTO.temps()));
        log.info("Min temp done");
        double avg = conversionToCelsius((inputSensorDataDTO.temps().stream()
                .mapToDouble(Double::doubleValue)
                .sum())/inputSensorDataDTO.temps().size());

        log.info("Avg temp done");

        return new OutputXMLSensorDataDTO(inputSensorDataDTO.localDate() ,min,avg,max);
    }

    double conversionToCelsius(double temp){
        int subtract = -32;
        double multiplier = 5;
        double divider = 9;
        return  multiplier/divider * (temp - subtract);
    }


}
