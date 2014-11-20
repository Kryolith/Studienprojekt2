package studienprojekt.osm;


public class OSMCoordinate 
{
	private double longitude;
	private double latitude;
	
	public OSMCoordinate(double latitude, double longitude)
	{
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public double getLongitude() 
	{
		return longitude;
	}
	public void setLongitude(double longitude) 
	{
		this.longitude = longitude;
	}
	public double getLatitude() 
	{
		return latitude;
	}
	public void setLatitude(double latitude) 
	{
		this.latitude = latitude;
	}
        
        public double distanceTo(OSMCoordinate coordinate) {
            
            int r = 6371;
            
            double rad_lat_1 = Math.toRadians(this.latitude);
            double rad_lat_2 = Math.toRadians(coordinate.getLatitude());
            
            double delta_lat = Math.toRadians(this.latitude - coordinate.getLatitude());
            double delta_lon = Math.toRadians(this.longitude - coordinate.getLongitude());
            
            double a = Math.pow(Math.sin(delta_lat / 2), 2)
                    + Math.cos(rad_lat_1) * Math.cos(rad_lat_2) 
                    * Math.pow(Math.sin(delta_lon / 2), 2);
            
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            
            return r * c;
        }
        
        @Override
        public String toString() {
            return "( " + this.longitude + " | " + this.latitude + " )";
        }
	
	
}
