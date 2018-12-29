package com.n26.transactions.add.infrastructure.test;

import arrow.core.Either;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.MyConfiguration;
import com.n26.transactions.domain.Transaction;
import com.n26.transactions.add.infrastructure.delivery.io.AddTransactionRequest;
import com.n26.transactions.add.infrastructure.AddTransactionObjectMother;
import com.n26.transactions.statistics.domain.Statistics;
import com.n26.transactions.statistics.domain.StatisticsObjectMother;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomJsonMapperTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new MyConfiguration().objectMapper();
    }

    @Test
    public void serialize_and_deserialize_an_AddTransaction() throws IOException {
        final Transaction original = AddTransactionObjectMother.valid();

        Transaction reversedObject = ((Either.Right<Transaction>) objectMapper.readValue(objectMapper.writeValueAsString(original), AddTransactionRequest.class).toDomain()).getB();

        assertThat(reversedObject).isEqualTo(original);
    }

    @Test
    public void serialize_the_date_in_the_correct_format() throws IOException {
        final Transaction original = AddTransactionObjectMother.aged(ZonedDateTime.of(LocalDateTime.MIN, ZoneId.of("GMT")));

        final String value = objectMapper.writeValueAsString(original);

        assertThat(value).isEqualTo("{\"amount\":\"12.3343\",\"timestamp\":\"+1000000000-01-01T00:00:00.000Z\"}");
    }

    @Test
    public void serialize_and_deserialize_Statistics() throws IOException {
        final Statistics original = StatisticsObjectMother.allZeroes();

        final String content = objectMapper.writeValueAsString(original);
        Statistics reversedObject = objectMapper.readValue(content, Statistics.class);

        assertThat(reversedObject).isEqualTo(original);
    }

    @Test
    public void serialize_and_deserialize_Statistics_case_random() throws IOException {
        for (int i = 0; i < 10_000; i++) {
            final Statistics original = StatisticsObjectMother.random();

            final String content = objectMapper.writeValueAsString(original);
            Statistics reversedObject = objectMapper.readValue(content, Statistics.class);

            assertThat(reversedObject).isEqualTo(original);
        }
    }
}
