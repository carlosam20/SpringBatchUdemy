package com.example.SpringBatchUdemy.config;

import lombok.NonNull;
import org.springframework.batch.item.file.transform.AbstractLineTokenizer;
import java.util.*;
import java.util.stream.Collectors;

public class MultiSplitterTokenizer extends AbstractLineTokenizer {

    @Override
    @NonNull
    public List<String> doTokenize(String line) {
        List<String> tokensResult = new ArrayList<>();

        String[] tokens = line.split("[:,]");
        StringBuilder addComma = new StringBuilder();

        //Obtain temps only
        String[] tempTokens = new String[tokens.length];

        int j=0;
        for (int i = 1; i < tokens.length; i++) {
                tempTokens[j] = tokens[i]+",";
                j++;
        }


        StringBuilder joinTemps = new StringBuilder();
        String tokenizedTemp = String.valueOf(joinTemps.append(String.join("", tempTokens)));
        //Clean last comma of the result
        int lastComma = tokenizedTemp.lastIndexOf(",");
        tokenizedTemp = (String) tokenizedTemp.subSequence(0,lastComma);

        tokensResult.add(tokens[0]);
        tokensResult.add(tokenizedTemp);

        return tokensResult
                .stream().map(String::trim)
                .collect(Collectors.toList());
    }
}
