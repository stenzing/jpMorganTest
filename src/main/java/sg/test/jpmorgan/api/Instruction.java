package sg.test.jpmorgan.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Instruction record data.
 * 
 * @author gstenzinger
 * 
 */
public class Instruction implements Serializable {
	private static final long serialVersionUID = 1L;

	public static class Builder {
		private String entity;
		private InstructionType type;
		private BigDecimal aggreedFx;
		private String currency;
		private Date instructionDate;
		private Date settlementDate;
		private int units;
		private BigDecimal unitPrice;
		
		public Builder fromInstuction(Instruction i) {
			setEntity(i.getEntity());
			setType(i.getType());
			setAggreedFx(i.getAggreedFx());
			setCurrency(i.getCurrency());
			setInstructionDate(i.getInstructionDate());
			setSettlementDate(i.getSettlementDate());
			setUnits(i.getUnits());
			setUnitPrice(i.getUnitPrice());
			return this;
		}

		public Builder setEntity(String entity) {
			this.entity = new String(entity);
			return this;
		}

		public Builder setType(InstructionType type) {
			this.type = type;
			return this;
		}

		public Builder setAggreedFx(BigDecimal aggreedFx) {
			this.aggreedFx = aggreedFx;
			return this;
		}

		public Builder setCurrency(String currency) {
			this.currency = new String(currency);
			return this;
		}

		public Builder setInstructionDate(Date instructionDate) {
			this.instructionDate = new Date(instructionDate.getTime());
			return this;
		}

		public Builder setSettlementDate(Date settlementDate) {
			this.settlementDate = new Date(settlementDate.getTime());
			return this;
		}

		public Builder setUnits(int units) {
			this.units = units;
			return this;
		}

		public Builder setUnitPrice(BigDecimal unitPrice) {
			this.unitPrice = unitPrice;
			return this;
		}

		public Instruction build() {
			return new Instruction(entity, type, aggreedFx, currency, instructionDate, settlementDate, units,
					unitPrice);
		}
	}

	private final String entity;
	private final InstructionType type;
	private final BigDecimal aggreedFx;
	private final String currency;
	private final Date instructionDate;
	private final Date settlementDate;
	private final int units;
	private final BigDecimal unitPrice;

	private Instruction(String entity, InstructionType type, BigDecimal aggreedFx, String currency,
			Date instructionDate, Date settlementDate, int units, BigDecimal unitPrice) {
		super();
		this.entity = entity;
		this.type = type;
		this.aggreedFx = aggreedFx;
		this.currency = currency;
		this.instructionDate = instructionDate;
		this.settlementDate = settlementDate;
		this.units = units;
		this.unitPrice = unitPrice;
	}

	public String getEntity() {
		return entity;
	}

	public InstructionType getType() {
		return type;
	}

	public String getCurrency() {
		return currency;
	}

	public BigDecimal getAggreedFx() {
		return aggreedFx;
	}

	public Date getInstructionDate() {
		return instructionDate;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public int getUnits() {
		return units;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	
	public BigDecimal getTotalValueUSD() {
		return aggreedFx.multiply(unitPrice).multiply(BigDecimal.valueOf(units));
	}

	@Override
	public String toString() {
		return "Instruction [entity=" + entity + ", type=" + type + ", aggreedFx=" + aggreedFx + ", currency="
				+ currency + ", instructionDate=" + instructionDate + ", settlementDate=" + settlementDate + ", units="
				+ units + ", unitPrice=" + unitPrice + ", getTotalValueUSD()=" + getTotalValueUSD() + "]";
	}

}
