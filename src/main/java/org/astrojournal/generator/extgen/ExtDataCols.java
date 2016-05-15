package org.astrojournal.generator.extgen;

/**
 * Target description for the extended generator.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public enum ExtDataCols {

    /** A string containing the target name. */
    TARGET_NAME("Target"),
    /** A string containing the constellation name. */
    CONSTELLATION_NAME("Cons"),
    /** A string containing the type name. */
    TYPE_NAME("Type"),
    /** A string containing the power name. */
    POWER_NAME("Power"),
    /** A string containing the notes name. */
    NOTES_NAME("Notes");

    private String colName;

    private ExtDataCols(String colName) {
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
