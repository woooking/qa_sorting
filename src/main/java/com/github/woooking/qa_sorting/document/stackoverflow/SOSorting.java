package com.github.woooking.qa_sorting.document.stackoverflow;

import com.github.woooking.qa_sorting.api.QASorting;

import java.util.List;

/**
 * The example usage of the sort api.
 */
public class SOSorting {

	public static void main(String[] args) {
		List<StackoverflowPost> results = StackExchange.search("regex match", "java", 10, true, StackOverflowParser::parseSearchResult);
		QASorting.sort(results).forEach(System.out::println);
	}
}
