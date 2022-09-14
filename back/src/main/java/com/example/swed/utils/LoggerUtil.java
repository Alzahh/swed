package com.example.swed.utils;

import com.example.swed.controlles.AuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggerUtil {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public void calculatedApiLog(String label, String request, String response) {
        StringBuilder sb = new StringBuilder(label);
        sb.append(" Request: ");
        sb.append(request);
        sb.append(" Response: ");
        sb.append(response);

        logger.info(sb.toString());


    }
}
