package com.github.woooking.so_sorting.adt.impl;

import com.github.woooking.so_sorting.adt.LispList;
import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A implementation of lisp-like list.
 * @param <T>  type of the element in the list
 */
public class LispListImpl<T> implements LispList<T> {
	private ImmutableList<T> list;

	public LispListImpl(List<T> list) {
		this.list = ImmutableList.copyOf(list);
	}

	public LispListImpl(Iterator<? extends T> iterator) {
		this.list = ImmutableList.copyOf(iterator);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public T car() {
		try {
			return list.get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new NoSuchElementException();
		}
	}

	public LispListImpl<T> cdr() {
		try {
			return new LispListImpl<>(list.subList(1, list.size()));
		} catch (IndexOutOfBoundsException e) {
			throw new NoSuchElementException();
		}
	}
}
