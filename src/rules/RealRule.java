/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A real rule. This takes a list of doubles for the condition, instead of a String.
 * Each condition contains two values between 0.0 and 1.0, which create a range.
 * an action can either be 1.0 or 0.0.
 * 
 * A given input matches a condition if it falls in the range of the two values
 * in a condition.
 * 
 * @author Andrew
 */
public class RealRule extends Rule {
    private List<Double> dConditions;
    private double dAction;
    
    /**
     * Create a rule from a list of doubles.
     * @param completeRule
     */
    public RealRule(List<Double> completeRule) {
        super();
        dAction = completeRule.get(completeRule.size()-1);
        dConditions = completeRule.subList(0, completeRule.size()-1);
        
        action = String.valueOf(dAction);
        condition = dConditions.toString();
    }
    
    /**
     * Lazy way of stopping the user from calling the wrong test function.
     * @param testString
     * @return
     */
    @Override
    public boolean testCondition(String testString) {
        throw new RuntimeException("testCondition(String) not implemented for RealRule.");
    }
    
    /**
     * test a list of inputs against the conditions. An input matches a condition
     * if it falls in the range created by the two elements of the condition.
     * 
     * if all inputs match all conditions, then the action is assumed to be valid.
     * 
     * @param testList
     * @return
     */
    @Override
    public boolean testCondition(List<Double> testList) {
      
        if (testList.size() * 2 != dConditions.size()) {
            return false;
        }
        
        for (int i = 0; i < testList.size(); i++) {
            double test = testList.get(i);
            List<Double> localConditions = dConditions.subList((i*2), (i*2)+2);
            Collections.sort(localConditions);
            if (test < localConditions.get(0) || test > localConditions.get(1)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * return a human readable version of the rule.
     * @return
     */
    @Override
    public String ruleRepresentation() {
        String ruleString = "";
        for (int i = 0; i < dConditions.size(); i = i+2) {
            List<Double> localConditions = dConditions.subList(i, i+2);
            Collections.sort(localConditions);
            ruleString += localConditions.get(0) + "-" + localConditions.get(1);
            if ((i+1) != dConditions.size() - 1) {
                ruleString += " and ";
            }
        }
        ruleString += " = " + dAction;
        return ruleString;
    }
    
    
    
    @Override
    public int compareTo(Rule otherRule) {
        return 1;
    }

}
