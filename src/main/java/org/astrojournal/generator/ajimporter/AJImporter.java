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
package org.astrojournal.generator.ajimporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.astrojournal.configuration.AJConfig;
import org.astrojournal.generator.observation.AJObservation;

/**
 * The parser for AstroJournal. It imports files containing the observations.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 28/05/2015
 */
public abstract class AJImporter {

    /** The keyword denoting the first line of the observation record */
    protected static String initialKeyword = AJObservation.DATE_NAME;

    /** The values contained in an imported string. */
    protected String[] values = null;

    // We pass this as parameter to leave the ability to pass different
    // configurators.
    /** An instance of AJConfig. */
    protected AJConfig ajConfig = null;

    /**
     * Default constructor
     * 
     * @param ajConfig
     *            The astro journal configurator.
     */
    public AJImporter(AJConfig ajConfig) {
	this.ajConfig = ajConfig;
    }

    /**
     * Returns the initial keyword denoting the beginning of the observation
     * record
     * 
     * @return initialKeyword
     */
    public static String getInitialKeyword() {
	return initialKeyword;
    }

    /**
     * Import the observations.
     * 
     * @return the imported observations
     */
    public abstract ArrayList<AJObservation> importObservations();

    /**
     * Imports the observation data stored in multiple files.
     * 
     * @param files
     *            An array of files to parse (either CSV or TSV file, separated
     *            by TAB delimiter).
     * @return a list of AJObservation objects
     */
    public ArrayList<AJObservation> importObservations(File[] files) {
	Arrays.sort(files);
	ArrayList<AJObservation> observations = new ArrayList<AJObservation>();
	for (File file : files) {
	    observations.addAll(importObservations(file));
	} // end for
	return observations;
    }

    /**
     * Imports the observation data stored in a file.
     * 
     * @param file
     *            The file to parse
     * @return a list of AJObservation objects
     */
    public abstract ArrayList<AJObservation> importObservations(File file);

    /**
     * Imports an observation record
     * 
     * @param reader
     *            the buffered reader associated to the file
     * @param obs
     *            the object containing the observation to import
     * @param line
     *            the current line parsed in the file (the first line of the
     *            record)
     * @param delimiter
     *            the field delimiter
     * @throws IOException
     *             if reader cannot read the observation
     */
    protected abstract void importObservation(BufferedReader reader,
	    AJObservation obs, String line, String delimiter)
	    throws IOException;

    /**
     * Return the importer name
     * 
     * @return the importer name
     */
    public abstract String getName();

    @Override
    public boolean equals(Object o) {
	if (o instanceof AJImporter) {
	    AJImporter that = (AJImporter) o;
	    return this.getName().equals(that.getName());
	}
	return false;
    }

}
