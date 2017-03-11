
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 *
 * @author Denny Desktop
 */
public class DateAndTime {
   
    
  
    
     public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    //public static String dt;
    //return a String of the current system date and time
    public static String DateTime()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
    
}
    

