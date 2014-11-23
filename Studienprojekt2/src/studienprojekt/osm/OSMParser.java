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
    
     private static List<OSMNode> nodes;
     private static double sufficientBigRadius;
     
     public static OSMMap getOSMMap(OSMCoordinate surCoordinate, double radius) throws ParserConfigurationException, SAXException, IOException
     { 
         OSMMap osmMap = new OSMMap();
         sufficientBigRadius = radius;
         
         do
         {
             osmMap = getScaledMap(surCoordinate, sufficientBigRadius);
             sufficientBigRadius += radius;
             // TESTAUSGABE:
             System.out.println("Anpassung der Map nötig, da kein POLYGON gefunden wurde.");
            System.out.println("Radius: "+sufficientBigRadius*1000 + " m \n");
         }
         while(!(mapContainsPolygons(osmMap)));
         
         return osmMap;
     }        
             
     public static OSMMap getScaledMap(OSMCoordinate surCoordinate, double radius) throws ParserConfigurationException, SAXException, IOException 
     {
        // Zwei Kontrollvariablen, mit deren Hilfe der "Radius" einer zu extrahierenden
        // OSMMap ggf. so lange angepasst (sprich: vergrößert) wird, bis der zugehörige 
        // way (bei dem es sich immer um ein geschlossenes Polygon handelt) in der 
        // Map vollständig abgebildet wird.
        boolean foundAnyWays = false;
        sufficientBigRadius = radius;
                
        OSMMap osmMap = new OSMMap();

        nodes = new ArrayList<OSMNode>();
        List<OSMWay> ways = new ArrayList<OSMWay>();

        //Load the input XML document, parse it and return an instance of the
        //Document class.
        //Document document = builder.parse(new File("C:/Users/Martin Tatara/Java-EE-Workspace/SUR_Determinator/ApSegment_1.osm"));
        
        NodeList tagList = null;
        NodeList childrenList = null;
        NodeList nodeList = null;
        List<Node> osmElements = null;
        String id;
          
        while (!(foundAnyWays))
        {    
            Document document = extractNewMapfile(surCoordinate, sufficientBigRadius);

            osmElements = new ArrayList<Node>();
            nodeList = document.getDocumentElement().getChildNodes();

            // Falls die nodeList leer ist, bedeutet dies, dass kein einziger OSMNode
            // in der OSMMap liegen kann - in diesem Fall kann der aktuelle 
            // Schleifendurchlauf bereits an dieser Stelle unterbrochen weden:
            nodeList.toString();

            for (int i = 0; i < nodeList.getLength(); i++) 
            {
                if(nodeList.item(i).getNodeName().equals("way"))
                {    
                    foundAnyWays = true;
                    break;
                }    
            }
            sufficientBigRadius += radius;
            // TESTAUSGABE:
            System.out.println("Anpassung der Map nötig, da kein WEG gefunden wurde.");
            System.out.println("Radius: "+sufficientBigRadius*1000 + " m \n");
        }
            


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
                defineNewNode(node);
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

                            if(!(nodeFound)) {
                                System.out.println("Wegnodereferenz zeigt auf keinen gültigen Node");
                                // osmWay.getWayComponents().add(new OSMNode(nodeRef));
                                sufficientBigRadius += radius;
                                osmWay = loadWay(node, surCoordinate, sufficientBigRadius);
                                // TESTAUSGABE:
                                System.out.println("Anpassung der Map nötig, da gefundener Weg teilweise AUßERHALB DER MAP-BOUNDS liegt. .");
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
        // for (Node osmNode : osmElements)
        // System.out.println(osmNode.toString());
       osmMap.setNodes(nodes);
       osmMap.setWays(ways);
  
       return osmMap; 
    }
    
    // Diese Methode wird aufgerufen, sobald ein für die SUR-Analyse relevanter 'way' 
    // innerhalb einer extrahierten OSMMap nicht vollständig erfasst wurde (weil er über die 
    // festgelegten Bounds hinausläuft) 
    public static OSMWay loadWay(Node xmlWayElement, OSMCoordinate coordinate, double radius) throws ParserConfigurationException, SAXException, IOException
    {
        boolean polygonFound = false;
        sufficientBigRadius = radius;
        OSMWay osmWay = new OSMWay();
        Document document = extractNewMapfile(coordinate, radius);
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        Node node;
        Node ndNode;
        String nodeName;
        NodeList wayNodes;
        String nodeRef;
        boolean nodeFound = false;        
        
        for (int i = 0; i < nodeList.getLength(); i++) 
        { 
            node = nodeList.item(i);
            nodeName = node.getNodeName();    
            if (nodeName.equals("node"))
            {    
                defineNewNode(node);
            }           
        }
        
        wayNodes = xmlWayElement.getChildNodes();
        
        // Foögende if-Abfrage filtert die Tag-Unterelemente eines way-Elements, 
        // die innerhalb dieser Funktion nicht beachtet werden sollen, heraus
        for (int j = 0; j < wayNodes.getLength(); j++)
        {  
        // Folgende if-Abfrage filtert die Tag-Unterelemente eines way-Elements, 
        // die innerhalb dieser Funktion nicht beachtet werden sollen, heraus
            if(!((wayNodes.item(j).getNodeName()).equals("nd")))
                continue;
            
            ndNode = wayNodes.item(j);
            nodeRef = ndNode.getAttributes().getNamedItem("ref").getTextContent();
        
            for(OSMNode n : nodes) 
            {
                if(n.getId().equals(nodeRef)) {
                    osmWay.getWayComponents().add(n);
                    nodeFound = true;
                    break;
                }
            }

            if(!(nodeFound)) 
            {
                System.out.println("Wegnodereferenz zeigt auf keinen gültigen Node");
                // osmWay.getWayComponents().add(new OSMNode(nodeRef));
                sufficientBigRadius += radius;
                osmWay = loadWay(xmlWayElement, coordinate, sufficientBigRadius);
            }
        }
        return osmWay;   
    }
     
    public static void defineNewNode(Node node)
    {
        boolean nodeAlreadyInList = false;
        String newNodeId = node.getAttributes().getNamedItem("id").getTextContent(); 
        for (OSMNode n: nodes)
        {
            if (newNodeId.equals(n.getId()))
            nodeAlreadyInList = true;
            break;
        }    
        
        if (!(nodeAlreadyInList))
        {    
            String nodeName = node.getNodeName();
            double nodeLongitude; 
            double nodeLatitude;
            OSMCoordinate nodeCoordinate = null;
            NodeList tagList = null;        

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
                        // System.out.println(key + " = " + value);
                        tagMap.put(key, value);
                        // System.out.println(key + " = " + tagMap.get(key));
                   }
                }

                //System.out.println("\n\n");
                osmNode.setTags(tagMap);
                }

            nodes.add(osmNode);
            //System.out.println(wert);
        }    
    } 
    
    // parst ein XML-File und gibt das Ergebnis als ein Objekt der Klasse Document zurück:
    public static Document extractNewMapfile(OSMCoordinate surCoordinate, double radius) throws ParserConfigurationException, SAXException, IOException
    {
        double longitude = surCoordinate.getLongitude();
        double latitude = surCoordinate.getLatitude();
        OSMApiHandler osmExtractor = new OSMApiHandler();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();        
        File mapfileToParse = osmExtractor.getMapfile(latitude, longitude, radius, "osm_Datei");
        
        return builder.parse(mapfileToParse);        
    }
    
    public static boolean mapContainsPolygons(OSMMap map)
    {
        for (OSMWay way : map.getWays())
        {   
           if(way.getFirstNodeId().equals(way.getLastNodeId()))
               return true;
        } 
        return false;
    }
}      	
