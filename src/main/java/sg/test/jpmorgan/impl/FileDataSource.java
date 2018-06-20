package sg.test.jpmorgan.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import sg.test.jpmorgan.api.DataSource;
import sg.test.jpmorgan.api.Instruction;
import sg.test.jpmorgan.api.Instruction.Builder;
import sg.test.jpmorgan.api.InstructionType;
import sg.test.jpmorgan.api.UnuseableInputException;

public class FileDataSource implements DataSource {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private BufferedReader source;

	public static FileDataSource fromFile(String filename) {
		try {
			File file = Paths.get(filename).toFile();
			FileDataSource result = new FileDataSource(file);
			return result;
		} catch (Exception e) {
			throw new UnuseableInputException("File could not be opened as datasource");
		}
	}

	private FileDataSource(File file) throws FileNotFoundException {
		source = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	}

	public Instruction readData() {
		try {
			String line = source.readLine();
			String[] parts = line.split(";");
			Builder b = new Builder().setEntity(parts[0]).setType(InstructionType.valueOf(parts[1]))
					.setAggreedFx(new BigDecimal(parts[2])).setCurrency(parts[3])
					.setInstructionDate(dateFormat.parse(parts[4])).setSettlementDate(dateFormat.parse(parts[5]))
					.setUnits(Integer.parseInt(parts[6])).setUnitPrice(new BigDecimal(parts[7]));
			return b.build();
		} catch (Exception e) {

		}
		return null;
	}

}
