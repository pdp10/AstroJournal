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
import org.astrojournal.generator.abstractgenerator.Exporter;
import org.astrojournal.generator.abstractgenerator.Importer;
import org.astrojournal.generator.abstractgenerator.LatexExporter;
import org.astrojournal.generator.extendedgenerator.LatexExporterByDate;
import org.astrojournal.utilities.ClassesInstanceOf;

/**
 * This class automatically generates astro journal documents.
 * 
 * @author Piero Dalle Pezze
 * @version 0.11
 * @since 12/04/2015
 */
public class Generator {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(Generator.class);

    /** The configuration. */
    private Configuration config;

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
     * 
     * @param config
     */
    public Generator(Configuration config) {
	this.config = config;
	this.resourceBundle = config.getResourceBundle();
    }

    /**
     * It generates the files for AstroJournal.
     * 
     * @return true if the reports sorted by date and by target have been
     *         exported correctly
     */
    public boolean generateJournals() {
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
	    log.warn(resourceBundle.getString("AJ.errSomeImporterFailed.text"));
	}
	if (!ajExport()) {
	    log.warn(resourceBundle.getString("AJ.errSomeExporterFailed.text"));
	    return false;
	}
	return true;
    }

    /**
     * Returns the imported reports.
     * 
     * @return the reports
     */
    public List<Report> getReports() {
	return reports;
    }

    /**
     * Returns the list of Importer objects.
     * 
     * @return the Importers
     */
    public List<Importer> getImporters() {
	return importers;
    }

    /**
     * Returns the list of Exporter objects.
     * 
     * @return the Exporters
     */
    public List<Exporter> getExporters() {
	return exporters;
    }

    /**
     * Reset the generator.
     */
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
    public void resetImporters() {
	importers = new ArrayList<Importer>();
    }

    /**
     * Reset the Exporters.
     */
    public void resetExporters() {
	exporters = new ArrayList<Exporter>();
    }

    /**
     * Export the reports using the available Importers.
     * 
     * @return true if the reports have been imported.
     */
    public boolean ajImport() {
	boolean status = true;
	List<String> importerNames = ClassesInstanceOf
		.getClassFullNamesInstanceOf(this.getClass().getPackage()
			.getName(), Importer.class);
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
	if (importers.isEmpty()) {
	    log.error(resourceBundle.getString("AJ.errNoDataImported.text"));
	    return false;
	}
	resetReports();
	for (Importer importer : importers) {

	    // TODO: TEMPORARY IMPLEMENTATION. WITH DEPENDENCY INJECTION, THESE
	    // PARAMETERS ARE PASSED BY THE INJECTOR
	    // THEREFORE, THERE IS NO NEED TO SET THEM HERE!! :)
	    importer.setFilesLocation(config
		    .getProperty(AJPropertyConstants.FILES_LOCATION.getKey()));
	    importer.setRawReportFolder(config
		    .getProperty(AJPropertyConstants.RAW_REPORTS_FOLDER
			    .getKey()));
	    importer.setResourceBundle(config.getResourceBundle());
	    // TODO: END

	    log.debug(importer.getName() + " is importing reports");
	    List<Report> obs = importer.importReports();
	    reports.addAll(obs);
	    log.debug(importer.getName() + " imported " + obs.size()
		    + " reports");
	}
	return status;
    }

    /**
     * Export the reports using the available Exporters.
     * 
     * @return true if the reports have been exported.
     */
    public boolean ajExport() {
	boolean status = true;
	List<String> exporterNames = ClassesInstanceOf
		.getClassFullNamesInstanceOf(this.getClass().getPackage()
			.getName(), Exporter.class);
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
	if (exporters.isEmpty()) {
	    log.error(resourceBundle.getString("AJ.errNoDataExported.text"));
	    return false;
	}
	for (Exporter exporter : exporters) {

	    // TODO: TEMPORARY IMPLEMENTATION. WITH DEPENDENCY INJECTION, THESE
	    // PARAMETERS ARE PASSED BY THE INJECTOR
	    // THEREFORE, THERE IS NO NEED TO SET THEM HERE!! :)
	    exporter.setFilesLocation(config
		    .getProperty(AJPropertyConstants.FILES_LOCATION.getKey()));
	    exporter.setQuiet(Boolean.parseBoolean(config
		    .getProperty(AJPropertyConstants.QUIET.getKey())));
	    exporter.setResourceBundle(config.getResourceBundle());
	    if (exporter instanceof LatexExporter) {
		((LatexExporter) exporter)
			.setLatexOutput(Boolean.parseBoolean(config
				.getProperty(AJPropertyConstants.SHOW_LATEX_OUTPUT
					.getKey())));
		if (exporter instanceof LatexExporterByDate) {
		    ((LatexExporterByDate) exporter)
			    .setReportFolder(config
				    .getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_DATE
					    .getKey()));
		    ((LatexExporterByDate) exporter)
			    .setReportFilename(config
				    .getProperty(AJPropertyConstants.LATEX_REPORT_BY_DATE_FILENAME
					    .getKey()));
		    ((LatexExporterByDate) exporter)
			    .setHeaderFooterFolder(config
				    .getProperty(AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER
					    .getKey()));
		    ((LatexExporterByDate) exporter)
			    .setHeaderFilename(config
				    .getProperty(AJPropertyConstants.LATEX_HEADER_BY_DATE_FILENAME
					    .getKey()));
		    ((LatexExporterByDate) exporter)
			    .setFooterFilename(config
				    .getProperty(AJPropertyConstants.LATEX_FOOTER_BY_DATE_FILENAME
					    .getKey()));
		}

		// TODO TEMPORARILY COMMENTED until this is ported to newgen
		// if (exporter instanceof AJLatexExporterByTarget) {
		// ((AJLatexExporterByTarget) exporter)
		// .setReportFolder(config
		// .getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_TARGET
		// .getKey()));
		// ((AJLatexExporterByTarget) exporter)
		// .setReportFilename(config
		// .getProperty(AJPropertyConstants.LATEX_REPORT_BY_TARGET_FILENAME
		// .getKey()));
		// ((AJLatexExporterByTarget) exporter)
		// .setHeaderFooterFolder(config
		// .getProperty(AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER
		// .getKey()));
		// ((AJLatexExporterByTarget) exporter)
		// .setHeaderFilename(config
		// .getProperty(AJPropertyConstants.LATEX_HEADER_BY_TARGET_FILENAME
		// .getKey()));
		// ((AJLatexExporterByTarget) exporter)
		// .setFooterFilename(config
		// .getProperty(AJPropertyConstants.LATEX_FOOTER_BY_TARGET_FILENAME
		// .getKey()));
		// }
		// if (exporter instanceof AJLatexExporterByConstellation) {
		// ((AJLatexExporterByConstellation) exporter)
		// .setReportFolder(config
		// .getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
		// .getKey()));
		// ((AJLatexExporterByConstellation) exporter)
		// .setReportFilename(config
		// .getProperty(AJPropertyConstants.LATEX_REPORT_BY_CONSTELLATION_FILENAME
		// .getKey()));
		// ((AJLatexExporterByConstellation) exporter)
		// .setHeaderFooterFolder(config
		// .getProperty(AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER
		// .getKey()));
		// ((AJLatexExporterByConstellation) exporter)
		// .setHeaderFilename(config
		// .getProperty(AJPropertyConstants.LATEX_HEADER_BY_CONSTELLATION_FILENAME
		// .getKey()));
		// ((AJLatexExporterByConstellation) exporter)
		// .setFooterFilename(config
		// .getProperty(AJPropertyConstants.LATEX_FOOTER_BY_CONSTELLATION_FILENAME
		// .getKey()));
		// }
		//
		// } else {
		// if (exporter instanceof AJTextExporterByDateSGL) {
		// ((AJTextExporterByDateSGL) exporter)
		// .setReportFolder(config
		// .getProperty(AJPropertyConstants.SGL_REPORTS_FOLDER_BY_DATE
		// .getKey()));
		// ((AJTextExporterByDateSGL) exporter)
		// .setReportFilename(config
		// .getProperty(AJPropertyConstants.SGL_REPORT_BY_DATE_FILENAME
		// .getKey()));
		// }
		// TODO TEMPORARILY COMMENTED
	    }

	    // TODO END

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
}
