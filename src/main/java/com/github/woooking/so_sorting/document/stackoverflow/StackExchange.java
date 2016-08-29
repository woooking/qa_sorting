package com.github.woooking.so_sorting.document.stackoverflow;

import com.github.woooking.so_sorting.analyze.Constant;
import com.github.woooking.so_sorting.utils.FileUtils;
import com.github.woooking.so_sorting.utils.HttpUtils;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class StackExchange {
	private static Logger logger = LoggerFactory.getLogger("StackExchange");

	public static <T> List<T> search(String query, String tag, int limit, boolean skipNonAccepted, Function<SearchResult, T> callback) {
		List<T> results = new ArrayList<>();
		int page = 0;
		while (results.size() < limit) {
			URI uri = getSearchURI(++page, query, tag);
			JSONObject json = new JSONObject(HttpUtils.get(uri));
			JSONArray items = json.getJSONArray("items");
			boolean hasMore = json.getBoolean("has_more");
			for (Object obj : items) {
				JSONObject item = (JSONObject) obj;
				if (skipNonAccepted && !item.has("accepted_answer_id")) continue;
				String title = item.getString("title");
				String link = item.getString("link");
				long questionID = item.getLong("question_id");
				logger.debug("Get question: {}", title);
				T result = callback.apply(new SearchResult(title, link, questionID));
				if (result != null) results.add(result);
				logger.debug("Get {} result for query {}.", results.size(), query);
				if (results.size() == limit) break;
			}
			if (!hasMore) break;
		}

		return results;
	}

	public static String getQuestion(long id) {
		File cached = new File(Constant.CACHE + "/" + id);
		if (cached.exists()) return FileUtils.getFileContent(cached);
		String html = HttpUtils.get("http://stackoverflow.com/questions/" + id);
		FileUtils.writeFile(cached, html);
		return html;
	}

	private static URI getSearchURI(int page, String index, String tag) {
		try {
			return new URIBuilder()
				.setScheme("http")
				.setHost("api.stackexchange.com")
				.setPath("/2.2/search/advanced")
				.setParameter("page", "" + page)
				.setParameter("pagesize", "20")
				.setParameter("order", "desc")
				.setParameter("sort", "relevance")
				.setParameter("tagged", tag)
				.setParameter("q", index)
				.setParameter("site", "stackoverflow")
				.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

}
