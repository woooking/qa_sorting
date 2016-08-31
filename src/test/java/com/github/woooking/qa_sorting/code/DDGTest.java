package com.github.woooking.qa_sorting.code;

import com.github.woooking.qa_sorting.code.cfg.CFG;
import com.github.woooking.qa_sorting.code.cfg.ddg.DDG;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DDGTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void test() {
		CFG cfg = DDG.createCFG(CFGTestUtil.code);
		CFGTestUtil.checkCFG(cfg);
	}
}
