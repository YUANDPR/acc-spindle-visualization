package com.acc.controller.system.tool;

import com.acc.controller.system.BaseController;
import com.acc.core.dto.ExecutingOrderDto;
import com.acc.core.entity.WorkOrder;
import com.acc.mapper.ExecutingOrderMapper;
import com.acc.mapper.WorkOrderMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * echarts仪表盘控制器
 */
@Controller
@RequestMapping("/echarts")
@Api(tags = "echarts仪表盘")
public class EChartsController extends BaseController {

    @Autowired
    private ExecutingOrderMapper executingOrderMapper;

    @Autowired
    private WorkOrderMapper workOrderMapper;


    @GetMapping("/executing")
    @ResponseBody
    public List<ExecutingOrderDto> getAllWorkOrders() {
        return executingOrderMapper.findAllExecutingOrders();
    }

    @GetMapping("/order")
    @ResponseBody
    public List<WorkOrder> getNewWorkOrders() {
        return workOrderMapper.selectNewWorkOrderList();
    }

    @GetMapping("/number")
    @ResponseBody
    public int[] getWorkOrderNumber() {
        int[] numbers = new int[2];
        numbers[0] = workOrderMapper.selectWorkOrderNumber();
        numbers[1] = workOrderMapper.selectFinishWorkOrderNumber();
        return numbers;
    }

    @GetMapping("/screen")
    public String getScreen() {
        return "screen";
    }

}
