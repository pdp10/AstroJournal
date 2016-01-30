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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.headfoot.LatexFooter;
import org.astrojournal.generator.headfoot.LatexHeader;
import org.astrojournal.generator.minigen.MiniDataCols;
import org.astrojournal.utilities.filefilters.LaTeXFilter;

/**
 * A generic latex Exporter with elements sorted by target.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 17 Jan 2016
 */
public abstract class LatexExporterByTarget extends LatexExporter {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(LatexExporterByTarget.class);

    /** A cache of the visited targets. */
    protected HashSet<String> processedTargetCache = new HashSet<String>(1000);

    /** A comparator for sorting catalogues */
    protected Comparator<String> catalogueItemComparator = new Comparator<String>() {
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
	    log.warn("Error when opening the file " + filesLocation
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

    @Override
    public void writeLatexMain(Writer writer, LatexHeader latexHeader,
	    LatexFooter latexFooter) throws Exception {
	// write the Latex Header
	writer.write(latexHeader.getHeader());
	// write the Latex Body
	// Write the observation reports
	// parse each file in the latex obs folder (sorted by observation
	// increasing)
	File[] files = new File(filesLocation + File.separator + reportFolder)
		.listFiles(new LaTeXFilter());
	if (files == null) {
	    throw new Exception("Folder " + filesLocation + File.separator
		    + reportFolder + " not found");
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
		    type = writeSectionName(writer, target, type,
			    "Solar System");
		} else if (target.matches("^(milkyway|MilkyWay|MILKYWAY).*$")) {
		    type = writeSectionName(writer, target, type, "Milky Way");
		} else if (target.matches("^(m|M)[0-9].*$")) {
		    type = writeSectionName(writer, target, type,
			    "Messier Catalogue");
		} else if (target.matches("^(ngc|NGC)[0-9].*$")) {
		    type = writeSectionName(writer, target, type,
			    "New General Catalogue (NGC)");
		} else if (target.matches("^(ic|IC)[0-9].*$")) {
		    type = writeSectionName(writer, target, type,
			    "Index Catalogue (IC)");
		} else if (target.matches("^(stock|Stock|STOCK)[0-9].*$")) {
		    type = writeSectionName(writer, target, type,
			    "Stock Catalogue");
		} else if (target.matches("^(mel|Mel|MEL)[0-9].*$")) {
		    type = writeSectionName(writer, target, type,
			    "Melotte Catalogue");
		} else if (target.matches("^(cr|Cr|CR)[0-9].*$")) {
		    type = writeSectionName(writer, target, type,
			    "Collider Catalogue");
		} else if (target.matches("^(pk|PK)[0-9].*$")) {
		    type = writeSectionName(writer, target, type,
			    "Perek-Kohoutex Catalogue");
		} else if (target.matches("^(b|B|Barnard|BARNARD)[0-9].*$")) {
		    type = writeSectionName(writer, target, type,
			    "Barnard Catalogue");
		} else if (target
			.matches("^(hcg|HCG|Hickson Compact Group)[0-9].*$")) {
		    type = writeSectionName(writer, target, type,
			    "Hickson Compact Group Catalogue");
		} else if (target.matches("^(ugc|UGC)[0-9].*$")) {
		    type = writeSectionName(writer, target, type,
			    "Uppsala General Catalogue");
		} else if (target.matches("^(Steph)[0-9].*$")) {
		    type = writeSectionName(writer, target, type,
			    "Steph Catalogue");
		} else {
		    type = writeSectionName(writer, target, type,
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
    }

    /**
     * Write a catalogue section.
     * 
     * @param writer
     * @param target
     * @param type
     * @param catName
     * @return type
     * @throws IOException
     */
    protected String writeSectionName(Writer writer, String target,
	    String type, String catName) throws IOException {
	if (!type.equals(catName)) {
	    type = catName;
	    writer.write("\\clearpage\n");
	    writer.write("\\section{" + type + "}\n");
	}
	return type;
    }

    /**
     * It closes the latex lists opened by the function exportObservations
     * 
     * @param reports
     *            the list of observations to exportObservation
     * @return true if the lists are closed
     */
    protected boolean closeLists(List<Report> reports) {
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
			    + File.separator + filenameOut);
		    log.debug("Error when opening the file " + filesLocation
			    + File.separator + filenameOut, ex);
		    return false;
		} catch (Exception ex) {
		    log.debug(ex);
		    log.error(ex, ex);
		    return false;
		} finally {
		    try {
			if (targetWriter != null)
			    targetWriter.close();
		    } catch (Exception ex) {
			log.debug(ex);
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
    protected String computeFileName(String[] targetEntry) {

	if (targetEntry[MiniDataCols.TYPE_NAME.ordinal()].toLowerCase().equals(
		"planet")
		|| targetEntry[MiniDataCols.TARGET_NAME.ordinal()]
			.toLowerCase().equals("moon")
		|| targetEntry[MiniDataCols.TARGET_NAME.ordinal()]
			.toLowerCase().equals("sun")) {
	    return targetEntry[MiniDataCols.TARGET_NAME.ordinal()].replaceAll(
		    "\\s+", "").replaceAll("/", "-");
	}
	if (targetEntry[MiniDataCols.TYPE_NAME.ordinal()].toLowerCase().equals(
		"asteroid")
		|| targetEntry[MiniDataCols.TYPE_NAME.ordinal()].toLowerCase()
			.equals("comet")) {
	    return targetEntry[MiniDataCols.TYPE_NAME.ordinal()].replaceAll(
		    "\\s+", "").replaceAll("/", "-");
	}
	if (targetEntry[MiniDataCols.TYPE_NAME.ordinal()].toLowerCase().equals(
		"star")
		|| targetEntry[MiniDataCols.TYPE_NAME.ordinal()].toLowerCase()
			.equals("dbl star")
		|| targetEntry[MiniDataCols.TYPE_NAME.ordinal()].toLowerCase()
			.equals("mlt star")) {
	    return targetEntry[MiniDataCols.CONSTELLATION_NAME.ordinal()]
		    + "_"
		    + targetEntry[MiniDataCols.TARGET_NAME.ordinal()]
			    .replaceAll("\\s+", "").replaceAll("/", "-");
	}
	if (targetEntry[MiniDataCols.TYPE_NAME.ordinal()].toLowerCase().equals(
		"galaxy")
		&& targetEntry[MiniDataCols.TARGET_NAME.ordinal()]
			.toLowerCase().equals("milky way")) {
	    // Don't print the constellation if we are processing the milky way!
	    return targetEntry[MiniDataCols.TARGET_NAME.ordinal()].replaceAll(
		    "\\s+", "").replaceAll("/", "-");
	}
	return targetEntry[MiniDataCols.TARGET_NAME.ordinal()].replaceAll(
		"\\s+", "").replaceAll("/", "-")
		+ "_" + targetEntry[MiniDataCols.CONSTELLATION_NAME.ordinal()];
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
    protected void sortFilesByTarget(File[] files) {
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

}
