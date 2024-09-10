package com.acc.controller.work;


import com.acc.core.annotation.Anonymous;
import com.acc.service.JDYService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/jdy")
@Api(tags = "jiandaoyun")
public class JDYController {

    private final JDYService jdyService;

    @Autowired
    JDYController(JDYService jdyService) {
        this.jdyService = jdyService;
    }

    /**
     * 处理更新操作
     */
    @PostMapping("/update")
    @Anonymous
    public ResponseEntity<?> handleUpdate(
            @RequestBody String body,  // 接收请求体
            @RequestHeader("x-jdy-signature") String receivedSignature,  // 接收签名
            @RequestParam("nonce") String nonce,  // 接收请求参数nonce
            @RequestParam("timestamp") String timestamp) {  // 接收请求参数timestamp

        log.info("RequestBody: {}", body);
        log.info("Signature: {}, Nonce: {}, Timestamp: {}", receivedSignature, nonce, timestamp);

        // 验证签名
        if (!jdyService.validateSignature(receivedSignature, nonce, body, timestamp)) {
            log.warn("Invalid signature. Request denied.");
            return ResponseEntity.status(401).body("Unauthorized: Invalid signature");
        }

        // 签名验证通过后处理更新
        try {
            jdyService.handleUpdate(body);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            log.error("Error while processing update", e);
            return ResponseEntity.status(400).body("error: " + e.getMessage());
        }
    }

}
