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
package org.astrojournal.generator.absgen;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ajconfiguration.AJPropertyConstants;
import org.astrojournal.generator.Report;
import org.astrojournal.utilities.filefilters.TSVRawReportFilter;

/**
 * The parser for AstroJournal. It imports files containing the reports.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public abstract class Importer {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(Importer.class);

    /** The absolute path of the location containing the data. */
    protected String filesLocation = System.getProperty("user.home");

    /** The folder containing the raw reports to import. */
    protected String rawReportFolder = "reports";

    /** The resource bundle. */
    protected ResourceBundle resourceBundle = null;

    /**
     * Default constructor
     */
    public Importer() {
    }

    /**
     * Configure the importer using a Configuration object
     * 
     * @param config
     *            The configuration
     */
    public void setConfiguration(Configuration config) {
	setResourceBundle(config.getResourceBundle());
	setFilesLocation(config.getProperty(AJPropertyConstants.FILES_LOCATION
		.getKey()));
	setRawReportFolder(config
		.getProperty(AJPropertyConstants.RAW_REPORTS_FOLDER.getKey()));
    }

    /**
     * Import the observations.
     * 
     * @return the imported observations
     */
    public List<Report> importReports() {
	if (resourceBundle != null) {
	    log.info("Importing observation files:");
	}
	String rawReportPath = filesLocation + File.separator + rawReportFolder;
	File[] files = new File(rawReportPath)
		.listFiles(new TSVRawReportFilter());
	if (files == null) {
	    log.error("Folder " + rawReportPath + " not found");
	    return new ArrayList<Report>();
	}
	return importReports(files);
    }

    /**
     * Imports the report data stored in multiple files.
     * 
     * @param files
     *            An array of files to parse (either CSV or TSV file, separated
     *            by TAB delimiter).
     * @return a list of Report objects
     */
    public List<Report> importReports(File[] files) {
	Arrays.sort(files, NameFileComparator.NAME_COMPARATOR);
	List<Report> reports = new ArrayList<Report>();
	for (File file : files) {
	    reports.addAll(importReports(file));
	}
	return reports;
    }

    /**
     * Imports the report data stored in a file.
     * 
     * @param file
     *            The file to parse
     * @return a list of Report objects
     */
    public abstract List<Report> importReports(File file);

    /**
     * Imports an report record
     * 
     * @param reader
     *            the buffered reader associated to the file
     * @param report
     *            the object containing the report to import
     * @param line
     *            the current line parsed in the file (the first line of the
     *            record)
     * @param delimiter
     *            the field delimiter
     * @throws IOException
     *             if reader cannot read the report
     */
    protected abstract void importReport(BufferedReader reader, Report report,
	    String line, String delimiter) throws IOException;

    /**
     * Return the importer name
     * 
     * @return the importer name
     */
    public String getName() {
	return this.getClass().getName();
    }

    @Override
    public boolean equals(Object o) {
	if (o instanceof Importer) {
	    Importer that = (Importer) o;
	    return this.getName().equals(that.getName());
	}
	return false;
    }

    /**
     * Return the absolute path for storing the data.
     * 
     * @return filesLocation
     */
    public String getFilesLocation() {
	return filesLocation;
    }

    /**
     * Set the absolute path for storing the data.
     * 
     * @param filesLocation
     *            the files location
     */
    public void setFilesLocation(String filesLocation) {
	this.filesLocation = filesLocation;
    }

    /**
     * Return the name of the folder containing the raw reports to import.
     * 
     * @return the rawReportFolder
     */
    public String getRawReportFolder() {
	return rawReportFolder;
    }

    /**
     * Set the name of the folder containing the raw reports to import.
     * 
     * @param rawReportFolder
     *            the rawReportFolder to set
     */
    public void setRawReportFolder(String rawReportFolder) {
	this.rawReportFolder = rawReportFolder;
    }

    /**
     * Get the resource bundle.
     * 
     * @return the resourceBundle
     */
    public ResourceBundle getResourceBundle() {
	return resourceBundle;
    }

    /**
     * Set the resource bundle.
     * 
     * @param resourceBundle
     *            the resourceBundle to set
     */
    public void setResourceBundle(ResourceBundle resourceBundle) {
	this.resourceBundle = resourceBundle;
    }

}
