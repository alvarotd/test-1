package com.n26.transactions.add.infrastructure.test;

import javax.xml.bind.DatatypeConverter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

public class DateUtils {
    public static LocalDateTime parseDate(String rawValue) {
        final Calendar calendar = DatatypeConverter.parseDateTime(rawValue);
        return toLocalDateTime(calendar);
    }

    private static LocalDateTime toLocalDateTime(Calendar calendar) {
        TimeZone tz = calendar.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zid);
    }
}
