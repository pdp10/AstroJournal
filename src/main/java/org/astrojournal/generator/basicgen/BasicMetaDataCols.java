package org.astrojournal.generator.basicgen;

/**
 * Report meta description for the basic generator.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 28 Jan 2016
 */
public enum BasicMetaDataCols {

    /** A string containing the date name. */
    DATE_NAME("Date"),
    /** A string containing the seeing name. */
    SEEING_NAME("Seeing"),
    /** A string containing the transparency name. */
    TRANSPARENCY_NAME("Transparency"),
    /** A string containing the list of telescopes name. */
    TELESCOPES_NAME("Telescopes");

    private String colName;

    private BasicMetaDataCols(String colName) {
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
