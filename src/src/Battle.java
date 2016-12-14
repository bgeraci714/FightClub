package src;
 

// GregorianCalendar(int year, int month, int dayOfMonth, int hourOfDay, int minute)
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Battle {
    // list of possible brackets for battles
    final String[] BRACKETS = {"UBER", "OU", "UU", "RU", "NU"};
    
    String player1 = "";
    String player2 = "";
    String bracket = "";
    Calendar time; 
    
    // Constructor
    // Battle b = new Battle("Blake", "Evan", "OU", calendarObj);
    public Battle (String p1, String p2, String b, Calendar bTime) {
        player1 = p1;
        player2 = p2;
        bracket = b;
        time = bTime;
    }
    
    
    
    // takes the date field and converts it into the hashtable's key format for lookup and addition
    public String getHashKey () {
        SimpleDateFormat fmt = new SimpleDateFormat("MMM-yyyy");
        return fmt.format(time.getTime());
    }
    
    // returns a properly styled string for storage purposes
    // example of format: "Blake%!Bri%!OU%!1481569200000"
    public String toStorageString () {
        
        String result = "";
        String sep = "%!";
        
        result += player1 + sep + player2 + sep + bracket + sep;
        result += time.getTimeInMillis();
        
        return result;
    }
    
    // used for quick retreival of time for later storage
    public Long getTimeMilliseconds() {
        return time.getTimeInMillis();
    }
    
    // Likely unnecessary because we'll be displaying them in the GUI anyways
    @Override 
    public String toString () {
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat fmtTime = new SimpleDateFormat("HH:mm");
        
        fmt.setCalendar(time);
        String dateFormattedFuck = fmt.format(time.getTime());
        String timeString = fmtTime.format(time.getTime());
        
        String result = "";
        
        result += player1 + " is battling " + player2 + " on " + dateFormattedFuck + " at " + timeString + " on the dot!\n";
        result += "The Battle will be in the " + bracket + " bracket!\n";
        
        return result;
    }
}
