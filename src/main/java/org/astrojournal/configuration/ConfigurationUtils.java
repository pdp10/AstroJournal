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

import java.io.IOException;

/**
 * A collection of utilities for Configuration.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 18 Jan 2016
 */
public interface ConfigurationUtils {

    /**
     * Create a string containing the license.
     * 
     * @return a string
     */
    public String printLicense();

    /**
     * Print the current configuration.
     * 
     * @param config
     *            The configuration
     * @return the current configuration
     */
    public String printConfiguration(Configuration config);

    /**
     * Prepare input and output folders for AstroJournal if these do not exist.
     * 
     * @param config
     *            The configuration
     */
    public void prepareFolders(Configuration config);

    /**
     * Delete the previous output folder content if this is present.
     * 
     * @param config
     *            The configuration.
     * @throws IOException
     *             if the folder could not be cleaned.
     */
    public void cleanFolder(Configuration config) throws IOException;
}
