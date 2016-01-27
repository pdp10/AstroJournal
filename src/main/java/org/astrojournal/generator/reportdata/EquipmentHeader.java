package org.astrojournal.generator.reportdata;

public interface EquipmentHeader {

    /** A string containing the list of telescopes name. */
    public static final String TELESCOPES_NAME = "Telescopes";
    /** A string containing the list of eyepieces name. */
    public static final String EYEPIECES_NAME = "Eyepieces";
    /** A string containing the filters name. */
    public static final String FILTERS_NAME = "Filters";

    /**
     * Sets the telescopes for this observation
     * 
     * @param telescopes
     */
    public void setTelescopes(String telescopes);

    /**
     * Returns the telescopes for this observation
     * 
     * @return telescopes
     */
    public String getTelescopes();

    /**
     * Sets the eyepieces for this observation
     * 
     * @param eyepieces
     */
    public void setEyepieces(String eyepieces);

    /**
     * Returns the eyepieces for this observation
     * 
     * @return eyepieces
     */
    public String getEyepieces();

    /**
     * Sets the filters used in this observation
     * 
     * @param filters
     */
    public void setFilters(String filters);

    /**
     * Returns the filters used in this observation
     * 
     * @return filters
     */
    public String getFilters();

}
