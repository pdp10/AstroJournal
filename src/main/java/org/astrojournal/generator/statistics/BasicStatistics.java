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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.mutable.MutableInt;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.extgen.ExtDataCols;
import org.astrojournal.generator.extgen.ExtMetaDataCols;

/**
 * A collector of statistics
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public class BasicStatistics {

    /**
     * A counter for each type of object.
     */
    private HashMap<String, MutableInt> countType = new HashMap<String, MutableInt>();

    /**
     * A counter for each location.
     */
    private HashMap<String, MutableInt> countLocations = new HashMap<String, MutableInt>();

    /**
     * Constructor.
     * 
     */
    public BasicStatistics() {
    }

    /**
     * Reset the statistics.
     */
    public void reset() {
	countType.clear();
	countLocations.clear();
    }

    /**
     * Extract statistics from the reports.
     * 
     * @param reports
     *            The report to process.
     * @return true if no error occurred.
     */
    public boolean process(List<Report> reports) {
	String entry = "";
	Set<String> processedTargets = new HashSet<String>();
	for (int i = 0; i < reports.size(); i++) {
	    Report report = reports.get(i);
	    String[] metaData = report.getMetaData();
	    List<String[]> targets = report.getAllData();

	    // extract and count the type
	    if (!targets.isEmpty()
		    && targets.get(0).length > ExtDataCols.TYPE_NAME.ordinal()) {
		for (int j = 0; j < targets.size(); j++) {
		    String[] targetEntry = targets.get(j);
		    // Process targets only once.
		    if (!processedTargets
			    .contains(targetEntry[ExtDataCols.TARGET_NAME
				    .ordinal()].trim().toLowerCase())) {
			processedTargets
				.add(targetEntry[ExtDataCols.TARGET_NAME
					.ordinal()].trim().toLowerCase());
			entry = targetEntry[ExtDataCols.TYPE_NAME.ordinal()];
			if (!entry.isEmpty()) {
			    incrementMap(countType, entry.trim().toLowerCase());
			}
		    }
		}

	    }
	    // extract the location
	    if (metaData.length > ExtMetaDataCols.LOCATION_NAME.ordinal()) {
		entry = metaData[ExtMetaDataCols.LOCATION_NAME.ordinal()];
		if (!entry.isEmpty()) {
		    incrementMap(countLocations, entry.trim().toLowerCase());
		}
	    }

	    /**
	     * CHECK THE SIZE OF METADATA - SEE ABOVE!! entry =
	     * metaData[ExtMetaDataCols.SEEING_NAME.ordinal()]; if
	     * (!entry.isEmpty()) { } entry =
	     * metaData[ExtMetaDataCols.TRANSPARENCY_NAME.ordinal()]; if
	     * (!entry.isEmpty()) { } entry =
	     * metaData[ExtMetaDataCols.DARKNESS_NAME.ordinal()]; if
	     * (!entry.isEmpty()) { }
	     */
	}
	return true;
    }

    /**
     * Increment the counting for this target type.
     * 
     * @param map
     *            The map to use
     * @param type
     *            The type to increment.
     */
    private void incrementMap(HashMap<String, MutableInt> map, String type) {
	if (!map.containsKey(type)) {
	    map.put(type, new MutableInt());
	}
	map.get(type).increment();
    }

    /**
     * Return the counting for the target types.
     * 
     * @return the counting object
     */
    public HashMap<String, MutableInt> getCountType() {
	return countType;
    }

    /**
     * Return the counting for the locations.
     * 
     * @return the counting object
     */
    public HashMap<String, MutableInt> getCountLocation() {
	return countLocations;
    }

    /**
     * Return the counting for this target type.
     * 
     * @param map
     *            The map to use
     * @param type
     *            the target type
     * @return the count
     */
    public int getCount(HashMap<String, MutableInt> map, String type) {
	if (countType.containsKey(type)) {
	    return countType.get(type).intValue();
	}
	return 0;
    }

}
