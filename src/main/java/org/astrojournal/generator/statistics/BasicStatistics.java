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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.mutable.MutableFloat;
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
    // TODO : consider splitting these statistics in different files.

    /**
     * A counter for each type of object.
     */
    private HashMap<String, MutableInt> typeCount = new HashMap<String, MutableInt>();

    /**
     * A counter for each location including information about seeing,
     * transparency and darkness.
     */
    private HashMap<String, MutableInt> countLocations = new HashMap<String, MutableInt>();
    private HashMap<String, ArrayList<ArrayList<Float>>> locationWeatherCount = new HashMap<String, ArrayList<ArrayList<Float>>>();

    /**
     * A counter for the reports per year
     */
    private HashMap<String, MutableInt> reportsYearCount = new HashMap<String, MutableInt>();

    /**
     * A counter for the average number of reports for each month.
     */
    private HashMap<String, MutableFloat> monthlyReportsAvg = new HashMap<String, MutableFloat>();

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
	typeCount.clear();
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
			    incrementIntMap(typeCount, entry.trim()
				    .toLowerCase());
			}
		    }
		}
	    }

	    // extract the reports per year AND avg reports per month
	    if (metaData.length > ExtMetaDataCols.DATE_NAME.ordinal()) {
		entry = metaData[ExtMetaDataCols.DATE_NAME.ordinal()];
		if (!entry.isEmpty()) {
		    incrementIntMap(reportsYearCount, entry.substring(6, 10));
		    incrementFloatMap(monthlyReportsAvg, entry.substring(3, 5));
		}
	    }

	    // extract the location
	    if (metaData.length > ExtMetaDataCols.LOCATION_NAME.ordinal()) {
		entry = metaData[ExtMetaDataCols.LOCATION_NAME.ordinal()];

		float seeing = 0.0f, transparency = 0.0f, darkness = 0.0f;
		String number;
		if (metaData.length > ExtMetaDataCols.SEEING_NAME.ordinal()) {
		    try {
			number = metaData[ExtMetaDataCols.SEEING_NAME.ordinal()];
			seeing = Float.parseFloat(number.substring(0,
				Math.min(1, number.length())));
		    } catch (NumberFormatException e) {
			seeing = 0.0f;
		    }
		}
		if (metaData.length > ExtMetaDataCols.TRANSPARENCY_NAME
			.ordinal()) {
		    try {
			number = metaData[ExtMetaDataCols.TRANSPARENCY_NAME
				.ordinal()];
			transparency = Float.parseFloat(number.substring(0,
				Math.min(1, number.length())));
		    } catch (NumberFormatException e) {
			transparency = 0.0f;
		    }
		}
		if (metaData.length > ExtMetaDataCols.DARKNESS_NAME.ordinal()) {
		    try {
			number = metaData[ExtMetaDataCols.DARKNESS_NAME
				.ordinal()];
			darkness = Float.parseFloat(number.substring(0,
				Math.min(4, number.length())));
		    } catch (NumberFormatException e) {
			darkness = 0.0f;
		    }
		}
		if (!entry.isEmpty()) {
		    incrementIntMap(countLocations, entry.trim().toLowerCase());
		    incrementLocationWeatherMap(entry.trim().toLowerCase(),
			    seeing, transparency, darkness);
		}
	    }
	}

	// post processing

	// Average reports per month
	// Scale by the number of years
	String[] keys = monthlyReportsAvg.keySet().toArray(new String[0]);
	for (String key : keys) {
	    monthlyReportsAvg.get(key).setValue(
		    monthlyReportsAvg.get(key).floatValue()
			    / reportsYearCount.size());
	}

	return true;
    }

    private void incrementLocationWeatherMap(String type, float seeing,
	    float transparency, float darkness) {
	if (!locationWeatherCount.containsKey(type)) {
	    locationWeatherCount.put(type, new ArrayList<ArrayList<Float>>(3));
	    locationWeatherCount.get(type).add(new ArrayList<Float>());
	    locationWeatherCount.get(type).add(new ArrayList<Float>());
	    locationWeatherCount.get(type).add(new ArrayList<Float>());
	}

	if (seeing != 0.0f)
	    locationWeatherCount.get(type).get(0).add(new Float(seeing));
	if (transparency != 0.0f)
	    locationWeatherCount.get(type).get(1).add(new Float(transparency));
	if (darkness != 0.0f)
	    locationWeatherCount.get(type).get(2).add(new Float(darkness));
    }

    /**
     * Increment the counting for this target type.
     * 
     * @param map
     *            The map to use
     * @param type
     *            The type to increment.
     */
    private void incrementIntMap(HashMap<String, MutableInt> map, String type) {
	if (!map.containsKey(type)) {
	    map.put(type, new MutableInt());
	}
	map.get(type).increment();
    }

    /**
     * Increment the counting for this target type.
     * 
     * @param map
     *            The map to use
     * @param type
     *            The type to increment.
     */
    private void incrementFloatMap(HashMap<String, MutableFloat> map,
	    String type) {
	if (!map.containsKey(type)) {
	    map.put(type, new MutableFloat());
	}
	map.get(type).increment();
    }

    /**
     * Return the counting for the target types.
     * 
     * @return the counting object
     */
    public HashMap<String, MutableInt> getTypeCount() {
	return typeCount;
    }

    /**
     * Return the counting for the locations.
     * 
     * @return the counting object
     */
    public HashMap<String, MutableInt> getLocationCount() {
	return countLocations;
    }

    /**
     * Return the counting for the locations with relative measures about
     * seeing, transparency and darkness.
     * 
     * @return the counting object
     */
    public HashMap<String, ArrayList<ArrayList<Float>>> getLocationWeatherCount() {
	return locationWeatherCount;
    }

    /**
     * Return the counting of reports per year.
     * 
     * @return the counting object
     */
    public HashMap<String, MutableInt> getReportsYearCount() {
	return reportsYearCount;
    }

    /**
     * Return the average number of reports per month
     * 
     * @return the counting object
     */
    public HashMap<String, MutableFloat> getMonthlyReportsAvg() {
	return monthlyReportsAvg;
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
    public int getIntCount(HashMap<String, MutableInt> map, String type) {
	if (map.containsKey(type)) {
	    return map.get(type).intValue();
	}
	return 0;
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
    public float getFloatCount(HashMap<String, MutableFloat> map, String type) {
	if (map.containsKey(type)) {
	    return map.get(type).floatValue();
	}
	return 0;
    }

    /**
     * Return the averages for seeing (0), transparency (1) and darkness (2).
     * 
     * @param type
     *            the target type
     * @param index
     *            the index of the reader. Seeing (0), transparency (1) and
     *            darkness (2).
     * @return the reading average
     */
    public float getLocationWeatherAvgs(String type, int index) {
	if (locationWeatherCount.containsKey(type) && index >= 0 && index <= 2) {
	    ArrayList<Float> reading = locationWeatherCount.get(type)
		    .get(index);
	    float sum = 0;
	    for (Float f : reading) {
		sum = sum + f.floatValue();
	    }
	    return sum / reading.size();
	}
	return 0;
    }
}
