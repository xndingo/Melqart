package assignment3;

import static java.lang.Double.parseDouble;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * InputReader
 * 

 * 
 * @author Theodoros Margomenos
 * @author Jeroen van Hoof
 */
public class InputReader {
    
    // Map var-value pairs.
    private Map mapQ;
    
    // Map QueryVar-vars.
    
    public InputReader() {
        mapQ = new HashMap();
    }
    
    
    /**
     * Scans the query.
     * 
     * @return the raw query from input.
     */
    public String getQueryLine() {
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
        String var = pair.substring(0, pair.indexOf("="));
        Double val = parseDouble(pair.substring(pair.indexOf("=") + 1));
        System.out.println(var + ", " + val);
        mapQ.put(var, val);
    }
    
    /**
     * Processes the input query.
     * 
     * Reads the input in the form of 
     *      "P("{QueryVar}"|"({Var=value}", ")^(n-1){Var=value}")"
     * with an optional comma at the end to add more queries.
     */
    public void processQuery() {
        // Get the query from input.
        String query = getQueryLine();
        
        // If last character is a comma, make sure to read next.
        boolean finished = !",".equals(query.substring(query.length() - 1));
        query = query.substring(0, query.length() - 1);
        
        // Determine the query variable.
        String queryVar = query.substring(2, query.indexOf("|"));
        
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
                String pair = query.substring(comma + 1);
                addToMap(pair);
                
                // Break the loop.
                break;
            }
        }
        
        if (!finished) {
            processQuery();
        }
    }    
    
    
    
     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InputReader read = new InputReader();
        read.processQuery();
    }
}
