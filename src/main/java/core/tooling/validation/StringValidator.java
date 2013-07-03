package core.tooling.validation;

import java.lang.reflect.Field;

import core.tooling.logging.LogFactory;
import core.tooling.logging.Logger;

public class StringValidator implements Validator {

	/** logger for this class */
    private Logger logger = LogFactory.getLogger(MaxValidator.class);
    
	private Object value;
	
	private StringValidation annotation;
	
	/** set in the constructor based on source of value to be validated */
	private String logPrefix = "";
	
	public StringValidator(StringValidation annotation, Object value) {
		this.value = value;
		this.annotation = annotation;
		logPrefix = "value=" + value;
	}
	
	public StringValidator(StringValidation annotation, Object object, Field field) throws IllegalArgumentException, IllegalAccessException {
		// verify java.lang.String type is annotated
		if (!field.getType().equals(java.lang.String.class)) 
		{
			throw new IllegalArgumentException("StringValidation can only annotate java.lang.String types (object="
					+ object.getClass().getSimpleName()
					+ ",field="
					+ field.getName()
					+ ").");
		}
		
		this.annotation = annotation;
		this.value = field.get(object);
		logPrefix = "object=" 
				+ object.getClass().getSimpleName()
				+ ",field="
				+ field.getName();
	}
	
	@Override
	public String validate() {
		logger.debug("String validation found on field (object=" 
				+ logPrefix
				+ ",annotation=" 
				+ annotation
				+ ").");
		
		// cast a string value
		String stringValue = (String) value;
		String info = "(object="
			+ logPrefix
			+ ",value="
			+ stringValue
			+ ")";
		
		// validate annotation options
		if (!annotation.allowNull()
				&& value == null) 
		{
			logger.debug("String validation does not allow null, value was found to be null " + info + ".");
			return annotation.message();
		}
		else if (!annotation.allowEmpty()
				&& (stringValue != null && stringValue.length() < 1))
		{
			logger.debug("String validation does not allow empty strings, value was found to be empty " + info + ".");
			return annotation.message();
		}
		// only check if stringValue is not empty
		else if (stringValue != null 
				&& stringValue.length() > 0 
				&& annotation.anyOf() != null 
				&& annotation.anyOf().trim().length() > 0) 
		{
			String[] anyOf = annotation.anyOf().split(",");
			boolean found = false;
			for (String oneOf : anyOf) 
			{
				if (oneOf.equals(stringValue)) 
				{
					found = true;
				}
			}
			if (!found) 
			{
    			logger.debug("String validation provides a list of possible values, the string does not equal any of the given values " + info + ".");
    			return annotation.message();
			}
		}
		else if (annotation.maxSize() > -1 
				&& stringValue != null
				&& stringValue.length() > annotation.maxSize())
			
		{
			logger.debug("String validation limits the max size of the string, the value of the string was found to exceed the limit " + info + ".");
			return annotation.message()	;
		}
		return null;
	}

}
