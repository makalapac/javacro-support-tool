package com.trilix.ai.voucher.support_tool.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.AdvisedRequest;
import org.springframework.ai.chat.client.RequestResponseAdvisor;

import java.util.Map;

public class LoggingAdvisor implements RequestResponseAdvisor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAdvisor.class);

    @Override
    public AdvisedRequest adviseRequest(AdvisedRequest request, Map<String, Object> context) {
        logger.info("Request: " + request);
        return request;
    }
}
