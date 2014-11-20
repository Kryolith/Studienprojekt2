package studienprojekt.osm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
	 
public class OSMParser {
	
     public OSMMap getOSMMap(OSMCoordinate surCoordinate, double radius) throws ParserConfigurationException, SAXException, IOException 
     {
        OSMMap osmMap = new OSMMap();
    	 
    	 List<OSMNode> nodes = new ArrayList<OSMNode>();
    	 List<OSMWay> ways = new ArrayList<OSMWay>();
    	 
    	double longitude = surCoordinate.getLongitude();
    	double latitude = surCoordinate.getLatitude();
    	OSMApiHandler osmExtractor = new OSMApiHandler();
    	File mapfileToParse = osmExtractor.getMapfile(latitude, longitude, radius, "osm_Datei");
 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
 
      //Load the input XML document, parse it and return an instance of the
      //Document class.
      //Document document = builder.parse(new File("C:/Users/Martin Tatara/Java-EE-Workspace/SUR_Determinator/ApSegment_1.osm"));
	    Document document = builder.parse(mapfileToParse);
 
	    List<Node> osmElements = new ArrayList<Node>();
	    NodeList nodeList = document.getDocumentElement().getChildNodes();
	    NodeList tagList = null;
	    NodeList childrenList = null;
	    String id;
	          
	    for (int i = 0; i < nodeList.getLength(); i++) 
	    {
           Node node = nodeList.item(i);
           
           if(node.getAttributes()!=null)
           {	
        	   /*
        	   if (node.getAttributes().getNamedItem("id") != null)
        	   {
        		   System.out.println("ID = "+ node.getAttributes().getNamedItem("id").getNodeValue());
        	   }
        	   */
        	   String nodeName = node.getNodeName();
        	   double nodeLongitude; 
        	   double nodeLatitude;
        	   OSMCoordinate nodeCoordinate = null;
        	   
  
//////////////////////Fall 1: geparstes xml-Element ist vom Typ 'node' //////////////////     	   
        	   
        	   if (nodeName.equals("node"))
        	   {    
        		  // System.out.println(node.getAttributes().getNamedItem("id").getTextContent());
        		  // System.out.println("TEST : " + (node.getAttributes().getNamedItem("lon")).toString());
        		   
        		   OSMNode osmNode = new OSMNode((node.getAttributes().getNamedItem("id")).getTextContent());
        		   nodeLongitude = Double.parseDouble((node.getAttributes().getNamedItem("lon")).getTextContent());
        		   nodeLatitude = Double.parseDouble((node.getAttributes().getNamedItem("lat")).getTextContent());
        		   //System.out.println(nodeLongitude);
        		   nodeCoordinate = new OSMCoordinate(nodeLongitude, nodeLatitude);
        		   osmNode.setNodeCoordinate(nodeCoordinate);
        		   
        		   //System.out.println("ID = " + osmNode.getId());
        		   //System.out.println("lon = " + osmNode.getCoordinate().getLongitude());
        		   //System.out.println("lat = " +osmNode.getCoordinate().getLatitude()+"\n");
        		   
        		   tagList = node.getChildNodes();
        		   
        		   if(tagList.getLength() > 0)
        		   {
        			   Node tagNode = null;
        			   String key = "";
        			   String value = "";
        			   HashMap<String,String> tagMap = new HashMap<String,String>();
        			   
        			   //System.out.println(tagNode);
        			   //System.out.println(" ");
        			   for (int j = 0; j < tagList.getLength(); j++)
        			   {
        				   if((tagList.item(j).getNodeName()).equals("tag"))
        				   {
        					   tagNode = tagList.item(j);
        					   key = tagNode.getAttributes().getNamedItem("k").getTextContent();
        					   value = tagNode.getAttributes().getNamedItem("v").getTextContent();
        					   //System.out.println(key + " = " + value);
        					   tagMap.put(key, value);
        					   //System.out.println(key + " = " + tagMap.get(key));
        				   }
        			   }
        			   
        			   //System.out.println("\n\n");
        			   osmNode.setTags(tagMap);
        		   }
        		   
        		   nodes.add(osmNode);
        	   		//System.out.println(wert);
        	   }
        	   
        	   
 ////////////////////// Fall 2: geparstes xml-Element ist vom Typ 'way' //////////////////
        	   
        	   if (nodeName.equals("way"))
        	   {    
        		  // System.out.println(node.getAttributes().getNamedItem("id").getTextContent());
        		  // System.out.println("TEST : " + (node.getAttributes().getNamedItem("lon")).toString());
        		   
        		   OSMWay osmWay = new OSMWay((node.getAttributes().getNamedItem("id")).getTextContent());
        		   
        		   //System.out.println("ID = " + osmNode.getId());
        		   //System.out.println("lon = " + osmNode.getCoordinate().getLongitude());
        		   //System.out.println("lat = " +osmNode.getCoordinate().getLatitude()+"\n");
        		   
        		   childrenList = node.getChildNodes();
        		   
        		   if(childrenList.getLength() > 0)
        		   {
        			   Node tagNode = null;
        			   Node ndNode = null;
        			   String key = "";
        			   String value = "";
        			   HashMap<String,String> tagMap = new HashMap<String,String>();
        			   
        			   //System.out.println(tagNode);
        			  // System.out.println(" ");
        			   
        			   for (int j = 0; j < childrenList.getLength(); j++)
        			   {
        				//////// Fall 2a: Kindelement von way ist <tag> ////////   
        				   if((childrenList.item(j).getNodeName()).equals("tag"))
        				   {
        					   tagNode = childrenList.item(j);
        					   key = tagNode.getAttributes().getNamedItem("k").getTextContent();
        					   value = tagNode.getAttributes().getNamedItem("v").getTextContent();
        					   //System.out.println(key + " = " + value);
        					   tagMap.put(key, value);
        					   //System.out.println(key + " = " + tagMap.get(key));
        				   }
        				//////// Fall 2b: Kindelement von way ist <nd>   
        				   if((childrenList.item(j).getNodeName()).equals("nd"))
        				   {
        					   ndNode = childrenList.item(j);
                                                   
                                                   // S: Füge existierenden Node dem Way zu, keinen neuen
                                                   String nodeRef = ndNode.getAttributes().getNamedItem("ref").getTextContent();
                                                   
                                                   boolean nodeFound = false;
                                                   
                                                   for(OSMNode n : nodes) {
                                                       if(n.getId().equals(nodeRef)) {
                                                           osmWay.getWayComponents().add(n);
                                                           nodeFound = true;
                                                           break;
                                                       }
                                                   }
                                                   
                                                   if(!nodeFound) {
                                                       System.out.println("Wegnodereferenz zeigt auf keinen gültigen Node");
                                                       osmWay.getWayComponents().add(new OSMNode(nodeRef));
                                                   }
                                                   
        					   //System.out.println("nd = " + ndNode.getAttributes().getNamedItem("ref"));
        				   }
        			   }
        			   //System.out.println("\n\n");
        			   osmWay.setTags(tagMap);
        		   }
        		   ways.add(osmWay);
        	   		//System.out.println(wert);
        	   }
           }	   

	 
	                    osmElements.add(node);
	          }
	 
	          // Print all osmElements.
	 //         for (Node osmNode : osmElements)
	 //              System.out.println(osmNode.toString());
	    	 osmMap.setNodes(nodes);
	    	 osmMap.setWays(ways);
	         return osmMap; 
	     }
	}