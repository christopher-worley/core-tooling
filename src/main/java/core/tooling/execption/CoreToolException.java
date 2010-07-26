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
package core.tooling.execption;

/**
 * This excepiton is used to wrap checked exceptions
 * in the Core tooling classes
 * 
 * @author cworley
 *
 */
public class CoreToolException extends RuntimeException
{

    public CoreToolException()
    {
        super();
    }

    public CoreToolException(String message)
    {
        super(message);
    }

    public CoreToolException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CoreToolException(Throwable cause)
    {
        super(cause);
    }

}
