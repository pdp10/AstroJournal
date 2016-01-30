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
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.absgen.LatexExporter;
import org.astrojournal.generator.headfoot.LatexFooter;
import org.astrojournal.generator.headfoot.LatexHeader;
import org.astrojournal.utilities.RunExternalCommand;
import org.astrojournal.utilities.filefilters.LaTeXFilter;

/**
 * Exports an AstroJournal observation to LaTeX code. This is an extended
 * exporter which uses MiniMetaDataCols and MiniDataCols enum types for column
 * export.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 28/05/2015
 */
public class MiniLatexExporterByDate extends LatexExporter {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(MiniLatexExporterByDate.class);

    /**
     * Default constructor.
     */
    public MiniLatexExporterByDate() {
	super();
    }

    /**
     * Generate the LaTeX document sorting the observation by decreasing date.
     */
    @Override
    public boolean generateJournal() {
	LatexHeader ajLatexHeaderByDate = new LatexHeader(filesLocation,
		headerFooterFolder, headerFilename);
	LatexFooter ajLatexFooterByDate = new LatexFooter(filesLocation,
		headerFooterFolder, footerFilename);
	Writer writerByDate = null;
	try {
	    writerByDate = new BufferedWriter(new OutputStreamWriter(
		    new FileOutputStream(filesLocation + File.separator
			    + reportFilename), "utf-8"));
	    // write the Latex Header
	    writerByDate.write(ajLatexHeaderByDate.getHeader());

	    // write the Latex Body
	    // Write the observation reports
	    writerByDate.write("\\section{Observation reports}\n");
	    writerByDate.write("\\vspace{4 mm}\n");
	    writerByDate.write("\\hspace{4 mm}\n");
	    // parse each file in the latex obs folder (sorted by observation
	    // increasing)
	    File[] files = new File(filesLocation + File.separator
		    + reportFolder).listFiles(new LaTeXFilter());
	    if (files == null) {
		log.warn("Folder " + filesLocation + File.separator
			+ reportFolder + " not found");
		return false;
	    }
	    Arrays.sort(files, Collections.reverseOrder());
	    // If this pathname does not denote a directory, then listFiles()
	    // returns null.
	    for (File file : files) {
		if (file.isFile()) {
		    // include the file removing the extension .tex
		    writerByDate.write("\\input{" + reportFolder + "/"
			    + file.getName().replaceFirst("[.][^.]+$", "")
			    + "}\n");
		}
	    }

	    writerByDate.write("\\clearpage \n");
	    // write the Latex Footer
	    writerByDate.write(ajLatexFooterByDate.getFooter());

	} catch (IOException ex) {
	    log.warn("Error when opening the file " + filesLocation
		    + File.separator + reportFilename);
	    log.debug("Error when opening the file " + filesLocation
		    + File.separator + reportFilename, ex);
	    return false;
	} catch (Exception ex) {
	    log.debug(ex);
	    log.error(ex, ex);
	    return false;
	} finally {
	    try {
		if (writerByDate != null)
		    writerByDate.close();
	    } catch (Exception ex) {
		log.debug(ex);
		log.error(ex, ex);
		return false;
	    }
	}
	return true;
    }

    @Override
    public boolean exportReports(List<Report> reports) {
	if (resourceBundle != null) {
	    log.info("");
	    log.info("Exporting reports by date:");
	}
	Report report = null;
	String[] metaData = null;
	int nReports = reports.size();
	boolean result = true;

	for (int i = 0; i < nReports; i++) {
	    report = reports.get(i);
	    metaData = report.getMetaData();
	    String date = metaData[MiniMetaDataCols.DATE_NAME.ordinal()];
	    log.debug("Report " + date);

	    Writer table = null;

	    String filenameOut = date;
	    filenameOut = filenameOut.substring(6, 10)
		    + filenameOut.substring(3, 5) + filenameOut.substring(0, 2);
	    // Add an additional char if this is present. This is the case in
	    // which more than one observation per day is done.
	    if (date.length() == 11) {
		filenameOut = filenameOut + date.charAt(10);
	    }

	    try {
		table = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(new File(filesLocation
				+ File.separator + reportFolder, "obs"
				+ filenameOut + ".tex")), "utf-8"));

		table.write("% General observation data\n");
		table.write("\\par");
		table.write("{\\bf "
			+ metaData[MiniMetaDataCols.DATE_NAME.ordinal()]
			+ " :} ");

		table.write("% Detailed observation data\n");
		String[] targetEntry;
		for (int j = 0; j < report.getDataRowNumber(); j++) {
		    targetEntry = report.getData(j);
		    log.debug("Target "
			    + targetEntry[MiniDataCols.TARGET_NAME.ordinal()]);

		    table.write(targetEntry[MiniDataCols.TARGET_NAME.ordinal()]);

		    if (j < report.getDataRowNumber() - 1) {
			table.write(", ");
		    } else {
			table.write(".\n");
		    }
		}
		table.write("\n");
		if (resourceBundle != null) {
		    log.info("\tExported report " + date + " ("
			    + report.getDataRowNumber() + " targets)");
		}
	    } catch (IOException ex) {
		log.error("Error when opening the file " + filesLocation
			+ File.separator + filenameOut);
		log.debug("Error when opening the file " + filesLocation
			+ File.separator + filenameOut, ex);
		result = false;
	    } catch (Exception ex) {
		log.debug(ex);
		log.error(ex, ex);
		result = false;
	    } finally {
		try {
		    if (table != null)
			table.close();
		} catch (Exception ex) {
		    log.debug(ex);
		    log.error(ex, ex);
		    return false;
		}
	    }
	}

	return result;
    }

    @Override
    public void postProcessing() throws IOException {
	// The pdflatex command must be called two times in order to
	// generate the list of contents correctly.
	String commandOutput;
	RunExternalCommand extCommand = new RunExternalCommand(filesLocation,
		resourceBundle);
	commandOutput = extCommand.runCommand(command + " " + reportFilename);
	if (!quiet && latexOutput && resourceBundle != null) {
	    log.info(commandOutput + "\n");
	}

	// A second execution is required for building the document index.
	commandOutput = extCommand.runCommand(command + " " + reportFilename);
	// if (!quiet && latexOutput && resourceBundle != null) {
	// log.info(commandOutput + "\n");
	// }

	// Add this at the end to avoid mixing with the latex command
	// output.
	if (resourceBundle != null) {
	    log.info("\t" + filesLocation + File.separator
		    + FilenameUtils.removeExtension(reportFilename) + ".pdf");
	}
	cleanPDFLatexOutput();
    }
}
