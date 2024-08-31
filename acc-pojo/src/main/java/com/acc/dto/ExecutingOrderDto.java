package com.acc.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class ExecutingOrderDto {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final Logger log = LoggerFactory.getLogger(ExecutingOrderDto.class);
    Integer id;
    int orderId;
    int executingProcedureId;
    boolean executing;
    Long update_time;

    public void generateUniqueId() {
        this.id = (int) ((System.currentTimeMillis() / 1000L) % Integer.MAX_VALUE + counter.getAndIncrement());
    }

    public int getId() {
        if (id == null) {
            generateUniqueId();
            log.info("Generating ExecutingOrderDto unique id for {}", id);
        }
        return id;
    }
}
