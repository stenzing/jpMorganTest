package sg.test.jpmorgan.api;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class InstructionBuilderTest {

	@Test
	public void testBuildSimple() {
		Instruction.Builder target = new Instruction.Builder();
		target.setEntity("Entity").setCurrency("HUF").setType(InstructionType.B).setAggreedFx(BigDecimal.valueOf(0.3));

		Instruction result = target.build();

		Assert.assertTrue("Entity".equals(result.getEntity()));
		Assert.assertTrue("HUF".equals(result.getCurrency()));
		Assert.assertEquals(InstructionType.B, result.getType());
		Assert.assertTrue(BigDecimal.valueOf(0.3).setScale(2).equals(result.getAggreedFx().setScale(2)));
	}
}
