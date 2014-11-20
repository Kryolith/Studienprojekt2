package studienprojekt.deprecated;

import java.util.ArrayList;
import java.util.List;
import studienprojekt.SpaceUsageRule;
import studienprojekt.osm.OSMCoordinate;
import studienprojekt.osm.OSMMap;
import studienprojekt.osm.OSMNode;
import studienprojekt.osm.OSMWay;

public class DefaultRule {// extends Rule { 
    
    private final int level = 1;
    
    public DefaultRule() { }
    
    // Gilt für jede Regel und gibt daher immer `true` zurück
   // @Override
    public boolean handles(SpaceUsageRule sur) {
        return  true;
    }

    // Finde den nächstbesten Weg und gebe diesen zurück
    //@Override
    public List<OSMWay> handle(OSMMap map, OSMCoordinate coordinate, SpaceUsageRule sur) {
        // Initialisiere Ergebnisvariable
        List<OSMWay> result = new ArrayList();
        
        // Initialisiere Suchergebnisse
        OSMNode nearestNode = null;
        double nearestDistance = Double.MAX_VALUE;
        
        // Suche Punkt, der am nächsten an den Koordinaten liegt
        for(OSMNode node : map.getNodes()) {
            double distance = node.getNodeCoordinate().distanceTo(coordinate);
            if(distance < nearestDistance) {
                nearestDistance = distance;
                nearestNode = node;
            }
        }
        
        // Falls kein Punkt gefunden wurde, dann abbruch
        if(nearestNode == null) return result;
        
        //System.out.println("\n\nNearest Node: " + nearestNode);
        
        // Ansonsten schauen ob Punkt in nem Weg liegt und der Ergebnisvariable hinzufügen
        for(OSMWay way : map.getWays()) {
            if(way.containsNode(nearestNode)) {
                result.add(way);
                break;
            }
        }
        
        if(result.isEmpty()) System.out.println("Kein zugehörigen Weg gefunden");
        
        // Ergebnis zurückgeben
        return result;
    }

    // Regel ist eine Standardregel (1)
    //@Override
    public int getLevel() {
        return this.level;
    }
    
}
