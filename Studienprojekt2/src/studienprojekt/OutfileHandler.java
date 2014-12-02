package studienprojekt;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import studienprojekt.osm.OSMNode;

public class OutfileHandler {
    
    private final String path;
    
    public OutfileHandler(String path) {
        this.path = path;
        File dir = new File(this.path);
        
        if(!dir.exists())
            dir.mkdir();
    }    
    
    public void saveData(Result result) {
        
        // Erstelle ein TemplateObject für die Ausgabedatei
        Template tpl = new Template();
        
        // Lade vorlage
        tpl.load("templates/truth.kml.tpl");
        
        // Füge Name im Template ein
        tpl.replace("name", result.getName());
        
        //if(result.getOSMWays().size() > 1)
        //   throw new UnsupportedOperationException("Kann derzeit nur einen Weg als Ergebnis speichern");
        
        if(result.getOSMWay().getWayComponents().isEmpty())
            throw new UnsupportedOperationException("Kann keine leeren Ergebnisse behandeln");

        // Lade ersten Weg aus Result (muss später durch einen Loop o.ä. ersetzt werden)
        List<OSMNode> currentWay = result.getOSMWay().getWayComponents();
        
        // Iteriere durch alle Nodes
        for (Iterator<OSMNode> it = currentWay.iterator(); it.hasNext();) {
            OSMNode node = it.next();
            
            // Baue Node-String
            String coordinateString = 
                    node.getNodeCoordinate().getLongitude() + "," + 
                    node.getNodeCoordinate().getLatitude();
            
            // Falls es noch einen nächsten gibt füge einen Zeilenumbruch an
            if(it.hasNext())
                coordinateString += "\n"; 
            
            // Ersetze Coordinates-Loop mit der aktuellen Zeile
            tpl.replace("coordinates", coordinateString);
        }
        
        // Speicher Template ab
        tpl.save(path + "/" + result.getName() + "." + result.getId() + ".truth.kml");
    }  
}

