package com.acc.controller.work;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 流程图控制器
 */
@RequestMapping("/flow")
@Controller
public class FlowChartController {

    @GetMapping("/table")
    public String tableIndex() {
        return "table";
    }

    @GetMapping("/allicon")
    public String alliconIndex() {
        return "allicon";
    }

}
