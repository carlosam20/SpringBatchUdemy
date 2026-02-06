package com.example.SpringBatchUdemy.mapper;

import com.example.SpringBatchUdemy.dto.XMLSensorDataStructure;
import com.example.SpringBatchUdemy.dto.InputSensorDataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TemperatureMappingConverter {

    TemperatureMappingConverter INSTANCE = Mappers.getMapper(TemperatureMappingConverter.class);
    XMLSensorDataStructure ConvertXML(InputSensorDataDTO inputSensorDataDTO);


}
