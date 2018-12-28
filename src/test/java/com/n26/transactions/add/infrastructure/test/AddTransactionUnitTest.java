package com.n26.transactions.add.infrastructure.test;

import com.n26.transactions.TransactionService;
import com.n26.transactions.add.infrastructure.TransactionController;
import com.n26.transactions.add.infrastructure.delivery.AddTransaction;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.n26.transactions.add.infrastructure.test.DateUtils.parseDate;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AddTransactionUnitTest {


    @Test
    public void delegate_the_call_to_the_service() {
        TransactionService service = mock(TransactionService.class);
        final LocalDateTime dateTime = parseDate("2018-07-17T09:59:51.312Z");
        final AddTransaction request = new AddTransaction(new BigDecimal("12.3343"), dateTime);
        TransactionController controller = new TransactionController(service);

        controller.addTransaction(request);

        verify(service).addTransaction(request);
    }
}
