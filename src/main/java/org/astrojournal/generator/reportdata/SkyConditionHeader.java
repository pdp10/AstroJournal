package org.astrojournal.generator.reportdata;

public interface SkyConditionHeader {

    /** A string containing the seeing name. */
    public static final String SEEING_NAME = "Seeing";
    /** A string containing the transparency name. */
    public static final String TRANSPARENCY_NAME = "Transparency";
    /** A string containing the darkness name. */
    public static final String DARKNESS_NAME = "Darkness";

    /**
     * Sets the observation seeing
     * 
     * @param seeing
     */
    public void setSeeing(String seeing);

    /**
     * Returns the observation seeing
     * 
     * @return seeing
     */
    public String getSeeing();

    /**
     * Sets the observation transparency
     * 
     * @param transparency
     */
    public void setTransparency(String transparency);

    /**
     * Returns the observation transparency
     * 
     * @return transparency
     */
    public String getTransparency();

    /**
     * Sets the observation sky darkness as measured via SQM-L sky meter quality
     * 
     * @param darkness
     */
    public void setSkyDarkness(String darkness);

    /**
     * Returns the observation sky darkness as measured via SQM-L sky meter
     * quality
     * 
     * @return sky darkness
     */
    public String getDarkness();

}
