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
package core.tooling.logging;


/**
 * LogFactory creates loggers and keeps instances of
 * all loggers so, they want be recreated
 * 
 * @author worleyc
 *
 */
public class LogFactory 
{

	/** instance */
	private static LogFactory instance;

	/**
	 * get singleton instance
	 */
	public static LogFactory getInstance() 
	{
		if (instance == null)
		{
			instance = new LogFactory();
		}
		return instance;
	}
	
	/**
	 * return logger for class
	 * 
	 * @param owner
	 * @return
	 */
	public static Logger getLogger(Class owner)
	{
	    return new Logger(owner);
	}
	
	/**
	 * Default constructor
	 */
	protected LogFactory() 
	{
	}
}
