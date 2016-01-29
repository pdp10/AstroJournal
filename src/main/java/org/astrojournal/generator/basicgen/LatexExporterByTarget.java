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
package org.astrojournal.generator.basicgen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
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
 * Exports an AstroJournal set of observations by target to Latex code. This is
 * a basic exporter which uses BasicMetaDataCols and BasicDataCols enum types
 * for column export.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 22/07/2015
 */
public class LatexExporterByTarget extends LatexExporter {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(LatexExporterByTarget.class);

    /** A cache of the visited targets. */
    private HashSet<String> processedTargetCache = new HashSet<String>(1000);

    /** A comparator for sorting catalogues */
    private Comparator<String> catalogueItemComparator = new Comparator<String>() {
	@Override
	public int compare(String o1, String o2) {
	    return extractInt(o1) - extractInt(o2);
	}

	int extractInt(String s) {
	    String num = s.replaceAll("\\D", "");
	    // return 0 if no digits found
	    return num.isEmpty() ? 0 : Integer.parseInt(num);
	}
    };

    /**
     * Default constructor.
     */
    public LatexExporterByTarget() {
	super();
    }

    /**
     * Generate the Latex document sorting the observation by decreasing target
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
	    // write the Latex Header
	    writer.write(latexHeader.getHeader());
	    // write the Latex Body
	    // Write the observation reports
	    // parse each file in the latex obs folder (sorted by observation
	    // increasing)
	    File[] files = new File(filesLocation + File.separator
		    + reportFolder).listFiles(new LaTeXFilter());
	    if (files == null) {
		log.warn("Folder " + filesLocation + File.separator
			+ reportFolder + " not found");
		return false;
	    }
	    sortFilesByTarget(files);
	    // If this pathname does not denote a directory, then listFiles()
	    // returns
	    // null.
	    String target = null, type = "";
	    for (File file : files) {
		target = file.getName();
		if (file.isFile()) {
		    if (target
			    .matches("^(sun|moon|mercury|venus|mars|asteroid|jupiter|saturn|uranus|neptune|pluto|comet|Sun|Moon|Mercury|Venus|Mars|Asteroid|Jupiter|Saturn|Uranus|Neptune|Pluto|Comet).*$")) {
			writeSectionName(writer, target, type, "Solar System");
		    } else if (target
			    .matches("^(milkyway|MilkyWay|MILKYWAY).*$")) {
			writeSectionName(writer, target, type, "Milky Way");
		    } else if (target.matches("^(m|M)[0-9].*$")) {
			writeSectionName(writer, target, type,
				"Messier Catalogue");
		    } else if (target.matches("^(ngc|NGC)[0-9].*$")) {
			writeSectionName(writer, target, type,
				"New General Catalogue (NGC)");
		    } else if (target.matches("^(ic|IC)[0-9].*$")) {
			writeSectionName(writer, target, type,
				"Index Catalogue (IC)");
		    } else if (target.matches("^(stock|Stock|STOCK)[0-9].*$")) {
			writeSectionName(writer, target, type,
				"Stock Catalogue");
		    } else if (target.matches("^(mel|Mel|MEL)[0-9].*$")) {
			writeSectionName(writer, target, type,
				"Melotte Catalogue");
		    } else if (target.matches("^(cr|Cr|CR)[0-9].*$")) {
			writeSectionName(writer, target, type,
				"Collider Catalogue");
		    } else if (target.matches("^(pk|PK)[0-9].*$")) {
			writeSectionName(writer, target, type,
				"Perek-Kohoutex Catalogue");
		    } else if (target.matches("^(b|B|Barnard|BARNARD)[0-9].*$")) {
			writeSectionName(writer, target, type,
				"Barnard Catalogue");
		    } else if (target
			    .matches("^(hcg|HCG|Hickson Compact Group)[0-9].*$")) {
			writeSectionName(writer, target, type,
				"Hickson Compact Group Catalogue");
		    } else if (target.matches("^(ugc|UGC)[0-9].*$")) {
			writeSectionName(writer, target, type,
				"Uppsala General Catalogue");
		    } else if (target.matches("^(Steph)[0-9].*$")) {
			writeSectionName(writer, target, type,
				"Steph Catalogue");
		    } else {
			writeSectionName(writer, target, type,
				"Stars and unclassified targets");
		    }
		    // include the file removing the extension .tex
		    writer.write("\\input{" + reportFolder + "/"
			    + target.replaceFirst("[.][^.]+$", "") + "}\n");
		    writer.write("\\vspace{4 mm}\n");
		}
	    }
	    // write the Latex Footer
	    writer.write(latexFooter.getFooter());
	} catch (IOException ex) {
	    log.warn("Error when opening the file " + filesLocation
		    + File.separator + reportFilename, ex);
	    return false;
	} catch (Exception ex) {
	    log.error(ex, ex);
	    return false;
	} finally {
	    try {
		if (writer != null)
		    writer.close();
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
	    log.info("Exporting observations by target:");
	}
	processedTargetCache.clear();
	for (int i = 0; i < reports.size(); i++) {
	    Report report = reports.get(i);
	    List<String[]> targets = report.getAllData();
	    for (int j = 0; j < targets.size(); j++) {
		String[] targetEntry = targets.get(j);
		String filenameOut = computeFileName(targetEntry);
		Writer targetWriter = null;
		try {
		    if (!processedTargetCache.contains(filenameOut)) {
			processedTargetCache.add(filenameOut);
			targetWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(
					new File(filesLocation + File.separator
						+ reportFolder, filenameOut
						+ ".tex")), "utf-8"));
			if (targetEntry[BasicDataCols.TYPE_NAME.ordinal()]
				.toLowerCase().equals("planet")
				|| targetEntry[BasicDataCols.TARGET_NAME
					.ordinal()].toLowerCase()
					.equals("moon")
				|| targetEntry[BasicDataCols.TARGET_NAME
					.ordinal()].toLowerCase().equals("sun")
				|| targetEntry[BasicDataCols.TYPE_NAME
					.ordinal()].toLowerCase().equals(
					"asteroid")
				|| targetEntry[BasicDataCols.TYPE_NAME
					.ordinal()].toLowerCase().equals(
					"comet")) {
			    targetWriter.write("\\subsection{"
				    + targetEntry[BasicDataCols.TARGET_NAME
					    .ordinal()]);
			} else if (targetEntry[BasicDataCols.TYPE_NAME
				.ordinal()].toLowerCase().equals("star")
				|| targetEntry[BasicDataCols.TYPE_NAME
					.ordinal()].toLowerCase().equals(
					"dbl star")
				|| targetEntry[BasicDataCols.TYPE_NAME
					.ordinal()].toLowerCase().equals(
					"mlt star")) {
			    targetWriter.write("\\subsection{"
				    + targetEntry[BasicDataCols.TARGET_NAME
					    .ordinal()]);
			} else if (targetEntry[BasicDataCols.TYPE_NAME
				.ordinal()].toLowerCase().equals("galaxy")
				&& targetEntry[BasicDataCols.TARGET_NAME
					.ordinal()].toLowerCase().equals(
					"milky way")) {
			    // Don't print the constellation if we are
			    // processing the milky way!
			    targetWriter.write("\\subsection{"
				    + targetEntry[BasicDataCols.TARGET_NAME
					    .ordinal()]);
			} else {
			    targetWriter.write("\\subsection{"
				    + targetEntry[BasicDataCols.TARGET_NAME
					    .ordinal()]);
			}
			targetWriter
				.write(", "
					+ targetEntry[BasicDataCols.TYPE_NAME
						.ordinal()] + "}\n");
			targetWriter.write("\\begin{itemize}\n");
		    } else {
			// if file was already created skip the previous two
			// lines
			targetWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(
					new File(filesLocation + File.separator
						+ reportFolder, filenameOut
						+ ".tex"), true), "utf-8"));
		    }
		    String[] metaData = report.getMetaData();
		    targetWriter
			    .write("\\item "
				    + metaData[BasicMetaDataCols.DATE_NAME.ordinal()]
				    + ". "
				    + metaData[BasicMetaDataCols.SEEING_NAME
					    .ordinal()]
				    + ", "
				    + metaData[BasicMetaDataCols.TRANSPARENCY_NAME
					    .ordinal()]
				    + ". "
				    + metaData[BasicMetaDataCols.TELESCOPES_NAME
					    .ordinal()]
				    + ", "
				    + targetEntry[BasicDataCols.POWER_NAME
					    .ordinal()]
				    + ". "
				    + targetEntry[BasicDataCols.NOTES_NAME
					    .ordinal()] + "\n");

		    // do not close the Latex 'itemize' block now because
		    // nothing is known about other observations
		    // for this target.

		} catch (IOException ex) {
		    log.warn("Error when opening the file " + filesLocation
			    + File.separator + filenameOut, ex);
		    return false;
		} catch (Exception ex) {
		    log.error(ex, ex);
		    return false;
		} finally {
		    try {
			if (targetWriter != null)
			    targetWriter.close();
		    } catch (Exception ex) {
			log.error(ex, ex);
			return false;
		    }
		}
	    }
	}
	return closeLists(reports);
    }

    /**
     * Write a catalogue section.
     * 
     * @param writer
     * @param target
     * @param type
     * @param catName
     * @throws IOException
     */
    private void writeSectionName(Writer writer, String target, String type,
	    String catName) throws IOException {
	if (!type.equals(catName)) {
	    type = catName;
	    writer.write("\\clearpage\n");
	    writer.write("\\section{" + type + "}\n");
	}
    }

