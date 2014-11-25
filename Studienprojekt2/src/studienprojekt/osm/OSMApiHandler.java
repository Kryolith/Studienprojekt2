package studienprojekt.osm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class OSMApiHandler 
{
	private File osmMapFile = null;
	private List ways;
	private List nodes;


	public File getMapfile(double left, double bottom, double right, double top, String mapfileName) 
	{
		//"Normierung" der �bergebenen Koordinaten auf einen festen Nachkommaanteil
		double l, b, r, t;
	    l = Math.rint(left * 100000) / 100000;
	    b = Math.rint(bottom * 100000) / 100000;
	    r = Math.rint(right * 100000) / 100000;
	    t = Math.rint(top * 100000) / 100000;
		
            //Generierung des entsprechenden API-Aufrufs, um eine "Teilmap" zu erhalten
	    String url = "http://api.openstreetmap.org/api/0.6/map?bbox="+l+","+b+","+r+","+t;
	    //System.out.println(url); 
	    HttpClient client = new DefaultHttpClient();
	    HttpGet request = new HttpGet(url);
	    HttpResponse response;
	    BufferedReader br;
	    String line = "";
	        
	    try {
		    	response = client.execute(request);
		    	br = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		    	osmMapFile = new File ("temp/" + mapfileName + ".osm");
	        	FileWriter writer = new FileWriter (osmMapFile , false);        	
	            while ((line = br.readLine()) != null) {
	                writer.write(line);
	                writer.write("\n");
	            }    
	        	writer.flush ();
	        	writer.close ();
	    	}
	    
	    	catch (IOException e) {
	    		e.printStackTrace ();
	    	}
		return osmMapFile;
	}
	
	public File getMapfile(double latitude, double longitude, double radius, String mapfileName)
	{
            /* Alte Berechnung mit Radiuslänge angegeben in Längen- bzw. Breitengradbruchteilen:
            double left, bottom, right, top;
	    left = Math.rint(((longitude - radius) * 100000)) / 100000;
	    bottom = Math.rint(((latitude - radius) * 100000)) / 100000;
	    right = Math.rint(((longitude + radius) * 100000)) / 100000;
	    top = Math.rint(((latitude + radius) * 100000)) / 100000;
	    */
            
            // Neue Berechnung mit Radiuslänge angegeben in Metern
            double left, bottom, right, top;
            
            //Folgende Berechnungsschritte sind trivial, da die Kilometerlänge eines
            //Breitengrades mit 111,32 km stets konstant bleibt
            double lat_rad = radius/111.32;
            top = latitude  + lat_rad;
            bottom = latitude - lat_rad;
            
            // Die Kilometerlänge eines Längengrades variiert in Abhängigkeit vom
            // aktuellen Breitengrad. Sie kann bestimmt werden, indem man mittels der Methode
            // distanceTo (der Klasse OSMCoordinate) den Abstand der aktuellen SURCoordinate
            // zu einer genau um einen Längengrad verschobenen weiteren Koordinate (mit demselben Breitengrad) berechnet
            OSMCoordinate surCoordinate = new OSMCoordinate(latitude, longitude);
            OSMCoordinate calibrationCoordinate = new OSMCoordinate(latitude, longitude + 1.0000);
            double currentLonLength = surCoordinate.distanceTo(calibrationCoordinate);
            double lon_rad = radius/currentLonLength;
            left = longitude - lon_rad;
            right = longitude + lon_rad;
            
	    return getMapfile(left, bottom, right, top, mapfileName);
	}

	public void setOsmMapFile(File osmMapFile) 
	{
		this.osmMapFile = osmMapFile;
	}
}
