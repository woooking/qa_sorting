package com.github.woooking.qa_sorting.adt.graph;

import com.google.common.collect.ImmutableSet;

public interface Node {
	ImmutableSet<? extends Node> getNexts();
	ImmutableSet<? extends Node> getPrevs();
}
