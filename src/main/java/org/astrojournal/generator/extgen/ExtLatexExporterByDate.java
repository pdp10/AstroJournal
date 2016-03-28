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
package org.astrojournal.generator.extgen;

import java.io.IOException;
import java.io.Writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.absgen.LatexExporterByDate;

/**
 * Exports an AstroJournal observation to LaTeX code. This is an extended
 * exporter which uses ExtMetaDataCols and ExtDataCols enum types for column
 * export.
 * 
 * @author Piero Dalle Pezze
 * @version 0.3
 * @since 28/05/2015
 */
public class ExtLatexExporterByDate extends LatexExporterByDate {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(ExtLatexExporterByDate.class);

    /**
     * Default constructor.
     */
    public ExtLatexExporterByDate() {
	super();
    }

    @Override
    public void writeLatexContent(Writer writer, Report report)
	    throws IOException {
	String[] metaData = report.getMetaData();

	writer.write("% General observation data\n");

	// first metadata table
	writer.write("\\begin{tabular}[t]{ll}\n");
	writer.write("{\\bf " + ExtMetaDataCols.DATE_NAME.getColName()
		+ ":} & " + metaData[ExtMetaDataCols.DATE_NAME.ordinal()]
		+ " \\\\ \n");
	if (!metaData[ExtMetaDataCols.TIME_NAME.ordinal()].isEmpty()) {
	    writer.write("{\\bf " + ExtMetaDataCols.TIME_NAME.getColName()
		    + ":} & " + metaData[ExtMetaDataCols.TIME_NAME.ordinal()]
		    + " \\\\ \n");
	}
	if (!metaData[ExtMetaDataCols.LOCATION_NAME.ordinal()].isEmpty()) {
	    writer.write("{\\bf " + ExtMetaDataCols.LOCATION_NAME.getColName()
		    + ":} & "
		    + metaData[ExtMetaDataCols.LOCATION_NAME.ordinal()]
		    + " \\\\ \n");
	}
	if (!metaData[ExtMetaDataCols.ALTITUDE_NAME.ordinal()].isEmpty()) {
	    writer.write("{\\bf " + ExtMetaDataCols.ALTITUDE_NAME.getColName()
		    + ":} & "
		    + metaData[ExtMetaDataCols.ALTITUDE_NAME.ordinal()]
		    + " \\\\ \n");
	}
	if (!metaData[ExtMetaDataCols.LUNAR_PHASE_NAME.ordinal()].isEmpty()) {
	    writer.write("{\\bf "
		    + ExtMetaDataCols.LUNAR_PHASE_NAME.getColName()
		    + ":} & "
		    + metaData[ExtMetaDataCols.LUNAR_PHASE_NAME.ordinal()]
			    .replace("%", "\\%") + " \\\\ \n");
	}
	writer.write("\\end{tabular}\n");
	writer.write("\\quad\n");

	// second metadata table
	writer.write("\\begin{tabular}[t]{ll}\n");
	if (!metaData[ExtMetaDataCols.TEMPERATURE_NAME.ordinal()].isEmpty()) {
	    writer.write("{\\bf "
		    + ExtMetaDataCols.TEMPERATURE_NAME.getColName() + ":} & "
		    + metaData[ExtMetaDataCols.TEMPERATURE_NAME.ordinal()]
		    + " \\\\ \n");
	}
	if (!metaData[ExtMetaDataCols.SEEING_NAME.ordinal()].isEmpty()) {
	    writer.write("{\\bf " + ExtMetaDataCols.SEEING_NAME.getColName()
		    + ":} & " + metaData[ExtMetaDataCols.SEEING_NAME.ordinal()]
		    + " \\\\ \n");
	}
	if (!metaData[ExtMetaDataCols.TRANSPARENCY_NAME.ordinal()].isEmpty()) {
	    writer.write("{\\bf "
		    + ExtMetaDataCols.TRANSPARENCY_NAME.getColName() + ":} & "
		    + metaData[ExtMetaDataCols.TRANSPARENCY_NAME.ordinal()]
		    + " \\\\ \n");
	}
	if (!metaData[ExtMetaDataCols.DARKNESS_NAME.ordinal()].isEmpty()) {
	    writer.write("{\\bf " + ExtMetaDataCols.DARKNESS_NAME.getColName()
		    + ":} & "
		    + metaData[ExtMetaDataCols.DARKNESS_NAME.ordinal()]
		    + " \\\\ \n");
	}
	if (!metaData[ExtMetaDataCols.TELESCOPES_NAME.ordinal()].isEmpty()) {
	    writer.write("{\\bf "
		    + ExtMetaDataCols.TELESCOPES_NAME.getColName() + ":} & "
		    + metaData[ExtMetaDataCols.TELESCOPES_NAME.ordinal()]
		    + " \\\\ \n");
	}
	if (!metaData[ExtMetaDataCols.EYEPIECES_NAME.ordinal()].isEmpty()) {
	    writer.write("{\\bf " + ExtMetaDataCols.EYEPIECES_NAME.getColName()
		    + ":} & "
		    + metaData[ExtMetaDataCols.EYEPIECES_NAME.ordinal()]
		    + " \\\\ \n");
	}
	if (!metaData[ExtMetaDataCols.FILTERS_NAME.ordinal()].isEmpty()) {
	    writer.write("{\\bf " + ExtMetaDataCols.FILTERS_NAME.getColName()
		    + ":} & "
		    + metaData[ExtMetaDataCols.FILTERS_NAME.ordinal()]
		    + " \\\\ \n");
	}
	writer.write("\\end{tabular}\n");

	writer.write("% Detailed observation data\n");
	writer.write("\\begin{longtable}{ p{0.7in}  p{0.3in}  p{0.6in}  p{0.9in}  p{5.8in} }\n");
	writer.write("\\hline \n");
	writer.write("{\\bf " + ExtDataCols.TARGET_NAME.getColName()
		+ "} & {\\bf " + ExtDataCols.CONSTELLATION_NAME.getColName()
		+ "} & {\\bf " + ExtDataCols.TYPE_NAME.getColName()
		+ "} & {\\bf " + ExtDataCols.POWER_NAME.getColName()
		+ "} & {\\bf " + ExtDataCols.NOTES_NAME.getColName()
		+ "} \\\\ \n");

	writer.write("\\hline \n");

	String[] targetEntry;
	for (int j = 0; j < report.getDataRowNumber(); j++) {
	    targetEntry = report.getData(j);
	    log.debug("Target "
		    + targetEntry[ExtDataCols.TARGET_NAME.ordinal()]);

	    writer.write(targetEntry[ExtDataCols.TARGET_NAME.ordinal()] + " & "
		    + targetEntry[ExtDataCols.CONSTELLATION_NAME.ordinal()]
		    + " & " + targetEntry[ExtDataCols.TYPE_NAME.ordinal()]
		    + " & " + targetEntry[ExtDataCols.POWER_NAME.ordinal()]
		    + " & " + targetEntry[ExtDataCols.NOTES_NAME.ordinal()]
		    + " \\\\ \n");
	}
	writer.write("\\hline \n");
	writer.write("\\end{longtable} \n");
	writer.write("\\clearpage \n");
    }
}
