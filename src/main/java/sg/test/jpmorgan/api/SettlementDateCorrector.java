package sg.test.jpmorgan.api;

public interface SettlementDateCorrector {
	Instruction correct(Instruction instruction); 
}
