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
package main;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.ajconfiguration.AJPropertyConstants;
import org.astrojournal.configuration.ajconfiguration.AppMetaInfo;
import org.astrojournal.utilities.PropertiesManager;
import org.astrojournal.utilities.ReadFromJar;

/**
 * A set of utilities for the tests.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 15 Jan 2016
 */
public class AJConfigurationTestUtils {

    /** The logger */
    private static Logger log = LogManager
	    .getLogger(AJConfigurationTestUtils.class);

    /**
     * Return the default application properties as Java Properties.
     * 
     * @return the default application properties.
     */
    private static Properties getDefaultApplicationProperties() {
	log.debug("Loading default configuration file: "
		+ AppMetaInfo.DEFAULT_CONFIGURATION_PROPERTIES_FILE_NAME
			.getInfo());
	Properties defaultProperties = new Properties();
	try {
	    // DEFAULT APPLICATION PROPERTIES: these are in resources/
	    File temp = new ReadFromJar()
		    .getFileFromJARFile(
			    "aj_config_",
			    "/"
				    + AppMetaInfo.DEFAULT_CONFIGURATION_PROPERTIES_FILE_NAME
					    .getInfo());
	    log.debug("Extracted "
		    + AppMetaInfo.DEFAULT_CONFIGURATION_PROPERTIES_FILE_NAME
			    .getInfo() + " from JAR and stored in "
		    + temp.getAbsolutePath());
	    defaultProperties = PropertiesManager.loadFromXML(temp
		    .getAbsolutePath());
	    defaultProperties.put(
		    AJPropertyConstants.FILES_LOCATION.getKey(),
		    System.getProperty("user.home")
			    + File.separator
			    + defaultProperties
				    .get(AJPropertyConstants.FILES_LOCATION
					    .getKey()));
	} catch (IOException e) {
	    log.debug(e, e);
	    log.error("Errors reading the default configuration file");

	}
	log.debug("Default configuration file is loaded.");
	return defaultProperties;
    }

    /**
     * Reset the default application properties.
     */
    public static void resetDefaultProperties() {
	// Notify AJConfig that the system properties have changed.
	Properties defaultProperties = AJConfigurationTestUtils
		.getDefaultApplicationProperties();
	PropertiesManager.updateSystemProperties(defaultProperties);
    }
}
