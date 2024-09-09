package com.acc.controller.system.tool;

import com.acc.controller.system.BaseController;
import com.acc.core.config.AccConfig;
import com.acc.core.config.ServerConfig;
import com.acc.core.entity.WorkOrder;
import com.acc.core.entity.WorkProcedure;
import com.acc.core.exception.file.NonWorkOrderFileException;
import com.acc.core.result.AjaxResult;
import com.acc.core.utils.file.FileUploadUtils;
import com.acc.core.utils.file.FileUtils;
import com.acc.mapper.OrderMapper;
import com.acc.mapper.ProcedureMapper;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.util.ConverterUtils;
import com.alibaba.excel.util.ListUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 工单文件处理接口
 */
@Controller
@RequestMapping("/tool/work")
@Slf4j
@Api(tags = "工单文件处理")
public class OrderFileController extends BaseController {
    private String prefix = "work/order";

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private OrderMapper workOrderMapper;

    @Autowired
    private ProcedureMapper workProcedureMapper;

    @RequiresPermissions("tool:work:view")
    @GetMapping
    @ApiOperation("获取工单文件上传页面")
    public String index() {
        return prefix + "/upload";
    }

    /**
     * 上传工单文件
     */
    @RequiresPermissions("work:order:edit")
    @PostMapping("/order")
    @ResponseBody
    @ApiOperation("上传工单文件")
    public AjaxResult uploadOrder(MultipartFile file) {
        try {
            // 上传文件路径
            String filePath = AccConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            // 文件磁盘路径
            String readPath = filePath + fileName.substring(15);
            // 文件后缀
            String extension = fileName.substring(fileName.lastIndexOf("."));
            // 读取数据并存储到数据库
            readOrderExcel(readPath);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        } catch (Exception e) {
            log.info(e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 上传工序文件
     */
    @RequiresPermissions("work:process:edit")
    @PostMapping("/process")
    @ResponseBody
    @ApiOperation("上传工序文件")
    public AjaxResult uploadProcess(MultipartFile file) {
        try {
            // 上传文件路径
            String filePath = AccConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            // 文件磁盘路径
            String readPath = filePath + fileName.substring(15);
            // 读取数据并存储到数据库
            readProcessExcel(readPath);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        } catch (Exception e) {
            log.info(e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 读取工序并存储工序信息
     */
    private void readProcessExcel(String readPath) {
        EasyExcel.read(readPath, WorkProcedure.class, new AnalysisEventListener<WorkProcedure>() {
            /**
             * 单次缓存的数据量
             */
            public static final int BATCH_COUNT = 100;

            private boolean isOrder = false;
            /**
             *临时存储
             */
            private List<WorkProcedure> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

            /**
             * 读取表头
             */
            @Override
            public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
                Map<Integer, String> integerStringMap = ConverterUtils.convertToStringMap(headMap, context);
                Set<String> values = Set.of("Material", "Description(CN)", "OpAc", "Work center", "Operation Description"
                        , "Setup", "Machine", "Labor");
                // 校验表头是否含有特定的一些字段
                if (!values.stream().allMatch(integerStringMap::containsValue)) {
                    throw new NonWorkOrderFileException("上传文件非工序文件，请重新上传！");
                } else {
                    isOrder = true;
                }
            }

            /**
             * 每读取一条数据执行
             */
            @Override
            public void invoke(WorkProcedure data, AnalysisContext context) {
                if (isOrder) {
                    cachedDataList.add(data);
                    if (cachedDataList.size() >= BATCH_COUNT) {
                        saveData(cachedDataList);
                        // 存储完成清理 list
                        cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                    }
                }
            }

            /**
             * 读取结束后执行
             */
            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                if (isOrder) {
                    saveData(cachedDataList);
                }
            }

            /**
             * 存储数据库
             */
            private void saveData(List<WorkProcedure> workProcedures) {
                workProcedureMapper.insertBatch(workProcedures);
            }
        }).sheet().doRead();
    }

    /**
     * 读取工单并存储工单信息
     */
    private void readOrderExcel(String readPath) {
        EasyExcel.read(readPath, WorkOrder.class, new AnalysisEventListener<WorkOrder>() {
            /**
             * 单次缓存的数据量
             */
            public static final int BATCH_COUNT = 100;

            private boolean isOrder = false;
            /**
             *临时存储
             */
            private List<WorkOrder> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

            /**
             * 读取表头
             */
            @Override
            public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
                Map<Integer, String> integerStringMap = ConverterUtils.convertToStringMap(headMap, context);
                Set<String> values = Set.of("订单", "物料", "物料描述", "基本开始", "基本完成");
                // 校验表头是否含有特定的一些字段
                if (!values.stream().allMatch(integerStringMap::containsValue)) {
                    throw new NonWorkOrderFileException("上传文件非工单，请重新上传！");
                } else {
                    isOrder = true;
                }
            }

            /**
             * 每读取一条数据执行
             */
            @Override
            public void invoke(WorkOrder data, AnalysisContext context) {
                if (isOrder) {
                    cachedDataList.add(data);
                    if (cachedDataList.size() >= BATCH_COUNT) {
                        saveData(cachedDataList);
                        // 存储完成清理 list
                        cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                    }
                }
            }

            /**
             * 读取结束后执行
             */
            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                if (isOrder) {
                    saveData(cachedDataList);
                }
            }

            /**
             * 存储数据库
             */
            private void saveData(List<WorkOrder> workOrders) {
                workOrderMapper.insertBatch(workOrders);
            }
        }).sheet().doRead();
    }

}
