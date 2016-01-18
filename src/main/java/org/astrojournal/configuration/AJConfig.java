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

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.utilities.PropertiesManager;

/**
 * A class containing the configuration of AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 12 Dec 2015
 */
public class AJConfig {
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
    private static Logger log = LogManager.getLogger(AJConfig.class);

    /**
     * The AJConfig instance to be used. Eager initialisation for this
     * singleton.
     */
    private static AJConfig instance = new AJConfig();

    /**
     * The user configuration file reference to the real file in the file
     * system.
     */
    private File configFile = null;

    /** The bundle for internationalisation */
    private ResourceBundle localeBundle = ResourceBundle.getBundle("locale.aj",
	    new Locale("en"));

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
     * NOTE: These field MUST NOT have a file separator because Latex uses '/'
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

    /**
     * Reset AJConfig as at its initialisation. AstroJournal Java properties are
     * not scanned by this method.
     */
    @Deprecated
    public void reset() {
	localeBundle = ResourceBundle.getBundle("locale.aj", new Locale("en"));
	quiet = false;
	showLatexOutput = false;
	showLicenseAtStart = true;
	showPDFLatexVersionAtStart = true;
	showConfigurationAtStart = true;
	filesLocation = new File(System.getProperty("user.home")
		+ File.separator + "AstroJournal_files");
	rawReportsFolder = "raw_reports";
	latexReportsFolderByDate = "latex_reports_by_date";
	latexReportsFolderByTarget = "latex_reports_by_target";
	latexReportsFolderByConstellation = "latex_reports_by_constellation";
	sglReportsFolderByDate = "sgl_reports_by_date";
	// Read the configuration file
	init();
    }

    /**
     * Private constructor for creating only one instance of AJConfig.
     */
    private AJConfig() {
	// Read the application properties
	init();
    }

    /**
     * Return the singleton instance of AJConfig.
     * 
     * @return the instance of AJConfig.
     */
    public static AJConfig getInstance() {
	return instance;
    }

    /**
     * Initialise the properties for this application by reading an
     * application-level and then an user-level xml configuration file
     * containing java properties. After these files are loaded, System
     * properties are checked for updates. Properties passed as System
     * properties are not saved in the user configuration file at this stage.
     */
    private void init() {
	configFile = AJConfigUtils.setupUserConfigurationFile();

	try {
	    log.debug("Loading application configuration file: "
		    + AJConstants.APPLICATION_PROPERTIES_FILE_NAME);
	    // Default application properties: these are in resources/
	    applicationProperties = PropertiesManager
		    .loadFromXML(AJConstants.APPLICATION_PROPERTIES_FILE_NAME);
	    /*
	     * // TODO IN THE JAR FILE IT SHOULD BE THIS: ClassLoader
	     * .getSystemResource(
	     * "AJConstants.APPLICATION_PROPERTIES_FILE_NAME") .toString());
	     */
	    // Unfortunately we cannot set the filesLocation in the application
	    // properties xml file as we do not
	    // know the file system. We adjust this here:
	    applicationProperties.put(
		    AJProperties.FILES_LOCATION,
		    System.getProperty("user.home")
			    + File.separator
			    + applicationProperties
				    .get(AJProperties.FILES_LOCATION));
	    log.debug("Application configuration file is loaded.");
	    // User application properties: these are in the user space
	    if (configFile != null && configFile.exists()) {
		log.debug("Loading user configuration file: "
			+ configFile.getAbsolutePath());
		applicationProperties = PropertiesManager.loadFromXML(
			applicationProperties, configFile.getAbsolutePath());
		log.debug("User configuration file is loaded.");
		validateProperties();
	    } else {
		// use the default
		log.info("User configuration file not found.");
		saveProperties();
		log.info("User configuration saved.");
	    }
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

	// TODO THIS DOES NOT CURRENTLY WORK. newLocale is not NULL when
	// instead it does not exist.
	// TRY TO SET the configuration file to enk and it still retrieves
	// it and set it...WEIRD!
	// Check that the locale exists
	ResourceBundle newResourceBundle = null;
	Locale locale = new Locale(
		applicationProperties.getProperty(AJProperties.LOCALE));
	newResourceBundle = ResourceBundle.getBundle("locale.aj", locale);
	if (newResourceBundle != null) {
	    log.debug(AJProperties.LOCALE + ":"
		    + applicationProperties.getProperty(AJProperties.LOCALE));
	    localeBundle = newResourceBundle;
	} else {
	    log.error("The locale : "
		    + applicationProperties.getProperty(AJProperties.LOCALE)
		    + " does not exist. Using previous `locale` setting.");
	}

	quiet = Boolean.parseBoolean(applicationProperties
		.getProperty(AJProperties.QUIET));
	log.debug(AJProperties.QUIET + ":" + quiet);

	showLatexOutput = Boolean.parseBoolean(applicationProperties
		.getProperty(AJProperties.SHOW_LATEX_OUTPUT));
	log.debug(AJProperties.SHOW_LATEX_OUTPUT + ":" + showLatexOutput);

	showLicenseAtStart = Boolean.parseBoolean(applicationProperties
		.getProperty(AJProperties.SHOW_LICENSE_AT_START));
	log.debug(AJProperties.SHOW_LICENSE_AT_START + ":" + showLicenseAtStart);

	showPDFLatexVersionAtStart = Boolean.parseBoolean(applicationProperties
		.getProperty(AJProperties.SHOW_PDFLATEX_VERSION_AT_START));
	log.debug(AJProperties.SHOW_PDFLATEX_VERSION_AT_START + ":"
		+ showPDFLatexVersionAtStart);

	showConfigurationAtStart = Boolean.parseBoolean(applicationProperties
		.getProperty(AJProperties.SHOW_CONFIGURATION_AT_START));
	log.debug(AJProperties.SHOW_CONFIGURATION_AT_START + ":"
		+ showConfigurationAtStart);

	File newFilesLocation = new File(
		applicationProperties.getProperty(AJProperties.FILES_LOCATION));
	if (newFilesLocation == null || !newFilesLocation.exists()
		|| !newFilesLocation.canWrite()) {
	    log.error("The property "
		    + AJProperties.FILES_LOCATION
		    + ":"
		    + newFilesLocation.getAbsolutePath()
		    + " is not accessible. Using previous `files location` setting.");
	    applicationProperties.put(AJProperties.FILES_LOCATION,
		    filesLocation.getAbsolutePath());
	} else {
	    filesLocation = newFilesLocation;
	}

	rawReportsFolder = applicationProperties
		.getProperty(AJProperties.RAW_REPORTS_FOLDER);
	log.debug(AJProperties.RAW_REPORTS_FOLDER + ":" + rawReportsFolder);

	latexReportsFolderByDate = applicationProperties
		.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE);
	log.debug(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE + ":"
		+ latexReportsFolderByDate);

	latexReportsFolderByTarget = applicationProperties
		.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET);
	log.debug(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET + ":"
		+ latexReportsFolderByTarget);

