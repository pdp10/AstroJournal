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

package org.astrojournal.generator.statistics;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A generic latex Exporter.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public class LatexStatistics {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(LatexStatistics.class);

    /** The LaTeX filename for storing the statistics. */
    protected String basicStatisticsFilename = "BasicStatistics.tex";

    /**
     * Default constructor.
     */
    public LatexStatistics() {
    }

    /**
     * Write all the statistics to a LaTeX file.
     * 
     * @param writer
     *            The writer to write the statistics in LaTeX format.
     * 
     * @param basicStatistics
     *            The statistics to write
     * 
     * @return true if the file was written correctly
     * @throws IOException
     *             if the statistics are not written correctly.
     */
    public boolean writeAll(Writer writer, BasicStatistics basicStatistics)
	    throws IOException {

	boolean value = true;
	log.debug("Writing basic statistics for the target type.");
	writer.write("% Basic statistics for the target type. \n");

	value = value && writeTypeCounts(writer, basicStatistics);
	writer.write("\\vspace{1cm} \n");

	value = value && writeLocationCount(writer, basicStatistics);
	writer.write("\\vspace{1cm} \n");

	value = value && writeReportsYearCount(writer, basicStatistics);
	writer.write("\\vspace{1cm} \n");

	value = value && writeMonthlyReportsAvg(writer, basicStatistics);
	writer.write("\\vspace{1cm} \n");

	writer.write("\\clearpage \n\n");

	return value;
    }

    /**
     * Write the counts of target type.
     * 
     * @param writer
     *            The writer to write the statistics in LaTeX format.
     * 
     * @param basicStatistics
     *            The statistics to write
     * 
     * @return true if the file was written correctly
     * @throws IOException
     *             if the statistics are not written correctly.
     */
    public boolean writeTypeCounts(Writer writer,
	    BasicStatistics basicStatistics) throws IOException {
	writer.write("\\begin{tabular}[t]{ll} \n");
	writer.write("\\hline \n");
	writer.write("{\\bf Target Type } & {\\bf Count} \\\\ \n");
	writer.write("\\hline \n");
	// Let's sort the elements for improving readability
	HashMap<String, MutableInt> typeCount = basicStatistics.getTypeCount();
	String[] sortedKeys = typeCount.keySet().toArray(new String[0]);
	Arrays.sort(sortedKeys);
	for (String key : sortedKeys) {
	    log.debug("Count(" + key.toUpperCase() + "): "
		    + basicStatistics.getIntCount(typeCount, key));
	    writer.write(key.toUpperCase() + " & "
		    + basicStatistics.getIntCount(typeCount, key) + "\\\\ \n");
	}
	writer.write("\\hline \n");
	writer.write("\\end{tabular} \n");
	return true;
    }

    /**
     * Write the location count to a LaTeX file.
     * 
     * @param writer
     *            The writer to write the statistics in LaTeX format.
     * 
     * @param basicStatistics
     *            The statistics to write
     * 
     * @return true if the file was written correctly
     * @throws IOException
     *             if the statistics are not written correctly.
     */
    public boolean writeLocationCount(Writer writer,
	    BasicStatistics basicStatistics) throws IOException {
	// Location counts
	writer.write("\\begin{tabular}[t]{ll} \n");
	writer.write("\\hline \n");
	writer.write("{\\bf Location } & {\\bf Reports} \\\\ \n");
	writer.write("\\hline \n");
	// Let's sort the elements for improving readability
	HashMap<String, MutableInt> locationCount = basicStatistics
		.getLocationCount();
	String[] sortedKeys = locationCount.keySet().toArray(new String[0]);
	Arrays.sort(sortedKeys);
	for (String key : sortedKeys) {
	    // Only print the first 30 chars.
	    log.debug("Count("
		    + key.substring(0, Math.min(30, key.length()))
			    .toUpperCase() + "): "
		    + basicStatistics.getIntCount(locationCount, key));
	    writer.write(key.substring(0, Math.min(30, key.length()))
		    .toUpperCase()
		    + " & "
		    + basicStatistics.getIntCount(locationCount, key)
		    + "\\\\ \n");
	}
	writer.write("\\hline \n");
	writer.write("\\end{tabular} \n");
	return true;
    }

    /**
     * Write the report per year count to a LaTeX file.
     * 
     * @param writer
     *            The writer to write the statistics in LaTeX format.
     * 
     * @param basicStatistics
     *            The statistics to write
     * 
     * @return true if the file was written correctly
     * @throws IOException
     *             if the statistics are not written correctly.
     */
    public boolean writeReportsYearCount(Writer writer,
	    BasicStatistics basicStatistics) throws IOException {
	// Reports per year
	writer.write("\\begin{tabular}[t]{ll} \n");
	writer.write("\\hline \n");
	writer.write("{\\bf Year } & {\\bf Reports} \\\\ \n");
	writer.write("\\hline \n");
	// Let's sort the elements for improving readability
	HashMap<String, MutableInt> reportsYearCount = basicStatistics
		.getReportsYearCount();
	String[] sortedKeys = reportsYearCount.keySet().toArray(new String[0]);
	Arrays.sort(sortedKeys);
	for (String key : sortedKeys) {
	    log.debug("Count(" + key.toUpperCase() + "): "
		    + basicStatistics.getIntCount(reportsYearCount, key));
	    writer.write(key.toUpperCase() + " & "
		    + basicStatistics.getIntCount(reportsYearCount, key)
		    + "\\\\ \n");
	}
	writer.write("\\hline \n");
	writer.write("\\end{tabular} \n");
	writer.write("\\vspace{1cm} \n");
	return true;
    }

    /**
     * Write the average number of monthly reports to a LaTeX file.
     * 
     * @param writer
     *            The writer to write the statistics in LaTeX format.
     * 
     * @param basicStatistics
     *            The statistics to write
     * 
     * @return true if the file was written correctly
     * @throws IOException
     *             if the statistics are not written correctly.
     */
    public boolean writeMonthlyReportsAvg(Writer writer,
	    BasicStatistics basicStatistics) throws IOException {
	// Average reports per month
	writer.write("\\begin{tabular}[t]{ll} \n");
	writer.write("\\hline \n");
	writer.write("{\\bf Month } & {\\bf Avg Reports} \\\\ \n");
	writer.write("\\hline \n");
	// Let's sort the elements for improving readability
	HashMap<String, MutableFloat> monthlyReportsAvg = basicStatistics
		.getMonthlyReportsAvg();
	String[] sortedKeys = monthlyReportsAvg.keySet().toArray(new String[0]);
	Arrays.sort(sortedKeys);
	for (String key : sortedKeys) {
	    log.debug("Count("
		    + new DateFormatSymbols().getMonths()[Integer.parseInt(key) - 1]
		    + "): "
		    + basicStatistics.getFloatCount(monthlyReportsAvg, key));
	    writer.write(new DateFormatSymbols().getMonths()[Integer
		    .parseInt(key) - 1]
		    + " & "
		    + basicStatistics.getFloatCount(monthlyReportsAvg, key)
		    + "\\\\ \n");
	}
	writer.write("\\hline \n");
	writer.write("\\end{tabular} \n");
	return true;
    }

}
