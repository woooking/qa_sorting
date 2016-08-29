package com.github.woooking.so_sorting.document.stackoverflow;

import java.util.ArrayList;
import java.util.List;

public class ContentInfo {

	private String content;
	private List<String> paragraphList;

	public ContentInfo(String content) {
		paragraphList = new ArrayList<>();
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public List<String> getParagraphList() {
		return paragraphList;
	}

	@Override
	public String toString() {
		return content;
	}

}
