package com.github.woooking.qa_sorting.analyze;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class Ranker {
	public static void rank(String query) {
		try {
			File rankFile = new File(Constant.SCORE + "/" + query);
			if (rankFile.exists()) return;
			Set<Long> ids = new HashSet<>();
			ids.addAll(Helper.readIDs(query, Constant.SO_RANK));
			ids.addAll(Helper.readIDs(query, Constant.MINE_RANK));
			PrintStream rankResult = new PrintStream(rankFile);

			int count = 0;
			for (long id : ids) {
				System.out.println("=====");
				System.out.println(++count + "/" + ids.size());
				System.out.println(query);
				System.out.println("*****");
				System.out.println("http://stackoverflow.com/questions/" + id);
				System.out.println("=====");
				Scanner scanner = new Scanner(System.in);
				while (true) {
					int score = scanner.nextInt();
					if (score >= 1 && score <= 5) {
						rankResult.println(id + " " + score);
						break;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}



	public static void main(String[] args) {
		for (String query : Constant.QUERIES) {
			rank(query);
		}
	}
}
