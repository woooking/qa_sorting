package com.github.woooking.so_sorting.api;

import com.github.woooking.so_sorting.document.IDocument;
import com.github.woooking.so_sorting.document.IIndexer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The main class of the QA server.
 */
public class QAServer extends Server {
	private class QueryHandler<T extends IDocument> extends HttpServlet {
		private IIndexer<T> indexer;
		private int limit;

		public QueryHandler(IIndexer<T> indexer, int limit) {
			this.limit = limit;
			this.indexer = indexer;
		}

		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String query = request.getParameter("query");
			if (query == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			JSONObject result = getNewResult(query);

			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(result.toString());
		}

		private JSONObject getNewResult(String query) {
			List<T> originResult = indexer.getDocuments(query, limit);
			List<T> newResult = QASorting.sort(originResult);
			JSONArray originArray = new JSONArray(originResult.stream().map(IDocument::toJson).collect(Collectors.toList()));
			JSONArray newArray = new JSONArray(newResult.stream().map(IDocument::toJson).collect(Collectors.toList()));
			JSONObject result = new JSONObject();
			result.put("origin", originArray);
			result.put("new", newArray);
			return result;
		}
	}

	private WebAppContext ctx;

	/**
	 * Create a QA server, specific the port, the path contains web pages and the query url.
	 * @param port the port which the QA server listen on (0 for a random port)
	 * @param dirPath the path of directory where web pages are
	 */
	public QAServer(int port, String dirPath)  {
		super(port);
		ctx = new WebAppContext(dirPath, "");
	}

	/**
	 * Add a service to the server.
	 * @param url the url for the service
	 * @param indexer the indexer specifics how to get document from query
	 * @param limit the max number of documents (0 means no limit)
	 * @param <T> the type of the document
	 * @return this server
	 */
	public <T extends IDocument> QAServer addService(String url, IIndexer<T> indexer, int limit) {
		ctx.addServlet(new ServletHolder(new QueryHandler<>(indexer, limit)), url);
		return this;
	}

	/**
	 * Luanch the server.
	 * @throws Exception the exception when launching the server
	 */
	public void luanch() throws Exception {
		setHandler(ctx);
		start();
		join();
	}
}
