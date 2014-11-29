/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rules;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class representing a set of rules. Provides functions that allows
 * the user to test an input value against each rule in the set.
 * 
 * @author Andrew
 */
public abstract class RuleSet {

    /**
     *
     */
    protected ArrayList<Rule> ruleSet;
    
    /**
     *
     */
    public RuleSet() {
        ruleSet = new ArrayList<>();
    }
    
    /**
     * test the input string against each rule in the ruleset. Stop if one matches,
     * and return the action of that rule.
     * 
     * @param testString
     * @return
     */
    public String testRuleSet(String testString) {
        String result = null; // If none of the rules match, return null.
        
        for (Rule rule : ruleSet) {
            if (rule.testCondition(testString)) {
                result = rule.getAction();
                break;
            }
        }
        
        return result;
    }
    
    /**
     * test the input list against each rule in the ruleset. Stop if one matches,
     * and return the action of that rule.
     * 
     * @param testlist
     * @return
     */
    public String testRuleSet(List<Double> testList) {
        String result = null; // If none of the rules match, return null.
        
        for (Rule rule : ruleSet) {
            if (rule.testCondition(testList)) {
                result = rule.getAction();
                break;
            }
        }
        
        return result;
    }
    
    /**
     * return a human readable representation of the ruleset.
     * @return
     */
    public String ruleSetRepresentation() {
        String ruleSetString = "";
        for (Rule rule : ruleSet) {
            ruleSetString += rule.ruleRepresentation() + ", ";
        }
        // trim off the remaining comma.
        ruleSetString = ruleSetString.substring(0, ruleSetString.length()-2);
        return ruleSetString;
    }

}
