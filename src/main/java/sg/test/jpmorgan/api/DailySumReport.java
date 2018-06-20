package sg.test.jpmorgan.api;

import java.math.BigDecimal;
import java.util.Date;

public class DailySumReport {
	private final Date date;
	private BigDecimal totalAmount;

	public DailySumReport(Date date) {
		this.date = new Date(date.getTime());
		totalAmount = BigDecimal.ZERO;
	}

	public void addAmount(Instruction entry) {
		totalAmount = totalAmount
				.add(entry.getTotalValueUSD());
	}

	public Date getDate() {
		return date;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
}
