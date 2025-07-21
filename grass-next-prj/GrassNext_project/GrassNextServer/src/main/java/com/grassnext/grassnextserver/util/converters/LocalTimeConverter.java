package com.grassnext.grassnextserver.util.converters;

import com.grassnext.grassnextserver.util.Consts;
import com.opencsv.bean.AbstractBeanField;
import java.time.LocalTime;

/**
 * Converts a String representation of a date into a {@code LocalDate} using
 * a predefined formatter {@code TIME_FORMATTER} from the {@code Consts}.
 *
 */
public class LocalTimeConverter extends AbstractBeanField<String, LocalTime> {
    public LocalTime convert(String time) {
        return LocalTime.parse(time, Consts.TIME_FORMATTER);
    }
}
