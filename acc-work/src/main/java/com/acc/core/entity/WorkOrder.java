package com.acc.core.entity;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 工单表 work_order
 */
@Getter
@Setter
@ToString
public class WorkOrder implements Serializable {

    private int id;

    @ExcelProperty("订单")
    private Integer orderId;

    @ExcelProperty("物料")
    private String materialId;

    @ExcelProperty("物料描述")
    private String description;

    @ExcelProperty("订单数量")
    private Integer orderQuantity;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty("基本开始")
    @DateTimeFormat("yyyy-MM-dd")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty("基本完成")
    @DateTimeFormat("yyyy-MM-dd")
    private Date finishTime;
}
