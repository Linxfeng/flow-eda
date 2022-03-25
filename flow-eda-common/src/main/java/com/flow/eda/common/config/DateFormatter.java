package com.flow.eda.common.config;

import org.springframework.format.Formatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter implements Formatter<Date> {
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        return df.parse(text);
    }

    @Override
    public String print(Date object, Locale locale) {
        return df.format(object);
    }
}
