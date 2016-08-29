package com.github.woooking.so_sorting.adt;

import java.util.*;

/**
 * The data structure used for work list algorithm.
 * @param <E> type of elements in the work list
 */
public class WorkingList<E> {
	private Queue<E> queue = new LinkedList<>();
	private Set<E> belong = new HashSet<>();

	/**
	 * Add an element to the work list. If the element is already existed, nothing happens.
	 * @param item the element will be added to the list
	 * @return return <tt>true</tt> if work list is changed
	 */
	public boolean push(E item) {
		if (belong.contains(item)) return false;
		queue.add(item);
		belong.add(item);
		return true;
	}

	/**
	 * Get and remove an element from the work list.
	 * @return the element get from the work list. If the work list is empty, return {@code null}
	 */
	public E pop() {
		E e = queue.poll();
		belong.remove(e);
		return e;
	}

	/**
	 * Clear the whole work list.
	 */
	public void clear() {
		queue.clear();
		belong.clear();
	}

	/**
	 * Return whether the work list is empty.
	 * @return return <tt>true</tt> if the work list is empty
	 */
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	/**
	 * Add some elements to the work list.
	 * @param ite the elements will be added to the list
	 */
	public void pushAll(Iterable<E> ite) {
		ite.forEach(this::push);
	}

}
