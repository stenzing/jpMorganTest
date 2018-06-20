package sg.test.jpmorgan.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import sg.test.jpmorgan.api.Instruction;
import sg.test.jpmorgan.api.SettlementDateCorrector;

public class CurrencyBasedSettlementDateCorrectorTest {

	private SettlementDateCorrector defaultCorrector = Mockito.spy(new SimpleSettlementDateCorrector(Calendar.SUNDAY));

	@After
	public void resetMocks() {
		Mockito.reset(defaultCorrector);
	}

	@Test
	public void testInit() {
		SettlementDateCorrector target = new CurrencyBasedSettlementDateCorrector(
				Collections.singletonMap("*", defaultCorrector), "*");

		Assert.assertNotNull(target);
	}

	@Test
	public void testCallForward() {
		SettlementDateCorrector target = new CurrencyBasedSettlementDateCorrector(
				Collections.singletonMap("*", defaultCorrector), "*");

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		Instruction sample = new Instruction.Builder().setEntity("a").setCurrency("AAA").setInstructionDate(c.getTime())
				.setSettlementDate(c.getTime()).build();

		target.correct(sample);

		Mockito.verify(defaultCorrector, Mockito.times(1)).correct(Mockito.eq(sample));
	}

	@Test
	public void testChooseCustom() {
		Map<String,SettlementDateCorrector> collectors = new HashMap<String, SettlementDateCorrector>();
		collectors.put("*", defaultCorrector);
		SettlementDateCorrector alternate = Mockito.mock(SettlementDateCorrector.class);
		collectors.put("AAA", alternate);
		SettlementDateCorrector target = new CurrencyBasedSettlementDateCorrector(collectors, "*");

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		Instruction sample = new Instruction.Builder().setEntity("a").setCurrency("AAA").setInstructionDate(c.getTime())
				.setSettlementDate(c.getTime()).build();

		target.correct(sample);

		Mockito.verify(defaultCorrector, Mockito.times(0)).correct(Mockito.eq(sample));
		Mockito.verify(alternate, Mockito.times(1)).correct(Mockito.eq(sample));
	}
}
