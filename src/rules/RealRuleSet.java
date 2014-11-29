/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rules;

import java.util.ArrayList;
import java.util.List;

/**
 * A ruleSet containing real valued numbers.
 * 
 * @author Andrew
 */
public class RealRuleSet extends RuleSet {

    /**
     * Construct a ruleSet consisting of real rules out of the given list of 
     * doubles. Each rule is ruleLength long.
     * 
     * @param ruleSetDoubles
     * @param ruleLength
     */
    public RealRuleSet(List<Double> ruleSetDoubles, int ruleLength) {
        super();
        ruleSet = new ArrayList<>();
                
        for (int i = 0; i < ruleSetDoubles.size(); i += ruleLength) {
            List<Double> rule = ruleSetDoubles.subList(i, i+ruleLength);
            ruleSet.add(new RealRule(rule));
        }
    }
}
