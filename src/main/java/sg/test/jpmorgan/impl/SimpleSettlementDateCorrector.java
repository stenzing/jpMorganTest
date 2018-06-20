package sg.test.jpmorgan.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import sg.test.jpmorgan.api.Instruction;
import sg.test.jpmorgan.api.SettlementDateCorrector;

/**
 * Settlement Date corrector that check against a set of date integers.
 * 
 * @author gstenzinger
 *
 */
public class SimpleSettlementDateCorrector implements SettlementDateCorrector {

	private final Calendar calendar = Calendar.getInstance();
	private final Set<Integer> nonWeekDays;

	public SimpleSettlementDateCorrector(Integer... nonWeekDays) {
		this.nonWeekDays = new HashSet<Integer>(Arrays.asList(nonWeekDays));
	}

	public Instruction correct(Instruction instruction) {

		calendar.setTime(instruction.getSettlementDate());

		Integer aDay = calendar.get(Calendar.DAY_OF_WEEK);
		Instruction result = instruction;

		while (nonWeekDays.contains(aDay)) {
			result = increaseSettlementDate(result);
			calendar.setTime(result.getSettlementDate());
			aDay = calendar.get(Calendar.DAY_OF_WEEK);
		}

		return result;
	}

	private Instruction increaseSettlementDate(Instruction previousInstruction) {
		calendar.setTime(previousInstruction.getSettlementDate());
		calendar.add(Calendar.DATE, 1);
		Date newDate = calendar.getTime();
		Instruction.Builder b = new Instruction.Builder().fromInstuction(previousInstruction)
				.setSettlementDate(newDate);
		return b.build();
	}

}
