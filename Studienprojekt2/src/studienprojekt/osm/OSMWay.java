package studienprojekt.osm;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class OSMWay extends OSMElement 
{
	private List<OSMNode> wayComponents = new ArrayList();
	private HashMap tags;
	
        
        public OSMWay()
        {
            super();
        }       
	public OSMWay(String id)
	{
		super(id);	
	}
        
        public Polygon getPolygonRepresentation()
        {
            Polygon polygonRepresentation = new Polygon();
            
            
            
            return polygonRepresentation;
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
        
        public boolean wrapsNode(OSMNode node)
        {
            return false;
        }
        
        public boolean wraps(OSMWay way)
        {
            return false;
        }        
        
        public String getFirstNodeId()
        {
            return wayComponents.get(0).getId();
        } 
        
        public String getLastNodeId()
        {
            return wayComponents.get(wayComponents.size() - 1).getId();
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
