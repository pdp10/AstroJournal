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
package org.astrojournal.gui;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
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
     * @param latexOutput
     *            true if the latex code should also be reported.
     */
    public void createJournal(boolean latexOutput) {

	// Delete previous content if present
	try {
	    AJConfig.getInstance().cleanAJFolder();
	} catch (IOException e) {
	    ajMainGUI.setStatusPanelText(AJConfig.BUNDLE
		    .getString("AJ.errUnconfiguredPreferences.text"));
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
	    commandOutput = RunExternalCommand
		    .runCommand("pdflatex " + AJConfig.REPORT_BY_DATE_FILENAME);
	    if (latexOutput)
		ajMainGUI.appendTextToTextArea(commandOutput + "\n");
	    commandOutput = RunExternalCommand
		    .runCommand("pdflatex " + AJConfig.REPORT_BY_DATE_FILENAME);
	    // if(latexOutput) ajMiniGUI.appendText(commandOutput + "\n");

	    commandOutput = RunExternalCommand.runCommand(
		    "pdflatex " + AJConfig.REPORT_BY_TARGET_FILENAME);
	    if (latexOutput)
		ajMainGUI.appendTextToTextArea(commandOutput + "\n");
	    commandOutput = RunExternalCommand.runCommand(
		    "pdflatex " + AJConfig.REPORT_BY_TARGET_FILENAME);
	    // if(latexOutput) ajMiniGUI.appendText(commandOutput + "\n");

	    commandOutput = RunExternalCommand.runCommand(
		    "pdflatex " + AJConfig.REPORT_BY_CONSTELLATION_FILENAME);
	    if (latexOutput)
		ajMainGUI.appendTextToTextArea(commandOutput + "\n");
	    commandOutput = RunExternalCommand.runCommand(
		    "pdflatex " + AJConfig.REPORT_BY_CONSTELLATION_FILENAME);
		    // if(latexOutput) ajMiniGUI.appendText(commandOutput +
		    // "\n");

	    // Add this at the end to avoid mixing with the latex command
	    // output.
	    ajMainGUI.appendTextToTextArea(
		    AJConfig.BUNDLE.getString("AJ.lblCreatedReports.text")
			    + " \n");
	    ajMainGUI
		    .appendTextToTextArea("\t" + path + File.separator
			    + FilenameUtils.removeExtension(
				    AJConfig.REPORT_BY_DATE_FILENAME)
		    + ".pdf\n");
	    ajMainGUI
		    .appendTextToTextArea("\t" + path + File.separator
			    + FilenameUtils.removeExtension(
				    AJConfig.REPORT_BY_TARGET_FILENAME)
		    + ".pdf\n");
	    ajMainGUI.appendTextToTextArea("\t" + path + File.separator
		    + FilenameUtils.removeExtension(
			    AJConfig.REPORT_BY_CONSTELLATION_FILENAME)
		    + ".pdf\n");

	    commandOutput = RunExternalCommand
		    .runCommand("rm -rf *.aux *.toc *.log *.out");
	    // if(latexOutput) ajMiniGUI.appendText(commandOutput + "\n");

	    ajMainGUI.setStatusPanelText(
		    AJConfig.BUNDLE.getString("AJ.lblCreatedReportsLong.text"));
	} catch (IOException ioe) {
	    System.err
		    .println(AJConfig.BUNDLE.getString("AJ.errPDFLatex.text"));
	    ajMainGUI.setStatusPanelText(
		    AJConfig.BUNDLE.getString("AJ.errPDFLatex.text"));
	}
    }

}
