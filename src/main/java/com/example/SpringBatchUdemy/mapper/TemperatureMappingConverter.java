package com.example.SpringBatchUdemy.mapper;

import com.example.SpringBatchUdemy.XMLSensorDataStructure;
import com.example.SpringBatchUdemy.dto.OutputXMLSensorDataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TemperatureMappingConverter {

    TemperatureMappingConverter INSTANCE = Mappers.getMapper(TemperatureMappingConverter.class);
    XMLSensorDataStructure ConvertXML(OutputXMLSensorDataDTO outputXMLSensorDataDTO);


}
