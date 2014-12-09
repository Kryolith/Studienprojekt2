package studienprojekt.osm;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

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
        
       // Diese Methode bildet einen (geschlossenen) Way - also ein OSM-Polygon -
        // auf ein Objekt der Klasse java.awt.Polygon ab.
        public Polygon getPolygonProjection()
        {
            
            int [] x_coordinates = new int[wayComponents.size()]; 
            int [] y_coordinates = new int[wayComponents.size()]; 
            
            double lat;
            double lon;
            
            for (int i = 0; i < wayComponents.size(); i++)
            {
                lat = wayComponents.get(i).getNodeCoordinate().getLatitude();
                lon = wayComponents.get(i).getNodeCoordinate().getLongitude();
                x_coordinates[i] = (int)(lon * 111.31832 * Math.cos(lat) * 1000000);
                y_coordinates[i] = (int)(lat * 111.31832 * 1000000);
            }    
            return new Polygon(x_coordinates, y_coordinates, wayComponents.size());
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
            Polygon polygonProjection = this.getPolygonProjection();
            
            double lat = node.getNodeCoordinate().getLatitude();
            double lon = node.getNodeCoordinate().getLongitude();
            double x, y;
            
            x = lon * 111.31832 * Math.cos(lat) * 1000000;
            y = lat * 111.31832 * 1000000;
            
            return polygonProjection.contains(x, y);
        }
        
        
        public boolean wraps(OSMWay way)
        {
            for (OSMNode node : way.getWayComponents())
            {
                if (!(this.wrapsNode(node)))
                    return false;
            }    
            return true;
        }        
        
        public String getFirstNodeId()
        {
            return wayComponents.get(0).getId();
        } 
        
        public String getLastNodeId()
        {
            return wayComponents.get(wayComponents.size() - 1).getId();
        }  
        
        public double shortestDistanceTo(OSMCoordinate coordinate)
        {
 /*           List<OSMWay> polygons = new ArrayList<OSMWay>();

            double scanRadius = 0.001;
            boolean foundSurArea = false;
            boolean foundAnyPolygon = false;
            OSMNode nearestNode = null;
       //     OSMNode leftNeighbourNode = null;
       //     OSMNode rightNeighbourNode = null;
            OSMNode measuredNeighbourNode = null;
            OSMNode surNode = null;
            // OSMNode edgeHalfPoint = null;
            double nearestDistance;
            double finalNearestDistance;

            ArrayList<OSMWay> polygonsToScan = (ArrayList<OSMWay>)map.getWays();
            ArrayList<OSMNode> noWayComponents = new ArrayList<OSMNode>();


            HashMap<String,Double> poly_to_sur_distances = new HashMap<String,Double>();
            ValueComparator bvc =  new ValueComparator(poly_to_sur_distances);
            TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);
*/

            // Suche nach dem nächsgelegenen 'node'
      /*      double nearestDistance = Double.MAX_VALUE;

            for(OSMNode node : map.getNodes()) 
            {
                double distance = node.getNodeCoordinate().distanceTo(coordinate);
                if(distance < nearestDistance) 
                {
                    nearestDistance = distance;
                    nearestNode = node;
                }
            }
            */

            // Überführe die Koordinaten von 'this' in ein Point2D.Double-Objekt

            double lat_thisSurPoint = coordinate.getLatitude();
            double lon_thisSurPoint= coordinate.getLongitude();
            double x_thisSurPoint = lon_thisSurPoint * 111.31832 * Math.cos(lat_thisSurPoint);
            double y_thisSurPoint = lat_thisSurPoint * 111.31832;

            Point2D.Double thisSurPoint = new Point2D.Double();
            thisSurPoint.setLocation(x_thisSurPoint, y_thisSurPoint);


   //         ArrayList<Point2D.Double> kanten = new ArrayList<Point2D.Double>();

            double x_1, x_2, y_1, y_2, lon_1, lon_2, lat_1, lat_2;
            Point2D.Double point_1, point_2;

            lat_1 = wayComponents.get(0).getNodeCoordinate().getLatitude();
            lon_1 = wayComponents.get(0).getNodeCoordinate().getLongitude();
            x_1 = lon_1 * 111.31832 * Math.cos(lat_1);
            y_1 = lat_1 * 111.31832;
            point_1 = new Point2D.Double();
            point_1.setLocation(x_1, y_1);

            lat_2 = wayComponents.get(1).getNodeCoordinate().getLatitude();
            lon_2 = wayComponents.get(1).getNodeCoordinate().getLongitude();
            x_2 = lon_2 * 111.31832 * Math.cos(lat_2);
            y_2 = lat_2 * 111.31832;
            point_2 = new Point2D.Double();
            point_2.setLocation(x_2, y_2);
            
            Line2D.Double line = new Line2D.Double();
            line.setLine(point_1, point_2);
            
            double currentDistance = line.ptLineDist(thisSurPoint);
            double minDistance = currentDistance;

            for (int i=2; i < wayComponents.size()-1; i++)
            {
                lat_1 = lat_2;
                lon_1 = lon_2;
                x_1 = x_2;
                y_1 = y_2;
                point_1 = new Point2D.Double();
                point_1.setLocation(x_1, y_1);
                
                lat_2 = wayComponents.get(i).getNodeCoordinate().getLatitude();
                lon_2 = wayComponents.get(i).getNodeCoordinate().getLongitude();
                x_2 = lon_2 * 111.31832 * Math.cos(lat_2);
                y_2 = lat_2 * 111.31832;
                point_2 = new Point2D.Double();
                point_2.setLocation(x_2, y_2);
                
                line = new Line2D.Double();
                line.setLine(point_1, point_2);
                minDistance = (line.ptLineDist(thisSurPoint) < minDistance) ? line.ptLineDist(thisSurPoint) : minDistance;
            }
            return 1000 * minDistance;   
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
