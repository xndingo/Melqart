// Show table if everything is given.


package assignment3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Theodoros Margomenos
 * @author Jeroen van Hoof
 */
public class dbReader {
    
    private List<String> database;
    
    /**
     * Gets the database.
     * 
     * @return database string array
     */
    public List<String> getDatabase() {
        return this.database;
    }
    
    /**
     * Reads the database file.
     * 
     * @throws FileNotFoundException 
     */
    public void readDb()
            throws FileNotFoundException {
        
        // Create scanner for database.
        Scanner sc = new Scanner(new File("src/resources/spiegelhalter.txt"));
        
        // Store text in array.
        List<String> lines = new ArrayList<>();
        int i = 0;
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
            i++;
        }
        
        // Store lines.
        this.database = lines;
    }
    
    /**
     * Finds and prints the line at which the query variable and corresponding
     * condition variables are.
     * 
     * @param queryVar the query variable
     * @param vars the condition variables
     * @return the line number of the header of the table we need
     */
    public int findQueryVar(String queryVar, String[] vars) {
        int lineNr = 0;
        
        for(String line : this.database) {
            
            // Check if line contains queryVar.
            if (line.indexOf(queryVar) != -1) {
                // Break down per word.
                String[] words = line.split(" ");
                               
                // Check if every word is a condition in the input.
                if (isSubset(words, vars)) {
                    System.out.println(line);
                    return lineNr;
                }
            }
            lineNr++;
        }
        
        return -1;
    }
    
    /**
     * Check if one set is the subset of another.
     * Note: skips the first element, because in this context it is queryVar.
     * 
     * @param subset subset to be checked
     * @param set set to be checked against
     * @return true if subset is a subset of set, false otherwise.
     */
    public boolean isSubset(String[] subset, String[] set) {
        
        // For each word, check if it is in the set.
        for(String subElem : subset) {
            
            // Skip first (because it is the queryVar).
            if (subElem.equals(subset[0])) {
                continue;
            }
            
            // Skip empty elements.
            if (subElem.isEmpty()) {
                continue;
            }
            boolean isInSet = false;
            
            // See if there is a var that is the same.
            for(String setElem : set) {
                
                // Skip empty elements.
                if (setElem.isEmpty()) {
                    continue;
                }
                
                if (subElem.equals(setElem)) {
                    isInSet = true;
                }
            }
            
            // If one word is not in vars, 
            if (!isInSet) {
                return false;
            }
        }
        
        return true;
    }
    
    public static void main(String[] args) {
        dbReader reader = new dbReader();
        try {
            reader.readDb();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(dbReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        reader.findQueryVar("Age", new String[] {"Disease", "Sick"});
        //reader.isSubset(new String[] {"what"}, new String[] {"what", "isWhat"});

    }
}
