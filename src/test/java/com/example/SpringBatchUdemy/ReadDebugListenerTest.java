package com.example.SpringBatchUdemy;

import com.example.SpringBatchUdemy.listener.ReadDebugListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ReadDebugListenerTest {

    private ReadDebugListener listener;

    @BeforeEach
    void setUp() {
        listener = new ReadDebugListener();
    }

    @Test
    @DisplayName("beforeRead should log current time without throwing exceptions")
    void testBeforeRead() {
        // Act & Assert
        assertDoesNotThrow(() -> listener.beforeRead());
    }

    @Test
    @DisplayName("afterRead should log the item successfully")
    void testAfterRead() {
        // Arrange
        Object mockItem = "Sensor-Data-Row-123";

        // Act & Assert
        assertDoesNotThrow(() -> listener.afterRead(mockItem));
    }

    @Test
    @DisplayName("onReadError should handle exception gracefully")
    void testOnReadError() {
        // Arrange
        Exception readException = new Exception("File format corrupted");

        // Act & Assert
        // Note: Your listener logs the error but does not throw it.
        assertDoesNotThrow(() -> listener.onReadError(readException));
    }
}