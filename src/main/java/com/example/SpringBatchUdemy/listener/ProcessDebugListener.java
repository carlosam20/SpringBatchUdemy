package com.example.SpringBatchUdemy.listener;

import com.example.SpringBatchUdemy.dto.InputSensorDataDTO;
import com.example.SpringBatchUdemy.dto.XMLSensorDataStructure;
import lombok.NonNull;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.ItemProcessListener;

public class ProcessDebugListener implements ItemProcessListener<InputSensorDataDTO, XMLSensorDataStructure> {
    Log log = LogFactory.getLog(ProcessDebugListener.class);


    @Override
    public void beforeProcess(@NonNull InputSensorDataDTO inputSensorDataDTO){
        log.info("Processing Sensor chunk: {} "+inputSensorDataDTO.localDate());
    }

    @Override
    public void afterProcess(@NonNull InputSensorDataDTO inputSensorDataDTO,XMLSensorDataStructure xmlSensorDataStructure){
        log.info("Processed Sensor chunk: {}"+inputSensorDataDTO.localDate());
        assert xmlSensorDataStructure != null;
    }

    @Override
    public void onProcessError(@NonNull InputSensorDataDTO inputSensorDataDTO, @NonNull Exception e){
        throw new RuntimeException("Exception processing: {}"+inputSensorDataDTO+" : ",e);
    }

}

