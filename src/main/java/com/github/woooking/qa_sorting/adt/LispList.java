package com.github.woooking.qa_sorting.adt;

/**
 * A lisp-like List.
 * @param <T> type of the element in the list
 */
public interface LispList<T> {
	/**
	 * Return whether the list is empty or not.
	 * @return <tt>true</tt> if this list is empty
	 */
	boolean isEmpty();

	/**
	 * Get the first element of the list. If the list is empty, a {@link java.util.NoSuchElementException NoSuchElementException} will be throwed.
	 * @return the first element of the list
	 */
	T car();

	/**
	 * Get the list without the first element. If the list is empty, a {@link java.util.NoSuchElementException NoSuchElementException} will be throwed.
	 * @return the list without the first element
	 */
	LispList<T> cdr();
}
