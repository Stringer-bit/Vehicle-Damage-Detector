package acsse.vehicle.Node;

public class mapEntry <K, V>
{
	/*
	 * Member variables for our key and value
	 */
	
	private K key = null;
	private V value = null;
	
	public mapEntry(K key, V value)
	{
		this.key = key;
		this.value = value;
	}

	/**
	 * Getter method to retrieve our key
	 * @return
	 */
	public K getKey() 
	{
		return key;
	}

	/**
	 * Setter method to set our key
	 * @param source
	 */
	public void setKey(K key) 
	{
		this.key = key;
	}

	/**
	 * Getter method to retrieve our value
	 * @return
	 */
	public V getValue() 
	{
		return value;
	}

	/**
	 * Setter method to set our value
	 * @param source
	 */
	public V setValue(V value) 
	{
		V old = this.value;
		this.value = value;
		
		return old;
	}
}
