package com.example.SpringBatchUdemy.config;

import lombok.NonNull;
import org.springframework.batch.item.file.transform.AbstractLineTokenizer;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MultiSplitterTokenizer extends AbstractLineTokenizer {

    @Override
    @NonNull
    protected List<String> doTokenize(String line) {
        String[] tokens = line.split("[:,]");
        StringBuilder addComma = new StringBuilder();
        String[] dateTokens = new String[tokens.length-1];
        
        for (int i = 1; i < tokens.length; i++) {
            dateTokens[j] = tokens[i];
        }

        String tokenizedTemp = String.valueOf(addComma.append(String.join("",dateTokens)).append(","));
        int lastComma = tokenizedTemp.lastIndexOf(",");
        tokenizedTemp = tokenizedTemp.substring(0,lastComma);
        tokens[1] = tokenizedTemp;
        return Arrays.stream(tokens)
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
