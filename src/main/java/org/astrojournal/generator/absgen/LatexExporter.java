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
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ajconfiguration.AJPropertyConstants;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.headfoot.LatexFooter;
import org.astrojournal.generator.headfoot.LatexHeader;
import org.astrojournal.generator.statistics.BasicStatistics;
import org.astrojournal.utilities.RunExternalCommand;

/**
 * A generic latex Exporter.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public abstract class LatexExporter extends Exporter {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(LatexExporter.class);

    /** The command to post-process the LaTeX code. */
    protected String command = "pdflatex -halt-on-error";

    /** If the LaTeX output should be printed. */
    protected boolean latexOutput = false;

    /** The LaTeX filename for storing the statistics. */
    protected String basicStatisticsFilename = "BasicStatistics.tex";

    /**
     * Default constructor.
     */
    public LatexExporter() {
	super();
    }

    @Override
    public void setConfiguration(Configuration config) {
	super.setConfiguration(config);
	setLatexOutput(Boolean.parseBoolean(config
		.getProperty(AJPropertyConstants.SHOW_LATEX_OUTPUT.getKey())));
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
    public boolean generateJournal(BasicStatistics basicStatistics) {
	LatexHeader latexHeader = new LatexHeader(filesLocation,
		headerFooterFolder, headerFilename);
	LatexFooter latexFooter = new LatexFooter(filesLocation,
		headerFooterFolder, footerFilename);
	Writer writer = null;
	try {

	    writer = new BufferedWriter(new OutputStreamWriter(
		    new FileOutputStream(filesLocation + File.separator
			    + reportFilename), "utf-8"));
	    writeLatexMain(writer, latexHeader, latexFooter, basicStatistics);

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
     * @param basicStatistics
     *            The statistics to write
     * @throws Exception
     */
    public abstract void writeLatexMain(Writer writer, LatexHeader latexHeader,
	    LatexFooter latexFooter, BasicStatistics basicStatistics)
	    throws Exception;

    /**
     * This method contains the LaTeX code for the document content.
     * 
     * @param writer
     * @param report
     * @throws IOException
     */
    public abstract void writeLatexContent(Writer writer, Report report)
	    throws IOException;

    /**
     * Write the statistics to a LaTeX file.
     * 
     * @param basicStatistics
     *            The statistics to write
     * 
     * @return true if the file was written correctly
     */
    public boolean writeLatexStatistics(BasicStatistics basicStatistics) {

	// TODO THIS CODE SHOULD BE PUT IN A SEPARATE CLASS.
	Writer writer = null;
	try {
	    writer = new BufferedWriter(new OutputStreamWriter(
		    new FileOutputStream(new File(filesLocation
			    + File.separator + reportFolder,
			    basicStatisticsFilename)), "utf-8"));

	    log.debug("Writing basic statistics for the target type.");

	    writer.write("% Basic statistics for the target type. \n");

	    // Type counts
	    writer.write("\\begin{tabular}[t]{ll} \n");
	    writer.write("\\hline \n");
	    writer.write("{\\bf Target Type } & {\\bf Count (\\#)} \\\\ \n");
	    writer.write("\\hline \n");
	    // Let's sort the elements for improving readability
	    HashMap<String, MutableInt> countType = basicStatistics
		    .getCountType();
	    String[] sortedKeys = countType.keySet().toArray(new String[0]);
	    Arrays.sort(sortedKeys);
	    for (String key : sortedKeys) {
		log.debug("Count(" + key.toUpperCase() + "): "
			+ basicStatistics.getCount(countType, key));
		writer.write(key.toUpperCase() + " & "
			+ basicStatistics.getCount(countType, key) + "\\\\ \n");
	    }
	    writer.write("\\hline \n");
	    writer.write("\\end{tabular} \n");
	    writer.write("\\vspace{1cm} \n");

	    // Location counts
	    writer.write("\\begin{tabular}[t]{ll} \n");
	    writer.write("\\hline \n");
	    writer.write("{\\bf Location } & {\\bf Count (\\#)} \\\\ \n");
	    writer.write("\\hline \n");
	    // Let's sort the elements for improving readability
	    HashMap<String, MutableInt> countLocations = basicStatistics
		    .getCountLocations();
	    sortedKeys = countLocations.keySet().toArray(new String[0]);
	    Arrays.sort(sortedKeys);
	    for (String key : sortedKeys) {
		log.debug("Count(" + key.toUpperCase() + "): "
			+ basicStatistics.getCount(countLocations, key));
		writer.write(key.toUpperCase() + " & "
			+ basicStatistics.getCount(countLocations, key)
			+ "\\\\ \n");
	    }
	    writer.write("\\hline \n");
	    writer.write("\\end{tabular} \n");
	    writer.write("\\vspace{1cm} \n");

	    // Reports per year
	    writer.write("\\begin{tabular}[t]{ll} \n");
	    writer.write("\\hline \n");
	    writer.write("{\\bf Target Type } & {\\bf Count (\\#)} \\\\ \n");
	    writer.write("\\hline \n");
	    // Let's sort the elements for improving readability
	    HashMap<String, MutableInt> countReportsYear = basicStatistics
		    .getCountReportsYear();
	    sortedKeys = countReportsYear.keySet().toArray(new String[0]);
	    Arrays.sort(sortedKeys);
	    for (String key : sortedKeys) {
		log.debug("Count(" + key.toUpperCase() + "): "
			+ basicStatistics.getCount(countReportsYear, key));
		writer.write(key.toUpperCase() + " & "
			+ basicStatistics.getCount(countReportsYear, key)
			+ "\\\\ \n");
	    }
	    writer.write("\\hline \n");
	    writer.write("\\end{tabular} \n");
	    writer.write("\\vspace{1cm} \n");

	    writer.write("\\clearpage \n\n");

	} catch (IOException ex) {
	    log.error("Error when opening the file " + filesLocation
		    + File.separator + reportFolder + File.separator
		    + basicStatisticsFilename);
	    log.debug("Error when opening the file " + filesLocation
		    + File.separator + reportFolder + File.separator
		    + basicStatisticsFilename, ex);
	    return false;
	} catch (Exception ex) {
	    log.debug(ex);
	    log.error(ex, ex);
	    return false;
	} finally {
	    try {
		if (writer != null)
		    writer.close();
	    } catch (Exception ex) {
		log.debug(ex);
		log.error(ex, ex);
		return false;
	    }
	}
	return true;
    }

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
