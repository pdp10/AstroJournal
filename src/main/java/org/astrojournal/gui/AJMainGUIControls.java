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
package org.astrojournal.gui;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.AJConfig;
import org.astrojournal.generator.AJGenerator;
import org.astrojournal.utilities.RunExternalCommand;

/**
 * A simple class containing the commands for AJMiniGUI.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 12 Dec 2015
 */
public class AJMainGUIControls {

    private static Logger log = LogManager.getLogger(AJMainGUIControls.class);

    /**
     * A reference to AJ Main GUI.
     */
    private AJMainGUI ajMainGUI = null;

    /**
     * Constructor
     * 
     * @param ajMainGUI
     */
    public AJMainGUIControls(AJMainGUI ajMainGUI) {
	this.ajMainGUI = ajMainGUI;
    }

    /**
     * Create the journals.
     * 
     * @return true if the observations sorted by date and by target have been
     *         exported to Latex correctly
     */
    public boolean createJournal() {

	AJConfig ajConfig = AJConfig.getInstance();

	boolean latexOutput = !ajConfig.isQuiet() && ajConfig.isLatexOutput();

	// prepare the folders for AJ.
	ajConfig.prepareAJFolders();

	// Delete previous content if present
	try {
	    ajConfig.cleanAJFolder();
	} catch (IOException e) {
	    ajMainGUI.setStatusPanelText(AJConfig.BUNDLE
		    .getString("AJ.errUnconfiguredPreferences.text"));
	    log.error(AJConfig.BUNDLE
		    .getString("AJ.errUnconfiguredPreferences.text"), e);
	    return false;
	}

	AJGenerator ajLatexGenerator = new AJGenerator();
	if (!ajLatexGenerator.generateJournals()) {
	    return false;
	}

	String path = ajConfig.getAJFilesLocation().getAbsolutePath();
	try {
	    // The pdflatex command must be called two times in order to
	    // generate the list of contents correctly.
	    String commandOutput;
	    commandOutput = RunExternalCommand.runCommand("pdflatex "
		    + AJConfig.REPORT_BY_DATE_FILENAME);
	    if (latexOutput)
		log.info(commandOutput);
	    commandOutput = RunExternalCommand.runCommand("pdflatex "
		    + AJConfig.REPORT_BY_DATE_FILENAME);
	    // if(latexOutput) log.info(commandOutput);

	    commandOutput = RunExternalCommand.runCommand("pdflatex "
		    + AJConfig.REPORT_BY_TARGET_FILENAME);
	    if (latexOutput)
		log.info(commandOutput);
	    commandOutput = RunExternalCommand.runCommand("pdflatex "
		    + AJConfig.REPORT_BY_TARGET_FILENAME);
	    // if(latexOutput) log.info(commandOutput);

	    commandOutput = RunExternalCommand.runCommand("pdflatex "
		    + AJConfig.REPORT_BY_CONSTELLATION_FILENAME);
	    if (latexOutput)
		log.info(commandOutput);
	    commandOutput = RunExternalCommand.runCommand("pdflatex "
		    + AJConfig.REPORT_BY_CONSTELLATION_FILENAME);
	    // if(latexOutput) log.info(commandOutput);

	    // Add this at the end to avoid mixing with the latex command
	    // output.
	    log.info("");
	    log.info(AJConfig.BUNDLE.getString("AJ.lblCreatedReports.text"));
	    log.info("\t"
		    + path
		    + File.separator
		    + FilenameUtils
			    .removeExtension(AJConfig.REPORT_BY_DATE_FILENAME)
		    + ".pdf");
	    log.info("\t"
		    + path
		    + File.separator
		    + FilenameUtils
			    .removeExtension(AJConfig.REPORT_BY_TARGET_FILENAME)
		    + ".pdf");
	    log.info("\t"
		    + path
		    + File.separator
		    + FilenameUtils
			    .removeExtension(AJConfig.REPORT_BY_CONSTELLATION_FILENAME)
		    + ".pdf");

	    // clean folders from LaTeX temporary, log, and output files
	    if (SystemUtils.IS_OS_WINDOWS) {
		commandOutput = RunExternalCommand
			.runCommand("cmd /c del /s *.aux *.toc *.log *.out");
	    } else {
		Runtime.getRuntime().exec(
			new String[] {
				"/bin/sh",
				"-c",
				"cd "
					+ AJConfig.getInstance()
						.getAJFilesLocation()
						.getAbsolutePath()
					+ " && rm -rf *.aux *.toc *.log *.out "
					+ " && cd -" });
	    }

	    ajMainGUI.setStatusPanelText(AJConfig.BUNDLE
		    .getString("AJ.lblCreatedReportsLong.text"));
	} catch (IOException ioe) {
	    log.error(AJConfig.BUNDLE.getString("AJ.errPDFLatex.text"), ioe);
	    ajMainGUI.setStatusPanelText(AJConfig.BUNDLE
		    .getString("AJ.errPDFLatexShort.text"));
	    return false;
	}
	return true;
    }
}
