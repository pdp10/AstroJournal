/* 
 * Author: Piero Dalle Pezze
 * Version: 0.1
 * Created on: 13/04/2015
 */
package org.astrojournal;

import java.util.ArrayList;


/** 
 * An Observation object contains the data of an observation.
 */
public class Observation {

    /** A string containing the date name. */
    public static final String DATENAME = "Date";
    /** A string containing the time name. */
    public static final String TIMENAME = "Time";
    /** A string containing the location name. */
    public static final String LOCATIONNAME = "Location";
    /** A string containing the altitude name. */
    public static final String ALTITUDENAME = "Altitude (MAMSL)";
    /** A string containing the temperature name. */
    public static final String TEMPERATURENAME = "Temperature (C)";
    /** A string containing the seeing name. */
    public static final String SEEINGNAME = "Seeing (Antoniadi scale)";
    /** A string containing the transparency name. */
    public static final String TRANSPARENCYNAME = "Transparency (AAAA scale)";

    /** A string containing the list of telescopes name. */
    public static final String TELESCOPESNAME = "Telescopes";
    /** A string containing the list of eyepieces name. */
    public static final String EYEPIECESNAME = "Eyepieces";
    /** A string containing the list of power/exit pupil/fov name. */
    public static final String POWEREXITPUPILFOVNAME = "Power, Exit pupil, FOV";
    /** A string containing the filters name. */
    public static final String FILTERSNAME = "Filters";


    /** A string containing the date. */
    private String date = "";
    /** A string containing the time. */
    private String time = "";
    /** A string containing the location. */
    private String location = "";
    /** A string containing the altitude. */
    private String altitude = "";
    /** A string containing the temperature. */
    private String temperature = "";
    /** A string containing the seeing. */
    private String seeing = "";
    /** A string containing the transparency. */
    private String transparency = "";

    /** A string containing the list of telescopes. */
    private String telescopes = "";
    /** A string containing the list of eyepieces. */
    private String eyepieces = "";
    /** A string containing the list of power/exit pupil/fov. */
    private String powerExitPupilFOV = "";
    /** A string containing the filters. */
    private String filters = "";


    private ArrayList<ObservationItem> observationItems = new ArrayList<ObservationItem>();



    /** Default constructor */
    public Observation() {}

    // Getter and Setter
    public void setDate(String date) {this.date = date; }
    public String getDate() { return date; }

    public void setTime(String time) {this.time = time; }
    public String getTime() { return time; }

    public void setLocation(String location) {this.location = location; }
    public String getLocation() { return location; }

    public void setAltitude(String altitude) {this.altitude = altitude; }
    public String getAltitude() { return altitude; }

    public void setTemperature(String temperature) {this.temperature = temperature; }
    public String getTemperature() { return temperature; }

    public void setSeeing(String seeing) {this.seeing = seeing; }
    public String getSeeing() { return seeing; }

    public void setTransparency(String transparency) {this.transparency = transparency; }
    public String getTransparency() { return transparency; }

    public void setTelescopes(String telescopes) {this.telescopes = telescopes; }
    public String getTelescopes() { return telescopes; }

    public void setEyepieces(String eyepieces) {this.eyepieces = eyepieces; }
    public String getEyepieces() { return eyepieces; }

    public void setPowerExitPupilFOV(String powerExitPupilFOV) {this.powerExitPupilFOV = powerExitPupilFOV; }
    public String getPowerExitPupilFOV() { return powerExitPupilFOV; }

    public void setFilters(String filters) {this.filters = filters; }
    public String getFilters() { return filters; }

    public void addObservationItem(ObservationItem obsItem) { observationItems.add(obsItem); }
    public ArrayList<ObservationItem> getObservationItems() { return observationItems; }

}
