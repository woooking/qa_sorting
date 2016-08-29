package com.github.woooking.qa_sorting.code.cfg.ddg;

import com.github.woooking.qa_sorting.code.cfg.CFG;
import com.github.woooking.qa_sorting.code.cfg.basiccfg.BasicCFG;
import com.github.woooking.qa_sorting.code.cfg.plaincfg.PlainCFG;
import com.github.woooking.qa_sorting.adt.graph.Node;
import com.github.woooking.qa_sorting.code.ir.IRExpression.IRAbstractVariable;
import com.github.woooking.qa_sorting.utils.Predicates;
import com.google.common.collect.ImmutableSet;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Implementation of data flow graph.
 */
public class DDG implements CFG {
	private int blockNum = 0;
	private DDGBlock entry;
	private DDGBlock exit;
	private DDGBlock[] blocks;
	private Map<DDGVariable, DDGBlock> defMap = new HashMap<>();

	private DDG(PlainCFG plainCFG) {
		blocks = new DDGBlock[plainCFG.getBlocks().size()];

		plainCFG.getBlocks().forEach(block -> {
			DDGBlock newBlock = new DDGBlock(this, block);
			blocks[newBlock.getID()] = newBlock;
			if (block == plainCFG.getEntry()) entry = newBlock;
			if (block == plainCFG.getExit()) exit = newBlock;
		});

		for (DDGBlock block : blocks) {
			if (block.getStatement() == null) continue;
			IRAbstractVariable defVar = block.getStatement().getDef();
			if (defVar == null) continue;
			DDGVariable variable = new DDGVariable(defVar);
			defMap.put(variable, block);
		}

		for (DDGBlock block : blocks) {
			if (block.getStatement() == null) continue;
			Stream<IRAbstractVariable> useVars = block.getStatement().getUseVariables();
			useVars.map(DDGVariable::new).map(defMap::get).filter(Predicates.notNull()).forEach(x -> x.addNext(block));
		}
	}

	public static CFG createCFG(PlainCFG cfg) {
		if (cfg == null) return null;
		return new DDG(cfg);
	}

	public static CFG createCFG(String methodBody) {
		CFG cfg = BasicCFG.createCFG(methodBody, true);
		CFG plainCFG = PlainCFG.createCFG((BasicCFG) cfg);
		return DDG.createCFG((PlainCFG) plainCFG);
	}

	public int getNextID() {
		return blockNum++;
	}

	@Override
	public ImmutableSet<DDGBlock> getBlocks() {
		return new ImmutableSet.Builder<DDGBlock>().add(blocks).build();
	}

	@Override
	public DDGBlock getExit() {
		return exit;
	}

	@Override
	public DDGBlock getEntry() {
		return entry;
	}

	@Override
	public ImmutableSet<DDGVariable> getVariables() {
		return ImmutableSet.copyOf(defMap.keySet());
	}

	@Override
	public ImmutableSet<? extends Node> getNodes() {
		return getBlocks();
	}
}
