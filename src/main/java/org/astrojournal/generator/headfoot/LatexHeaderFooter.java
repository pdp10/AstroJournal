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
package org.astrojournal.generator.headfoot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Header Footer of the LaTeX main file.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 28/05/2015
 */
public abstract class LatexHeaderFooter {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(LatexHeaderFooter.class);

    /** The resource bundle. */
    protected ResourceBundle resourceBundle = null;

    /** Default constructor. It initialises default header and footer. */
    public LatexHeaderFooter() {
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
		log.debug(ex, ex);
		if (resourceBundle != null) {
		    log.error(resourceBundle
			    .getString("AJ.errReadingFile.text")
			    + " "
			    + file.getAbsolutePath());
		}
	    } finally {
		try {
		    if (reader != null)
			reader.close();
		} catch (IOException ex) {
		    if (resourceBundle != null) {
			log.error(resourceBundle
				.getString("AJ.errClosingFile.text")
				+ " "
				+ file.getAbsolutePath());
		    }
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
