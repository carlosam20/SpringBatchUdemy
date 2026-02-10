package com.example.SpringBatchUdemy;


import com.example.SpringBatchUdemy.dto.InputSensorDataDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InputSensorDataDTOTest {

    @Test
    @DisplayName("Should correctly initialize and retrieve values from InputSensorDataDTO")
    void shouldStoreAndRetrieveData() {
        // Given
        LocalDate date = LocalDate.of(2026, 2, 10);
        List<Double> temperatures = List.of(22.5, 23.1, 21.8);

        // When
        InputSensorDataDTO dto = new InputSensorDataDTO(date, temperatures);

        // Then
        assertThat(dto.localDate()).isEqualTo(date);
        assertThat(dto.temps())
                .hasSize(3)
                .containsExactly(22.5, 23.1, 21.8);
    }

    @Test
    @DisplayName("Equality check should work correctly for Records")
    void testEquality() {
        LocalDate date = LocalDate.now();
        List<Double> temps = List.of(10.0);

        InputSensorDataDTO dto1 = new InputSensorDataDTO(date, temps);
        InputSensorDataDTO dto2 = new InputSensorDataDTO(date, temps);

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }
}
