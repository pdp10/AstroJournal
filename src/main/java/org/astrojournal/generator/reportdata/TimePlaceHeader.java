package org.astrojournal.generator.reportdata;

public interface TimePlaceHeader extends ReportHeader {

    /** A string containing the time name. */
    public static final String TIME_NAME = "Time";
    /** A string containing the location name. */
    public static final String LOCATION_NAME = "Location";
    /** A string containing the altitude name. */
    public static final String ALTITUDE_NAME = "Altitude";
    /** A string containing the temperature name. */
    public static final String TEMPERATURE_NAME = "Temperature";

    /**
     * Sets the observation time
     * 
     * @param time
     */
    public void setTime(String time);

    /**
     * Returns the observation time
     * 
     * @return time
     */
    public String getTime();

    /**
     * Sets the observation location
     * 
     * @param location
     */
    public void setLocation(String location);

    /**
     * Returns the observation location
     * 
     * @return location
     */
    public String getLocation();

    /**
     * Sets the observation altitude
     * 
     * @param altitude
     */
    public void setAltitude(String altitude);

    /**
     * Returns the observation altitude
     * 
     * @return altitude
     */
    public String getAltitude();

    /**
     * Sets the observation temperature
     * 
     * @param temperature
     */
    public void setTemperature(String temperature);

    /**
     * Returns the observation temperature
     * 
     * @return temperature
     */
    public String getTemperature();

}
