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
package org.astrojournal.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.observation.AJObservation;
import org.astrojournal.observation.AJObservationItem;

/**
 * The parser for AstroJournal. It imports tab separated value (tsv or csv)
 * files containing the observations.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 28/05/2015
 */
public class AJTabSeparatedValueImporter extends AJImporter {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(AJTabSeparatedValueImporter.class);

    /** Default constructor */
    public AJTabSeparatedValueImporter() {
	super();
	System.out.println("Importing observation files:");
    }

    /**
     * Imports the observation data stored in a file.
     * 
     * @param file
     *            The file to parse (either CSV or TSV file, separated by TAB
     *            delimiter).
     * @return a list of AJObservation objects
     */
    @Override
    public ArrayList<AJObservation> importObservations(File file) {
	ArrayList<AJObservation> observations = new ArrayList<AJObservation>();
	if (file.isFile()) {

	    // whether this is tsv or csv it does not matter as long as fields
	    // are
	    // separated by a TAB character
	    String delimiter;
	    if (file.getName().endsWith(".tsv")) {
		delimiter = "\t";
	    } else if (file.getName().endsWith(".csv")) {
		delimiter = "\t";
	    } else {
		System.err
			.println("input files must be either .tsv or .csv . Field delimiter must be a TAB");
		return observations;
	    }

	    // Get the current file name.
	    String rawFilename = file.getName();
	    System.out.println("\t" + rawFilename);
	    // Create a buffered reader to read the file
	    BufferedReader reader = null;
	    try {
		reader = new BufferedReader(new FileReader(file));
		// reader = new BufferedReader(new FileReader(new
		// File(rawReportsFolder,
		// rawFilename)));
		String line;
		// Read all lines
		while ((line = reader.readLine()) != null) {
		    log.debug(line);
		    if (line.indexOf(AJTabSeparatedValueImporter
			    .getInitialKeyword()) > -1) {
			AJObservation obs = new AJObservation();
			// this should receive (obs, rawReportsFolder) as input
			// instead of
			// (obs, line, reader) and manage the reader thing
			// internally.
			// import a new observation
			importObservation(reader, obs, line, delimiter);
			// Add the new observation to the list of observations
			observations.add(obs);
		    }
		} // end while
	    } catch (IOException ex) {
		System.err.println(ex);
	    } finally {
		try {
		    if (reader != null)
			reader.close();
		} catch (IOException ex) {
		}
	    }
	} // end if
	return observations;
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
	}
    }

    /**
     * Imports an observation record
     * 
     * @param reader
     *            the buffered reader associated to the file
     * @param obs
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
    protected void importObservation(BufferedReader reader, AJObservation obs,
	    String line, String delimiter) throws IOException {
	log.debug(line);
	// copy the first line
	values = line.split(delimiter);
	// clean the field values if containing quotes at the beginning or end
	cleanFields();
	log.debug("Line length (A): " + values.length);
	if (values.length == 2 && values[0].equals(AJObservation.DATE_NAME)) {
	    obs.setDate(values[1]);
	    log.debug("values[0]" + values[0] + " values[1]=" + values[1]);
	}
	// Read the other lines for this observation
	while ((line = reader.readLine()) != null) {
	    values = line.split(delimiter);
	    // clean the field values if containing quotes at the beginning or
	    // end
	    cleanFields();
	    log.debug("Line length (B): " + values.length);
	    if (values.length == 0 || values[0].equals("")) {
		break;
	    }
	    if (values.length == 2) {
		if (values[0].equals(AJObservation.TIME_NAME)) {
		    obs.setTime(values[1]);
		    log.debug("values[0]==" + values[0] + " values[1]="
			    + values[1]);
		} else if (values[0].equals(AJObservation.LOCATION_NAME)) {
		    obs.setLocation(values[1]);
		    log.debug("values[0]==" + values[0] + " values[1]="
			    + values[1]);
		} else if (values[0].equals(AJObservation.ALTITUDE_NAME)) {
		    obs.setAltitude(values[1]);
		    log.debug("values[0]==" + values[0] + " values[1]="
			    + values[1]);
		} else if (values[0].equals(AJObservation.TEMPERATURE_NAME)) {
		    obs.setTemperature(values[1]);
		    log.debug("values[0]==" + values[0] + " values[1]="
			    + values[1]);
		} else if (values[0].equals(AJObservation.SEEING_NAME)) {
		    obs.setSeeing(values[1]);
		    log.debug("values[0]==" + values[0] + " values[1]="
			    + values[1]);
		} else if (values[0].equals(AJObservation.TRANSPARENCY_NAME)) {
		    obs.setTransparency(values[1]);
		    log.debug("values[0]==" + values[0] + " values[1]="
			    + values[1]);
		} else if (values[0].equals(AJObservation.DARKNESS_NAME)) {
		    obs.setSkyDarkness(values[1]);
		    log.debug("values[0]==" + values[0] + " values[1]="
			    + values[1]);
		} else if (values[0].equals(AJObservation.TELESCOPES_NAME)) {
		    obs.setTelescopes(values[1]);
		    log.debug("values[0]==" + values[0] + " values[1]="
			    + values[1]);
		} else if (values[0].equals(AJObservation.EYEPIECES_NAME)) {
		    obs.setEyepieces(values[1]);
		    log.debug("values[0]==" + values[0] + " values[1]="
			    + values[1]);
		} else if (values[0].equals(AJObservation.FILTERS_NAME)) {
		    obs.setFilters(values[1]);
		    log.debug("values[0]==" + values[0] + " values[1]="
			    + values[1]);
		}
	    } else if (values.length == 5
		    && values[0].equals(AJObservationItem.TARGET_NAME)
		    && values[1].equals(AJObservationItem.CONSTELLATION_NAME)
		    && values[2].equals(AJObservationItem.TYPE_NAME)
		    && values[3].equals(AJObservationItem.POWER_NAME)
		    && values[4].equals(AJObservationItem.NOTES_NAME)) {
		while ((line = reader.readLine()) != null) {
		    values = line.split(delimiter);
		    // clean the field values if containing quotes at the
		    // beginning or end
		    cleanFields();
		    if (values.length != 5 || values[0].equals("")) {
			break;
		    }
		    log.debug(line);
		    log.debug("Line length (C): " + values.length);
		    log.debug(values[4]);
		    AJObservationItem item = new AJObservationItem();
		    item.setTarget(values[0]);
		    item.setConstellation(values[1]);
		    item.setType(values[2]);
		    item.setPower(values[3]);
		    item.setNotes(values[4].replace("%", "\\%"));
		    obs.addObservationItem(item);
		}
	    }
	}
    }
}
