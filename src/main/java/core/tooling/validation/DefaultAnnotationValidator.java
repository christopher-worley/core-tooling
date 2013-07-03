package core.tooling.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import core.tooling.logging.LogFactory;
import core.tooling.logging.Logger;

public class DefaultAnnotationValidator implements Validator
{
	/** logger for this class */
	Logger logger = LogFactory.getLogger(DefaultAnnotationValidator.class);

	/** objects to validate */
	Object[] objects;

	/**
	 * @param method
	 * @param objects
	 */
	public DefaultAnnotationValidator(Object[] objects) 
	{
		super();
		this.objects = objects;
	}
	
	
	/**
	 * @param object
	 * @param field
	 * @param annotation
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private String doMax(Object object, Field field, Max annotation) throws IllegalArgumentException, IllegalAccessException
	{
		MaxValidator validator = new MaxValidator(annotation, object, field);
		return validator.validate();
	}
	
	/**
	 * @param object
	 * @param field
	 * @param annotation
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private String doMin(Object object, Field field, Min annotation) throws IllegalArgumentException, IllegalAccessException
	{
		MinValidator validator = new MinValidator(annotation, object, field);
		return validator.validate();
	}


	/**
	 * @param object
	 * @param field
	 * @param annotation
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private String doRequired(Object object, Field field, Required annotation) throws IllegalArgumentException, IllegalAccessException
	{
		RequiredValidator validator = new RequiredValidator(annotation, object, field);
		return validator.validate();
	}


	/**
	 * @param object
	 * @param field
	 * @param annotation
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String doValidateString(Object object, Field field,
			StringValidation annotation) throws IllegalArgumentException, IllegalAccessException
	{
		StringValidator validator = new StringValidator(annotation, object, field);
		return validator.validate();
	}


	/* (non-Javadoc)
	 * @see core.service.validation.Validator#validate()
	 */
	public String validate() 
	{
		logger.debug("Validating objects (count=" + (objects == null ? "null" : objects.length) + ").");
		if (objects != null) 
		{
			for (Object object : objects)
			{
				try
				{
					return validate(object);
				} 
				catch (IllegalArgumentException e)
				{
					throw new ValidateException("Failed to validate objects.", e);
				} 
				catch (IllegalAccessException e)
				{
					throw new ValidateException("Failed to validate objects.", e);
				} 
				catch (InstantiationException e)
				{
					throw new ValidateException("Failed to validate objects.", e);
				} 
				catch (SecurityException e)
				{
					throw new ValidateException("Failed to validate objects.", e);
				} 
				catch (NoSuchMethodException e)
				{
					throw new ValidateException("Failed to validate objects.", e);
				} 
				catch (InvocationTargetException e)
				{
					throw new ValidateException("Failed to validate objects.", e);
				}
			}
		}
		return null;
	}


	/**
	 * find any validation annotations on class fields.  Validate any that
	 * are found.
	 * 
	 * @param object
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	private String validate(Object object) throws IllegalArgumentException, IllegalAccessException, InstantiationException, SecurityException, NoSuchMethodException, InvocationTargetException 
	{
		Field[] fields = object.getClass().getDeclaredFields();
		if (fields != null) 
		{
			logger.debug("Validating fields for object (objectClass=" + object.getClass().getSimpleName() + ",fieldCount=" + fields.length + ").");
    		for (Field field : fields) 
    		{
    			field.setAccessible(true);
    			return validate(object, field);
    		}
		}
		else 
		{
			logger.debug("No fields found for object (objectClass=" + object.getClass().getSimpleName() + ").");
		}
		return null;
	}


	/**
	 * find any validation annotations on the field.  Validate any that 
	 * are found.
	 * 
	 * @param object
	 * @param field
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	private String validate(Object object, Field field) throws IllegalArgumentException, IllegalAccessException, InstantiationException, SecurityException, NoSuchMethodException, InvocationTargetException 
	{
		Annotation[] annotations = field.getAnnotations();
		if (annotations != null) 
		{
			for (Annotation annotation : annotations) 
			{
				if (annotation instanceof StringValidation)
				{
					return doValidateString(object, field, (StringValidation) annotation);
				} 
				else if (annotation instanceof Required) 
				{
					return doRequired(object, field, (Required) annotation);
				}
				else if (annotation instanceof Max) 
				{
					return doMax(object, field, (Max) annotation);
				}
				else if (annotation instanceof Min) 
				{
					return doMin(object, field, (Min) annotation);
				}
				else if (annotation instanceof Validate) 
				{
					return doValidate(object, field, annotation);
				}
				else
				{
					return validate(object, field, annotation);
				}
			}
		}
		return null;
	}


	/**
	 * @param object
	 * @param field
	 * @param annotation
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	private String doValidate(Object object, Field field, Annotation annotation) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
	{
		// cast validate annotation
		Validate validate = (Validate) annotation;
		// get constructor 
		Constructor constructor = validate.validationClass().getConstructor(Object[].class);
		// instantiate validator class for validating the value of the field
		Object[] args = new Object[]{field.get(object)};
		// pass array to constructor, wrap in Object[] to deal with java 1.5 ... args
		Validator validator = (Validator) constructor.newInstance(new Object[] {args});
		// validate 
		try 
		{
			return validator.validate();
		}
		catch (ValidateException e)
		{
			throw new ValidateException(validate.message() + ": " + e.getMessage());
		}
	}


	/**
	 * Override to implement validation for more annotations
	 * 
	 * @param object
	 * @param field
	 * @param annotation
	 */
	protected String validate(Object object, Field field, Annotation annotation)
	{
		logger.debug("Not validation annotation: " + annotation.getClass().getSimpleName());
		return null;
	}
	
	
}
