package com.github.woooking.so_sorting.code.cfg;

import com.github.woooking.so_sorting.adt.graph.Graph;
import com.google.common.collect.ImmutableSet;

/**
 * Interface for CFG. A CFG consists of some basic block {%link CFGBlock} and variables. There is a entry block and a exit block in the basic blocks.
 */
public interface CFG extends Graph {
	/**
	 * Get all basic blocks in the CFG, contains entry and exit.
	 * @return the immutable set of all basic blocks
	 */
	ImmutableSet<? extends CFGBlock> getBlocks();

	/**
	 * Get the entry block of the CFG.
	 * @return the entry block
	 */
	CFGBlock getEntry();

	/**
	 * Get the exit block of the CFG.
	 * @return the entry block
	 */
	CFGBlock getExit();

	/**
	 * Get the variables of the CFG.
	 * @return variables of the CFG
	 */
	ImmutableSet<? extends CFGVariable> getVariables();

}
