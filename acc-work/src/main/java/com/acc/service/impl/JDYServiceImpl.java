package com.acc.service.impl;


import com.acc.api.jdy.FormApiClient;
import com.acc.api.jdy.FormDataApiClient;
import com.acc.constants.HttpConstant;
import com.acc.core.dto.ExecutingOrderDto;
import com.acc.core.entity.ExecutingOrder;
import com.acc.core.exception.IllegalOrderCorrespondingQuantityException;
import com.acc.core.exception.OrderNotFoundException;
import com.acc.mapper.ExecutingOrderMapper;
import com.acc.model.form.FormDataQueryParam;
import com.acc.model.form.FormDataUpdateParam;
import com.acc.model.form.FormQueryParam;
import com.acc.service.JDYService;
import com.acc.service.OrderService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JDYServiceImpl implements JDYService {

    private static final String SECRET = "c3LPBsgMxuvfwKmrVEaFFE2n";  // 设置你的密钥
    private static final String APP_ID = "66c98f660039c970b3a1d4f5";
    private static final String ENTRY_ID = "66e016bf16f0a1ea919ced75";

    private final OrderService orderService;
    private final ExecutingOrderMapper executingOrderMapper;
    private static final FormApiClient formApiClient = new FormApiClient(HttpConstant.API_KEY, HttpConstant.HOST);
    private static final FormDataApiClient formDataApiClient = new FormDataApiClient(HttpConstant.API_KEY, HttpConstant.HOST);

    private List<EWResult> currentEWResults;

    public JDYServiceImpl(OrderService orderService, ExecutingOrderMapper executingOrderMapper) {
        this.orderService = orderService;
        this.executingOrderMapper = executingOrderMapper;

        try {
            JDYDataPush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void JDYDataPush() throws Exception {
        formWidgets();
        entryList();
        FormDataQueryParam formDataQueryParam = new FormDataQueryParam(APP_ID, ENTRY_ID);
        Map<String, Object> v5 = formDataApiClient.batchDataQuery(formDataQueryParam, "v5");
        List<EWResult> ewResults = parseEWResults(v5);
        ewResults.forEach(ewResult -> System.out.println(ewResults));
        currentEWResults = ewResults;

//        FormDataUpdateParam formDataUpdateParam = new FormDataUpdateParam(APP_ID, ENTRY_ID,);
    }

    private static void formWidgets() throws Exception {
        Map<String, Object> result = formApiClient.formWidgets(APP_ID, ENTRY_ID, null);
        System.out.println("formWidgets result \n" + result);
    }

    private static void entryList() throws Exception {
        FormQueryParam queryParam = new FormQueryParam();
        queryParam.setSkip(0);
        queryParam.setLimit(0);
        queryParam.setApp_id(APP_ID);
        Map<String, Object> result = formApiClient.entryList(queryParam, null);
        System.out.println("entryList result \n" + result);
    }

    // 签名验证方法
    public boolean validateSignature(String receivedSignature, String nonce, String payload, String timestamp) {
        String calculatedSignature = DigestUtils.sha1Hex(nonce + ":" + payload + ":" + SECRET + ":" + timestamp);
        log.warn("Invalid signature:{}, correct:{}.", calculatedSignature, receivedSignature);
        return calculatedSignature.equals(receivedSignature);
    }

    // 处理更新操作
    public void handleUpdate(String body) throws IllegalOrderCorrespondingQuantityException {
        // 解析请求体并处理
        JSONObject data;
        try {
            JSONObject payloadJSON = new JSONObject(body);
            String op = payloadJSON.getString("op");
            data = payloadJSON.getJSONObject("data");
            log.info("handle Update op:{}", op);
        } catch (JSONException e) {
            log.error(e.getMessage());
            return;
        }

        // 处理数据
        ExecutingOrderDto executingOrderDto = processDataCreate(data);
        try {
            ExecutingOrder executingOrder = orderService.deserializeExecutingOrder(executingOrderDto);
            ExecutingOrderDto byId = executingOrderMapper.getById(executingOrder.getId());

            if (byId != null) {
                executingOrderMapper.updateExecutingOrder(executingOrderDto);
            } else {
                List<ExecutingOrderDto> byOrderId = executingOrderMapper.getByOrderId(executingOrderDto.getOrderId());
                if (byOrderId != null && !byOrderId.isEmpty()) {
                    throw new IllegalOrderCorrespondingQuantityException("发现了该orderId: "+ executingOrder.getOrder().getOrderId() +"对应的多个运行工单！");
                }
                executingOrderMapper.insertExecutingOrder(executingOrderDto);
            }
        } catch (OrderNotFoundException e) {
            log.error("Order not found.", e);
        }

    }

    // 处理数据创建操作
    private ExecutingOrderDto processDataCreate(JSONObject data) {
        // 使用 optString、optInt 等方法来安全提取字段

        // 提取所有字段信息
        String formName = data.optString("formName", null);
        String _id = data.optString("_id", null);
        String appId = data.optString("appId", null);
        String entryId = data.optString("entryId", null);

        // 提取 creator, updater, deleter (json 对象)
        JSONObject creator = data.optJSONObject("creator");
        Result creatorResult = getResult(creator);

        JSONObject updater = data.optJSONObject("updater");
        Result updaterResult = getResult(updater);

        JSONObject deleter = data.optJSONObject("deleter");
        Result deleterResult = getResult(deleter);

        // 提取时间字段 (UTC 时间戳)
        String createTime = data.optString("createTime", null);
        String updateTime = data.optString("updateTime", null);
        String deleteTime = data.optString("deleteTime", null);

        // 提取其他字段
        int id = data.optInt("id", -1);
        String orderId = data.optString("order_id", null);
        String executingProcedureId = data.optString("executing_procedure_id", null);
        boolean executing = data.optJSONArray("executing").length() == 1;  // 数组
        int update_time = data.optInt("update_time", -1);

        // 输出日志
        System.out.println("Processing data_create:");
        System.out.println("Form Name: " + formName);
        System.out.println("App ID: " + appId);
        System.out.println("Entry ID: " + entryId);
        System.out.println("Creator: " + creatorResult.creatorName() + " (" + creatorResult.creatorUsername() + "), Status: " + creatorResult.creatorStatus());
        System.out.println("Updater: " + updaterResult.creatorName() + " (" + updaterResult.creatorUsername() + "), Status: " + updaterResult.creatorStatus());
        System.out.println("Deleter: " + deleterResult.creatorName() + " (" + deleterResult.creatorUsername() + "), Status: " + deleterResult.creatorStatus());
        System.out.println("Create Time: " + createTime);
        System.out.println("Update Time: " + updateTime);
        System.out.println("Delete Time: " + deleteTime);
        System.out.println("ID: " + id);
        System.out.println("Order ID: " + orderId);
        System.out.println("Executing Procedure ID: " + executingProcedureId);
        System.out.println("Executing: " + executing);
        System.out.println("Update Time: " + update_time);

        ExecutingOrderDto executingOrderDto = new ExecutingOrderDto();
        executingOrderDto.setId(id);
        executingOrderDto.setOrderId(Integer.parseInt(orderId));
        executingOrderDto.setExecuting(executing);
        executingOrderDto.setUpdate_time((long) update_time);
        executingOrderDto.setExecutingProcedureId(Integer.parseInt(executingProcedureId));

        return executingOrderDto;
    }

    private static Result getResult(JSONObject creator) {
        String creatorName = creator != null ? creator.optString("name", null) : null;
        String creatorUsername = creator != null ? creator.optString("username", null) : null;
        int creatorStatus = creator != null ? creator.optInt("status", -1) : -1;
        return new Result(creatorName, creatorUsername, creatorStatus);
    }

    private record Result(String creatorName, String creatorUsername, int creatorStatus) {
    }

    @Data
    private class EWResult {
        private User creator;
        private User updater;
        private User deleter;
        private String createTime;
        private String updateTime;
        private String deleteTime;
        private int id;
        private int orderId;
        private int executingProcedureId;
        private List<Object> executing;
        private Long updateTimeValue;
        private String _id;
        private String appId;
        private String entryId;
    }
    @Data
    private static class User {
        private String name;
        private String username;
        private int status;
        private int type;
        private List<Integer> departments;
    }


    private List<EWResult> parseEWResults(Map<String, Object> dataMap){

        // 获取 data 对应的列表
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) dataMap.get("data");

        // 创建结果的列表
        List<EWResult> ewResults = new ArrayList<>();

        // 遍历列表，并手动解析每个对象
        for (Map<String, Object> item : dataList) {
            EWResult result = new EWResult();

            // 处理 creator 对象
            Map<String, Object> creatorMap = (Map<String, Object>) item.get("creator");
            result.setCreator(parseUser(creatorMap));

            // 处理 updater 对象
            Map<String, Object> updaterMap = (Map<String, Object>) item.get("updater");
            result.setUpdater(parseUser(updaterMap));

            // 处理 deleter 对象 (如果有)
            Map<String, Object> deleterMap = (Map<String, Object>) item.get("deleter");
            if (deleterMap != null) {
                result.setDeleter(parseUser(deleterMap));
            }

            // 设置其他简单的字段
            result.setCreateTime((String) item.get("createTime"));
            result.setUpdateTime((String) item.get("updateTime"));
            result.setDeleteTime((String) item.get("deleteTime"));
            System.out.println(item.get("id"));
            result.setId((Integer) item.get("id"));
            result.setOrderId(Integer.parseInt((String) item.get("order_id")));
            result.setExecutingProcedureId(Integer.parseInt((String) item.get("executing_procedure_id")));
            result.setExecuting((List<Object>) item.get("executing"));
            result.setUpdateTimeValue(Long.valueOf((item.get("update_time").toString())));
            result.set_id((String) item.get("_id"));
            result.setAppId((String) item.get("appId"));
            result.setEntryId((String) item.get("entryId"));

            // 将构建好的 EWResult 对象添加到列表中
            ewResults.add(result);
        }
        return ewResults;
    }

    private User parseUser(Map<String, Object> userMap) {
        User user = new User();
        user.setName((String) userMap.get("name"));
        user.setUsername((String) userMap.get("username"));
        user.setStatus((Integer) userMap.get("status"));
        user.setType((Integer) userMap.get("type"));
        user.setDepartments((List<Integer>) userMap.get("departments"));
        return user;
    }
}
