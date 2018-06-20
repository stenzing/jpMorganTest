package sg.test.jpmorgan.impl;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

import sg.test.jpmorgan.api.Instruction;
import sg.test.jpmorgan.api.SettlementDateCorrector;

public class SimpleSettlementDateCorrectorTest {

	@Test
	public void testInit() {
		SettlementDateCorrector target = new SimpleSettlementDateCorrector();

		Assert.assertNotNull(target);
	}


	@Test
	public void testDateKeep() {
		SettlementDateCorrector target = new SimpleSettlementDateCorrector(Calendar.SUNDAY);

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		Instruction sample = new Instruction.Builder().setEntity("a").setCurrency("AAA").setInstructionDate(c.getTime())
				.setSettlementDate(c.getTime()).build();

		Instruction result = target.correct(sample);

		Assert.assertNotNull(result);
		Assert.assertEquals(c.getTime(), result.getSettlementDate());
	}
	
	@Test
	public void testDateShift() {
		SettlementDateCorrector target = new SimpleSettlementDateCorrector(Calendar.SUNDAY);

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		Instruction sample = new Instruction.Builder().setEntity("a").setCurrency("AAA").setInstructionDate(c.getTime())
				.setSettlementDate(c.getTime()).build();

		Instruction result = target.correct(sample);

		c.add(Calendar.DATE, 1);
		Assert.assertNotNull(result);
		Assert.assertEquals(c.getTime(), result.getSettlementDate());
	}
}
