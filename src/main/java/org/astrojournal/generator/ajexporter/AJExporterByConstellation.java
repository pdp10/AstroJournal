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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.AJConfig;
import org.astrojournal.generator.headerfooter.AJLatexFooter;
import org.astrojournal.generator.headerfooter.AJLatexHeader;
import org.astrojournal.generator.observation.AJObservation;
import org.astrojournal.generator.observation.AJObservationItem;

/**
 * Exports the observed targets by constellation to Latex code.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 28/05/2015
 */
public class AJExporterByConstellation extends AJExporter {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(AJExporterByConstellation.class);

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
     * Default Constructor
     */
    public AJExporterByConstellation() {
	super();
    }

    /**
     * Constructor
     * 
     * @param ajFilesLocation
     */
    public AJExporterByConstellation(File ajFilesLocation) {
	super(ajFilesLocation);
    }

    /**
     * Generate the Latex document sorted by constellation
     * 
     * @param observations
     *            the list of observations to exportObservation
     * @return true if the observations sorted by constellation have been
     *         exported to Latex correctly
     */
    @Override
    public boolean generateJournal(ArrayList<AJObservation> observations) {
	// export the imported observation by constellation to Latex
	log.info("");
	log.info("Exporting observations by constellation:");
	boolean resultByConstellation = exportObservations(observations,
		AJConfig.getInstance().getLatexReportsFolderByConstellation());
	generateJournal(AJConfig.getInstance()
		.getLatexReportsFolderByConstellation(),
		AJConfig.HEADER_BY_CONSTELLATION_FILENAME,
		AJConfig.REPORT_BY_CONSTELLATION_FILENAME,
		AJConfig.FOOTER_BY_CONSTELLATION_FILENAME);
	return resultByConstellation;
    }

    /**
     * Generate the Latex document by constellation
     * 
     * @param latexReportsFolderByConst
     *            the directory containing the single observations by date in
     *            latex format (output)
     * @param latexHeaderByConst
     *            the latex header code (by date)
     * @param latexMainByConst
     *            the latex main body code (by date)
     * @param latexFooterByConst
     *            the latex footer code (by date)
     */
    @Override
    public void generateJournal(String latexReportsFolderByConst,
	    String latexHeaderByConst, String latexMainByConst,
	    String latexFooterByConst) {
	AJLatexHeader ajLatexHeaderByConst = new AJLatexHeader(
		ajFilesLocation.getAbsolutePath(), latexHeaderByConst);
	AJLatexFooter ajLatexFooterByConst = new AJLatexFooter(
		ajFilesLocation.getAbsolutePath(), latexFooterByConst);
	Writer writerByConst = null;
	try {
	    writerByConst = new BufferedWriter(new OutputStreamWriter(
		    new FileOutputStream(ajFilesLocation.getAbsolutePath()
			    + File.separator + latexMainByConst), "utf-8"));
	    // write the Latex Header
	    writerByConst.write(ajLatexHeaderByConst.getHeader());

	    // write the Latex Body
	    // parse each file in the latex obs folder
	    File[] files = new File(ajFilesLocation.getAbsolutePath()
		    + File.separator + latexReportsFolderByConst).listFiles();
	    if (files == null) {
		log.warn("Folder " + ajFilesLocation.getAbsolutePath()
			+ File.separator + latexReportsFolderByConst
			+ " not found");
		return;
	    }
	    // sort the constellations when we parse the files
	    Arrays.sort(files);

	    String currConst = "", filename = "";
	    // If this pathname does not denote a directory, then listFiles()
	    // returns null.
	    for (File file : files) {
		filename = file.getName();
		if (file.isFile() && filename.endsWith(".tex")) {
		    if (!currConst.equals(filename.substring(
			    filename.indexOf("_") + 1, filename.indexOf(".")))) {
			currConst = filename.substring(
				filename.indexOf("_") + 1,
				filename.indexOf("."));
			writerByConst.write("\\section{" + currConst + "}\n");
		    }
		    // include the file removing the extension .tex
		    writerByConst.write("\\input{" + latexReportsFolderByConst
			    + "/" + filename.replaceFirst("[.][^.]+$", "")
			    + "}\n");
		    // writerByConst.write("\\clearpage \n");
		}
	    }

	    // write the Latex Footer
	    writerByConst.write(ajLatexFooterByConst.getFooter());

	} catch (IOException ex) {
	    log.warn("Error when opening the file "
		    + ajFilesLocation.getAbsolutePath() + File.separator
		    + latexMainByConst);
	} catch (Exception ex) {
	    log.error(ex);
	} finally {
	    try {
		if (writerByConst != null)
		    writerByConst.close();
	    } catch (Exception ex) {
		log.error(ex);
	    }
	}
    }

    /**
     * Exports an observation record to Latex
     * 
     * @param observations
     *            the list of observations to exportObservation
     * @param latexReportsByConstFolder
     *            the folder to write the observation in.
     * @return true if the observations are exported
     */
    @Override
    public boolean exportObservations(ArrayList<AJObservation> observations,
	    String latexReportsByConstFolder) {
	boolean result = true;
	if (constellations.size() == 0) {
	    organiseTargetsByConstellation(observations);
	}
	String[] keys = constellations.keySet().toArray(new String[0]);
	for (int i = 0; i < keys.length; i++) {
	    Writer list = null;
	    String filenameOut = keys[i];
	    try {
		list = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(new File(
				ajFilesLocation.getAbsolutePath()
					+ File.separator
					+ latexReportsByConstFolder, "const_"
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
		log.info("\tExported constellation " + filenameOut);
	    } catch (IOException ex) {
		log.warn("Error when opening the file "
			+ ajFilesLocation.getAbsolutePath() + File.separator
			+ filenameOut);
		result = false;
	    } catch (Exception ex) {
		log.error(ex);
		result = false;
	    } finally {
		try {
		    if (list != null)
			list.close();
		} catch (Exception ex) {
		    log.error(ex);
		}
	    }
	}
	return result;
    }

    /**
     * Organise the targets by constellation in a suitable data structure.
     * 
     * @param observations
     *            the list of observations to exportObservation
     */
    private void organiseTargetsByConstellation(
	    ArrayList<AJObservation> observations) {
	AJObservation obs = null;
	int nObservations = observations.size();
	for (int i = 0; i < nObservations; i++) {
	    obs = observations.get(i);
	    ArrayList<AJObservationItem> items = obs.getObservationItems();
	    for (int j = 0; j < items.size(); j++) {
		AJObservationItem item = items.get(j);
		// skip solar system targets. We only consider DSOs.
		if (item.getType().toLowerCase().equals("planet")
			|| item.getTarget().toLowerCase().equals("sun")
			|| item.getTarget().toLowerCase().equals("moon")
			|| item.getTarget().toLowerCase().equals("milky way")) {
		    continue;
		}
		if (!constellations.containsKey(item.getConstellation())) {
		    constellations.put(item.getConstellation(),
			    new HashSet<String>());
		}
		log.debug(item.getConstellation() + " " + item.getTarget()
			+ " (" + item.getType() + ")");
		constellations.get(item.getConstellation()).add(
			item.getTarget() + " (" + item.getType() + ")");
	    }
	}
    }
}
