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
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.absgen.TextExporterByDateSGL;
import org.astrojournal.generator.extgen.ExtMetaDataCols;

/**
 * Exports an AstroJournal observation to txt for Stargazers Lounge reports.
 * This is a basic exporter which uses BasicMetaDataCols and BasicDataCols enum
 * types for column export.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 11/09/2015
 */
public class BasicTextExporterByDateSGL extends TextExporterByDateSGL {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(BasicTextExporterByDateSGL.class);

    /**
     * Default constructor
     */
    public BasicTextExporterByDateSGL() {
	super();
    }

    @Override
    protected void writeTextContent(Writer writer, Report report)
	    throws IOException {
	String[] metaData = report.getMetaData();
	List<String[]> targets = report.getAllData();
	writer.write(BasicMetaDataCols.DATE_NAME.getColName() + ": "
		+ metaData[BasicMetaDataCols.DATE_NAME.ordinal()] + "\n");

	if (!metaData[ExtMetaDataCols.SEEING_NAME.ordinal()].isEmpty()) {
	    writer.write(ExtMetaDataCols.SEEING_NAME.getColName() + " "
		    + metaData[ExtMetaDataCols.SEEING_NAME.ordinal()] + "\n");
	}
	if (!metaData[ExtMetaDataCols.TRANSPARENCY_NAME.ordinal()].isEmpty()) {
	    writer.write(ExtMetaDataCols.TRANSPARENCY_NAME.getColName() + " "
		    + metaData[ExtMetaDataCols.TRANSPARENCY_NAME.ordinal()]
		    + "\n");
	}
	if (!metaData[ExtMetaDataCols.TELESCOPES_NAME.ordinal()].isEmpty()) {
	    writer.write(ExtMetaDataCols.TELESCOPES_NAME.getColName() + " "
		    + metaData[ExtMetaDataCols.TELESCOPES_NAME.ordinal()]
		    + "\n");
	}
	writer.write("\n");

	for (String[] targetEntry : targets) {
	    log.debug("Target "
		    + targetEntry[BasicDataCols.TARGET_NAME.ordinal()]);
	    writer.write(targetEntry[BasicDataCols.TARGET_NAME.ordinal()] + " "
		    + targetEntry[BasicDataCols.CONSTELLATION_NAME.ordinal()]
		    + " " + targetEntry[BasicDataCols.TYPE_NAME.ordinal()]
		    + " " + targetEntry[BasicDataCols.POWER_NAME.ordinal()]
		    + "\n");
	}
    }

}
