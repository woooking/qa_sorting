package com.github.woooking.qa_sorting.analyze;

import com.github.woooking.qa_sorting.document.stackoverflow.ContentInfo;
import com.github.woooking.qa_sorting.document.stackoverflow.StackOverflowParser;
import com.github.woooking.qa_sorting.utils.FileUtils;
import com.github.woooking.qa_sorting.utils.ParseUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Helper {
	private static Logger logger = LoggerFactory.getLogger("Helper");

	public static void cleanCache() {
		int total = 0, deleted = 0;
		File cached = new File(Constant.CACHE);
		for (File file : cached.listFiles()) {
			++total;
			String html = FileUtils.getFileContent(file);
			Pair<ContentInfo, ContentInfo> parsed = StackOverflowParser.parse(html);
			ContentInfo answerInfo = parsed.getRight();
			List<String> codes = new ArrayList<>();
			answerInfo.getParagraphList().stream().forEach(p -> codes.addAll(ParseUtil.getMethodBodys(p)));
			if (codes.isEmpty()) {
				file.delete();
				++deleted;
			}
		}
		logger.info("Cleaned {} of {}", deleted, total);
	}

	public static void cleanData() {
		for (File dir: new File(Constant.CODE_DATA).listFiles())
			if (!Arrays.asList(Constant.QUERIES).contains(dir.getName()))
				FileUtils.deleteDirectory(dir);


		for (File file: new File(Constant.SCORE).listFiles())
			if (!Arrays.asList(Constant.QUERIES).contains(file.getName()))
				file.delete();

		for (File file: new File(Constant.MINE_RANK).listFiles())
			if (!Arrays.asList(Constant.QUERIES).contains(file.getName()))
				file.delete();


	}

	public static void runAll() {
		cleanData();
		Ranker.main(null);
		Generator.main(null);
		Evaluator.main(null);
	}

	public static List<Long> readIDs(String query, String folder) {
		File testRankFile = new File(folder + "/" + query);
		List<Long> ids = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(testRankFile);
			while (scanner.hasNextLong()) {
				long id = scanner.nextLong();
				ids.add(id);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return ids;
	}

	public static void main(String[] args) {
//		cleanCache();
//		cleanData();
		runAll();
	}
}
