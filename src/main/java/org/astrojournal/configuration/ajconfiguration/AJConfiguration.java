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
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ConfigurationUtils;
import org.astrojournal.utilities.PropertiesManager;
import org.astrojournal.utilities.ReadFromJar;

/**
 * A class containing the configuration of AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 12 Dec 2015
 */
public class AJConfiguration implements Configuration {

    /** The logger */
    private static Logger log = LogManager.getLogger(AJConfiguration.class);

    /**
     * The user configuration file reference to the real file in the file
     * system.
     */
    private File configFile = null;

    /** The bundle for internationalisation */
    private ResourceBundle localeBundle = ResourceBundle
	    .getBundle(AJPropertyConstants.LOCALE.getValue());

    /**
     * The Properties for this application.
     */
    private Properties applicationProperties;

    /*
     * These data field store the previous value of each Property if this has
     * changed and not yet validated. They are only required for validation. The
     * rest of the program uses the properties instead. We initialise them with
     * the default value found in AJProperties. These values will be overwritten
     * by the default configuration file, and then by the user configuration
     * file, if found. If the default configuration file is corrupted, a rescue
     * procedure is implemented for restoring the fields from AJProperties,
     * skipping the file.
     */
    // FLAGS
    /** True if the application should run quietly */
    private boolean quiet = Boolean.parseBoolean(AJPropertyConstants.QUIET
	    .getValue());

    /** True if latex output should be printed. */
    private boolean showLatexOutput = Boolean
	    .parseBoolean(AJPropertyConstants.SHOW_LATEX_OUTPUT.getValue());

    /** True if the license should be shown at start. */
    private boolean showLicenseAtStart = Boolean
	    .parseBoolean(AJPropertyConstants.SHOW_LICENSE_AT_START.getValue());

    /** True if the version of pdflatex. */
    private boolean showPDFLatexVersionAtStart = Boolean
	    .parseBoolean(AJPropertyConstants.SHOW_PDFLATEX_VERSION_AT_START
		    .getValue());

    /** True if the configuration should be shown at start. */
    private boolean showConfigurationAtStart = Boolean
	    .parseBoolean(AJPropertyConstants.SHOW_CONFIGURATION_AT_START
		    .getValue());

    // MAIN FOLDER
    /** The absolute path containing AstroJournal input and output folders. */
    private File filesLocation = new File(
	    AJPropertyConstants.FILES_LOCATION.getValue());

    // INPUT FOLDER
    /**
     * The relative path containing the raw files (observation input folder).
     */
    private String rawReportsFolder = AJPropertyConstants.RAW_REPORTS_FOLDER
	    .getValue();

    // THE HEADER / FOOTER FOLDER
    /** The folder containing the latex header and footer. */
    private String latexHeaderFooterFolder = AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER
	    .getValue();

    // LATEX REPORT BY DATE
    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    private String latexReportsFolderByDate = AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_DATE
	    .getValue();

    /** The name of the main Latex file sorted by date. */
    private String latexReportByDateFilename = AJPropertyConstants.LATEX_REPORT_BY_DATE_FILENAME
	    .getValue();

    /** The Latex header with path for astrojournal by date. */
    private String latexHeaderByDateFilename = AJPropertyConstants.LATEX_HEADER_BY_DATE_FILENAME
	    .getValue();

    /** The Latex footer with path for astrojournal by date. */
    private String latexFooterByDateFilename = AJPropertyConstants.LATEX_FOOTER_BY_DATE_FILENAME
	    .getValue();

    // LATEX REPORT BY TARGET
    /**
     * The name of the folder containing the latex observation files by target
     * (observation output folder).
     */
    private String latexReportsFolderByTarget = AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_TARGET
	    .getValue();

    /** The name of the main Latex file sorted by target. */
    private String latexReportByTargetFilename = AJPropertyConstants.LATEX_REPORT_BY_TARGET_FILENAME
	    .getValue();

    /** The Latex header with path for astrojournal by target. */
    private String latexHeaderByTargetFilename = AJPropertyConstants.LATEX_HEADER_BY_TARGET_FILENAME
	    .getValue();

    /** The Latex footer with path for astrojournal by target. */
    private String latexFooterByTargetFilename = AJPropertyConstants.LATEX_FOOTER_BY_TARGET_FILENAME
	    .getValue();

    // LATEX REPORT BY CONSTELLATION
    /**
     * The name of the folder containing the latex observation files by
     * constellation (observation output folder).
     */
    private String latexReportsFolderByConstellation = AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
	    .getValue();

