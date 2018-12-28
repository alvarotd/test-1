package com.n26.transactions.add.infrastructure.test;

import arrow.core.Either;
import com.n26.transactions.TransactionService;
import com.n26.transactions.add.infrastructure.TransactionController;
import com.n26.transactions.add.infrastructure.TransactionSuccess;
import com.n26.transactions.add.infrastructure.delivery.AddTransaction;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static com.n26.transactions.add.infrastructure.test.DateUtils.parseDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddTransactionUnitTest {


    @Test
    public void delegate_the_call_to_the_service() {
        TransactionService service = mock(TransactionService.class);
        when(service.addTransaction(any())).thenReturn(Either.Companion.right(new TransactionSuccess()));
        final AddTransaction request = new AddTransaction(new BigDecimal("12.3343"), parseDate("2018-07-17T09:59:51.312Z"));
        TransactionController controller = new TransactionController(service);

        controller.addTransaction(request);

        verify(service).addTransaction(request);
    }

    @Test
    public void a_valid_response_is_mapped() {
        TransactionService service = mock(TransactionService.class);
        when(service.addTransaction(any())).thenReturn(Either.Companion.right(new TransactionSuccess()));
        TransactionController controller = new TransactionController(service);
        final AddTransaction request = new AddTransaction(new BigDecimal("12.3343"), parseDate("2018-07-17T09:59:51.312Z"));

        final ResponseEntity<?> responseEntity = controller.addTransaction(request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(null);
    }
}
