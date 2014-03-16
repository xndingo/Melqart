/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package assignment3;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Theodoros Margomenos
 * @author Jeroen van Hoof
 */
public class dataProcess {
    
    /**
     * Asks the user for a query variable and returns it.
     * 
     * 
     * @return {@code queryVar}
     */
    public String askQuery() {
        Scanner in = new Scanner(System.in);
        String queryVar;
        System.out.println("Enter query variable: ");
        queryVar = in.nextLine();
        return queryVar;
    }

    /**
     * Asks the user to input variables and their probabilities and returns
     * a map that maps the variables with their respective probabilities.
     *
     * @return {@code vars}
     */
    public Map<String, Float> askVar() {
        String varName;
        Float value;
        Map<String, Float> vars = new HashMap<>();
        Scanner in = new Scanner(System.in);
        System.out.println("Enter a random variable: ");
        varName = in.nextLine();
        System.out.println("Enter a value for it: ");
        value = in.nextFloat();
        vars.put(varName, value);
        boolean loop = true;
        Scanner sc = new Scanner(System.in);
        while (loop) {            
            System.out.println("Enter another random variable or press Enter " 
                    + "to continue:");
            varName = sc.nextLine();
            if(varName.equals("")) {
                break;
            }            
            System.out.println("Enter a value for it: ");
            value = in.nextFloat();
            vars.put(varName, value);            
        }        
        return vars;        
    }
    
    /**
     * Prints the query.
     */
    public void printQuery(String queryVar, Map<String, Float> vars) {
        System.out.print("P( " + queryVar + " | ");
        Set<String> keySet = vars.keySet();
        Iterator<String> keySetIterator = keySet.iterator();
        String key = keySetIterator.next();
        System.out.print(key + " = " + vars.get(key) + " ");        
        while (keySetIterator.hasNext()) {    
            key = keySetIterator.next();
            System.out.print(", " + key + " = " + 
                    vars.get(key) + " ");         
        }
        System.out.print(")");
    }
    
    
}
