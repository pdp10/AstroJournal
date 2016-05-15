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

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.absgen.LatexExporterByConst;
import org.astrojournal.generator.headfoot.LatexFooter;
import org.astrojournal.generator.headfoot.LatexHeader;
import org.astrojournal.utilities.filefilters.LaTeXFilter;

/**
 * Exports the observed targets by constellation to Latex code. This is an basic
 * exporter which uses MiniMetaDataCols and MiniDataCols enum types for column
 * export.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public class MiniLatexExporterByConst extends LatexExporterByConst {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(MiniLatexExporterByConst.class);

    /**
     * Default constructor.
     */
    public MiniLatexExporterByConst() {
	super();
    }

    @Override
    public void writeLatexMain(Writer writer, LatexHeader latexHeader,
	    LatexFooter latexFooter) throws Exception {
	// write the Latex Header
	writer.write(latexHeader.getHeader());

	// write the Latex Body
	// parse each file in the latex folder
	File[] files = new File(filesLocation + File.separator + reportFolder)
		.listFiles(new LaTeXFilter());
	if (files == null) {
	    throw new Exception("Folder " + filesLocation + File.separator
		    + reportFolder + " not found");
	}
	// sort the constellations when we parse the files
	Arrays.sort(files, NameFileComparator.NAME_COMPARATOR);

	String currConst = "", filename = "";
	// If this pathname does not denote a directory, then listFiles()
	// returns null.
	for (File file : files) {
	    filename = file.getName();
	    if (file.isFile()) {
		if (!currConst.equals(filename.substring(
			filename.indexOf("_") + 1, filename.indexOf(".")))) {
		    currConst = filename.substring(filename.indexOf("_") + 1,
			    filename.indexOf("."));
		    writer.write("\\section{" + currConst + "}\n");
		}
		// include the file removing the extension .tex
		writer.write("\\input{" + reportFolder + "/"
			+ filename.replaceFirst("[.][^.]+$", "") + "}\n");
		// writerByConst.write("\\clearpage \n");
	    }
	}

	// write the Latex Footer
	writer.write(latexFooter.getFooter());
    }

    @Override
    public void writeLatexContent(Writer writer, Report report)
	    throws IOException {
    }

    @Override
    protected void organiseTargetsByConstellation(List<Report> reports) {
	Report report = null;
	int nReports = reports.size();
	for (int i = 0; i < nReports; i++) {
	    report = reports.get(i);
	    List<String[]> targets = report.getAllData();
	    for (int j = 0; j < targets.size(); j++) {
		String[] targetEntry = targets.get(j);
		// skip solar system targets. We only consider DSOs.
		if (targetEntry[MiniDataCols.TYPE_NAME.ordinal()].toLowerCase()
			.equals("planet")
			|| targetEntry[MiniDataCols.TARGET_NAME.ordinal()]
				.toLowerCase().equals("sun")
			|| targetEntry[MiniDataCols.TARGET_NAME.ordinal()]
				.toLowerCase().equals("moon")
			|| targetEntry[MiniDataCols.TARGET_NAME.ordinal()]
				.toLowerCase().equals("milky way")) {
		    continue;
		}
		if (!constellations
			.containsKey(targetEntry[MiniDataCols.CONSTELLATION_NAME
				.ordinal()])) {
		    constellations.put(
			    targetEntry[MiniDataCols.CONSTELLATION_NAME
				    .ordinal()], new HashSet<String>());
		}
		log.debug(targetEntry[MiniDataCols.CONSTELLATION_NAME.ordinal()]
			+ " "
			+ targetEntry[MiniDataCols.TARGET_NAME.ordinal()]
			+ " ("
			+ targetEntry[MiniDataCols.TYPE_NAME.ordinal()]
			+ ")");
		constellations.get(
			targetEntry[MiniDataCols.CONSTELLATION_NAME.ordinal()])
			.add(targetEntry[MiniDataCols.TARGET_NAME.ordinal()]
				+ " ("
				+ targetEntry[MiniDataCols.TYPE_NAME.ordinal()]
				+ ")");
	    }
	}
    }

}
