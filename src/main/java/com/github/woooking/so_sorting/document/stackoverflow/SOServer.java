package com.github.woooking.so_sorting.document.stackoverflow;

import com.github.woooking.so_sorting.api.QAServer;

/**
 * The example server for Stackoverflow QA documents of java.
 */
public class SOServer {
	public static void main(String[] args) throws Exception {
		new QAServer(8080, "src/main/web")
			.addService("/query", (q, l) -> StackExchange.search(q, "java", l, true, StackOverflowParser::parseSearchResult), 10)
			.luanch();
	}
}
