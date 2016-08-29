package com.github.woooking.qa_sorting.adt.graph.algorithm;

import com.github.woooking.qa_sorting.adt.graph.Graph;
import com.github.woooking.qa_sorting.adt.graph.Node;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Map;

/**
 * The class to calculate dominance frontier.
 */
public class DominanceFrontierResolver {
	private Multimap<Node, Node> df;
	private Map<Node, Node> idom;
	private Multimap<Node, Node> child;
	private Graph g;
	private Node entry;

	/**
	 * Build the resolver of given {@link Graph} g and entry {@link Node} entry.
	 * @param g The given graph
	 * @param entry The entry of the gragh g
	 */
	public DominanceFrontierResolver(Graph g, Node entry) {
		this.g = g;
		this.entry = entry;
	}

	/**
	 * Calculate the dominance frontier of the {@link Graph}.
	 * @return The dominance frontier multimap where each node as key and its dominance frontiers as values.
	 */
	public Multimap<Node, Node> calculate() {
		// step 1: calc idom
		idom = new ImmediateDominatorResolver(g, entry).calculate();

		// step 2: build idom tree
		child = HashMultimap.create();
		idom.forEach((k, v) -> child.put(v, k));

		// step 3: compute dominance frontier
		df = HashMultimap.create();
		compute(entry);
		return df;
	}

	/**
	 * Get the immediate dominance tree of the {@link Graph}. Must call {@link #calculate()} first.
	 * @return The immediate dominance tree multimap where each node as key and its children as values.
	 */
	public Multimap<Node, Node> getIDomTree() {
		return child;
	}

	private void compute(Node n) {
		// calc DF local
		n.getNexts().forEach(y -> {
			if (idom.get(y) != n) df.put(n, y);
		});

		// calc DF up
		child.get(n).forEach(c -> {
			compute(c);
			df.get(c).forEach(w -> {
				if (!isDominator(n, w) || n == w) df.put(n, w);
			});
		});
	}

	/**
	 * Judge whether a {@link Node} a is a dominator of another {@link Node} b
	 *
	 * @param a The {@link Node} a
	 * @param b The dominator {@link Node} b
	 * @return whether a is a dominator of b
	 */
	private boolean isDominator(Node a, Node b) {
		while (b != null) {
			if (a == b) return true;
			b = idom.get(b);
		}
		return false;
	}
}
