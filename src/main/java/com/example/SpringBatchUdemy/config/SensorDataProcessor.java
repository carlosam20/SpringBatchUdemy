package com.example.SpringBatchUdemy.config;

import com.example.SpringBatchUdemy.MathTempServiceImpl;
import com.example.SpringBatchUdemy.dto.XMLSensorDataStructure;
import com.example.SpringBatchUdemy.dto.InputSensorDataDTO;
import lombok.NonNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;



@Component
    public class SensorDataProcessor implements ItemProcessor<InputSensorDataDTO, XMLSensorDataStructure> {
    @Override
        public @NonNull XMLSensorDataStructure process(InputSensorDataDTO isd){
        if(isd.temps() == null|| isd.temps().isEmpty() ||  isd.localDate() == null){
            throw new RuntimeException("Input DTO has empty values");
        }
        MathTempServiceImpl mathTempService = new MathTempServiceImpl();
        return mathTempService.tempOperation(isd);


    }
}


