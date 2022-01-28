package com.vaadin.addon.spreadsheet.charts.converter.xssfreader;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public enum Decimals {

    UTILS;

    public static Optional<BigDecimal> toBigDecimalOptional(final Object value) {
        Optional<BigDecimal> ret = Optional.empty();
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = Optional.of((BigDecimal) value);
            } else if (value instanceof String) {
                if (((String) value).isEmpty()) {
                    ret = Optional.of(BigDecimal.ZERO);
                } else {
                    ret = Optional.of(new BigDecimal((String) value));
                }
            } else if (value instanceof BigInteger) {
                ret = Optional.of(new BigDecimal((BigInteger) value));
            } else if (value instanceof Integer) {
                ret = Optional.of(new BigDecimal((Integer) value));
            } else if (value instanceof Long) {
                ret = Optional.of(new BigDecimal((Long) value));
            } else if (value instanceof Number) {
                Double d = ((Number) value).doubleValue();
                if (!d.isInfinite() && !d.isNaN()) {
                    ret = Optional.of(BigDecimal.valueOf(d));
                }
            } else if (value instanceof Boolean) {
                ret = Optional.of(((Boolean) value).booleanValue() ? BigDecimal.ONE : BigDecimal.ZERO);
            } else if (value instanceof LocalDate) {
                ret = Optional.of(new BigDecimal(toYMD8((LocalDate) value)));
            } else if (value instanceof Date) {
                ret = Optional.of(new BigDecimal(toYMD8(toLocalDate((Date) value))));
            } else {
                throw new ClassCastException("Not possible to cast [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
            }
        }
        return ret;
    }

    public static BigDecimal toBigDecimal(final Object value) {
        return toBigDecimalOptional(value).orElse(BigDecimal.ZERO);
    }

    public static LocalDate toLocalDate(final Date date) {
        if (date == null) {
            return null;
        }
        return new java.sql.Date(date.getTime()).toLocalDate();
    }

    public static Integer toYMD8(final LocalDate date) {
        return (date.getYear() * 10000) + (date.getMonthValue() * 100) + date.getDayOfMonth();
    }

}
