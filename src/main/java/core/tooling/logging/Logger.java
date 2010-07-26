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

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.log4j.Level;

/**
 * logger api for core framework
 * 
 * This allows core decoupled from 3rd party loggers
 * 
 * @author worleyc
 * 
 */
public class Logger implements Log
{
    
    // log4j logger
    private org.apache.log4j.Logger logger;
    
    /**
     * Default constructor
     * 
     * @param owner
     */
    public Logger(Class owner)
    {
        logger = org.apache.log4j.Logger.getLogger(owner);
    }

    @Override
    public void debug(Object message) {
        logger.debug(message);
    }

    @Override
    public void debug(Object message, Throwable throwable) {
        logger.debug(message, throwable);
    }

    public void debug(String message, Object ... params) {
        if (isDebugEnabled()) {
            logger.debug(MessageFormat.format(message, params));
        }
    }

    public void debug(String message, Throwable throwable, Object ... params) {
        if (isDebugEnabled()) {
            logger.debug(MessageFormat.format(message, params), throwable);
        }
    }

    @Override
    public void error(Object message) {
        logger.error(message);
    }

    @Override
    public void error(Object message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public void error(String message, Object ... params) {
        if (isErrorEnabled()) {
            logger.error(MessageFormat.format(message, params));
        }
    }

    public void error(String message, Throwable throwable, Object ... params) {
        if (isErrorEnabled()) {
            logger.error(MessageFormat.format(message, params), throwable);
        }
    }

    @Override
    public void fatal(Object message) {
        logger.fatal(message);
    }

    @Override
    public void fatal(Object message, Throwable throwable) {
        logger.fatal(message, throwable);
    }

    public void fatal(String message, Object ... params) {
        if (isFatalEnabled()) {
            logger.fatal(MessageFormat.format(message, params));
        }
    }

    public void fatal(String message, Throwable throwable, Object ... params) {
        if (isFatalEnabled()) {
            logger.fatal(MessageFormat.format(message, params), throwable);
        }
    }

    @Override
    public void info(Object message) {
        logger.info(message);
    }

    @Override
    public void info(Object message, Throwable throwable) {
        logger.info(message, throwable);
    }

    public void info(String message, Object ... params) {
        if (isInfoEnabled()) {
            logger.info(MessageFormat.format(message, params));
        }
    }

    public void info(String message, Throwable throwable, Object ... params) {
        if (isInfoEnabled()) {
            logger.info(MessageFormat.format(message, params), throwable);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isEnabledFor(Level.DEBUG);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isEnabledFor(Level.ERROR);
    }

    @Override
    public boolean isFatalEnabled() {
        return logger.isEnabledFor(Level.FATAL);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isEnabledFor(Level.INFO);
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isEnabledFor(Level.TRACE);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isEnabledFor(Level.WARN);
    }

    @Override
    public void trace(Object message) {
        logger.trace(message);
    }

    @Override
    public void trace(Object message, Throwable throwable) {
        logger.trace(message, throwable);
    }

    public void trace(String message, Object ... params) {
        if (isTraceEnabled()) {
            logger.trace(MessageFormat.format(message, params));
        }
    }

    public void trace(String message, Throwable throwable, Object ... params) {
        if (isTraceEnabled()) {
            logger.trace(MessageFormat.format(message, params), throwable);
        }
    }

    @Override
    public void warn(Object message) {
        logger.warn(message);
    }

    @Override
    public void warn(Object message, Throwable throwable) {
        logger.warn(message, throwable);
    }

    public void warn(String message, Object ... params) {
        if (isWarnEnabled()) {
            logger.warn(MessageFormat.format(message, params));
        }
    }

    public void warn(String message, Throwable throwable, Object ... params) {
        if (isWarnEnabled()) {
            logger.warn(MessageFormat.format(message, params), throwable);
        }
    }
}
