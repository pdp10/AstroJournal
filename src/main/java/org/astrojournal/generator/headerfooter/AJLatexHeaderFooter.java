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
package org.astrojournal.generator.headerfooter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Header Footer of the LaTeX main file.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 28/05/2015
 */
public abstract class AJLatexHeaderFooter {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(AJLatexHeaderFooter.class);

    /** Default constructor. It initialises default header and footer. */
    public AJLatexHeaderFooter() {
    }

    /**
     * Imports a LaTeX file
     * 
     * @param file
     *            The LaTeX file to import.
     * @return the imported LaTeX file as a string
     */
    protected String importLatex(File file) {
	StringBuilder sb = new StringBuilder();
	if (file.isFile() && file.getName().endsWith(".tex")) {
	    log.debug("Processing LaTeX file " + file.getAbsolutePath());
	    // Create a buffered reader to read the file
	    BufferedReader reader = null;
	    try {
		reader = new BufferedReader(new FileReader(file));
		String line;
		// Read all lines
		while ((line = reader.readLine()) != null) {
		    // log.debug(line);
		    sb.append(line).append(" \n");
		} // end while
	    } catch (IOException ex) {
		log.error(
			"Error when reading the file " + file.getAbsolutePath(),
			ex);
	    } finally {
		try {
		    if (reader != null)
			reader.close();
		} catch (IOException ex) {
		    log.error(
			    "Error when closing the file "
				    + file.getAbsolutePath(), ex);
		}
	    }
	}
	log.debug("LaTeX file " + file.getAbsolutePath()
		+ " was processed correctly");
	// No need to replace \ with \\ as this is not interpreted by Java at
	// this stage
	return sb.toString();
    }

    /**
     * Constructor. It reads the header and footer from files.
     * 
     * @param path
     *            The path to the file
     * @param folder
     *            The folder containing the filename, if any
     * @param filename
     *            The file name
     */
    protected String buildPath(String path, String folder, String filename) {
	String fileString;
	if (folder == null || folder == "") {
	    fileString = path + File.separator + filename;
	} else {
	    fileString = path + File.separator + folder + File.separator
		    + filename;
	}
	return fileString;
    }
}
