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
package org.astrojournal.generator.abstractgenerator;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import org.astrojournal.generator.Report;

/**
 * Abstract class of AstroJournal report exporter.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 13/09/2015
 */
public abstract class Exporter {

    /** The absolute path of the location to export the data. */
    protected String filesLocation = System.getProperty("user.home");

    /** The folder containing the reports. */
    protected String reportFolder = "reports";

    /** The report body file name. */
    protected String reportFilename = "report.txt";

    /** The header / footer folder. */
    protected String headerFooterFolder = "";

    /** The report header file name. */
    protected String headerFilename = "";

    /** The report footer file name. */
    protected String footerFilename = "";

    /** If no output should be printed. */
    protected boolean quiet = true;

    /** The resource bundle. */
    protected ResourceBundle resourceBundle = null;

    /**
     * Default constructor.
     */
    public Exporter() {
    }

    /**
     * Exports an observation record.
     * 
     * @param reports
     *            the list of reports to exportReport
     * @return true if the reports were exported
     */
    public abstract boolean exportReports(List<Report> reports);

    /**
     * Generate the journal document.
     * 
     * @return true if the journal was generated
     */
    public abstract boolean generateJournal();

    /**
     * Run additional post processing commands if necessary.
     * 
     * @throws IOException
     *             if the post-processing failed.
     */
    public abstract void postProcessing() throws IOException;

    /**
     * Return the exporter name
     * 
     * @return the exporter name
     */
    public String getName() {
	return this.getClass().getName();
    }

    @Override
    public boolean equals(Object o) {
	if (o instanceof Exporter) {
	    Exporter that = (Exporter) o;
	    return this.getName().equals(that.getName());
	}
	return false;
    }

    /**
     * Return the absolute path of the files to export.
     * 
     * @return filesLocation
     */
    public String getFilesLocation() {
	return filesLocation;
    }

    /**
     * Set the absolute path of the files to export.
     * 
     * @param filesLocation
     *            the files location
     */
    public void setFilesLocation(String filesLocation) {
	this.filesLocation = filesLocation;
    }

    /**
     * Get the folder containing the reports for this exporter.
     * 
     * @return the reportFolder
     */
    public String getReportFolder() {
	return reportFolder;
    }

    /**
     * Set the folder containing the reports for this exporter.
     * 
     * @param reportFolder
     *            the reportFolder to set
     */
    public void setReportFolder(String reportFolder) {
	this.reportFolder = reportFolder;
    }

    /**
     * Get the report filename.
     * 
     * @return the reportFilename
     */
    public String getReportFilename() {
	return reportFilename;
    }

    /**
     * Set the report filename.
     * 
     * @param reportFilename
     *            the reportFilename to set
     */
    public void setReportFilename(String reportFilename) {
	this.reportFilename = reportFilename;
    }

    /**
     * Get the header / footer folder
     * 
     * @return the headerFooterFolder
     */
    public String getHeaderFooterFolder() {
	return headerFooterFolder;
    }

    /**
     * Set the header / footer folder
     * 
     * @param headerFooterFolder
     *            the headerFooterFolder to set
     */
    public void setHeaderFooterFolder(String headerFooterFolder) {
	this.headerFooterFolder = headerFooterFolder;
    }

    /**
     * Get the header filename.
     * 
     * @return the headerFilename
     */
    public String getHeaderFilename() {
	return headerFilename;
    }

    /**
     * Set the header filename.
     * 
     * @param headerFilename
     *            the headerFilename to set
     */
    public void setHeaderFilename(String headerFilename) {
	this.headerFilename = headerFilename;
    }

    /**
     * Get the footer filename.
     * 
     * @return the footerFilename
     */
    public String getFooterFilename() {
	return footerFilename;
    }

    /**
     * Set the footer filename.
     * 
     * @param footerFilename
     *            the footerFilename to set
     */
    public void setFooterFilename(String footerFilename) {
	this.footerFilename = footerFilename;
    }

    /**
     * Get true if no additional prints should be made.
     * 
     * @return the quiet
     */
    public boolean isQuiet() {
	return quiet;
    }

    /**
     * Set true if no additional prints should be made.
     * 
     * @param quiet
     *            the quiet to set
     */
    public void setQuiet(boolean quiet) {
	this.quiet = quiet;
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