    /**
     * It closes the latex lists opened by the function exportObservations
     * 
     * @param reports
     *            the list of observations to exportObservation
     * @return true if the lists are closed
     */
    private boolean closeLists(List<Report> reports) {
	processedTargetCache.clear();
	for (int i = 0; i < reports.size(); i++) {
	    Report report = reports.get(i);
	    List<String[]> targets = report.getAllData();
	    for (int j = 0; j < targets.size(); j++) {
		String[] targetEntry = targets.get(j);
		String filenameOut = computeFileName(targetEntry);
		Writer targetWriter = null;
		try {
		    if (!processedTargetCache.contains(filenameOut)) {
			processedTargetCache.add(filenameOut);
			targetWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(
					new File(filesLocation + File.separator
						+ reportFolder, filenameOut
						+ ".tex"), true), "utf-8"));
			targetWriter.write("\\end{itemize}\n");
			if (resourceBundle != null) {
			    log.info("\tExported target " + filenameOut);
			}
		    }

		} catch (IOException ex) {
		    log.warn("Error when opening the file " + filesLocation
			    + File.separator + filenameOut, ex);
		    return false;
		} catch (Exception ex) {
		    log.error(ex, ex);
		    return false;
		} finally {
		    try {
			if (targetWriter != null)
			    targetWriter.close();
		    } catch (Exception ex) {
			log.error(ex, ex);
			return false;
		    }
		}
	    }
	}
	return true;
    }

    /**
     * Create the filename with different formats depending on whether this is a
     * planet, a double/multiple star system, or everything else
     * 
     * @return the name of the file
     */
    private String computeFileName(String[] targetEntry) {
	if (targetEntry[BasicDataCols.TYPE_NAME.ordinal()].toLowerCase()
		.equals("planet")
		|| targetEntry[BasicDataCols.TARGET_NAME.ordinal()]
			.toLowerCase().equals("moon")
		|| targetEntry[BasicDataCols.TARGET_NAME.ordinal()]
			.toLowerCase().equals("sun")) {
	    return targetEntry[BasicDataCols.TARGET_NAME.ordinal()].replaceAll(
		    "\\s+", "").replaceAll("/", "-");
	}
	if (targetEntry[BasicDataCols.TYPE_NAME.ordinal()].toLowerCase()
		.equals("asteroid")
		|| targetEntry[BasicDataCols.TYPE_NAME.ordinal()].toLowerCase()
			.equals("comet")) {
	    return targetEntry[BasicDataCols.TYPE_NAME.ordinal()].replaceAll(
		    "\\s+", "").replaceAll("/", "-");
	}
	if (targetEntry[BasicDataCols.TYPE_NAME.ordinal()].toLowerCase()
		.equals("star")
		|| targetEntry[BasicDataCols.TYPE_NAME.ordinal()].toLowerCase()
			.equals("dbl star")
		|| targetEntry[BasicDataCols.TYPE_NAME.ordinal()].toLowerCase()
			.equals("mlt star")) {
	    return targetEntry[BasicDataCols.TARGET_NAME.ordinal()].replaceAll(
		    "\\s+", "").replaceAll("/", "-");
	}
	if (targetEntry[BasicDataCols.TYPE_NAME.ordinal()].toLowerCase()
		.equals("galaxy")
		&& targetEntry[BasicDataCols.TARGET_NAME.ordinal()]
			.toLowerCase().equals("milky way")) {
	    // Don't print the constellation if we are processing the milky way!
	    return targetEntry[BasicDataCols.TARGET_NAME.ordinal()].replaceAll(
		    "\\s+", "").replaceAll("/", "-");
	}
	return targetEntry[BasicDataCols.TARGET_NAME.ordinal()].replaceAll(
		"\\s+", "").replaceAll("/", "-");
    }

    /**
     * Recreate the list of files as sorted by catalogue
     * 
     * @param list
     *            the sorted catalogue
     * @param files
     *            the full list of files
     * @param idx
     *            the current index for files
     * @return idx the new index for files
     */
    private int addSortedFiles(List<String> list, File[] files, int idx) {
	for (int i = 0; i < list.size(); i++) {
	    if (idx < files.length) {
		files[idx] = new File(list.get(i));
		idx++;
	    }
	}
	return idx;
    }

    /**
     * Sort the files by target.
     * 
     * @param files
     *            the files to be sorted by target
     */
    private void sortFilesByTarget(File[] files) {
	// solar system in ArrayList instead of simple array, because we do not
	// know
	// how many conjunctions there are.
	LinkedList<String> solarSystem = new LinkedList<String>();
	ArrayList<String> milkyWay = new ArrayList<String>(1);
	ArrayList<String> messier = new ArrayList<String>(110);
	ArrayList<String> ngc = new ArrayList<String>(2500);
	ArrayList<String> ic = new ArrayList<String>(1000);
	ArrayList<String> stock = new ArrayList<String>(100);
	ArrayList<String> melotte = new ArrayList<String>(400);
	ArrayList<String> collider = new ArrayList<String>(300);
	ArrayList<String> pk = new ArrayList<String>(500);
	ArrayList<String> barnard = new ArrayList<String>(366);
	ArrayList<String> hickson = new ArrayList<String>(100);
	ArrayList<String> abell = new ArrayList<String>(500);
	ArrayList<String> ugc = new ArrayList<String>(2000);
	ArrayList<String> steph = new ArrayList<String>(500);
	ArrayList<String> unclassified = new ArrayList<String>(500);

	// Add empty data for the solar system. Conjunctions will be added in
	// the end.
	solarSystem.add("");
	solarSystem.add("");
	solarSystem.add("");
	solarSystem.add("");
	solarSystem.add("");
	solarSystem.add("");
	solarSystem.add("");
	solarSystem.add("");
	solarSystem.add("");
	solarSystem.add("");
	String target = null;
	for (int i = 0; i < files.length; i++) {
	    target = files[i].getName();
	    if (target
		    .matches("^(sun|moon|mercury|venus|mars|asteroid|jupiter|saturn|uranus|neptune|pluto|comet|Sun|Moon|Mercury|Venus|Mars|Asteroid|Jupiter|Saturn|Uranus|Neptune|Pluto|Comet).*$")) {
		if (target.matches("^(sun|Sun)\\.tex$")) {
		    solarSystem.remove(0);
		    solarSystem.add(0, files[i].toString());
		} else if (target.matches("^(moon|Moon)\\.tex$")) {
		    solarSystem.remove(1);
		    solarSystem.add(1, files[i].toString());
		} else if (target.matches("^(mercury|Mercury)\\.tex$")) {
		    solarSystem.remove(2);
		    solarSystem.add(2, files[i].toString());
		} else if (target.matches("^(venus|Venus)\\.tex$")) {
		    solarSystem.remove(3);
		    solarSystem.add(3, files[i].toString());
		} else if (target.matches("^(mars|Mars)\\.tex$")) {
		    solarSystem.remove(4);
		    solarSystem.add(4, files[i].toString());
		} else if (target.matches("^(asteroid|Asteroid)\\.tex$")) {
		    solarSystem.remove(5);
		    solarSystem.add(5, files[i].toString());
		} else if (target.matches("^(jupiter|Jupiter)\\.tex$")) {
		    solarSystem.remove(6);
		    solarSystem.add(6, files[i].toString());
		} else if (target.matches("^(saturn|Saturn)\\.tex$")) {
		    solarSystem.remove(7);
		    solarSystem.add(7, files[i].toString());
		} else if (target.matches("^(uranus|Uranus)\\.tex$")) {
		    solarSystem.remove(8);
		    solarSystem.add(8, files[i].toString());
		} else if (target.matches("^(neptune|Neptune)\\.tex$")) {
		    solarSystem.remove(9);
		    solarSystem.add(9, files[i].toString());
		} else if (target.matches("^(pluto|Pluto)\\.tex$")) {
		    solarSystem.remove(10);
		    solarSystem.add(10, files[i].toString());
		} else if (target.matches("^(comet|Comet)\\.tex$")) {
		    solarSystem.remove(11);
		    solarSystem.add(11, files[i].toString());
		}
		// conjunctions
		else {
		    solarSystem.add(files[i].toString());
		}
		log.debug(target);
	    } else if (target.matches("^(milkyway|MilkyWay|MILKYWAY).*$")) {
		milkyWay.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(m|M)[0-9].*$")) {
		messier.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(ngc|NGC)[0-9].*$")) {
		ngc.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(ic|IC)[0-9].*$")) {
		ic.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(stock|Stock|STOCK)[0-9].*$")) {
		stock.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(mel|Mel|MEL)[0-9].*$")) {
		melotte.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(cr|Cr|CR)[0-9].*$")) {
		collider.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(b|B|Barnard|BARNARD)[0-9].*$")) {
		barnard.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(pk|PK)[0-9].*$")) {
		pk.add(files[i].toString());
		log.debug(target);
	    } else if (target
		    .matches("^(hcg|HCG|Hickson Compact Group)[0-9].*$")) {
		hickson.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(Abell|ABELL)[0-9].*$")) {
		abell.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(ugc|UGC)[0-9].*$")) {
		ugc.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(Steph)[0-9].*$")) {
		steph.add(files[i].toString());
		log.debug(target);
	    } else {
		unclassified.add(files[i].toString());
		log.debug(target);
	    }
	}
	// note Planets and Milky Way are manually sorted
	Collections.sort(messier, catalogueItemComparator);
	Collections.sort(ngc, catalogueItemComparator);
	Collections.sort(ic, catalogueItemComparator);
	Collections.sort(stock, catalogueItemComparator);
	Collections.sort(melotte, catalogueItemComparator);
	Collections.sort(collider, catalogueItemComparator);
	Collections.sort(pk, catalogueItemComparator);
	Collections.sort(barnard, catalogueItemComparator);
	Collections.sort(hickson, catalogueItemComparator);
	Collections.sort(abell, catalogueItemComparator);
	Collections.sort(ugc, catalogueItemComparator);
	Collections.sort(steph, catalogueItemComparator);
	// normal lexicon-numerical sorting for stars
	Collections.sort(unclassified);
	log.debug("Catalogues are now sorted by target.");

	int j = 0;
	j = addSortedFiles(solarSystem, files, j);
	j = addSortedFiles(milkyWay, files, j);
	j = addSortedFiles(messier, files, j);
	j = addSortedFiles(ngc, files, j);
	j = addSortedFiles(ic, files, j);
	j = addSortedFiles(stock, files, j);
	j = addSortedFiles(melotte, files, j);
	j = addSortedFiles(collider, files, j);
	j = addSortedFiles(pk, files, j);
	j = addSortedFiles(barnard, files, j);
	j = addSortedFiles(hickson, files, j);
	j = addSortedFiles(abell, files, j);
	j = addSortedFiles(ugc, files, j);
	j = addSortedFiles(steph, files, j);
	j = addSortedFiles(unclassified, files, j);

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
