package com.acc.core.aspect;

import com.acc.core.annotation.NeedSignature;
import com.acc.core.exception.InvalidSignatureException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

@Aspect
@Component
public class SignatureAspect {

    private static final String SECRET_KEY = "your-secret-key"; // 请使用安全的密钥

    private final HttpServletRequest request;

    public SignatureAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Pointcut("@annotation(com.acc.core.annotation.NeedSignature)")
    public void needSignaturePointcut() {
    }

    @Around("needSignaturePointcut()")
    public Object validateSignature(ProceedingJoinPoint joinPoint) throws Throwable {
        // 从请求头中获取签名和时间戳
        String requestSignature = request.getHeader("X-Signature");
        String timestamp = request.getHeader("X-Timestamp");

        if (requestSignature == null || timestamp == null) {
            throw new InvalidSignatureException("Missing signature or timestamp in request");
        }

        // 生成服务器端的签名
        String generatedSignature = generateSignature(joinPoint, timestamp);

        // 比较签名
        if (!Objects.equals(requestSignature, generatedSignature)) {
            throw new InvalidSignatureException("Invalid signature");
        }

        // 签名验证通过，继续执行方法
        return joinPoint.proceed();
    }

    private String generateSignature(ProceedingJoinPoint joinPoint, String timestamp) throws Exception, InvalidSignatureException {
        // 获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // 获取注解
        NeedSignature needSignature = method.getAnnotation(NeedSignature.class);
        String[] dataKeys = needSignature.data();

        // 获取方法参数名称和值
        String[] paramNames = methodSignature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();

        // 构建签名数据
        StringBuilder sb = new StringBuilder();

        // 使用指定的参数名获取对应的值
        for (String key : dataKeys) {
            boolean found = false;
            for (int i = 0; i < paramNames.length; i++) {
                if (paramNames[i].equals(key)) {
                    sb.append(paramValues[i] != null ? paramValues[i].toString() : "null");
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new InvalidSignatureException("Parameter '" + key + "' not found in method arguments");
            }
        }

        // 添加时间戳和密钥
        sb.append(timestamp);
        sb.append(SECRET_KEY);

        // 生成 SHA-256 哈希
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(sb.toString().getBytes(StandardCharsets.UTF_8));

        // 将哈希转换为十六进制字符串
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
