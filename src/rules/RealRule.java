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
 *
 * @author Andrew
 */
public class RealRule extends Rule {
    private List<Double> dConditions;
    private double dAction;
    
    public RealRule(List<Double> completeRule) {
        super();
        dAction = completeRule.get(completeRule.size()-1);
        dConditions = completeRule.subList(0, completeRule.size()-1);
        
        action = String.valueOf(dAction);
        condition = dConditions.toString();
    }
    
    @Override
    public boolean testCondition(String testString) {
        throw new RuntimeException("testCondition(String) not implemented for RealRule.");
    }
    
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
    
    
    
    @Override
    public int compareTo(Rule otherRule) {
        return 1;
    }

}
