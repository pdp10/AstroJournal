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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.absgen.Exporter;
import org.astrojournal.utilities.filefilters.TextFilter;

/**
 * Exports an AstroJournal observation to txt for Stargazers Lounge reports.
 * This is a basic exporter which uses BasicMetaDataCols and BasicDataCols enum
 * types for column export.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 11/09/2015
 */
public class BasicTextExporterByDateSGL extends Exporter {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(BasicTextExporterByDateSGL.class);

    /**
     * Default constructor
     */
    public BasicTextExporterByDateSGL() {
	super();
    }

    /**
     * Generate a txt document sorting the observation by decreasing date
     */
    @Override
    public boolean generateJournal() {
	Writer writerByDate = null;
	try {
	    writerByDate = new BufferedWriter(new OutputStreamWriter(
		    new FileOutputStream(filesLocation + File.separator
			    + reportFilename), "utf-8"));
	    // write the Header

	    // write the Body
	    // Write the observation reports
	    // parse each file in the obs folder (sorted by observation
	    // increasing)
	    File[] files = new File(filesLocation + File.separator
		    + reportFolder).listFiles(new TextFilter());
	    if (files == null) {
		log.warn("Folder " + filesLocation + File.separator
			+ reportFolder + " not found");
		return false;
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
			writerByDate.write(text);
		    } catch (NoSuchElementException e) {
			log.error(e);
			log.debug(e, e);
		    }
		    writerByDate.write("\n\n\n\n");
		}
	    }

	    // write the Footer

	} catch (IOException ex) {
	    log.warn("Error when opening the file " + filesLocation
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
		if (writerByDate != null)
		    writerByDate.close();
	    } catch (Exception ex) {
		log.error(ex);
		log.debug(ex, ex);
		return false;
	    }
	}
	return true;
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

	    Writer text = null;

	    String filenameOut = metaData[BasicMetaDataCols.DATE_NAME.ordinal()];
	    filenameOut = filenameOut.substring(6, 10)
		    + filenameOut.substring(3, 5) + filenameOut.substring(0, 2);
	    // Add an additional char if this is present. This is the case in
	    // which
	    // more than one observation per day is done.
	    if (metaData[BasicMetaDataCols.DATE_NAME.ordinal()].length() == 11) {
		filenameOut = filenameOut
			+ metaData[BasicMetaDataCols.DATE_NAME.ordinal()]
				.charAt(10);
	    }

	    List<String[]> targets = report.getAllData();
	    try {
		text = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(new File(filesLocation
				+ File.separator + reportFolder, "obs"
				+ filenameOut + ".txt")), "utf-8"));

		text.write(BasicMetaDataCols.DATE_NAME.getColName() + ": "
			+ metaData[BasicMetaDataCols.DATE_NAME.ordinal()]
			+ "\n");
		text.write(BasicMetaDataCols.SEEING_NAME.getColName() + ": "
			+ metaData[BasicMetaDataCols.SEEING_NAME.ordinal()]
			+ "\n");
		text.write(BasicMetaDataCols.TRANSPARENCY_NAME.getColName()
			+ ": "
			+ metaData[BasicMetaDataCols.TRANSPARENCY_NAME
				.ordinal()] + "\n");
		text.write(BasicMetaDataCols.TELESCOPES_NAME.getColName()
			+ ": "
			+ metaData[BasicMetaDataCols.TELESCOPES_NAME.ordinal()]
			+ "\n\n");
		for (String[] targetEntry : targets) {
		    log.debug("Target "
			    + targetEntry[BasicDataCols.TARGET_NAME.ordinal()]);
		    text.write(targetEntry[BasicDataCols.TARGET_NAME.ordinal()]
			    + " "
			    + targetEntry[BasicDataCols.CONSTELLATION_NAME
				    .ordinal()] + " "
			    + targetEntry[BasicDataCols.TYPE_NAME.ordinal()]
			    + " "
			    + targetEntry[BasicDataCols.POWER_NAME.ordinal()]
			    + "\n");
		}
		if (resourceBundle != null) {
		    log.info("\tExported report "
			    + metaData[BasicMetaDataCols.DATE_NAME.ordinal()]
			    + " (" + targets.size() + " targets)");
		}
	    } catch (IOException ex) {
		log.debug("Error when opening the file " + filesLocation
			+ File.separator + filenameOut);
		log.error("Error when opening the file " + filesLocation
			+ File.separator + filenameOut, ex);
		result = false;
	    } catch (Exception ex) {
		log.error(ex);
		log.debug(ex, ex);
		result = false;
	    } finally {
		try {
		    if (text != null)
			text.close();
		} catch (Exception ex) {
		    log.error(ex);
		    log.debug(ex, ex);
		    result = false;
		}
	    }
	}

	return result;
    }

    @Override
    public void postProcessing() throws IOException {
	if (resourceBundle != null) {
	    log.info("\t" + filesLocation + File.separator + reportFilename);
	}
    }

}
