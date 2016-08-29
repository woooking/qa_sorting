package com.github.woooking.so_sorting.analyze;

import com.github.woooking.so_sorting.code.cfg.ddg.DDG;
import com.github.woooking.so_sorting.code.mining.Miner;
import com.github.woooking.so_sorting.code.mining.MiningNode;
import com.github.woooking.so_sorting.document.stackoverflow.StackoverflowPost;
import com.github.woooking.so_sorting.sort.Sorter;
import com.github.woooking.so_sorting.document.stackoverflow.StackExchange;
import com.github.woooking.so_sorting.document.stackoverflow.StackOverflowParser;
import com.github.woooking.so_sorting.utils.Predicates;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.parsemis.graph.Graph;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

public class Generator {
	public static void test(String query) {
		generateSO(query);
		generateMine(query);
		generateMineRatio(query);
	}

	private static void generateMine(String query) {
		File resultFile = new File(Constant.MINE_RANK + "/" + query);
		if (resultFile.exists()) return;

		try {
			List<StackoverflowPost> results = StackExchange.search(query, "java", 10, true, StackOverflowParser::parseSearchResult);
			Multimap<String, DDG> ddgs = HashMultimap.create();

			results.stream().flatMap(
					r -> r.codesInAnswer.stream()
							.map(c -> (DDG) DDG.createCFG(c))
							.filter(Predicates.notNull())
							.map(c -> Pair.of(r.questionID, c))
			).forEach(p -> ddgs.put(p.getLeft().toString(), p.getRight()));

			List<Graph<MiningNode, Integer>> graphs = Miner.mineGraphFromDDG(ddgs.values(), Miner.createSetting(3, 3));

			List<String> idList = Sorter.sort(ddgs, graphs);

			PrintStream result = new PrintStream(resultFile);
			idList.forEach(result::println);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void generateMineRatio(String query) {
		File resultFile = new File(Constant.MINE_RANK_BY_RATIO + "/" + query);
		if (resultFile.exists()) return;

		try {
			List<StackoverflowPost> results = StackExchange.search(query, "java", 10, true, StackOverflowParser::parseSearchResult);
			Multimap<String, DDG> ddgs = HashMultimap.create();

			results.stream().flatMap(
					r -> r.codesInAnswer.stream()
							.map(c -> (DDG) DDG.createCFG(c))
							.filter(Predicates.notNull())
							.map(c -> Pair.of(r.questionID, c))
			).forEach(p -> ddgs.put(p.getLeft().toString(), p.getRight()));

			List<Graph<MiningNode, Integer>> graphs = Miner.mineGraphFromDDG(ddgs.values(), Miner.createSetting(3, 3));

			List<String> idList = Sorter.sort(ddgs, graphs, Sorter::rankBySize);

			PrintStream result = new PrintStream(resultFile);
			idList.forEach(result::println);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void generateSO(String query) {
		File resultFile = new File(Constant.SO_RANK + "/" + query);
		if (resultFile.exists()) return;

		try {
			List<Long> results = StackExchange.search(query, "java", 10, false, x -> x).stream().map(r -> r.questionID).collect(Collectors.toList());
			PrintStream ps = new PrintStream(resultFile);
			results.forEach(ps::println);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		for (String query : Constant.QUERIES) {
			test(query);
		}
	}
}
