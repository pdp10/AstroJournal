package org.astrojournal.generator.extgen;

/**
 * Report meta description for the extended generator.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 28 Jan 2016
 */
public enum ExtMetaDataCols {

    /** A string containing the date name. */
    DATE_NAME("Date"),
    /** A string containing the time name. */
    TIME_NAME("Time"),
    /** A string containing the location name. */
    LOCATION_NAME("Location"),
    /** A string containing the altitude name. */
    ALTITUDE_NAME("Altitude"),
    /** A string containing the temperature name. */
    TEMPERATURE_NAME("Temperature"),
    /** A string containing the seeing name. */
    SEEING_NAME("Seeing"),
    /** A string containing the transparency name. */
    TRANSPARENCY_NAME("Transparency"),
    /** A string containing the darkness name. */
    DARKNESS_NAME("Darkness"),
    /** A string containing the list of telescopes name. */
    TELESCOPES_NAME("Telescopes"),
    /** A string containing the list of eyepieces name. */
    EYEPIECES_NAME("Eyepieces"),
    /** A string containing the filters name. */
    FILTERS_NAME("Filters");

    private String colName;

    private ExtMetaDataCols(String colName) {
	this.colName = colName;
    }

    /**
     * Return the column name.
     * 
     * @return the colName
     */
    public String getColName() {
	return this.colName;
    }
}
