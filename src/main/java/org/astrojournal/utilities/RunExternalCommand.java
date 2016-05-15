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
package org.astrojournal.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

/**
 * A simple class to run an external command from AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public class RunExternalCommand {

    /** The files location. */
    private String filesLocation = System.getProperty("user.home");

    /** The Resource Bundle object. */
    private ResourceBundle resourceBundle = null;

    /**
     * Constructor
     * 
     * @param filesLocation
     *            The location of the files
     * @param resourceBundle
     *            The resource bundle object containing the output strings
     */
    public RunExternalCommand(String filesLocation,
	    ResourceBundle resourceBundle) {
	this.filesLocation = filesLocation;
	this.resourceBundle = resourceBundle;
    }

    /**
     * Run a command
     * 
     * @param command
     *            The command to run
     * @throws IOException
     * @return the output and output error for the executed command
     */
    public String runCommand(String command) throws IOException {
	StringBuilder sb = new StringBuilder();
	// NOTE: for some reason Runtime.getRuntime().exec() works only if the
	// command output is captured on Windows.
	// On Linux, this does not matter, but on Windows this does not work..
	// So
	// leave it.
	Process p = Runtime.getRuntime().exec(command, null,
		new File(filesLocation));
	// read the output messages from the command
	BufferedReader stdInput = new BufferedReader(new InputStreamReader(
		p.getInputStream()));
	sb.append("\n\n\n");
	if (resourceBundle != null) {
	    sb.append(resourceBundle
		    .getString("AJ.lblOutputForTheCommand.text")
		    + " `"
		    + command + "`:\n\n");
	}
	String temp;
	while ((temp = stdInput.readLine()) != null) {
	    sb.append(temp).append("\n");
	}
	stdInput.close();

	// read the error messages from the command
	BufferedReader stdError = new BufferedReader(new InputStreamReader(
		p.getErrorStream()));
	if (resourceBundle != null) {
	    sb.append("\n"
		    + resourceBundle
			    .getString("AJ.lblErrorsForTheCommand.text") + " `"
		    + command + "`:\n\n");
	}
	while ((temp = stdError.readLine()) != null) {
	    sb.append(temp).append("\n");
	}
	stdError.close();
	return sb.toString();
    }
}
