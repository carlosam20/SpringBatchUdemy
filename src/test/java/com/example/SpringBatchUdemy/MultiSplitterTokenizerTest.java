package com.example.SpringBatchUdemy;

import com.example.SpringBatchUdemy.config.MultiSplitterTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MultiSplitterTokenizerTest {

    @Mock
    private MultiSplitterTokenizer tokenizer;

    @BeforeEach
    void setUp() {
        tokenizer = new MultiSplitterTokenizer();
    }

    @Test
    @DisplayName("Should tokenize a standard colon and comma separated line")
    void testStandardTokenization() {
        // Arrange
        String input = "01-02-2015:76.63,76.82,77.52,76.66,77.16,76.78,75.80,75.93,75.42,";
        //TODO is repeating data
        List<String> result = tokenizer.doTokenize(input);

        // Assert
        // Expected: ["01-02-2015", "25.5,26.0,24.8"]
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo("01-02-2015");
        assertThat(result.get(1)).isEqualTo("76.63,76.82,77.52,76.66,77.16,76.78,75.80,75.93,75.42");
    }

    @Test
    @DisplayName("Should trim whitespace from tokens")
    void testWhitespaceTrimming() {
        // Arrange
        String input = " 01-02-2015 : 10.0 , 11.5 ";

        // Act
        List<String> result = tokenizer.doTokenize(input);

        // Assert
        assertThat(result.get(0)).isEqualTo("01-02-2015");
        assertThat(result.get(1)).isEqualTo("10.0,11.5");
    }

    @Test
    @DisplayName("Should handle multiple colons as separators")
    void testMultipleColons() {
        // Arrange
        String input = "DeviceA:Part1:Part2";

        // Act
        List<String> result = tokenizer.doTokenize(input);

        // Assert
        assertThat(result.get(0)).isEqualTo("DeviceA");
        assertThat(result.get(1)).isEqualTo("Part1,Part2");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("Should throw exception on empty or blank input")
    void testEmptyInput(String input) {
        // Your current implementation uses tokens.length-1 and substring.
        // If there's no colon/comma, tokens.length will be 1, causing an ArrayOutOfBounds or StringIndexOutOfBounds.
        assertThatThrownBy(() -> tokenizer.doTokenize(input))
                .isInstanceOf(Exception.class);
    }
}