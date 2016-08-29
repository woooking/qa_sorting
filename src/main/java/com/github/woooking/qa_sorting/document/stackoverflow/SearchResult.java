package com.github.woooking.qa_sorting.document.stackoverflow;

public class SearchResult {
	public String title;
	public String link;
	public long questionID;

	public SearchResult(String title, String link, long questionID) {
		this.title = title;
		this.link = link;
		this.questionID = questionID;
	}
}
