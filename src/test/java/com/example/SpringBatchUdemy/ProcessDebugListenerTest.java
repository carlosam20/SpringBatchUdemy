package com.example.SpringBatchUdemy;


import com.example.SpringBatchUdemy.dto.InputSensorDataDTO;
import com.example.SpringBatchUdemy.dto.XMLSensorDataStructure;
import com.example.SpringBatchUdemy.listener.ProcessDebugListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessDebugListenerTest {

    private ProcessDebugListener listener;

    @Mock
    private InputSensorDataDTO inputDto;

    @Mock
    private XMLSensorDataStructure outputDto;

    @BeforeEach
    void setUp() {
        listener = new ProcessDebugListener();
    }

    @Test
    @DisplayName("beforeProcess should execute without errors")
    void testBeforeProcess() {
        // Arrange
        when(inputDto.localDate()).thenReturn(LocalDate.now());

        // Act & Assert
        assertDoesNotThrow(() -> listener.beforeProcess(inputDto));
    }

    @Test
    @DisplayName("afterProcess should execute when output is not null")
    void testAfterProcessSuccess() {
        // Arrange
        when(inputDto.localDate()).thenReturn(LocalDate.now());

        // Act & Assert
        assertDoesNotThrow(() -> listener.afterProcess(inputDto, outputDto));
    }

    @Test
    @DisplayName("onProcessError should wrap exception in RuntimeException")
    void testOnProcessError() {
        // Arrange
        Exception originalException = new Exception("Original error");

        // Act & Assert
        assertThatThrownBy(() -> listener.onProcessError(inputDto, originalException))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Exception processing")
                .hasCause(originalException);
    }
}