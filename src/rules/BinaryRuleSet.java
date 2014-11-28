/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rules;

import java.util.ArrayList;

/**
 *
 * @author Andrew
 */
public class BinaryRuleSet extends RuleSet {

    public BinaryRuleSet(String ruleSetString, int ruleLength) {
        super();
        ruleSet = new ArrayList<>();
        for (int i = 0; i < ruleSetString.length(); i += ruleLength) {
            ruleSet.add(new BinaryRule(ruleSetString.substring(i, i + ruleLength)));
        }
    }
}
