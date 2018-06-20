package sg.test.jpmorgan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import sg.test.jpmorgan.api.DailySumReport;
import sg.test.jpmorgan.api.DataSource;
import sg.test.jpmorgan.api.Instruction;
import sg.test.jpmorgan.api.InstructionType;
import sg.test.jpmorgan.api.SettlementDateCorrector;
import sg.test.jpmorgan.impl.CurrencyBasedSettlementDateCorrector;
import sg.test.jpmorgan.impl.SimpleSettlementDateCorrector;
import sg.test.jpmorgan.impl.FileDataSource;

/**
 * Marketing data calculator and reporter.
 * 
 * @author gstenzinger
 *
 */
public class MarketReporter {

	private static final String DEFAULT_CORRECTOR_KEY = "*";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final DataSource source;
	private final Map<Date, Collection<Instruction>> dataStore;
	private static final SettlementDateCorrector dateCorrector;

	static {
		Map<String, SettlementDateCorrector> correctors = new HashMap<String, SettlementDateCorrector>();
		correctors.put(DEFAULT_CORRECTOR_KEY, new SimpleSettlementDateCorrector(Calendar.SATURDAY, Calendar.SUNDAY));
		SimpleSettlementDateCorrector correctorStartingWithSunday = new SimpleSettlementDateCorrector(Calendar.SATURDAY,
				Calendar.FRIDAY);
		correctors.put("AED", correctorStartingWithSunday);
		correctors.put("SAR", correctorStartingWithSunday);

		dateCorrector = new CurrencyBasedSettlementDateCorrector(correctors, DEFAULT_CORRECTOR_KEY);
	}

	public MarketReporter(DataSource source) {
		this.source = source;
		this.dataStore = new TreeMap<Date, Collection<Instruction>>();
	}

	public void proccessInput() {
		Instruction data;
		while ((data = source.readData()) != null) {
			Instruction correctedEntry = dateCorrector.correct(data);

			Date settlementDate = correctedEntry.getSettlementDate();
			if (!dataStore.containsKey(settlementDate)) {
				dataStore.put(settlementDate, new LinkedList<Instruction>());
			}
			dataStore.get(settlementDate).add(correctedEntry);
		}
	}

	public List<DailySumReport> getIncommingByDay() {
		return getSummaryByType(InstructionType.S);
	}

	public List<DailySumReport> getOutgoingByDay() {
		return getSummaryByType(InstructionType.B);
	}

	public List<Instruction> getInstructionRanking() {
		List<Instruction> result = new LinkedList<Instruction>();

		for (Entry<Date, Collection<Instruction>> dayset : dataStore.entrySet()) {
			result.addAll(dayset.getValue());
		}
		Collections.sort(result, new Comparator<Instruction>() {

			@Override
			public int compare(Instruction o1, Instruction o2) {
				return -1 * o1.getTotalValueUSD().compareTo(o2.getTotalValueUSD());
			}
		});

		return result;
	}

	private List<DailySumReport> getSummaryByType(InstructionType type) {
		List<DailySumReport> result = new LinkedList<DailySumReport>();
		for (Entry<Date, Collection<Instruction>> dayset : dataStore.entrySet()) {
			DailySumReport report = new DailySumReport(dayset.getKey());
			dayset.getValue().stream().filter(ins -> ins.getType().equals(type)).forEach(ins -> report.addAmount(ins));
			result.add(report);
		}
		return result;
	}

	public static void main(String[] args) {
		DataSource source = FileDataSource.fromFile(args[0]);
		MarketReporter reporter = new MarketReporter(source);
		reporter.proccessInput();

		List<DailySumReport> reports = reporter.getIncommingByDay();

		System.out.println("Daily incoming");
		for (DailySumReport aDay : reports) {
			System.out.println(String.format("Incoming for day %s is %.2f", dateFormat.format(aDay.getDate()),
					aDay.getTotalAmount().doubleValue()));
		}

		reports = reporter.getOutgoingByDay();

		System.out.println("Daily Outgoing");
		for (DailySumReport aDay : reports) {
			System.out.println(String.format("Outgoing for day %s is %.2f", dateFormat.format(aDay.getDate()),
					aDay.getTotalAmount().doubleValue()));
		}

		List<Instruction> rankedList = reporter.getInstructionRanking();

		System.out.println("Ranked list");
		for (Instruction instruction : rankedList) {
			System.out.println(instruction);
		}

	}

}
