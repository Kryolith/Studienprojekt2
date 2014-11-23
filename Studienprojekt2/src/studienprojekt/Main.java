package studienprojekt;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import studienprojekt.osm.OSMCoordinate;
import studienprojekt.osm.OSMMap;
import studienprojekt.osm.OSMParser;

public class Main {
    
    public static void main(String[] args) {
   
        OSMParser parser = new OSMParser();
        // Testkoordinaten:
        
        // 0001: Erste SUR aus Testdatensatz
        //OSMCoordinate coor = new OSMCoordinate(50.9304, 5.33901);
        
        // 0034: 
        OSMCoordinate coor = new OSMCoordinate(50.9306, 5.36695);
        OSMMap testMap = null;
        try
        {
            // Übertrieben kleiner Radius von 0.5 m (bzw. 0.0005 km) --> zu Testzwecken
            testMap = parser.getOSMMap(coor, 0.0005);
        }    
        catch (ParserConfigurationException | SAXException | IOException ex)
        {
            System.out.println(ex);
        }    
        
        System.out.println(testMap);
 /*       // Construct and initialize the main object
        Mapper mapper = new Mapper();
        mapper.initialize();
        
        // Run the main algorithm
        mapper.run();
 */
   
        
    } 
}
