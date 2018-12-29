package com.n26.transactions.add.infrastructure.test;

import com.n26.Application;
import com.n26.transactions.add.infrastructure.AddTransactionObjectMother;
import okhttp3.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@RunWith(SpringRunner.class)
public class AddTransactionTest {

    @LocalServerPort
    int serverPort;

    OkHttpClient client = new OkHttpClient();

    @Test
    public void adding_a_valid_transaction() throws IOException {
        final String content = JSONUtils.toJSON(AddTransactionObjectMother.valid());
        final RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), content);

        final Response response = POST(baseUrl("/transactions"), requestBody);

        assertThat(response.code()).isEqualTo(201);
        assertThat(response.body().string()).isEqualTo("");
    }

    @Test
    public void adding_a_transaction_with_invalid_json() throws IOException {
        final RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), "");

        final Response response = POST(baseUrl("/transactions"), requestBody);

        assertThat(response.code()).isEqualTo(BAD_REQUEST.value());
    }

    @Test
    public void adding_a_transaction_with_invalid_json_2() throws IOException {
        final RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), "Hello World!");

        final Response response = POST(baseUrl("/transactions"), requestBody);

        assertThat(response.code()).isEqualTo(BAD_REQUEST.value());
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

    private Response POST(String url, RequestBody requestBody) throws IOException {
        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }

    private String baseUrl(String suffix) {
        return String.format("http://localhost:%s%s", serverPort, suffix);
    }
}
