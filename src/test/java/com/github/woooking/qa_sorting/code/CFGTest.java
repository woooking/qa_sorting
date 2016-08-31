package com.github.woooking.qa_sorting.code;

import com.github.woooking.qa_sorting.code.cfg.basiccfg.BasicCFG;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CFGTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void testNonSSA() {
		BasicCFG cfg = (BasicCFG) BasicCFG.createCFG(CFGTestUtil.code, false);
		assertFalse(cfg.isSSAForm());
		CFGTestUtil.checkCFG(cfg);
	}

	@Test
	public void testSSA() {
		BasicCFG cfg = (BasicCFG) BasicCFG.createCFG(CFGTestUtil.code, true);
		assertTrue(cfg.isSSAForm());
		CFGTestUtil.checkCFG(cfg);
	}

}
