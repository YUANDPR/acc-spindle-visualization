package com.acc.service;


import com.acc.core.entity.WorkRecord;

import java.util.List;

public interface RecordService {
    void addWorkRecord(WorkRecord workRecord);

    List<WorkRecord> getWorkRecordsByOrderId(int orderId);

    List<WorkRecord> getWorkRecordsByOperator(String operator);

    void deleteWorkRecordsByOrderId(int orderId);

    void deleteWorkRecordById(int id);

    List<WorkRecord> getAllWorkRecords();
}
