/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 *
 * @author Mk
 */
public class Convert {
    public static String toSimpleDate(Timestamp timestamp) {
        LocalDateTime date = timestamp.toLocalDateTime();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy").withLocale(Locale.US).withZone(ZoneId.of("UTC+7"));
        String dateString = date.format(format);
        return dateString;
    }
    
    public static String toSimpleDateWithHour(Timestamp timestamp) {
        LocalDateTime date = timestamp.toLocalDateTime();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("k:mm - EEEE, dd MMMM yyyy").withLocale(Locale.US).withZone(ZoneId.of("UTC+7"));
        String dateString = date.format(format);
        return dateString;
    }
}
