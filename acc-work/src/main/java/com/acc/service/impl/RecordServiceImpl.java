package com.acc.service.impl;

import com.acc.core.entity.WorkRecord;
import com.acc.mapper.RecordMapper;
import com.acc.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    RecordMapper recordMapper;

    @Autowired
    RecordServiceImpl(RecordMapper recordMapper) {
        this.recordMapper = recordMapper;
    }

    /**
     * 添加一个工作记录。
     * Add a work record.
     *
     * @param workRecord 工作记录实体。
     *                   The work record entity.
     */
    @Override
    public void addWorkRecord(WorkRecord workRecord) {
        recordMapper.insertWorkRecord(workRecord);
    }

    /**
     * 根据 orderId 查询工作记录。
     * Retrieve work records by orderId.
     *
     * @param orderId 工单的ID。
     *                The ID of the order.
     * @return 包含所有匹配记录的列表。
     * A list of all matching records.
     */
    @Override
    public List<WorkRecord> getWorkRecordsByOrderId(int orderId) {
        return recordMapper.findWorkRecordsByOrderId(orderId);
    }

    /**
     * 根据操作员查询工作记录。
     * Retrieve work records by operator.
     *
     * @param operator 操作员名称。
     *                 The operator's name.
     * @return 包含所有匹配记录的列表。
     * A list of all matching records.
     */
    @Override
    public List<WorkRecord> getWorkRecordsByOperator(String operator) {
        return recordMapper.findWorkRecordsByOperator(operator);
    }

    /**
     * 根据 orderId 删除工作记录。
     * Delete work records by orderId.
     *
     * @param orderId 工单的ID。
     *                The ID of the order.
     */
    @Override
    public void deleteWorkRecordsByOrderId(int orderId) {
        recordMapper.deleteWorkRecordsByOrderId(orderId);
    }

    /**
     * 根据 ID 删除工作记录。
     * Delete a work record by its ID.
     *
     * @param id 记录的ID。
     *           The ID of the record.
     */
    @Override
    public void deleteWorkRecordById(int id) {
        recordMapper.deleteWorkRecordById(id);
    }

    /**
     * 查询所有工作记录。
     * Retrieve all work records.
     *
     * @return 包含所有工作记录的列表。
     * A list of all work records.
     */
    @Override
    public List<WorkRecord> getAllWorkRecords() {
        return recordMapper.findAllWorkRecords();
    }
}
