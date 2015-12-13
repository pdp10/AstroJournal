/*
 * This file is part of AstroJournal.
 * AstroJournal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * AstroJournal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with AstroJournal. If not, see <http://www.gnu.org/licenses/>.
 */

package org.astrojournal.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FilenameUtils;
import org.astrojournal.configuration.AJConfig;
import org.astrojournal.generator.AJGenerator;
import org.astrojournal.utilities.ConsoleOutputCapturer;

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
     * A reference to AJ mini GUI.
     */
    private AJMainGUI ajMiniGUI = null;

    /**
     * Constructor
     * 
     * @param ajMiniGUI
     */
    public AJMainGUIControls(AJMainGUI ajMiniGUI) {
	this.ajMiniGUI = ajMiniGUI;
    }

    /**
     * Create the journals.
     * 
     * @param latexOutput
     *            true if the latex code should also be reported.
     */
    public void createJournal(boolean latexOutput) {

	// Delete previous content if present
	AJConfig.getInstance().cleanAJFolder();

	AJGenerator ajLatexGenerator = new AJGenerator();

	// generate Latex code for the observation records
	ConsoleOutputCapturer outputCapturer = new ConsoleOutputCapturer();
	outputCapturer.start();
	ajLatexGenerator.generateJournals();
	ajMiniGUI.appendTextToTextArea(outputCapturer.stop() + "\n");
	try {
	    // The pdflatex command must be called two times in order to
	    // generate the list of contents correctly.
	    String commandOutput;
	    commandOutput = runCommand("pdflatex "
		    + AJConfig.getInstance().REPORT_BY_DATE_FILENAME);
	    if (latexOutput)
		ajMiniGUI.appendTextToTextArea(commandOutput + "\n");
	    commandOutput = runCommand("pdflatex "
		    + AJConfig.getInstance().REPORT_BY_DATE_FILENAME);
	    // if(latexOutput) ajMiniGUI.appendText(commandOutput + "\n");

	    commandOutput = runCommand("pdflatex "
		    + AJConfig.getInstance().REPORT_BY_TARGET_FILENAME);
	    if (latexOutput)
		ajMiniGUI.appendTextToTextArea(commandOutput + "\n");
	    commandOutput = runCommand("pdflatex "
		    + AJConfig.getInstance().REPORT_BY_TARGET_FILENAME);
	    // if(latexOutput) ajMiniGUI.appendText(commandOutput + "\n");

	    commandOutput = runCommand("pdflatex "
		    + AJConfig.getInstance().REPORT_BY_CONSTELLATION_FILENAME);
	    if (latexOutput)
		ajMiniGUI.appendTextToTextArea(commandOutput + "\n");
	    commandOutput = runCommand("pdflatex "
		    + AJConfig.getInstance().REPORT_BY_CONSTELLATION_FILENAME);
	    // if(latexOutput) ajMiniGUI.appendText(commandOutput + "\n");

	    // Add this at the end to avoid mixing with the latex command
	    // output.
	    ajMiniGUI.appendTextToTextArea("Created reports: \n");
	    ajMiniGUI
		    .appendTextToTextArea("\t"
			    + FilenameUtils.removeExtension(AJConfig
				    .getInstance().REPORT_BY_DATE_FILENAME)
			    + ".pdf\n");
	    ajMiniGUI
		    .appendTextToTextArea("\t"
			    + FilenameUtils.removeExtension(AJConfig
				    .getInstance().REPORT_BY_TARGET_FILENAME)
			    + ".pdf\n");
	    ajMiniGUI
		    .appendTextToTextArea("\t"
			    + FilenameUtils.removeExtension(AJConfig
				    .getInstance().REPORT_BY_CONSTELLATION_FILENAME)
			    + ".pdf\n");

	    commandOutput = runCommand("rm -rf *.aux *.toc *.log *.out");
	    // if(latexOutput) ajMiniGUI.appendText(commandOutput + "\n");

	    ajMiniGUI
		    .setStatusPanelText("Journals should be created! Check pdf documents.");
	} catch (IOException ioe) {
	}
    }

    /**
     * Run a command
     * 
     * @param command
     *            The command to run
     * @throws IOException
     * @return the output and output error for the executed command
     */
    public static String runCommand(String command) throws IOException {
	StringBuilder sb = new StringBuilder();
	// NOTE: for some reason Runtime.getRuntime().exec() works only if the
	// command output is captured on Windows.
	// On Linux, this does not matter, but on Windows this does not work..
	// So
	// leave it.
	Process p = Runtime.getRuntime().exec(command);
	// read the output messages from the command
	BufferedReader stdInput = new BufferedReader(new InputStreamReader(
		p.getInputStream()));
	sb.append("#######################\n\n");
	sb.append("Output for the command `" + command + "`:\n\n");
	String temp;
	while ((temp = stdInput.readLine()) != null) {
	    sb.append(temp).append("\n");
	}
	// read the error messages from the command
	BufferedReader stdError = new BufferedReader(new InputStreamReader(
		p.getErrorStream()));
	sb.append("\nErrors for the command `" + command + "`:\n\n");
	while ((temp = stdError.readLine()) != null) {
	    sb.append(temp).append("\n");
	}
	return sb.toString();
    }

}
