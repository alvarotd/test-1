package com.n26.transactions.delete.infrastructure.test;

import com.n26.Application;
import com.n26.transactions.add.infrastructure.AddTransactionObjectMother;
import com.n26.transactions.add.infrastructure.test.JSONUtils;
import okhttp3.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@RunWith(SpringRunner.class)
public class DeleteTransactionTest {

    @LocalServerPort
    int serverPort;

    OkHttpClient client = new OkHttpClient();

    @Test
    public void adding_a_valid_transaction() throws IOException {

        final Response response = DELETE(baseUrl("/transactions"));

        assertThat(response.code()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Response DELETE(String url) throws IOException {
        Request request = new Request.Builder()
                .delete()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }

    private String baseUrl(String suffix) {
        return String.format("http://localhost:%s%s", serverPort, suffix);
    }
}
