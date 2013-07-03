package core.tooling.validation;

import java.lang.reflect.Field;

import core.tooling.logging.LogFactory;
import core.tooling.logging.Logger;

public class RequiredValidator implements Validator {

	/** logger for this class */
    private Logger logger = LogFactory.getLogger(MaxValidator.class);
    
	private Object value;
	
	private Required annotation;
	
	/** set in the constructor based on source of value to be validated */
	private String logPrefix = "";
	
	public RequiredValidator(Required annotation, Object value) {
		this.value = value;
		this.annotation = annotation;
		logPrefix = "value=" + value;
	}
	
	public RequiredValidator(Required annotation, Object object, Field field) throws IllegalArgumentException, IllegalAccessException {
		this.annotation = annotation;
		this.value = field.get(object);
		logPrefix = "object=" 
				+ object.getClass().getSimpleName()
				+ ",field="
				+ field.getName();
	}
	
	@Override
	public String validate() {
		logger.debug("Required value validation found on field (object=" 
				+ logPrefix
				+ ",annotation=" 
				+ annotation
				+ ").");
		
		if (value == null) 
		{
			logger.debug("Required field cannot have null value (object="
        			+ logPrefix
        			+ ",value="
        			+ value
        			+ ")");
			return annotation.message();
		}
		return null;
	}

}
