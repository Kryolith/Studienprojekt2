package studienprojekt;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import studienprojekt.osm.OSMCoordinate;
import studienprojekt.osm.OSMMap;
import studienprojekt.osm.OSMParser;

public class Main {
    
//Dieses Kommentar gibt's nur im Martin-Branch!
    
    public static void main(String[] args) {
        
        // Construct and initialize the main object
        Mapper mapper = Mapper.getInstance();
        mapper.initialize();
        
        // Run the main algorithm
        mapper.run();
    } 
}

