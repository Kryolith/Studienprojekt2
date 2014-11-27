package studienprojekt.osm;

public class OSMElement {
	
	private String id;

        public OSMElement()
	{

	}        
        
	public OSMElement(String id)
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}

	public void setId(String id) 
	{
		this.id = id;
	}
}
