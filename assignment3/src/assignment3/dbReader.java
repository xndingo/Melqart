/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package assignment3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 * @author Theodoros Margomenos
 * @author Jeroen van Hoof
 */
public class dbReader {
    
    private String[] database;
    
    /**
     * Gets the database.
     * 
     * @return database string array
     */
    public String[] getDatabase() {
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
        Scanner sc = new Scanner(new File("resources/spiegelhalter.txt"));
        
        // Store text in array.
        String[] lines = {};
        int i = 0;
        while (sc.hasNextLine()) {
            lines[i] = sc.nextLine();
            i++;
        }
        
        // Store lines.
        this.database = lines;
    }
}
