package studienprojekt.rules.data;

public class Tag
{
	private String key;
	private String value;
	private int weight;
	
	public Tag(String key, String value, int weight)
	{
		this.key = key;
		this.value = value;
		this.weight = weight;
	}

	public String getKey()
	{
		return key;
	}
	public String getValue()
	{
		return value;
	}
	public int getWeight()
	{
		return weight;
	}
}
