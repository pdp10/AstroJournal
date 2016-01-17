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
package org.astrojournal.generator.ajexporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.AJConfig;
import org.astrojournal.generator.headerfooter.AJLatexFooter;
import org.astrojournal.generator.headerfooter.AJLatexHeader;
import org.astrojournal.generator.observation.AJObservation;
import org.astrojournal.generator.observation.AJObservationItem;
import org.astrojournal.utilities.RunExternalCommand;

/**
 * Exports an AstroJournal observation to Latex code.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 28/05/2015
 */
public class AJLatexExporterByDate extends AJLatexExporter {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(AJLatexExporterByDate.class);

    /**
     * Default constructor
     */
    public AJLatexExporterByDate() {
	super();
    }

    /**
     * Constructor
     * 
     * @param ajFilesLocation
     */
    public AJLatexExporterByDate(File ajFilesLocation) {
	super(ajFilesLocation);
    }

    /**
     * Generate the Latex document sorted by date
     * 
     * @param observations
     *            the list of observations to exportObservation
     * @return true if the observations sorted by date have been exported to
     *         Latex correctly
     */
    @Override
    public boolean generateJournal(ArrayList<AJObservation> observations) {
	// export the imported observation by date to Latex
	log.info("");
	log.info("Exporting observations by date:");
	boolean resultByDate = exportObservations(observations, AJConfig
		.getInstance().getLatexReportsFolderByDate());
	generateJournal(AJConfig.getInstance().getLatexReportsFolderByDate(),
		AJConfig.HEADER_BY_DATE_FILENAME,
		AJConfig.REPORT_BY_DATE_FILENAME,
		AJConfig.FOOTER_BY_DATE_FILENAME);
	return resultByDate;
    }

    /**
     * Generate the Latex document sorting the observation by decreasing date
     * 
     * @param latexReportsFolderByDate
     *            the directory containing the single observations by date in
     *            latex format (output)
     * @param latexHeaderByDate
     *            the latex header code (by date)
     * @param latexMainByDate
     *            the latex main body code (by date)
     * @param latexFooterByDate
     *            the latex footer code (by date)
     */
    @Override
    public void generateJournal(String latexReportsFolderByDate,
	    String latexHeaderByDate, String latexMainByDate,
	    String latexFooterByDate) {
	AJLatexHeader ajLatexHeaderByDate = new AJLatexHeader(
		ajFilesLocation.getAbsolutePath(), latexHeaderByDate);
	AJLatexFooter ajLatexFooterByDate = new AJLatexFooter(
		ajFilesLocation.getAbsolutePath(), latexFooterByDate);
	Writer writerByDate = null;
	try {
	    writerByDate = new BufferedWriter(new OutputStreamWriter(
		    new FileOutputStream(ajFilesLocation.getAbsolutePath()
			    + File.separator + latexMainByDate), "utf-8"));
	    // write the Latex Header
	    writerByDate.write(ajLatexHeaderByDate.getHeader());

	    // write the Latex Body
	    // Write the observation reports
	    writerByDate.write("\\section{Observation reports}\n");
	    writerByDate.write("\\vspace{4 mm}\n");
	    writerByDate.write("\\hspace{4 mm}\n");
	    // parse each file in the latex obs folder (sorted by observation
	    // increasing)
	    File[] files = new File(ajFilesLocation.getAbsolutePath()
		    + File.separator + latexReportsFolderByDate).listFiles();
	    if (files == null) {
		log.warn("Folder " + ajFilesLocation.getAbsolutePath()
			+ File.separator + latexReportsFolderByDate
			+ " not found");
		return;
	    }
	    Arrays.sort(files, Collections.reverseOrder());
	    // If this pathname does not denote a directory, then listFiles()
	    // returns null.
	    for (File file : files) {
		if (file.isFile() && file.getName().endsWith(".tex")) {
		    // include the file removing the extension .tex
		    writerByDate.write("\\input{" + latexReportsFolderByDate
			    + "/"
			    + file.getName().replaceFirst("[.][^.]+$", "")
			    + "}\n");
		    writerByDate.write("\\clearpage \n");
		}
	    }

	    // write the Latex Footer
	    writerByDate.write(ajLatexFooterByDate.getFooter());

	} catch (IOException ex) {
	    log.warn(
		    "Error when opening the file "
			    + ajFilesLocation.getAbsolutePath()
			    + File.separator + latexMainByDate, ex);
	} catch (Exception ex) {
	    log.error(ex, ex);
	} finally {
	    try {
		if (writerByDate != null)
		    writerByDate.close();
	    } catch (Exception ex) {
		log.error(ex, ex);
	    }
	}
    }

