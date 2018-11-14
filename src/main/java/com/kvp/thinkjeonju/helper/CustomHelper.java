package com.kvp.thinkjeonju.helper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.kvp.thinkjeonju.dto.MoneyDTO;
import com.kvp.thinkjeonju.dto.ScheSpotDTO;
import com.kvp.thinkjeonju.dto.ScheduleDTO;
import com.kvp.thinkjeonju.support.Paging;
import org.springframework.stereotype.Component;

import com.github.jknack.handlebars.Options;

import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

@Component
@HandlebarsHelper
public class CustomHelper {

    public String drawModal( ScheduleDTO schedule, List<ScheSpotDTO> scheSpot, List<MoneyDTO> money) throws IOException {
        System.out.println("여기 들어옴!");
        LocalDate startDate =schedule.getStartDate();
        LocalDate endDate = schedule.getEndDate();
        String html ="";
        int day = (int)startDate.until(endDate, ChronoUnit.DAYS) + 1;

        for(int count=1; count<=day;count++){
            html += "<div id='drawDay"+count+"' class='draw-form day"+count+"'>\n"+
                    "                <div class='nameDay'>"+count+" 일차</div>\n" +
                    "                <div class='drawDay'>\n";
            String constHTML ="";
            for(int j=0;j<scheSpot.size();j++){
                if((day - (int)scheSpot.get(j).getScheDate().until(endDate, ChronoUnit.DAYS) + 1)+1 == count){
                    constHTML += "<div class='draw' data-spot-id='"+ scheSpot.get(j).getSpotId() +"'>\n" +
                            "                        <img src='"+ scheSpot.get(j).getSpotimg()+"'  class='spotimg'>\n" +
                            "                    </div>\n" +
                            "<img src='/image/right-arrow.png' class='arrowimg'>\n";
                }
            }
            if(constHTML!=""){
                constHTML += "<img src='/image/finish-flag.png' class='finish_flag'>\n";
            }
            html+=constHTML;

            html+=   "                </div>\n" +
                    "            </div>";
        }

        return html;
    }

    public String draw( ScheduleDTO schedule, List<ScheSpotDTO> scheSpot, List<MoneyDTO> money) throws IOException {
        System.out.println("여기 들어옴!");
        LocalDate startDate =schedule.getStartDate();
        LocalDate endDate = schedule.getEndDate();
        String html ="";
        int day = (int)startDate.until(endDate, ChronoUnit.DAYS) + 1;

        for(int count=1; count<=day;count++){
           html += "<div id='drawDay"+count+"' class='draw-form day"+count+"'>\n"+
                    "                <div class='nameDay'>"+count+" 일차</div>\n" +
                    "                <div class='drawDay'>\n";
           String constHTML ="";
           for(int j=0;j<scheSpot.size();j++){
               if((day - (int)scheSpot.get(j).getScheDate().until(endDate, ChronoUnit.DAYS) + 1)+1 == count){
                   constHTML += "<div class='draw' data-spot-id='"+ scheSpot.get(j).getSpotId() +"'>\n" +
                           "                        <img src='"+ scheSpot.get(j).getSpotimg()+"'  class='spotimg'>\n" +
                           "                    </div>\n" +
                           "<img src='/image/right-arrow.png' class='arrowimg'>\n";
               }
           }
           if(constHTML!=""){
               constHTML += "<img src='/image/finish-flag.png' class='finish_flag'>\n";
           }
           html+=constHTML;

           html+=   "                </div>\n" +
                    "            </div>";
        }

        return html;
    }


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
