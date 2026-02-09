package com.example.SpringBatchUdemy.listener;

import lombok.NonNull;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.ItemReadListener;

import java.time.Instant;


public class ReadDebugListener implements ItemReadListener<Object> {
    Log log = LogFactory.getLog(ReadDebugListener.class);
    @Override
    public void beforeRead() {
        log.info("Reading element:"+ Instant.now());
    }

    @Override
    public void afterRead(@NonNull  Object item) {
        log.info("Successfully read item: "+item);
    }

    @Override
    public void onReadError(@NonNull Exception ex) {
        log.error("Error reading line: {}", ex);
    }
}