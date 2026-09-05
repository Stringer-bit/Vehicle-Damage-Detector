package acsse.vehicle.Position;

import java.util.Iterator;

import acsse.vehicle.Node.Node;

public class LinkedPositionalList<E> implements PositionalList<E> 
{
	/**
	 * Instance variables of the LinkedPositionalList
	 */
	private Node<E> header;
	private Node<E> trailer;
	private int size = 0;
	
	/**
	 * Construction to instantiate a new empty 
	 * Positional List
	 */
	public LinkedPositionalList()
	{
		header = new Node<>(null, null, null);
		trailer = new Node<>(null, null, null);
		
		header.setNext(trailer);
	}
	
	/*
	 * Private utility functions
	 */
	
	/**
	 * Validates the position and returns a node
	 * @param p
	 * @return
	 */
	private Node<E> validate(Position<E> p) throws IllegalArgumentException
	{
		/*
		 * checking if the given position p is actually a
		 * node such that it can be casted to a node to
		 * return
		 * 
		 */
		if(!(p instanceof Node))
			throw new IllegalArgumentException("Invalid Position given");
		
		/*
		 * casting our given position to a node safely
		 */
		Node<E> node = (Node<E>) p;
		
		/*
		 * Convention for defunct node
		 */
		if(node.getNext() == null)
			throw new IllegalArgumentException("p is no longer in the list");
		
		/*
		 * returning the node that exists in the list
		 */
		return node;
	}
	
	/**
	 * returns the given node as a Position
	 * 
	 * @param node
	 * @return given node as a Position
	 */
	private Position<E> position(Node<E> node)
	{
		if(node == header || node == trailer)
			return null;
		
		return node;
	}
	
	/**
	 * Function to add a new node in between two given
	 * nodes
	 *  
	 * @param e
	 * @param prev
	 * @param after
	 * @return the node that was input
	 */
	private Position<E> addBetween(E e, Node<E> prev, Node<E> after)
	{
		Node<E> newNode = new Node<>(after, prev, e);
		
		prev.setNext(newNode);
		after.setPrev(newNode);
		size++;
		
		return position(newNode);
	}
	
	/*
	 * Public accessor methods
	 */

	/**
	 * returns the number of elements in the linked list
	 */
	@Override
	public int size() 
	{
		// TODO Auto-generated method stub
		return size;
	}

	/**
	 * Tests whether the list is empty.
	 * 
	 * @return
	 */
	@Override
	public boolean isEmpty() 
	{
		// TODO Auto-generated method stub
		return (size == 0);
	}

	/**
	 * Returns the first Position in the list.
	 * 
	 * @return
	 */
	@Override
	public Position<E> first() 
	{
		// TODO Auto-generated method stub
		return position(header.getNext());
	}

	/**
	 * Returns the last Position in the list.
	 * 
	 * @return
	 */
	@Override
	public Position<E> last() 
	{
		// TODO Auto-generated method stub
		return position(trailer.getPrev());
	}

	/**
	 * Returns the position immediately before Position p
	 * or null if p is first.
	 * 
	 * @param p
	 * @return
	 * @throws IllegalArgumentException
	 */
	@Override
	public Position<E> before(Position<E> p) throws IllegalArgumentException 
	{
		// TODO Auto-generated method stub
		/*
		 * Validate the position and return it as a node
		 * such that we can return the previous position
		 */
		Node<E> node = validate(p);
		
		/*
		 * return the previous position node relative to 
		 * the given node
		 */
		return position(node.getPrev());
	}

	/**
	 * Returns the position immediately after Position p
	 * or null if p is last.
	 * 
	 * @param p
	 * @return node that was inserted after the given position
	 * @throws IllegalArgumentException
	 */
	@Override
	public Position<E> after(Position<E> p) throws IllegalArgumentException 
	{
		// TODO Auto-generated method stub
		/*
		 * Validate the position and return it as a node
		 * such that we can return the previous position
		 */
		Node<E> node = validate(p);
		/*
		 * return the position node after the node that
		 * was given
		 */
		return position(node.getNext());
	}

