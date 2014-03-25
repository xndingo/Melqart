/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package assignment3;

import java.util.Map;

/**
 *
 * @author jvhoof
 */
public class Query {
    private String queryVar;
    private Map conditions;
    
    public Query(String query, Map map) {
        this.queryVar = query;
        this.conditions = map;
    }
    
    public String getQueryVar() {
        return queryVar;
    }
    
    public Map getConditions(){
        return conditions;
    }
}
