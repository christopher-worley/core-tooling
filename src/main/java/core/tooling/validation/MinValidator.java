package core.tooling.validation;

import java.lang.reflect.Field;

import core.tooling.lang.NumberComparator;
import core.tooling.logging.LogFactory;
import core.tooling.logging.Logger;


public class MinValidator implements Validator {

	/** logger for this class */
    private Logger logger = LogFactory.getLogger(MaxValidator.class);
    
	private Object value;
	
	private Min annotation;
	
	/** set in the constructor based on source of value to be validated */
	private String logPrefix = "";
	
	public MinValidator(Min annotation, Object value) {
		this.value = value;
		this.annotation = annotation;
		logPrefix = "value=" + value;
	}
	
	public MinValidator(Min annotation, Object object, Field field) throws IllegalArgumentException, IllegalAccessException {
		this.annotation = annotation;
		this.value = field.get(object);
		logPrefix = "object=" 
				+ object.getClass().getSimpleName()
				+ ",field="
				+ field.getName();
	}

	@Override
	public String validate() {
		logger.debug("Min value validation found on field (object=" 
				+ logPrefix
				+ ",annotation=" 
				+ annotation
				+ ").");
		
		if (value instanceof Number
				&& new NumberComparator().compare(annotation.value(), value) > -1) 
		{
			logger.debug("Min value exceeded for field (object="
        			+ logPrefix
        			+ ",value="
        			+ value
        			+ ")");
			return annotation.message();
		}
		return null;
	}

}
