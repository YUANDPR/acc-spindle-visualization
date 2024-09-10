package com.acc.service;

import com.acc.core.exception.IllegalOrderCorrespondingQuantityException;
import org.springframework.boot.configurationprocessor.json.JSONException;

public interface JDYService {
    boolean validateSignature(String receivedSignature, String nonce, String body, String timestamp);

    void handleUpdate(String body) throws IllegalOrderCorrespondingQuantityException;
}
