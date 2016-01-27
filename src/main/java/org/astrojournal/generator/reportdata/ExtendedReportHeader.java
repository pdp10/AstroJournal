package org.astrojournal.generator.reportdata;


public class ExtendedReportHeader extends MinimalReportHeader implements
	TimePlaceHeader, SkyConditionHeader, EquipmentHeader {

    /** A string containing the time. */
    protected String time = "";
    /** A string containing the location. */
    protected String location = "";
    /** A string containing the altitude. */
    protected String altitude = "";
    /** A string containing the temperature. */
    protected String temperature = "";

    /** A string containing the seeing. */
    protected String seeing = "";
    /** A string containing the transparency. */
    protected String transparency = "";
    /**
     * A string containing the sky darkness as measured via SQM-L sky meter
     * quality.
     */
    protected String darkness = "";

    /** A string containing the list of telescopes. */
    protected String telescopes = "";
    /** A string containing the list of eyepieces. */
    protected String eyepieces = "";
    /** A string containing the filters. */
    protected String filters = "";

    public ExtendedReportHeader() {
    }

    @Override
    public void setTime(String time) {
	this.time = time;
    }

    @Override
    public String getTime() {
	return time;
    }

    @Override
    public void setLocation(String location) {
	this.location = location;
    }

    @Override
    public String getLocation() {
	return location;
    }

    @Override
    public void setAltitude(String altitude) {
	this.altitude = altitude;
    }

    @Override
    public String getAltitude() {
	return altitude;
    }

    @Override
    public void setTemperature(String temperature) {
	this.temperature = temperature;
    }

    @Override
    public String getTemperature() {
	return temperature;
    }

    @Override
    public void setSeeing(String seeing) {
	this.seeing = seeing;
    }

    @Override
    public String getSeeing() {
	return seeing;
    }

    @Override
    public void setTransparency(String transparency) {
	this.transparency = transparency;
    }

    @Override
    public String getTransparency() {
	return transparency;
    }

    @Override
    public void setSkyDarkness(String darkness) {
	this.darkness = darkness;
    }

    @Override
    public String getDarkness() {
	return darkness;
    }

    @Override
    public void setTelescopes(String telescopes) {
	this.telescopes = telescopes;
    }

    @Override
    public String getTelescopes() {
	return telescopes;
    }

    @Override
    public void setEyepieces(String eyepieces) {
	this.eyepieces = eyepieces;
    }

    @Override
    public String getEyepieces() {
	return eyepieces;
    }

    @Override
    public void setFilters(String filters) {
	this.filters = filters;
    }

    @Override
    public String getFilters() {
	return filters;
    }
}
