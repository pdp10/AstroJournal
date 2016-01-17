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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.AJConfig;
import org.astrojournal.generator.ajexporter.AJExporter;
import org.astrojournal.generator.ajimporter.AJImporter;
import org.astrojournal.generator.observation.AJObservation;
import org.astrojournal.utilities.ExporterSearcher;
import org.astrojournal.utilities.ImporterSearcher;

/**
 * This class automatically generates astro journal documents.
 * 
 * @author Piero Dalle Pezze
 * @version 0.11
 * @since 12/04/2015
 */
public class AJGenerator {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(AJGenerator.class);

    /** The list of observations. */
    private ArrayList<AJObservation> observations = new ArrayList<AJObservation>();

    /** The list of AJImporter objects. */
    private ArrayList<AJImporter> ajImporters = new ArrayList<AJImporter>();

    /** The list of AJExporter objects. */
    private ArrayList<AJExporter> ajExporters = new ArrayList<AJExporter>();

    /** Default constructor */
    public AJGenerator() {
    }

    /**
     * It generates the files for AstroJournal.
     * 
     * @return true if the observations sorted by date and by target have been
     *         exported correctly
     */
    public boolean generateJournals() {
	AJConfig ajConfig = AJConfig.getInstance();

	if (!ajConfig.isQuiet() && ajConfig.isShowLicenseAtStart()) {
	    showLicense();
	}

	if (!ajConfig.isQuiet() && ajConfig.isShowPDFLatexVersionAtStart()) {
	    if (!showPDFLatexVersion()) {
		return false;
	    }
	}

	if (!ajConfig.isQuiet() && ajConfig.isShowConfigurationAtStart()) {
	    showConfiguration();
	}

	if (!ajImport()) {
	    if (observations.isEmpty()) {
		log.error("No observation was imported. Is the folder "
			+ AJConfig.getInstance().getFilesLocation()
				.getAbsolutePath() + File.separator
			+ AJConfig.getInstance().getRawReportsFolder()
			+ " empty?");
		return false;
	    }
	    log.warn("Some importer failed, but there are still observations which can be processed.");
	}

	if (!ajExport()) {
	    log.warn("Some exporter failed!");
	    return false;
	}

	return true;
    }

    /**
     * Print the license for AstroJournal.
     */
    public void showLicense() {
	log.info(AJConfig.getInstance().printLicense());
    }

    /**
     * Print the version of pdflatex.
     * 
     * @return true if pdflatex is installed, false otherwise
     */
    public boolean showPDFLatexVersion() {
	String pdfLatexVersion = AJConfig.getInstance().printPDFLatexVersion();
	if (pdfLatexVersion.isEmpty()) {
	    return false;
	}
	log.info(pdfLatexVersion);
	return true;
    }

    /**
     * Print the configuration for AstroJournal.
     */
    public void showConfiguration() {
	String[] conf = AJConfig.getInstance().printConfiguration().split("\n");
	for (String str : conf) {
	    log.info(str);
	}
	log.info(System.getProperty("line.separator"));
    }

    /**
     * Returns the imported observations.
     * 
     * @return the observations
     */
    public ArrayList<AJObservation> getObservations() {
	return observations;
    }

    /**
     * Returns the list of AJImporter objects.
     * 
     * @return the AJImporters
     */
    public ArrayList<AJImporter> getAJImporters() {
	return ajImporters;
    }

    /**
     * Returns the list of AJExporter objects.
     * 
     * @return the AJExporters
     */
    public ArrayList<AJExporter> getAJExporters() {
	return ajExporters;
    }

    /**
     * Reset the generator.
     */
    public void reset() {
	resetObservations();
	resetAJImporters();
	resetAJExporters();
    }

    /**
     * Reset the generator observations.
     */
    public void resetObservations() {
	observations = new ArrayList<AJObservation>();
    }

    /**
     * Reset the AJImporters.
     */
    public void resetAJImporters() {
	ajImporters = new ArrayList<AJImporter>();
    }