    /** The name of the main Latex file sorted by constellation. */
    private String latexReportByConstellationFilename = AJPropertyConstants.LATEX_REPORT_BY_CONSTELLATION_FILENAME
	    .getValue();

    /** The Latex header with path for astrojournal by constellation. */
    private String latexHeaderByConstellationFilename = AJPropertyConstants.LATEX_HEADER_BY_CONSTELLATION_FILENAME
	    .getValue();

    /** The Latex footer with path for astrojournal by constellation. */
    private String latexFooterByConstellationFilename = AJPropertyConstants.LATEX_FOOTER_BY_CONSTELLATION_FILENAME
	    .getValue();

    // SGL REPORT BY DATE
    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    private String sglReportsFolderByDate = AJPropertyConstants.SGL_REPORTS_FOLDER_BY_DATE
	    .getValue();

    /** The name of the SGL main file sorted by date. */
    private String sglReportByDateFilename = AJPropertyConstants.SGL_REPORT_BY_DATE_FILENAME
	    .getValue();

    /**
     * Default constructor.
     */
    public AJConfiguration() {
	init();
    }

    /**
     * Save the properties to a XML file.
     */
    void saveProperties() {
	try {
	    PropertiesManager.storeToXML(applicationProperties, configFile
		    .getAbsolutePath(),
		    AppMetaInfo.USER_CONFIGURATION_PROPERTIES_FILE_COMMENT
			    .getInfo());
	} catch (IOException e) {
	    log.error(
		    "Errors when writing the file "
			    + configFile.getAbsolutePath(), e);
	}
    }

    @Override
    public void loadSystemProperties() {
	log.debug("Loading system properties");
	applicationProperties = PropertiesManager
		.updateWithMatchSystemProperties(applicationProperties);
	log.debug("System properties loaded");
	validateProperties();
    }

    @Override
    public ResourceBundle getResourceBundle() {
	return localeBundle;
    }

    @Override
    public String getProperty(String key) {
	// Doing so, we don't need a set method for each property, saving code
	// and time.
	return applicationProperties.getProperty(key);
    }

    @Override
    public ConfigurationUtils getConfigurationUtils() {
	return new AJConfigurationUtils();
    }

    /**
     * Initialise the properties for this application by reading an
     * application-level and then an user-level xml configuration file
     * containing java properties. After these files are loaded, System
     * properties are checked for updates. Properties passed as System
     * properties are not saved in the user configuration file at this stage.
     */
    private void init() {
	configFile = AJConfigurationUtils.setupUserConfigurationFile();

	log.debug("Loading application configuration file: "
		+ AppMetaInfo.DEFAULT_CONFIGURATION_PROPERTIES_FILE_NAME
			.getInfo());
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
	    applicationProperties = PropertiesManager.loadFromXML(temp
		    .getAbsolutePath());
	    String[] keys = applicationProperties.keySet().toArray(
		    new String[0]);
	    log.debug("If the default configuration file is corrupted, expect to "
		    + "see inconsistencies in the list below.");
	    log.debug("List of Properties imported from : "
		    + temp.getAbsolutePath());
	    for (String s : keys) {
		log.debug(s);
	    }
	    // Adjust the files location as this information is not known a
	    // priori (we don't know the user.home!)
	    applicationProperties.setProperty(
		    AJPropertyConstants.FILES_LOCATION.getKey(),
		    System.getProperty("user.home")
			    + File.separator
			    + applicationProperties
				    .get(AJPropertyConstants.FILES_LOCATION
					    .getKey()));

	    if (!validateProperties()) {
		log.error("It seems that the default configuration file is corrupted. "
			+ "The application can continue, but this should be reported "
			+ "as an issue.");
		log.info("Attempting to restore the properties using the default "
			+ "configuration values stored in this Java class.");
		rescueProperties();
	    }

	    log.debug("Application configuration file is loaded.");

	    // USER APPLICATION PROPERTIES: these are in the user space
	    if (configFile != null && configFile.exists()) {
		log.debug("Loading user configuration file: "
			+ configFile.getAbsolutePath());
		applicationProperties = PropertiesManager.loadFromXML(
			applicationProperties, configFile.getAbsolutePath());
		log.debug("User configuration file is loaded.");
		if (!validateProperties()) {
		    log.warn("Found inconsistencies in the user configuration file. The inconsistent fields will be re-written.");
		    saveProperties();
		    log.info("User configuration saved.");
		}
	    } else {
		// use the default
		log.info("User configuration file not found.");
		validateProperties();
		saveProperties();
		log.info("User configuration saved.");
	    }
	} catch (IOException e) {
	    // NOTE: we always have the default, as it is in the jar file
	    log.debug(e, e);
	    log.warn("Errors reading the user configuration file: "
		    + configFile.getAbsolutePath());
	    saveProperties();
	    log.info("A new configuration file was saved.");
	}

