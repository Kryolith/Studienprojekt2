/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studienprojekt.rules;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import studienprojekt.SpaceUsageRule;
import studienprojekt.osm.OSMCoordinate;
import studienprojekt.osm.OSMMap;
import studienprojekt.osm.OSMNode;
import studienprojekt.osm.OSMWay;
import studienprojekt.osm.ValueComparator;
import studienprojekt.rules.Rule;

/**
 *
 * @author Martin Tatara
 */
public class SmokingRule extends Rule {

    public boolean handles(SpaceUsageRule sur) {
      //  if ((sur.getRule()).equals(this.sur.getRule())) {
            return true;
      //  }
     //   return false;
    }

    public List<OSMWay> handle(OSMMap map, OSMCoordinate coordinate, SpaceUsageRule sur) 
    {

            ArrayList<String> yesTags = new ArrayList<String>();
            ArrayList<String> noTags = new ArrayList<String>();
            
            ArrayList<OSMWay> waysToExclude = new ArrayList<OSMWay>();
            
            HashMap<String, String> wayTags = new HashMap<String, String>();
            
            String key = "";
            int counter;
        // Zunächst Auslesen der "Tag-Regeln" aus manuell erstellter Datei:
        try
        {    
            String tagName;
            String tagValue;
            
            String iterations;
	    String data = "";	
            FileReader fr = new FileReader("Testdatensatz/SmokingAmenity.txt");
            BufferedReader br = new BufferedReader(fr);
            iterations = br.readLine();
            int n = Integer.parseInt(iterations);
            for (int i = 0;  i < n; i++)
            {
                    data += br.readLine() + "\n" ;
            } 
            StringTokenizer st1 = new StringTokenizer(data, "\n");
            StringTokenizer st2 = null;

            String[] datensaetze = new String[n];
            
            for (int i = 0;  i < n; i++)
            {
                if(!(st1.hasMoreTokens()))
                        break;
                datensaetze[i] = st1.nextToken();
            }
            
            
            for (int i = 0;  i < n; i++)
            {
               // System.out.println(datensaetze[i]);
                st2 = new StringTokenizer(datensaetze[i], ":");
                tagName = st2.nextToken();
                tagValue = st2.nextToken();
                if(tagValue.equals("yes"))
                    yesTags.add(tagName);
                else
                    noTags.add(tagName);
            }            
        }
        catch(Exception e)
        {}  
    

           
  /*      System.out.println("NoTags:");
        for (String item : noTags)
        {
            System.out.println(item);
        }
        
        System.out.println("YesTags:");
        for (String item : yesTags)
        {
            System.out.println(item);
        }   
        */
    
        List<OSMWay> polygons = new ArrayList<OSMWay>();
        
        double scanRadius = 0.001;
        boolean foundSurArea = false;
        boolean foundAnyPolygon = false;
        OSMNode nearestNode = null;
        OSMNode leftNeighbourNode = null;
        OSMNode rightNeighbourNode = null;
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
        
        Line2D.Double edgeToLeftNeighbour = new Line2D.Double();
        Line2D.Double edgeToRightNeighbour = new Line2D.Double();
        double distToLeftEdge;
        double distToRightEdge;
        
        double lat_leftNeighbour;
        double lon_leftNeighbour;
        double x_leftNeighbour;
        double y_leftNeighbour;
        int leftNeighbourIndex;
        Point2D.Double leftNeighbour = new Point2D.Double();
        
        double lat_nearestPoint;
        double lon_nearestPoint;        
        double x_nearestPoint;
        double y_nearestPoint;
        int nearestPointIndex;
        Point2D.Double nearestPoint = new Point2D.Double();
        
        double lat_rightNeighbour;
        double lon_rightNeighbour;
        double x_rightNeighbour;
        double y_rightNeighbour;
        int rightNeighbourIndex;
        Point2D.Double rightNeighbour = new Point2D.Double();
          
        
        // Ermittle Distanz zum nächstgelegenen Knoten des als Polygon identifizierten 'ways'
        double distance;
        
        for (OSMWay way : polygonsToScan)
        {
            // 'way' ist kein Polygon:
            if (!((way.getFirstNodeId()).equals(way.getLastNodeId())))
                    continue;
            
            // Für den (unwahrscheinlichen) Fall, dass in einer OSM-Map 'ways' eingezeichnet sind,
            // die aus nur einem einzigen Knoten bestehen:
            if (way.getWayComponents().size() < 3)
                continue;
            
  //          System.out.println("max-Indesx-Ausgabe: "+way.getWayComponents().size());
            
            // Ermittle Distanz zum nächstgelegenen Knoten des als Polygon identifizierten 'ways'
            nearestDistance = Double.MAX_VALUE;
            for(OSMNode node : way.getWayComponents()) 
            {
                distance = node.getNodeCoordinate().distanceTo(coordinate);
                if(distance < nearestDistance) 
                {
                    nearestDistance = distance;
                    nearestNode = node;
                }
            }
            
            // Ermittle die (kürzesten) Distanzen zu den beiden vom 'nearestNode'
            // ausgehenden Polygonkanten. Nimm anschliessend die kürzere der beiden
            
            // Zuvor aber: Indexanpassung, falls zwei Nachbarn gefunden werden sollten,
            // von denen einer an erster und der andere an letzter Position der ArrayList liegt
            if(way.getWayComponents().indexOf(nearestNode) == 0)
            {
   //             System.out.println("Hier wird berechnet: left = "+way.getWayComponents().size() + " - 1, und nearest = 0");
                leftNeighbourIndex = (way.getWayComponents().size()) - 2;
                nearestPointIndex = 0;
                rightNeighbourIndex = 1;
            }    
                    
            else if (way.getWayComponents().indexOf(nearestNode) == (way.getWayComponents().size() - 1))
            {
      //          System.out.println("Hier wird berechnet: left = "+way.getWayComponents().size() + " - 2");
                leftNeighbourIndex = (way.getWayComponents().size()) - 2;
                nearestPointIndex = (way.getWayComponents().size()) - 1;
                rightNeighbourIndex =  1;  
      //          System.out.println("Ergebnis: " + leftNeighbourIndex +"\n");
            } 
            
            else
            {
      //          System.out.println("Hier wird berechnet: left = "+way.getWayComponents().indexOf(nearestNode) + " - 1, und nearest bleibt unverändert.");
                leftNeighbourIndex = (way.getWayComponents().indexOf(nearestNode)) - 1;
                nearestPointIndex = way.getWayComponents().indexOf(nearestNode);
                rightNeighbourIndex = (way.getWayComponents().indexOf(nearestNode)) + 1;    
       //         System.out.println("Ergebnis: " + leftNeighbourIndex +"\n");
            }    
            
            // "linker Nachbar" vom nearestNode:
            lat_leftNeighbour = way.getWayComponents().get(leftNeighbourIndex).getNodeCoordinate().getLatitude();
            lon_leftNeighbour = way.getWayComponents().get(leftNeighbourIndex).getNodeCoordinate().getLongitude();
            x_leftNeighbour = lon_leftNeighbour * 111.31832 * Math.cos(lat_leftNeighbour);
            y_leftNeighbour = lat_leftNeighbour * 111.31832;
            leftNeighbour.setLocation(x_leftNeighbour, y_leftNeighbour);
            
            // der 'nearestNode' selbst:
            lat_nearestPoint = way.getWayComponents().get(nearestPointIndex).getNodeCoordinate().getLatitude();
            lon_nearestPoint = way.getWayComponents().get(nearestPointIndex).getNodeCoordinate().getLongitude();
            x_nearestPoint = lon_nearestPoint * 111.31832 * Math.cos(lat_leftNeighbour);
            y_nearestPoint = lat_nearestPoint * 111.31832;
            nearestPoint.setLocation(x_nearestPoint, y_nearestPoint);          
            
            // "rechter Nachbar" vom nearestNode:
            lat_rightNeighbour = way.getWayComponents().get(rightNeighbourIndex).getNodeCoordinate().getLatitude();
            lon_rightNeighbour = way.getWayComponents().get(rightNeighbourIndex).getNodeCoordinate().getLongitude();
            x_rightNeighbour = lon_leftNeighbour * 111.31832 * Math.cos(lat_leftNeighbour);
            y_rightNeighbour = lat_leftNeighbour * 111.31832;
            rightNeighbour.setLocation(x_rightNeighbour, y_rightNeighbour);
            
            // "ziehe Kante" zum linken Nachbarn:
            edgeToLeftNeighbour.setLine(nearestPoint, leftNeighbour);
            
            // "ziehe Kante" zum rechten Nachbarn:
            edgeToRightNeighbour.setLine(nearestPoint, rightNeighbour);
            
            distToLeftEdge = edgeToLeftNeighbour.ptSegDistSq(thisSurPoint);
            distToRightEdge = edgeToRightNeighbour.ptSegDistSq(thisSurPoint);
            
            finalNearestDistance = distToRightEdge;
            
            if (distToLeftEdge <= distToRightEdge)
            {    
                finalNearestDistance = distToLeftEdge;
                measuredNeighbourNode = way.getWayComponents().get(leftNeighbourIndex);
            }    
            else if(distToLeftEdge > distToRightEdge)
            {    
                finalNearestDistance = distToRightEdge;
                measuredNeighbourNode = way.getWayComponents().get(rightNeighbourIndex);
            }    
      //      else if(Double.isNaN(distToLeftEdge))
      //          finalNearestDistance = distToRightEdge;
      //      else 
      //          finalNearestDistance = distToLeftEdge;
            
        //    if (!(Double.isNaN(finalNearestDistance)))
           poly_to_sur_distances.put(way.getId(), finalNearestDistance);
           
   
           
     /*         OSMWay testWay = way;
              ArrayList<OSMNode> testWayComponents = new ArrayList<OSMNode>();
              
              surNode = new OSMNode("1111111");
              surNode.setNodeCoordinate(coordinate);
              
              testWayComponents.add(surNode);
              testWayComponents.add(measuredNeighbourNode);
              testWayComponents.add(nearestNode);
              testWayComponents.add(surNode);
              
              testWay.setWayComponents(testWayComponents);
         */


        }
    
        
        // Sortiere die Map nach den Entfernungen der sur-Koordinate zu allen 
        //(geschlossenen) umliegenden Polygonen
      

        sorted_map.putAll(poly_to_sur_distances);
        
        System.out.println("results: "+sorted_map);
     //   ArrayList<OSMWay> polygonsScanned = new ArrayList<OSMWay>();
     //   double minDist = Double.MAX_VALUE;       
        
        Set<String> wayIds = sorted_map.keySet();
        Iterator<String> wayIdIterator = wayIds.iterator();
        
        String foundPolyId = wayIdIterator.next();
        OSMWay foundPolygon = new OSMWay();
        OSMWay nearestPolygon = new OSMWay();
        
OUTER:  for(;;)
        {   
INNER:      for (OSMWay way : polygonsToScan) 
            {
                if (!(foundPolyId.equals(way.getId())))
                    continue; 
                foundPolygon = way;
                
                wayTags = foundPolygon.getTags();
                
                if (wayTags.containsKey("name"))
                {
                    System.out.println("name =" + wayTags.get("name") + " , " + poly_to_sur_distances.get(foundPolyId));
                }    


                if (wayTags.get("building") != null)
                {
                   // System.out.println("Building NOT NULL!!");
                    if (wayTags.get("building").equals("yes"))
                    {
                        // leerer Rumpf ==> nach Verlassen der beiden if-Rümpfe
                        // wird einfach weitergemacht
                    }    
                    else    
                    {    
                        foundPolyId = wayIdIterator.next();
                        System.out.println("Building-Tag with NO-Value!!!");
                        continue OUTER;
                    }    
                }
                else
                {
                        foundPolyId = wayIdIterator.next();
                        System.out.println("Building-Tag with NO-Value!!!");
                        continue OUTER;
                }
                
                
                /*
                if (way.getTags().get("landuse") != null)
                {    
                    System.out.println("On landuses smoking is not forbidden!");

                    foundPolyId = wayIdIterator.next();
                    System.out.println("Building-Tag with NO-Value!!!");
                    continue OUTER;
                }

               // System.out.println("Amenity="+wayTags.get("amenity"));

                if (noTags.contains(wayTags.get("amenity")))
                {  
                    foundPolyId = wayIdIterator.next();
                    System.out.println("Polygon excluded due to tag-analysis!!!");
                    continue OUTER;
                }

                
                if (wayTags.get("shop") == null & wayTags.get("building") == null)
                { 
                    foundPolyId = wayIdIterator.next();
                    System.out.println("Polygon is not a building and not a shop");
                    continue OUTER;
                }
                */
                // Wenn das Programm bis hierhin alle Tag-Überprüfungen "bestanden" hat
                // kann man im Großen und Ganzen davon ausgehen, dass die übergebene
                // SUR (mit ihren koordinaten) mit hoher Wahrscheinlichkeit für eben dieses Polygon gilt....
              
            
                //Testsequenz
              ///////////////////////////////////  


/* auskommentiert am 20.11.2014              
           nearestDistance = Double.MAX_VALUE;
            for(OSMNode node : foundPolygon.getWayComponents()) 
            {
                distance = node.getNodeCoordinate().distanceTo(coordinate);
                if(distance < nearestDistance) 
                {
                    nearestDistance = distance;
                    nearestNode = node;
                }
            }
            
            System.out.println("Anzahl der Knoten im Polygon: "+ foundPolygon.getWayComponents().size() + "\n");
            System.out.println();
            if(foundPolygon.getWayComponents().indexOf(nearestNode) == 0)
            {
                  leftNeighbourIndex = foundPolygon.getWayComponents().size() - 1;
                  rightNeighbourIndex = 1;
            }  
            
            else if(foundPolygon.getWayComponents().indexOf(nearestNode) == foundPolygon.getWayComponents().size() - 1)
            {
                  leftNeighbourIndex = foundPolygon.getWayComponents().size() - 2;
                  rightNeighbourIndex = 0;
            }
            
            else 
            {
                  leftNeighbourIndex = foundPolygon.getWayComponents().indexOf(nearestNode) - 1;
                  rightNeighbourIndex = foundPolygon.getWayComponents().indexOf(nearestNode) + 1;
            } 
            Ende: auskommentiert am 20.11.2014   
            */
    /*        
            OSMWay testWay = foundPolygon;
            ArrayList<OSMNode> testWayComponents = new ArrayList<OSMNode>();

            surNode = new OSMNode("2068646505");
            surNode.setNodeCoordinate(coordinate);

            leftNeighbourNode = foundPolygon.getWayComponents().get(leftNeighbourIndex);
            rightNeighbourNode = foundPolygon.getWayComponents().get(rightNeighbourIndex);
            
            System.out.println(foundPolygon);
            
            System.out.println("Nearest-Index: "+ foundPolygon.getWayComponents().indexOf(nearestNode));
            System.out.println("RN-Index: " + foundPolygon.getWayComponents().indexOf(rightNeighbourNode));
            System.out.println("LN-Index: " + foundPolygon.getWayComponents().indexOf(leftNeighbourNode));
            
            testWayComponents.add(leftNeighbourNode);
            System.out.println("leftNeighbCoor: "+ leftNeighbourNode.getNodeCoordinate().getLatitude() + " , " + leftNeighbourNode.getNodeCoordinate().getLongitude());
            testWayComponents.add(nearestNode);
            System.out.println("nearestNodeCoor: "+ nearestNode.getNodeCoordinate().getLatitude() + " , " + nearestNode.getNodeCoordinate().getLongitude());
            testWayComponents.add(rightNeighbourNode);
             System.out.println("rightNeighbCoor: "+ rightNeighbourNode.getNodeCoordinate().getLatitude() + " , " + rightNeighbourNode.getNodeCoordinate().getLongitude());
            testWayComponents.add(surNode);
            System.out.println("surCoor: "+ surNode.getNodeCoordinate().getLatitude() + " , " + surNode.getNodeCoordinate().getLongitude());
            testWayComponents.add(leftNeighbourNode);

            foundPolygon.setWayComponents(testWayComponents); 
    */        
            polygons.add(foundPolygon);

            //////////// Ende Testsequenz ////////////////////
            
            //polygons.add(foundPolygon);
                

               break;
            } 
            break;
        }
        System.out.println("---------------------------------------\n\n");
        return polygons;
}   
        //////////hier endet die ursprüngliche Smoking-Regel//////////////
 //////////////////////////////////////////////////////////////////////////////       
        
/*        
        // Initialisiere Ergebnisvariable
        List<OSMWay> result = new ArrayList();
        ArrayList<OSMWay> polygonsToScan = (ArrayList<OSMWay>)map.getWays();
        
        // Initialisiere Suchergebnisse
        OSMNode nearestNode = null;
        OSMNode surNode = null;
        OSMNode leftNeighbourNode = null;
        OSMNode rightNeighbourNode = null;
        
        OSMWay foundPolygon = null;
       
        double nearestDistance = Double.MAX_VALUE;
        double distance;
        int leftNeighbourIndex, rightNeighbourIndex;
        // Suche Punkt, der am nächsten an den Koordinaten liegt
        
        for (OSMWay way : polygonsToScan)
        {  
            if (!((way.getFirstNodeId()).equals(way.getLastNodeId())) | way.getWayComponents().size() < 3)
                    continue;
       */     
        /*    
            if (way.getTags().get("building") != null)
            {
               // System.out.println("Building NOT NULL!!");
                if (way.getTags().get("building").equals("yes"))
                {
                    // leerer Rumpf ==> nach Verlassen der beiden if-Rümpfe
                    // wird einfach weitergemacht
                }    
                else    
                {    
                    continue;
                }    
            }
            else
            {
                    continue;
            } 
            zunächst NUR die Entfernungsberechnung testen!!
            */
  /*          
            for(OSMNode node : way.getWayComponents()) 
            {
                distance = node.getNodeCoordinate().distanceTo(coordinate);
                if(distance < nearestDistance)
                {
                    nearestDistance = distance;
                    nearestNode = node;
                    foundPolygon = way;
                }
            }
        }
        
        if(foundPolygon.getWayComponents().indexOf(nearestNode) == 0)
        {
              leftNeighbourIndex = foundPolygon.getWayComponents().size() - 2;
              rightNeighbourIndex = 1;
        }  

        else if(foundPolygon.getWayComponents().indexOf(nearestNode) == foundPolygon.getWayComponents().size() - 1)
        {
              leftNeighbourIndex = foundPolygon.getWayComponents().size() - 2;
              rightNeighbourIndex = 1;
        }

        else 
        {
              leftNeighbourIndex = foundPolygon.getWayComponents().indexOf(nearestNode) - 1;
              rightNeighbourIndex = foundPolygon.getWayComponents().indexOf(nearestNode) + 1;
        } 
        
        ArrayList<OSMNode> testWayComponents = new ArrayList<OSMNode>();
        
        surNode = new OSMNode("2068646505");
        surNode.setNodeCoordinate(coordinate);

        leftNeighbourNode = foundPolygon.getWayComponents().get(leftNeighbourIndex);
        rightNeighbourNode = foundPolygon.getWayComponents().get(rightNeighbourIndex);

        System.out.println(foundPolygon);

        System.out.println("Nearest-Index: "+ foundPolygon.getWayComponents().indexOf(nearestNode));
        System.out.println("RN-Index: " + foundPolygon.getWayComponents().indexOf(rightNeighbourNode));
        System.out.println("LN-Index: " + foundPolygon.getWayComponents().indexOf(leftNeighbourNode));

        testWayComponents.add(surNode);
        System.out.println("surCoor: "+ surNode.getNodeCoordinate().getLatitude() + " , " + surNode.getNodeCoordinate().getLongitude());
        
        testWayComponents.add(nearestNode);
        System.out.println("nearestNodeCoor: "+ nearestNode.getNodeCoordinate().getLatitude() + " , " + nearestNode.getNodeCoordinate().getLongitude());
        
        testWayComponents.add(surNode);
        
        testWayComponents.add(leftNeighbourNode);
        System.out.println("leftNeighbCoor: "+ leftNeighbourNode.getNodeCoordinate().getLatitude() + " , " + leftNeighbourNode.getNodeCoordinate().getLongitude());

        testWayComponents.add(surNode);
        
        testWayComponents.add(rightNeighbourNode);
        System.out.println("rightNeighbCoor: "+ rightNeighbourNode.getNodeCoordinate().getLatitude() + " , " + rightNeighbourNode.getNodeCoordinate().getLongitude());
     //   testWayComponents.add(leftNeighbourNode);

        foundPolygon.setWayComponents(testWayComponents);          
        
        result.add(foundPolygon);
        
        // Ergebnis zurückgeben
        return result;
    }
    */
    public int getLevel()
    {
        return 0;
    }
}
