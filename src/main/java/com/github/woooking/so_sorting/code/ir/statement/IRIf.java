package com.github.woooking.so_sorting.code.ir.statement;

import com.github.woooking.so_sorting.code.cfg.basiccfg.BasicCFGConditionBlock;
import com.github.woooking.so_sorting.code.cfg.basiccfg.BasicCFGRegularBlock;
import com.github.woooking.so_sorting.code.ir.IRExpression;
import com.github.woooking.so_sorting.code.ir.IRScope;
import com.github.woooking.so_sorting.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IRIf implements IRAbstractStatement {
	private int depth = 1;
	private IRExpression condition;
	private List<IRAbstractStatement> thenStatements = new ArrayList<>();
	private List<IRAbstractStatement> elseStatements = new ArrayList<>();

	private IRScope thenScope = thenStatements::add;
	private IRScope elseScope = elseStatements::add;

	public IRIf(IRExpression condition, int depth) {
		this.condition = condition;
		this.depth = depth;
	}

	public IRScope getThenScope() {
		return thenScope;
	}

	public IRScope getElseScope() {
		return elseScope;
	}

	@Override
	public String toString() {
		String result = String.format("if (%s):\n", condition);
		for (IRAbstractStatement statement : thenStatements)
			result += StringUtils.getNTabs(depth) + statement + "\n";
		if (!elseStatements.isEmpty()) {
			result += StringUtils.getNTabs(depth - 1) + "else:\n";
			for (IRAbstractStatement statement : elseStatements)
				result += StringUtils.getNTabs(depth) + statement + "\n";
		}

		result += StringUtils.getNTabs(depth - 1) + "end if";
		return result;
	}

	@Override
	public BasicCFGRegularBlock buildCFG(BasicCFGRegularBlock block) {
		BasicCFGRegularBlock thenBlock = block.getCFG().createRegularBlock();
		BasicCFGRegularBlock elseBlock = block.getCFG().createRegularBlock();
		BasicCFGRegularBlock endBlock = block.getCFG().createRegularBlock();
		BasicCFGConditionBlock conditionBlock = block.getCFG().createConditionBlock();
		block.setNext(conditionBlock);
		conditionBlock.addNext(new BasicCFGConditionBlock.Condition.BooleanCondition(condition, IRExpression.TRUE), thenBlock);
		conditionBlock.addNext(new BasicCFGConditionBlock.Condition.BooleanCondition(condition, IRExpression.FALSE), elseBlock);
		for (IRAbstractStatement thenStatement : thenStatements) thenBlock = thenStatement.buildCFG(thenBlock);
		for (IRAbstractStatement elseStatement : elseStatements) elseBlock = elseStatement.buildCFG(elseBlock);
		thenBlock.setNext(endBlock);
		elseBlock.setNext(endBlock);
		return endBlock;
	}

}
