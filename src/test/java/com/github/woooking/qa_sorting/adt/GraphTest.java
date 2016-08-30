package com.github.woooking.qa_sorting.adt;

import com.github.woooking.qa_sorting.adt.graph.Graph;
import com.github.woooking.qa_sorting.adt.graph.Node;
import com.github.woooking.qa_sorting.adt.graph.algorithm.DominanceFrontierResolver;
import com.github.woooking.qa_sorting.adt.graph.algorithm.ImmediateDominatorResolver;
import com.github.woooking.qa_sorting.adt.impl.NodeImpl;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.*;

public class GraphTest {
	private NodeImpl node1 = new NodeImpl("1");
	private NodeImpl node2 = new NodeImpl("2");
	private NodeImpl node3 = new NodeImpl("3");
	private NodeImpl node4 = new NodeImpl("4");
	private NodeImpl node5 = new NodeImpl("5");
	private NodeImpl node6 = new NodeImpl("6");
	private NodeImpl node7 = new NodeImpl("7");
	private NodeImpl node8 = new NodeImpl("8");
	private NodeImpl node9 = new NodeImpl("9");
	private Graph g;


	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Before
	public void buildGraph() {
		node1.add(node2);
		node2.add(node3, node4);
		node3.add(node5, node6);
		node4.add(node7);
		node5.add(node2);
		node6.add(node8);
		node7.add(node8);
		node8.add(node9);
		g = () -> ImmutableSet.of(node1, node2, node3, node4, node5, node6, node7, node8, node9);
	}

	@Test
	public void testImmediateDominator() {
		ImmediateDominatorResolver solver = new ImmediateDominatorResolver(g, node1);
		Map<Node, Node> result = solver.calculate();
		assertNull(result.get(node1));
		assertEquals(result.get(node2), node1);
		assertEquals(result.get(node3), node2);
		assertEquals(result.get(node4), node2);
		assertEquals(result.get(node5), node3);
		assertEquals(result.get(node6), node3);
		assertEquals(result.get(node7), node4);
		assertEquals(result.get(node8), node2);
		assertEquals(result.get(node9), node8);
	}

	@Test
	public void testDominanceFrontier() {
		DominanceFrontierResolver solver = new DominanceFrontierResolver(g, node1);
		Multimap<Node, Node> result = solver.calculate();
		assertTrue(isEquals(result.get(node1), ImmutableSet.of()));
		assertTrue(isEquals(result.get(node2), ImmutableSet.of(node2)));
		assertTrue(isEquals(result.get(node3), ImmutableSet.of(node2, node8)));
		assertTrue(isEquals(result.get(node4), ImmutableSet.of(node8)));
		assertTrue(isEquals(result.get(node5), ImmutableSet.of(node2)));
		assertTrue(isEquals(result.get(node6), ImmutableSet.of(node8)));
		assertTrue(isEquals(result.get(node7), ImmutableSet.of(node8)));
		assertTrue(isEquals(result.get(node8), ImmutableSet.of()));
		assertTrue(isEquals(result.get(node9), ImmutableSet.of()));
	}

	private <T> boolean isEquals(Collection<T> a, Collection<T> b) {
		return a.containsAll(b) && b.containsAll(a);
	}
}
