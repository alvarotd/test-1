package com.n26.transactions.statistics.infrastructure.test;

import com.n26.transactions.domain.TransactionService;
import com.n26.transactions.statistics.domain.Statistics;
import com.n26.transactions.statistics.infrastructure.delivery.StatisticsController;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

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
}
