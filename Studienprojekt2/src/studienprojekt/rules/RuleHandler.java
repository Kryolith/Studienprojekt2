/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studienprojekt.rules;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import studienprojekt.Mapper;
import studienprojekt.osm.OSMCoordinate;
import studienprojekt.osm.OSMMap;
import studienprojekt.osm.OSMParser;
import studienprojekt.osm.OSMWay;

/**
 *
 * @author Sebastian
 */
public class RuleHandler {
    
    
    public OSMWay handle(Rule rule, OSMCoordinate cood) {
        // Erstmal API-Handler besorgen (Singleton-Pattern)
        //OSMApiHandler api = OSMApiHandler.getInstance();
        
        // Radius aus den rule-properties laden (oder aus der config)
        double range = Double.parseDouble(Mapper.getInstance().getConfiguration().get("default:range", "25"));
        if(rule.getConfiguration().has("range"))
            range = Double.parseDouble(rule.getConfiguration().get("range")); 
        
        
        // OSM-Map laden
        //OSMMap map = api.getMapByRadius(cood, range);
        
        OSMMap map = new OSMMap();
        try
        {    
             map = OSMParser.getOSMMap(cood, range);
        }
        catch (ParserConfigurationException | SAXException | IOException ex)
        {
            System.out.println(ex);
        }
        
        
        
        // Die Wege abfragen
        List<OSMWay> ways = map.getWays();
        
        for(OSMWay way : ways)
            System.out.println(way);
        
        // Die Wege nacheinander durchgehen, dabei direkt den höchstwertigen Weg speichern
        double max_value = 0;
        OSMWay max_way = null;
        
        for(OSMWay way : ways) {
            
            // Liste mit den Tags des Weges erstellen
            // Zunächst die normal eingetragenen Tags abrufen
            Map<String, String> wayTags = way.getTags();
            
            // Anschließend die Tags innerhalb dieses Weges suchen
            //Map<String, String> wayInnerTags = way.getInnerTags(map); 
            
            // Und diese in eine gemeinsame Liste mit den gewichteten Tags einfügen
            Map<String, Tag> tags = new HashMap();
            
            Set<String> wayTagsKeys = wayTags.keySet();
            for(String key : wayTagsKeys) {
              if(rule.hasTagWithKey(key)) {
                  Tag current = rule.getTagWithKey(key);
                  tags.put(current.getHash(), current);
              }  
            }
            
            /*wayTagsKeys = wayInnerTags.keySet();
            for(String key : wayTagsKeys) {
              if(rule.hasTagWithKey(key)) {
                  Tag current = rule.getTagWithKey(key);
                  tags.put(current.getHash(), current);
              }  
            }*/
            
            // Get Max Key weight
            double keyMaxWeight = 0;
            Set<String> tagKeys = tags.keySet();
            for(String key : tagKeys) {
                if(tags.get(key).getWeight() > keyMaxWeight)
                    keyMaxWeight = tags.get(key).getWeight();
            }
            
            // distance muss in metern sein
            double distance = way.shortestDistanceTo(cood);
            
            // Gewichtungsfaktor berechnen
            double wayWeight = getWayWeight(range, distance);
            
            // Finale Wertung errechnen
            double value = keyMaxWeight * wayWeight;
            
            
            
            // Direkt als besten weg setzen wenn ers ist
            if(value > max_value) {
                max_value = value;
                max_way = way;
            }
        }
        
        return max_way;
    }
    
    // Die Gewichtungsfunktion für die Entfernungen
    public double getWayWeight(double range, double distance) {        
        // Einmal als lineare Funktion
        return (distance >= range) ? 0 : -(distance / range) + 1;
        
        // Einmal als SinusFunktion
        //return (distance >= range) ? 0 : Math.sin(Math.PI * (distance / range));
    }
}
