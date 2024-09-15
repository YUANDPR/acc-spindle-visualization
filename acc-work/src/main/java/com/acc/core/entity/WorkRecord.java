package com.acc.core.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class WorkRecord {

    private static final AtomicInteger counter = new AtomicInteger(0);
    private final int id;
    private int orderId;
    private int procedureId;
    private String operator;
    private LocalDateTime time;
    private boolean startOrFinish;

    public WorkRecord(int orderId, int procedureId, String operator, LocalDateTime time, boolean startOrFinish) {
        this.id = generateUniqueId(); // 生成唯一ID
        this.orderId = orderId;
        this.procedureId = procedureId;
        this.operator = operator;
        this.time = time;
        this.startOrFinish = startOrFinish;
    }


    private static int generateUniqueId() {
        return (int) (System.currentTimeMillis() / 1000L) % Integer.MAX_VALUE + counter.getAndIncrement();
    }

}
