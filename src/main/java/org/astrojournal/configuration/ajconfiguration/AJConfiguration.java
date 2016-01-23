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
import java.util.Locale;
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
    /*
     * NOTE: To be a proper singleton and not a `global variable`, store here
     * the variables which can change over time: the actual configuration.
     * Therefore this object is always accessed via AJConfig.getInstance(). To
     * dynamically change the internal status of this singleton, one can either
     * invoke System.setProperty(AJProperty prop, value) and then call the
     * method AJConfig.getInstance().loadAJProperties() or change the
     * configuration file. This improves thread safety although a proper
     * thread-safe mechanism has not yet been implemented.
     * 
     * 
     * Global constants go in AJConstants. AJ Java properties go in
     * AJProperties.
     */

    /** The logger */
    private static Logger log = LogManager.getLogger(AJConfiguration.class);

    /**
     * The user configuration file reference to the real file in the file
     * system.
     */
    private File configFile = null;

    /** The bundle for internationalisation */
    private ResourceBundle localeBundle = ResourceBundle.getBundle("locale.aj",
	    new Locale("en", "GB"));

    /**
     * The Properties for this application.
     */
    private Properties applicationProperties;

    /** True if the application should run quietly */
    private boolean quiet = false;

    /** True if latex output should be printed. */
    private boolean showLatexOutput = false;

    /** True if the license should be shown at start. */
    private boolean showLicenseAtStart = true;

    /** True if the version of pdflatex. */
    private boolean showPDFLatexVersionAtStart = true;

    /** True if the configuration should be shown at start. */
    private boolean showConfigurationAtStart = true;

    /** The absolute path containing AstroJournal input and output folders. */
    private File filesLocation = new File(System.getProperty("user.home")
	    + File.separator + "AstroJournal_files");

    /*
     * NOTE: These fields MUST NOT have a file separator because Latex uses '/'
     * by default.
     */
    /**
     * The relative path containing the raw files (observation input folder).
     */
    private String rawReportsFolder = "raw_reports";

    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    private String latexReportsFolderByDate = "latex_reports_by_date";

    /**
     * The name of the folder containing the latex observation files by target
     * (observation output folder).
     */
    private String latexReportsFolderByTarget = "latex_reports_by_target";

    /**
     * The name of the folder containing the latex observation files by
     * constellation (observation output folder).
     */
    private String latexReportsFolderByConstellation = "latex_reports_by_constellation";

    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    private String sglReportsFolderByDate = "sgl_reports_by_date";

    // Extra properties
    /** The name of the main Latex file sorted by date. */
    private String report_by_date_filename = "astrojournal_by_date.tex";

    /** The name of the main Latex file sorted by target. */
    private String report_by_target_filename = "astrojournal_by_target.tex";

    /** The name of the main Latex file sorted by constellation. */
    private String report_by_constellation_filename = "astrojournal_by_constellation.tex";

    /** The name of the SGL main file sorted by date. */
    private String sgl_report_by_date_filename = "astrojournal_by_date_sgl.txt";

    /** The folder containing the latex header and footer. */
    private String latex_header_footer_folder = "latex_header_footer";

    // NOTE: These fields require File.separator in order to be found by Java in
    // the file system.
    /** The Latex header with path for astrojournal by date. */
    private String header_by_date_filename = latex_header_footer_folder
	    + File.separator + "header_by_date.tex";

    /** The Latex footer with path for astrojournal by date. */
    private String footer_by_date_filename = latex_header_footer_folder
	    + File.separator + "footer_by_date.tex";

    /** The Latex header with path for astrojournal by target. */
    private String header_by_target_filename = latex_header_footer_folder
	    + File.separator + "header_by_target.tex";

    /** The Latex footer with path for astrojournal by target. */
    private String footer_by_target_filename = latex_header_footer_folder
	    + File.separator + "footer_by_target.tex";

    /** The Latex header with path for astrojournal by constellation. */
    private String header_by_constellation_filename = latex_header_footer_folder
	    + File.separator + "header_by_constellation.tex";

    /** The Latex footer with path for astrojournal by constellation. */
    private String footer_by_constellation_filename = latex_header_footer_folder
	    + File.separator + "footer_by_constellation.tex";

    /**
     * Default constructor.
     */
    public AJConfiguration() {
	init();
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
		+ AppMetaInfo.DEFAULT_CONFIGURATION_PROPERTIES_FILE_NAME);
	try {

	    // DEFAULT APPLICATION PROPERTIES: these are in resources/
	    File temp = new ReadFromJar().getFileFromJARFile("aj_config_", "/"
		    + AppMetaInfo.DEFAULT_CONFIGURATION_PROPERTIES_FILE_NAME);
	    log.debug("Extracted "
		    + AppMetaInfo.DEFAULT_CONFIGURATION_PROPERTIES_FILE_NAME
		    + " from JAR and stored in " + temp.getAbsolutePath());
	    applicationProperties = PropertiesManager.loadFromXML(temp
		    .getAbsolutePath());

	    // Adjust the files location as this information is not known a
	    // priori (we don't know the user.home!)
	    applicationProperties
		    .setProperty(
			    AJProperties.FILES_LOCATION.toString(),
			    System.getProperty("user.home")
				    + File.separator
				    + applicationProperties
					    .get(AJProperties.FILES_LOCATION
						    .toString()));

	    log.debug("Application configuration file is loaded.");

	    // USER APPLICATION PROPERTIES: these are in the user space
	    if (configFile != null && configFile.exists()) {
		log.debug("Loading user configuration file: "
			+ configFile.getAbsolutePath());
		applicationProperties = PropertiesManager.loadFromXML(
			applicationProperties, configFile.getAbsolutePath());
		log.debug("User configuration file is loaded.");

	    } else {
		// use the default
		log.info("User configuration file not found.");
	    }
	    validateProperties();
	    saveProperties();
	    log.info("User configuration saved.");
	} catch (IOException e) {
	    // NOTE: we always have the default, as it is in the jar file
	    log.debug(e, e);
	    log.error("Errors reading the user configuration file: "
		    + configFile.getAbsolutePath());
	    saveProperties();
	    log.info("A new configuration file was saved.");
	}

	// override property values with corresponding system property values
	// passed to the application via command line if this is the case.
	loadSystemProperties();
    }

    /**
     * Validate the loaded properties for this application.
     */
    private void validateProperties() {
	// This is the only function knowing about the actual properties.
	// example of processing a file location

	log.debug("Validating properties");
	adjustFileSeparator();

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
	// log.error("The locale : "
	// + applicationProperties.getProperty(AJProperties.LOCALE)
	// + " does not exist. Using previous `locale` setting.");
	// applicationProperties.setProperty(AJProperties.LOCALE,
	// localeBundle.getLocale());
	// }
	// log.debug(AJProperties.LOCALE + ":" + localeBundle.getLocale());

	quiet = Boolean.parseBoolean(applicationProperties
		.getProperty(AJProperties.QUIET.toString()));
	log.debug(AJProperties.QUIET + ":" + quiet);

	showLatexOutput = Boolean.parseBoolean(applicationProperties
		.getProperty(AJProperties.SHOW_LATEX_OUTPUT.toString()));
	log.debug(AJProperties.SHOW_LATEX_OUTPUT + ":" + showLatexOutput);

	showLicenseAtStart = Boolean.parseBoolean(applicationProperties
		.getProperty(AJProperties.SHOW_LICENSE_AT_START.toString()));
	log.debug(AJProperties.SHOW_LICENSE_AT_START + ":" + showLicenseAtStart);

	showPDFLatexVersionAtStart = Boolean.parseBoolean(applicationProperties
		.getProperty(AJProperties.SHOW_PDFLATEX_VERSION_AT_START
			.toString()));
	log.debug(AJProperties.SHOW_PDFLATEX_VERSION_AT_START + ":"
		+ showPDFLatexVersionAtStart);

	showConfigurationAtStart = Boolean.parseBoolean(applicationProperties
		.getProperty(AJProperties.SHOW_CONFIGURATION_AT_START
			.toString()));
	log.debug(AJProperties.SHOW_CONFIGURATION_AT_START + ":"
		+ showConfigurationAtStart);

	File newFilesLocation = new File(
		applicationProperties.getProperty(AJProperties.FILES_LOCATION
			.toString()));
	if (newFilesLocation == null || !newFilesLocation.exists()
		|| !newFilesLocation.canWrite()) {
	    log.error("The property "
		    + AJProperties.FILES_LOCATION
		    + ":"
		    + newFilesLocation.getAbsolutePath()
		    + " is not accessible. Using previous `files location` setting.");
	    applicationProperties.setProperty(
		    AJProperties.FILES_LOCATION.toString(),
		    filesLocation.getAbsolutePath());
	} else {
	    filesLocation = newFilesLocation;
	}

	rawReportsFolder = applicationProperties
		.getProperty(AJProperties.RAW_REPORTS_FOLDER.toString());
	log.debug(AJProperties.RAW_REPORTS_FOLDER + ":" + rawReportsFolder);

	latexReportsFolderByDate = applicationProperties
		.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE
			.toString());
	log.debug(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE + ":"
		+ latexReportsFolderByDate);

	latexReportsFolderByTarget = applicationProperties
		.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET
			.toString());
	log.debug(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET + ":"
		+ latexReportsFolderByTarget);

	latexReportsFolderByConstellation = applicationProperties
		.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
			.toString());
	log.debug(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION + ":"
		+ latexReportsFolderByConstellation);

	sglReportsFolderByDate = applicationProperties
		.getProperty(AJProperties.SGL_REPORTS_FOLDER_BY_DATE.toString());
	log.debug(AJProperties.SGL_REPORTS_FOLDER_BY_DATE + ":"
		+ sglReportsFolderByDate);

	// additional properties.

	report_by_date_filename = applicationProperties
		.getProperty(AJProperties.REPORT_BY_DATE_FILENAME.toString());
	log.debug(AJProperties.REPORT_BY_DATE_FILENAME + ":"
		+ report_by_date_filename);

	report_by_target_filename = applicationProperties
		.getProperty(AJProperties.REPORT_BY_TARGET_FILENAME.toString());
	log.debug(AJProperties.REPORT_BY_TARGET_FILENAME + ":"
		+ report_by_target_filename);

	report_by_constellation_filename = applicationProperties
		.getProperty(AJProperties.REPORT_BY_CONSTELLATION_FILENAME
			.toString());
	log.debug(AJProperties.REPORT_BY_CONSTELLATION_FILENAME + ":"
		+ report_by_constellation_filename);

	sgl_report_by_date_filename = applicationProperties
		.getProperty(AJProperties.SGL_REPORT_BY_DATE_FILENAME
			.toString());
	log.debug(AJProperties.SGL_REPORT_BY_DATE_FILENAME + ":"
		+ sgl_report_by_date_filename);

	latex_header_footer_folder = applicationProperties
		.getProperty(AJProperties.LATEX_HEADER_FOOTER_FOLDER.toString());
	log.debug(AJProperties.LATEX_HEADER_FOOTER_FOLDER + ":"
		+ latex_header_footer_folder);

	header_by_date_filename = applicationProperties
		.getProperty(AJProperties.HEADER_BY_DATE_FILENAME.toString());
	log.debug(AJProperties.HEADER_BY_DATE_FILENAME + ":"
		+ header_by_date_filename);

	footer_by_date_filename = applicationProperties
		.getProperty(AJProperties.FOOTER_BY_DATE_FILENAME.toString());
	log.debug(AJProperties.FOOTER_BY_DATE_FILENAME + ":"
		+ footer_by_date_filename);

	header_by_target_filename = applicationProperties
		.getProperty(AJProperties.HEADER_BY_TARGET_FILENAME.toString());
	log.debug(AJProperties.HEADER_BY_TARGET_FILENAME + ":"
		+ header_by_target_filename);

	footer_by_target_filename = applicationProperties
		.getProperty(AJProperties.FOOTER_BY_TARGET_FILENAME.toString());
	log.debug(AJProperties.FOOTER_BY_TARGET_FILENAME + ":"
		+ footer_by_target_filename);

	header_by_constellation_filename = applicationProperties
		.getProperty(AJProperties.HEADER_BY_CONSTELLATION_FILENAME
			.toString());
	log.debug(AJProperties.HEADER_BY_CONSTELLATION_FILENAME + ":"
		+ header_by_constellation_filename);

	footer_by_constellation_filename = applicationProperties
		.getProperty(AJProperties.FOOTER_BY_CONSTELLATION_FILENAME
			.toString());
	log.debug(AJProperties.FOOTER_BY_CONSTELLATION_FILENAME + ":"
		+ footer_by_constellation_filename);

	log.debug("Properties are validated.");
    }

    /**
     * Save the properties to a XML file.
     */
    void saveProperties() {
	try {
	    PropertiesManager.storeToXML(applicationProperties, configFile
		    .getAbsolutePath(),
		    AppMetaInfo.USER_CONFIGURATION_PROPERTIES_FILE_COMMENT
			    .toString());
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
     * Adjust the file separator if needed. The file separator must be '/' as
     * this is the default file separator in LaTeX. Therefore, let's replace '\'
     * with '/'.
     */
    private void adjustFileSeparator() {
	applicationProperties.setProperty(
		AJProperties.RAW_REPORTS_FOLDER.toString(),
		applicationProperties.getProperty(
			AJProperties.RAW_REPORTS_FOLDER.toString()).replace(
			"\\", "/"));
	applicationProperties.setProperty(
		AJProperties.LATEX_REPORTS_FOLDER_BY_DATE.toString(),
		applicationProperties.getProperty(
			AJProperties.LATEX_REPORTS_FOLDER_BY_DATE.toString())
			.replace("\\", "/"));
	applicationProperties.setProperty(
		AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET.toString(),
		applicationProperties.getProperty(
			AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET.toString())
			.replace("\\", "/"));
	applicationProperties.setProperty(
		AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION.toString(),
		applicationProperties.getProperty(
			AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
				.toString()).replace("\\", "/"));
	applicationProperties.setProperty(
		AJProperties.SGL_REPORTS_FOLDER_BY_DATE.toString(),
		applicationProperties.getProperty(
			AJProperties.SGL_REPORTS_FOLDER_BY_DATE.toString())
			.replace("\\", "/"));
    }

}