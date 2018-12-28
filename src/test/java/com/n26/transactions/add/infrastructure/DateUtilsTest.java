package com.n26.transactions.add.infrastructure;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class DateUtilsTest {

    @Test
    public void parseDate(){
        final LocalDateTime original = DateUtils.parseDate("2018-07-17T09:59:51.312Z");

        final LocalDateTime reversed = DateUtils.parseDate(DateUtils.formatted(original));

        assertThat(original).isEqualTo(reversed);
    }

}