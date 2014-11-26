/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rules;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Andrew
 */
public abstract class RuleSet {
    protected ArrayList<Rule> ruleSet;
    
    public RuleSet() {
        ruleSet = new ArrayList<>();
    }
    
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

}
