package com.n26.transactions.add.infrastructure;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class DateUtilsTest {

    @Test
    public void parseDate(){
        final LocalDateTime actual = DateUtils.parseDate("2018-07-17T09:59:51.312Z");
        assertThat(actual).isEqualTo(DateUtils.parseDate(DateUtils.formatted(actual)));
    }

}