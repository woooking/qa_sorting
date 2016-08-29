package com.github.woooking.so_sorting.document;

import java.util.List;

/**
 * Interface for document indexer. A indexer receives a query and a number limit, return documents (no more than limit) related to query.
 * @param <T> the type of the document
 */
@FunctionalInterface
public interface IIndexer<T extends IDocument> {
	List<T> getDocuments(String query, int limit);
}
