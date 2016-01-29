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
package org.astrojournal.generator.extgen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.absgen.LatexExporter;
import org.astrojournal.generator.headfoot.LatexFooter;
import org.astrojournal.generator.headfoot.LatexHeader;
import org.astrojournal.utilities.RunExternalCommand;
import org.astrojournal.utilities.filefilters.LaTeXFilter;

/**
 * Exports the observed targets by constellation to Latex code. This is an
 * extended exporter which uses ExtMetaDataCols and ExtDataCols enum types for
 * column export.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 28/05/2015
 */
public class ExtLatexExporterByConst extends LatexExporter {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(ExtLatexExporterByConst.class);

    private HashMap<String, HashSet<String>> constellations = new HashMap<String, HashSet<String>>();

    /** A comparator for sorting items */
    private Comparator<String> itemComparator = new Comparator<String>() {
	@Override
	public int compare(String a, String b) {
	    // if both are numbers
	    if (a.matches("\\d+") && b.matches("\\d+")) {
		return Integer.parseInt(a) - Integer.parseInt(b);
	    }
	    // else, compare normally.
	    return a.compareTo(b);
	}
    };

    /**
     * Default constructor.
     */
    public ExtLatexExporterByConst() {
	super();
    }

    /**
     * Generate the Latex document by constellation
     */
    @Override
    public boolean generateJournal() {
	LatexHeader latexHeader = new LatexHeader(filesLocation,
		headerFooterFolder, headerFilename);
	LatexFooter latexFooter = new LatexFooter(filesLocation,
		headerFooterFolder, footerFilename);
	Writer writerByConst = null;
	try {
	    writerByConst = new BufferedWriter(new OutputStreamWriter(
		    new FileOutputStream(filesLocation + File.separator
			    + reportFilename), "utf-8"));
	    // write the Latex Header
	    writerByConst.write(latexHeader.getHeader());

	    // write the Latex Body
	    // parse each file in the latex obs folder
	    File[] files = new File(filesLocation + File.separator
		    + reportFolder).listFiles(new LaTeXFilter());
	    if (files == null) {
		log.warn("Folder " + filesLocation + File.separator
			+ reportFolder + " not found");
		return false;
	    }
	    // sort the constellations when we parse the files
	    Arrays.sort(files);

	    String currConst = "", filename = "";
	    // If this pathname does not denote a directory, then listFiles()
	    // returns null.
	    for (File file : files) {
		filename = file.getName();
		if (file.isFile()) {
		    if (!currConst.equals(filename.substring(
			    filename.indexOf("_") + 1, filename.indexOf(".")))) {
			currConst = filename.substring(
				filename.indexOf("_") + 1,
				filename.indexOf("."));
			writerByConst.write("\\section{" + currConst + "}\n");
		    }
		    // include the file removing the extension .tex
		    writerByConst.write("\\input{" + reportFolder + "/"
			    + filename.replaceFirst("[.][^.]+$", "") + "}\n");
		    // writerByConst.write("\\clearpage \n");
		}
	    }

	    // write the Latex Footer
	    writerByConst.write(latexFooter.getFooter());

	} catch (IOException ex) {
	    log.error("Error when opening the file " + filesLocation
		    + File.separator + reportFilename, ex);
	    return false;
	} catch (Exception ex) {
	    log.error(ex, ex);
	    return false;
	} finally {
	    try {
		if (writerByConst != null)
		    writerByConst.close();
	    } catch (Exception ex) {
		log.error(ex, ex);
		return false;
	    }
	}
	return true;
    }

    @Override
    public boolean exportReports(List<Report> reports) {
	if (resourceBundle != null) {
	    log.info("");
	    log.info("Exporting observations by constellation:");
	}
	boolean result = true;
	if (constellations.size() == 0) {
	    organiseTargetsByConstellation(reports);
	}
	String[] keys = constellations.keySet().toArray(new String[0]);
	for (int i = 0; i < keys.length; i++) {
	    Writer list = null;
	    String filenameOut = keys[i];
	    try {
		list = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(new File(filesLocation
				+ File.separator + reportFolder, "const_"
				+ filenameOut + ".tex")), "utf-8"));
		String[] targets = constellations.get(keys[i]).toArray(
			new String[0]);
		// sort the targets here, before writing them in the file
		Arrays.sort(targets, itemComparator);
		StringBuilder listOfTargets = new StringBuilder();
		for (int j = 0; j < targets.length; j++) {
		    if (j == targets.length - 1)
			listOfTargets.append(targets[j]);
		    else
			listOfTargets.append(targets[j] + ", ");
		}
		list.write(listOfTargets.toString() + "\n\n");
		if (resourceBundle != null) {
		    log.info("\tExported constellation " + filenameOut);
		}
	    } catch (IOException ex) {
		log.error("Error when opening the file " + filesLocation
			+ File.separator + filenameOut, ex);
		result = false;
	    } catch (Exception ex) {
		log.error(ex, ex);
		result = false;
	    } finally {
		try {
		    if (list != null)
			list.close();
		} catch (Exception ex) {
		    log.error(ex, ex);
		    result = false;
		}
	    }
	}
	return result;
    }

    /**
     * Organise the targets by constellation in a suitable data structure.
     * 
     * @param reports
     *            the list of observations to exportObservation
     */
    private void organiseTargetsByConstellation(List<Report> reports) {
	Report report = null;
	int nReports = reports.size();
	for (int i = 0; i < nReports; i++) {
	    report = reports.get(i);
	    List<String[]> targets = report.getAllData();
	    for (int j = 0; j < targets.size(); j++) {
		String[] targetEntry = targets.get(j);
		// skip solar system targets. We only consider DSOs.
		if (targetEntry[ExtDataCols.TYPE_NAME.ordinal()].toLowerCase()
			.equals("planet")
			|| targetEntry[ExtDataCols.TARGET_NAME.ordinal()]
				.toLowerCase().equals("sun")
			|| targetEntry[ExtDataCols.TARGET_NAME.ordinal()]
				.toLowerCase().equals("moon")
			|| targetEntry[ExtDataCols.TARGET_NAME.ordinal()]
				.toLowerCase().equals("milky way")) {
		    continue;
		}
		if (!constellations
			.containsKey(targetEntry[ExtDataCols.CONSTELLATION_NAME
				.ordinal()])) {
		    constellations.put(
			    targetEntry[ExtDataCols.CONSTELLATION_NAME.ordinal()],
			    new HashSet<String>());
		}
		log.debug(targetEntry[ExtDataCols.CONSTELLATION_NAME.ordinal()]
			+ " " + targetEntry[ExtDataCols.TARGET_NAME.ordinal()]
			+ " (" + targetEntry[ExtDataCols.TYPE_NAME.ordinal()]
			+ ")");
		constellations.get(
			targetEntry[ExtDataCols.CONSTELLATION_NAME.ordinal()])
			.add(targetEntry[ExtDataCols.TARGET_NAME.ordinal()] + " ("
				+ targetEntry[ExtDataCols.TYPE_NAME.ordinal()]
				+ ")");
	    }
	}
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
	    log.info("\t" + filesLocation + File.separator
		    + FilenameUtils.removeExtension(reportFilename) + ".pdf");
	}
	cleanPDFLatexOutput();
    }
}
