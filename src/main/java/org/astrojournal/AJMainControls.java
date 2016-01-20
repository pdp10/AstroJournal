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
package org.astrojournal;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.AJConfig;
import org.astrojournal.configuration.AJConfigUtils;
import org.astrojournal.configuration.AJConstants;
import org.astrojournal.configuration.AJProperties;
import org.astrojournal.generator.AJGenerator;

/**
 * A generic class containing the commands running the logic of the application.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 12 Dec 2015
 */
public abstract class AJMainControls {

    private static Logger log = LogManager.getLogger(AJMainControls.class);

    /**
     * A reference to AJ Generator.
     */
    protected AJGenerator ajGenerator = new AJGenerator();

    /**
     * A reference to AJConfig
     */
    // TODO inject this as parameter.
    protected AJConfig ajConfig = AJConfig.getInstance();

    /**
     * Constructor
     */
    public AJMainControls() {
    }

    /**
     * Create the journals.
     * 
     * @return true if the observations have been exported correctly
     */
    public abstract boolean createJournal();

    /**
     * Print the license for AstroJournal.
     */
    public void showLicense() {
	log.info(AJConstants.SHORT_LICENSE);
    }

    /**
     * Print the version of pdflatex.
     * 
     * @return true if pdflatex is installed, false otherwise
     */
    public boolean showPDFLatexVersion() {
	String pdfLatexVersion = AJConfigUtils.printPDFLatexVersion();
	if (pdfLatexVersion.isEmpty()) {
	    return false;
	}
	log.info(pdfLatexVersion);
	return true;
    }

    /**
     * Print the configuration for AstroJournal.
     */
    public void showConfiguration() {
	String[] conf = AJConfigUtils.printConfiguration().split("\n");
	for (String str : conf) {
	    log.info(str);
	}
	log.info(System.getProperty("line.separator"));
    }

    /**
     * Initialise the folders.
     * 
     * @return true if the pre-processing phase succeeded.
     */
    protected boolean preProcessing() {
	log.debug("Starting pre-processing");

	// prepare the folders for AJ.
	ajConfig.prepareAJFolders();

	// Delete previous content if present
	try {
	    ajConfig.cleanAJFolder();
	} catch (IOException e) {
	    log.error(
		    ajConfig.getLocaleBundle().getString(
			    "AJ.errUnconfiguredPreferences.text"), e);
	    return false;
	}
	log.debug("Pre-processing was SUCCESSFUL");
	return true;
    }

    /**
     * Generate the journals.
     * 
     * @return true if the processing phase succeeded.
     */
    protected boolean processing() {
	log.debug("Starting processing");
	if (ajConfig.getProperty(AJProperties.QUIET).equals("false")
		&& ajConfig.getProperty(AJProperties.SHOW_LICENSE_AT_START)
			.equals("true")) {
	    showLicense();
	}
	if (ajConfig.getProperty(AJProperties.QUIET).equals("false")
		&& ajConfig.getProperty(
			AJProperties.SHOW_PDFLATEX_VERSION_AT_START).equals(
			"true")) {
	    if (!showPDFLatexVersion()) {
		return false;
	    }
	}
	if (ajConfig.getProperty(AJProperties.QUIET).equals("false")
		&& ajConfig.getProperty(
			AJProperties.SHOW_CONFIGURATION_AT_START)
			.equals("true")) {
	    showConfiguration();
	}
	if (!ajGenerator.generateJournals()) {
	    log.error(ajConfig.getLocaleBundle().getString(
		    "AJ.errJournalNotExported.text"));
	    return false;
	}
	log.debug("Processing was SUCCESSFUL");
	return true;
    }

    /**
     * Run post processing commands after the generation of the journals.
     * 
     * @return true if the post-processing phase succeeded.
     */
    protected boolean postProcessing() {
	log.debug("Starting post-processing");
	log.info("");
	log.info(ajConfig.getLocaleBundle().getString(
		"AJ.lblCreatingReports.text"));
	if (!ajGenerator.postProcessing()) {
	    log.error(ajConfig.getLocaleBundle().getString(
		    "AJ.errPDFLatex.text"));
	    return false;
	}
	log.debug("Post-processing was SUCCESSFUL");
	return true;
    }
}
