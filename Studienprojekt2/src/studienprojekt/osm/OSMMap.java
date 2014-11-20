package studienprojekt.osm;

import java.util.ArrayList;
import java.util.List;


public class OSMMap 
{	
	private List<OSMNode> nodes;
	private List<OSMWay> ways;
	
	public List<OSMNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<OSMNode> nodes) 
	{
		this.nodes = nodes;
	}
	
	public List<OSMWay> getWays() 
	{
		return ways;
	}
	
	public void setWays(List<OSMWay> ways) 
	{
		this.ways = ways;
	}	
	
	@Override
	public String toString()
	{
		String osmMapDescription="\n--------------------------\nNODES CONTAINED WITHIN THIS MAP:\n--------------------------\n";
		
		for(OSMNode node : nodes)
		{
			osmMapDescription += node.toString();
		}
		osmMapDescription += "\n--------------------------\nWAYS CONTAINED WITHIN THIS MAP:\n--------------------------\n";
		for(OSMWay way : ways)
		{
			osmMapDescription += way.toString();
		}
		
		return osmMapDescription;
	}
}
