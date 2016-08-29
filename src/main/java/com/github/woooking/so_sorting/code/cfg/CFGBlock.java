package com.github.woooking.so_sorting.code.cfg;

import com.github.woooking.so_sorting.adt.graph.Node;
import com.github.woooking.so_sorting.code.ir.statement.IRStatement;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * Interface for CFGBlock. A CFG consists of some statements {%link IRStatement}.
 */
public interface CFGBlock extends Node {
	@Override
	ImmutableSet<? extends CFGBlock> getNexts();

	@Override
	ImmutableSet<? extends CFGBlock> getPrevs();

	/**
	 * Get the statements of the CFGBlock.
	 * @return the list of statements
	 */
	ImmutableList<IRStatement> getStatements();
}
