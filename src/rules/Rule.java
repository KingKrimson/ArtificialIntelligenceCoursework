/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rules;

import java.util.List;

/**
 * abstract class that a given Rule must implement.
 * 
 * @author Andrew
 */
public abstract class Rule implements Comparable<Rule> {

    /**
     * The condition of the rule.
     */
    protected String condition;

    /**
     * The action of the rule that's output if the condition is met.
     */
    protected String action;

    /**
     *
     */
    public Rule() {
        
    }
    
    /**
     * create a rule with the given condition and action.
     * @param condition
     * @param action
     */
    public Rule(String condition, String action) {
        this.condition = condition;
        this.action = action;
    }
    
    /**
     * create a rule from a whole string
     * @param completeRule
     */
    public Rule(String completeRule) {
        this.condition = completeRule.substring(0, (completeRule.length() - 1)).trim();
        this.action = completeRule.substring(completeRule.length()-1);
    }

    /**
     *
     * @return
     */
    public String getCondition() {
        return condition;
    }

    /**
     *
     * @param condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     *
     * @return
     */
    public String getAction() {
        return action;
    }

    /**
     *
     * @param action
     */
    public void setAction(String action) {
        this.action = action;
    }
    
    /**
     * test the condition with a String (for the binary rule).
     * @param testString
     * @return
     */
    public abstract boolean testCondition(String testString);
    
    /**
     * test the condition with a list of doubles (for the real rule)
     * @param testList
     * @return
     */
    public abstract boolean testCondition(List<Double> testList);
    
    /**
     * return a human readable version of the rule.
     * @return
     */
    public abstract String ruleRepresentation();
    
    @Override
    public abstract int compareTo(Rule otherRule);
}
