package com.github.woooking.qa_sorting.analyze;

import com.google.common.math.DoubleMath;
import com.google.common.math.IntMath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Evaluator {
	private static PrintStream evaluation;
	private static int[] ks = new int[]{1, 2, 3, 4, 5};
	private static int caseNum = 0;
	private static double[] stackoverflowScore = new double[ks.length];
	private static double[] mineScore = new double[ks.length];
	private static double[] mineRatioScore = new double[ks.length];
	private static double[] patternScore = new double[ks.length];

	static {
		try {
			evaluation = new PrintStream(Constant.EVALUATION);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void evaluate(String query) {
		File rankFile = new File(Constant.SCORE + "/" + query);

		try {
			Map<Long, Integer> ranks = new HashMap<>();
			Scanner rankScanner = new Scanner(rankFile);

			List<Long> stackoverflow = getSORank(query);
			List<Long> mine = getMineRank(query);
			List<Long> mineRatio = Helper.readIDs(query, Constant.MINE_RANK_BY_RATIO);
			List<Long> pattern = Helper.readIDs(query, Constant.MINE_RANK);

			while (rankScanner.hasNext()) {
				String[] line = rankScanner.nextLine().split(" ");
				long id = Long.parseLong(line[0]);
				int score = Integer.parseInt(line[1]);
				ranks.put(id, score);
			}

			for (int i = 0; i < ks.length; ++i) {
				int k = ks[i];
				double normalizationBest = best(k, new ArrayList<>(ranks.values()));
				double normalizationWorst = worst(k, new ArrayList<>(ranks.values()));
				double normalization = normalizationBest - normalizationWorst;
				double stackoverflowResult = (cumulating(k, stackoverflow.stream().map(ranks::get).collect(Collectors.toList())) - normalizationWorst) / normalization;
				double mineResult = (cumulating(k, mine.stream().map(ranks::get).collect(Collectors.toList())) - normalizationWorst) / normalization;
				double mineRatioResult = (cumulating(k, mineRatio.stream().map(ranks::get).collect(Collectors.toList())) - normalizationWorst) / normalization;
				double patternResult = (cumulating(k, pattern.stream().map(ranks::get).collect(Collectors.toList())) - normalizationWorst) / normalization;
				stackoverflowScore[i] += stackoverflowResult;
				mineScore[i] += mineResult;
				mineRatioScore[i] += mineRatioResult;
				patternScore[i] += patternResult;
				evaluation.println(String.format("%s\t%d\t%.3f\t%.3f\t%.3f\t%.3f", query, k, stackoverflowResult, mineResult, mineRatioResult, patternResult));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static List<Long> getSORank(String query) {
		return Helper.readIDs(query, Constant.SO_RANK);
	}

	private static List<Long> getMineRank(String query) {
		List<Long> ids = Helper.readIDs(query, Constant.SO_RANK);
		List<Long> mine = Helper.readIDs(query, Constant.MINE_RANK);

		int pos = 0;
		for (int i = 0; i < ids.size(); ++i) {
			Long id = ids.get(i);
			if (mine.contains(id)) ids.set(i, mine.get(pos++));
		}

		return ids;
	}

	public static void main(String[] args) {
		for (String query : Constant.QUERIES) {
			++caseNum;
			evaluate(new File(Constant.SCORE + "/" + query).getName());
		}
		System.out.println("\tstackoverflow\tmine_replace\tmine_ratio\tpattern only");
		for (int i = 0; i < ks.length; ++i) {
			System.out.println(String.format("Top %d:\t%.3f\t%.3f\t%.3f\t%.3f", ks[i], stackoverflowScore[i] / caseNum, mineScore[i] / caseNum, mineRatioScore[i] / caseNum, patternScore[i] / caseNum));
		}
	}

	public static int gain(int level) {
		return IntMath.pow(2, level - 1) - 1;
//		return level - 1;
	}

	public static double discount(int pos) {
		return DoubleMath.log2(pos + 1);
	}

	public static double best(int k, List<Integer> levels) {
		Collections.sort(levels, (x, y) -> y.compareTo(x));
		return cumulating(k, levels);
	}

	public static double worst(int k, List<Integer> levels) {
		Collections.sort(levels, (x, y) -> x.compareTo(y));
		return cumulating(k, levels);
	}

	public static double cumulating(int k, List<Integer> levels) {
		return IntStream.rangeClosed(1, k).mapToDouble(x -> gain(levels.get(x - 1)) / discount(x)).reduce(Double::sum).getAsDouble();
	}
}
