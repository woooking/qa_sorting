package com.github.woooking.so_sorting.adt.graph;

import com.google.common.collect.ImmutableSet;

public interface Graph {
	ImmutableSet<? extends Node> getNodes();
}
