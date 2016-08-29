package com.github.woooking.qa_sorting.code.cfg.ddg;

import com.github.woooking.qa_sorting.code.cfg.CFGVariable;
import com.github.woooking.qa_sorting.code.cfg.basiccfg.AbstractBasicCFGBlock;
import com.github.woooking.qa_sorting.code.ir.IRExpression;
import com.github.woooking.qa_sorting.code.ir.VariableUnit;
import com.github.woooking.qa_sorting.code.ir.statement.IRStatement;
import com.github.woooking.qa_sorting.utils.Predicates;
import com.google.common.collect.ImmutableSet;

import java.util.stream.Collectors;

public class DDGVariable implements CFGVariable {
	private VariableUnit variableUnit;
	private int version;

	public DDGVariable(IRExpression.IRAbstractVariable variable) {
		this.variableUnit = variable.getVariable();
		this.version = variable.getVersion();
	}

	@Override
	public int hashCode() {
		return variableUnit.hashCode() ^ version;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DDGVariable)) return false;
		DDGVariable other = (DDGVariable) obj;
		return variableUnit == other.variableUnit && version == other.version;
	}

	@Override
	public String getName() {
		return variableUnit.getName();
	}

	@Override
	public ImmutableSet<AbstractBasicCFGBlock> getDefBlocks() {
		return ImmutableSet.copyOf(variableUnit.getDefBoxes().map(IRStatement::getBelongBlock).filter(Predicates.notNull()).collect(Collectors.toSet()));
	}

	@Override
	public ImmutableSet<AbstractBasicCFGBlock> getUseBlocks() {
		return ImmutableSet.copyOf(variableUnit.getUseBoxes().map(IRStatement::getBelongBlock).filter(Predicates.notNull()).collect(Collectors.toSet()));
	}

	@Override
	public ImmutableSet<IRStatement> getDefStatements() {
		return ImmutableSet.copyOf(variableUnit.getDefBoxes().collect(Collectors.toSet()));
	}

	@Override
	public ImmutableSet<IRStatement> getUseStatements() {
		return ImmutableSet.copyOf(variableUnit.getUseBoxes().collect(Collectors.toSet()));
	}

	@Override
	public String toString() {
		return String.format("[DDGVariable]%s@%s", variableUnit.getName(), version);
	}
}