	/**
	 * Inserts element e at the front of the list and
	 * returns it's new Position.
	 * 
	 * @param e
	 * @return
	 */
	@Override
	public Position<E> addFirst(E e) 
	{
		// TODO Auto-generated method stub
		/*
		 * add a new node between our header which is null
		 * and the node after which is logically our
		 * header
		 */
		return addBetween(e, header, header.getNext());
	}

	/**
	 * Inserts an element e at the back of the list and
	 * returns it's new position.
	 * 
	 * @param e
	 * @return the position that was added to the end
	 */
	@Override
	public Position<E> addLast(E e) 
	{
		// TODO Auto-generated method stub
		/*
		 * add the given element in between our trailer which
		 * is null and the value that is logically the 
		 * trailer such that the given element is now the
		 * trailer
		 */
		return addBetween(e, trailer.getPrev(), trailer);
	}

	/**
	 * Inserts element e immediately before Position p
	 * and returns it's new Position.
	 * 
	 * @param p
	 * @param e
	 * @return the position that was added before p
	 * @throws IllegalArgumentException
	 */
	@Override
	public Position<E> addBefore(Position<E> p, E e) throws IllegalArgumentException 
	{
		// TODO Auto-generated method stub
		/*
		 * Validate the position and return it as a node
		 * such that we can return the previous position
		 */
		Node<E> node = validate(p);
		/*
		 * adding the element before p and returning the
		 * position added
		 */
		return addBetween(e, node.getPrev(), node);
	}

	/**
	 * Inserts element e immediately before Position p
	 * and returns it's new Position.
	 * 
	 * @param p
	 * @param e
	 * @return the position we added after p
	 * @throws IllegalArgumentException
	 */
	@Override
	public Position<E> addAfter(Position<E> p, E e) throws IllegalArgumentException 
	{
		// TODO Auto-generated method stub
		/*
		 * Validate the position and return it as a node
		 * such that we can return the previous position
		 */
		Node<E> node = validate(p);
		/*
		 * add node after given p and return it
		 */
		return addBetween(e, node, node.getNext());
	}

	/**
	 * Replaces the element stored at Position p 
	 * and returns the replaced element.
	 * 
	 * @param p
	 * @param e
	 * @return the element that has been replaced
	 * @throws IllegalArgumentException
	 */
	@Override
	public E set(Position<E> p, E e) throws IllegalArgumentException 
	{
		// TODO Auto-generated method stub
		/*
		 * Validate the position and return it as a node
		 * such that we can return the previous position
		 */
		Node<E> node = validate(p);
		
		/*
		 * store the element that is about to be replaced
		 */
		E removed = node.getElement();
		
		/*
		 * setting the new element
		 */
		node.setElement(e);
		
		/*
		 * returning the element removed
		 */
		return removed;
	}

	/**
	  * Removes the element stored at Position p and 
	  * returns it(invalidating p).
	  * 
	  * @param p
	  * @return return the element that was stored at p
	  * @throws IllegalArgumentException
	  */
	@Override
	public E remove(Position<E> p) throws IllegalArgumentException 
	{
		// TODO Auto-generated method stub
		/*
		 * Store reference to the node given and the nodes
		 * before and after it
		 */
		Node<E> node = validate(p);
		Node<E> prev = node.getPrev();
		Node<E> next = node.getNext();
		
		/*
		 * Changing the nodes before and after the previous
		 * and next nodes
		 */
		prev.setNext(next);
		next.setPrev(prev);
		
		/*
		 * decrementing the size of the list
		 */
		size--;
		
		/*
		 * storing reference to the element of the node
		 * we removed
		 */
		E removed = node.getElement();
		
		/*
		 * Setting the removed node's values to null
		 */
		node.setElement(null);
		node.setNext(null);
		node.setPrev(null);
		
		/*
		 * returning the element that was removed
		 */
		return removed;
	}

	public Iterator<E> iterator() 
	{
		return null;
	}

	public Iterator<Position<E>> Iterator() 
	{
		// TODO Auto-generated method stub
		return new PositionIterator<>(this);
	}

}
