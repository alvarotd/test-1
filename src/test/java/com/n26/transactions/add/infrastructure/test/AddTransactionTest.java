package com.n26.transactions.add.infrastructure.test;

import com.n26.Application;
import com.n26.transactions.add.infrastructure.DateUtils;
import com.n26.transactions.add.domain.AddTransaction;
import okhttp3.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
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
    public void adding_a_valid_transaction() throws IOException {
        final LocalDateTime dateTime = DateUtils.parseDate("2018-07-17T09:59:51.312Z");
        final String content = JSONUtils.toJSON(new AddTransaction(new BigDecimal("12.3343"), dateTime));
        final RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), content);

        final Response response = POST(baseUrl("/transactions"), requestBody);

        assertThat(response.code()).isEqualTo(201);
        assertThat(response.body().string()).isEqualTo("");
    }

    @Test
    public void adding_a_transaction_with_invalid_json() throws IOException {
        final RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), "");

        final Response response = POST(baseUrl("/transactions"), requestBody);

        assertThat(response.code()).isEqualTo(NOT_ACCEPTABLE.value());
    }

    @Test
    public void adding_a_transaction_with_invalid_json_because_not_parseable() throws IOException {
        final RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), "{\n" +
                "  \"amount\": \"12.3343\",\n" +
                "  \"timestamp\": \"2018-07-17T09:59.312Z\"\n" +
                "}");

        final Response response = POST(baseUrl("/transactions"), requestBody);

        assertThat(response.code()).isEqualTo(422);
    }

    private String baseUrl(String suffix) {
        return String.format("http://localhost:%s%s", serverPort, suffix);
    }
}
