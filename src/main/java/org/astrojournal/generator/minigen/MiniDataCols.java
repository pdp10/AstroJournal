package org.astrojournal.generator.minigen;

/**
 * Target description for the minimal generator.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public enum MiniDataCols {

    /** A string containing the target name. */
    TARGET_NAME("Target"),
    /** A string containing the constellation name. */
    CONSTELLATION_NAME("Cons"),
    /** A string containing the type name. */
    TYPE_NAME("Type");

    private String colName;

    private MiniDataCols(String colName) {
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
