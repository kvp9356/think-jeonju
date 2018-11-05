package com.kvp.thinkjeonju.helper;

import com.github.jknack.handlebars.Options;
import org.springframework.stereotype.Component;
import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@HandlebarsHelper
public class CustomHelper {

    public CharSequence isMyLike(Object like, Options options) throws IOException {
        if((boolean)like) {
            return options.fn(this);
        }
        return options.inverse(this);
    }

    public String diffDate(LocalDate startDate, LocalDate endDate, Options options) {
        return Long.toString(startDate.until(endDate, ChronoUnit.DAYS) + 1);
    }
}
