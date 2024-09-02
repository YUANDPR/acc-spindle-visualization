package com.acc.core.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.util.Pair;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;

/**
 * 如果一个工序正在执行，则waiting和done都不含此工序<br>
 * 如果没有工序执行（或者处于等待），executing=null
 */
@Getter
@Setter
public class ExecutingOrder {
    private static boolean DEBUG = true;
    int id;
    WorkOrder order;
    Queue<WorkProcedure> waiting;
    WorkProcedure executing;
    List<WorkProcedure> done;

    LocalDateTime stateUpdateTime;

    public ExecutingOrder(WorkOrder order) {
        this.order = order;
    }

    public Pair<Integer, Boolean> update() {
        Integer operationId;
        boolean isStart = executing == null;
        stateUpdateTime = LocalDateTime.now();
        if (executing != null) {
            done.add(executing);
            operationId = executing.getOperationId();
            executing = null;
        } else {
            executing = waiting.poll();
            if (executing == null) {
                operationId = done.get(done.size() - 1).getId();
            } else operationId = executing.getOperationId();
        }

        if (DEBUG) {
            if (executing == null) {
                System.out.println("update: finish " + operationId);
            } else {
                System.out.println("update: start " + operationId);
            }
            System.out.println("waiting: " + waiting.size());
            System.out.println("done: " + done.size());
            System.out.println("current: " + (executing == null ? "null" : executing.getOperationId()));
        }

        return new Pair<>(operationId, isStart);
    }
}
