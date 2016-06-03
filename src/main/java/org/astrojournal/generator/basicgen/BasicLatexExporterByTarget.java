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
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.absgen.LatexExporterByTarget;

/**
 * Exports an AstroJournal set of observations by target to Latex code. This is
 * an basic exporter which uses BasicMetaDataCols and BasicDataCols enum types
 * for column export.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public class BasicLatexExporterByTarget extends LatexExporterByTarget {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(BasicLatexExporterByTarget.class);

    /**
     * Default constructor.
     */
    public BasicLatexExporterByTarget() {
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
			    writer.write("\\subsection{"
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
			    writer.write("\\subsection{"
				    + targetEntry[BasicDataCols.CONSTELLATION_NAME
					    .ordinal()]);
			    writer.write(", "
				    + targetEntry[BasicDataCols.TARGET_NAME
					    .ordinal()]);
			} else if (targetEntry[BasicDataCols.TYPE_NAME
				.ordinal()].toLowerCase().equals("galaxy")
				&& targetEntry[BasicDataCols.TARGET_NAME
					.ordinal()].toLowerCase().equals(
					"milky way")) {
			    // Don't print the constellation if we are
			    // processing the milky way!
			    writer.write("\\subsection{"
				    + targetEntry[BasicDataCols.TARGET_NAME
					    .ordinal()]);
			} else {
			    writer.write("\\subsection{"
				    + targetEntry[BasicDataCols.TARGET_NAME
					    .ordinal()]);
			    writer.write(", "
				    + targetEntry[BasicDataCols.CONSTELLATION_NAME
					    .ordinal()]);
			}
			writer.write(", "
				+ targetEntry[BasicDataCols.TYPE_NAME.ordinal()]
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
			    + metaData[BasicMetaDataCols.DATE_NAME.ordinal()]);
		    if (!metaData[BasicMetaDataCols.SEEING_NAME.ordinal()]
			    .isEmpty()) {
			writer.write("; "
				+ BasicMetaDataCols.SEEING_NAME.getColName()
				+ ": "
				+ metaData[BasicMetaDataCols.SEEING_NAME
					.ordinal()]);
		    }
		    if (!metaData[BasicMetaDataCols.TRANSPARENCY_NAME.ordinal()]
			    .isEmpty()) {
			writer.write(", "
				+ BasicMetaDataCols.TRANSPARENCY_NAME
					.getColName()
				+ ": "
				+ metaData[BasicMetaDataCols.TRANSPARENCY_NAME
					.ordinal()]);
		    }
		    if (!metaData[BasicMetaDataCols.TELESCOPES_NAME.ordinal()]
			    .isEmpty()) {
			writer.write("; "
				+ metaData[BasicMetaDataCols.TELESCOPES_NAME
					.ordinal()]);
		    }
		    if (!targetEntry[BasicDataCols.POWER_NAME.ordinal()]
			    .isEmpty()) {
			writer.write(", "
				+ targetEntry[BasicDataCols.POWER_NAME
					.ordinal()]);
		    }

		    writer.write(".\n");

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
    }

}
