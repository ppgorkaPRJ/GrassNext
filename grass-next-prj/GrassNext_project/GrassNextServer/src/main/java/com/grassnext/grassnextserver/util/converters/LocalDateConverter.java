package com.grassnext.grassnextserver.util.converters;

import com.grassnext.grassnextserver.util.Consts;
import com.opencsv.bean.AbstractBeanField;
import java.time.LocalDate;

/**
 * Converts a String representation of a date into a {@code LocalDate} using
 * a predefined formatter {@code DATE_FORMATTER} from the {@code Consts}.
 *
 */
public class LocalDateConverter extends AbstractBeanField<String, LocalDate> {
    public LocalDate convert(String date) {
        return LocalDate.parse(date, Consts.DATE_FORMATTER);
    }
}
