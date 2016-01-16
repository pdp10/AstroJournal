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
 * @version 0.10
 * @since 12/04/2015
 */
public class AJGenerator {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(AJGenerator.class);

    /** The list of observations. */
    private ArrayList<AJObservation> observations = new ArrayList<AJObservation>();

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
	AJConfig ajConfig = AJConfig.getInstance();

	if (!ajConfig.isQuiet() && ajConfig.isShowLicenseAtStart()) {
	    showLicense();
	}

	if (!ajConfig.isQuiet() && ajConfig.isShowPDFLatexVersion()) {
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
			+ AJConfig.getInstance().getAJFilesLocation()
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
     * Export the observations using the available exporters.
     * 
     * @return true if the observations have been exported.
     */
    public boolean ajImport() {
	boolean status = true;
	ArrayList<String> ajImporters = ImporterSearcher
		.getImporterFullNameList();
	Collections.sort(ajImporters);
	for (String ajImporterName : ajImporters) {
	    try {
		log.debug("Loading importer " + ajImporterName);
		Class<?> cls = Class.forName(ajImporterName);
		Object clsInstance = cls.newInstance();
		AJImporter ajImporter = (AJImporter) clsInstance;
		log.debug("Importer " + ajImporter.getClass() + " is loaded");
		observations.addAll(ajImporter.importObservations());
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
	return status;
    }

    /**
     * Export the observations using the available exporters.
     * 
     * @return true if the observations have been exported.
     */
    public boolean ajExport() {
	boolean status = true;
	ArrayList<String> ajExporters = ExporterSearcher
		.getExporterFullNameList();
	Collections.sort(ajExporters);
	for (String ajExporterName : ajExporters) {
	    try {
		log.debug("Loading exporter " + ajExporterName);
		Class<?> cls = Class.forName(ajExporterName);
		Object clsInstance = cls.getDeclaredConstructor(File.class)
			.newInstance(
				AJConfig.getInstance().getAJFilesLocation());
		AJExporter ajExporter = (AJExporter) clsInstance;
		log.debug("Exporter " + ajExporter.getClass() + " is loaded");
		status = ajExporter.generateJournal(observations) && status;
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
	return status;
    }

}
