package com.acc.entity.work;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 工序表 work_procedure
 */
@Getter
@Setter
public class WorkProcedure implements Serializable {
    private int id;
    private String materialId;
    private String description;
    private Integer operationId;
    private String workCenter;
    private String operationDescription;
    private Integer setupTime;
    private Integer machineTime;
    private Integer laborTime;

}
