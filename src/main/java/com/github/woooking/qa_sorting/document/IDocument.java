package com.github.woooking.qa_sorting.document;

import org.json.JSONObject;

import java.util.List;

/**
 * Interface for documents. A document contains a few code snippets.
 */
public interface IDocument {
	/**
	 * Get code snippets from the document.
	 * @return the list of code snippets
	 */
	List<String> getCodes();

	/**
	 * Transform the document to Json Object, used for server.
	 * @return {@link JSONObject} represents the document
	 */
	JSONObject toJson();
}
