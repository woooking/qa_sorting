package com.github.woooking.so_sorting.api;

import com.github.woooking.so_sorting.code.cfg.ddg.DDG;
import com.github.woooking.so_sorting.code.mining.Miner;
import com.github.woooking.so_sorting.code.mining.MiningNode;
import com.github.woooking.so_sorting.document.IDocument;
import com.github.woooking.so_sorting.sort.Sorter;
import com.github.woooking.so_sorting.utils.Predicates;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import de.parsemis.graph.Graph;
import kotlin.jvm.functions.Function2;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The helper class for the sort api.
 */
public class QASorting {
	/**
	 * The sort api of this tool. Input the documents, the method will calculate the most useful documents using
	 * internal sort algorithm based on code pattern.
	 *
	 * @param documents the documents need to be sorted
	 * @param <T>       the type of the documents
	 * @return documents already be sorted
	 */
	public static <T extends IDocument> List<T> sort(List<T> documents) {
		return sort(documents, null);
	}

	/**
	 * The sort api of this tool. Input the documents, the method will calculate the most useful documents using
	 * input function.
	 *
	 * @param documents the documents need to be sorted
	 * @param rankFunc the rank function
	 * @param <T>       the type of the documents
	 * @return documents already be sorted
	 */
	public static <T extends IDocument> List<T> sort(List<T> documents, Function2<Collection<DDG>, List<Graph<MiningNode, Integer>>, Double> rankFunc) {
		Map<String, T> documentMap = new ImmutableMap.Builder<String, T>().putAll(
			IntStream.range(0, documents.size()).mapToObj(i -> Pair.of("" + i, documents.get(i))).collect(Collectors.toList())
		).build();
		Multimap<String, DDG> ddgs = HashMultimap.create();

		documentMap.entrySet().stream().flatMap(
			entry -> entry.getValue().getCodes().stream()
				.map(c -> (DDG) DDG.createCFG(c))
				.filter(Predicates.notNull())
				.map(c -> Pair.of(entry.getKey(), c))
		).forEach(p -> ddgs.put(p.getLeft(), p.getRight()));

		List<Graph<MiningNode, Integer>> graphs = Miner.mineGraphFromDDG(ddgs.values(), Miner.createSetting(3, 3));

		List<String> ids;
		if (rankFunc == null) ids = Sorter.sort(ddgs, graphs);
		else ids = Sorter.sort(ddgs, graphs, rankFunc);

		return ids.stream().map(documentMap::get).collect(Collectors.toList());
	}

}
