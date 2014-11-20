package studienprojekt;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public final class Configuration {
    
    private Map<String, String> properties;
    private String configPath;
    
    public Configuration() {
        
        this.properties = new HashMap();
        this.configPath = "";        
    }
    
    public boolean has(String key) {
        return this.properties.containsKey(key);
    }
    
    public String get(String key) {
        return this.properties.get(key);
    }
    
    public String get(String key, String defaultValue) {
        return has(key) ? get(key) : defaultValue;
    }
    
    public void load(String filepath) {
        Document doc;
        
        this.configPath = filepath;
        
        File f = new File(this.configPath);
        
        try {
            SAXBuilder builder = new SAXBuilder();
            doc = builder.build(f);
            
            Element root = doc.getRootElement(); 
            
            List<Element> new_properties = root.getChild("properties").getChildren("property");
            
            for(Element property : new_properties) {
                String key = property.getChildText("key");
                String value = property.getChildText("value");
                
                this.properties.put(key, value);
            }

        } catch(JDOMException | IOException e) {
            System.out.println(e);
        }
    }
    
    public void load() {
        load("config/default.xml");
    }
}
