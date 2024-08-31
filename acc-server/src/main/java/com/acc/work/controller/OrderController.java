package com.acc.work.controller;

import com.acc.dto.ExecutingOrderDto;
import com.acc.entity.work.ExecutingOrder;
import com.acc.entity.work.WorkOrder;
import com.acc.entity.work.WorkProcedure;
import com.acc.exception.IllegalOrderCorrespondingQuantityException;
import com.acc.exception.OrderNotFoundException;
import com.acc.work.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.acc.AccApplication.DEBUG;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> createWorkOrder(@RequestBody WorkOrder workOrder) {
        log.info("Attempting to create a new work order: {}", workOrder);
        try {
            ArrayList<WorkOrder> workOrders = new ArrayList<>();
            workOrders.add(workOrder);
            orderService.insertWorkOrders(workOrders);
            log.info("Work order created successfully");
            return ResponseEntity.ok("Work order created successfully.");
        } catch (Exception e) {
            log.error("Error creating work order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating work order: " + e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getWorkOrder(@PathVariable int orderId) {
        log.info("Fetching work order with orderId: {}", orderId);
        if (DEBUG) {
            System.out.println("Fetching work order with orderId: " + orderId);
        }
        try {
            WorkOrder workOrder = orderService.getWorkOrderByOrderId(orderId);
            if (DEBUG) {
                System.out.println("Work order fetched successfully");
            }
            log.info("Work order fetched successfully");
            return ResponseEntity.ok(workOrder);
        } catch (OrderNotFoundException | IllegalOrderCorrespondingQuantityException e) {
            if (DEBUG) {
                System.out.println("Error fetching work order: " + e.getMessage());
            }
            log.error("Error fetching work order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/procedures/{orderId}")
    public ResponseEntity<?> getWorkProcedures(@PathVariable int orderId) {
        log.info("Fetching work procedures with orderId: {}", orderId);
        if (DEBUG) {
            System.out.println("Fetching work procedures with orderId: " + orderId);
        }
        try {
            List<WorkProcedure> workProcedure = orderService.getWorkProcedure(orderId);
            if (DEBUG) {
                System.out.println("Work procedures fetched successfully");
            }
            log.info("Work procedures fetched successfully");
            return ResponseEntity.ok(workProcedure);
        } catch (OrderNotFoundException | IllegalOrderCorrespondingQuantityException e) {
            if (DEBUG) {
                System.out.println("Error fetching work procedures: " + e.getMessage());
            }
            log.error("Error fetching work procedures: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deleteWorkOrderByOrderId(@PathVariable int orderId) {
        log.info("Attempting to delete work order with orderId: {}", orderId);
        try {
            orderService.deleteWorkOrdersByOrderId(orderId);
            log.info("Work order deleted successfully");
            return ResponseEntity.ok("Work order deleted successfully.");
        } catch (Exception e) {
            log.error("Error deleting work order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting work order: " + e.getMessage());
        }
    }

    @GetMapping("/executing/{orderId}")
    public ResponseEntity<?> createExecutingOrder(@PathVariable int orderId) {
        log.info("Attempting to get executing order for orderId: {}", orderId);
        if (DEBUG) {
            System.out.println("Attempting to get executing order for orderId: " + orderId);
        }
        try {
            ExecutingOrder executingOrder = orderService.getExecutingOrderByOrderId(orderService.getUniqueWorkOrderId(orderId));
            ExecutingOrderDto orderDto = orderService.serializeExecutingOrder(executingOrder);
            log.info("Executing order get successfully");
            if (DEBUG) {
                System.out.println("Executing order get successfully: " + orderDto);
            }
            return ResponseEntity.ok(orderDto);
        } catch (OrderNotFoundException | IllegalOrderCorrespondingQuantityException e) {
            log.error("Error creating executing order: {}", e.getMessage());
            if (DEBUG) {
                System.out.println("Error creating executing order: " + e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating executing order: " + e.getMessage());
        }
    }

    @GetMapping("/update/{username}/{orderId}")
    public ResponseEntity<?> updateExecutingOrder(@PathVariable int orderId, @PathVariable String username) {
        log.info("Attempting to update executing order for orderId: {}", orderId);
        try {
            orderService.updateExecutingOrderState(username, orderId);
            log.info("Executing order updated successfully");
            return ResponseEntity.ok("Executing order updated successfully.");
        } catch (OrderNotFoundException | IllegalOrderCorrespondingQuantityException e) {
            log.error("Error updating executing order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error updating executing order: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExecutingOrder>> getAllExecutingOrders() {
        log.info("Fetching all executing orders");
        try {
            List<ExecutingOrder> orders = orderService.getAllExecutingOrders();
            if (orders.isEmpty()) {
                log.info("No executing orders found");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(orders);
        } catch (OrderNotFoundException e) {
            log.error("Error fetching executing orders: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
