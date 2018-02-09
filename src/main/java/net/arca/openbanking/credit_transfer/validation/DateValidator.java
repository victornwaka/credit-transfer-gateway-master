package net.arca.openbanking.credit_transfer.validation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator {

    public static boolean isDateValid(String strDate){
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime ldt = LocalDateTime.parse(strDate, dtf);
            return  true;
        }catch (DateTimeParseException e){
            return false;
        }catch(Exception e){
            return false;
        }
    }
}