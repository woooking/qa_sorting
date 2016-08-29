package com.github.woooking.so_sorting.code.cfg;

import com.github.woooking.so_sorting.code.ir.statement.IRStatement;
import com.google.common.collect.ImmutableSet;

/**
 * Interface for CFGVariable.
 */
public interface CFGVariable {
	/**
	 * Get the name of the variable.
	 * @return the name of the variable
	 */
	String getName();

	/**
	 * Get the all the blocks which definite this variable
	 * @return the immutable set of blocks which definite this variable
	 */
	ImmutableSet<? extends CFGBlock> getDefBlocks();

	/**
	 * Get the all the blocks which use this variable
	 * @return the immutable set of blocks which use this variable
	 */
	ImmutableSet<? extends CFGBlock> getUseBlocks();

	/**
	 * Get the all the statements which definite this variable
	 * @return the immutable set of statements which definite this variable
	 */
	ImmutableSet<IRStatement> getDefStatements();

	/**
	 * Get the all the statements which use this variable
	 * @return the immutable set of statements which use this variable
	 */
	ImmutableSet<IRStatement> getUseStatements();

}
