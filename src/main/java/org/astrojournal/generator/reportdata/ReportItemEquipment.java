package org.astrojournal.generator.reportdata;

public interface ReportItemEquipment {

    /** A string containing the power name. */
    public static final String POWER_NAME = "Power";

    /**
     * Sets the magnification used for this target
     * 
     * @param power
     */
    public void setPower(String power);

    /**
     * Returns the magnification used for this target
     * 
     * @return power
     */
    public String getPower();
}
