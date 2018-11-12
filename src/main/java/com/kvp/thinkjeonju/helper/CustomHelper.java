package com.kvp.thinkjeonju.helper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.github.jknack.handlebars.Options;

import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

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
    
    public CharSequence existSpot(Object spots, Options options) throws IOException {
    	ArrayList array = (ArrayList)spots;
    	
    	if(array.size() != 0) {
    		return options.fn(this);
    	} 
    	return options.inverse(this);
    }
    
    public CharSequence existAddr(Object addr, Options options) throws IOException {
    	String address = (String)addr;
    	
    	if(address != null) {
    		return options.fn(this);
    	} 
    	return options.inverse(this);
    }
}
