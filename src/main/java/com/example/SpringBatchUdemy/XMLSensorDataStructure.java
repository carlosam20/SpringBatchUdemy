package com.example.SpringBatchUdemy;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.AnnotateWith;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "data")
public class XMLSensorDataStructure{
    @XmlElementWrapper(name = "daily-data")
    @XmlElement(name ="date")
    LocalDate date;
    @XmlElement(name ="minTemp")
    double minTemp;
    @XmlElement(name ="avgTemp")
    double avgTemp;
    @XmlElement(name ="maxTemp")
    double maxTemp;
}
