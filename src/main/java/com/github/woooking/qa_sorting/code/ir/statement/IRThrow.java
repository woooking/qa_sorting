package com.github.woooking.qa_sorting.code.ir.statement;

import com.github.woooking.qa_sorting.code.cfg.basiccfg.BasicCFGRegularBlock;
import com.github.woooking.qa_sorting.code.mining.MiningNode;
import com.github.woooking.qa_sorting.code.ir.IRExpression;

import java.util.stream.Stream;

public class IRThrow extends IRStatement {
	private IRExpression exception;

	public IRThrow(IRExpression exception) {
		addUse(exception);

		this.exception = exception;
	}

	@Override
	public String toString() {
		return String.format("throw %s", exception);
	}

	@Override
	public BasicCFGRegularBlock buildCFG(BasicCFGRegularBlock block) {
		// TODO: 16-1-8
		block.addNode(this);
		return block;
	}

	@Override
	public Stream<IRExpression> getUses(ExpressionFilter builder) {
		return Stream.empty();
	}

	@Override
	public MiningNode toMiningNode() {
		return MiningNode.THROW;
	}

}
