package acsse.vehicle.Node;

import acsse.vehicle.Position.Position;

public class Node<T> implements Position<T>
{
	private T element = null;
	private Node<T> next = null;
	private Node<T> prev = null;
	//Modified Nod class By Paballo From Tumisang Code
	int id;

    int size = 0;
    int sumIntensity = 0;
    double meanIntensity;

    private int minX = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxY = Integer.MIN_VALUE;
    private java.util.HashSet<String> pixelSet; // Store "x,y" strings //added configuration for Image RAg 
	
	public Node(int id) {
        this.id = id;
    }
    public void addPixel(int x, int y, int intensity) {

        size++;
        sumIntensity += intensity;

        if (x < getMinX()) setMinX(x);
        if (x > getMaxX()) setMaxX(x);
        if (y < getMinY()) setMinY(y);
        if (y > getMaxY()) setMaxY(y);
       //added configuration for Image RAg 
        if (pixelSet == null) pixelSet = new java.util.HashSet<>();
        pixelSet.add(x + "," + y);
    }

    public void finalNode() {
        if (size > 0) {
            meanIntensity = (double) sumIntensity / size;
        }
    }
    //Paballo adding Gettor methods
    public int getId() {
		return id;
	}
    public int getSize() {
		return this.size;
	}
    public double getMeanIntensity() {
		return this.meanIntensity;
	}
    public int getMinX() {
		return minX;
	}
	public void setMinX(int minX) {
		this.minX = minX;
	}
	public int getMinY() {
		return minY;
	}
	public void setMinY(int minY) {
		this.minY = minY;
	}
	public int getMaxX() {
		return maxX;
	}
	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return maxY;
	}
	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
	

	/*
	 * ----------------------------------------------------------------------------------------------------
	 */
	
	public Node(Node<T> next, Node<T> prev, T element)
	
	{
		this.next = next;
		this.prev = prev;
		this.element = element;
	
	}
	
	
	/*
	 * Getter and setter functions
	 */
	public T getElement() 
	{
		if(next == null)
			throw new IllegalStateException("Position no longer valid");
		return element;
	}

	public void setElement(T element) 
	{
		this.element = element;
	}

	public Node<T> getNext() 
	{
		return next;
	}

	public void setNext(Node<T> next) 
	{
		this.next = next;
	}

	public Node<T> getPrev() 
	{
		return prev;
	}

	public void setPrev(Node<T> prev) 
	{
		this.prev = prev;
	}
	
	//Add configurations for Image Construction
	
	public boolean containsPixel(int x, int y) {
	    // Check if this node contains the pixel at (x,y)
	    // You'll need to store pixel coordinates or implement this based on your data structure
	    // Option 1: If you have a Set of pixel coordinates
	    // return pixelSet.contains(x + "," + y);
	    
	    // Option 2: If you store min/max bounds and all pixels are contiguous
	    return (x >= minX && x <= maxX && y >= minY && y <= maxY);
	}
	

}