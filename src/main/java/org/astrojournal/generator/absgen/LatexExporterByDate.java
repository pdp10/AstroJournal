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
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ajconfiguration.AJPropertyConstants;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.basicgen.BasicMetaDataCols;
import org.astrojournal.generator.headfoot.LatexFooter;
import org.astrojournal.generator.headfoot.LatexHeader;
import org.astrojournal.generator.statistics.BasicStatistics;
import org.astrojournal.utilities.filefilters.LaTeXFilter;

/**
 * A generic latex Exporter with entries by date.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public abstract class LatexExporterByDate extends LatexExporter {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(LatexExporterByDate.class);

    /**
     * Default constructor.
     */
    public LatexExporterByDate() {
	super();
    }

    @Override
    public void setConfiguration(Configuration config) {
	super.setConfiguration(config);
	setReportFolder(config
		.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_DATE
			.getKey()));
	setReportFilename(config
		.getProperty(AJPropertyConstants.LATEX_REPORT_BY_DATE_FILENAME
			.getKey()));
	setHeaderFilename(config
		.getProperty(AJPropertyConstants.LATEX_HEADER_BY_DATE_FILENAME
			.getKey()));
	setFooterFilename(config
		.getProperty(AJPropertyConstants.LATEX_FOOTER_BY_DATE_FILENAME
			.getKey()));
    }

    @Override
    public boolean exportReports(List<Report> reports) {
	if (resourceBundle != null) {
	    log.info("");
	    log.info("Exporting reports by date:");
	}
	Report report = null;
	String[] metaData = null;
	int nReports = reports.size();
	boolean result = true;

	for (int i = 0; i < nReports; i++) {
	    report = reports.get(i);
	    metaData = report.getMetaData();
	    String date = metaData[BasicMetaDataCols.DATE_NAME.ordinal()];
	    log.debug("Report " + date);

	    String filenameOut = date;
	    filenameOut = filenameOut.substring(6, 10)
		    + filenameOut.substring(3, 5) + filenameOut.substring(0, 2);
	    // Add an additional char if this is present. This is the case in
	    // which more than one observation per day is done.
	    if (date.length() == 11) {
		filenameOut = filenameOut + date.charAt(10);
	    }

	    Writer writer = null;
	    try {
		writer = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(new File(filesLocation
				+ File.separator + reportFolder, filenameOut
				+ ".tex")), "utf-8"));

		writeLatexContent(writer, report);

		if (resourceBundle != null) {
		    log.info("\tExported report " + date + " ("
			    + report.getDataRowNumber() + " targets)");
		}
	    } catch (IOException ex) {
		log.error("Error when opening the file " + filesLocation
			+ File.separator + filenameOut);
		log.debug("Error when opening the file " + filesLocation
			+ File.separator + filenameOut, ex);
		result = false;
	    } catch (Exception ex) {
		log.error(ex);
		log.debug(ex, ex);
		result = false;
	    } finally {
		try {
		    if (writer != null)
			writer.close();
		} catch (Exception ex) {
		    log.error(ex);
		    log.debug(ex, ex);
		    return false;
		}
	    }
	}

	return result;
    }

    @Override
    public void writeLatexMain(Writer writer, LatexHeader latexHeader,
	    LatexFooter latexFooter, BasicStatistics basicStatistics)
	    throws Exception {

	// write the Latex Header
	writer.write(latexHeader.getHeader());

	// write target type statistics
	writeSectionStatistics(writer);
	writeLatexStatistics(basicStatistics);

	// write the Latex Body
	// Write the observation reports
	writer.write("\\section{Observation reports}\n");
	writer.write("\\vspace{4 mm}\n");
	writer.write("\\hspace{4 mm}\n");
	// parse each file in the latex obs folder (sorted by observation
	// increasing)
	// If this pathname does not denote a directory, then listFiles()
	// returns null.
	File[] files = new File(filesLocation + File.separator + reportFolder)
		.listFiles(new LaTeXFilter());
	if (files == null) {
	    throw new Exception("Folder " + filesLocation + File.separator
		    + reportFolder + " not found");
	}
	Arrays.sort(files, Collections.reverseOrder());
	String currentYear = "";
	for (File file : files) {
	    if (file.isFile()) {
		if (!currentYear.equals(file.getName().substring(0, 4))) {
		    // collect observations by year
		    currentYear = file.getName().substring(0, 4);
		    writer.write("\\subsection{" + currentYear + "}\n");
		}
		// include the file removing the extension .tex
		writer.write("\\input{" + reportFolder + "/"
			+ file.getName().replaceFirst("[.][^.]+$", "") + "}\n");
	    }
	}

	// write the Latex Footer
	writer.write(latexFooter.getFooter());
    }

}
