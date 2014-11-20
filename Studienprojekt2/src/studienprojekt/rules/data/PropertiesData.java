/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studienprojekt.rules.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Sebastian
 */
public class PropertiesData extends Data {
    
    //private Node node;
    protected Map<String, String> values;
    
    
    public PropertiesData(Node node) {
        super(node.getNodeName());
        //this.node = node;
        this.values = new HashMap();
        
        NodeList childNodes = node.getChildNodes();
        
        for(int i = 0; i < childNodes.getLength(); i++) {
            Node currentNode = childNodes.item(i);
            this.values.put(currentNode.getNodeName(), currentNode.getTextContent());
        }
        
        //System.out.println(this);
    }
    
    public boolean propertyExists(String key) {
        return this.values.containsKey(key);
    }
    
    public String getProperty(String key) {
        return this.values.get(key);
    }
    
    @Override
    public final String toString() {
        String result = this.name + ":\n";
        for(Entry<String, String> entry : this.values.entrySet()) {
            if(!entry.getKey().equalsIgnoreCase("#comment") && !entry.getKey().equalsIgnoreCase("#text"))
                result += entry.getKey() + ":" + entry.getValue() + "\n";
        }
        
        return result;
    }
    
}
