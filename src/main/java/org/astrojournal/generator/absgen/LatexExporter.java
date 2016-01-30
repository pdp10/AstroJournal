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
package org.astrojournal.generator.absgen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.headfoot.LatexFooter;
import org.astrojournal.generator.headfoot.LatexHeader;
import org.astrojournal.utilities.RunExternalCommand;

/**
 * A generic latex Exporter.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 17 Jan 2016
 */
public abstract class LatexExporter extends Exporter {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(LatexExporter.class);

    /** The command to post-process the LaTeX code. */
    protected String command = "pdflatex -halt-on-error";

    /** If the LaTeX output should be printed. */
    protected boolean latexOutput = false;

    /**
     * Default constructor.
     */
    public LatexExporter() {
	super();
    }

    /**
     * Get true if the latex output is printed.
     * 
     * @return the latexOutput
     */
    public boolean isLatexOutput() {
	return latexOutput;
    }

    /**
     * Set true to print the latex output.
     * 
     * @param latexOutput
     *            the latexOutput to set
     */
    public void setLatexOutput(boolean latexOutput) {
	this.latexOutput = latexOutput;
    }

    /**
     * Generate the LaTeX document sorting the observation by decreasing date.
     */
    @Override
    public boolean generateJournal() {
	LatexHeader latexHeader = new LatexHeader(filesLocation,
		headerFooterFolder, headerFilename);
	LatexFooter latexFooter = new LatexFooter(filesLocation,
		headerFooterFolder, footerFilename);
	Writer writer = null;
	try {

	    writer = new BufferedWriter(new OutputStreamWriter(
		    new FileOutputStream(filesLocation + File.separator
			    + reportFilename), "utf-8"));
	    writeLatexMain(writer, latexHeader, latexFooter);

	} catch (IOException ex) {
	    log.error("Error when opening the file " + filesLocation
		    + File.separator + reportFolder + File.separator
		    + reportFilename);
	    log.debug("Error when opening the file " + filesLocation
		    + File.separator + reportFolder + File.separator
		    + reportFilename, ex);
	    return false;
	} catch (Exception e) {
	    log.debug(e);
	    log.error(e, e);
	    return false;
	} finally {
	    try {
		if (writer != null)
		    writer.close();
	    } catch (Exception e) {
		log.debug(e);
		log.error(e, e);
		return false;
	    }
	}
	return true;
    }

    /**
     * This method contains the LaTeX code for the main file.
     * 
     * @param writer
     * @param latexHeader
     * @param latexFooter
     * @throws Exception
     */
    public abstract void writeLatexMain(Writer writer, LatexHeader latexHeader,
	    LatexFooter latexFooter) throws Exception;

    /**
     * This method contains the LaTeX code for the document content.
     * 
     * @param writer
     * @param report
     * @throws IOException
     */
    public abstract void writeLatexContent(Writer writer, Report report)
	    throws IOException;

    @Override
    public void postProcessing() throws IOException {
	// The pdflatex command must be called two times in order to
	// generate the list of contents correctly.
	String commandOutput;
	RunExternalCommand extCommand = new RunExternalCommand(filesLocation,
		resourceBundle);
	commandOutput = extCommand.runCommand(command + " " + reportFilename);
	if (!quiet && latexOutput && resourceBundle != null) {
	    log.info(commandOutput + "\n");
	}

	// A second execution is required for building the document index.
	commandOutput = extCommand.runCommand(command + " " + reportFilename);
	// if (!quiet && latexOutput && resourceBundle != null) {
	// log.info(commandOutput + "\n");
	// }

	// Add this at the end to avoid mixing with the latex command
	// output.
	if (resourceBundle != null) {
	    String pdfFile = filesLocation + File.separator
		    + FilenameUtils.removeExtension(reportFilename) + ".pdf";
	    if (new File(pdfFile).exists())
		log.info("\t" + pdfFile);
	    else {
		log.error("\t" + pdfFile + " FAILED");
		throw new FileNotFoundException("Error: File not created!");
	    }
	}
	cleanPDFLatexOutput();
    }

    /**
     * Clean folders from LaTeX temporary, log, and output files
     * 
     * @throws IOException
     *             if cleaning failed.
     */
    public void cleanPDFLatexOutput() throws IOException {
	if (SystemUtils.IS_OS_WINDOWS) {
	    RunExternalCommand extCommand = new RunExternalCommand(
		    filesLocation, resourceBundle);
	    extCommand.runCommand("cmd /c del /s *.aux *.toc *.log *.out");
	} else {
	    Runtime.getRuntime().exec(
		    new String[] {
			    "/bin/sh",
			    "-c",
			    "cd " + filesLocation
				    + " && rm -rf *.aux *.toc *.log *.out"
				    + " && cd -" });
	}

    }
}
