package org.astrojournal.generator.reportdata;

public interface ReportHeader {
    /** A string containing the date name. */
    public static final String DATE_NAME = "Date";

    // Getter and Setter
    /**
     * Sets the observation date
     * 
     * @param date
     */
    public void setDate(String date);

    /**
     * Returns the observation date
     * 
     * @return date
     */
    public String getDate();
}
