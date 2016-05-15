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
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ajconfiguration.AJPropertyConstants;
import org.astrojournal.generator.Report;

/**
 * A generic latex Exporter.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public abstract class LatexExporterByConst extends LatexExporter {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(LatexExporterByConst.class);

    protected HashMap<String, HashSet<String>> constellations = new HashMap<String, HashSet<String>>();

    /** A comparator for sorting items */
    protected Comparator<String> itemComparator = new Comparator<String>() {
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
    public LatexExporterByConst() {
	super();
    }

    @Override
    public void setConfiguration(Configuration config) {
	super.setConfiguration(config);
	setReportFolder(config
		.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
			.getKey()));
	setReportFilename(config
		.getProperty(AJPropertyConstants.LATEX_REPORT_BY_CONSTELLATION_FILENAME
			.getKey()));
	setHeaderFilename(config
		.getProperty(AJPropertyConstants.LATEX_HEADER_BY_CONSTELLATION_FILENAME
			.getKey()));
	setFooterFilename(config
		.getProperty(AJPropertyConstants.LATEX_FOOTER_BY_CONSTELLATION_FILENAME
			.getKey()));
    }

    @Override
    public boolean exportReports(List<Report> reports) {
	if (resourceBundle != null) {
	    log.info("");
	    log.info("Exporting reports by constellation:");
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
			+ File.separator + filenameOut);
		log.debug("Error when opening the file " + filesLocation
			+ File.separator + filenameOut, ex);
		result = false;
	    } catch (Exception ex) {
		log.debug(ex);
		log.error(ex, ex);
		result = false;
	    } finally {
		try {
		    if (list != null)
			list.close();
		} catch (Exception ex) {
		    log.debug(ex);
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
    protected abstract void organiseTargetsByConstellation(List<Report> reports);

}
