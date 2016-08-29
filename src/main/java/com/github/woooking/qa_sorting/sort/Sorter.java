package com.github.woooking.qa_sorting.sort;

import com.github.woooking.qa_sorting.code.cfg.ddg.DDG;
import com.github.woooking.qa_sorting.code.cfg.ddg.DDGBlock;
import com.github.woooking.qa_sorting.code.mining.MiningGraph;
import com.github.woooking.qa_sorting.code.mining.MiningNode;
import com.google.common.collect.Multimap;
import de.parsemis.graph.Graph;
import kotlin.jvm.functions.Function2;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Sorter {
	public static double rank(Collection<DDG> ddgs, List<Graph<MiningNode, Integer>> minedGraphs) {
		return minedGraphs.stream().filter(g -> ddgs.stream().anyMatch(ddg -> MiningGraph.findSubDDG(ddg, g) != null)).count();
	}

	public static double rankBySize(Collection<DDG> ddgs, List<Graph<MiningNode, Integer>> minedGraphs) {
		int nodes = 0, matches = 0;
		for (DDG ddg : ddgs) {
			nodes += ddg.getBlocks().size();
			for (Graph<MiningNode, Integer> minedGraph : minedGraphs) {
				Set<DDGBlock> sub = MiningGraph.findSubDDG(ddg, minedGraph);
				if (sub == null) continue;
				matches += sub.size();
			}
		}
		return (double) matches / nodes;
	}

	public static List<String> sort(Multimap<String, DDG> ddgs, List<Graph<MiningNode, Integer>> minedGraphs) {
		List<Pair<String, Double>> scores = new ArrayList<>();

		for (String id : ddgs.keySet()) {
			scores.add(Pair.of(id, rank(ddgs.get(id), minedGraphs)));
		}

		Collections.sort(scores, (x, y) -> y.getRight().compareTo(x.getRight()));

		return scores.stream().map(Pair::getLeft).collect(Collectors.toList());
	}

	public static List<String> sort(Multimap<String, DDG> ddgs, List<Graph<MiningNode, Integer>> minedGraphs, Function2<Collection<DDG>, List<Graph<MiningNode, Integer>>, Double> rankFunc) {
		List<Pair<String, Double>> scores = new ArrayList<>();

		for (String id : ddgs.keySet()) {
			scores.add(Pair.of(id, rankFunc.invoke(ddgs.get(id), minedGraphs)));
		}

		Collections.sort(scores, (x, y) -> y.getRight().compareTo(x.getRight()));

		return scores.stream().map(Pair::getLeft).collect(Collectors.toList());
	}

}
