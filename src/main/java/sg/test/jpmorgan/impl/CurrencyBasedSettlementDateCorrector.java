package sg.test.jpmorgan.impl;

import java.util.Collections;
import java.util.Map;

import sg.test.jpmorgan.api.Instruction;
import sg.test.jpmorgan.api.SettlementDateCorrector;

public class CurrencyBasedSettlementDateCorrector implements SettlementDateCorrector {

	private final Map<String, SettlementDateCorrector> correctors;
	private final String defaultCorrector;

	public CurrencyBasedSettlementDateCorrector(Map<String, SettlementDateCorrector> correctors,
			String defaultCorrector) {
		this.correctors = Collections.unmodifiableMap(correctors);
		this.defaultCorrector = defaultCorrector;
	}

	public Instruction correct(Instruction instruction) {

		if (correctors.containsKey(instruction.getCurrency())) {
			return correctors.get(instruction.getCurrency()).correct(instruction);
		}
		return correctors.get(defaultCorrector).correct(instruction);
	}

}
