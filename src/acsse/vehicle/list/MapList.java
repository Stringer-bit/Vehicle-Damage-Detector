package acsse.vehicle.list;

import java.util.ArrayList;
import java.util.Iterator;

import acsse.vehicle.Node.mapEntry;

public class MapList<K, V>
{
	/*
	 * list to store our entries
	 */
	private ArrayList<mapEntry<K, V>> list = null;
	
	/**
	 * Constructor to initialize our list
	 */
	public MapList()
	{
		this.list = new ArrayList<>();
	}
	
	/**
	 * Method to return the size of our map
	 * @return the size of our list
	 */
	public int size()
	{
		return list.size();
	}
	
	/**
	 * method to determine weather or not our list is empty
	 * @return weather or not our list is empty
	 */
	public boolean isEmpty()
	{
		return (list.size() == 0);
	}
	
	/**
	 * Method to find the index of an entry given it's key
	 * @param key
	 * @return the index of the given key or -1 if the key doesn't exist
	 */
	public int findIndex(K key)
	{
		/*
		 * For each index i from 0 to the size of our list -1
		 */
		for(int i = 0; i < list.size(); i++)
			if(list.get(i).equals(key))//check if the key of the item in index i equals the given key
				return i;//If we found a matching key return the index where we found it
		
		return -1;
	}
	
	/**
	 * Method to get the value of a specific key
	 * @param key
	 * @return the value of the given key or return null if we couln't find
	 * the index of the given key
	 */
	public V get(K key)
	{
		//Find the index of the given key
		int index = findIndex(key);
		
		//If we couldn't find the given index return null
		if(index == -1)
			return null;
		
		//return the value of the item at the index of the given key
		return list.get(index).getValue();
	}
	
	/**
	 * Method to put a value into the map with a key
	 * @param key
	 * @param value
	 * @return the value replaced or null if the key doesn't exist
	 */
	public V put(K key, V value)
	{
		//Find the index of the given key
		int index = findIndex(key);
		
		//If we didn't find the key
		if(index == -1)
		{
			//Add a new map entry to the list
			list.add(new mapEntry<K,V>(key, value));
			return null;
		}
		
		//local variable to store the value before we replace it
		V old = list.get(index).getValue();
		
		//replace the old value with the new value
		list.get(index).setValue(value);
		
		//return the value we removed
		return old;
	}
	
	public V remove(K key)
	{
		//Find the index of the given key
		int index = findIndex(key);
		
		//If we couldn't find the given index return null
		if(index == -1)
			return null;
		
		//storing the value of the entry we want to remove
		V old = list.get(index).getValue();
		
		//If index given isn't the last index in the list
		if(index != size() - 1)
			list.set(index, list.get(size() - 1));//replace the index we want removed with the end of the list
		
		//remove the last entry in the list
		list.remove(size() - 1);
		
		return old;
	}
	
	public Iterator<K> keySet()
	{
		return null;
	}
	
	public Iterator<V> valueSet()
	{
		return null;
	}
	
	public Iterator<mapEntry<K,V>> entrySet()
	{
		return null;
	}
}
