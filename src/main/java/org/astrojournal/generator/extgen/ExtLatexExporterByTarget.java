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
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.absgen.LatexExporterByTarget;

/**
 * Exports an AstroJournal set of observations by target to Latex code. This is
 * an extended exporter which uses ExtMetaDataCols and ExtDataCols enum types
 * for column export.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 22/07/2015
 */
public class ExtLatexExporterByTarget extends LatexExporterByTarget {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(ExtLatexExporterByTarget.class);

    /**
     * Default constructor.
     */
    public ExtLatexExporterByTarget() {
	super();
    }

    @Override
    public boolean exportReports(List<Report> reports) {
	if (resourceBundle != null) {
	    log.info("");
	    log.info("Exporting reports by target:");
	}
	processedTargetCache.clear();
	for (int i = 0; i < reports.size(); i++) {
	    Report report = reports.get(i);
	    List<String[]> targets = report.getAllData();
	    for (int j = 0; j < targets.size(); j++) {
		String[] targetEntry = targets.get(j);
		String filenameOut = computeFileName(targetEntry);
		Writer writer = null;
		try {
		    if (!processedTargetCache.contains(filenameOut)) {
			processedTargetCache.add(filenameOut);
			// Let's create a new document
			writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(filesLocation
					+ File.separator + reportFolder,
					filenameOut + ".tex")), "utf-8"));
			if (targetEntry[ExtDataCols.TYPE_NAME.ordinal()]
				.toLowerCase().equals("planet")
				|| targetEntry[ExtDataCols.TARGET_NAME
					.ordinal()].toLowerCase()
					.equals("moon")
				|| targetEntry[ExtDataCols.TARGET_NAME
					.ordinal()].toLowerCase().equals("sun")
				|| targetEntry[ExtDataCols.TYPE_NAME.ordinal()]
					.toLowerCase().equals("asteroid")
				|| targetEntry[ExtDataCols.TYPE_NAME.ordinal()]
					.toLowerCase().equals("comet")) {
			    writer.write("\\subsection{"
				    + targetEntry[ExtDataCols.TARGET_NAME
					    .ordinal()]);
			} else if (targetEntry[ExtDataCols.TYPE_NAME.ordinal()]
				.toLowerCase().equals("star")
				|| targetEntry[ExtDataCols.TYPE_NAME.ordinal()]
					.toLowerCase().equals("dbl star")
				|| targetEntry[ExtDataCols.TYPE_NAME.ordinal()]
					.toLowerCase().equals("mlt star")) {
			    writer.write("\\subsection{"
				    + targetEntry[ExtDataCols.CONSTELLATION_NAME
					    .ordinal()]);
			    writer.write(", "
				    + targetEntry[ExtDataCols.TARGET_NAME
					    .ordinal()]);
			} else if (targetEntry[ExtDataCols.TYPE_NAME.ordinal()]
				.toLowerCase().equals("galaxy")
				&& targetEntry[ExtDataCols.TARGET_NAME
					.ordinal()].toLowerCase().equals(
					"milky way")) {
			    // Don't print the constellation if we are
			    // processing the milky way!
			    writer.write("\\subsection{"
				    + targetEntry[ExtDataCols.TARGET_NAME
					    .ordinal()]);
			} else {
			    writer.write("\\subsection{"
				    + targetEntry[ExtDataCols.TARGET_NAME
					    .ordinal()]);
			    writer.write(", "
				    + targetEntry[ExtDataCols.CONSTELLATION_NAME
					    .ordinal()]);
			}
			writer.write(", "
				+ targetEntry[ExtDataCols.TYPE_NAME.ordinal()]
				+ "}\n");
			writer.write("\\begin{itemize}\n");
		    } else {
			// if file was already created skip the previous two
			// lines and continue appending text (NOTE: true)
			writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(filesLocation
					+ File.separator + reportFolder,
					filenameOut + ".tex"), true), "utf-8"));
		    }
		    String[] metaData = report.getMetaData();
		    writer.write("\\item "
			    + metaData[ExtMetaDataCols.DATE_NAME.ordinal()]
			    + " "
			    + metaData[ExtMetaDataCols.TIME_NAME.ordinal()]
			    + ", "
			    + metaData[ExtMetaDataCols.LOCATION_NAME.ordinal()]);
		    if (!metaData[ExtMetaDataCols.LUNAR_PHASE_NAME.ordinal()]
			    .isEmpty()) {
			writer.write(", "
				+ ExtMetaDataCols.LUNAR_PHASE_NAME.getColName()
				+ ": "
				+ metaData[ExtMetaDataCols.LUNAR_PHASE_NAME
					.ordinal()].replace("%", "\\%"));
		    }
		    writer.write(". "
			    + ExtMetaDataCols.SEEING_NAME.getColName()
			    + ": "
			    + metaData[ExtMetaDataCols.SEEING_NAME.ordinal()]
			    + ", "
			    + ExtMetaDataCols.TRANSPARENCY_NAME.getColName()
			    + ": "
			    + metaData[ExtMetaDataCols.TRANSPARENCY_NAME
				    .ordinal()]);
		    if (!metaData[ExtMetaDataCols.DARKNESS_NAME.ordinal()]
			    .isEmpty()) {
			writer.write(", "
				+ ExtMetaDataCols.DARKNESS_NAME.getColName()
				+ ": "
				+ metaData[ExtMetaDataCols.DARKNESS_NAME
					.ordinal()]);
		    }
		    writer.write(". "
			    + metaData[ExtMetaDataCols.TELESCOPES_NAME
				    .ordinal()] + ", "
			    + targetEntry[ExtDataCols.POWER_NAME.ordinal()]
			    + ". "
			    + targetEntry[ExtDataCols.NOTES_NAME.ordinal()]
			    + "\n");

		    // do not close the Latex 'itemize' block now because
		    // nothing is known about other observations
		    // for this target.

		} catch (IOException ex) {
		    log.error("Error when opening the file " + filesLocation
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
			if (writer != null)
			    writer.close();
		    } catch (Exception ex) {
			log.debug(ex);
			log.error(ex, ex);
			return false;
		    }
		}
	    }
	}
	return closeLists(reports);
    }

    @Override
    public void writeLatexContent(Writer writer, Report report)
	    throws IOException {
	// TODO Auto-generated method stub

    }

}
