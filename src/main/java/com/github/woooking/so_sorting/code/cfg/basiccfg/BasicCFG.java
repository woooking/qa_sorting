package com.github.woooking.so_sorting.code.cfg.basiccfg;

import com.github.woooking.so_sorting.adt.graph.Node;
import com.github.woooking.so_sorting.code.cfg.CFG;
import com.github.woooking.so_sorting.code.cfg.SSACFGConverter;
import com.github.woooking.so_sorting.code.ir.IRRepresentation;
import com.github.woooking.so_sorting.code.ir.VariableUnit;
import com.github.woooking.so_sorting.code.ir.statement.IRAssignment;
import com.github.woooking.so_sorting.code.ir.statement.IRLabel;
import com.github.woooking.so_sorting.code.ir.statement.IRStatement;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Implementation of normal CFG.
 *
 */
public class BasicCFG implements CFG {
	public static final Logger logger = LoggerFactory.getLogger(BasicCFG.class);

	private int maxBlockNum = 0;
	private boolean isSSAForm = false;
	private BasicCFGRegularBlock currentBlock;
	private BasicCFGSpecialBlock.Entry entry;
	private BasicCFGSpecialBlock.Exit exit;
	private Set<AbstractBasicCFGBlock> blocks = new HashSet<>();
	private Map<IRLabel, AbstractBasicCFGBlock> labelMap = new HashMap<>();
	private Map<BasicCFGRegularBlock, IRLabel> gotoInfo = new HashMap<>();
	private Map<VariableUnit, CFGVariableImpl> variableMap = new HashMap<>();

	private BasicCFG() {
	}

	public static CFG createCFG(IRRepresentation irRepresentation, boolean isSSAForm) {
		BasicCFG cfg = new BasicCFG();
		cfg.entry = new BasicCFGSpecialBlock.Entry(cfg, cfg.maxBlockNum++);
		cfg.exit = new BasicCFGSpecialBlock.Exit(cfg, cfg.maxBlockNum++);
		cfg.currentBlock = cfg.createRegularBlock();
		cfg.entry.setNext(cfg.currentBlock);
		irRepresentation.buildCFG(cfg.currentBlock).setNext(cfg.exit);
		cfg.setupGotoInfo();
		cfg.setupVariable(irRepresentation);
		cfg.optimize();
		if (isSSAForm) SSACFGConverter.convertToSSAForm(cfg);
		return cfg;
	}

	public static CFG createCFG(String methodBody, boolean isSSAForm) {
		try {
			IRRepresentation body = new IRRepresentation(methodBody);
			return createCFG(body, isSSAForm);
		} catch (UnsupportedOperationException e) {
			logger.error("Unsupported syntax!");
			logger.error(methodBody);
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodBody);
			return null;
		}
	}

	public BasicCFGRegularBlock createRegularBlock() {
		BasicCFGRegularBlock block = new BasicCFGRegularBlock(this, maxBlockNum++);
		blocks.add(block);
		return block;
	}

	public BasicCFGConditionBlock createConditionBlock() {
		BasicCFGConditionBlock block = new BasicCFGConditionBlock(this, maxBlockNum++);
		blocks.add(block);
		return block;
	}

	public boolean isSSAForm() {
		return isSSAForm;
	}

	public void ssaSetup() {
		isSSAForm = true;
		BasicCFGRegularBlock exitPrev = createRegularBlock();
		exitPrev.prevs.addAll(exit.getPrevs());
		for (AbstractBasicCFGBlock block : exit.getPrevs()) {
			if (block instanceof BasicCFGRegularBlock) ((BasicCFGRegularBlock) block).setNext(exitPrev);
			else if (block instanceof BasicCFGSpecialBlock.Entry)
				((BasicCFGSpecialBlock.Entry) block).setNext(exitPrev);
			else if (block instanceof BasicCFGConditionBlock)
				((BasicCFGConditionBlock) block).replaceNext(exit, exitPrev);
		}
		exit.prevs.clear();
		exitPrev.setNext(exit);
	}

	public CFGVariableImpl getVariable(VariableUnit variableUnit) {
		return variableMap.get(variableUnit);
	}

	public void mapLabelBlock(IRLabel label, AbstractBasicCFGBlock block) {
		labelMap.put(label, block);
	}

	public void recordGotoInfo(BasicCFGRegularBlock block, IRLabel label) {
		gotoInfo.put(block, label);
	}

	private void setupGotoInfo() {
		gotoInfo.forEach((block, label) -> {
			AbstractBasicCFGBlock nextBlock = labelMap.get(label);
			block.setNext(nextBlock);
		});
	}

	private void setupVariable(IRRepresentation irRepresentation) {
		irRepresentation.getVariables().forEach(v -> variableMap.put(v, new CFGVariableImpl(v)));
	}

	private void optimize() {
		optimizeTempVar();
		// 去除空Block
		blocks.stream()
			.filter(x -> x instanceof BasicCFGRegularBlock)
			.map(x -> (BasicCFGRegularBlock) x)
			.filter(x -> x.getStatements().isEmpty())
			.forEach(BasicCFGRegularBlock::removeBlock);
		// 去除不可达Block
		entry.visit();
		blocks.removeIf(x -> !x.reachable);
		blocks.forEach(AbstractBasicCFGBlock::checkPrev);
		exit.checkPrev();
	}

	private void optimizeTempVar() {
		List<VariableUnit> removedVar = new ArrayList<>();
		variableMap.forEach((unit, var) -> {
			if (!unit.getName().startsWith("#")) return;
			if (unit.getUseBoxes().count() != 1) return;
			IRStatement useStatement = unit.getUseBoxes().findFirst().get();
			IRStatement defStatement = unit.getDefBoxes().findFirst().get();
			if (useStatement.getBelongBlock() != defStatement.getBelongBlock()) return;
			if (!(useStatement instanceof IRAssignment)) return;
			defStatement.replaceDef(useStatement.getDef());
			useStatement.getBelongBlock().removeStatement(useStatement);
			removedVar.add(unit);
		});
		removedVar.forEach(variableMap::remove);
	}

	@Override
	public ImmutableSet<AbstractBasicCFGBlock> getBlocks() {
		return new ImmutableSet.Builder<AbstractBasicCFGBlock>().addAll(blocks).add(entry).add(exit).build();
	}

	@Override
	public BasicCFGSpecialBlock.Exit getExit() {
		return exit;
	}

	@Override
	public BasicCFGSpecialBlock.Entry getEntry() {
		return entry;
	}

	@Override
	public ImmutableSet<CFGVariableImpl> getVariables() {
		return ImmutableSet.copyOf(variableMap.values());
	}

	@Override
	public ImmutableSet<Node> getNodes() {
		return new ImmutableSet.Builder<Node>().addAll(blocks).add(entry).add(exit).build();
	}
}
