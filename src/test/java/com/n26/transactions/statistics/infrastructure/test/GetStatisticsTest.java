package com.n26.transactions.statistics.infrastructure.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.Application;
import com.n26.transactions.statistics.domain.Statistics;
import com.n26.transactions.statistics.domain.StatisticsObjectMother;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@RunWith(SpringRunner.class)
public class GetStatisticsTest {

    @LocalServerPort
    int serverPort;

    @Autowired
    ObjectMapper objectMapper;

    OkHttpClient client = new OkHttpClient();

    @Test
    public void valid_request() throws IOException {

        final Response response = GET(baseUrl("/statistics"));

        assertThat(response.code()).isEqualTo(200);
        assertThat(parse(response)).isEqualTo(StatisticsObjectMother.allZeroes());
    }

    private Statistics parse(Response response) throws IOException {
        return objectMapper.readValue(response.body().string(), Statistics.class);
    }

    private Response GET(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }

    private String baseUrl(String suffix) {
        return String.format("http://localhost:%s%s", serverPort, suffix);
    }
}
