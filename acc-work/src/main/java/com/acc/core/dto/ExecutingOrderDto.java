package com.acc.core.dto;

import com.acc.core.entity.EWResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.acc.core.entity.EWResult.getSimpleValueMap;

@Data
@Slf4j
public class ExecutingOrderDto {
    private static final AtomicInteger counter = new AtomicInteger(0);
    Integer id;
    int orderId;
    int executingProcedureId;
    boolean executing;
    Date updateTime;

    public void generateUniqueId() {
        this.id = (int) ((System.currentTimeMillis() / 1000L) % Integer.MAX_VALUE + counter.getAndIncrement());
    }

    public int getId() {
        if (id == null) {
            generateUniqueId();
            log.info("Generating ExecutingOrderDto unique id for {}", id);
        }
        return id;
    }

    @Override
    public String toString() {
        return "ExecutingOrderDto{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", executingProcedureId=" + executingProcedureId +
                ", executing=" + executing +
                ", updateTime=" + updateTime +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> item = new HashMap<>();

        // 设置其他简单的字段
        item.put("id", getSimpleValueMap(this.getId()));
        item.put("order_id", getSimpleValueMap(this.getOrderId()));
        item.put("executing_procedure_id", getSimpleValueMap(this.getExecutingProcedureId()));

        if (this.isExecuting()){
            item.put("executing", Collections.singletonList("executing"));
        } else {
            item.put("executing", Collections.emptyList());
        }
        item.put("update_time", getSimpleValueMap(this.getCustomUpdateTime()));

        // 将构建好的 Map 对象添加到列表中
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("data", item);
        return ret;
    }

    public ExecutingOrderDto (JSONObject data) {
        int id = data.optInt("id", -1);
        String orderId = data.optString("order_id", null);
        String executingProcedureId = data.optString("executing_procedure_id", null);
        boolean executing = data.optJSONArray("executing").length() == 1;  // 数组
        int update_time = data.optInt("update_time", -1);

        setId(id);
        setOrderId(Integer.parseInt(orderId));
        setExecuting(executing);
        setCustomUpdateTime(update_time);
        setExecutingProcedureId(Integer.parseInt(executingProcedureId));
    }

    public ExecutingOrderDto(EWResult ewResult) {
        setId(ewResult.getId());
        setOrderId(ewResult.getOrderId());
        setExecutingProcedureId(ewResult.getExecutingProcedureId());
        setExecuting(ewResult.getExecuting() != null && !ewResult.getExecuting().isEmpty());
        setCustomUpdateTime(ewResult.getUpdateTimeValue());
    }

    public void setCustomUpdateTime(long updateTime) {
        this.updateTime = new Date(updateTime);
    }

    public Long getCustomUpdateTime() {
        return updateTime.getTime();
    }

    public ExecutingOrderDto() {}
}
