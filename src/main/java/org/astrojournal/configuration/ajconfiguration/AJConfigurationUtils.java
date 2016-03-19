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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ConfigurationUtils;
import org.astrojournal.utilities.filefilters.LaTeXFilter;
import org.astrojournal.utilities.filefilters.TSVRawReportFilter;

/**
 * A collection of utilities used by AJConfiguration.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 18 Jan 2016
 */
public class AJConfigurationUtils implements ConfigurationUtils {

    /** The logger */
    private static Logger log = LogManager
	    .getLogger(AJConfigurationUtils.class);

    /**
     * Setup the configuration file
     * 
     * @return return the user configuration file reference to the actual file
     *         in the file system.
     */
    // Mark this as final as nobody should be able to change the user
    // configuration file by overriding this method.
    public static final File setupUserConfigurationFile() {
	File configFile = null;
	if (SystemUtils.IS_OS_MAC_OSX) {
	    configFile = new File(
		    System.getProperty("user.home")
			    + File.separator
			    + "."
			    + AJMetaInfo.USER_CONFIGURATION_PROPERTIES_FILE_NAME
				    .getInfo());
	} else if (SystemUtils.IS_OS_WINDOWS) {
	    configFile = new File(
		    System.getProperty("user.home")
			    + File.separator
			    + AJMetaInfo.USER_CONFIGURATION_PROPERTIES_FILE_NAME
				    .getInfo());
	} else if (SystemUtils.IS_OS_UNIX) {
	    configFile = new File(
		    System.getProperty("user.home")
			    + File.separator
			    + "."
			    + AJMetaInfo.USER_CONFIGURATION_PROPERTIES_FILE_NAME
				    .getInfo());
	} else {
	    configFile = new File(
		    System.getProperty("user.home")
			    + File.separator
			    + AJMetaInfo.USER_CONFIGURATION_PROPERTIES_FILE_NAME
				    .getInfo());
	}
	return configFile;
    }

    @Override
    public final String printLicense() {
	return AJMetaInfo.SHORT_LICENSE.getInfo();
    }

    @Override
    public String printPDFLatexVersion(Configuration config) {
	StringBuilder sb = new StringBuilder();
	String command = "pdflatex";
	String argument = "-version";
	Process p;
	try {
	    p = Runtime.getRuntime().exec(command + " " + argument);
	    // read the output messages from the command
	    BufferedReader stdInput = new BufferedReader(new InputStreamReader(
		    p.getInputStream()));
	    sb.append(config.getResourceBundle().getString(
		    "AJ.lblOutputForPDFLatexVersion.text")
		    + " `" + command + " " + argument + "`:\n\n");
	    String temp;
	    while ((temp = stdInput.readLine()) != null) {
		sb.append(temp).append("\n");
	    }
	    stdInput.close();
	    // read the error messages from the command
	    BufferedReader stdError = new BufferedReader(new InputStreamReader(
		    p.getErrorStream()));
	    sb.append("\n"
		    + config.getResourceBundle().getString(
			    "AJ.lblErrorForPDFLatexVersion.text") + " `"
		    + command + " " + argument + "`:\n\n");
	    while ((temp = stdError.readLine()) != null) {
		sb.append(temp).append("\n");
	    }
	    stdError.close();
	} catch (IOException e) {
	    // Don't report this exception stack trace.
	    log.fatal("The command "
		    + command
		    + " was not found. \n"
		    + command
		    + " is required for generating PDF documents from \n"
		    + "LaTeX code. \n\n"
		    + "Please install:\n"
		    + " - TeX Live (http://www.tug.org/texlive/) (GNU/Linux Users); \n"
		    + " - MikTeX (http://miktex.org/download) (Windows Users); n"
		    + " - MacTeX (https://tug.org/mactex/) (Mac OS X Users). \n\n"
		    + "See http://pdp10.github.io/AstroJournal/ for information \n"
		    + "about AstroJournal requirements.\n\n" + "Abort.");
	    return "";
	}

	return sb.toString();
    }

