package com.acc.core.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@Slf4j
public class ExecutingOrderDto {
    private static final AtomicInteger counter = new AtomicInteger(0);
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
