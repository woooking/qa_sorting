package com.github.woooking.qa_sorting.document.stackoverflow;

import com.github.woooking.qa_sorting.api.QAServer;
import com.google.common.io.Resources;

import java.net.URI;

/**
 * The example server for Stackoverflow QA documents of java.
 */
public class SOServer {
	public static void main(String[] args) throws Exception {
		URI web = Resources.getResource("web").toURI();
		new QAServer(8080, web.toString())
			.addService("/query", (q, l) -> StackExchange.search(q, "java", l, true, StackOverflowParser::parseSearchResult), 10)
			.luanch();
	}
}
