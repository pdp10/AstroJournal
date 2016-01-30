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
import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ConfigurationUtils;
import org.astrojournal.configuration.ajconfiguration.AJConfigurationUtils;
import org.astrojournal.configuration.ajconfiguration.AJMetaInfo;
import org.astrojournal.configuration.ajconfiguration.AJPropertyConstants;
import org.astrojournal.generator.Generator;

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

    /** The application configuration. */
    protected Configuration config;

    /** The application configuration utilities. */
    private ConfigurationUtils configUtils;

    /**
     * Constructor
     * 
     * @param config
     *            The configuration
     */
    public AJMainControls(Configuration config) {
	this.config = config;
	configUtils = this.config.getConfigurationUtils();
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
	log.info(AJMetaInfo.SHORT_LICENSE.getInfo());
    }

    /**
     * Print the version of pdflatex.
     * 
     * @return true if pdflatex is installed, false otherwise
     */
    public boolean showPDFLatexVersion() {
	String pdfLatexVersion = "";
	if (configUtils instanceof AJConfigurationUtils) {
	    pdfLatexVersion = ((AJConfigurationUtils) configUtils)
		    .printPDFLatexVersion(config);
	}
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
	String[] conf = configUtils.printConfiguration(config).split("\n");
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
	configUtils.prepareFolders(config);

	// Delete previous content if present
	try {
	    configUtils.cleanFolder(config);
	} catch (IOException e) {
	    log.error(
		    config.getResourceBundle().getString(
			    "AJ.errUnconfiguredPreferences.text"), e);
	    return false;
	}
	log.debug("Pre-processing was SUCCESSFUL");
	return true;
    }

    /**
     * Generate the journals.
     * 
     * @param generator
     *            the generator
     * @return true if the processing phase succeeded.
     */
    protected boolean processing(Generator generator) {
	log.debug("Starting processing");
	if (config.getProperty(AJPropertyConstants.QUIET.getKey()).equals(
		"false")
		&& config.getProperty(
			AJPropertyConstants.SHOW_LICENSE_AT_START.getKey())
			.equals("true")) {
	    showLicense();
	}
	if (config.getProperty(AJPropertyConstants.QUIET.getKey()).equals(
		"false")
		&& config.getProperty(
			AJPropertyConstants.SHOW_PDFLATEX_VERSION_AT_START
				.getKey()).equals("true")) {
	    if (!showPDFLatexVersion()) {
		return false;
	    }
	}
	if (config.getProperty(AJPropertyConstants.QUIET.getKey()).equals(
		"false")
		&& config.getProperty(
			AJPropertyConstants.SHOW_CONFIGURATION_AT_START
				.getKey()).equals("true")) {
	    showConfiguration();
	}
	if (!generator.generateJournals()) {
	    log.error(config.getResourceBundle().getString(
		    "AJ.errJournalNotExported.text"));
	    return false;
	}
	log.debug("Processing was SUCCESSFUL");
	return true;
    }

    /**
     * Run post processing commands after the generation of the journals.
     * 
     * @param generator
     *            the generator
     * @return true if the post-processing phase succeeded.
     */
    protected boolean postProcessing(Generator generator) {
	log.debug("Starting post-processing");
	log.info("");
	log.info(config.getResourceBundle().getString(
		"AJ.lblCreatingReports.text"));
	if (!generator.postProcessing()) {
	    log.error(config.getResourceBundle().getString(
		    "AJ.errPDFLatex.text"));
	    return false;
	}
	log.debug("Post-processing was SUCCESSFUL");
	return true;
    }
}
