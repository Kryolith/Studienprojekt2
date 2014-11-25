package studienprojekt.rules;

public class Tag
{
	private String key;
	private String value;
	private double weight;
	
	public Tag(String key, String value, double weight)
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
	public double getWeight()
	{
		return weight;
	}
        
        public String getHash() {
            return this.key + "=" + this.value;
        }
}
