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
package org.astrojournal.generator.minigen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.absgen.LatexExporterByDate;
import org.astrojournal.generator.headfoot.LatexFooter;
import org.astrojournal.generator.headfoot.LatexHeader;
import org.astrojournal.utilities.filefilters.LaTeXFilter;

/**
 * Exports an AstroJournal observation to LaTeX code. This is an extended
 * exporter which uses MiniMetaDataCols and MiniDataCols enum types for column
 * export.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public class MiniLatexExporterByDate extends LatexExporterByDate {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(MiniLatexExporterByDate.class);

    /**
     * Default constructor.
     */
    public MiniLatexExporterByDate() {
	super();
    }

    @Override
    public boolean generateJournal() {
	LatexHeader latexHeader = new LatexHeader();
	LatexFooter latexFooter = new LatexFooter();
	Writer writer = null;
	try {
	    writer = new BufferedWriter(new OutputStreamWriter(
		    new FileOutputStream(filesLocation + File.separator
			    + reportFilename), "utf-8"));
	    writeLatexMain(writer, latexHeader, latexFooter);

	} catch (IOException ex) {
	    log.error("Error when opening the file " + filesLocation
		    + File.separator + reportFolder + File.separator
		    + reportFilename);
	    log.debug("Error when opening the file " + filesLocation
		    + File.separator + reportFolder + File.separator
		    + reportFilename, ex);
	    return false;
	} catch (Exception e) {
	    log.debug(e);
	    log.error(e, e);
	    return false;
	} finally {
	    try {
		if (writer != null)
		    writer.close();
	    } catch (Exception e) {
		log.debug(e);
		log.error(e, e);
		return false;
	    }
	}
	return true;
    }

    /**
     * This journal does not have a list of contents. This method removes the
     * section number.
     */
    @Override
    public void writeLatexMain(Writer writer, LatexHeader latexHeader,
	    LatexFooter latexFooter) throws Exception {

	// write the Latex Header
	writer.write(latexHeader.getHeader());

	// write the Latex Body
	// Write the observation reports
	writer.write("\\section*{Observation reports}\n");
	writer.write("\\vspace{4 mm}\n");
	writer.write("\\hspace{4 mm}\n");
	// parse each file in the latex obs folder (sorted by observation
	// increasing)
	// If this pathname does not denote a directory, then listFiles()
	// returns null.
	File[] files = new File(filesLocation + File.separator + reportFolder)
		.listFiles(new LaTeXFilter());
	if (files == null) {
	    throw new Exception("Folder " + filesLocation + File.separator
		    + reportFolder + " not found");
	}
	Arrays.sort(files, Collections.reverseOrder());
	String currentYear = "";
	for (File file : files) {
	    if (file.isFile()) {
		if (!currentYear.equals(file.getName().substring(0, 4))) {
		    // collect observations by year
		    currentYear = file.getName().substring(0, 4);
		    writer.write("\\subsection*{" + currentYear + "}\n");
		}
		// include the file removing the extension .tex
		writer.write("\\input{" + reportFolder + "/"
			+ file.getName().replaceFirst("[.][^.]+$", "") + "}\n");
	    }
	}

	// write the Latex Footer
	writer.write(latexFooter.getFooter());
    }

    @Override
    public void writeLatexContent(Writer writer, Report report)
	    throws IOException {
	String[] metaData = report.getMetaData();
	writer.write("% General observation data\n");
	writer.write("\\par");
	writer.write("{\\bf " + metaData[MiniMetaDataCols.DATE_NAME.ordinal()]
		+ " :} ");

	writer.write("% Detailed observation data\n");
	String[] targetEntry;
	for (int j = 0; j < report.getDataRowNumber(); j++) {
	    targetEntry = report.getData(j);
	    log.debug("Target "
		    + targetEntry[MiniDataCols.TARGET_NAME.ordinal()]);

	    writer.write(targetEntry[MiniDataCols.TARGET_NAME.ordinal()]);

	    if (j < report.getDataRowNumber() - 1) {
		writer.write(", ");
	    } else {
		writer.write(".\n");
	    }
	}
	writer.write("\n");
    }

}
