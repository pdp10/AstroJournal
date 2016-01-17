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

import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;
import org.astrojournal.configuration.AJConfig;
import org.astrojournal.utilities.RunExternalCommand;

/**
 * A generic AJ Latex Exporter.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 17 Jan 2016
 */
public abstract class AJLatexExporter extends AJExporter {

    /** The command to post-process the LaTeX code. */
    protected String command = "pdflatex -halt-on-error";

    /**
     * Constructor
     * 
     * @param ajConfig
     */
    public AJLatexExporter(AJConfig ajConfig) {
	super(ajConfig);
    }

    /**
     * Clean folders from LaTeX temporary, log, and output files
     * 
     * @throws IOException
     *             if cleaning failed.
     */
    public void cleanPDFLatexOutput() throws IOException {
	if (SystemUtils.IS_OS_WINDOWS) {
	    RunExternalCommand
		    .runCommand("cmd /c del /s *.aux *.toc *.log *.out");
	} else {
	    Runtime.getRuntime().exec(
		    new String[] {
			    "/bin/sh",
			    "-c",
			    "cd "
				    + ajConfig.getFilesLocation()
					    .getAbsolutePath()
				    + " && rm -rf *.aux *.toc *.log *.out"
				    + " && cd -" });
	}

    }
}
