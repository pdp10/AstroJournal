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
package org.astrojournal.configuration.ajconfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.AJMetaInfo;
import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ConfigurationUtils;
import org.astrojournal.generator.absgen.Importer;
import org.astrojournal.utilities.ClassesInstanceOf;
import org.astrojournal.utilities.PropertiesManager;
import org.astrojournal.utilities.ReadFromJar;

/**
 * A class containing the configuration of AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public class AJConfiguration implements Configuration {

    /** The logger */
    private static Logger log = LogManager.getLogger(AJConfiguration.class);

    /**
     * The locale prefix with the folder. This is static as it is used for
     * configuring localeBundle and validating the locale.
     */
    private static String localePrefix = "locale.aj_";

    /**
     * The user configuration file reference to the real file in the file
     * system.
     */
    private File configFile = null;

    /** The bundle for internationalisation */
    private ResourceBundle resourceBundle = ResourceBundle
	    .getBundle(localePrefix + AJPropertyConstants.LOCALE.getValue());

    /** The list of generators */
    private List<String> generatorNames = null;

    /**
     * The Properties for this application.
     */
    private Properties applicationProperties;

    /**
     * Default constructor.
     */
    public AJConfiguration() {
	init();
    }

    @Override
    public void loadSystemProperties() {
	log.debug("Loading system properties");
	Properties systemProperties = System.getProperties();
	Properties ajPropertiesFromSystem = new Properties();

	AJPropertyConstants[] keys = AJPropertyConstants.values();
	for (AJPropertyConstants key : keys) {
	    if (systemProperties.containsKey(key.getKey())) {
		ajPropertiesFromSystem.setProperty(key.getKey(),
			systemProperties.getProperty(key.getKey()));
	    }
	}

	log.debug("System properties loaded");
	validate(ajPropertiesFromSystem);
    }

    @Override
    public ResourceBundle getResourceBundle() {
	return resourceBundle;
    }

    @Override
    public List<String> getGeneratorNames() {
	return generatorNames;
    }

    @Override
    public String getProperty(String key) {
	return applicationProperties.getProperty(key);
    }

    @Override
    public ConfigurationUtils getConfigurationUtils() {
	return new AJConfigurationUtils();
    }

    /**
     * Save the properties to a XML file.
     */
    void saveProperties() {
	try {
	    PropertiesManager.storeToXML(applicationProperties, configFile
		    .getAbsolutePath(),
		    AJMetaInfo.USER_CONFIGURATION_PROPERTIES_FILE_COMMENT
			    .getInfo());
	} catch (IOException e) {
	    log.error(resourceBundle.getString("AJ.errWritingFile.text") + " "
		    + configFile.getAbsolutePath(), e);
	}
    }

    /**
     * Initialise the properties for this application by reading the application
     * properties, then an application-level configuration file (which can be
     * different for testing / main purposes), and then an user-level
     * configuration file. After these files are loaded, System properties are
     * checked for updates. Properties passed as System properties are not saved
     * in the user configuration file at this stage.
     */
    private void init() {

	applicationProperties = new Properties();
	initialiseApplicationProperties();
	log.debug(AJPropertyConstants.printAllProperties());
	// log.debug("Locale:"
	// + applicationProperties.getProperty(AJPropertyConstants.LOCALE
	// .getKey()));
	// log.debug(getConfigurationUtils().printConfiguration(this));
	initialiseGenerators();

	Properties defaultProperties = loadDefaultConfigurationFileProperties();
	if (!validate(defaultProperties)) {
	    log.error(resourceBundle
		    .getString("AJ.errDefaultConfigFailed.text"));
	}

	Properties userProperties = loadUserConfigurationFileProperties();
	if (userProperties.isEmpty()) {
	    saveProperties();
	    log.info(resourceBundle.getString("AJ.lblUserConfigSaved.text"));
	} else if (!validate(userProperties)) {
	    log.error(resourceBundle.getString("AJ.errUserConfig.text"));
	    saveProperties();
	    log.info(resourceBundle.getString("AJ.lblUserConfigRestored.text"));
	} else if (applicationProperties.size() > userProperties.size()) {
	    saveProperties();
	    log.info(resourceBundle.getString("AJ.lblUserConfigUpdated.text"));
	}

	// This method also validates the properties, so that the process is
	// done automatically and there is no need to offer the validation
	// method to the client.
	loadSystemProperties();

	// Don't save the application properties now, as at this stage system
	// properties can be passed as input parameters and they are only
	// temporary.

    }

    /** Retrieve the available generators by retrieving their package names. */
    private void initialiseGenerators() {
	// retrieve generator names
	generatorNames = ClassesInstanceOf.getClassPackageInstanceOf(
		"org.astrojournal.generator", Importer.class);
	for (int i = 0; i < generatorNames.size(); i++) {
	    String[] packageHierachy = generatorNames.get(i).split("\\.");
	    generatorNames.set(i, packageHierachy[packageHierachy.length - 1]);
	}
    }

    /**
     * Adjust the files location when setting the initial configuration from
     * Java class and from default configuration file. This information is not
     * known a priori (we don't know the user.home!). Don't invoke this method
     * after calling the user configuration file instead.
     * 
     * @param properties
     */
    private void adjustFilesLocationPath(Properties properties) {
	if (properties.containsKey(AJPropertyConstants.FILES_LOCATION.getKey())) {
	    properties.setProperty(
		    AJPropertyConstants.FILES_LOCATION.getKey(),
		    System.getProperty("user.home")
			    + File.separator
			    + properties.get(AJPropertyConstants.FILES_LOCATION
				    .getKey()));
	}
    }

    /**
     * Configure applicationProperties with AJProperties. This is invoked inside
     * the init() procedure. Don't pass it as constructor parameter as
     * AJProperties cannot be extended. If AJConfiguration is extended, a new
     * enum will have to be defined for this new configuration (if this is the
     * desired implementation).
     */
    private void initialiseApplicationProperties() {
	log.debug("Loading application properties from "
		+ AJPropertyConstants.class.getName());
	AJPropertyConstants[] keys = AJPropertyConstants.values();
	for (AJPropertyConstants key : keys) {
	    applicationProperties.setProperty(key.getKey(), key.getValue());
	}
	adjustFilesLocationPath(applicationProperties);
	log.debug("Application properties are loaded");
    }

    /**
     * Parse the default configuration file and store the properties in
     * defaultProperties. Then define a method validation(Properties
     * newProperties), which will copy the new property values in
     * applicationProperties if these are okay, otherwise don't replace them
     * 
     * @return the default properties
     */
    private Properties loadDefaultConfigurationFileProperties() {
	String defaultConfigurationFile = AJMetaInfo.DEFAULT_CONFIGURATION_PROPERTIES_FILE_NAME
		.getInfo();
	log.debug("Loading default configuration file: "
		+ defaultConfigurationFile);
	Properties defaultProperties = new Properties();
	try {
	    // this file is in resources/
	    File temp = new ReadFromJar().getFileFromJARFile("aj_config_", "/"
		    + defaultConfigurationFile);
	    log.debug("Extracted " + defaultConfigurationFile
		    + " from JAR and stored in " + temp.getAbsolutePath());
	    defaultProperties = PropertiesManager.loadFromXML(temp
		    .getAbsolutePath());
	} catch (IOException e) {
	    log.error(resourceBundle
		    .getString("AJ.errDefaultConfigFailed.text"));
	}
	adjustFilesLocationPath(defaultProperties);
	log.debug("Default configuration file is loaded.");
	return defaultProperties;
    }

    /**
     * Parse the user configuration file and store the properties in
     * userProperties. Then invoke the method validation(Properties
     * newProperties) passing this set of properties. Same procedure as above.
     * 
     * @return the user properties
     */
    private Properties loadUserConfigurationFileProperties() {
	// These properties are in the user space
	configFile = AJConfigurationUtils.setupUserConfigurationFile();
	Properties userProperties = new Properties();
	if (configFile != null && configFile.exists()) {
	    log.debug("Loading user configuration file: "
		    + configFile.getAbsolutePath());
	    try {
		userProperties = PropertiesManager.loadFromXML(userProperties,
			configFile.getAbsolutePath());
	    } catch (IOException e) {
		log.error(resourceBundle
			.getString("AJ.errReadingUserConfig.text"));
		return userProperties;
	    }
	    log.debug("User configuration file is loaded.");
	} else {
	    log.info(resourceBundle
		    .getString("AJ.lblUserConfigFileNotFound.text"));
	}
	return userProperties;
    }

    /**
     * Validate the loaded properties for this application.
     * 
     * @return true if the validation succeeded.
     */
    private boolean validate(Properties properties) {
	log.debug("Validating properties");

	// Check the file separator. For LaTeX this must be a slash, even on
	// windows.
	adjustFileSeparatorForLatex(properties);
	boolean status = true;
	String[] keys = properties.keySet().toArray(new String[0]);
	for (String key : keys) {
	    // LOCALE and FILES_LOCATION are treated separately
	    if (key.equals(AJPropertyConstants.LOCALE.getKey())) {
		status = status && localeValidation(properties, key);
	    } else if (key.equals(AJPropertyConstants.FILES_LOCATION.getKey())) {
		status = status && filepathValidation(properties, key);
	    } else if (key.equals(AJPropertyConstants.GENERATOR_NAME.getKey())) {
		status = status && generatorValidation(properties, key);
	    } else {
		status = status && genericValidation(properties, key);
	    }
	}
	log.debug("Properties are validated.");
	return status;
    }

    /**
     * Validate a property containing a language LOCALE.
     * 
     * @param properties
     * @param key
     * @return true if the validation for this property is valid.
     */
    private boolean localeValidation(Properties properties, String key) {
	boolean status = true;
	String value = properties.getProperty(key);
	if (value == null || value.equals("")) {
	    // Check the key exists.
	    status = false;
	    log.error("Property not valid: " + key);
	} else {
	    // Check that the file exists and is not empty
	    String localeFile = localePrefix.replace('.', '/') + value
		    + ".properties";
	    log.debug("Loading " + localeFile + " from Jar");
	    try {
		// Retrieve the string containing the file if this exists.
		String bundleString = new ReadFromJar()
			.getStringFileFromJARFile("/" + localeFile);
		log.debug("Locale file " + localeFile + " is loaded.");
		if (bundleString == null || bundleString.isEmpty()) {
		    status = false;
		    log.error("The locale file for the property : " + key
			    + " is not valid.");
		} else {
		    resourceBundle = ResourceBundle.getBundle(localePrefix
			    + value);
		    applicationProperties.setProperty(key, value);
		}
	    } catch (IOException e) {
		status = false;
		log.debug(e);
		log.error("The locale : " + value
			+ " does not exist. Using previous locale setting.");
	    }
	}
	log.debug(key + ":" + applicationProperties.getProperty(key));
	return status;
    }

    /**
     * Validate a property containing a file path as value.
     * 
     * @param properties
     * @param key
     * @return true if the validation for this property is valid.
     */
    private boolean filepathValidation(Properties properties, String key) {
	boolean status = true;
	String value = properties.getProperty(key);
	File file = new File(key);
	// Let's temporarily create this folder to test it.
	boolean created = false;
	if (file != null) {
	    created = file.mkdir();
	}
	if (file == null || !file.exists() || !file.canWrite()) {
	    status = false;
	    log.error("Property not valid: " + key);
	} else {
	    applicationProperties.setProperty(key, value);
	}
	if (created) {
	    file.delete();
	}
	log.debug("Using " + key + ":" + applicationProperties.getProperty(key));
	return status;
    }

    /**
     * Validate the generator name.
     * 
     * @param properties
     * @param key
     * @return true if the validation for this property is valid.
     */
    private boolean generatorValidation(Properties properties, String key) {
	boolean status = true;
	String value = properties.getProperty(key);
	if (value == null || value.equals("")) {
	    status = false;
	    log.error("Property not valid: " + key);
	} else {
	    boolean found = false;
	    for (int i = 0; i < generatorNames.size() && !found; i++) {
		if (generatorNames.get(i).equals(value)) {
		    applicationProperties.setProperty(key, value);
		    found = true;
		}
	    }
	    if (!found) {
		status = false;
		log.error("Property not valid: " + key + ":=" + value);
	    }
	}
	log.debug("Using " + key + ":" + applicationProperties.getProperty(key));
	return status;
    }

    /**
     * Validate the property key in properties and add it to the application
     * properties if this is fine.
     * 
     * @param properties
     * @param key
     * @return true if the validation for this property is valid.
     */
    private boolean genericValidation(Properties properties, String key) {
	boolean status = true;
	String value = properties.getProperty(key);
	if (value == null || value.equals("")) {
	    status = false;
	    log.error("Property not valid: " + key);
	} else {
	    applicationProperties.setProperty(key, value);
	}
	log.debug("Using " + key + ":" + applicationProperties.getProperty(key));
	return status;
    }

    /**
     * Adjust the file separator if needed. The file separator must be '/' as
     * this is the default file separator in LaTeX. Therefore, let's replace '\'
     * with '/'.
     * 
     * @param newProperties
     */
    private void adjustFileSeparatorForLatex(Properties newProperties) {

	String[] propertiesToAdjust = new String[] {
		AJPropertyConstants.RAW_REPORTS_FOLDER.getKey(),
		AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER.getKey(),
		AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_DATE.getKey(),
		AJPropertyConstants.LATEX_REPORT_BY_DATE_FILENAME.getKey(),
		AJPropertyConstants.LATEX_HEADER_BY_DATE_FILENAME.getKey(),
		AJPropertyConstants.LATEX_FOOTER_BY_DATE_FILENAME.getKey(),

		AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_TARGET.getKey(),
		AJPropertyConstants.LATEX_REPORT_BY_TARGET_FILENAME.getKey(),
		AJPropertyConstants.LATEX_HEADER_BY_TARGET_FILENAME.getKey(),
		AJPropertyConstants.LATEX_FOOTER_BY_TARGET_FILENAME.getKey(),

		AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
			.getKey(),
		AJPropertyConstants.LATEX_REPORT_BY_CONSTELLATION_FILENAME
			.getKey(),
		AJPropertyConstants.LATEX_HEADER_BY_CONSTELLATION_FILENAME
			.getKey(),
		AJPropertyConstants.LATEX_FOOTER_BY_CONSTELLATION_FILENAME
			.getKey() };

	for (String key : propertiesToAdjust) {
	    String value = newProperties.getProperty(key);
	    if (value != null) {
		while (value.length() > 0
			&& (value.startsWith("\\") || value.startsWith("/"))) {
		    value = value.substring(1);
		}
		newProperties.setProperty(key, value.replace("\\", "/"));
	    }
	}
    }
}