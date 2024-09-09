package com.acc.core.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 工序表 work_procedure
 */
@Getter
@Setter
@ToString
public class WorkProcedure implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 物料号
     */
    @ExcelProperty("Material")
    private String materialId;
    /**
     * 物料描述
     */
    @ExcelProperty("Description(CN)")
    private String description;
    /**
     * 工序号
     */
    @ExcelProperty("OpAc")
    private Integer operationId;
    /**
     * 加工中心
     */
    @ExcelProperty("Work center")
    private String workCenter;
    /**
     * 工序描述
     */
    @ExcelProperty("Operation Description")
    private String operationDescription;
    /**
     * 准备工时
     */
    @ExcelProperty("Setup")
    private Double setupTime;
    /**
     * 加工工时
     */
    @ExcelProperty("Machine")
    private Double machineTime;
    /**
     * 人工工时
     */
    @ExcelProperty("Labor")
    private Double laborTime;
}