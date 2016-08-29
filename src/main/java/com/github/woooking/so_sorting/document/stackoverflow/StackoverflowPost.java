package com.github.woooking.so_sorting.document.stackoverflow;

import com.github.woooking.so_sorting.document.IDocument;
import com.github.woooking.so_sorting.utils.ParseUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class StackoverflowPost implements IDocument {
	public long questionID;
	public String title;
	public ContentInfo questionContent;
	public ContentInfo answerContent;
	public List<String> codesInAnswer;

	public StackoverflowPost(long questionID, String title, String html) {
		Pair<ContentInfo, ContentInfo> parsed = StackOverflowParser.parse(html);
		this.questionID = questionID;
		this.title = title;
		this.questionContent = parsed.getLeft();
		this.answerContent = parsed.getRight();
		codesInAnswer = answerContent.getParagraphList().stream()
			.flatMap(p -> ParseUtil.getMethodBodys(p).stream())
			.collect(Collectors.toList());
	}

	@Override
	public List<String> getCodes() {
		return codesInAnswer;
	}

	@Override
	public JSONObject toJson() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", questionID);
		jsonObject.put("link", "http://stackoverflow.com/questions/" + questionID);
		jsonObject.put("title", title);
		return jsonObject;
	}
}
