package com.kvp.thinkjeonju.helper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.kvp.thinkjeonju.support.Paging;
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
    
    public CharSequence existAddr(Object addr, Object addrDtl, Options options) throws IOException {
    	String address = (String)addr;
    	String addressDtl = (String)addrDtl;
    	
    	if(address != null || addressDtl != null) {
    		return options.fn(this);
    	} 
    	return options.inverse(this);
    }

    public String makeBlock(Paging paging) {
        String html = "";
        int start = paging.getBlockStart();
        int end = paging.getBlockEnd();
        int current = paging.getCurrentBlock();
        int last = paging.getLastPage();

        if(start != 1) {
            html += "<li class='page-item'> <a href='/schedules?page="+(start - 1)+"' class='page-link' data-page='"+(start - 1)+"'> &laquo; </a></li>";
        }

        for(int i = start; i <= end; i++) {
            if(current == i) {
                html += "<li class='active page-item'><span class='page-link' data-page='" + i + "'>" + i + "</span></li>";
            } else {
                html += "<li class='page-item'><a href='/schedules?page="+i+"' class='page-link' data-page='" + i + "'>" + i + "</a></li>";
            }
        }

        if(end < last) {
            html += "<li class='page-item'> <a href='/schedules?page="+ (end + 1)+"'  class='page-link' data-page='"+(end + 1)+"'> &raquo; </a></li>";
        }

        return html;
    }
}
