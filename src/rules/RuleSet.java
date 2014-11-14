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
public class RuleSet {
    private ArrayList<Rule> ruleSet;
    
    public RuleSet(String RuleSet, int ruleLength) {
        ruleSet = new ArrayList<>();
        for (int i = 0; i < RuleSet.length(); i += ruleLength) {
            ruleSet.add(new Rule(RuleSet.substring(i, i + ruleLength)));
        }
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
