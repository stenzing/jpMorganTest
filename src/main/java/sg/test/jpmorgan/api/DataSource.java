package sg.test.jpmorgan.api;

/**
 * Interface to retrive data from any source.
 * 
 * @author gstenzinger
 *
 */
public interface DataSource {
	/**
	 * Return next {@link Instruction} or <code>null</code> if no data available.
	 * 
	 * @return next instruction from source
	 */
	Instruction readData();
}
