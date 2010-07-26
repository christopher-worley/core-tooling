/**
 * Copyright 2009 Core Information Solutions LLC
 *
 * This file is part of Core Tooling.
 *
 * Core Tooling is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Core Tooling is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along 
 * with Core Tooling.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package core.tooling.test;

public class Assert extends org.junit.Assert
{

    /**
     * Assert that invoking <code>run</code> will throw an exception
     * of the given type
     * 
     * @param class1
     * @param runnable
     */
    public static void assertException(Class exception, Runnable runnable)
    {
        boolean exceptionCaught = false;
        try 
        {
            runnable.run();
        }
        catch(Exception e)
        {
            Assert.assertTrue(e.getClass().equals(exception));
            exceptionCaught = true;
        }
        if (!exceptionCaught)
        {
            fail("No exception caught");
        }
    }

}
