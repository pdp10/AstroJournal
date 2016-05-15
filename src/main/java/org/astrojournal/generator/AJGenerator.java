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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ajconfiguration.AJPropertyConstants;
import org.astrojournal.generator.absgen.Exporter;
import org.astrojournal.generator.absgen.Importer;
import org.astrojournal.utilities.ClassesInstanceOf;

/**
 * This class automatically generates astro journal documents.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public class AJGenerator implements Generator {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(AJGenerator.class);

    /** The configuration. */
    private Configuration config = null;

    /** The resource bundle. */
    private ResourceBundle resourceBundle = null;

    /** The list of reports. */
    private List<Report> reports = new ArrayList<Report>();

    /** The list of Importer objects. */
    private List<Importer> importers = new ArrayList<Importer>();

    /** The list of Exporter objects. */
    private List<Exporter> exporters = new ArrayList<Exporter>();

    /**
     * Default constructor
     */
    public AJGenerator() {
    }

    /**
     * Return the configuration.
     * 
     * @return config
     */
    @Override
    public Configuration getConfiguration() {
	return config;
    }

    /**
     * Set a new configuration
     * 
     * @param config
     */
    @Override
    public void setConfiguration(Configuration config) {
	this.config = config;
	this.resourceBundle = config.getResourceBundle();
    }

    /**
     * It generates the files for AstroJournal.
     * 
     * @return true if the reports sorted by date and by target have been
     *         exported correctly
     */
    @Override
    public boolean generateJournals() {
	if (config == null) {
	    return false;
	}
	if (!ajImport()) {
	    if (reports.isEmpty()) {
		log.error(resourceBundle
			.getString("AJ.errNoObservationFound.text")
			+ " "
			+ config.getProperty(AJPropertyConstants.FILES_LOCATION
				.getKey())
			+ File.separator
			+ config.getProperty(AJPropertyConstants.RAW_REPORTS_FOLDER
				.getKey()));
		return false;
	    }
	    log.error(resourceBundle.getString("AJ.errSomeImporterFailed.text"));
	}
	if (!ajExport()) {
	    log.error(resourceBundle.getString("AJ.errSomeExporterFailed.text"));
	    return false;
	}
	return true;
    }

    /**
     * Returns the imported reports.
     * 
     * @return the reports
     */
    @Override
    public List<Report> getReports() {
	return reports;
    }

    /**
     * Returns the list of Importer objects.
     * 
     * @return the Importers
     */
    @Override
    public List<Importer> getImporters() {
	return importers;
    }

    /**
     * Returns the list of Exporter objects.
     * 
     * @return the Exporters
     */
    @Override
    public List<Exporter> getExporters() {
	return exporters;
    }

    /**
     * Reset the generator.
     */
    @Override
    public void reset() {
	resetImporters();
	resetExporters();
    }

    /**
     * Reset the generator reports.
     */
    private void resetReports() {
	reports = new ArrayList<Report>();
    }

    /**
     * Reset the Importers.
     */
    @Override
    public void resetImporters() {
	importers = new ArrayList<Importer>();
    }

    /**
     * Reset the Exporters.
     */
    @Override
    public void resetExporters() {
	exporters = new ArrayList<Exporter>();
    }

    /**
     * Export the reports using the available Importers.
     * 
     * @return true if the reports have been imported.
     */
    @Override
    public boolean ajImport() {
	if (config == null) {
	    return false;
	}
	boolean status = loadImporters();
	if (importers.isEmpty()) {
	    log.error(resourceBundle.getString("AJ.errNoDataImported.text"));
	    return false;
	}
	configureImporters();

	// reset the reports
	resetReports();
	// import the reports
	for (Importer importer : importers) {
	    log.debug(importer.getName() + " is importing reports");
	    List<Report> obs = importer.importReports();
	    reports.addAll(obs);
	    log.debug(importer.getName() + " imported " + obs.size()
		    + " reports");
	}

	Collections.sort(reports);
	// reverse the reports so that the most recent is the first.
	Collections.reverse(reports);

	return status;
    }

    /**
     * Export the reports using the available Exporters.
     * 
     * @return true if the reports have been exported.
     */
    @Override
    public boolean ajExport() {
	if (config == null) {
	    return false;
	}
	boolean status = loadExporters();
	if (exporters.isEmpty()) {
	    log.error(resourceBundle.getString("AJ.errNoDataExported.text"));
	    return false;
	}
	configureExporters();

	// Export the reports
	for (Exporter exporter : exporters) {
	    log.debug(exporter.getName() + " is exporting reports");
	    boolean result = exporter.exportReports(reports)
		    && exporter.generateJournal();
	    status = result && status;
	    if (result) {
		log.debug(exporter.getName() + " SUCCEEDED");
	    } else {
		log.debug(exporter.getName() + " FAILED");
	    }
	}
	return status;
    }

    /**
     * Runs the method postProcessing() for each available ajExporter.
     * 
     * @return true if the post processing succeeded.
     */
    @Override
    public boolean postProcessing() {
	boolean status = true;
	for (Exporter exporter : exporters) {
	    try {
		exporter.postProcessing();
	    } catch (IOException e) {
		log.debug(exporter.getName()
			+ " failed the post processing phase.", e);
		status = false;
	    }
	}
	return status;
    }

    /**
     * Load the importers.
     * 
     * @return true if the importers are loaded correctly.
     */
    private boolean loadImporters() {
	resetImporters();
	boolean status = true;
	List<String> importerNames = ClassesInstanceOf
		.getClassFullNamesInstanceOf(
			this.getClass().getPackage().getName()
				+ "."
				+ config.getProperty(AJPropertyConstants.GENERATOR_NAME
					.getKey()), Importer.class);
	Collections.sort(importerNames);
	for (String importerName : importerNames) {
	    try {
		log.debug("Loading importer " + importerName);
		Class<?> cls = Class.forName(importerName);
		Object clsInstance = cls.newInstance();
		Importer importer = (Importer) clsInstance;
		if (!importers.contains(importer)) {
		    importers.add(importer);
		    log.debug("Importer " + importer.getName() + " is loaded");
		} else {
		    log.debug("Importer " + importer.getName()
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
	return status;
    }

    /**
     * Load the exporters.
     * 
     * @return true if the exporters are loaded correctly.
     */
    private boolean loadExporters() {
	resetExporters();
	boolean status = true;
	List<String> exporterNames = ClassesInstanceOf
		.getClassFullNamesInstanceOf(
			this.getClass().getPackage().getName()
				+ "."
				+ config.getProperty(AJPropertyConstants.GENERATOR_NAME
					.getKey()), Exporter.class);
	Collections.sort(exporterNames);
	for (String exporterName : exporterNames) {
	    try {
		log.debug("Loading exporter " + exporterName);
		Class<?> cls = Class.forName(exporterName);
		Object clsInstance = cls.newInstance();
		Exporter exporter = (Exporter) clsInstance;
		if (!exporters.contains(exporter)) {
		    exporters.add(exporter);
		    log.debug("Exporter " + exporter.getName() + " is loaded");
		} else {
		    log.debug("Exporter " + exporter.getName()
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
	return status;
    }

    /** Configure the importers. */
    private void configureImporters() {
	for (Importer importer : importers) {
	    importer.setConfiguration(config);
	}
    }

    /** Configure the exporters. */
    private void configureExporters() {
	for (Exporter exporter : exporters) {
	    exporter.setConfiguration(config);
	}
    }

}
