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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.absgen.Importer;
import org.astrojournal.utilities.filefilters.TSVRawReportFilter;

/**
 * The parser for AstroJournal. It imports tab separated value (tsv or csv)
 * files containing the reports. This is a basic TSV importer which uses
 * BasicMetaDataCols and BasicDataCols enum types for column import.
 * 
 * @author Piero Dalle Pezze
 * @version 0.4
 * @since 28/05/2015
 */
public class BasicTSVImporter extends Importer {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(BasicTSVImporter.class);

    /** The values contained in an imported string. */
    private String[] values = null;

    /** The meta data for the current report. */
    private String[] metaEntry = null;

    /**
     * Default constructor
     */
    public BasicTSVImporter() {
	super();
    }

    /**
     * Imports the report data stored in a file.
     * 
     * @param file
     *            The file to parse (either CSV or TSV file, separated by TAB
     *            delimiter).
     * @return a list of Report objects
     */
    @Override
    public List<Report> importReports(File file) {
	List<Report> reports = new ArrayList<Report>();
	if (file.isFile() && new TSVRawReportFilter().accept(file)) {

	    // whether this is tsv or csv it does not matter as long as fields
	    // are separated by a TAB character
	    String delimiter = "\t";

	    // Get the current file name.
	    String rawFilename = file.getName();
	    if (resourceBundle != null) {
		log.info("\t" + rawFilename);
	    }
	    // Create a buffered reader to read the file
	    BufferedReader reader = null;
	    try {
		reader = new BufferedReader(new FileReader(file));
		String line;
		// Read all lines
		boolean foundWrongDate = false;
		while ((line = reader.readLine()) != null) {
		    line = line.trim();
		    // log.debug(line);
		    if (line.equals("") || line.startsWith("#")) {
			// comments or empty line. Skip

		    } else if (line
			    .indexOf(BasicMetaDataCols.DATE_NAME.getColName()) > -1) {
			Report report = new Report();
			importReport(reader, report, line, delimiter);
			// Add the new report to the list of reports
			reports.add(report);
			foundWrongDate = false;
		    } else {
			if (!foundWrongDate) {
			    foundWrongDate = true;
			    log.warn("Expected 'Date' but found unknown property ["
				    + line + "]. Report discarded.");
			}
		    }
		}
	    } catch (IOException ex) {
		log.error(ex, ex);
	    } finally {
		try {
		    if (reader != null)
			reader.close();
		} catch (IOException ex) {
		    log.error(ex, ex);
		}
	    }
	}
	return reports;
    }