    /**
     * Exports an observation record to Latex
     * 
     * @param observations
     *            the list of observations to exportObservation
     * @param latexReportsByDateFolder
     *            the folder to write the observation in.
     * @return true if the observations are exported
     */
    @Override
    public boolean exportObservations(ArrayList<AJObservation> observations,
	    String latexReportsByDateFolder) {

	AJObservation obs = null;
	int nObservations = observations.size();
	boolean result = true;

	for (int i = 0; i < nObservations; i++) {
	    obs = observations.get(i);

	    Writer table = null;

	    String filenameOut = obs.getDate();
	    filenameOut = filenameOut.substring(6, 10)
		    + filenameOut.substring(3, 5) + filenameOut.substring(0, 2);
	    // Add an additional char if this is present. This is the case in
	    // which
	    // more than one observation per day is done.
	    if (obs.getDate().length() == 11) {
		filenameOut = filenameOut + obs.getDate().charAt(10);
	    }

	    ArrayList<AJObservationItem> observationItems = obs
		    .getObservationItems();
	    try {
		table = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(new File(
				ajFilesLocation.getAbsolutePath()
					+ File.separator
					+ latexReportsByDateFolder, "obs"
					+ filenameOut + ".tex")), "utf-8"));

		table.write("% General observation data\n");
		table.write("\\begin{tabular}{ p{0.7in} p{1.2in} p{1.1in} p{5.7in}}\n");
		table.write("{\\bf " + AJObservation.DATE_NAME + ":} & "
			+ obs.getDate() + " & {\\bf "
			+ AJObservation.TEMPERATURE_NAME + ":} & "
			+ obs.getTemperature() + " \\\\ \n");
		table.write("{\\bf " + AJObservation.TIME_NAME + ":} & "
			+ obs.getTime() + " & {\\bf "
			+ AJObservation.SEEING_NAME + ":} & " + obs.getSeeing()
			+ " \\\\ \n");
		table.write("{\\bf " + AJObservation.LOCATION_NAME + ":} & "
			+ obs.getLocation() + " & {\\bf "
			+ AJObservation.TRANSPARENCY_NAME + ":} & "
			+ obs.getTransparency() + " \\\\ \n");

		// Darkness requires a SQM-L sky quality meter reading. Not
		// everyone has it
		// or use it. At this stage, let's leave it as optional.
		if (!obs.getDarkness().equals("")) {
		    table.write("{\\bf " + AJObservation.ALTITUDE_NAME
			    + ":} & " + obs.getAltitude() + " & {\\bf "
			    + AJObservation.DARKNESS_NAME + ":} & "
			    + obs.getDarkness() + " \\\\ \n");
		    table.write("& & {\\bf " + AJObservation.TELESCOPES_NAME
			    + ":} & " + obs.getTelescopes() + " \\\\ \n");
		} else {
		    table.write("{\\bf " + AJObservation.ALTITUDE_NAME
			    + ":} & " + obs.getAltitude() + " & {\\bf "
			    + AJObservation.TELESCOPES_NAME + ":} & "
			    + obs.getTelescopes() + " \\\\ \n");
		}

		table.write("& & {\\bf " + AJObservation.EYEPIECES_NAME
			+ ":} & " + obs.getEyepieces() + " \\\\ \n");
		table.write("& & {\\bf " + AJObservation.FILTERS_NAME + ":} & "
			+ obs.getFilters() + " \\\\ \n");

		table.write("\\end{tabular}\n");

		table.write("% Detailed observation data\n");
		table.write("\\begin{longtable}{ p{0.7in}  p{0.3in}  p{0.6in}  p{0.9in}  p{5.8in} }\n");
		table.write("\\hline \n");
		table.write("{\\bf " + AJObservationItem.TARGET_NAME
			+ "} & {\\bf " + AJObservationItem.CONSTELLATION_NAME
			+ "} & {\\bf " + AJObservationItem.TYPE_NAME
			+ "} & {\\bf " + AJObservationItem.POWER_NAME
			+ "} & {\\bf " + AJObservationItem.NOTES_NAME
			+ "} \\\\ \n");

		table.write("\\hline \n");
		for (AJObservationItem item : observationItems) {
		    log.debug("Target " + item.getTarget());
		    table.write(item.getTarget() + " & "
			    + item.getConstellation() + " & " + item.getType()
			    + " & " + item.getPower() + " & " + item.getNotes()
			    + " \\\\ \n");
		}
		table.write("\\hline \n");
		table.write("\\end{longtable} \n");
		log.info("\tExported report " + obs.getDate() + " ("
			+ observationItems.size() + " targets)");
	    } catch (IOException ex) {
		log.error(
			"Error when opening the file "
				+ ajFilesLocation.getAbsolutePath()
				+ File.separator + filenameOut, ex);
		result = false;
	    } catch (Exception ex) {
		log.error(ex, ex);
		result = false;
	    } finally {
		try {
		    if (table != null)
			table.close();
		} catch (Exception ex) {
		    log.error(ex, ex);
		}
	    }
	}

	return result;
    }

    @Override
    public String getName() {
	return this.getClass().getName();
    }

    @Override
    public void postProcessing() throws IOException {
	AJConfig ajConfig = AJConfig.getInstance();

	// The pdflatex command must be called two times in order to
	// generate the list of contents correctly.
	String commandOutput;
	commandOutput = RunExternalCommand.runCommand(command + " "
		+ AJConfig.REPORT_BY_DATE_FILENAME);
	if (!ajConfig.isQuiet() && ajConfig.isShowLatexOutput())
	    log.info(commandOutput + "\n");
	commandOutput = RunExternalCommand.runCommand(command + " "
		+ AJConfig.REPORT_BY_DATE_FILENAME);
	// if(latexOutput) log.info(commandOutput + "\n");

	// Add this at the end to avoid mixing with the latex command
	// output.
	log.info("\t"
		+ ajConfig.getFilesLocation().getAbsolutePath()
		+ File.separator
		+ FilenameUtils
			.removeExtension(AJConfig.REPORT_BY_DATE_FILENAME)
		+ ".pdf");

	cleanPDFLatexOutput();
    }

}
