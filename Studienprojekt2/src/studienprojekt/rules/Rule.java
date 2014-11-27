package studienprojekt.rules;

import studienprojekt.rules.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import studienprojekt.Configuration;
import studienprojekt.osm.OSMCoordinate;
import studienprojekt.osm.OSMWay;


public class Rule 
{
    private Configuration config;
    private ArrayList<Tag> tagList;
    
    public Rule()
    {
        this.tagList = new ArrayList<>();
    }
    
    public Configuration getConfiguration() {
        return this.config;
    }
        
    
    public void setConfiguration(Configuration config){
        this.config = config;
    }
    
    public void addTag(Tag tag)
    {
    	tagList.add(tag);
    }
    
    public Tag getTag(int i)
    {
            return tagList.get(i);
    }
    
    public int sizeTagList()
    {
    	return tagList.size();
    }
    
    public boolean hasTagWithKey(String key) {
        for(Tag tag : tagList) {
            if(tag.getKey().equalsIgnoreCase(key))
                return true;
        }
        return false;
    }
    
    public Tag getTagWithKey(String key) {
        for(Tag tag : tagList) {
            if(tag.getKey().equalsIgnoreCase(key))
                return tag;
        }
        return null;
    }
}
