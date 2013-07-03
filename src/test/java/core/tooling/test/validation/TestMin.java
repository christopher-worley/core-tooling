package core.tooling.test.validation;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import core.tooling.logging.LogFactory;
import core.tooling.logging.Logger;
import core.tooling.validation.DefaultAnnotationValidator;
import core.tooling.validation.Min;
import core.tooling.validation.ValidateException;
import core.tooling.validation.Validator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-tooling-context.xml")
public class TestMin
{
    /** logger for this class */
    private Logger logger = LogFactory.getLogger(TestMin.class);
	
    @Autowired
    private ApplicationContext context;

    @Before
    public void setup()
    {
    }

    @Test
    public void testMin() 
    {
    	final class MinClass
    	{
    		@Min(value=10,message="integer")
    		private Integer integer;
    		
    		@Min(value=100.1,message="money")
    		private Double money;

			public Integer getInteger()
			{
				return integer;
			}

			public Double getMoney()
			{
				return money;
			}

			public void setInteger(Integer integer)
			{
				this.integer = integer;
			}

			public void setMoney(Double money)
			{
				this.money = money;
			}
    	}
    	
    	// Create object to validate
    	MinClass maxClass = new MinClass();
    	maxClass.setInteger(220);
    	maxClass.setMoney(200.0);
    	
    	Validator executor = new DefaultAnnotationValidator(new Object[] {maxClass});
    	// validate valid initial values
    	executor.validate();

    	// set to higher value that will fail
    	maxClass.setInteger(1);
    	try
    	{
    		executor.validate();
    	}
    	catch (ValidateException e)
    	{
    		Assert.assertEquals("integer", e.getMessage());
    	}

    	// set integer back to valid value, set money to invalid value
    	maxClass.setInteger(220);
    	maxClass.setMoney(1.2);
    	
    	try
    	{
    		executor.validate();
    	}
    	catch (ValidateException e)
    	{
    		Assert.assertEquals("money", e.getMessage());
    	}
    	
    }
}
