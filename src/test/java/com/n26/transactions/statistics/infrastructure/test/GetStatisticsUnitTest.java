package com.n26.transactions.statistics.infrastructure.test;

import com.n26.transactions.TransactionService;
import com.n26.transactions.statistics.domain.Statistics;
import com.n26.transactions.statistics.infrastructure.delivery.StatisticsController;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.n26.transactions.statistics.domain.Statistics.Companion;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class GetStatisticsUnitTest {

    @Test
    public void delegate_a_valid_request() throws IOException {
        final TransactionService service = mock(TransactionService.class);

        controller(service).getStatistics();

        verify(service).getStatistics();
    }

    @Test
    public void map_a_valid_request() throws IOException {
        final TransactionService service = mock(TransactionService.class);
        final Statistics statistics = Companion.of(10);
        when(service.getStatistics()).thenReturn(statistics);

        final ResponseEntity<?> response = controller(service).getStatistics();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(statistics);
    }

    private StatisticsController controller(TransactionService service) {
        return new StatisticsController(service);
    }

    @NotNull
    private List<Pair<String, String>> allZeroes() {
        return Arrays.asList(
                new Pair<>("sum", "0.00"),
                new Pair<>("avg", "0.00"),
                new Pair<>("max", "0.00"),
                new Pair<>("min", "0.00"));
    }
}
