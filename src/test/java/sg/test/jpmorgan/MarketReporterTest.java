package sg.test.jpmorgan;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import sg.test.jpmorgan.api.DailySumReport;
import sg.test.jpmorgan.api.DataSource;
import sg.test.jpmorgan.api.Instruction;
import sg.test.jpmorgan.api.InstructionType;

public class MarketReporterTest {

	private DataSource testSource = mock(DataSource.class);

	@After
	public void after() {
		reset(testSource);
	}

	@Test
	public void testInit() {
		MarketReporter target = new MarketReporter(testSource);

		Assert.assertNotNull(target);
	}

	@Test
	public void testReadSource() {
		Instruction mockReply = getMockInstruction(InstructionType.B);
		when(testSource.readData()).thenReturn(mockReply, (Instruction) null);
		MarketReporter target = new MarketReporter(testSource);

		target.proccessInput();

		verify(testSource, times(2)).readData();
	}

	@Test
	public void testIncomingByDayResult() {
		Instruction mockReply1 = getMockInstruction(InstructionType.B);
		Instruction mockReply2 = getMockInstruction(InstructionType.S);
		Instruction mockReply3 = getMockInstruction(InstructionType.S);
		when(testSource.readData()).thenReturn(mockReply1, mockReply2, mockReply3, (Instruction) null);
		MarketReporter target = new MarketReporter(testSource);

		target.proccessInput();

		List<DailySumReport> result = target.getIncommingByDay();

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertTrue(BigDecimal.valueOf(1000).setScale(2).equals(result.get(0).getTotalAmount().setScale(2)));
	}

	@Test
	public void testOutgoingByDayResult() {
		Instruction mockReply1 = getMockInstruction(InstructionType.B);
		Instruction mockReply2 = getMockInstruction(InstructionType.B);
		Instruction mockReply3 = getMockInstruction(InstructionType.S);
		when(testSource.readData()).thenReturn(mockReply1, mockReply2, mockReply3, (Instruction) null);
		MarketReporter target = new MarketReporter(testSource);

		target.proccessInput();

		List<DailySumReport> result = target.getOutgoingByDay();

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertTrue(BigDecimal.valueOf(1000).setScale(2).equals(result.get(0).getTotalAmount().setScale(2)));
	}

	@Test
	public void testRanking() {
		Instruction mockReply1 = getMockInstruction(InstructionType.B,20);
		Instruction mockReply2 = getMockInstruction(InstructionType.B,30);
		Instruction mockReply3 = getMockInstruction(InstructionType.S,40);
		when(testSource.readData()).thenReturn(mockReply1, mockReply2, mockReply3, (Instruction) null);
		MarketReporter target = new MarketReporter(testSource);

		target.proccessInput();

		List<Instruction> result = target.getInstructionRanking();

		Assert.assertNotNull(result);
		Assert.assertEquals(3, result.size());
		BigDecimal lastAmount = BigDecimal.valueOf(10000);
		for (Instruction item :result) {
			Assert.assertTrue(lastAmount.compareTo(item.getTotalValueUSD())>0);
			lastAmount = item.getTotalValueUSD();
		}
	}	
	
	
	private Instruction getMockInstruction(InstructionType type) {
		return getMockInstruction(type, 100);
	}
	
	private Instruction getMockInstruction(InstructionType type, int amount) {
		Date d = new Date();
		Instruction reply = new Instruction.Builder().setSettlementDate(d).setAggreedFx(BigDecimal.valueOf(0.5))
				.setType(type).setUnits(amount).setUnitPrice(BigDecimal.TEN).build();
		return reply;
	}
}