	// override property values with corresponding system property values
	// passed to the application via command line if this is the case.
	loadSystemProperties();
    }

    /**
     * This is a rescue method in case the default configuration file is
     * corrupted. First it clears the current loaded properties. Then, it
     * retrieves the property names from the class AJPropertyNames and
     * repopulate applicationProperties with entries like (key, ""). Then it
     * runs validateProperties(), for restoring the values from the data member
     * parameters.
     */
    private void rescueProperties() {
	log.debug("Starting rescue procedure");
	applicationProperties.clear();
	AJPropertyConstants[] keys = AJPropertyConstants.values();
	for (AJPropertyConstants key : keys) {
	    applicationProperties.setProperty(key.getKey(), key.getValue());
	}
	log.debug("Rescue procedure terminated.");
	validateProperties();
    }

    /**
     * Validate the loaded properties for this application.
     * 
     * @return true if the validation succeeded.
     */
    private boolean validateProperties() {
	log.debug("Validating properties");
	adjustFileSeparator();

	boolean status = true;

	// LOCALE

	// TODO solve the locale. it doesn't work.
	// try {
	// String locale = "locale/aj_"
	// + applicationProperties.getProperty(AJProperties.LOCALE);
	// String bundle = new ReadFromJar().getStringFileFromJARFile("/"
	// + locale + ".properties");
	// if (!bundle.isEmpty()) {
	// localeBundle = ResourceBundle.getBundle(locale);
	// }
	// } catch (IOException e) {
	// log.debug(e, e);
	// log.warn("The locale : "
	// + applicationProperties.getProperty(AJProperties.LOCALE)
	// + " does not exist. Using previous `locale` setting.");
	// applicationProperties.setProperty(AJProperties.LOCALE,
	// localeBundle.getLocale());
	// status = false;
	// }
	// log.debug(AJProperties.LOCALE + ":" + localeBundle.getLocale());

	// FLAGS
	status = checkFlags() && status;

	// GENERAL LOCATION
	status = checkStrings() && status;

	log.debug("Properties are validated.");
	return status;
    }

    private boolean checkFlags() {
	boolean status = true;
	String propertyValue;

	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.QUIET.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties
		    .setProperty(AJPropertyConstants.QUIET.getKey(),
			    Boolean.toString(quiet));
	} else {
	    quiet = Boolean.parseBoolean(propertyValue);
	}
	log.debug(AJPropertyConstants.QUIET + ":" + quiet);

	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.SHOW_LATEX_OUTPUT.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.SHOW_LATEX_OUTPUT.getKey(),
		    Boolean.toString(showLatexOutput));
	} else {
	    showLatexOutput = Boolean.parseBoolean(propertyValue);
	}
	log.debug(AJPropertyConstants.SHOW_LATEX_OUTPUT + ":" + showLatexOutput);

	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.SHOW_LICENSE_AT_START.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.SHOW_LICENSE_AT_START.getKey(),
		    Boolean.toString(showLicenseAtStart));
	} else {
	    showLicenseAtStart = Boolean.parseBoolean(propertyValue);
	}
	log.debug(AJPropertyConstants.SHOW_LICENSE_AT_START + ":"
		+ showLicenseAtStart);

	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.SHOW_PDFLATEX_VERSION_AT_START
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties
		    .setProperty(
			    AJPropertyConstants.SHOW_PDFLATEX_VERSION_AT_START
				    .getKey(), Boolean
				    .toString(showPDFLatexVersionAtStart));
	} else {
	    showPDFLatexVersionAtStart = Boolean.parseBoolean(propertyValue);
	}
	log.debug(AJPropertyConstants.SHOW_PDFLATEX_VERSION_AT_START + ":"
		+ showPDFLatexVersionAtStart);

	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.SHOW_CONFIGURATION_AT_START
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.SHOW_CONFIGURATION_AT_START.getKey(),
		    Boolean.toString(showConfigurationAtStart));
	} else {
	    showConfigurationAtStart = Boolean.parseBoolean(propertyValue);
	}
	log.debug(AJPropertyConstants.SHOW_CONFIGURATION_AT_START + ":"
		+ showConfigurationAtStart);
	return status;
    }

    private boolean checkStrings() {
	boolean status = true;
	String propertyValue;

	File newFilesLocation = new File(
		applicationProperties
			.getProperty(AJPropertyConstants.FILES_LOCATION
				.getKey()));
	if (newFilesLocation == null || !newFilesLocation.exists()
		|| !newFilesLocation.canWrite()) {
	    log.debug("The property "
		    + AJPropertyConstants.FILES_LOCATION
		    + ":"
		    + newFilesLocation.getAbsolutePath()
		    + " is not accessible. Using previous `files location` setting ("
		    + filesLocation.getAbsolutePath() + ").");
	    // reset the previous property
	    applicationProperties.setProperty(
		    AJPropertyConstants.FILES_LOCATION.getKey(),
		    filesLocation.getAbsolutePath());
	    status = false;
	} else {
	    filesLocation = newFilesLocation;
	}
	if (newFilesLocation.equals("")) {
	    status = false;
	}
	log.debug(AJPropertyConstants.FILES_LOCATION + ":"
		+ filesLocation.getAbsolutePath());

	// INPUT FOLDER
	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.RAW_REPORTS_FOLDER.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.RAW_REPORTS_FOLDER.getKey(),
		    rawReportsFolder);
	} else {
	    rawReportsFolder = propertyValue;
	}
	log.debug(AJPropertyConstants.RAW_REPORTS_FOLDER + ":"
		+ rawReportsFolder);

	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER.getKey(),
		    latexHeaderFooterFolder);
	} else {
	    latexHeaderFooterFolder = propertyValue;
	}
	log.debug(AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER + ":"
		+ latexHeaderFooterFolder);

	// LATEX REPORT BY DATE
	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_DATE
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_DATE.getKey(),
		    latexReportsFolderByDate);
	} else {
	    latexReportsFolderByDate = propertyValue;
	}
	log.debug(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_DATE + ":"
		+ latexReportsFolderByDate);

	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.LATEX_REPORT_BY_DATE_FILENAME
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.LATEX_REPORT_BY_DATE_FILENAME.getKey(),
		    latexReportByDateFilename);
	} else {
	    latexReportByDateFilename = propertyValue;
	}
	log.debug(AJPropertyConstants.LATEX_REPORT_BY_DATE_FILENAME + ":"
		+ latexReportByDateFilename);

	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.LATEX_HEADER_BY_DATE_FILENAME
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.LATEX_HEADER_BY_DATE_FILENAME.getKey(),
		    latexHeaderByDateFilename);
	} else {
	    latexHeaderByDateFilename = propertyValue;
	}
	log.debug(AJPropertyConstants.LATEX_HEADER_BY_DATE_FILENAME + ":"
		+ latexHeaderByDateFilename);

	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.LATEX_FOOTER_BY_DATE_FILENAME
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.LATEX_FOOTER_BY_DATE_FILENAME.getKey(),
		    latexFooterByDateFilename);
	} else {
	    latexFooterByDateFilename = propertyValue;
	}
	log.debug(AJPropertyConstants.LATEX_FOOTER_BY_DATE_FILENAME + ":"
		+ latexFooterByDateFilename);

	// LATEX REPORT BY TARGET
	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_TARGET
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties
		    .setProperty(
			    AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_TARGET
				    .getKey(), latexReportsFolderByTarget);
	} else {
	    latexReportsFolderByTarget = propertyValue;
	}
	log.debug(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_TARGET + ":"
		+ latexReportsFolderByTarget);

	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.LATEX_REPORT_BY_TARGET_FILENAME
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.LATEX_REPORT_BY_DATE_FILENAME.getKey(),
		    latexReportByTargetFilename);
	} else {
	    latexReportByTargetFilename = propertyValue;
	}
	log.debug(AJPropertyConstants.LATEX_REPORT_BY_TARGET_FILENAME + ":"
		+ latexReportByTargetFilename);

	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.LATEX_HEADER_BY_TARGET_FILENAME
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.LATEX_HEADER_BY_TARGET_FILENAME
			    .getKey(), latexHeaderByTargetFilename);
	} else {
	    latexHeaderByTargetFilename = propertyValue;
	}
	log.debug(AJPropertyConstants.LATEX_HEADER_BY_TARGET_FILENAME + ":"
		+ latexHeaderByTargetFilename);

	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.LATEX_FOOTER_BY_TARGET_FILENAME
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.LATEX_FOOTER_BY_TARGET_FILENAME
			    .getKey(), latexFooterByTargetFilename);
	} else {
	    latexFooterByTargetFilename = propertyValue;
	}
	log.debug(AJPropertyConstants.LATEX_FOOTER_BY_TARGET_FILENAME + ":"
		+ latexFooterByTargetFilename);

	// LATEX REPORT BY CONSTELLATION
	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
			    .getKey(), latexReportsFolderByConstellation);
	} else {
	    latexReportsFolderByConstellation = propertyValue;
	}
	log.debug(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
		+ ":" + latexReportsFolderByConstellation);

	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.LATEX_REPORT_BY_CONSTELLATION_FILENAME
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.LATEX_REPORT_BY_CONSTELLATION_FILENAME
			    .getKey(), latexReportByConstellationFilename);
	} else {
	    latexReportByConstellationFilename = propertyValue;
	}
	log.debug(AJPropertyConstants.LATEX_REPORT_BY_CONSTELLATION_FILENAME
		+ ":" + latexReportByConstellationFilename);

	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.LATEX_HEADER_BY_CONSTELLATION_FILENAME
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.LATEX_HEADER_BY_CONSTELLATION_FILENAME
			    .getKey(), latexHeaderByConstellationFilename);
	} else {
	    latexHeaderByConstellationFilename = propertyValue;
	}
	log.debug(AJPropertyConstants.LATEX_HEADER_BY_CONSTELLATION_FILENAME
		+ ":" + latexHeaderByConstellationFilename);

	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.LATEX_FOOTER_BY_CONSTELLATION_FILENAME
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.LATEX_FOOTER_BY_CONSTELLATION_FILENAME
			    .getKey(), latexFooterByConstellationFilename);
	} else {
	    latexFooterByConstellationFilename = propertyValue;
	}
	log.debug(AJPropertyConstants.LATEX_FOOTER_BY_CONSTELLATION_FILENAME
		+ ":" + latexFooterByConstellationFilename);

	// SGL REPORT BY DATE
	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.SGL_REPORTS_FOLDER_BY_DATE
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.SGL_REPORTS_FOLDER_BY_DATE.getKey(),
		    sglReportsFolderByDate);
	} else {
	    sglReportsFolderByDate = propertyValue;
	}
	log.debug(AJPropertyConstants.SGL_REPORTS_FOLDER_BY_DATE + ":"
		+ sglReportsFolderByDate);

	propertyValue = applicationProperties
		.getProperty(AJPropertyConstants.SGL_REPORT_BY_DATE_FILENAME
			.getKey());
	if (propertyValue == null || propertyValue.equals("")) {
	    status = false;
	    applicationProperties.setProperty(
		    AJPropertyConstants.SGL_REPORT_BY_DATE_FILENAME.getKey(),
		    sglReportByDateFilename);
	} else {
	    sglReportByDateFilename = propertyValue;
	}
	log.debug(AJPropertyConstants.SGL_REPORT_BY_DATE_FILENAME + ":"
		+ sglReportByDateFilename);

	return status;
    }

    /**
     * Adjust the file separator if needed. The file separator must be '/' as
     * this is the default file separator in LaTeX. Therefore, let's replace '\'
     * with '/'.
     */
    private void adjustFileSeparator() {
	applicationProperties.setProperty(
		AJPropertyConstants.RAW_REPORTS_FOLDER.getKey(),
		applicationProperties.getProperty(
			AJPropertyConstants.RAW_REPORTS_FOLDER.getKey())
			.replace("\\", "/"));
	applicationProperties.setProperty(
		AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER.getKey(),
		applicationProperties
			.getProperty(
				AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER
					.getKey()).replace("\\", "/"));
	applicationProperties.setProperty(
		AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_DATE.getKey(),
		applicationProperties.getProperty(
			AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_DATE
				.getKey()).replace("\\", "/"));
	applicationProperties.setProperty(
		AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_TARGET.getKey(),
		applicationProperties.getProperty(
			AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_TARGET
				.getKey()).replace("\\", "/"));
	applicationProperties
		.setProperty(
			AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
				.getKey(),
			applicationProperties
				.getProperty(
					AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
						.getKey()).replace("\\", "/"));
	applicationProperties.setProperty(
		AJPropertyConstants.SGL_REPORTS_FOLDER_BY_DATE.getKey(),
		applicationProperties
			.getProperty(
				AJPropertyConstants.SGL_REPORTS_FOLDER_BY_DATE
					.getKey()).replace("\\", "/"));
    }

}