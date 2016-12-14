package src;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;

// Battles are organized via a hashtable which is organized by month-year 
// The values of the hashtable are linked lists that contain the actual Battle events
public class Schedule {
    Hashtable <String, LinkedList<Battle>> schedule;
    Path storageDirPath;
    Path storageFilePath;
    
    public Schedule() {
        schedule = new Hashtable();
    }
    
    
    
    // adds to the schedule if possible, returns true if successful
    public boolean addToSchedule(Battle b) {
        String key = b.getHashKey();
        boolean availableTime = true;
        if (schedule.containsKey(key)) {
            // check to make sure time is available 
            availableTime = isAvailableTime(b);
            if (availableTime) {
                schedule.get(key).add(b);
            }    
            else {
                System.out.println("We didn't add your event because of a conflict!\n");
            }
            return availableTime;
        }
        else {
            schedule.put(b.getHashKey(), new LinkedList<Battle>());
            schedule.get(key).add(b);
            return true;
        }
    }
    
    // Updates a Battle by removing the initial version and tacking on the new one.
    // Because we're using linked lists anyways, we are unconerned with the sorting.
    // Returns true if successful.
    public boolean updateBattleInSchedule(Battle b1, Battle b2) {
        boolean updatedBattle = false;
        String key = b1.getHashKey();
        
        if (schedule.get(key).contains(b1)) {
            int index = schedule.get(key).indexOf(b1);
            deleteFromSchedule(b1);
            if (isAvailableTime(b2)) {
                schedule.get(key).add(index, b2);
                updatedBattle = true;
            }
            else {
                System.out.println("We couldn't update your battle because of a time conflict.");
                addToSchedule(b1);
            }
        }
        
        return updatedBattle;
    }
    
    // Deletes from the Schedule if the item is actually in the list. 
    // The function also checks to see if that was the last item in the list, if so, it also removes the key. 
    // Returns true if successful, false if not.
    public boolean deleteFromSchedule(Battle b) {
        boolean deletedBattle = false;
        String key = b.getHashKey();
        if (schedule.get(key).contains(b)) {
            schedule.get(key).remove(b);
            deletedBattle = true;
        }
        if (schedule.get(key).isEmpty()) {
            schedule.remove(key);
        }
        
        return deletedBattle;
    }
    
    // Checks to see if the battle passed as an argument is fitting into an available time slot. 
    // The Battles are set to go for thirty minutes each and thus a battle starting less than thirty
    // minutes before or less than thirty minutes after, is disallowed. 
    private boolean isAvailableTime (Battle b) {
        final int THIRTY_MINUTES = 1000 * 60 * 30;
        boolean isAvailable = true;
        
        
        LinkedList<Battle> events = schedule.get(b.getHashKey());
        for (Battle battle : events) {
            Long curBattleTime = battle.getTimeMilliseconds();
            Long newBattleTime = b.getTimeMilliseconds();
            
            if (    curBattleTime < newBattleTime + THIRTY_MINUTES && 
                    curBattleTime + THIRTY_MINUTES > newBattleTime){
                isAvailable = false;
            }
        }
        return isAvailable;
        
    }
    
    // Returns all battles currently in the schedule
    public LinkedList<Battle> getListOfBattles () {
        LinkedList<Battle> list = new LinkedList<>();
        Set<String> hashkeys = schedule.keySet();
        for (String key : hashkeys) {
            for (Battle b : schedule.get(key))
                list.add(b);
        }
        
        return list;
    }
    
    // Gets all the battles found a given month-year key
    public LinkedList<Battle> getBattlesFromMonth (String monthYearKey) {
        LinkedList<Battle> list = null;
        if (schedule.containsKey(monthYearKey)) {
            list = schedule.get(monthYearKey);
        }
        else 
            System.out.println("We did not find the month-year key you were using.");
        
        return list;
    } 
    
    // toString implementation primarily used for quick testing.
    @Override
    public String toString() {
        String result = "";
        Set<String> hashkeys = schedule.keySet();
        
        for (String key : hashkeys) {
            result += key + ":: " + schedule.get(key) + "\n";
        }
        
        return result;
    }
    
    
    // File IO functions 
    // Makes sure to set the paths (you can pass arguments to each of them, 
    // so small implementation changes can be made easily).
    // Returns true if the operation is successful.
    public boolean writeScheduleToStorageFile () {
        setDirPath();
        setFilePath();
        
        if (checkValidPath(this.storageDirPath, "d") && checkValidPath(this.storageFilePath, "f")) {
           
            File storageFile = this.storageFilePath.toFile();    
            try (PrintWriter out = new PrintWriter (
                                   new BufferedWriter(
                                   new FileWriter(storageFile)))) // set true so that it appends as opposed to deleting
            {                                                         // new FileWriter(stuffFile, true)))) for appending
                // outputs to your file
                
                LinkedList<Battle> allBattles = this.getListOfBattles();
                for (Battle b : allBattles) {
                    out.println(b.toStorageString());
                }
                return true;
                
            }
            catch (IOException e) {
                System.out.println(e);
            }
            
        } 
        return false;
    }
    
    // Also makes sure to set paths before reading from a possible file.
    // Returns true if successfully read in.
    public boolean loadScheduleFromStorageFile () {
        setDirPath();
        setFilePath();
        
        if (Files.exists(this.storageFilePath)) {
            File storageFile = this.storageFilePath.toFile(); 
            try (BufferedReader in = new BufferedReader(
                                     new FileReader(storageFile)))
            {
                String line = in.readLine();
                String[] curInput;
                String sep = "%!";
                while (line != null) {
                    // take input from file and create Battle objects
                    // legend: curInput == [p1, p2, bracket, timeInMillis]
                    curInput = line.split(sep);

                    //System.out.println(curInput);

                    // get and format time
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(new Long(curInput[3]));

                    Battle b = new Battle(curInput[0], curInput[1], curInput[2], cal);
                    this.addToSchedule(b);

                    line = in.readLine();

                }
               
                return true;
            }
            catch (IOException e) 
            {
                System.out.println(e);
                return false;
            }
        }
        
        
        return false;
    }
    
    // The below functions handle the actual path details and creation of a new directory if there isn't one.
    
    // Overrides the below function thus allowing for a pseudo default argument. 
    // To change the default argument (or require one) change either storage or
    // simply pass in an argument.
    private void setDirPath() {
        setDirPath("storage");
    }
    
    private void setDirPath(String dirName) {
        String filePathString = new File("").getAbsolutePath();
        String dirString = File.separator + dirName + File.separator;
        
        Path dirPath = Paths.get(filePathString + dirString);
        this.storageDirPath = dirPath;
    }
    
    private void setFilePath() {
        setFilePath("storage.txt");
    }
    
    private void setFilePath(String fileName) {
        Path storageDir = this.storageDirPath;
       
        String filePathString = storageDir.toString() + File.separator + fileName;
        
        Path filePath = Paths.get(filePathString);
        
        this.storageFilePath = filePath;
    }
    
    // Checks for a valid path and more importantly creates a directory/file if one is missing
    private boolean checkValidPath (Path path, String flag) {
        try {
            if (Files.notExists(path)) {
                if (flag.equals("d"))
                    Files.createDirectories(path);
                else if (flag.equals("f"))
                    Files.createFile(path);
                else {
                    throw new Error("Improper flag input to findOrCreateAtPath");
                }
            }
             
            return true;
        }
        catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }
    
    
   
}
