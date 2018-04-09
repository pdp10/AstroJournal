package org.astrojournal.generator.basicgen;

/**
 * Target description for the basic generator.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public enum BasicDataCols {

    /** A string containing the target name. */
    TARGET_NAME("Target"),
    /** A string containing the constellation name. */
    CONSTELLATION_NAME("Cons"),
    /** A string containing the type name. */
    TYPE_NAME("Type"),
    /** A string containing the power name. */
    POWER_NAME("Power");

    private String colName;

    private BasicDataCols(String colName) {
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
