package com.n26;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.n26.transactions.add.infrastructure.AddTransactionDeserializer;
import com.n26.transactions.add.infrastructure.AddTransactionSerializer;
import com.n26.transactions.add.domain.AddTransaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(AddTransaction.class, new AddTransactionDeserializer());
        simpleModule.addSerializer(AddTransaction.class, new AddTransactionSerializer());
        return new ObjectMapper()
                .registerModule(simpleModule)
                .registerModule(new KotlinModule())
                ;
    }
}
