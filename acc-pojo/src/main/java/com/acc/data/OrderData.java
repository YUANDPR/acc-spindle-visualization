package com.acc.data;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 订单Excel表
 */
@Getter
@Setter
@EqualsAndHashCode
public class OrderData {

    @ExcelProperty("订单")
    private Integer orderId;

    @ExcelProperty("物料")
    private String materialId;

    @ExcelProperty("物料描述")
    private String description;

    @ExcelProperty("订单数量")
    private Integer orderQuantity;

    @ExcelProperty("基本开始")
    private Date startTime;

    @ExcelProperty("基本完成")
    private Date finishTime;
}
