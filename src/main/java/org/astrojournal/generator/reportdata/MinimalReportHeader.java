package org.astrojournal.generator.reportdata;


public class MinimalReportHeader implements ReportHeader {

    /** A string containing the date. */
    protected String date = "";

    public MinimalReportHeader() {
    }

    /**
     * Sets the observation date
     * 
     * @param date
     */
    @Override
    public void setDate(String date) {
	this.date = date;
    }

    /**
     * Returns the observation date
     * 
     * @return date
     */
    @Override
    public String getDate() {
	return date;
    }
}
