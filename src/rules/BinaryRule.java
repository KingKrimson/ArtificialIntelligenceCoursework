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
public class BinaryRule extends Rule {

    public BinaryRule(String condition, String action) {
        super(condition, action);
    }

    public BinaryRule(String completeRule) {
        super(completeRule);
    }

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
    
    @Override
    public boolean testCondition(List<Double> testList) {
        throw new RuntimeException("testcondition(List<Double>) not implemented for BinaryRule.");
    }

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
