package acsse.vehicle.Position;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PositionIterator<E> implements Iterator<Position<E>>
{
	private Position<E> cursor;
	private Position<E> recent;
	private LinkedPositionalList<E> list;
	
	public PositionIterator(LinkedPositionalList<E> list)
	{
		cursor = list.first();
		recent = null;
		this.list = list;
	}

	@Override
	public boolean hasNext() 
	{
		return (cursor == null);
	}

	@Override
	public Position<E> next() throws NoSuchElementException
	{
		if(cursor == null)
			throw new NoSuchElementException("Nothing left");
		
		recent = cursor;
		cursor = list.after(cursor);
		
		return cursor;
	}
	
	public void remove(Position<E> p) throws IllegalStateException
	{
		if(recent == null)
			throw new IllegalStateException("No such element");
		
		list.remove(recent);
		
		recent = null;
	}

}
