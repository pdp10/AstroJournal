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
package org.astrojournal.console;

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
 * A simple class containing the commands for AJMainConsole.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 22 Dec 2015
 */
public class AJMainConsoleControls {

    private static Logger log = LogManager
	    .getLogger(AJMainConsoleControls.class);

    /**
     * Constructor
     */
    public AJMainConsoleControls() {
    }

    /**
     * Create the journals.
     * 
     * @param latexOutput
     *            true if the latex code should also be reported.
     */
    public void createJournal(boolean latexOutput) {

	// prepare the folders for AJ.
	AJConfig.getInstance().prepareAJFolders();

	// Delete previous content if present
	try {
	    AJConfig.getInstance().cleanAJFolder();
	} catch (IOException e) {
	    log.error(AJConfig.BUNDLE
		    .getString("AJ.errUnconfiguredPreferences.text"), e);
	    return;
	}

	AJGenerator ajLatexGenerator = new AJGenerator();
	ajLatexGenerator.generateJournals();

	String path = AJConfig.getInstance().getAJFilesLocation()
		.getAbsolutePath();
	try {
	    // The pdflatex command must be called two times in order to
	    // generate the list of contents correctly.
	    String commandOutput;
	    commandOutput = RunExternalCommand.runCommand("pdflatex "
		    + AJConfig.REPORT_BY_DATE_FILENAME);
	    if (latexOutput)
		log.info(commandOutput + "\n");
	    commandOutput = RunExternalCommand.runCommand("pdflatex "
		    + AJConfig.REPORT_BY_DATE_FILENAME);
	    // if(latexOutput) log.info(commandOutput + "\n");

	    commandOutput = RunExternalCommand.runCommand("pdflatex "
		    + AJConfig.REPORT_BY_TARGET_FILENAME);
	    if (latexOutput)
		log.info(commandOutput + "\n");
	    commandOutput = RunExternalCommand.runCommand("pdflatex "
		    + AJConfig.REPORT_BY_TARGET_FILENAME);
	    // if(latexOutput) log.info(commandOutput + "\n");

	    commandOutput = RunExternalCommand.runCommand("pdflatex "
		    + AJConfig.REPORT_BY_CONSTELLATION_FILENAME);
	    if (latexOutput)
		log.info(commandOutput + "\n");
	    commandOutput = RunExternalCommand.runCommand("pdflatex "
		    + AJConfig.REPORT_BY_CONSTELLATION_FILENAME);
	    // if(latexOutput) log.info(commandOutput +
	    // "\n");

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
		    + ".pdf\n");

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
					+ " && rm -rf *.aux *.toc *.log *.out"
					+ " && cd -" });
	    }

	    log.info(AJConfig.BUNDLE.getString("AJ.lblCreatedReportsLong.text"));
	} catch (IOException ioe) {
	    log.error(AJConfig.BUNDLE.getString("AJ.errPDFLatex.text"), ioe);
	}
    }
}
