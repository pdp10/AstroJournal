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

    /**
     * Imports a LaTeX file
     * 
     * @param path
     *            The path to the file
     * @param file
     *            The LaTeX file
     * @return the imported LaTeX file as a string
     */
    protected String importLatex(String path, String file) {
	File f = new File(path + File.separator + file);
	StringBuilder sb = new StringBuilder();
	if (f.isFile() && f.getName().endsWith(".tex")) {
	    log.debug("Processing latex file " + path + File.separator + file);
	    // Create a buffered reader to read the file
	    BufferedReader reader = null;
	    try {
		reader = new BufferedReader(new FileReader(f));
		String line;
		// Read all lines
		while ((line = reader.readLine()) != null) {
		    // log.debug(line);
		    sb.append(line).append(" \n");
		} // end while
	    } catch (IOException ex) {
		log.error(ex, ex);
	    } finally {
		try {
		    if (reader != null)
			reader.close();
		} catch (IOException ex) {
		    log.error(ex, ex);
		}
	    }
	}
	// No need to replace \ with \\ as this is not interpreted by Java at
	// this stage
	return sb.toString();
    }

    /** Default constructor. It initialises default header and footer. */
    public AJLatexHeaderFooter() {
    }

}
