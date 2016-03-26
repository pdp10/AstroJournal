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
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ajconfiguration.AJPropertyConstants;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.extgen.ExtMetaDataCols;
import org.astrojournal.generator.minigen.MiniMetaDataCols;
import org.astrojournal.utilities.filefilters.TextFilter;

/**
 * A generic latex Exporter with entries by date.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 17 Jan 2016
 */
public abstract class TextExporterByDateSGL extends Exporter {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(TextExporterByDateSGL.class);

    /**
     * Default constructor.
     */
    public TextExporterByDateSGL() {
	super();
    }

    @Override
    public void setConfiguration(Configuration config) {
	super.setConfiguration(config);
	setReportFolder(config
		.getProperty(AJPropertyConstants.SGL_REPORTS_FOLDER_BY_DATE
			.getKey()));
	setReportFilename(config
		.getProperty(AJPropertyConstants.SGL_REPORT_BY_DATE_FILENAME
			.getKey()));
    }

    @Override
    public boolean exportReports(List<Report> reports) {
	if (resourceBundle != null) {
	    log.info("");
	    log.info("Exporting reports by date for SGL:");
	}
	Report report = null;
	int nReports = reports.size();
	boolean result = true;

	for (int i = 0; i < nReports; i++) {
	    report = reports.get(i);
	    String[] metaData = report.getMetaData();

	    Writer writer = null;

	    String filenameOut = metaData[MiniMetaDataCols.DATE_NAME.ordinal()];
	    filenameOut = filenameOut.substring(6, 10)
		    + filenameOut.substring(3, 5) + filenameOut.substring(0, 2);
	    // Add an additional char if this is present. This is the case in
	    // which
	    // more than one observation per day is done.
	    if (metaData[MiniMetaDataCols.DATE_NAME.ordinal()].length() == 11) {
		filenameOut = filenameOut
			+ metaData[MiniMetaDataCols.DATE_NAME.ordinal()]
				.charAt(10);
	    }

	    List<String[]> targets = report.getAllData();
	    try {
		writer = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(new File(filesLocation
				+ File.separator + reportFolder, filenameOut
				+ ".txt")), "utf-8"));

		writeTextContent(writer, report);

		if (resourceBundle != null) {
		    log.info("\tExported report "
			    + metaData[ExtMetaDataCols.DATE_NAME.ordinal()]
			    + " (" + targets.size() + " targets)");
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
		    result = false;
		}
	    }
	}

	return result;
    }

    /**
     * Write the document content.
     * 
     * @param writer
     * @param report
     * @throws IOException
     */
    protected abstract void writeTextContent(Writer writer, Report report)
	    throws IOException;

    /**
     * Generate a txt document sorting the observation by decreasing date
     */
    @Override
    public boolean generateJournal() {
	Writer writer = null;
	try {
	    writer = new BufferedWriter(new OutputStreamWriter(
		    new FileOutputStream(filesLocation + File.separator
			    + reportFilename), "utf-8"));
	    writeTextMain(writer, "", "");

	} catch (IOException ex) {
	    log.error("Error when opening the file " + filesLocation
		    + File.separator + reportFilename);
	    log.debug("Error when opening the file " + filesLocation
		    + File.separator + reportFilename, ex);
	    return false;
	} catch (Exception ex) {
	    log.error(ex);
	    log.debug(ex, ex);
	    return false;
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
	return true;
    }

    /**
     * Write the main document
     * 
     * @param writer
     * @param header
     * @param footer
     * @throws Exception
     */
    public void writeTextMain(Writer writer, String header, String footer)
	    throws Exception {
	// write the Header

	// write the Body
	// Write the observation reports
	// parse each file in the obs folder (sorted by observation
	// increasing)
	File[] files = new File(filesLocation + File.separator + reportFolder)
		.listFiles(new TextFilter());
	if (files == null) {
	    throw new Exception("Folder " + filesLocation + File.separator
		    + reportFolder + " not found");
	}
	Arrays.sort(files, Collections.reverseOrder());
	// If this pathname does not denote a directory, then listFiles()
	// returns null.
	for (File file : files) {
	    if (file.isFile()) {
		// include the file removing the extension .txt
		try {
		    Scanner scanner = new Scanner(file, "UTF-8");
		    String text = scanner.useDelimiter("\\A").next();
		    scanner.close();
		    writer.write(text);
		} catch (NoSuchElementException e) {
		    log.debug(e, e);
		    log.error(e);
		}
		writer.write("\n\n");
	    }
	}
	// write the Footer
    }

    @Override
    public void postProcessing() throws IOException {
	if (resourceBundle != null) {
	    log.info("\t" + filesLocation + File.separator + reportFilename);
	}
    }

}
