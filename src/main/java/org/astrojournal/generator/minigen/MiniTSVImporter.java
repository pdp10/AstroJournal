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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.absgen.TSVImporter;
import org.astrojournal.generator.basicgen.BasicMetaDataCols;

/**
 * The parser for AstroJournal. It imports tab separated value (tsv or csv)
 * files containing the reports.
 * 
 * @author Piero Dalle Pezze
 * @version 0.4
 * @since 28/05/2015
 */
public class MiniTSVImporter extends TSVImporter {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(MiniTSVImporter.class);

    /**
     * Default constructor
     */
    public MiniTSVImporter() {
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
	metaEntry = new String[MiniMetaDataCols.values().length];
	Arrays.fill(metaEntry, "");
	if (values.length == 2) {
	    setMetaData(MiniMetaDataCols.DATE_NAME);
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

	    if (values.length >= 3) {

		report.addMetaData(metaEntry);

		if (values[0].toLowerCase().equals(
			MiniDataCols.TARGET_NAME.getColName().toLowerCase())
			&& values[1].toLowerCase().equals(
				MiniDataCols.CONSTELLATION_NAME.getColName()
					.toLowerCase())
			&& values[2].toLowerCase().equals(
				MiniDataCols.TYPE_NAME.getColName()
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
			if (values.length < 3) {
			    log.warn("Report:"
				    + metaEntry[MiniMetaDataCols.DATE_NAME
					    .ordinal()]
				    + ". Malformed target [" + line
				    + "]. Target discarded.");
			    break;
			}
			targetEntry = new String[MiniDataCols.values().length];
			Arrays.fill(targetEntry, "");
			targetEntry[MiniDataCols.TARGET_NAME.ordinal()] = values[0];
			log.debug(MiniDataCols.TARGET_NAME + "=" + values[0]);
			targetEntry[MiniDataCols.CONSTELLATION_NAME.ordinal()] = values[1];
			log.debug(MiniDataCols.CONSTELLATION_NAME + "="
				+ values[1]);
			targetEntry[MiniDataCols.TYPE_NAME.ordinal()] = values[2];
			log.debug(MiniDataCols.TYPE_NAME + "=" + values[2]);
			report.addData(targetEntry);
		    }
		} else {
		    log.warn("Report:"
			    + metaEntry[MiniMetaDataCols.DATE_NAME.ordinal()]
			    + ". Unknown property [" + values[0] + "]");
		}
	    } else {
		log.warn("Report:"
			+ metaEntry[BasicMetaDataCols.DATE_NAME.ordinal()]
			+ ". Discarding line [" + line + "].");
	    }
	}
    }

    /** Set the meta data */
    private boolean setMetaData(MiniMetaDataCols column) {
	if (values[0].toLowerCase().equals(column.getColName().toLowerCase())) {
	    metaEntry[column.ordinal()] = values[1];
	    log.debug(column + "=" + values[1]);
	    return true;
	}
	return false;
    }

}
