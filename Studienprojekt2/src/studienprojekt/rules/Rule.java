package studienprojekt.rules;

import studienprojekt.rules.data.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import studienprojekt.osm.OSMCoordinate;
import studienprojekt.osm.OSMWay;


public class Rule 
{
    
    private Map<String, String> properties;
    private ArrayList<Tag> tagList = new ArrayList<Tag>();
    
	public Rule()
	{
	}
	
	public void addProperty(String key, String value)
	{
		properties.put(key, value);
	}
        
        public List<OSMWay> handle(OSMCoordinate coordinate) {
            return null;
        }
        
    
    public String getValueOfProperty(String key)
    {
    	return properties.get(key); 
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
    
}
