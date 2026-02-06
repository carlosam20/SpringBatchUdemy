package com.example.SpringBatchUdemy.config;

import com.example.SpringBatchUdemy.MathTempServiceImpl;
import com.example.SpringBatchUdemy.dto.InputSensorDataDTO;
import com.example.SpringBatchUdemy.dto.OutputXMLSensorDataDTO;
import com.example.SpringBatchUdemy.mapper.TemperatureMappingConverter;
import lombok.NonNull;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;



@Component
    public class SensorDataProcessor implements ItemProcessor<InputSensorDataDTO, OutputXMLSensorDataDTO> {
    Log log = LogFactory.getLog(TemperatureMappingConverter.class);
    @Override
        public @NonNull OutputXMLSensorDataDTO process(InputSensorDataDTO isd){
        if(isd.temps() == null|| isd.temps().isEmpty() ||  isd.localDate() == null){
            log.error("InputDTO issue on ItemProcessor {}");
            throw new RuntimeException("InputDTO has empty values");
        }
        MathTempServiceImpl mathTempService = new MathTempServiceImpl();
        return mathTempService.tempOperation(isd);


    }
    }


