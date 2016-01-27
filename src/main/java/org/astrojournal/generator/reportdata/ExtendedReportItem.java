package org.astrojournal.generator.reportdata;


public class ExtendedReportItem extends MinimalReportItem implements
	ReportItemAnnotation, ReportItemEquipment, ReportItemNotes {

    /** A string containing the constellation. */
    protected String constellation = "";
    /** A string containing the type. */
    protected String type = "";
    /** A string containing the power. */
    protected String power = "";
    /** A string containing the notes. */
    protected String notes = "";

    public ExtendedReportItem() {
    }

    @Override
    public void setConstellation(String constellation) {
	this.constellation = constellation;
    }

    @Override
    public String getConstellation() {
	return constellation;
    }

    @Override
    public void setType(String type) {
	this.type = type;
    }

    @Override
    public String getType() {
	return type;
    }

    @Override
    public void setPower(String power) {
	this.power = power;
    }

    @Override
    public String getPower() {
	return power;
    }

    @Override
    public void setNotes(String notes) {
	this.notes = notes;
    }

    @Override
    public String getNotes() {
	return notes;
    }
}
