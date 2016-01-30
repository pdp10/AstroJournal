/*
 * Copyright 2015 Piero Dalle Pezze
 *
 * This file is part of AstroJournal.
 *
 * AstroJournal is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
/*
 * Changelog:
 * - Piero Dalle Pezze: class creation.
 */
package org.astrojournal.configuration;

import java.util.List;
import java.util.ResourceBundle;

/**
 * The interface of the application configuration.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 12 Dec 2015
 */
public interface Configuration {

    /**
     * Update the application and user property values with the property values
     * defined as System properties if these are defined. A validation process
     * will occur checking that the inserted property values are consistent with
     * their meaning.
     */
    public void loadSystemProperties();

    /**
     * Return the locale bundle containing the application strings.
     * 
     * @return the localeBundle
     */
    public ResourceBundle getResourceBundle();

    /**
     * Return the value for a property key.
     * 
     * @param key
     *            the property key to retrieve or null if this does not exist.
     * @return the value for the Java property key
     */
    public String getProperty(String key);

    /**
     * Return the configuration utils for this configuration.
     * 
     * @return ConfigurationUtils
     */
    public ConfigurationUtils getConfigurationUtils();

    /**
     * Return the list of available generators
     * 
     * @return the list of generators
     */
    public List<String> getGeneratorNames();

}