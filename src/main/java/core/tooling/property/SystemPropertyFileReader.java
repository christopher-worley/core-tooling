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
package core.tooling.property;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import core.tooling.execption.CoreToolException;

/**
 * Read properties file and create system properties with
 * the contents
 * 
 * @author worleyc
 *
 */
public class SystemPropertyFileReader
{
    private String filename;

    /** properties */
    private Properties properties;

    /**
     * 
     */
    public SystemPropertyFileReader(String filename)
    {
        super();
        this.filename = filename;
        initialize();
    }

    /**
     * 
     */
    private void initialize() {
        try {
            properties = new Properties();
            URL url = ClassLoader.getSystemResource(filename);
            if(url == null) {
                throw new CoreToolException("Could not get URL for resource "+filename);
            }
            properties.load(url.openStream());
        } catch (IOException e) {
            throw new CoreToolException(e);
        }
        
        createSystemProperties();
    }

    /**
     * Read all properties in file and create
     * system properties
     * 
     */
    private void createSystemProperties()
    {
        for (Iterator iter = properties.keySet().iterator(); iter.hasNext();) 
        {
            Object key = iter.next();
            System.setProperty((String)key, (String)properties.get(key));
        }
        
    }

}
