package com.example.SpringBatchUdemy.mapper;

import com.example.SpringBatchUdemy.dto.OutputXMLSensorDataDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;

@Mapper(componentModel = "spring")
public interface TemperatureMappingConverter {


    Log log = LogFactory.getLog(TemperatureMappingConverter.class);
    TemperatureMappingConverter INSTANCE = Mappers.getMapper(TemperatureMappingConverter.class);


    @Mapping(target = "min", source = "minTemp", qualifiedByName = "FaToc")
    OutputXMLSensorDataDTO ConvertToXML(PulsarProperties.Transaction tr);


    @Named("FaToC")
    default double conversionToCelsius(double temp){
        int subtract = -32;
        double multiplier = 5;
        double divider = 9;
        return  multiplier/divider * (temp - subtract);
    }



}
