package com.github.woooking.so_sorting.document.stackoverflow;

import com.github.woooking.so_sorting.code.cfg.CFG;
import com.github.woooking.so_sorting.code.cfg.ddg.DDG;
import com.github.woooking.so_sorting.code.mining.Miner;
import com.github.woooking.so_sorting.code.mining.MiningGraph;
import com.github.woooking.so_sorting.code.mining.MiningNode;
import com.github.woooking.so_sorting.utils.CFGUtil;
import com.github.woooking.so_sorting.utils.Predicates;
import de.parsemis.graph.Graph;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StackOverflowParser {
	public static Pair<ContentInfo, ContentInfo> parse(String content) {
		Document doc = Jsoup.parse(content);

		Element question = doc.select("div.question").first().select("div.post-text").first();
		ContentInfo questionContent = new ContentInfo(question.toString());
		parseHTMLContent(questionContent);

		Element answer = doc.select("div.accepted-answer").first().select("div.post-text").first();
		ContentInfo answerContent = new ContentInfo(answer.toString());
		parseHTMLContent(answerContent);

		return Pair.of(questionContent, answerContent);
	}

	public static void main(String[] args) {
		List<StackoverflowPost> results = StackExchange.search("parse json array", "java", 10, true, StackOverflowParser::parseSearchResult);
		List<String> codes = new ArrayList<>();
		for (StackoverflowPost result : results) {
			System.out.println(result.questionID);
			codes.addAll(result.codesInAnswer);
		}

		List<DDG> ddgs = codes.stream()
			.map(x -> (DDG) DDG.createCFG(x))
			.filter(Predicates.notNull())
			.collect(Collectors.toList());

		List<Graph<MiningNode, Integer>> graphs = Miner.mineGraphFromDDG(ddgs, Miner.createSetting(3, 3));

		System.out.println(graphs.size());
		int num = 0;

		for (Graph<MiningNode, Integer> graph : graphs) {
			CFG cfg = MiningGraph.createCFGFromMiningGraph(ddgs, graph);
			CFGUtil.saveCFG(cfg, "out/" + num++ + ".png");
		}
	}

	private static ContentInfo parseHTMLContent(ContentInfo content) {
		String contentText = content.getContent();
		if (contentText == null) return content;

		Document htmlRoot = Jsoup.parse(content.getContent(), "UTF-8");

		List<String> paragraphList = parseHTMLNodeToParagraphs(htmlRoot);
		if (paragraphList != null && paragraphList.size() > 0)
			content.getParagraphList().addAll(paragraphList);
		else {
			String codeInfo = content.getContent();
			content.getParagraphList().add(codeInfo);
		}

		return content;
	}

	private static List<String> parseHTMLNodeToParagraphs(Node node) {
		List<String> paragraphList = new ArrayList<>();
		List<Node> childNodes = node.childNodes();
		for (Node childNode : childNodes) {
			if (childNode.nodeName().equals("p") || childNode.nodeName().equals("li")) continue;
			if (childNode.nodeName().equals("pre"))
				childNode.childNodes().stream()
						.filter(n -> n.nodeName().equals("code"))
						.map(n -> StringEscapeUtils.unescapeHtml4(((Element) n).text()))
						.forEach(paragraphList::add);
			else paragraphList.addAll(parseHTMLNodeToParagraphs(childNode));
		}
		return paragraphList;
	}

	public static StackoverflowPost parseSearchResult(SearchResult result) {
		String html = StackExchange.getQuestion(result.questionID);
		StackoverflowPost parsed = new StackoverflowPost(result.questionID, result.title, html);
		if (parsed.codesInAnswer.isEmpty()) return null;
		return parsed;
	}
}
