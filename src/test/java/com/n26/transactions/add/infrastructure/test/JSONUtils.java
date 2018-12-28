package com.n26.transactions.add.infrastructure.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.n26.transactions.add.infrastructure.delivery.AddTransaction;

public class JSONUtils {
    public static String toJSON(AddTransaction addTransaction) throws JsonProcessingException {
        return new ObjectMapper().registerModule(new KotlinModule()).writeValueAsString(addTransaction);
    }
}
