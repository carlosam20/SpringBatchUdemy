package com.example.SpringBatchUdemy.config;

import com.example.SpringBatchUdemy.MathTempServiceImpl;
import com.example.SpringBatchUdemy.dto.InputSensorDataDTO;
import com.example.SpringBatchUdemy.dto.OutputXMLSensorDataDTO;
import org.springframework.batch.item.ItemProcessor;



    public class SensorDataProcessor implements ItemProcessor<InputSensorDataDTO, OutputXMLSensorDataDTO> {
        public OutputXMLSensorDataDTO process(InputSensorDataDTO isd) throws Exception {
            MathTempServiceImpl mathTempService = new MathTempServiceImpl();
            return mathTempService.tempOperation(isd);
        }
    }


