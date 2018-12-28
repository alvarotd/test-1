package com.n26.transactions.add.infrastructure.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.n26.transactions.add.infrastructure.delivery.AddTransaction;
import okhttp3.*;
import org.jetbrains.annotations.Nullable;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AddTransactionTest {

    @LocalServerPort
    int serverPort;

    OkHttpClient client = new OkHttpClient();

    private Response POST(String url, RequestBody requestBody) throws IOException {
        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }

    @Test
    @Ignore("Current feature")
    public void adding_a_valid_transaction() throws IOException {
        final LocalDateTime dateTime = parseDate("2018-07-17T09:59:51.312Z");
        final RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), toJSON(new AddTransaction(new BigDecimal("12.3343"), dateTime)));

        final Response response = POST(baseUrl("/transactions"), requestBody);

        assertThat(response.code()).isEqualTo(201);
        assertThat(response.body().string()).isEqualTo("");
    }

    private LocalDateTime parseDate(String rawValue) {
        final Calendar calendar = DatatypeConverter.parseDateTime(rawValue);
        return toLocalDateTime(calendar);
    }

    public static LocalDateTime toLocalDateTime(Calendar calendar) {
        TimeZone tz = calendar.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zid);
    }

    private String toJSON(AddTransaction addTransaction) throws JsonProcessingException {
        return new ObjectMapper().registerModule(new KotlinModule()).writeValueAsString(addTransaction);
    }

    private String baseUrl(String suffix) {
        return String.format("http://localhost:%s%s", serverPort, suffix);
    }
}
