package com.github.woooking.qa_sorting.adt;

import com.github.woooking.qa_sorting.adt.impl.LispListImpl;
import com.google.common.collect.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LispListTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void testFunction() {
		LispList<Integer> list = new LispListImpl<>(Lists.newArrayList(1, 2, 3));
		assertFalse(list.isEmpty());
		assertTrue(list.car() == 1);
		list = list.cdr();
		assertTrue(list.car() == 2);
		list = list.cdr();
		assertTrue(list.car() == 3);
	}

	@Test
	public void testEmpty() {
		LispList<Integer> list = new LispListImpl<>(Lists.newArrayList());
		assertTrue(list.isEmpty());
		exception.expect(NoSuchElementException.class);
		list.car();
	}

	@Test
	public void testImmutable() {
		LispList<Integer> list = new LispListImpl<>(Lists.newArrayList(1, 2, 3));
		assertTrue(list.car() == 1);
		LispList<Integer> cdrList = list.cdr();
		assertTrue(list.car() == 1);
		assertTrue(cdrList.car() == 2);
	}
}
