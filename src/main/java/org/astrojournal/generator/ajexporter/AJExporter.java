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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.astrojournal.generator.observation.AJObservation;

/**
 * Abstract class of AstroJournal observation exporter.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 13/09/2015
 */
public abstract class AJExporter {

    protected File ajFilesLocation;

    /**
     * Default constructor
     */
    public AJExporter() {
    }

    /**
     * The path location where to export the files.
     * 
     * @param ajFilesLocation
     */
    public AJExporter(File ajFilesLocation) {
	this.ajFilesLocation = ajFilesLocation;
    }

    /**
     * Exports an observation record to Latex
     * 
     * @param observations
     *            the list of observations to exportObservation
     * @param outputReportsFolder
     *            the folder to write the observation in.
     * @return true if the observations are exported
     */
    public abstract boolean exportObservations(
	    ArrayList<AJObservation> observations, String outputReportsFolder);

    /**
     * Generate the journal document
     * 
     * @param observations
     *            the list of observations to exportObservation
     * @return true if the observations have been exported correctly
     */
    public abstract boolean generateJournal(
	    ArrayList<AJObservation> observations);

    /**
     * Generate the journal document
     * 
     * @param outputReportsFolder
     *            the directory containing the single observations in a specific
     *            format
     * @param headerFilename
     *            the header filename
     * @param mainFilename
     *            the main filename
     * @param footerFilename
     *            the footer filename
     */
    public abstract void generateJournal(String outputReportsFolder,
	    String headerFilename, String mainFilename, String footerFilename);

    /**
     * Return AstroJournal files location
     * 
     * @return the ajFilesLocation
     */
    public File getAjFilesLocation() {
	return ajFilesLocation;
    }

    /**
     * Set AstroJournal Files location
     * 
     * @param ajFilesLocation
     *            the ajFilesLocation to set
     */
    public void setAjFilesLocation(File ajFilesLocation) {
	this.ajFilesLocation = ajFilesLocation;
    }

    /**
     * Return the exporter name
     * 
     * @return the exporter name
     */
    public abstract String getName();

    @Override
    public boolean equals(Object o) {
	if (o instanceof AJExporter) {
	    AJExporter that = (AJExporter) o;
	    return this.getName().equals(that.getName());
	}
	return false;
    }

    /**
     * Run additional post processing commands if necessary.
     * 
     * @throws IOException
     *             if the post-processing failed.
     */
    public abstract void postProcessing() throws IOException;
}