    /**
     * Imports a report record
     * 
     * @param reader
     *            the buffered reader associated to the file
     * @param report
     *            the object containing the observation to import
     * @param line
     *            the current line parsed in the file (the first line of the
     *            record)
     * @param delimiter
     *            the field delimiter
     * @throws IOException
     *             if reader cannot read the observation
     */
    @Override
    protected void importReport(BufferedReader reader, Report report,
	    String line, String delimiter) throws IOException {
	// log.debug(line);
	// copy the first line
	values = line.split(delimiter);
	// clean the field values if containing quotes at the beginning or end
	cleanFields();
	metaEntry = new String[BasicMetaDataCols.values().length];
	Arrays.fill(metaEntry, "");
	if (values.length == 2) {
	    setMetaData(BasicMetaDataCols.DATE_NAME);
	}
	// Read the other lines for this observation
	while ((line = reader.readLine()) != null) {
	    line = line.trim();
	    values = line.split(delimiter);
	    // clean the field values if containing quotes at the beginning or
	    // end
	    cleanFields();
	    if (values.length == 0 || line.equals("")) {
		return;
	    }
	    if (values.length == 1) {
		// We also accept naked eye observations. Therefore, the only
		// required value
		// is the date.
		values = (line + '\t' + " ").split(delimiter);
	    }
	    if (values.length == 2) {

		if (setMetaData(BasicMetaDataCols.SEEING_NAME)
			|| setMetaData(BasicMetaDataCols.TRANSPARENCY_NAME)
			|| setMetaData(BasicMetaDataCols.TELESCOPES_NAME)) {
		    // do nothing. || is faster than processing &&
		} else {
		    log.warn("Report:"
			    + metaEntry[BasicMetaDataCols.DATE_NAME.ordinal()]
			    + ". Unknown property [" + values[0] + ":"
			    + values[1] + "]. Property discarded.");
		}

	    } else if (values.length == 4) {

		report.addMetaData(metaEntry);

		if (values[0].toLowerCase().equals(
			BasicDataCols.TARGET_NAME.getColName().toLowerCase())
			&& values[1].toLowerCase().equals(
				BasicDataCols.TYPE_NAME.getColName().toLowerCase())
			&& values[2].toLowerCase().equals(
				BasicDataCols.POWER_NAME.getColName().toLowerCase())
			&& values[3].toLowerCase().equals(
				BasicDataCols.NOTES_NAME.getColName().toLowerCase())) {

		    String[] targetEntry;
		    while ((line = reader.readLine()) != null) {
			line = line.trim();
			values = line.split(delimiter);
			// clean the field values if containing quotes at the
			// beginning or end
			cleanFields();
			if (line.equals("")) {
			    return;
			}
			if (values.length != 4) {
			    log.warn("Report:"
				    + metaEntry[BasicMetaDataCols.DATE_NAME
					    .ordinal()]
				    + ". Malformed target [" + line
				    + "]. Target discarded.");
			    break;
			}
			targetEntry = new String[BasicDataCols.values().length];
			Arrays.fill(targetEntry, "");
			targetEntry[BasicDataCols.TARGET_NAME.ordinal()] = values[0];
			log.debug(BasicDataCols.TARGET_NAME + "=" + values[0]);
			targetEntry[BasicDataCols.TYPE_NAME.ordinal()] = values[1];
			log.debug(BasicDataCols.TYPE_NAME + "=" + values[1]);
			targetEntry[BasicDataCols.POWER_NAME.ordinal()] = values[2];
			log.debug(BasicDataCols.POWER_NAME + "=" + values[2]);
			targetEntry[BasicDataCols.NOTES_NAME.ordinal()] = values[3]
				.replace("%", "\\%").replace("&", " and ");
			log.debug(BasicDataCols.NOTES_NAME + "=" + values[3]);
			report.addData(targetEntry);
		    }
		} else {
		    log.warn("Report:"
			    + metaEntry[BasicMetaDataCols.DATE_NAME.ordinal()]
			    + ". Unknown property [" + values[0] + " "
			    + values[1] + " " + values[2] + " " + values[3]
			    + " " + values[4] + "]");
		}
	    } else {
		log.warn("Report:"
			+ metaEntry[BasicMetaDataCols.DATE_NAME.ordinal()]
			+ ". Malformed property [" + line
			+ "]. Property discarded.");
	    }
	}
    }

    /** Set the meta data */
    private boolean setMetaData(BasicMetaDataCols column) {
	if (values[0].toLowerCase().equals(column.getColName().toLowerCase())) {
	    metaEntry[column.ordinal()] = values[1];
	    log.debug(column + "=" + values[1]);
	    return true;
	}
	return false;
    }

    /**
     * Removes single or double quotes at the beginning and at the end of each
     * field if these are present. This can be the case for .csv files.
     */
    private void cleanFields() {
	for (int i = 0; i < values.length; i++) {
	    if (values[i].startsWith("\"") || values[i].startsWith("\'")) {
		values[i] = values[i].substring(1);
	    }
	    if (values[i].endsWith("\"") || values[i].endsWith("\'")) {
		values[i] = values[i].substring(0, values[i].length() - 1);
	    }
	    values[i] = values[i].trim();
	}
    }

}
