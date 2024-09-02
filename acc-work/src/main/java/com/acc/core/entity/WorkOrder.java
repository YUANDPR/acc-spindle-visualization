package com.acc.core.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 工单表 work_order
 */
@Getter
@Setter
public class WorkOrder implements Serializable {
    private int id;
    private Integer orderId;
    private String materialId;
    private String description;
    private Integer orderQuantity;
    private Date startTime;
    private Date finishTime;
}
