package acsse.vehicle.Position;

public interface Position<E> 
{
	/**
	 * Returns the element stored at this position
	 * 
	 * @return
	 * @throws IllegalStateException
	 */
	E getElement() throws IllegalStateException;
}
