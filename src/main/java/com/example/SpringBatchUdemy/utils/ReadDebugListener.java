package com.example.SpringBatchUdemy.utils;

import com.example.SpringBatchUdemy.mapper.TemperatureMappingConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.ItemReadListener;

public class ReadDebugListener implements ItemReadListener<Object> {
    Log log = LogFactory.getLog(TemperatureMappingConverter.class);
    @Override
    public void beforeRead() {
        log.info("Reading element");
    }

    @Override
    public void afterRead(Object item) {
        log.info((Object) "Successfully read item: {}", (Throwable) item);
    }

    @Override
    public void onReadError(Exception ex) {
        log.error("Error reading line: {}", ex);
    }
}