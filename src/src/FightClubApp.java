package src;

import java.io.*;
import java.nio.file.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;



public class FightClubApp {
    public static void main (String[] args) throws IOException {
        
        
        Calendar time = new GregorianCalendar(2016, 5, 15, 14, 0);
        Battle b1 = new Battle("Blake", "Isabelle", "OU", time);
        
        Calendar time2 = new GregorianCalendar(2016, 6, 14, 13, 30);
        Battle b2 = new Battle("Blake", "Evan", "OU", time2);
        
        Calendar time3 = new GregorianCalendar(2016, 10, 11, 14, 30);
        Battle b3 = new Battle("Blake", "Louie CK", "OU", time3);
        
        
        Schedule s = new Schedule ();
        s.loadScheduleFromStorageFile();
        
        System.out.println(s);
        
        
        s.addToSchedule(b1); // wont work if there's a conflict (which there should be)
        s.addToSchedule(b2); // wont work if there's a conflict (which there should be)
        s.addToSchedule(b3); // wont work if there's a conflict (which there should be)
        
        
        
        //System.out.println(s.writeScheduleToStorageFile()); // should print out true on success
        
        
        System.out.println(s);
        
    }
}
