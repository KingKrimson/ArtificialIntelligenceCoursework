/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rules;

import java.util.ArrayList;

/**
 * A ruleset containing binary rules.
 * 
 * @author Andrew
 */
public class BinaryRuleSet extends RuleSet {

    /**
     * Construct a ruleSet consisting of binary rules out of the given String. 
     * Each rule is ruleLength long.
     * 
     * @param ruleSetString
     * @param ruleLength
     */
    public BinaryRuleSet(String ruleSetString, int ruleLength) {
        super();
        ruleSet = new ArrayList<>();
        for (int i = 0; i < ruleSetString.length(); i += ruleLength) {
            ruleSet.add(new BinaryRule(ruleSetString.substring(i, i + ruleLength)));
        }
    }
}
