/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studienprojekt.deprecated;

import studienprojekt.deprecated.Data;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import studienprojekt.deprecated.PropertiesData;
/**
 *
 * @author Sebastian
 */
public class TagsData extends Data {

    //private Node node;
    protected List<PropertiesData> tags;
    
    
    public TagsData(Node node) {
        super(node.getNodeName());
        //this.node = node;
        this.tags = new ArrayList();
        
        NodeList childNodes = node.getChildNodes();
        
        for(int i = 0; i < childNodes.getLength(); i++) {
            this.tags.add(new PropertiesData(childNodes.item(i)));
        }
        
        System.out.println(this);
    }
    
    public List<PropertiesData> getTags() {
        return this.tags;
    }
    
    @Override
    public final String toString() {
        String result = this.name + ":\n";
        for(PropertiesData tag : this.tags) {
            //if(!this.name.equals("#text") && !this.name.equals("#comment:"))
                result += tag + "-------------------\n";
        }
        
        return result;
    }
    

    
}
