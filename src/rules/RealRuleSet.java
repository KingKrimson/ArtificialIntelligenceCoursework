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
public class RealRuleSet extends RuleSet {
    public RealRuleSet(String RuleSet, int ruleLength) {
        super();
        ruleSet = new ArrayList<>();
        String[] rules = RuleSet.split(" +");
                
        for (int i = 0; i < rules.length; i += ruleLength) {
            String rule = "";
            for (int j = i; j < ruleLength+i; j++) {
                rule += rules[j] + " ";
            }
            ruleSet.add(new RealRule(rule.trim()));
        }
    }
}
