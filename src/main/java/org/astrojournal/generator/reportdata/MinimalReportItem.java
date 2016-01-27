package org.astrojournal.generator.reportdata;


public class MinimalReportItem implements ReportItem {

    /** A string containing the target. */
    protected String target = "";

    public MinimalReportItem() {
    }

    @Override
    public void setTarget(String target) {
	this.target = target;
    }

    @Override
    public String getTarget() {
	return target;
    }
}
