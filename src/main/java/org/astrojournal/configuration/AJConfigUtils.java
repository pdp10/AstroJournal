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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		    + AJConstants.USER_PROPERTIES_FILE_NAME);
	} else if (SystemUtils.IS_OS_WINDOWS) {
	    configFile = new File(System.getProperty("user.home")
		    + File.separator + AJConstants.USER_PROPERTIES_FILE_NAME);
	} else if (SystemUtils.IS_OS_UNIX) {
	    configFile = new File(System.getProperty("user.home")
		    + File.separator + "."
		    + AJConstants.USER_PROPERTIES_FILE_NAME);
	} else {
	    configFile = new File(System.getProperty("user.home")
		    + File.separator + AJConstants.USER_PROPERTIES_FILE_NAME);
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
     * @return the current configuration
     */
    public static String printPDFLatexVersion() {
	StringBuilder sb = new StringBuilder();
	String command = "pdflatex";
	String argument = "-version";
	Process p;
	try {
	    p = Runtime.getRuntime().exec(command + " " + argument);
	    // read the output messages from the command
	    BufferedReader stdInput = new BufferedReader(new InputStreamReader(
		    p.getInputStream()));
	    sb.append(AJConfig.getInstance().getLocaleBundle()
		    .getString("AJ.lblOutputForPDFLatexVersion.text")
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
		    + AJConfig.getInstance().getLocaleBundle()
			    .getString("AJ.lblErrorForPDFLatexVersion.text")
		    + " `" + command + " " + argument + "`:\n\n");
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
     * @return the current configuration
     */
    public static String printConfiguration() {
	AJConfig ajConfig = AJConfig.getInstance();
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
}
