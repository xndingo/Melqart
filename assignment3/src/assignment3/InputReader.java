package assignment3;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * InputReader
 * 
 * Reads the input and stores the variables.
 * Takes as input for example P(Disease | BirthAphyxia = yes),
 * and stores the queryVar and maps the conditions. These are retrievable via
 * getQueryVar() and getConditions().
 * 
 * 
 * @author Theodoros Margomenos
 * @author Jeroen van Hoof
 */
public class InputReader {
      
    // Queryvar
    private String queryVar;
     
    // Conditions
    private Map conditions;
    
    /**
     * Constructor.
     */
    public InputReader() {
        queryVar = "";
        conditions = new HashMap();
    }
    
    /**
     * Gets the queryVar.
     * 
     * @return queryVar
     */
    public String getQueryVar() {
        return queryVar;
    }
    
    /**
     * Gets the conditions.
     * 
     * @return conditions
     */
    public Map getConditions() {
        return conditions;
    }
    
    
    /**
     * Scans the query.
     * 
     * @return the raw query from input.
     */
    public String readQueryLine() {
        Scanner sc = new Scanner(System.in);
        System.out.print("> ");
        
        // Ignore spaces
        return sc.nextLine().replaceAll(" ", "");
    }

    /**
     * Accepts a string with 'var=value' and adds it to the map.
     * 
     * @param pair the var-value string
     */
    public void addToMap(String pair) {
        
        // Get variable.
        String var = pair.substring(0, pair.indexOf("="));
        
        // Get value.
        String val = pair.substring(pair.indexOf("=") + 1);
        
        // Debugging.
        System.out.println(var + ", " + val);
        
        // Add condition.
        conditions.put(var, val);
    }
    
    /**
     * Processes the input query.
     * 
     * Reads the input in the form of 
     *      "P("{QueryVar}"|"({Var=value}", ")^(n-1){Var=value}")"
     */
    public void processQuery() {
        // Get the query from input.
        String query = readQueryLine();
               
        // Determine the query variable.
        this.queryVar = query.substring(2, query.indexOf("|"));
        
        // Find all var-value pairs.
        int comma = query.indexOf("|");
        
        while (true) {
            
            // Find next comma.
            int nextComma = query.indexOf(",", comma + 1);
            
            // Check if there is a next comma.
            if (nextComma != -1){
                
                // Output the var-value pair.
                String pair = query.substring(comma + 1, nextComma);
                addToMap(pair);
                
                // Move to the next comma.
                comma = nextComma;
                
            // We reached the end.
            } else {
                
                // Output the var-value pair.
                String pair = query.substring(comma + 1, query.indexOf(")"));
                addToMap(pair);
                
                // Break the loop.
                break;
            }
        }
    }    
}