// P(HypDistrib | DuctFlow=Lt_to_Rt) should return false
// Printed line before it should equal the line before that
// Fix checkConditions and do something with the String-cast stuff.
// The problem is in the getTables.

/* P(CO2|LungParench=Normal)
P(LungParench | Disease=PFC)
P(HypDistrib | DuctFlow=Lt_to_Rt) */

package assignment3;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
    static private Map<String, String> conditions;

    public QueryProcess() throws FileNotFoundException {
        Query query = readQuery();
        queryVar = query.getQueryVar();
        conditions = query.getConditions();
        printQuery(query);
        List tables = getTables(query);
        System.out.println(tables.get(0));
        if (checkConditions((String[]) tables.get(0))){
            System.out.println("true");
        } else {
            System.out.println("false");
        }
        boolean check = checkConditions(new String[]{
            "HypDistrib DuctFlow CardiacMixing",
            "Equal      Lt_to_Rt None          0.95",
            "Unequal    Lt_to_Rt None          0.05",
            "Equal      None     None          0.95",
            "Unequal    None     None          0.05",
            "Equal      Rt_to_Lt None          0.05",
            "Unequal    Rt_to_Lt None          0.95",
            "Equal      Lt_to_Rt Mild          0.95",
            "Unequal    Lt_to_Rt Mild          0.05",
            "Equal      None     Mild          0.95",
            "Unequal    None     Mild          0.05",
            "Equal      Rt_to_Lt Mild          0.5",
            "Unequal    Rt_to_Lt Mild          0.5", 
            "Equal      Lt_to_Rt Complete      0.95",
            "Unequal    Lt_to_Rt Complete      0.05",
            "Equal      None     Complete      0.95",
            "Unequal    None     Complete      0.05",
            "Equal      Rt_to_Lt Complete      0.95",
            "Unequal    Rt_to_Lt Complete      0.05",
            "Equal      Lt_to_Rt Transp.       0.95",
            "Unequal    Lt_to_Rt Transp.       0.05",
            "Equal      None     Transp.       0.95",
            "Unequal    None     Transp.       0.05",
            "Equal      Rt_to_Lt Transp.       0.5",
            "Unequal    Rt_to_Lt Transp.       0.5",
        });
        
        //System.out.println(check);
        
//        getNumAnswers(new String[]{
//            "HypDistrib DuctFlow CardiacMixing",
//            "Equal      Lt_to_Rt None          0.95",
//            "Unequal    Lt_to_Rt None          0.05",
//            "Equal      None     None          0.95",
//            "Unequal    None     None          0.05",
//            "Equal      Rt_to_Lt None          0.05",
//            "Unequal    Rt_to_Lt None          0.95",
//            "Equal      Lt_to_Rt Mild          0.95",
//            "Unequal    Lt_to_Rt Mild          0.05",
//            "Equal      None     Mild          0.95",
//            "Unequal    None     Mild          0.05",
//            "Equal      Rt_to_Lt Mild          0.5",
//            "Unequal    Rt_to_Lt Mild          0.5", 
//            "Equal      Lt_to_Rt Complete      0.95",
//            "Unequal    Lt_to_Rt Complete      0.05",
//            "Equal      None     Complete      0.95",
//            "Unequal    None     Complete      0.05",
//            "Equal      Rt_to_Lt Complete      0.95",
//            "Unequal    Rt_to_Lt Complete      0.05",
//            "Equal      Lt_to_Rt Transp.       0.95",
//            "Unequal    Lt_to_Rt Transp.       0.05",
//            "Equal      None     Transp.       0.95",
//            "Unequal    None     Transp.       0.05",
//            "Equal      Rt_to_Lt Transp.       0.5",
//            "Unequal    Rt_to_Lt Transp.       0.5",
//        });
    }

    
    
    /**
     * Just get the query and stuff. What do we do with it? Save it to a map?
     * Return something...
     */
    public Query readQuery() {

        // Initialize map for putting in the query parts.
        Map<String, String> map = new HashMap<String, String>();

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

        return result;
    }

    public int getNumAnswers(String[] lines) {
        String first = lines[1].substring(0, lines[1].indexOf(" "));
       
        int answers = 0;
        
        for (int i = 2; i < lines.length; i++) {
            int end = lines[i].indexOf(" ");
            String current = lines[i].substring(0, end);
            answers++;
            if (current.equals(first)) {
                return answers;
            }
        }
        return answers;
    }
    
    public boolean checkConditions(String[] lines) {
        for(String condition : conditions.keySet()) {
            if (!lines[0].contains(condition)){
                return false;
            }
        }
        return true;
    }

    public List getTables(Query query) throws FileNotFoundException {
        List data = readData();
        List<Integer> lineNums = getAllLineNums(data);
        System.out.println(lineNums);
        
        // List of arrays
        List tables = seperateTables(data, lineNums);

        for (int i = 0; i < tables.size(); i++) {
             System.out.println(Arrays.deepToString((Object[]) tables.get(i)));
        }
        
        return tables;
    }
   
    public List seperateTables(List<String> data, List<Integer> lineNums) {
        List tables = new ArrayList<String>();
        
        for (int k = 0; k < lineNums.size(); k++) {
            int start = lineNums.get(k) - 1;
            int i = start;
            
            // Increase i until we find an empty line.
            while (!"".equals(data.get(i))) {
                i++;    
            }
            
            // Set the end-point of the table.
            int end = i;
            
            System.out.println("start = " + start);
            System.out.println("end = " + end);
            
            // Print lines
            String[] entry = new String[end-start];
            for (int line = start; line < end; line++) {
                entry[line-start] = data.get(line);
                System.out.println(data.get(line));
            }
            
            for (String ent : entry) {
                System.out.println(ent);
            }
            
            // Add to the table
            tables.add(entry);
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
        System.out.println(firstColLineNum);
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
                lineNum = line + 1;
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
                lineNums.add(line + 1);
            }
        }
        return lineNums;
    }

    // make a new method that is going to return a boolean
    public boolean checkTable() {

        return false;
    }

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
