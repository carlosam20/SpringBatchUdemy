package com.example.SpringBatchUdemy;


import com.example.SpringBatchUdemy.dto.XMLSensorDataStructure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class XMLSensorDataStructureTest {

    @Test
    @DisplayName("Should successfully create DTO using AllArgsConstructor and Getters")
    void testAllArgsConstructorAndGetters() {
        // Arrange
        LocalDate now = LocalDate.now();
        double min = 10.5;
        double avg = 20.0;
        double max = 30.5;

        // Act
        XMLSensorDataStructure dto = new XMLSensorDataStructure(now, min, avg, max);

        // Assert
        assertThat(dto.getDate()).isEqualTo(now);
        assertThat(dto.getMin()).isEqualTo(min);
        assertThat(dto.getAvg()).isEqualTo(avg);
        assertThat(dto.getMax()).isEqualTo(max);
    }

    @Test
    @DisplayName("Should successfully set values using NoArgsConstructor and Setters")
    void testNoArgsConstructorAndSetters() {
        // Arrange
        XMLSensorDataStructure dto = new XMLSensorDataStructure();
        LocalDate specificDate = LocalDate.of(2023, 12, 25);

        // Act
        dto.setDate(specificDate);
        dto.setMin(5.0);
        dto.setAvg(15.0);
        dto.setMax(25.0);

        // Assert
        assertThat(dto.getDate()).isEqualTo(specificDate);
        assertThat(dto.getMin()).isEqualTo(5.0);
        assertThat(dto.getAvg()).isEqualTo(15.0);
        assertThat(dto.getMax()).isEqualTo(25.0);
    }

    @Test
    @DisplayName("Should handle null date and zero values")
    void testDefaultValues() {
        // Act
        XMLSensorDataStructure dto = new XMLSensorDataStructure();

        // Assert
        assertThat(dto.getDate()).isNull();
        assertThat(dto.getMin()).isZero();
        assertThat(dto.getAvg()).isZero();
        assertThat(dto.getMax()).isZero();
    }
}