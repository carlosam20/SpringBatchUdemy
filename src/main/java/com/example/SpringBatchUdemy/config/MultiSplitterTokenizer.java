package com.example.SpringBatchUdemy.config;

import org.springframework.batch.item.file.transform.AbstractLineTokenizer;

import java.util.Arrays;
import java.util.List;

public class MultiSplitterTokenizer extends AbstractLineTokenizer {

    @Override
    protected List<String> doTokenize(String line) {
        return Arrays.asList(line.split("[:,]"));
    }
}
