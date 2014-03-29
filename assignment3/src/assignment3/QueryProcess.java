package assignment3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Jeroen van Hoof
 * @author Theodoros Margomenos
 */
public class QueryProcess {

    static private String queryVar;
    static private Map conditions;

    public QueryProcess() throws FileNotFoundException {
        Query query = readQuery();
        queryVar = query.getQueryVar();
        conditions = query.getConditions();
        printQuery(query);
        getTables(query);
    }

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

    public List getTables(Query query) throws FileNotFoundException {
        List data = readData();
        List<Integer> lineNums = getAllLineNums(data);
        List tables = seperateTables(data, lineNums);
        return tables;

    }

    public int[] toIntArray(List<Integer> list) {
        int[] result = new int[list.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = list.get(i);
        }
        return result;
    }
    
    public String[] toStringArray(List<String> list) {
        String[] result = new String[list.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = list.get(i);
        }
        return result;
    }    
    
    public String[] makeArrayRow(int start, int end, List<String> data) {
        return toStringArray(data.subList(start, end));
    }

    public List seperateTables(List<String> data, List<Integer> lineNums) {
        List tables = new ArrayList();
        
        // Convert to int array.
        int[] lines = toIntArray(lineNums);
        
        // Find start and end
        for (int start : lines) {
            
            // Begin iterating at start (header of table).
            int i = start;
            
            // Increase i until we find an empty line.
            while (!"".equals(data.get(i))) {
                i++;                
            }
            
            // Set the end-point of the table.
            int end = i;
            
            // Add to the table
            tables.add(makeArrayRow(start, end, data));
        }
        
        //  lineNrs.
        return tables;
    }

    public List readData() throws FileNotFoundException {
        // Create scanner for database.
        Scanner sc = new Scanner(new File("src/resources/spiegelhalter.txt"));

        // Store text in array.
        List<String> lines = new ArrayList<>();
        while (sc.hasNextLine()) {
            String curLine = sc.nextLine();
            lines.add(curLine);
            System.out.println(curLine);
        }
        return lines;
    }

    /**
     * Returns a list with all the line numbers where the query variable is
     * found. The first element in the list is where the query variable is found
     * in the first column and all the others are where it's found at another
     * column but the first. If the query variable can't be found in the first
     * column of a table in the data set then this returns an empty list.
     *
     * @param lines
     * @return
     */
    public List getAllLineNums(List<String> lines) {
        int firstColLineNum = getLine(lines);
        List otherColLineNums = getLines(lines);
        List lineNums = new ArrayList();
        if (firstColLineNum == -1) {
            return lineNums; //empty list cause no first column with queryVar.
        }
        lineNums.add(firstColLineNum);
        for (int i = 0; i < otherColLineNums.size(); i++) {
            lineNums.add(otherColLineNums.get(i));
        }
        return lineNums;
    }

    /**
     * Gets the line number where the query variable is in the first column.
     *
     * @param lines
     * @return
     */
    public int getLine(List<String> lines) {
        int lineNum = -1; // line containing the query variable
        // finds the line with the query variable in the first column
        for (int line = 0; line < lines.size(); line++) {
            if (lines.get(line).startsWith(queryVar)) {
                lineNum = line;
                break; // found it so break out
            }
        }
        return lineNum;
    }

    /**
     * Returns a list of all the line indexes where the query variable exist in
     * those lines and isn't in the starting string of those lines.
     *
     * @param lines
     * @return
     */
    public List getLines(List<String> lines) {
        List lineNums = new ArrayList();
        // finds the line with the query variable in the first column
        for (int line = 0; line < lines.size(); line++) {
            if (!lines.get(line).startsWith(queryVar)
                    && lines.get(line).contains(queryVar)) {
                lineNums.add(line);
            }
        }
        return lineNums;
    }

    // make a new method that is going to return a boolean
    public boolean checkTable() {

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
    }
}
