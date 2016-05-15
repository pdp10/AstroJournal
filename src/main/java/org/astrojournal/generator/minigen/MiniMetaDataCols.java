package org.astrojournal.generator.minigen;

/**
 * Report meta description for the minimal generator.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public enum MiniMetaDataCols {

    /** A string containing the date name. */
    DATE_NAME("Date");

    private String colName;

    private MiniMetaDataCols(String colName) {
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
