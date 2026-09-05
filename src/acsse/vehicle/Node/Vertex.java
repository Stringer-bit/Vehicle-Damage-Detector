package acsse.vehicle.Node;

import acsse.vehicle.Position.Position;

import java.util.HashMap;
import java.util.Map;


public class Vertex<E, V> 
{
	private V element;
	/*
	 * Reference to the position of the vertex instance in 
	 * the list V, allowing for V to be efficiently 
	 * removed from an edge list
	 */
	private Position<Vertex<E, V>> pos;
	private Map<Vertex<E, V>, Edge<E, V>> outgoing, incoming;
	
	public Vertex(V element, boolean isDirected)
	{
		this.element = element;
		
		outgoing = new HashMap<>();
		if(isDirected)
			incoming = new HashMap<>();
		else
			incoming = outgoing;
	}
	
	public Position<Vertex<E,V>> getPosition() 
	{
		return pos;
	}

	public V getElement()
	{
		return element;
	}
	
	public void setPosition(Position<Vertex<E, V>> p)
	{
		pos = p;
	}
	
	public Map<Vertex<E, V>, Edge<E, V>> getOutgoing()
	{
		return outgoing;
	}
	
	public Map<Vertex<E, V>, Edge<E, V>> getIncoming()
	{
		return incoming;
	}
	
	
}
