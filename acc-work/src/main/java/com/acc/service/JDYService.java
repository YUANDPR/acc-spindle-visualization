package com.acc.service;

import com.acc.core.exception.IllegalOrderCorrespondingQuantityException;
import com.acc.core.exception.OrderNotFoundException;
import org.json.JSONException;

public interface JDYService {
    void pushAllData2JDY() throws Exception;

    boolean validateSignature(String receivedSignature, String nonce, String body, String timestamp);

    void handleUpdate(String body) throws IllegalOrderCorrespondingQuantityException, JSONException, OrderNotFoundException;

    void pullAndMergeAllDataFromJDY();
}