	latexReportsFolderByConstellation = applicationProperties
		.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION);
	log.debug(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION + ":"
		+ latexReportsFolderByConstellation);

	sglReportsFolderByDate = applicationProperties
		.getProperty(AJProperties.SGL_REPORTS_FOLDER_BY_DATE);
	log.debug(AJProperties.SGL_REPORTS_FOLDER_BY_DATE + ":"
		+ sglReportsFolderByDate);

	log.debug("Properties are validated.");
    }

    /**
     * Save the properties to a XML file.
     */
    public void saveProperties() {
	try {
	    PropertiesManager.storeToXML(applicationProperties,
		    configFile.getAbsolutePath(),
		    AJConstants.USER_PROPERTIES_FILE_COMMENT);
	} catch (IOException e) {
	    System.out.println("Errors when writing the file "
		    + configFile.getAbsolutePath());
	    e.printStackTrace();
	}
    }

    /**
     * Update the application and user property values with the property values
     * defined as System properties if these are defined. A validation process
     * will occur checking that the inserted property values are consistent with
     * their meaning.
     */
    public void loadSystemProperties() {
	applicationProperties = PropertiesManager
		.updateWithMatchingSystemProperties(applicationProperties);
	validateProperties();
    }

    /**
     * Return the value for a property key.
     * 
     * @param key
     *            the property key to retrieve or null if this does not exist.
     * @return the value for the Java property key
     */
    public String getProperty(String key) {
	// Doing so, we don't need a set method for each property, saving code
	// and time.
	return applicationProperties.getProperty(key);
    }

    /**
     * Adjust the file separator if needed.
     */
    private void adjustFileSeparator() {
	// File separator must be '/' as this is the default file separator in
	// LaTeX. Therefore, let's replace eventual '\' with '/'.
	rawReportsFolder = rawReportsFolder.replace("\\", "/");
	latexReportsFolderByDate = latexReportsFolderByDate.replace("\\", "/");
	latexReportsFolderByTarget = latexReportsFolderByTarget.replace("\\",
		"/");
	latexReportsFolderByConstellation = latexReportsFolderByConstellation
		.replace("\\", "/");
	sglReportsFolderByDate = sglReportsFolderByDate.replace("\\", "/");
    }

    /**
     * Prepare input and output folders for AstroJournal if these do not exist.
     */
    public void prepareAJFolders() {
	adjustFileSeparator();
	// Create the folders if these do not exist.

	filesLocation.mkdir();

	// AJ header footer folder
	File ajHeaderFooterDir = new File(
		AJConstants.LATEX_HEADER_FOOTER_FOLDER);
	ajHeaderFooterDir.mkdir();
	// Create a local folder for header_footer and copy the content from
	// the AJ folder to here
	File userHeaderFooterDir = new File(filesLocation.getAbsolutePath()
		+ File.separator + AJConstants.LATEX_HEADER_FOOTER_FOLDER);

	FileFilter latexFilter = new FileFilter() {
	    @Override
	    public boolean accept(File pathname) {
		return pathname.getName().endsWith(".tex");
	    }
	};

	// if the header footer folder does not exist, let's copy the default
	// one.
	if (!userHeaderFooterDir.exists()
		|| userHeaderFooterDir.listFiles(latexFilter).length < 1) {
	    try {
		FileUtils.copyDirectory(ajHeaderFooterDir, userHeaderFooterDir,
			true);
	    } catch (IOException e) {
		log.error(localeBundle
			.getString("AJ.errCannotCopyHeaderFooterFolder.text"),
			e);
	    }
	}

	// Let's do the same for the raw_reports folder
	// AJ raw reports folder
	File ajRawReportsDir = new File(rawReportsFolder);
	ajRawReportsDir.mkdir();
	// Create a local folder for ajRawReports and copy the content from
	// the AJ folder to here
	File userRawReportsDir = new File(filesLocation.getAbsolutePath()
		+ File.separator + rawReportsFolder);

	FileFilter rawReportsFilter = new FileFilter() {
	    @Override
	    public boolean accept(File pathname) {
		return pathname.getName().endsWith(".csv")
			|| pathname.getName().endsWith(".tsv");
	    }
	};

	// if the raw reports folder does not exist, let's copy the default one.
	// This is convenient for testing.
	if (!userRawReportsDir.exists()
		|| userRawReportsDir.listFiles(rawReportsFilter).length < 1) {
	    try {
		FileUtils.copyDirectory(ajRawReportsDir, userRawReportsDir,
			true);
	    } catch (IOException e) {
		log.error(localeBundle
			.getString("AJ.errCannotCopyRawReportsFolder.text"), e);
	    }
	}

	new File(filesLocation.getAbsolutePath() + File.separator
		+ latexReportsFolderByDate).mkdir();
	new File(filesLocation.getAbsolutePath() + File.separator
		+ latexReportsFolderByTarget).mkdir();
	new File(filesLocation.getAbsolutePath() + File.separator
		+ latexReportsFolderByConstellation).mkdir();
	new File(filesLocation.getAbsolutePath() + File.separator
		+ sglReportsFolderByDate).mkdir();
    }

    /**
     * Delete the previous output folder content if this is present.
     * 
     * @throws IOException
     *             if the folder could not be cleaned.
     */
    public void cleanAJFolder() throws IOException {
	try {
	    if (!(filesLocation.exists() && filesLocation.canWrite())) {
		throw new FileNotFoundException();
	    }
	    FileUtils.cleanDirectory(new File(filesLocation.getAbsolutePath()
		    + File.separator + latexReportsFolderByDate));
	    FileUtils.cleanDirectory(new File(filesLocation.getAbsolutePath()
		    + File.separator + latexReportsFolderByTarget));
	    FileUtils.cleanDirectory(new File(filesLocation.getAbsolutePath()
		    + File.separator + latexReportsFolderByConstellation));
	    FileUtils.cleanDirectory(new File(filesLocation.getAbsolutePath()
		    + File.separator + sglReportsFolderByDate));
	} catch (IOException e) {
	    throw e;
	}
    }

    /**
     * @return the localeBundle
     */
    public ResourceBundle getLocaleBundle() {
	return localeBundle;
    }

    // TODO replace these with getProperty()
    /**
     * @return the quiet
     */
    public boolean isQuiet() {
	return quiet;
    }

    /**
     * @return the showLatexOutput
     */
    public boolean isShowLatexOutput() {
	return showLatexOutput;
    }

    /**
     * @return the showConfigurationAtStart
     */
    public boolean isShowConfigurationAtStart() {
	return showConfigurationAtStart;
    }

    /**
     * @return the showLicenseAtStart
     */
    public boolean isShowLicenseAtStart() {
	return showLicenseAtStart;
    }

    /**
     * @return the showPDFLatexVersionAtStart
     */
    public boolean isShowPDFLatexVersionAtStart() {
	return showPDFLatexVersionAtStart;
    }

    /**
     * Return the file containing all input and output files.
     * 
     * @return the AstroJournal Files Location.
     */
    public File getFilesLocation() {
	return filesLocation;
    }

    /**
     * @return the rawReportsFolder
     */
    public String getRawReportsFolder() {
	return rawReportsFolder;
    }

    /**
     * @return the latexReportsFolderByDate
     */
    public String getLatexReportsFolderByDate() {
	return latexReportsFolderByDate;
    }

    /**
     * @return the latexReportsFolderByTarget
     */
    public String getLatexReportsFolderByTarget() {
	return latexReportsFolderByTarget;
    }

    /**
     * @return the latexReportsFolderByConstellation
     */
    public String getLatexReportsFolderByConstellation() {
	return latexReportsFolderByConstellation;
    }

    /**
     * @return the sglReportsFolderByDate
     */
    public String getSglReportsFolderByDate() {
	return sglReportsFolderByDate;
    }

}