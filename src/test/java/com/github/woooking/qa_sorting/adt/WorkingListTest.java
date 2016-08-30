package com.github.woooking.qa_sorting.adt;

import com.google.common.collect.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class WorkingListTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void testFunction() {
		WorkingList<Integer> list = new WorkingList<>();
		assertTrue(list.isEmpty());
		assertTrue(list.push(1));
		assertTrue(list.push(2));
		assertFalse(list.push(2));
		assertTrue(list.push(3));
		assertFalse(list.isEmpty());
		while(!list.isEmpty()) {
			assertFalse(list.isEmpty());
			list.pop();
		}
		assertTrue(list.isEmpty());
		assertNull(list.pop());
	}

	@Test
	public void testPushAll() {
		WorkingList<Integer> list = new WorkingList<>();
		list.pushAll(Lists.newArrayList(1, 2, 3, 4, 5));
		assertFalse(list.isEmpty());
		int sum = 0;
		while(!list.isEmpty()) {
			sum += list.pop();
		}
		assertEquals(sum, 15);
	}

}
