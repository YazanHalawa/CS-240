package server.database;

@SuppressWarnings("serial")
/**
 * This class holds the information for a Database Exception
 * @author Yazan Halawa
 *
 */
public class DatabaseException extends Exception {

	public DatabaseException() {
		return;
	}

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(Throwable cause) {
		super(cause);

	}

	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

}
