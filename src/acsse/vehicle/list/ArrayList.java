package acsse.vehicle.list;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayList<E> implements Iterable<E>
{

    private Object[] data;
    private int size;

    public ArrayList() 
    {
        data = new Object[10];
        size = 0;
    }

    public void add(E element) {
        if (size == data.length) {
            grow();
        }
        data[size++] = element;
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return (E) data[index];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void grow() {
        Object[] newData = new Object[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    @Override
    public Iterator<E> iterator() 
    {
        return new Iterator<E>() 
        {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @SuppressWarnings("unchecked")
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (E) data[index++];
            }
        };
    }
}

