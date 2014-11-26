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
public class RealRule extends Rule {

    public RealRule(String condition, String action) {
        super(condition, action);
    }
    
    public RealRule(String completeRule) {
        this.condition = completeRule.substring(0, completeRule.length()-1).trim();
        this.action = completeRule.trim().substring(completeRule.length()-1, completeRule.length());
    }
    
    @Override
    public boolean testCondition(String testString) {
        String [] conditions = condition.split(" +");
        String [] tests = testString.split(" +");
        
        if (tests.length != conditions.length * 2) {
            return false;
        }
        for (int i = 0; i < tests.length; i++) {
            double test = Double.parseDouble(tests[i]);
            double firstCondition = Double.parseDouble(conditions[i*2]);
            double secondCondition = Double.parseDouble(conditions[(i*2)+1]);
            
            if ((int)firstCondition >= 100) {
                if (test > secondCondition) {
                    return false;
                }
            } else if ((int)secondCondition >= 100) {
                if (test < firstCondition) {
                    return false;
                }
            } else if (!((int)firstCondition >= 100 && (int)secondCondition >= 100)) {
                if (test < firstCondition || test > secondCondition) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public int compareTo(Rule otherRule) {
        return 1;
    }

}
