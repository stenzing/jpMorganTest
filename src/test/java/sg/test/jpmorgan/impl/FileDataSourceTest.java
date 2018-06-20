package sg.test.jpmorgan.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Test;

import sg.test.jpmorgan.api.DataSource;
import sg.test.jpmorgan.api.Instruction;
import sg.test.jpmorgan.api.InstructionType;
import sg.test.jpmorgan.api.UnuseableInputException;

public class FileDataSourceTest {

	private static final SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");

	@Test
	public void testInit() {
		DataSource target = FileDataSource.fromFile("src/test/resources/test_input_1.csv");

		Assert.assertNotNull(target);
	}

	@Test(expected = UnuseableInputException.class)
	public void testFileNotFound() {
		FileDataSource.fromFile("src/test/resources/nonexisting.csv");

		Assert.fail("Invalid file should throw UnuseableInputException");
	}

	@Test
	public void testReadFromFile() {
		DataSource target = FileDataSource.fromFile("src/test/resources/test_input_1.csv");
		Instruction result = target.readData();
		Assert.assertNotNull(result);
	}

	@Test
	public void testNoResultAfterEnd() {
		DataSource target = FileDataSource.fromFile("src/test/resources/test_input_1.csv");
		target.readData();
		target.readData();
		Instruction result = target.readData();
		Assert.assertNull(result);

	}

	@Test
	public void testReadMapping() throws ParseException {
		DataSource target = FileDataSource.fromFile("src/test/resources/test_input_1.csv");
		Instruction result = target.readData();
		Assert.assertTrue("user1".equals(result.getEntity()));
		Assert.assertEquals(InstructionType.B, result.getType());
		Assert.assertTrue(BigDecimal.valueOf(0.1).setScale(2).equals(result.getAggreedFx().setScale(2)));
		Assert.assertTrue("HUF".equals(result.getCurrency()));
		Assert.assertEquals(dateParser.parse("2018-06-20"), result.getInstructionDate());
		Assert.assertEquals(dateParser.parse("2018-06-21"), result.getSettlementDate());
		Assert.assertEquals(100, result.getUnits());
		Assert.assertTrue(BigDecimal.valueOf(10.3).setScale(2).equals(result.getUnitPrice().setScale(2)));
	}
}
