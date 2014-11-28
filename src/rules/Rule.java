/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rules;

import java.util.List;

/**
 *
 * @author Andrew
 */
public abstract class Rule implements Comparable<Rule> {
    protected String condition;
    protected String action;

    public Rule() {
        
    }
    
    public Rule(String condition, String action) {
        this.condition = condition;
        this.action = action;
    }
    
    public Rule(String completeRule) {
        this.condition = completeRule.substring(0, (completeRule.length() - 1)).trim();
        this.action = completeRule.substring(completeRule.length()-1);
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    
    public abstract boolean testCondition(String testString);
    
    public abstract boolean testCondition(List<Double> testList);
    
    @Override
    public abstract int compareTo(Rule otherRule);
}
