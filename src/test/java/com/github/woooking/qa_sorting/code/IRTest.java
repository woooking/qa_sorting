package com.github.woooking.qa_sorting.code;

import com.github.woooking.qa_sorting.code.ir.IRRepresentation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IRTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void testEnhancedFor() {
		new IRRepresentation("" +
			"for (IRAbstractStatement statement : statements) {\n" +
			"    block = statement.buildCFG(block);\n" +
			"}\n" +
			"return block;");

	}

	@Test
	public void testIf() {
		new IRRepresentation("" +
			"VariableUnit v;\n" +
			"v = variables.get(name);\n" +
			"if (v == null) {\n" +
			"    v = new VariableUnit(name);\n" +
			"    variables.put(name, v);\n" +
			"}\n" +
			"return new IRVariable(v);");
	}

	@Test
	public void testLambda() {
		exception.expect(UnsupportedOperationException.class);
		new IRRepresentation("" +
			"statements.forEach(System.out::println);\n" +
			"\n" +
			"\t\tvariables.forEach((k, v) -> System.out.println(v));");
	}

}
