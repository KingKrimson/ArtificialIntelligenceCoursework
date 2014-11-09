/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rules;

/**
 *
 * @author Andrew
 */
public class Rule implements Comparable<Rule> {
    private String condition;
    private String action;

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
    public int compareTo(Rule otherRule) {
        final int LESS = -1;
        final int EQUAL = 0;
        final int MORE = 1;    
        
        int myWildcards = 0;
        int otherWildcards = 0;
        
        for (char character : this.action.toCharArray()) {
            if (character == '2') {
                myWildcards++;
            }
        }
        
        for (char character : otherRule.action.toCharArray()) {
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