    /**
     * Print the current configuration.
     * 
     * @param config
     *            The configuration
     * @return the current configuration
     */
    @Override
    public String printConfiguration(Configuration config) {
	ResourceBundle resourceBundle = config.getResourceBundle();
	String configuration = "AstroJournal current configuration:\n" + "\t"
		+ resourceBundle.getString("AJ.cbxGenerator.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.GENERATOR_NAME
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblAJFilesLocation.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.FILES_LOCATION
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblInpDir.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.RAW_REPORTS_FOLDER
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblLatexHeaderFolderDir.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblOutByDateDir.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_DATE
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblOutByDateFile.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.LATEX_REPORT_BY_DATE_FILENAME
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblHeaderByDateFile.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.LATEX_HEADER_BY_DATE_FILENAME
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblFooterByDateFile.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.LATEX_FOOTER_BY_DATE_FILENAME
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblOutByTargetDir.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_TARGET
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblOutByTargetFile.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.LATEX_REPORT_BY_TARGET_FILENAME
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblHeaderByTargetFile.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.LATEX_HEADER_BY_TARGET_FILENAME
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblFooterByTargetFile.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.LATEX_FOOTER_BY_TARGET_FILENAME
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblOutByConstellationDir.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblOutByConstellationFile.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.LATEX_REPORT_BY_CONSTELLATION_FILENAME
			.getKey())
		+ "\n\t"
		+ resourceBundle
			.getString("AJ.lblHeaderByConstellationFile.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.LATEX_HEADER_BY_CONSTELLATION_FILENAME
			.getKey())
		+ "\n\t"
		+ resourceBundle
			.getString("AJ.lblFooterByConstellationFile.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.LATEX_FOOTER_BY_CONSTELLATION_FILENAME
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblSGLOutByDateDir.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.SGL_REPORTS_FOLDER_BY_DATE
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblSGLOutByDateFile.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.SGL_REPORT_BY_DATE_FILENAME
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblQuiet.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.QUIET.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblShowLatexOutput.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.SHOW_LATEX_OUTPUT
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblShowLicenseAtStart.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.SHOW_LICENSE_AT_START
			.getKey())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblShowPDFLatexVersion.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.SHOW_PDFLATEX_VERSION_AT_START
			.getKey())
		+ "\n\t"
		+ resourceBundle
			.getString("AJ.lblShowConfigurationAtStart.text")
		+ " "
		+ config.getProperty(AJPropertyConstants.SHOW_CONFIGURATION_AT_START
			.getKey()) + "\n" + "\n\n";
	return configuration;
    }

    /**
     * Prepare input and output folders for AstroJournal if these do not exist.
     * 
     * @param config
     *            The configuration
     */
    @Override
    public void prepareFolders(Configuration config) {
	// Create the folders if these do not exist.
	File filesLocation = new File(
		config.getProperty(AJPropertyConstants.FILES_LOCATION.getKey()));
	filesLocation.getParentFile().mkdirs();
	filesLocation.mkdir();

	prepareAJHeaderFooter(config, filesLocation);

	prepareRawReportsDir(config, filesLocation);

	prepareOutputReportsDir(config, filesLocation);

    }

    /**
     * Create the folder containing headers and footers.
     * 
     * @param config
     *            The configuration
     * @param filesLocation
     */
    private void prepareAJHeaderFooter(Configuration config, File filesLocation) {
	// AJ header footer folder
	File file = new File(
		config.getProperty(AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER
			.getKey()));
	if (file.getParentFile() != null) {
	    file.getParentFile().mkdirs();
	}
	file.mkdir();

	// Create a local folder for header_footer and copy the content from
	// the AJ folder to here
	File userHeaderFooterDir = new File(
		filesLocation.getAbsolutePath()
			+ File.separator
			+ config.getProperty(AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER
				.getKey()));

	FileFilter latexFilter = new LaTeXFilter();

	// if the header footer folder does not exist, let's copy the default
	// one.
	if (!userHeaderFooterDir.exists()
		|| userHeaderFooterDir.listFiles(latexFilter).length < 1) {
	    try {
		FileUtils.copyDirectory(file, userHeaderFooterDir, true);
	    } catch (IOException e) {
		log.error(config.getResourceBundle().getString(
			"AJ.errCannotPrepareHeaderFooterFolder.text"));
	    }
	}
    }

    /**
     * Create the folder containing the input raw reports.
     * 
     * @param config
     *            The configuration
     * @param filesLocation
     */
    private void prepareRawReportsDir(Configuration config, File filesLocation) {
	// Let's do the same for the raw_reports folder
	// AJ raw reports folder
	File file = new File(
		config.getProperty(AJPropertyConstants.RAW_REPORTS_FOLDER
			.getKey()));
	if (file.getParentFile() != null) {
	    file.getParentFile().mkdirs();
	}
	file.mkdir();
	// Create a local folder for ajRawReports and copy the content from
	// the AJ folder to here
	File userRawReportsDir = new File(filesLocation.getAbsolutePath()
		+ File.separator
		+ config.getProperty(AJPropertyConstants.RAW_REPORTS_FOLDER
			.getKey()));

	FileFilter rawReportFilter = new TSVRawReportFilter();

	// if the raw reports folder does not exist, let's copy the default one.
	// This is convenient for testing.
	if (!userRawReportsDir.exists()
		|| userRawReportsDir.listFiles(rawReportFilter).length < 1) {
	    try {
		FileUtils.copyDirectory(file, userRawReportsDir, true);
	    } catch (IOException e) {
		log.error(config.getResourceBundle().getString(
			"AJ.errCannotPrepareRawReportsFolder.text"));
	    }
	}
    }

    /**
     * Create the folder containing the output reports.
     * 
     * @param config
     *            The configuration
     * @param filesLocation
     */
    private void prepareOutputReportsDir(Configuration config,
	    File filesLocation) {
	File file = new File(
		filesLocation.getAbsolutePath()
			+ File.separator
			+ config.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_DATE
				.getKey()));
	if (file.getParentFile() != null) {
	    file.getParentFile().mkdirs();
	}
	file.mkdir();

	file = new File(
		filesLocation.getAbsolutePath()
			+ File.separator
			+ config.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_TARGET
				.getKey()));
	if (file.getParentFile() != null) {
	    file.getParentFile().mkdirs();
	}
	file.mkdir();

	file = new File(
		filesLocation.getAbsolutePath()
			+ File.separator
			+ config.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
				.getKey()));
	if (file.getParentFile() != null) {
	    file.getParentFile().mkdirs();
	}
	file.mkdir();

	file = new File(
		filesLocation.getAbsolutePath()
			+ File.separator
			+ config.getProperty(AJPropertyConstants.SGL_REPORTS_FOLDER_BY_DATE
				.getKey()));
	if (file.getParentFile() != null) {
	    file.getParentFile().mkdirs();
	}
	file.mkdir();
    }

    /**
     * Delete the previous output folder content if this is present.
     * 
     * @param config
     *            The configuration.
     * @throws IOException
     *             if the folder could not be cleaned.
     */
    @Override
    public void cleanFolder(Configuration config) throws IOException {
	File filesLocation = new File(
		config.getProperty(AJPropertyConstants.FILES_LOCATION.getKey()));
	if (!(filesLocation.exists() && filesLocation.canWrite())) {
	    throw new FileNotFoundException();
	}
	try {
	    FileUtils
		    .cleanDirectory(new File(
			    filesLocation.getAbsolutePath()
				    + File.separator
				    + config.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_DATE
					    .getKey())));
	    FileUtils
		    .cleanDirectory(new File(
			    filesLocation.getAbsolutePath()
				    + File.separator
				    + config.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_TARGET
					    .getKey())));
	    FileUtils
		    .cleanDirectory(new File(
			    filesLocation.getAbsolutePath()
				    + File.separator
				    + config.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
					    .getKey())));
	    FileUtils
		    .cleanDirectory(new File(
			    filesLocation.getAbsolutePath()
				    + File.separator
				    + config.getProperty(AJPropertyConstants.SGL_REPORTS_FOLDER_BY_DATE
					    .getKey())));
	} catch (IOException e) {
	    throw e;
	}
    }

    /**
     * Print the property names.
     * 
     * @param properties
     */
    static void printNames(Properties properties) {
	String[] keys = properties.keySet().toArray(new String[0]);
	log.debug("List of Properties: ");
	for (String s : keys) {
	    log.debug(s);
	}
    }
}
