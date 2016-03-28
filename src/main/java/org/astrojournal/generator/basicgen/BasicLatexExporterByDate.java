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
package org.astrojournal.generator.basicgen;

import java.io.IOException;
import java.io.Writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.absgen.LatexExporterByDate;

/**
 * Exports an AstroJournal observation to LaTeX code. This is a basic exporter
 * which uses BasicMetaDataCols and BasicDataCols enum types for column export.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 28/05/2015
 */
public class BasicLatexExporterByDate extends LatexExporterByDate {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(BasicLatexExporterByDate.class);

    /**
     * Default constructor.
     */
    public BasicLatexExporterByDate() {
	super();
    }

    @Override
    public void writeLatexContent(Writer writer, Report report)
	    throws IOException {
	String[] metaData = report.getMetaData();
	writer.write("% General observation data\n");
	// first metadata table
	writer.write("\\begin{tabular}[t]{ll}\n");
	writer.write("{\\bf " + BasicMetaDataCols.DATE_NAME.getColName()
		+ ":} & " + metaData[BasicMetaDataCols.DATE_NAME.ordinal()]
		+ " \\\\ \n");
	if (!metaData[BasicMetaDataCols.TELESCOPES_NAME.ordinal()].isEmpty()) {
	    writer.write("{\\bf "
		    + BasicMetaDataCols.TELESCOPES_NAME.getColName() + ":} & "
		    + metaData[BasicMetaDataCols.TELESCOPES_NAME.ordinal()]
		    + " \\\\ \n");
	}
	writer.write("\\end{tabular}\n");
	writer.write("\\quad\n");

	// second metadata table
	writer.write("\\begin{tabular}[t]{ll}\n");
	if (!metaData[BasicMetaDataCols.SEEING_NAME.ordinal()].isEmpty()) {
	    writer.write("{\\bf " + BasicMetaDataCols.SEEING_NAME.getColName()
		    + ":} & "
		    + metaData[BasicMetaDataCols.SEEING_NAME.ordinal()]
		    + " \\\\ \n");
	}
	if (!metaData[BasicMetaDataCols.TRANSPARENCY_NAME.ordinal()].isEmpty()) {
	    writer.write("{\\bf "
		    + BasicMetaDataCols.TRANSPARENCY_NAME.getColName()
		    + ":} & "
		    + metaData[BasicMetaDataCols.TRANSPARENCY_NAME.ordinal()]
		    + " \\\\ \n");
	}
	writer.write("\\end{tabular}\n");

	writer.write("% Detailed observation data\n");
	writer.write("\\begin{longtable}{ p{0.7in}  p{0.3in}  p{0.6in}  p{0.9in} }\n");
	writer.write("\\hline \n");
	writer.write("{\\bf " + BasicDataCols.TARGET_NAME.getColName()
		+ "} & {\\bf " + BasicDataCols.CONSTELLATION_NAME.getColName()
		+ "} & {\\bf " + BasicDataCols.TYPE_NAME.getColName()
		+ "} & {\\bf " + BasicDataCols.POWER_NAME.getColName()
		+ "} \\\\ \n");

	writer.write("\\hline \n");

	String[] targetEntry;
	for (int j = 0; j < report.getDataRowNumber(); j++) {
	    targetEntry = report.getData(j);
	    log.debug("Target "
		    + targetEntry[BasicDataCols.TARGET_NAME.ordinal()]);

	    writer.write(targetEntry[BasicDataCols.TARGET_NAME.ordinal()]
		    + " & "
		    + targetEntry[BasicDataCols.CONSTELLATION_NAME.ordinal()]
		    + " & " + targetEntry[BasicDataCols.TYPE_NAME.ordinal()]
		    + " & " + targetEntry[BasicDataCols.POWER_NAME.ordinal()]
		    + " \\\\ \n");
	}
	writer.write("\\hline \n");
	writer.write("\\end{longtable} \n");
	writer.write("\\clearpage \n");
    }
}
