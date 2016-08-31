package com.github.woooking.qa_sorting.code;

import com.github.woooking.qa_sorting.adt.graph.Node;
import com.github.woooking.qa_sorting.code.cfg.CFG;

import static org.junit.Assert.assertTrue;

public class CFGTestUtil {
	public static String code = "\tisSSAForm = true;\n" +
		"\t\tBasicCFGRegularBlock exitPrev = createRegularBlock();\n" +
		"\t\texitPrev.prevs.addAll(exit.getPrevs());\n" +
		"\t\tfor (AbstractBasicCFGBlock block : exit.getPrevs()) {\n" +
		"\t\t\tif (block instanceof BasicCFGRegularBlock) ((BasicCFGRegularBlock) block).setNext(exitPrev);\n" +
		"\t\t\telse if (block instanceof BasicCFGSpecialBlock.Entry)\n" +
		"\t\t\t\t((BasicCFGSpecialBlock.Entry) block).setNext(exitPrev);\n" +
		"\t\t\telse if (block instanceof BasicCFGConditionBlock)\n" +
		"\t\t\t\t((BasicCFGConditionBlock) block).replaceNext(exit, exitPrev);\n" +
		"\t\t}\n" +
		"\t\texit.prevs.clear();\n" +
		"\t\texitPrev.setNext(exit);";

	public static void checkCFG(CFG cfg) {
		assertTrue(cfg.getNodes().contains(cfg.getEntry()));
		assertTrue(cfg.getNodes().contains(cfg.getExit()));
		for (Node node : cfg.getNodes()) {
			for (Node next: node.getNexts()) {
				assertTrue(next.getPrevs().contains(node));
			}
		}
	}

}
