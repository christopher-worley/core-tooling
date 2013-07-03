package core.tooling.validation;

import java.lang.reflect.Field;

import core.tooling.lang.NumberComparator;
import core.tooling.logging.LogFactory;
import core.tooling.logging.Logger;

public class MaxValidator {

	/** logger for this class */
    private Logger logger = LogFactory.getLogger(MaxValidator.class);
    
	private Object value;
	
	private Max annotation;
	
	/** set in the constructor based on source of value to be validated */
	private String logPrefix = "";
	
	public MaxValidator(Max annotation, Object value) {
		this.value = value;
		this.annotation = annotation;
		logPrefix = "value=" + value;
	}
	
	public MaxValidator(Max annotation, Object object, Field field) throws IllegalArgumentException, IllegalAccessException {
		this.annotation = annotation;
		this.value = field.get(object);
		logPrefix = "object=" 
				+ object.getClass().getSimpleName()
				+ ",field="
				+ field.getName();
	}
	
	/**
	 * @return
	 */
	public String validate() {
		logger.debug("Max value validation found on field ("
				+ logPrefix
				+ ",annotation=" 
				+ annotation
				+ ").");
		
		
		if (value instanceof Number
				&& new NumberComparator().compare(value, annotation.value()) > 0) 
		{
			logger.debug("Max value exceeded for field (object="
        			+ logPrefix
        			+ ",value="
        			+ value
        			+ ")");
			return annotation.message();
		}
		return null;
	}
}
