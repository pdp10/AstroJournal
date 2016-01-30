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

import java.io.IOException;
import java.io.Writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.absgen.LatexExporterByDate;

/**
 * Exports an AstroJournal observation to LaTeX code. This is an extended
 * exporter which uses MiniMetaDataCols and MiniDataCols enum types for column
 * export.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 28/05/2015
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
