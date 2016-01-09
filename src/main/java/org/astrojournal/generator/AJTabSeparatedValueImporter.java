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
			.println("Input files must be either .tsv or .csv . Field delimiter must be a TAB");
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
		boolean foundWrongDate = false;
		while ((line = reader.readLine()) != null) {
		    // log.debug(line);
		    if (line.trim().equals("") || line.trim().startsWith("#")) {
			// comments or empty line. Skip

		    } else if (line.indexOf(AJTabSeparatedValueImporter
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
			foundWrongDate = false;

		    } else {
			if (!foundWrongDate) {
			    foundWrongDate = true;
			    log.warn("Expected 'Date' but found unknown property ["
				    + line.trim() + "]. Report discarded.");
			}
		    }

		} // end while
	    } catch (IOException ex) {
		ex.printStackTrace();
	    } finally {
		try {
		    if (reader != null)
			reader.close();
		} catch (IOException ex) {
		    ex.printStackTrace();
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
	    values[i] = values[i].trim();
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
	// log.debug(line);
	// copy the first line
	values = line.split(delimiter);
	// clean the field values if containing quotes at the beginning or end
	cleanFields();
	if (values.length == 2) {
	    if (values[0].toLowerCase().equals(
		    AJObservation.DATE_NAME.toLowerCase())) {
		obs.setDate(values[1]);
		log.debug(AJObservation.DATE_NAME + "=" + values[1]);
	    }
	}
	// Read the other lines for this observation
	while ((line = reader.readLine()) != null) {
	    values = line.split(delimiter);
	    // clean the field values if containing quotes at the beginning or
	    // end
	    cleanFields();
	    if (values.length == 0 || line.trim().equals("")) {
		return;
	    }
	    if (values.length == 2) {
		if (values[0].toLowerCase().equals(
			AJObservation.TIME_NAME.toLowerCase())) {
		    obs.setTime(values[1]);
		    log.debug(AJObservation.TIME_NAME + "=" + values[1]);
		} else if (values[0].toLowerCase().equals(
			AJObservation.LOCATION_NAME.toLowerCase())) {
		    obs.setLocation(values[1]);
		    log.debug(AJObservation.LOCATION_NAME + "=" + values[1]);
		} else if (values[0].toLowerCase().equals(
			AJObservation.ALTITUDE_NAME.toLowerCase())) {
		    obs.setAltitude(values[1]);
		    log.debug(AJObservation.ALTITUDE_NAME + "=" + values[1]);
		} else if (values[0].toLowerCase().equals(
			AJObservation.TEMPERATURE_NAME.toLowerCase())) {
		    obs.setTemperature(values[1]);
		    log.debug(AJObservation.TEMPERATURE_NAME + "=" + values[1]);
		} else if (values[0].toLowerCase().equals(
			AJObservation.SEEING_NAME.toLowerCase())) {
		    obs.setSeeing(values[1]);
		    log.debug(AJObservation.SEEING_NAME + "=" + values[1]);
		} else if (values[0].toLowerCase().equals(
			AJObservation.TRANSPARENCY_NAME.toLowerCase())) {
		    obs.setTransparency(values[1]);
		    log.debug(AJObservation.TRANSPARENCY_NAME + "=" + values[1]);
		} else if (values[0].toLowerCase().equals(
			AJObservation.DARKNESS_NAME.toLowerCase())) {
		    obs.setSkyDarkness(values[1]);
		    log.debug(AJObservation.DARKNESS_NAME + "=" + values[1]);
		} else if (values[0].toLowerCase().equals(
			AJObservation.TELESCOPES_NAME.toLowerCase())) {
		    obs.setTelescopes(values[1]);
		    log.debug(AJObservation.TELESCOPES_NAME + "=" + values[1]);
		} else if (values[0].toLowerCase().equals(
			AJObservation.EYEPIECES_NAME.toLowerCase())) {
		    obs.setEyepieces(values[1]);
		    log.debug(AJObservation.EYEPIECES_NAME + "=" + values[1]);
		} else if (values[0].toLowerCase().equals(
			AJObservation.FILTERS_NAME.toLowerCase())) {
		    obs.setFilters(values[1]);
		    log.debug(AJObservation.FILTERS_NAME + "=" + values[1]);
		} else {
		    log.warn("Report:" + obs.getDate() + ". Unknown property ["
			    + values[0] + ":" + values[1]
			    + "]. Property discarded.");
		}
	    } else if (values.length == 5) {

		if (values[0].toLowerCase().equals(
			AJObservationItem.TARGET_NAME.toLowerCase())
			&& values[1].toLowerCase().equals(
				AJObservationItem.CONSTELLATION_NAME
					.toLowerCase())
			&& values[2].toLowerCase().equals(
				AJObservationItem.TYPE_NAME.toLowerCase())
			&& values[3].toLowerCase().equals(
				AJObservationItem.POWER_NAME.toLowerCase())
			&& values[4].toLowerCase().equals(
				AJObservationItem.NOTES_NAME.toLowerCase())) {
		    while ((line = reader.readLine()) != null) {
			values = line.split(delimiter);
			// clean the field values if containing quotes at the
			// beginning or end
			cleanFields();
			if (line.trim().equals("")) {
			    return;
			}
			if (values.length != 5) {
			    log.warn("Report:" + obs.getDate()
				    + ". Malformed target [" + line.trim()
				    + "]. Target discarded.");
			    break;
			}
			// log.debug(line);
			log.debug(AJObservationItem.TARGET_NAME + "="
				+ values[0]);
			AJObservationItem item = new AJObservationItem();
			item.setTarget(values[0]);
			item.setConstellation(values[1]);
			item.setType(values[2]);
			item.setPower(values[3]);
			item.setNotes(values[4].replace("%", "\\%"));
			obs.addObservationItem(item);
		    }
		} else {
		    log.warn("Report:" + obs.getDate() + ". Unknown property ["
			    + values[0] + " " + values[1] + " " + values[2]
			    + " " + values[3] + " " + values[4] + "]");
		}
	    } else {
		log.warn("Report:" + obs.getDate() + ". Malformed property ["
			+ line.trim() + "]. Property discarded.");
	    }
	}
    }
}
