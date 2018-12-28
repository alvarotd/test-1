package com.n26.transactions.add.infrastructure.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.MyConfiguration;
import com.n26.transactions.add.domain.AddTransaction;
import com.n26.transactions.add.infrastructure.AddTransactionObjectMother;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomJsonMapperTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new MyConfiguration().objectMapper();
    }

    @Test
    public void serialize_and_deserialize_an_AddTransaction() throws IOException {
        final AddTransaction original = AddTransactionObjectMother.valid();

        AddTransaction reversedObject = objectMapper.readValue(objectMapper.writeValueAsString(original), AddTransaction.class);

        assertThat(reversedObject).isEqualTo(original);
    }
}
