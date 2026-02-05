package com.example.SpringBatchUdemy.config;

import com.example.SpringBatchUdemy.XMLSensorDataStructure;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;

public class SensorDataProcessor implements ItemProcessor<PulsarProperties.Transaction, XMLSensorDataStructure> {
    @Override
    public XMLSensorDataStructure process(PulsarProperties.Transaction item) throws Exception {

        return null;
    }
}
