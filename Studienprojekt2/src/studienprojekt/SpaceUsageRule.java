package studienprojekt;

import org.apache.commons.lang3.StringUtils;

public class SpaceUsageRule {
    
    private String rule;
    private String value;
    
    public SpaceUsageRule(String rule, String value) {
        this.rule = rule;
        this.value = value;
    }
    
    public static SpaceUsageRule parseSpaceUsageRule(String string) {
        String[] splitted = string.split("=");

        return new SpaceUsageRule(splitted[0], StringUtils.strip(splitted[1], "\""));
    }

    /**
     * @return the rule
     */
    public String getRule() {
        return rule;
    }

    /**
     * @param rule the rule to set
     */
    public void setRule(String rule) {
        this.rule = rule;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }   
}
