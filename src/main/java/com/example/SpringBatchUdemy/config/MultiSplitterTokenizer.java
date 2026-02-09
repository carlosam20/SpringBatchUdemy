package com.example.SpringBatchUdemy.config;

import lombok.NonNull;
import org.springframework.batch.item.file.transform.AbstractLineTokenizer;
import java.util.*;
import java.util.stream.Collectors;

public class MultiSplitterTokenizer extends AbstractLineTokenizer {

    @Override
    @NonNull
    protected List<String> doTokenize(String line) {
        List<String> tokensResult = new ArrayList<>();

        String[] tokens = line.split("[:,]");
        StringBuilder addComma = new StringBuilder();
        //Obtain temps only
        String[] dateTokens = new String[tokens.length-1];

        int j=0;
        for (int i = 1; i < tokens.length; i++) {
            dateTokens[j] = String.valueOf(addComma.append(tokens[i]).append(","));
            j++;
        }

        String tokenizedTemp = String.valueOf(addComma.append(String.join("",dateTokens)));
        //Clean last comma of the result
        int lastComma = tokenizedTemp.lastIndexOf(",");
        tokenizedTemp = tokenizedTemp.substring(0,lastComma);

        tokensResult.add(tokens[0]);
        tokensResult.add(tokenizedTemp);

        return tokensResult
                .stream().map(String::trim)
                .collect(Collectors.toList());
    }
}
