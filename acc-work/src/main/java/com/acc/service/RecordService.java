package com.acc.service;


import com.acc.core.entity.WorkRecord;

import java.util.List;

/**
 * 定义了一个用于管理工单记录的接口，提供添加、查询和删除工单记录的功能
 */
public interface RecordService {
    /**
     * 添加一条工单记录
     *
     * @param workRecord 要添加的工单记录对象
     */
    void addWorkRecord(WorkRecord workRecord);

    /**
     * 根据工单ID获取工单记录列表
     *
     * @param orderId 工单ID
     * @return 与该工单相关的工单记录列表
     */
    List<WorkRecord> getWorkRecordsByOrderId(int orderId);

    /**
     * 根据操作员姓名获取工单记录列表
     *
     * @param operator 操作员姓名
     * @return 由指定操作员处理的工单记录列表
     */
    List<WorkRecord> getWorkRecordsByOperator(String operator);

    /**
     * 根据工单ID删除工单记录
     *
     * @param orderId 工单ID
     */
    void deleteWorkRecordsByOrderId(int orderId);

    /**
     * 根据工单记录ID删除单个工单记录
     *
     * @param id 工单记录ID
     */
    void deleteWorkRecordById(int id);

    /**
     * 获取所有工单记录
     *
     * @return 包含所有工单记录的列表
     */
    List<WorkRecord> getAllWorkRecords();
}

