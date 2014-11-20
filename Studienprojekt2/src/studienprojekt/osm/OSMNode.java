package studienprojekt.osm;

import java.util.HashMap;
import java.util.Set;


public class OSMNode extends OSMElement{
	
	private OSMCoordinate nodeCoordinate;
	private HashMap tags;
	
	public OSMNode(String id)
	{
		super(id);	
	}

	public HashMap getTags() 
	{
		return tags;
	}

	public void setTags(HashMap tags) 
	{
		this.tags = tags;
	}

	public OSMCoordinate getNodeCoordinate() 
	{
		return nodeCoordinate;
	}

	public void setNodeCoordinate(OSMCoordinate nodeCoordinate) 
	{
		this.nodeCoordinate = nodeCoordinate;
	}	
	
	@Override
	public String toString()
	{
		String id = super.getId();
		String allTags = "";
		String nodeDescription ="";
		
		double lon = nodeCoordinate.getLongitude();
		double lat = nodeCoordinate.getLatitude();
		Set<String> tagKeys;
		
		if (tags != null)
		{	
			allTags += "Tags of the node with ID " + id +":\n";
			tagKeys = tags.keySet();
			for(String key : tagKeys)
			{
				allTags += " " + key + " = " + tags.get(key) + "\n";
			}
		}
		
		nodeDescription = " ID = " + id + "\n lon = " + lon + "\n lat = " + lat + "\n\n" + allTags;
		
		return nodeDescription;
	}
}
