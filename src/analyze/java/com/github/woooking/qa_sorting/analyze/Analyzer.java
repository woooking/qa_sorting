package com.github.woooking.qa_sorting.analyze;

import com.github.woooking.qa_sorting.code.cfg.ddg.DDG;
import com.github.woooking.qa_sorting.code.mining.Miner;
import com.github.woooking.qa_sorting.code.mining.MiningGraph;
import com.github.woooking.qa_sorting.code.mining.MiningNode;
import com.github.woooking.qa_sorting.sort.Sorter;
import com.github.woooking.qa_sorting.document.stackoverflow.StackoverflowPost;
import com.github.woooking.qa_sorting.document.stackoverflow.StackExchange;
import com.github.woooking.qa_sorting.document.stackoverflow.StackOverflowParser;
import com.github.woooking.qa_sorting.utils.Predicates;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.parsemis.graph.Graph;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Analyzer {
	public static void analyze(String query) {
//		List<Post> results = getPosts(query);
		List<StackoverflowPost> results = getPostsOffline(query);
		outputLink(results);

		Multimap<Long, DDG> ddgs = HashMultimap.create();

		results.stream().flatMap(
				r -> r.codesInAnswer.stream()
						.map(c -> (DDG) DDG.createCFG(c))
						.filter(Predicates.notNull())
						.map(c -> Pair.of(r.questionID, c))
		).forEach(p -> ddgs.put(p.getLeft(), p.getRight()));
		outputDDGInfo(ddgs);

		List<Graph<MiningNode, Integer>> graphs = Miner.mineGraphFromDDG(ddgs.values(), Miner.createSetting(3, 3));
		outputMiningInfo(ddgs, graphs);
	}

	private static List<StackoverflowPost> getPosts(String query) {
		return StackExchange.search(query, "java", 10, true, StackOverflowParser::parseSearchResult);
	}

	private static List<StackoverflowPost> getPostsOffline(String query) {
		File resultFile = new File(Constant.MINE_RANK + "/" + query);
		List<StackoverflowPost> result = new ArrayList<>();
		try {
			Scanner sc = new Scanner(resultFile);
			while (sc.hasNextLong()) {
				long id = sc.nextLong();
				String html = StackExchange.getQuestion(id);
				StackoverflowPost parsed = new StackoverflowPost(id, "", html);
				result.add(parsed);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static void outputLink(List<StackoverflowPost> results) {
		System.out.println("Document Links:");
		results.stream().map(x -> "http://stackoverflow.com/questions/" + x.questionID).forEach(System.out::println);
	}

	private static void outputDDGInfo(Multimap<Long, DDG> ddgs) {
		System.out.println("DDG Info");
		System.out.println("Total DDG Count: " + ddgs.size());
	}

	private static void outputMiningInfo(Multimap<Long, DDG> ddgs, List<Graph<MiningNode, Integer>> graphs) {
		System.out.println("Mining Info");
		System.out.println("Total Result Count: " + graphs.size());
		graphs.forEach(MiningGraph::printMiningGraph);

		List<Pair<Long, Double>> scores = new ArrayList<>();
		for (Long id : ddgs.keySet()) {
			scores.add(Pair.of(id, Sorter.rank(ddgs.get(id), graphs)));
		}

		Collections.sort(scores, (x, y) -> y.getRight().compareTo(x.getRight()));

		System.out.println("Result:");
		scores.forEach(System.out::println);
	}

	public static void main(String[] args) {
		analyze("create tcp server");
	}
}
