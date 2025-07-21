package com.grassnext.grassnextserver.util.converters;

import com.grassnext.grassnextserver.util.Consts;
import com.opencsv.bean.AbstractBeanField;
import java.time.LocalDateTime;

/**
 * Converts a String representation of a date into a {@code LocalDate} using
 * a predefined formatter {@code DATE_TIME_FORMATTER} from the {@code Consts}.
 *
 */
public class LocalDateTimeConverter extends AbstractBeanField<String, LocalDateTime> {
    public LocalDateTime convert(String dateTime) {
        return LocalDateTime.parse(dateTime, Consts.DATE_TIME_FORMATTER);
    }
}
