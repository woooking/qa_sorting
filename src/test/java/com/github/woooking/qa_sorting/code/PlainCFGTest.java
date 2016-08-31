package com.github.woooking.qa_sorting.code;

import com.github.woooking.qa_sorting.code.cfg.CFG;
import com.github.woooking.qa_sorting.code.cfg.basiccfg.BasicCFG;
import com.github.woooking.qa_sorting.code.cfg.plaincfg.PlainCFG;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class PlainCFGTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void test() {
		BasicCFG basicCFG = (BasicCFG) BasicCFG.createCFG(CFGTestUtil.code, false);
		CFG cfg= PlainCFG.createCFG(basicCFG);
		CFGTestUtil.checkCFG(cfg);
		assertEquals(cfg.getVariables(), basicCFG.getVariables());
	}


}
