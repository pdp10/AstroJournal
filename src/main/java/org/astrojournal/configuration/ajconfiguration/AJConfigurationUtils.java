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
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ConfigurationUtils;
import org.astrojournal.utilities.filefilters.LaTeXFilter;
import org.astrojournal.utilities.filefilters.TabSeparatedValueRawReportFilter;

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
	    configFile = new File(System.getProperty("user.home")
		    + File.separator + "."
		    + AJConstants.USER_CONFIGURATION_PROPERTIES_FILE_NAME);
	} else if (SystemUtils.IS_OS_WINDOWS) {
	    configFile = new File(System.getProperty("user.home")
		    + File.separator
		    + AJConstants.USER_CONFIGURATION_PROPERTIES_FILE_NAME);
	} else if (SystemUtils.IS_OS_UNIX) {
	    configFile = new File(System.getProperty("user.home")
		    + File.separator + "."
		    + AJConstants.USER_CONFIGURATION_PROPERTIES_FILE_NAME);
	} else {
	    configFile = new File(System.getProperty("user.home")
		    + File.separator
		    + AJConstants.USER_CONFIGURATION_PROPERTIES_FILE_NAME);
	}
	return configFile;
    }

    /**
     * Create a string containing the license for AstroJournal.
     * 
     * @return a string
     */
    @Override
    public final String printLicense() {
	return AJConstants.SHORT_LICENSE;
    }

    /**
     * Create a string containing the output of the command `pdflatex -version`.
     * 
     * @param config
     *            The configuration.
     * @return the current configuration
     */
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
		+ resourceBundle.getString("AJ.lblAJFilesLocation.text")
		+ " "
		+ config.getProperty(AJProperties.FILES_LOCATION.toString())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblInpDir.text")
		+ " "
		+ config.getProperty(AJProperties.RAW_REPORTS_FOLDER.toString())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblOutByDateDir.text")
		+ " "
		+ config.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE
			.toString())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblOutByTargetDir.text")
		+ " "
		+ config.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET
			.toString())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblOutByConstellationDir.text")
		+ " "
		+ config.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
			.toString())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblSGLOutByDateDir.text")
		+ " "
		+ config.getProperty(AJProperties.SGL_REPORTS_FOLDER_BY_DATE
			.toString())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblQuiet.text")
		+ " "
		+ config.getProperty(AJProperties.QUIET.toString())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblShowLatexOutput.text")
		+ " "
		+ config.getProperty(AJProperties.SHOW_LATEX_OUTPUT.toString())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblShowLicenseAtStart.text")
		+ " "
		+ config.getProperty(AJProperties.SHOW_LICENSE_AT_START
			.toString())
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblShowPDFLatexVersion.text")
		+ " "
		+ config.getProperty(AJProperties.SHOW_PDFLATEX_VERSION_AT_START
			.toString())
		+ "\n\t"
		+ resourceBundle
			.getString("AJ.lblShowConfigurationAtStart.text")
		+ " "
		+ config.getProperty(AJProperties.SHOW_CONFIGURATION_AT_START
			.toString()) + "\n" + "\n\n";
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
		config.getProperty(AJProperties.FILES_LOCATION.toString()));
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
	File ajHeaderFooterDir = new File(
		AJConstants.LATEX_HEADER_FOOTER_FOLDER.toString());
	ajHeaderFooterDir.mkdir();
	// Create a local folder for header_footer and copy the content from
	// the AJ folder to here
	File userHeaderFooterDir = new File(filesLocation.getAbsolutePath()
		+ File.separator
		+ AJConstants.LATEX_HEADER_FOOTER_FOLDER.toString());

	FileFilter latexFilter = new LaTeXFilter();

	// if the header footer folder does not exist, let's copy the default
	// one.
	if (!userHeaderFooterDir.exists()
		|| userHeaderFooterDir.listFiles(latexFilter).length < 1) {
	    try {
		FileUtils.copyDirectory(ajHeaderFooterDir, userHeaderFooterDir,
			true);
	    } catch (IOException e) {
		log.error(
			config.getResourceBundle().getString(
				"AJ.errCannotCopyHeaderFooterFolder.text"), e);
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
	File ajRawReportsDir = new File(
		config.getProperty(AJProperties.RAW_REPORTS_FOLDER.toString()));
	ajRawReportsDir.mkdir();
	// Create a local folder for ajRawReports and copy the content from
	// the AJ folder to here
	File userRawReportsDir = new File(
		filesLocation.getAbsolutePath()
			+ File.separator
			+ config.getProperty(AJProperties.RAW_REPORTS_FOLDER
				.toString()));

	FileFilter rawReportFilter = new TabSeparatedValueRawReportFilter();

	// if the raw reports folder does not exist, let's copy the default one.
	// This is convenient for testing.
	if (!userRawReportsDir.exists()
		|| userRawReportsDir.listFiles(rawReportFilter).length < 1) {
	    try {
		FileUtils.copyDirectory(ajRawReportsDir, userRawReportsDir,
			true);
	    } catch (IOException e) {
		log.error(
			config.getResourceBundle().getString(
				"AJ.errCannotCopyRawReportsFolder.text"), e);
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
	new File(filesLocation.getAbsolutePath()
		+ File.separator
		+ config.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE
			.toString())).mkdir();
	new File(
		filesLocation.getAbsolutePath()
			+ File.separator
			+ config.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET
				.toString())).mkdir();
	new File(
		filesLocation.getAbsolutePath()
			+ File.separator
			+ config.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
				.toString())).mkdir();
	new File(filesLocation.getAbsolutePath()
		+ File.separator
		+ config.getProperty(AJProperties.SGL_REPORTS_FOLDER_BY_DATE
			.toString())).mkdir();
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
		config.getProperty(AJProperties.FILES_LOCATION.toString()));
	if (!(filesLocation.exists() && filesLocation.canWrite())) {
	    throw new FileNotFoundException();
	}
	try {
	    FileUtils
		    .cleanDirectory(new File(
			    filesLocation.getAbsolutePath()
				    + File.separator
				    + config.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE
					    .toString())));
	    FileUtils
		    .cleanDirectory(new File(
			    filesLocation.getAbsolutePath()
				    + File.separator
				    + config.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET
					    .toString())));
	    FileUtils
		    .cleanDirectory(new File(
			    filesLocation.getAbsolutePath()
				    + File.separator
				    + config.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
					    .toString())));
	    FileUtils
		    .cleanDirectory(new File(
			    filesLocation.getAbsolutePath()
				    + File.separator
				    + config.getProperty(AJProperties.SGL_REPORTS_FOLDER_BY_DATE
					    .toString())));
	} catch (IOException e) {
	    throw e;
	}
    }

}
