package acsse.vehicle.Node;

import acsse.vehicle.Position.Position;

public class Edge <E, V>
{
	private E element = null;
	private Position<Edge<E, V>> pos;
	private Vertex<E, V>[] endpoints;
	@SuppressWarnings("unused")
	private Integer weight;
	

	/**
	 * Constructor to initialize default values of our "Edge" class
	 * @param source
	 * @param destination
	 */
	@SuppressWarnings("unchecked")
	public Edge(Vertex<E, V> u, Vertex<E, V> v, E element)
	{
		this.element = element;
		endpoints = (Vertex<E, V>[]) new Vertex[] {u,v};
	}
	
	/*
	 * Getters and setters
	 */
	
	/**
	 * Getter method to retrieve our element
	 * @return
	 */
	public E getElement() 
	{
		return element;
	}
	
	/**
	 * Function to retrieve the array of vertexes 
	 * @return endpoints array
	 */
	public Vertex<E, V>[] getEndpoints()
	{
		return endpoints;
	}

	/**
	 * Setter method to set our source
	 * @param source
	 */
	public void setElement(E element) 
	{
		this.element = element;
	}

	/**
	 * Getter method to retrieve our source
	 * @return
	 */
	public Vertex<E, V> getSource() 
	{
		return endpoints[0];
	}

	/**
	 * Setter method to set our source
	 * @param source
	 */
	public void setSource(Vertex<E, V> u) 
	{
		endpoints[0] = u;
	}

	/**
	 * Getter method to retrieve our destination
	 * @return
	 */
	public Vertex<E, V> getDestination() 
	{
		return endpoints[1];
	}

	/**
	 * Setter method to set our destination
	 * @param source
	 */
	public void setDestination(Vertex<E, V> v) 
	{
		endpoints[1] = v;
	}
	
	public void setPosition(Position<Edge<E, V>> pos)
	{
		this.pos = pos;
	}
	
	public Position<Edge<E, V>> getPosition()
	{
		return pos;
	}

	//--Paballo

	public double getWeight()
    {
        return ((Number) element).doubleValue();
    }

    /**
     * Set weight using double (stored as E)
     */
    @SuppressWarnings("unchecked")
    public void setWeight(double weight)
    {
        this.element = (E) (Double) weight;
    }


}




	