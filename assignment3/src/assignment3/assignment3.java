/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package assignment3;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Theodoros Margomenos
 * @author Jeroen van Hoof
 */
public class assignment3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        dataProcess data = new dataProcess();
        String queryVar = data.askQuery();
        Map<String, Float> vars = data.askVar();
        data.printQuery(queryVar, vars);
        
                
    }
    
}
