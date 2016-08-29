package com.github.woooking.qa_sorting.code.mining;

import com.github.woooking.qa_sorting.code.cfg.CFG;
import com.github.woooking.qa_sorting.code.cfg.ddg.DDG;
import com.github.woooking.qa_sorting.utils.FileUtils;
import com.github.woooking.qa_sorting.utils.Predicates;
import de.parsemis.graph.Graph;
import de.parsemis.graph.ListGraph;
import de.parsemis.miner.environment.Settings;
import de.parsemis.miner.general.IntFrequency;
import de.parsemis.parsers.IntLabelParser;
import de.parsemis.strategy.BFSStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Miner {
	public static Settings<MiningNode, Integer> createSetting(int minFreq, int minNodes) {
		Settings<MiningNode, Integer> setting = new Settings<>();
		setting.algorithm = new de.parsemis.algorithms.gSpan.Algorithm<>();
		setting.strategy = new BFSStrategy<>();
		setting.minFreq = new IntFrequency(minFreq);
		setting.factory = new ListGraph.Factory<>(new MiningNodeParser(), new IntLabelParser());
		setting.minNodes = minNodes;

		return setting;
	}

	public static List<CFG> mineFromFiles(List<String> files) {
		Settings<MiningNode, Integer> setting = new Settings<>();
		setting.algorithm = new de.parsemis.algorithms.gSpan.Algorithm<>();
		setting.strategy = new BFSStrategy<>();
		setting.minFreq = new IntFrequency(2);
		setting.factory = new ListGraph.Factory<>(new MiningNodeParser(), new IntLabelParser());
		setting.minNodes = 2;

		return mineFromFiles(files, setting);
	}

	public static List<CFG> mineFromFiles(List<String> files, Settings<MiningNode, Integer> setting) {
		List<String> bodys = files.stream()
			.map(f -> new File("testdata/cfg/" + f))
			.map(FileUtils::getFileContent)
			.collect(Collectors.toList());

		return mine(bodys, setting);
	}

	public static List<CFG> mine(List<String> bodys, Settings<MiningNode, Integer> setting) {
		List<Graph<MiningNode, Integer>> graphs = new ArrayList<>();
		List<DDG> ddgs = bodys.stream()
				.map(DDG::createCFG)
				.map(x -> (DDG) x)
				.filter(Predicates.notNull())
				.collect(Collectors.toList());

		return mineFromDDG(ddgs, setting);
	}

	public static List<Graph<MiningNode, Integer>> mineGraphFromDDG(Collection<DDG> ddgs, Settings<MiningNode, Integer> setting) {
		List<Graph<MiningNode, Integer>> graphs = ddgs.stream().map(MiningGraph::convertDDGToMiningGraph).collect(Collectors.toList());
		return MiningGraph.resultFilter(de.parsemis.Miner.mine(graphs, setting));
	}

	public static List<CFG> mineFromDDG(List<DDG> ddgs, Settings<MiningNode, Integer> setting) {
		return mineGraphFromDDG(ddgs, setting).stream().map(r -> MiningGraph.createCFGFromMiningGraph(ddgs, r)).collect(Collectors.toList());
	}
}
