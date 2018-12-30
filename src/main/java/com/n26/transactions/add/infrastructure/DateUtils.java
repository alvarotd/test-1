package com.n26.transactions.add.infrastructure;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.DatatypeConverter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.TimeZone;

public class DateUtils {
    public static ZonedDateTime parseDate(String rawValue) {
        return ZonedDateTime.parse(rawValue, getFormatter());
    }

    public static String formatted(ZonedDateTime dateTime) {
        final String format = dateTime.format(getFormatter());
        return format;
    }

    @NotNull
    private static DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX").withZone(ZoneId.of("UTC"));
    }

    @NotNull
    public static ZonedDateTime now() {
        return ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
    }
}
