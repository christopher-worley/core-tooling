package core.tooling.validation;


public interface Validator
{

	/**
	 * Perform validation on object.
	 * 
	 * If null is returned it is expected that no errors exist.  Otherwise
	 * return a non null value which represents an error message.  The
	 * error message should be considered readable by the end user.
	 * 
	 * @return error message, otherwise null
	 */
	public String validate();

}
