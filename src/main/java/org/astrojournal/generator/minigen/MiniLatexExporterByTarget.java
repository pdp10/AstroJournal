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
package org.astrojournal.generator.minigen;

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
import org.astrojournal.generator.headfoot.LatexFooter;
import org.astrojournal.generator.headfoot.LatexHeader;

/**
 * Exports an AstroJournal set of observations by target to Latex code. This is
 * an basic exporter which uses MiniMetaDataCols and MiniDataCols enum types for
 * column export.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public class MiniLatexExporterByTarget extends LatexExporterByTarget {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(MiniLatexExporterByTarget.class);

    /**
     * Default constructor.
     */
    public MiniLatexExporterByTarget() {
	super();
    }

    @Override
    public boolean generateJournal() {
	LatexHeader latexHeader = new LatexHeader();
	LatexFooter latexFooter = new LatexFooter();
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

    @Override
    public boolean exportReports(List<Report> reports) {
	if (resourceBundle != null) {
	    log.info("");
	    log.info("Exporting reports by target:");
	}
	// Statistics
	String typeCount = "";
	targetStatistics.reset();

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
			if (targetEntry[MiniDataCols.TYPE_NAME.ordinal()]
				.toLowerCase().equals("planet")
				|| targetEntry[MiniDataCols.TARGET_NAME
					.ordinal()].toLowerCase()
					.equals("moon")
				|| targetEntry[MiniDataCols.TARGET_NAME
					.ordinal()].toLowerCase().equals("sun")
				|| targetEntry[MiniDataCols.TYPE_NAME.ordinal()]
					.toLowerCase().equals("asteroid")
				|| targetEntry[MiniDataCols.TYPE_NAME.ordinal()]
					.toLowerCase().equals("comet")) {
			    writer.write("\\subsection*{"
				    + targetEntry[MiniDataCols.TARGET_NAME
					    .ordinal()]);
			    typeCount = "solar system";
			} else if (targetEntry[MiniDataCols.TYPE_NAME.ordinal()]
				.toLowerCase().equals("star")
				|| targetEntry[MiniDataCols.TYPE_NAME.ordinal()]
					.toLowerCase().equals("dbl star")
				|| targetEntry[MiniDataCols.TYPE_NAME.ordinal()]
					.toLowerCase().equals("mlt star")) {
			    writer.write("\\subsection*{"
				    + targetEntry[MiniDataCols.CONSTELLATION_NAME
					    .ordinal()]);
			    writer.write(", "
				    + targetEntry[MiniDataCols.TARGET_NAME
					    .ordinal()]);
			    typeCount = "star";
			} else if (targetEntry[MiniDataCols.TYPE_NAME.ordinal()]
				.toLowerCase().equals("galaxy")
				&& targetEntry[MiniDataCols.TARGET_NAME
					.ordinal()].toLowerCase().equals(
					"milky way")) {
			    // Don't print the constellation if we are
			    // processing the milky way!
			    writer.write("\\subsection*{"
				    + targetEntry[MiniDataCols.TARGET_NAME
					    .ordinal()]);
			    typeCount = "galaxy";
			} else {
			    writer.write("\\subsection*{"
				    + targetEntry[MiniDataCols.TARGET_NAME
					    .ordinal()]);
			    writer.write(", "
				    + targetEntry[MiniDataCols.CONSTELLATION_NAME
					    .ordinal()]);
			    typeCount = targetEntry[MiniDataCols.TYPE_NAME
				    .ordinal()].toLowerCase();
			}
			writer.write(", "
				+ targetEntry[MiniDataCols.TYPE_NAME.ordinal()]
				+ "}\n");
			// increment the counter for this typeCount.
			targetStatistics.increment(typeCount);

			writer.write("\\par\n");
		    } else {
			// if file was already created skip the previous two
			// lines and continue appending text (NOTE: true)
			writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(filesLocation
					+ File.separator + reportFolder,
					filenameOut + ".tex"), true), "utf-8"));
		    }
		    String[] metaData = report.getMetaData();
		    writer.write("("
			    + metaData[MiniMetaDataCols.DATE_NAME.ordinal()]
			    + ") \n");

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
		    if (resourceBundle != null) {
			log.info("\tExported target " + filenameOut);
		    }
		}
	    }
	}
	return true;
    }

    @Override
    protected String writeSectionName(Writer writer, String type, String catName)
	    throws IOException {
	if (!type.equals(catName)) {
	    type = catName;
	    writer.write("\\section*{" + type + "}\n");
	}
	return type;
    }

    @Override
    protected void writeSectionStatistics(Writer writer) throws Exception {
	writer.write("\\clearpage\n");
	writer.write("\\section*{Basic Statistics}\n");
	// include the file removing the extension .tex
	writer.write("\\input{" + reportFolder + "/"
		+ basicStatisticsFilename.replaceFirst("[.][^.]+$", "") + "}\n");
    }

    @Override
    public void writeLatexContent(Writer writer, Report report)
	    throws IOException {
	// TODO Auto-generated method stub
    }

}
