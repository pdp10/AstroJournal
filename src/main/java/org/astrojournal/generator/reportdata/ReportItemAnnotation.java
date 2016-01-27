package org.astrojournal.generator.reportdata;

public interface ReportItemAnnotation extends ReportItem {

    /** A string containing the constellation name. */
    public static final String CONSTELLATION_NAME = "Cons";
    /** A string containing the type name. */
    public static final String TYPE_NAME = "Type";

    /**
     * Sets the target constellation
     * 
     * @param constellation
     */
    public void setConstellation(String constellation);

    /**
     * Returns the target constellation
     * 
     * @return constellation
     */
    public String getConstellation();

    /**
     * Sets the target type
     * 
     * @param type
     */
    public void setType(String type);

    /**
     * Returns the target type
     * 
     * @return type
     */
    public String getType();
}
