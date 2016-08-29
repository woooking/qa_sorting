package com.github.woooking.qa_sorting.utils;

import com.github.woooking.qa_sorting.code.cfg.CFG;
import com.github.woooking.qa_sorting.code.cfg.CFGBlock;
import com.github.woooking.graphlib.KotNode;
import com.github.woooking.graphlib.graph.DirectedNodeGraph;
import com.github.woooking.graphlib.graphviz.BasicDirectedEdgeFormatter;
import com.github.woooking.graphlib.graphviz.BasicGraphFormatter;
import com.github.woooking.graphlib.graphviz.BasicNodeFormatter;
import com.github.woooking.graphlib.graphviz.GraphFormatter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import kotlin.Unit;

public class CFGUtil {
	public static void printCFG(CFG cfg) {
		cfg.getBlocks().forEach(b ->
			b.getNexts().forEach(n -> System.out.println(b + " -> " + n))
		);

		System.out.println("-----");

		cfg.getBlocks().forEach(b -> {
			System.out.println(b);
			b.getStatements().forEach(System.out::println);
			System.out.println();
		});
	}

	public static void saveCFG(CFG cfg, String fileName) {
		BasicNodeFormatter nodeFormatter = new BasicNodeFormatter<String, Unit>();
		BasicDirectedEdgeFormatter edgeFormatter = new BasicDirectedEdgeFormatter(nodeFormatter, false);
		BasicGraphFormatter graphFormatter = new BasicGraphFormatter(nodeFormatter, edgeFormatter);
		DirectedNodeGraph<CFGBlock> g = new DirectedNodeGraph<>();

		BiMap<CFGBlock, KotNode<CFGBlock, Unit>> map = HashBiMap.create();

		for (CFGBlock block : cfg.getBlocks()) {
			KotNode<CFGBlock, Unit> node = g.addNode(block);
			map.put(block, node);
		}

		for (CFGBlock block : cfg.getBlocks()) {
			for (CFGBlock next : block.getNexts()) {
				KotNode<CFGBlock, Unit> nodeA = map.get(block);
				KotNode<CFGBlock, Unit> nodeB = map.get(next);
				g.addEdge(nodeA, nodeB);
			}
		}
		System.out.println("===" + fileName + "===");
		System.out.println(graphFormatter.toDot(g));
		graphFormatter.save(g, GraphFormatter.Type.PNG, fileName);
	}
}
