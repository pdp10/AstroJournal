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
import org.astrojournal.utilities.filefilters.LaTeXFilter;
import org.astrojournal.utilities.filefilters.TabSeparatedValueRawReportFilter;

/**
 * A collection of utilities used by AJConfig.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 18 Jan 2016
 */
public class AJConfigUtils {

    /** The logger */
    private static Logger log = LogManager.getLogger(AJConfigUtils.class);

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
    public static final String printLicense() {
	return AJConstants.SHORT_LICENSE;
    }

    /**
     * Create a string containing the output of the command `pdflatex -version`.
     * 
     * @param ajConfig
     *            The configurator.
     * @return the current configuration
     */
    public static String printPDFLatexVersion(AJConfigurator ajConfig) {
	StringBuilder sb = new StringBuilder();
	String command = "pdflatex";
	String argument = "-version";
	Process p;
	try {
	    p = Runtime.getRuntime().exec(command + " " + argument);
	    // read the output messages from the command
	    BufferedReader stdInput = new BufferedReader(new InputStreamReader(
		    p.getInputStream()));
	    sb.append(ajConfig.getLocaleBundle().getString(
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
		    + ajConfig.getLocaleBundle().getString(
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
     * @param ajConfig
     *            The configurator
     * @return the current configuration
     */
    public static String printConfiguration(AJConfigurator ajConfig) {
	ResourceBundle resourceBundle = ajConfig.getLocaleBundle();
	String configuration = "AstroJournal current configuration:\n" + "\t"
		+ resourceBundle.getString("AJ.lblAJFilesLocation.text")
		+ " "
		+ ajConfig.getProperty(AJProperties.FILES_LOCATION)
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblInpDir.text")
		+ " "
		+ ajConfig.getProperty(AJProperties.RAW_REPORTS_FOLDER)
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblOutByDateDir.text")
		+ " "
		+ ajConfig
			.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE)
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblOutByTargetDir.text")
		+ " "
		+ ajConfig
			.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET)
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblOutByConstellationDir.text")
		+ " "
		+ ajConfig
			.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION)
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblSGLOutByDateDir.text")
		+ " "
		+ ajConfig.getProperty(AJProperties.SGL_REPORTS_FOLDER_BY_DATE)
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblQuiet.text")
		+ " "
		+ ajConfig.getProperty(AJProperties.QUIET)
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblShowLatexOutput.text")
		+ " "
		+ ajConfig.getProperty(AJProperties.SHOW_LATEX_OUTPUT)
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblShowLicenseAtStart.text")
		+ " "
		+ ajConfig.getProperty(AJProperties.SHOW_LICENSE_AT_START)
		+ "\n\t"
		+ resourceBundle.getString("AJ.lblShowPDFLatexVersion.text")
		+ " "
		+ ajConfig
			.getProperty(AJProperties.SHOW_PDFLATEX_VERSION_AT_START)
		+ "\n\t"
		+ resourceBundle
			.getString("AJ.lblShowConfigurationAtStart.text")
		+ " "
		+ ajConfig
			.getProperty(AJProperties.SHOW_CONFIGURATION_AT_START)
		+ "\n" + "\n\n";
	return configuration;
    }

    /**
     * Prepare input and output folders for AstroJournal if these do not exist.
     * 
     * @param ajConfig
     *            The configurator
     */
    public static void prepareAJFolders(AJConfigurator ajConfig) {
	// Create the folders if these do not exist.
	File filesLocation = new File(
		ajConfig.getProperty(AJProperties.FILES_LOCATION));
	filesLocation.mkdir();

	prepareAJHeaderFooter(ajConfig, filesLocation);

	prepareRawReportsDir(ajConfig, filesLocation);

	prepareOutputReportsDir(ajConfig, filesLocation);

    }

    /**
     * Create the folder containing headers and footers.
     * 
     * @param ajConfig
     *            The configurator
     * @param filesLocation
     */
    private static void prepareAJHeaderFooter(AJConfigurator ajConfig,
	    File filesLocation) {
	// AJ header footer folder
	File ajHeaderFooterDir = new File(
		AJConstants.LATEX_HEADER_FOOTER_FOLDER);
	ajHeaderFooterDir.mkdir();
	// Create a local folder for header_footer and copy the content from
	// the AJ folder to here
	File userHeaderFooterDir = new File(filesLocation.getAbsolutePath()
		+ File.separator + AJConstants.LATEX_HEADER_FOOTER_FOLDER);

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
			ajConfig.getLocaleBundle().getString(
				"AJ.errCannotCopyHeaderFooterFolder.text"), e);
	    }
	}
    }

    /**
     * Create the folder containing the input raw reports.
     * 
     * @param ajConfig
     *            The configurator
     * @param filesLocation
     */
    private static void prepareRawReportsDir(AJConfigurator ajConfig,
	    File filesLocation) {
	// Let's do the same for the raw_reports folder
	// AJ raw reports folder
	File ajRawReportsDir = new File(
		ajConfig.getProperty(AJProperties.RAW_REPORTS_FOLDER));
	ajRawReportsDir.mkdir();
	// Create a local folder for ajRawReports and copy the content from
	// the AJ folder to here
	File userRawReportsDir = new File(filesLocation.getAbsolutePath()
		+ File.separator
		+ ajConfig.getProperty(AJProperties.RAW_REPORTS_FOLDER));

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
			ajConfig.getLocaleBundle().getString(
				"AJ.errCannotCopyRawReportsFolder.text"), e);
	    }
	}
    }

    /**
     * Create the folder containing the output reports.
     * 
     * @param ajConfig
     *            The configurator
     * @param filesLocation
     */
    private static void prepareOutputReportsDir(AJConfigurator ajConfig,
	    File filesLocation) {
	new File(
		filesLocation.getAbsolutePath()
			+ File.separator
			+ ajConfig
				.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE))
		.mkdir();
	new File(
		filesLocation.getAbsolutePath()
			+ File.separator
			+ ajConfig
				.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET))
		.mkdir();
	new File(
		filesLocation.getAbsolutePath()
			+ File.separator
			+ ajConfig
				.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION))
		.mkdir();
	new File(filesLocation.getAbsolutePath() + File.separator
		+ ajConfig.getProperty(AJProperties.SGL_REPORTS_FOLDER_BY_DATE))
		.mkdir();
    }

    /**
     * Delete the previous output folder content if this is present.
     * 
     * @param ajConfig
     *            The configurator.
     * @throws IOException
     *             if the folder could not be cleaned.
     */
    public static void cleanAJFolder(AJConfigurator ajConfig) throws IOException {
	File filesLocation = new File(
		ajConfig.getProperty(AJProperties.FILES_LOCATION));
	if (!(filesLocation.exists() && filesLocation.canWrite())) {
	    throw new FileNotFoundException();
	}
	try {
	    FileUtils
		    .cleanDirectory(new File(
			    filesLocation.getAbsolutePath()
				    + File.separator
				    + ajConfig
					    .getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE)));
	    FileUtils
		    .cleanDirectory(new File(
			    filesLocation.getAbsolutePath()
				    + File.separator
				    + ajConfig
					    .getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET)));
	    FileUtils
		    .cleanDirectory(new File(
			    filesLocation.getAbsolutePath()
				    + File.separator
				    + ajConfig
					    .getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION)));
	    FileUtils
		    .cleanDirectory(new File(
			    filesLocation.getAbsolutePath()
				    + File.separator
				    + ajConfig
					    .getProperty(AJProperties.SGL_REPORTS_FOLDER_BY_DATE)));
	} catch (IOException e) {
	    throw e;
	}
    }
}
