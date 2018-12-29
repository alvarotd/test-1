package com.n26;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.n26.transactions.add.domain.AddTransaction;
import com.n26.transactions.add.infrastructure.AddTransactionSerializer;
import com.n26.transactions.add.infrastructure.StatisticsDeserializer;
import com.n26.transactions.add.infrastructure.StatisticsSerializer;
import com.n26.transactions.statistics.domain.Statistics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        SimpleModule transactionModule = new SimpleModule();
        transactionModule.addSerializer(AddTransaction.class, new AddTransactionSerializer());

        SimpleModule statisticsModule = new SimpleModule();
        statisticsModule.addSerializer(Statistics.class, new StatisticsSerializer());
        statisticsModule.addDeserializer(Statistics.class, new StatisticsDeserializer());
        return new ObjectMapper()
                .registerModule(transactionModule)
                .registerModule(statisticsModule)
                .registerModule(new KotlinModule())
                ;
    }
}
