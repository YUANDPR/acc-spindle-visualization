package com.acc.controller.work;

import com.acc.core.annotation.Anonymous;
import com.acc.core.entity.WorkRecord;
import com.acc.service.RecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/records")
@Api(tags = "加工数据记录")
@Anonymous
public class RecordController {

    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    /**
     * 添加新的工作记录。
     * Add a new work record.
     *
     * @param workRecord 工作记录数据。
     *                   The work record data.
     * @return 响应实体。
     * The response entity.
     */
    @Anonymous
    @PostMapping
    @ApiOperation("添加新的工作记录")
    public ResponseEntity<String> addWorkRecord(@RequestBody WorkRecord workRecord) {
        recordService.addWorkRecord(workRecord);
        return ResponseEntity.ok("Record added successfully");
    }

    /**
     * 根据 orderId 查询工作记录。
     * Retrieve work records by orderId.
     *
     * @param orderId 工单ID。
     *                The order ID.
     * @return 工作记录列表。
     * List of work records.
     */
    @Anonymous
    @GetMapping("/order/{orderId}")
    @ApiOperation("根据工单号查询工作记录")
    public ResponseEntity<List<WorkRecord>> getRecordsByOrderId(@PathVariable int orderId) {
        List<WorkRecord> records = recordService.getWorkRecordsByOrderId(orderId);
        return ResponseEntity.ok(records);
    }

    /**
     * 根据操作员查询工作记录。
     * Retrieve work records by operator.
     *
     * @param operator 操作员名。
     *                 The operator name.
     * @return 工作记录列表。
     * List of work records.
     */
    @Anonymous
    @GetMapping("/operator/{operator}")
    @ApiOperation("根据操作员查询工作记录")
    public ResponseEntity<List<WorkRecord>> getRecordsByOperator(@PathVariable String operator) {
        List<WorkRecord> records = recordService.getWorkRecordsByOperator(operator);
        return ResponseEntity.ok(records);
    }

    /**
     * 根据 orderId 删除工作记录。
     * Delete work records by orderId.
     *
     * @param orderId 工单ID。
     *                The order ID.
     * @return 响应实体。
     * The response entity.
     */
    @Anonymous
    @DeleteMapping("/order/{orderId}")
    @ApiOperation("根据工单号删除工作记录")
    public ResponseEntity<String> deleteRecordsByOrderId(@PathVariable int orderId) {
        recordService.deleteWorkRecordsByOrderId(orderId);
        return ResponseEntity.ok("Records deleted successfully");
    }

    /**
     * 根据记录ID删除工作记录。
     * Delete a work record by its ID.
     *
     * @param id 记录ID。
     *           The record ID.
     * @return 响应实体。
     * The response entity.
     */
    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除工作记录")
    public ResponseEntity<String> deleteRecordById(@PathVariable int id) {
        recordService.deleteWorkRecordById(id);
        return ResponseEntity.ok("Record deleted successfully");
    }

    /**
     * 查询所有工作记录。
     * Retrieve all work records.
     *
     * @return 工作记录列表。
     * List of all work records.
     */
    @Anonymous
    @GetMapping
    @ApiOperation("查询所有工作记录")
    public ResponseEntity<List<WorkRecord>> getAllRecords() {
        List<WorkRecord> records = recordService.getAllWorkRecords();
        return ResponseEntity.ok(records);
    }
}
