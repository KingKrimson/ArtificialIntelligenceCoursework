/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rules;

import java.util.List;

/**
 * Binary rule. each rule has a set of conditions, which can contain 0, 1, or 2
 * (a wildcard that matches either 0 or 1). If a given input value matches the
 * conditions, then the action (1 or 0) can be assumed to be valid.
 * @author Andrew
 */
public class BinaryRule extends Rule {

    /**
     *
     * @param condition
     * @param action
     */
    public BinaryRule(String condition, String action) {
        super(condition, action);
    }

    /**
     *
     * @param completeRule
     */
    public BinaryRule(String completeRule) {
        super(completeRule);
    }

    /**
     * test the given string against the inputs given in the test String.
     * if the inputs match, return the true.
     * Return 
     * @param testString
     * @return
     */
    @Override
    public boolean testCondition(String testString) {
        if (testString.length() != condition.length()) {
            return false;
        }
        for (int i = 0; i < testString.length(); i++) {
            char testChar = testString.charAt(i);
            char conditionChar = condition.charAt(i);
            if (testChar != conditionChar && conditionChar != '2') {
                return false;
            }
        }

        return true;
    }
    
    /**
     * Lazy way of stopping the user from calling the wrong test function.
     * @param testList
     * @return
     */
    @Override
    public boolean testCondition(List<Double> testList) {
        throw new RuntimeException("testcondition(List<Double>) not implemented for BinaryRule.");
    }
    
    /**
     * return a human readable version of the rule
     * @return
     */
    @Override
    public String ruleRepresentation() {
        return condition + " = " + action;
    }
    
    // Sort rules based on how many wildcards they have.
    @Override
    public int compareTo(Rule otherRule) {
        final int LESS = -1;
        final int EQUAL = 0;
        final int MORE = 1;

        int myWildcards = 0;
        int otherWildcards = 0;

        for (char character : this.condition.toCharArray()) {
            if (character == '2') {
                myWildcards++;
            }
        }

        for (char character : otherRule.condition.toCharArray()) {
            if (character == '2') {
                otherWildcards++;
            }
        }

        if (myWildcards < otherWildcards) {
            return LESS;
        } else if (myWildcards == otherWildcards) {
            return EQUAL;
        } else {
            return MORE;
        }
    }
}
