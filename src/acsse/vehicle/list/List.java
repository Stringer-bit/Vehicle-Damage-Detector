package acsse.vehicle.list;


public interface List<E> 
{
	void add(E element);
    E get(int index);
    int size();
    boolean isEmpty();
}

