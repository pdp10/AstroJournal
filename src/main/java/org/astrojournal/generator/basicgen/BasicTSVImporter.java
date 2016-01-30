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
import java.io.IOException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.absgen.TSVImporter;

/**
 * The parser for AstroJournal. It imports tab separated value (tsv or csv)
 * files containing the reports. This is a basic TSV importer which uses
 * BasicMetaDataCols and BasicDataCols enum types for column import.
 * 
 * @author Piero Dalle Pezze
 * @version 0.4
 * @since 28/05/2015
 */
public class BasicTSVImporter extends TSVImporter {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(BasicTSVImporter.class);

    /**
     * Default constructor
     */
    public BasicTSVImporter() {
	super();
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

	    } else if (values.length >= 4) {

		report.addMetaData(metaEntry);

		if (values[0].toLowerCase().equals(
			BasicDataCols.TARGET_NAME.getColName().toLowerCase())
			&& values[1].toLowerCase().equals(
				BasicDataCols.CONSTELLATION_NAME.getColName()
					.toLowerCase())
			&& values[2].toLowerCase().equals(
				BasicDataCols.TYPE_NAME.getColName()
					.toLowerCase())
			&& values[3].toLowerCase().equals(
				BasicDataCols.POWER_NAME.getColName()
					.toLowerCase())) {

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
			if (values.length < 4) {
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
			targetEntry[BasicDataCols.CONSTELLATION_NAME.ordinal()] = values[1];
			log.debug(BasicDataCols.CONSTELLATION_NAME + "="
				+ values[1]);
			targetEntry[BasicDataCols.TYPE_NAME.ordinal()] = values[2];
			log.debug(BasicDataCols.TYPE_NAME + "=" + values[2]);
			targetEntry[BasicDataCols.POWER_NAME.ordinal()] = values[3];
			log.debug(BasicDataCols.POWER_NAME + "=" + values[3]);
			report.addData(targetEntry);
		    }
		} else {
		    log.warn("Report:"
			    + metaEntry[BasicMetaDataCols.DATE_NAME.ordinal()]
			    + ". Unknown property [" + values[0] + " "
			    + values[1] + " " + values[2] + " " + values[3]
			    + "]");
		}
	    } else {
		log.warn("Report:"
			+ metaEntry[BasicMetaDataCols.DATE_NAME.ordinal()]
			+ ". Discarding line [" + line + "].");
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

}
