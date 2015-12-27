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

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.astrojournal.configuration.AJConfig;
import org.astrojournal.observation.AJObservation;

/**
 * This class automatically generates astro journal documents.
 * 
 * @author Piero Dalle Pezze
 * @version 0.8
 * @since 12/04/2015
 */
public class AJGenerator {

    /** The log associated to this class */
    private static Logger log = Logger.getLogger(AJGenerator.class);

    /** The list of observations. */
    private ArrayList<AJObservation> observations = new ArrayList<AJObservation>(
	    1000);

    /** True if the observations have been processed. */
    private boolean observationsProcessed = false;

    /** Default constructor */
    public AJGenerator() {
    }

    /**
     * It generates the latex files for AstroJournal.
     * 
     * @return true if the observations sorted by date and by target have been
     *         exported to Latex correctly
     */
    public boolean generateJournals() {

	if (AJConfig.getInstance().isShowLicenseAtStart()) {
	    showLicense();
	}

	if (AJConfig.getInstance().isShowConfigurationAtStart()) {
	    showConfiguration();
	}

	if (!importObservations()) {
	    log.warn(
		    "raw observation file is not valid. Cannot generate Latex code for the observations.");
	    return false;
	}
	boolean exportedByDate = true, exportedByTarget = true,
		exportedByConstellation = true, exportedByDateSGL = true;
	exportedByDate = generateJournalByDate();
	exportedByTarget = generateJournalByTarget();
	exportedByConstellation = generateJournalByConstellation();
	exportedByDateSGL = generateJournalByDateSGL();
	return exportedByDate && exportedByTarget && exportedByConstellation
		&& exportedByDateSGL;
    }

    /**
     * Print the license for AstroJournal.
     */
    public void showLicense() {
	System.out.println(AJConfig.getInstance().printLicense());
    }

    /**
     * Print the configuration for AstroJournal.
     */
    public void showConfiguration() {
	System.out.println(AJConfig.getInstance().printConfiguration());
    }

    /**
     * Generate the Latex document sorted by date
     * 
     * @return true if the observations sorted by date have been exported to
     *         Latex correctly
     */
    public boolean generateJournalByDate() {
	if (!importObservations()) {
	    log.warn(
		    "raw observation file is not valid. Cannot generate Latex code for the observations.");
	    return false;
	}
	AJExporter ajExporterByDate = new AJExporterByDate(
		AJConfig.getInstance().getAJFilesLocation());
	// export the imported observation by date to Latex
	System.out.println("\nExporting observation by date:");
	boolean resultByDate = ajExporterByDate.exportObservations(observations,
		AJConfig.getInstance().getLatexReportsFolderByDate());
	ajExporterByDate.generateJournal(
		AJConfig.getInstance().getLatexReportsFolderByDate(),
		AJConfig.HEADER_BY_DATE_FILENAME,
		AJConfig.REPORT_BY_DATE_FILENAME,
		AJConfig.FOOTER_BY_DATE_FILENAME);
	return resultByDate;
    }

    /**
     * Generate the txt document for SGL reports sorted by date
     * 
     * @return true if the observations sorted by date have been exported to
     *         Latex correctly
     */
    public boolean generateJournalByDateSGL() {
	if (!importObservations()) {
	    log.warn(
		    "raw observation file is not valid. Cannot generate txt code for the observations.");
	    return false;
	}
	AJExporter ajExporterByDateSGL = new AJExporterByDateSGL(
		AJConfig.getInstance().getAJFilesLocation());
	// export the imported observation by date to txt
	System.out.println("\nExporting observation by date for SGL:");
	boolean resultByDateSGL = ajExporterByDateSGL.exportObservations(
		observations,
		AJConfig.getInstance().getSglReportsFolderByDate());
	ajExporterByDateSGL.generateJournal(
		AJConfig.getInstance().getSglReportsFolderByDate(), "",
		AJConfig.SGL_REPORT_BY_DATE_FILENAME, "");
	return resultByDateSGL;
    }

    /**
     * Generate the Latex document sorted by target.
     * 
     * @return true if the observations sorted by target have been exported to
     *         Latex correctly
     */
    public boolean generateJournalByTarget() {
	if (!importObservations()) {
	    log.warn(
		    "raw observation file is not valid. Cannot generate Latex code for the observations.");
	    return false;
	}
	AJExporter ajExporterByTarget = new AJExporterByTarget(
		AJConfig.getInstance().getAJFilesLocation());
	// export the imported observation by target to Latex
	System.out.println("\nExporting observation by target:");
	boolean resultByTarget = ajExporterByTarget.exportObservations(
		observations,
		AJConfig.getInstance().getLatexReportsFolderByTarget());
	ajExporterByTarget.generateJournal(
		AJConfig.getInstance().getLatexReportsFolderByTarget(),
		AJConfig.HEADER_BY_TARGET_FILENAME,
		AJConfig.REPORT_BY_TARGET_FILENAME,
		AJConfig.FOOTER_BY_TARGET_FILENAME);
	return resultByTarget;
    }

    /**
     * Generate the Latex document sorted by constellation.
     * 
     * @return true if the observations sorted by target have been exported to
     *         Latex correctly
     */
    public boolean generateJournalByConstellation() {
	if (!importObservations()) {
	    log.warn(
		    "raw observation file is not valid. Cannot generate Latex code for the observations.");
	    return false;
	}
	AJExporter ajExporterByConstellation = new AJExporterByConstellation(
		AJConfig.getInstance().getAJFilesLocation());
	// export the imported observation by constellation to Latex
	System.out.println("\nExporting observation by constellation:");
	boolean resultByConstellation = ajExporterByConstellation
		.exportObservations(observations, AJConfig.getInstance()
			.getLatexReportsFolderByConstellation());
	ajExporterByConstellation.generateJournal(
		AJConfig.getInstance().getLatexReportsFolderByConstellation(),
		AJConfig.HEADER_BY_CONSTELLATION_FILENAME,
		AJConfig.REPORT_BY_CONSTELLATION_FILENAME,
		AJConfig.FOOTER_BY_CONSTELLATION_FILENAME);
	return resultByConstellation;
    }

    /**
     * Import the observations.
     * 
     * @return true if the procedure succeeds, false otherwise.
     */
    private boolean importObservations() {
	if (!observationsProcessed) {
	    String rawReportPath = AJConfig.getInstance().getAJFilesLocation()
		    .getAbsolutePath() + File.separator
		    + AJConfig.getInstance().getRawReportsFolder();
	    File[] files = new File(rawReportPath).listFiles();
	    if (files == null) {
		log.warn("Folder " + rawReportPath + " not found");
		return false;
	    }
	    AJImporter ajImporter = new AJTabSeparatedValueImporter();
	    observations.addAll(ajImporter.importObservations(files));
	    observationsProcessed = true;
	}
	return observationsProcessed;
    }
}
