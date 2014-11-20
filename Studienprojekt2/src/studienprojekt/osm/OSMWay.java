package studienprojekt.osm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class OSMWay extends OSMElement 
{
	private List<OSMNode> wayComponents = new ArrayList();
	private HashMap tags;
	
	public OSMWay(String id)
	{
		super(id);	
	}

	public List<OSMNode> getWayComponents() 
	{
		return wayComponents;
	}

	public void setWayComponents(List<OSMNode> wayComponents) 
	{
		this.wayComponents = wayComponents;
	}

	public HashMap getTags() 
	{
		return tags;
	}

	public void setTags(HashMap tags) 
	{
		this.tags = tags;
	}
	
        public boolean containsNode(OSMNode node) {
            for(OSMNode tnode : wayComponents) {
                if(tnode.getId().equalsIgnoreCase(node.getId())) {
                    return true;
                }
            }
            
            return false;
        }
        
	@Override
	public String toString()
	{
		String wayDescription = "\n\nWAY WITH ID: " + super.getId() + "\n";
		wayDescription += "Nodes contained within this way:\n";
		String wayNodeIds = "";
		String allTags = "";
		Set<String> tagKeys;
;		
		for (OSMNode wayComp : wayComponents)
		{
			String id = wayComp.getId() 
                                + " " + wayComp.getNodeCoordinate();
			wayNodeIds += "ref = " + id + "\n";
		}
		
		wayDescription += wayNodeIds + "Available Tags for this way:\n";		
		
		if (tags != null)
		{	
			tagKeys = tags.keySet();
			for(String key : tagKeys)
			{
				allTags += " " + key + " = " + tags.get(key) + "\n";
			}
		}
		
		wayDescription += allTags;
		
		return wayDescription;
	}
	
}
