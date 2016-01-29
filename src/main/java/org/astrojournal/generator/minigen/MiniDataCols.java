package org.astrojournal.generator.minigen;

/**
 * Target description for the minimal generator.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 28 Jan 2016
 */
public enum MiniDataCols {

    /** A string containing the target name. */
    TARGET_NAME("Target");

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
