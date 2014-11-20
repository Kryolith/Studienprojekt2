package studienprojekt;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import studienprojekt.osm.OSMCoordinate;
import studienprojekt.osm.OSMMap;
import studienprojekt.osm.OSMWay;
import studienprojekt.rules.Rule;
import studienprojekt.rules.RuleParser;

public class RuleManager {
    
    private Map<String, Rule> rules;
    private RuleParser ruleParser;
    
    public RuleManager() {
        this.rules = new HashMap();
        this.ruleParser = new RuleParser();
    }
    
    public List<OSMWay> handle(OSMMap map, OSMCoordinate coordinate, SpaceUsageRule sur) throws Exception {
        
        // SpaceUsageRule zu einzigartigen "Key" umwandeln
        String ruleFilename = getRuleFilenameFromSUR(sur);
        System.out.println(ruleFilename);
        // Schauen ob diese Rule schon geparst wurde
        Rule rule = null;
        
        if(rules.containsKey(ruleFilename)) {
            // Wenn ja lade diese aus der Map
            rule = rules.get(ruleFilename);
        }else{
            // Ansonsten parse sie neu
            try {
            rule = RuleParser.parseFile(new File("rules/" + ruleFilename));
            } catch(ParserConfigurationException | SAXException | IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                System.out.println(e);
            }
        }
        
        return rule.handle(map, coordinate, sur);
    }
    
    public String getRuleFilenameFromSUR(SpaceUsageRule sur) {
        String result = "";
        
        result += sur.getRule().replace(":", "-");
        result += "_";
        result += sur.getValue();
        result += ".xml";
        
        return result;
    }
    
}
