/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studienprojekt.rules;


import java.io.File;
import java.io.IOException; 
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document; 
import org.jdom2.Element; 
import org.jdom2.JDOMException; 
import org.jdom2.input.SAXBuilder; 
import studienprojekt.Configuration;


/**
 *
 * @author Dawid Wozniok
 * @version 20.11.2014
 */
public class RuleParser 
{
    public static Rule parseFile(File file)
    {
    	Document ruleXML = null; 
        File ruleFile = file;
        Rule ruleObject = new Rule();
        
        try { 
            // Das Dokument erstellen 
            SAXBuilder builder = new SAXBuilder(); 
            ruleXML = builder.build(ruleFile);  

            Element ruleRootElement = ruleXML.getRootElement();  
            
            //Element properties = ruleRootElement.getChild("properties");
            //ArrayList<Element> propertiesChildren = (ArrayList<Element>) properties.getChildren();
            
            Configuration config = new Configuration();
            config.load(ruleXML);
            
            ruleObject.setConfiguration(config);
            
            Element tagList = ruleRootElement.getChild("taglist");
            List<Element> tagListChildren = (List<Element>) tagList.getChildren();
            
            /*for (int i = 0; i < propertiesChildren.size(); i++)
			{
            	String key = propertiesChildren.get(i).getName();
            	String value = propertiesChildren.get(i).getValue();
            	ruleObject.addProperty(key, value);
			}*/
            
            for (int i = 0; i < tagListChildren.size(); i++)
            {
                String key = tagListChildren.get(i).getChild("key").getValue();
                String value = tagListChildren.get(i).getChild("value").getValue();
                int weight = Integer.parseInt(tagListChildren.get(i).getChild("weight").getValue());
                Tag aTag = new Tag(key, value, weight);
                ruleObject.addTag(aTag);
            }


        } catch (JDOMException | IOException e) { 
            System.out.println(e);
        } 
        
    	
		return ruleObject;	
    }
   
}
