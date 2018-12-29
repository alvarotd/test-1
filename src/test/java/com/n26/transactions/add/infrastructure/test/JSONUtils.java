package com.n26.transactions.add.infrastructure.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.MyConfiguration;
import com.n26.transactions.domain.Transaction;

public class JSONUtils {
    public static String toJSON(Transaction transaction) throws JsonProcessingException {
        final ObjectMapper objectMapper = new MyConfiguration().objectMapper();
        return objectMapper.writeValueAsString(transaction);
    }
}
