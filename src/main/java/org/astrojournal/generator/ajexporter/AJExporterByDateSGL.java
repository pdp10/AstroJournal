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
package org.astrojournal.generator.ajexporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.AJConfig;
import org.astrojournal.generator.observation.AJObservation;
import org.astrojournal.generator.observation.AJObservationItem;

/**
 * Exports an AstroJournal observation to txt for Stargazers Lounge reports.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 11/09/2015
 */
public class AJExporterByDateSGL extends AJExporter {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(AJExporterByDateSGL.class);

    /**
     * Default constructor
     */
    public AJExporterByDateSGL() {
	super();
    }

    /**
     * Constructor
     * 
     * @param ajFilesLocation
     */
    public AJExporterByDateSGL(File ajFilesLocation) {
	super(ajFilesLocation);
    }

    /**
     * Generate the Latex document sorted by date for the forum StargazersLounge
     * 
     * @param observations
     *            the list of observations to exportObservation
     * @return true if the observations sorted by date have been exported to txt
     *         correctly
     */
    @Override
    public boolean generateJournal(ArrayList<AJObservation> observations) {
	// export the imported observation by date to txt
	log.info("");
	log.info("Exporting observations by date for SGL:");
	boolean resultByDateSGL = exportObservations(observations, AJConfig
		.getInstance().getSglReportsFolderByDate());
	generateJournal(AJConfig.getInstance().getSglReportsFolderByDate(), "",
		AJConfig.SGL_REPORT_BY_DATE_FILENAME, "");
	return resultByDateSGL;
    }

    /**
     * Generate a txt document sorting the observation by decreasing date
     * 
     * @param reportsFolderByDate
     *            the directory containing the single observations by date
     *            (output)
     * @param headerByDate
     *            the header code (by date)
     * @param mainByDate
     *            the main body code (by date)
     * @param footerByDate
     *            the footer code (by date)
     */
    @Override
    public void generateJournal(String reportsFolderByDate,
	    String headerByDate, String mainByDate, String footerByDate) {
	Writer writerByDate = null;
	try {
	    writerByDate = new BufferedWriter(new OutputStreamWriter(
		    new FileOutputStream(ajFilesLocation.getAbsolutePath()
			    + File.separator + mainByDate), "utf-8"));
	    // write the Header

	    // write the Body
	    // Write the observation reports
	    // parse each file in the obs folder (sorted by observation
	    // increasing)
	    File[] files = new File(ajFilesLocation.getAbsolutePath()
		    + File.separator + reportsFolderByDate).listFiles();
	    if (files == null) {
		log.warn("Folder " + ajFilesLocation.getAbsolutePath()
			+ File.separator + reportsFolderByDate + " not found");
		return;
	    }
	    Arrays.sort(files, Collections.reverseOrder());
	    // If this pathname does not denote a directory, then listFiles()
	    // returns null.
	    for (File file : files) {
		if (file.isFile() && file.getName().endsWith(".txt")) {
		    // include the file removing the extension .txt
		    try {
			Scanner scanner = new Scanner(file, "UTF-8");
			String text = scanner.useDelimiter("\\A").next();
			scanner.close();
			writerByDate.write(text);
		    } catch (NoSuchElementException e) {
			log.warn(e, e);
		    }
		    writerByDate.write("\n\n\n\n");
		}
	    }

	    // write the Footer

	} catch (IOException ex) {
	    log.warn(
		    "Error when opening the file "
			    + ajFilesLocation.getAbsolutePath()
			    + File.separator + mainByDate, ex);
	} catch (Exception ex) {
	    log.error(ex, ex);
	} finally {
	    try {
		if (writerByDate != null)
		    writerByDate.close();
	    } catch (Exception ex) {
		log.error(ex, ex);
	    }
	}
    }

    /**
     * Exports an observation record to txt
     * 
     * @param observations
     *            the list of observations to exportObservation
     * @param reportsByDateFolder
     *            the folder to write the observation in.
     * @return true if the observations are exported
     */
    @Override
    public boolean exportObservations(ArrayList<AJObservation> observations,
	    String reportsByDateFolder) {

	AJObservation obs = null;
	int nObservations = observations.size();
	boolean result = true;

	for (int i = 0; i < nObservations; i++) {
	    obs = observations.get(i);

	    Writer text = null;

	    String filenameOut = obs.getDate();
	    filenameOut = filenameOut.substring(6, 10)
		    + filenameOut.substring(3, 5) + filenameOut.substring(0, 2);
	    // Add an additional char if this is present. This is the case in
	    // which
	    // more than one observation per day is done.
	    if (obs.getDate().length() == 11) {
		filenameOut = filenameOut + obs.getDate().charAt(10);
	    }

	    ArrayList<AJObservationItem> observationItems = obs
		    .getObservationItems();
	    try {
		text = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(new File(
				ajFilesLocation.getAbsolutePath()
					+ File.separator + reportsByDateFolder,
				"obs" + filenameOut + ".txt")), "utf-8"));

		text.write(AJObservation.DATE_NAME + " " + obs.getDate() + "\n");
		text.write(AJObservation.TIME_NAME + " " + obs.getTime() + "\n");
		text.write(AJObservation.LOCATION_NAME + " "
			+ obs.getLocation() + "\n");
		text.write(AJObservation.ALTITUDE_NAME + " "
			+ obs.getAltitude() + "\n");
		text.write(AJObservation.TEMPERATURE_NAME + " "
			+ obs.getTemperature() + "\n");
		text.write(AJObservation.SEEING_NAME + " " + obs.getSeeing()
			+ "\n");
		text.write(AJObservation.TRANSPARENCY_NAME + " "
			+ obs.getTransparency() + "\n");
		// This requires a SQM-L meter.
		if (!obs.getDarkness().equals("")) {
		    text.write(AJObservation.DARKNESS_NAME + " "
			    + obs.getDarkness() + "\n");
		}
		text.write(AJObservation.TELESCOPES_NAME + " "
			+ obs.getTelescopes() + "\n");
		text.write(AJObservation.EYEPIECES_NAME + " "
			+ obs.getEyepieces() + "\n");
		text.write(AJObservation.FILTERS_NAME + " " + obs.getFilters()
			+ "\n\n");

		for (AJObservationItem item : observationItems) {
		    log.debug("Target " + item.getTarget());
		    text.write(item.getTarget() + " " + item.getConstellation()
			    + " " + item.getType() + " " + item.getPower()
			    + "\n" + item.getNotes() + "\n\n");
		}
		log.info("\tExported report " + obs.getDate() + " ("
			+ observationItems.size() + " targets)");
	    } catch (IOException ex) {
		log.error(
			"Error when opening the file "
				+ ajFilesLocation.getAbsolutePath()
				+ File.separator + filenameOut, ex);
		result = false;
	    } catch (Exception ex) {
		log.error(ex, ex);
		result = false;
	    } finally {
		try {
		    if (text != null)
			text.close();
		} catch (Exception ex) {
		    log.error(ex, ex);
		}
	    }
	}

	return result;
    }

}
