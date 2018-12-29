package com.n26.transactions.add.infrastructure;

import org.junit.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DateUtilsTest {

    @Test
    public void parseDate(){
        final ZonedDateTime original = DateUtils.parseDate("2018-07-17T09:59:51.312Z");

        final ZonedDateTime reversed = DateUtils.parseDate(DateUtils.formatted(original));

        assertThat(original).isEqualTo(reversed);
    }

}