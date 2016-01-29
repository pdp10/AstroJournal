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

import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;
import org.astrojournal.utilities.RunExternalCommand;

/**
 * A generic latex Exporter.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 17 Jan 2016
 */
public abstract class LatexExporter extends Exporter {

    /** The command to post-process the LaTeX code. */
    protected String command = "pdflatex -halt-on-error";

    /** If the LaTeX output should be printed. */
    protected boolean latexOutput = false;

    /**
     * Default constructor.
     */
    public LatexExporter() {
	super();
    }

    /**
     * Clean folders from LaTeX temporary, log, and output files
     * 
     * @throws IOException
     *             if cleaning failed.
     */
    public void cleanPDFLatexOutput() throws IOException {
	if (SystemUtils.IS_OS_WINDOWS) {
	    RunExternalCommand extCommand = new RunExternalCommand(
		    filesLocation, resourceBundle);
	    extCommand.runCommand("cmd /c del /s *.aux *.toc *.log *.out");
	} else {
	    Runtime.getRuntime().exec(
		    new String[] {
			    "/bin/sh",
			    "-c",
			    "cd " + filesLocation
				    + " && rm -rf *.aux *.toc *.log *.out"
				    + " && cd -" });
	}

    }

    /**
     * Get true if the latex output is printed.
     * 
     * @return the latexOutput
     */
    public boolean isLatexOutput() {
	return latexOutput;
    }

    /**
     * Set true to print the latex output.
     * 
     * @param latexOutput
     *            the latexOutput to set
     */
    public void setLatexOutput(boolean latexOutput) {
	this.latexOutput = latexOutput;
    }
}
