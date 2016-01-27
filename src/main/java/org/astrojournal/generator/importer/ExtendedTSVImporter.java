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
package org.astrojournal.generator.importer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.reportdata.ExtendedReportHeader;
import org.astrojournal.generator.reportdata.ExtendedReportItem;
import org.astrojournal.utilities.filefilters.TabSeparatedValueRawReportFilter;

/**
 * The parser for AstroJournal. It imports tab separated value (tsv or csv)
 * files containing the reports.
 * 
 * @author Piero Dalle Pezze
 * @version 0.4
 * @since 28/05/2015
 */
public class ExtendedTSVImporter extends Importer {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(ExtendedTSVImporter.class);

    /**
     * Default constructor
     */
    public ExtendedTSVImporter() {
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
	if (file.isFile()
		&& new TabSeparatedValueRawReportFilter().accept(file)) {

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

		    } else if (line.indexOf(ExtendedTSVImporter
			    .getInitialKeyword()) > -1) {
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
	ExtendedReportHeader reportHeader = new ExtendedReportHeader();
	if (values.length == 2) {
	    if (values[0].toLowerCase().equals(
		    ExtendedReportHeader.DATE_NAME.toLowerCase())) {
		reportHeader.setDate(values[1]);
		report.setDataHeader(reportHeader);
		log.debug(ExtendedReportHeader.DATE_NAME + "=" + values[1]);
	    }
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
		if (values[0].toLowerCase().equals(
			ExtendedReportHeader.TIME_NAME.toLowerCase())) {
		    reportHeader.setTime(values[1]);
		    log.debug(ExtendedReportHeader.TIME_NAME + "=" + values[1]);
		} else if (values[0].toLowerCase().equals(
			ExtendedReportHeader.LOCATION_NAME.toLowerCase())) {
		    reportHeader.setLocation(values[1]);
		    log.debug(ExtendedReportHeader.LOCATION_NAME + "="
			    + values[1]);
		} else if (values[0].toLowerCase().equals(
			ExtendedReportHeader.ALTITUDE_NAME.toLowerCase())) {
		    reportHeader.setAltitude(values[1]);
		    log.debug(ExtendedReportHeader.ALTITUDE_NAME + "="
			    + values[1]);
		} else if (values[0].toLowerCase().equals(
			ExtendedReportHeader.TEMPERATURE_NAME.toLowerCase())) {
		    reportHeader.setTemperature(values[1]);
		    log.debug(ExtendedReportHeader.TEMPERATURE_NAME + "="
			    + values[1]);
		} else if (values[0].toLowerCase().equals(
			ExtendedReportHeader.SEEING_NAME.toLowerCase())) {
		    reportHeader.setSeeing(values[1]);
		    log.debug(ExtendedReportHeader.SEEING_NAME + "="
			    + values[1]);
		} else if (values[0].toLowerCase().equals(
			ExtendedReportHeader.TRANSPARENCY_NAME.toLowerCase())) {
		    reportHeader.setTransparency(values[1]);
		    log.debug(ExtendedReportHeader.TRANSPARENCY_NAME + "="
			    + values[1]);
		} else if (values[0].toLowerCase().equals(
			ExtendedReportHeader.DARKNESS_NAME.toLowerCase())) {
		    reportHeader.setSkyDarkness(values[1]);
		    log.debug(ExtendedReportHeader.DARKNESS_NAME + "="
			    + values[1]);
		} else if (values[0].toLowerCase().equals(
			ExtendedReportHeader.TELESCOPES_NAME.toLowerCase())) {
		    reportHeader.setTelescopes(values[1]);
		    log.debug(ExtendedReportHeader.TELESCOPES_NAME + "="
			    + values[1]);
		} else if (values[0].toLowerCase().equals(
			ExtendedReportHeader.EYEPIECES_NAME.toLowerCase())) {
		    reportHeader.setEyepieces(values[1]);
		    log.debug(ExtendedReportHeader.EYEPIECES_NAME + "="
			    + values[1]);
		} else if (values[0].toLowerCase().equals(
			ExtendedReportHeader.FILTERS_NAME.toLowerCase())) {
		    reportHeader.setFilters(values[1]);
		    log.debug(ExtendedReportHeader.FILTERS_NAME + "="
			    + values[1]);
		} else {
		    log.warn("Report:" + reportHeader.getDate()
			    + ". Unknown property [" + values[0] + ":"
			    + values[1] + "]. Property discarded.");
		}
	    } else if (values.length == 5) {

		if (values[0].toLowerCase().equals(
			ExtendedReportItem.TARGET_NAME.toLowerCase())
			&& values[1].toLowerCase().equals(
				ExtendedReportItem.CONSTELLATION_NAME
					.toLowerCase())
			&& values[2].toLowerCase().equals(
				ExtendedReportItem.TYPE_NAME.toLowerCase())
			&& values[3].toLowerCase().equals(
				ExtendedReportItem.POWER_NAME.toLowerCase())
			&& values[4].toLowerCase().equals(
				ExtendedReportItem.NOTES_NAME.toLowerCase())) {
		    while ((line = reader.readLine()) != null) {
			line = line.trim();
			values = line.split(delimiter);
			// clean the field values if containing quotes at the
			// beginning or end
			cleanFields();
			if (line.equals("")) {
			    return;
			}
			if (values.length != 5) {
			    log.warn("Report:" + reportHeader.getDate()
				    + ". Malformed target [" + line
				    + "]. Target discarded.");
			    break;
			}
			ExtendedReportItem reportItem = new ExtendedReportItem();
			reportItem.setTarget(values[0]);
			log.debug(ExtendedReportItem.TARGET_NAME + "="
				+ values[0]);
			reportItem.setConstellation(values[1]);
			log.debug(ExtendedReportItem.CONSTELLATION_NAME + "="
				+ values[1]);
			reportItem.setType(values[2]);
			log.debug(ExtendedReportItem.TYPE_NAME + "="
				+ values[2]);
			reportItem.setPower(values[3]);
			log.debug(ExtendedReportItem.POWER_NAME + "="
				+ values[3]);
			reportItem.setNotes(values[4].replace("%", "\\%")
				.replace("&", " and "));
			log.debug(ExtendedReportItem.NOTES_NAME + "="
				+ values[4]);
			report.addReportItem(reportItem);
		    }
		} else {
		    log.warn("Report:" + reportHeader.getDate()
			    + ". Unknown property [" + values[0] + " "
			    + values[1] + " " + values[2] + " " + values[3]
			    + " " + values[4] + "]");
		}
	    } else {
		log.warn("Report:" + reportHeader.getDate()
			+ ". Malformed property [" + line
			+ "]. Property discarded.");
	    }
	}
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
