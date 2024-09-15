package com.acc.controller.system.tool;

import com.acc.controller.system.BaseController;
import com.acc.core.dto.ExecutingOrderDto;
import com.acc.mapper.ExecutingOrderMapper;
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


    @GetMapping("/carousel")
    @ResponseBody
    public List<ExecutingOrderDto> getAllWorkOrders() {
        return executingOrderMapper.findAllExecutingOrders();
    }

    @GetMapping("/screen")
    public String getScreen() {
        return "/screen";
    }

}
