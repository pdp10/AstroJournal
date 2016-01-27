package org.astrojournal.generator.reportdata;

public interface ReportItem {

    /** A string containing the target name. */
    public static final String TARGET_NAME = "Target";

    /**
     * Sets the target name
     * 
     * @param target
     */
    public void setTarget(String target);

    /**
     * Returns the target name
     * 
     * @return target
     */
    public String getTarget();

}