    /**
     * Reset the AJExporters.
     */
    public void resetAJExporters() {
	ajExporters = new ArrayList<AJExporter>();
    }

    /**
     * Export the observations using the available AJImporters.
     * 
     * @return true if the observations have been imported.
     */
    public boolean ajImport() {
	boolean status = true;
	ArrayList<String> ajImporterNames = ImporterSearcher
		.getImporterFullNameList();
	Collections.sort(ajImporterNames);
	for (String ajImporterName : ajImporterNames) {
	    try {
		log.debug("Loading importer " + ajImporterName);
		Class<?> cls = Class.forName(ajImporterName);
		Object clsInstance = cls.newInstance();
		AJImporter ajImporter = (AJImporter) clsInstance;
		if (!ajImporters.contains(ajImporter)) {
		    ajImporters.add(ajImporter);
		    log.debug("Importer " + ajImporter.getName() + " is loaded");
		} else {
		    log.debug("Importer " + ajImporter.getName()
			    + " was already loaded");
		}
	    } catch (InstantiationException e) {
		status = false;
		log.debug(e);
	    } catch (IllegalAccessException e) {
		status = false;
		log.debug(e);
	    } catch (ClassNotFoundException e) {
		status = false;
		log.debug(e);
	    } catch (IllegalArgumentException e) {
		status = false;
		log.debug(e);
	    } catch (SecurityException e) {
		status = false;
		log.debug(e);
	    }
	}
	if (ajImporters.isEmpty()) {
	    log.error("Data loading failed. There is no AJImporter.");
	    return false;
	}
	resetObservations();
	for (AJImporter ajImporter : ajImporters) {
	    observations.addAll(ajImporter.importObservations());
	}
	return status;
    }

    /**
     * Export the observations using the available AJExporters.
     * 
     * @return true if the observations have been exported.
     */
    public boolean ajExport() {
	boolean status = true;
	ArrayList<String> ajExporterNames = ExporterSearcher
		.getExporterFullNameList();
	Collections.sort(ajExporterNames);
	for (String ajExporterName : ajExporterNames) {
	    try {
		log.debug("Loading exporter " + ajExporterName);
		Class<?> cls = Class.forName(ajExporterName);
		Object clsInstance = cls.getDeclaredConstructor(File.class)
			.newInstance(
				AJConfig.getInstance().getFilesLocation());
		AJExporter ajExporter = (AJExporter) clsInstance;
		if (!ajExporters.contains(ajExporter)) {
		    ajExporters.add(ajExporter);
		    log.debug("Exporter " + ajExporter.getName() + " is loaded");
		} else {
		    log.debug("Exporter " + ajExporter.getName()
			    + " was already loaded");
		}
	    } catch (InstantiationException e) {
		status = false;
		log.debug(e);
	    } catch (IllegalAccessException e) {
		status = false;
		log.debug(e);
	    } catch (ClassNotFoundException e) {
		status = false;
		log.debug(e);
	    } catch (IllegalArgumentException e) {
		status = false;
		log.debug(e);
	    } catch (InvocationTargetException e) {
		status = false;
		log.debug(e);
	    } catch (NoSuchMethodException e) {
		status = false;
		log.debug(e);
	    } catch (SecurityException e) {
		status = false;
		log.debug(e);
	    }
	}
	if (ajExporters.isEmpty()) {
	    log.error("Data exporting failed. There is no AJExporter.");
	    return false;
	}
	for (AJExporter ajExporter : ajExporters) {
	    status = ajExporter.generateJournal(observations) && status;
	}
	return status;
    }

    /**
     * Runs the method postProcessing() for each available ajExporter.
     * 
     * @return true if the post processing succeeded.
     */
    public boolean postProcessing() {
	boolean status = true;
	for (AJExporter ajExporter : ajExporters) {
	    try {
		ajExporter.postProcessing();
	    } catch (IOException e) {
		log.debug(ajExporter.getName()
			+ " failed the post processing phase.", e);
		status = false;
	    }
	}
	return status;
    }
}
