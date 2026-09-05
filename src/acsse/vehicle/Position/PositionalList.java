package acsse.vehicle.Position;

import java.util.Iterator;

public interface PositionalList<E> extends Iterable<E>
{
	/**
	 * Returns the number of elements in the list.
	 * 
	 * @return
	 */
	int size();
	
	/**
	 * Tests whether the list is empty.
	 * 
	 * @return
	 */
	boolean isEmpty();
	
	/**
	 * Returns the first Position in the list.
	 * 
	 * @return
	 */
	Position<E> first();
	
	/**
	 * Returns the last Position in the list.
	 * 
	 * @return
	 */
	Position<E> last();
	
	/**
	 * Returns the position immediately before Position p
	 * or null if p is first.
	 * 
	 * @param p
	 * @return
	 * @throws IllegalArgumentException
	 */
	Position<E> before(Position<E> p) throws IllegalArgumentException;
	
	/**
	 * Returns the position immediately after Position p
	 * or null if p is last.
	 * 
	 * @param p
	 * @return node that was inserted after the given position
	 * @throws IllegalArgumentException
	 */
	Position<E> after(Position<E> p) throws IllegalArgumentException;
	
	/**
	 * Inserts element e at the front of the list and
	 * returns it's new Position.
	 * 
	 * @param e
	 * @return
	 */
	Position<E> addFirst(E e);
	
	/**
	 * Inserts an element e at the back of the list and
	 * returns it's new position.
	 * 
	 * @param e
	 * @return the position that was added to the end
	 */
	Position<E> addLast(E e);
	
	/**
	 * Inserts element e immediately before Position p
	 * and returns it's new Position.
	 * 
	 * @param p
	 * @param e
	 * @return the position that was added before p
	 * @throws IllegalArgumentException
	 */
	Position<E> addBefore(Position<E> p, E e) throws IllegalArgumentException;
	
	/**
	 * Inserts element e immediately before Position p
	 * and returns it's new Position.
	 * 
	 * @param p
	 * @param e
	 * @return the position we added after p
	 * @throws IllegalArgumentException
	 */
	Position<E> addAfter(Position<E> p, E e) throws IllegalArgumentException;
	
	/**
	 * Replaces the element stored at Position p 
	 * and returns the replaced element.
	 * 
	 * @param p
	 * @param e
	 * @return the element that has been replaced
	 * @throws IllegalArgumentException
	 */
	 E set(Position<E> p, E e) throws IllegalArgumentException;
	 
	 /**
	  * Removes the element stored at Position p and 
	  * returns it(invalidating p).
	  * 
	  * @param p
	  * @return return the element that was stored at p
	  * @throws IllegalArgumentException
	  */
	 E remove(Position<E> p) throws IllegalArgumentException;
	 
	 Iterator<Position<E>> Iterator();
}
