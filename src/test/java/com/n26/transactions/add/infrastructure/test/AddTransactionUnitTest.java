package com.n26.transactions.add.infrastructure.test;

import arrow.core.Either;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.MyConfiguration;
import com.n26.transactions.TransactionService;
import com.n26.transactions.add.domain.AddTransaction;
import com.n26.transactions.add.infrastructure.*;
import com.n26.transactions.add.infrastructure.TransactionError.TransactionDateInTheFuture;
import com.n26.transactions.add.infrastructure.TransactionError.TransactionIsTooOld;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import static arrow.core.Either.Companion;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddTransactionUnitTest {

    public static final TransactionsObjectMapper objectMapper = new TransactionsObjectMapper(new MyConfiguration().objectMapper());

    public static final AddTransaction REQUEST = AddTransactionObjectMother.valid();

    @Test
    public void delegate_the_call_to_the_service() {
        TransactionService service = serviceResponding(Companion.right(new TransactionSuccess()));
        TransactionController controller = new TransactionController(service, objectMapper);

        addTransaction(controller);

        verify(service).addTransaction(REQUEST);
    }

    @Test
    public void a_valid_response_is_mapped() {
        TransactionController controller = controllerUsing(Companion.right(new TransactionSuccess()));
        final ResponseEntity<?> responseEntity = addTransaction(controller);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isEqualTo(null);
    }

    @Test
    public void statistic_is_too_old_response_is_mapped() {
        TransactionController controller = controllerUsing(Companion.left(TransactionIsTooOld.INSTANCE));

        final ResponseEntity<?> responseEntity = addTransaction(controller);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(responseEntity.getBody()).isEqualTo(null);
    }

    @Test
    public void statistic_date_in_the_future_response_is_mapped() {
        TransactionController controller = controllerUsing(Companion.left(TransactionDateInTheFuture.INSTANCE));

        final ResponseEntity<?> responseEntity = addTransaction(controller);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(responseEntity.getBody()).isEqualTo(null);
    }

    @Test
    public void delete_delegate_the_call_to_the_service() {
        TransactionService service = mock(TransactionService.class);
        TransactionController controller = new TransactionController(service, objectMapper);

        controller.deleteAllTransactions();

        verify(service).deleteAllTransactions();
    }

    @NotNull
    private ResponseEntity<?> addTransaction(TransactionController controller) {
        try {
            return controller.addTransaction(objectMapper.getObjectMapper().writeValueAsString(REQUEST));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private BindingResult noBindingErrors() {
        final BindingResult mock = mock(BindingResult.class);
        when(mock.hasErrors()).thenReturn(false);
        return mock;
    }

    @NotNull
    private TransactionController controllerUsing(Either serviceResult) {
        TransactionService service = serviceResponding(serviceResult);
        return new TransactionController(service, objectMapper);
    }

    @NotNull
    private TransactionService serviceResponding(Either result) {
        TransactionService service = mock(TransactionService.class);
        when(service.addTransaction(any())).thenReturn(result);
        return service;
    }
}
