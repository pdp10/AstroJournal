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

import org.astrojournal.observation.AJObservation;

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
     * The path location where to export the files.
     * 
     * @param ajFilesLocation
     */
    public AJExporter(File ajFilesLocation) {
	this.ajFilesLocation = ajFilesLocation;
    }

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

}
