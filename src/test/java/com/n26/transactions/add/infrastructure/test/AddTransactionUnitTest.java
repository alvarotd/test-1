package com.n26.transactions.add.infrastructure.test;

import arrow.core.Either;
import com.n26.transactions.TransactionService;
import com.n26.transactions.add.infrastructure.TransactionController;
import com.n26.transactions.add.infrastructure.TransactionSuccess;
import com.n26.transactions.add.infrastructure.delivery.AddTransaction;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static com.n26.transactions.add.infrastructure.test.DateUtils.parseDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddTransactionUnitTest {


    public static final AddTransaction REQUEST = new AddTransaction(new BigDecimal("12.3343"), parseDate("2018-07-17T09:59:51.312Z"));

    @Test
    public void delegate_the_call_to_the_service() {
        TransactionService service = serviceResponding();
        TransactionController controller = new TransactionController(service);

        controller.addTransaction(REQUEST);

        verify(service).addTransaction(REQUEST);
    }

    @Test
    public void a_valid_response_is_mapped() {
        TransactionService service = serviceResponding();
        TransactionController controller = new TransactionController(service);

        final ResponseEntity<?> responseEntity = controller.addTransaction(REQUEST);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(null);
    }

    @NotNull
    private TransactionService serviceResponding() {
        TransactionService service = mock(TransactionService.class);
        when(service.addTransaction(any())).thenReturn(Either.Companion.right(new TransactionSuccess()));
        return service;
    }
}
