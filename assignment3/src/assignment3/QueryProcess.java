package assignment3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Jeroen van Hoof
 * @author Theodoros Margomenos
 */
public class QueryProcess {

    /**
     * Just get the query and stuff. What do we do with it? Save it to a map?
     * Return something...
     */
    public Query readQuery() {

        // Initialize map for putting in the query parts.
        Map map = new HashMap();

        // Get the query from input.
        String query = readQueryLine();

        // Determine the query variable.
        String queryVar = query.substring(2, query.indexOf("|"));

        Query result;

        // Find all var-value pairs.
        int comma = query.indexOf("|");
        String pair, var, val;
        while (true) {

            // Find next comma.
            int nextComma = query.indexOf(",", comma + 1);

            // Check if there is a next comma.
            if (nextComma != -1) {

                // Output the var-value pair.
                pair = query.substring(comma + 1, nextComma);

                // Move to the next comma.
                comma = nextComma;

                // We reached the end.
            } else {

                // Output the var-value pair.
                pair = query.substring(comma + 1, query.indexOf(")"));
            }

            // Get variable.
            var = pair.substring(0, pair.indexOf("="));

            // Get value.
            val = pair.substring(pair.indexOf("=") + 1);
            
            // Put to map.
            map.put(var, val);
            System.out.println("var: " + var);
            System.out.println("val: " + val);
            
            if (nextComma == -1) {
                break;
            }
        }
        result = new Query(queryVar, map);

        System.out.println("Your query was: " + pair);
        return result;
    }
    
    public void getTable(Query query) throws FileNotFoundException {
        
        // Create scanner for database.
        Scanner sc = new Scanner(new File("src/resources/spiegelhalter.txt"));
        
        // Store text in array.
        List<String> lines = new ArrayList<>();
        int i = 0;
        while (sc.hasNextLine()) {
            String curLine = sc.nextLine();
            lines.add(curLine);
            System.out.println(curLine);
            i++;
        }
        
        // Initialize line number.
        int lineNr = -1;
        
        // Find the line of query variable.
        //int start = findQueryVar();
        
    }
    
    // make a new method that is going to return a boolean
    public boolean checkTAble() {
        
        // Check if the condtions are all at (isSubset) first column
        // Then true otherwise false
        // checktable --> true then use method and otherwise use chain rule.
        
        return false;
    }
    
    
    /**
     * Finds and prints the line at which the query variable and corresponding
     * condition variables are.
     * 
     * @param queryVar the query variable
     * @param vars the condition variables
     * @return the line number of the header of the table we need
     */
//    public int findQueryVar(String queryVar, String[] vars) {
//        int lineNr = 0;
//        
//        for(String line : this.database) {
//            
//            // Check if line contains queryVar.
//            if (line.indexOf(queryVar) != -1) {
//                // Break down per word.
//                String[] words = line.split(" ");
//                               
//                // Check if every word is a condition in the input.
//                if (isSubset(words, vars)) {
//                    System.out.println(line);
//                    return lineNr;
//                }
//            }
//            lineNr++;
//        }
//        
//        return -1;
//    }    

    /**
     * Scans the query.
     *
     * @return the raw query from input.
     */
    public String readQueryLine() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your query:");
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

    }
    
    public void printQuery(Query query) {
        System.out.println("Your query variable is: " + query.getQueryVar());
        System.out.println("Your conditions are: " + query.getConditions());        
    }

    public static void main(String[] args) throws FileNotFoundException {
        QueryProcess proc = new QueryProcess();
        Query query = proc.readQuery();
        proc.printQuery(query);
        proc.getTable(query);
    }
}
