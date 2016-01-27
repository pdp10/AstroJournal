/*
 * Copyright 2015 Piero Dalle Pezze
 *
 * This file is part of AstroJournal.
 *
 * AstroJournal is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
/*
 * Changelog:
 * - Piero Dalle Pezze: class creation.
 */
package org.astrojournal.generator.DEPRECobservation;

import java.util.ArrayList;

/**
 * An Observation object contains the data of an observation.
 * 
 * @author Piero Dalle Pezze
 * @version 0.3
 * @since 13/04/2015
 */
public class AJObservation {

    /** A string containing the date name. */
    public static final String DATE_NAME = "Date";
    /** A string containing the time name. */
    public static final String TIME_NAME = "Time";
    /** A string containing the location name. */
    public static final String LOCATION_NAME = "Location";
    /** A string containing the altitude name. */
    public static final String ALTITUDE_NAME = "Altitude";
    /** A string containing the temperature name. */
    public static final String TEMPERATURE_NAME = "Temperature";
    /** A string containing the seeing name. */
    public static final String SEEING_NAME = "Seeing";
    /** A string containing the transparency name. */
    public static final String TRANSPARENCY_NAME = "Transparency";
    /** A string containing the darkness name. */
    public static final String DARKNESS_NAME = "Darkness";

    /** A string containing the list of telescopes name. */
    public static final String TELESCOPES_NAME = "Telescopes";
    /** A string containing the list of eyepieces name. */
    public static final String EYEPIECES_NAME = "Eyepieces";
    /** A string containing the filters name. */
    public static final String FILTERS_NAME = "Filters";

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
    /**
     * A string containing the sky darkness as measured via SQM-L sky meter
     * quality.
     */
    private String darkness = "";

    /** A string containing the list of telescopes. */
    private String telescopes = "";
    /** A string containing the list of eyepieces. */
    private String eyepieces = "";
    /** A string containing the filters. */
    private String filters = "";

    private ArrayList<AJObservationItem> observationItems = new ArrayList<AJObservationItem>();

    /** Default constructor */
    public AJObservation() {
    }

    // Getter and Setter
    /**
     * Sets the observation date
     * 
     * @param date
     */
    public void setDate(String date) {
	this.date = date;
    }

    /**
     * Returns the observation date
     * 
     * @return date
     */
    public String getDate() {
	return date;
    }

    /**
     * Sets the observation time
     * 
     * @param time
     */
    public void setTime(String time) {
	this.time = time;
    }

    /**
     * Returns the observation time
     * 
     * @return time
     */
    public String getTime() {
	return time;
    }

    /**
     * Sets the observation location
     * 
     * @param location
     */
    public void setLocation(String location) {
	this.location = location;
    }

    /**
     * Returns the observation location
     * 
     * @return location
     */
    public String getLocation() {
	return location;
    }

    /**
     * Sets the observation altitude
     * 
     * @param altitude
     */
    public void setAltitude(String altitude) {
	this.altitude = altitude;
    }

    /**
     * Returns the observation altitude
     * 
     * @return altitude
     */
    public String getAltitude() {
	return altitude;
    }

    /**
     * Sets the observation temperature
     * 
     * @param temperature
     */
    public void setTemperature(String temperature) {
	this.temperature = temperature;
    }

    /**
     * Returns the observation temperature
     * 
     * @return temperature
     */
    public String getTemperature() {
	return temperature;
    }

    /**
     * Sets the observation seeing
     * 
     * @param seeing
     */
    public void setSeeing(String seeing) {
	this.seeing = seeing;
    }

    /**
     * Returns the observation seeing
     * 
     * @return seeing
     */
    public String getSeeing() {
	return seeing;
    }

    /**
     * Sets the observation transparency
     * 
     * @param transparency
     */
    public void setTransparency(String transparency) {
	this.transparency = transparency;
    }

    /**
     * Returns the observation transparency
     * 
     * @return transparency
     */
    public String getTransparency() {
	return transparency;
    }

    /**
     * Sets the observation sky darkness as measured via SQM-L sky meter quality
     * 
     * @param darkness
     */
    public void setSkyDarkness(String darkness) {
	this.darkness = darkness;
    }

    /**
     * Returns the observation sky darkness as measured via SQM-L sky meter
     * quality
     * 
     * @return sky darkness
     */
    public String getDarkness() {
	return darkness;
    }

    /**
     * Sets the telescopes for this observation
     * 
     * @param telescopes
     */
    public void setTelescopes(String telescopes) {
	this.telescopes = telescopes;
    }

    /**
     * Returns the telescopes for this observation
     * 
     * @return telescopes
     */
    public String getTelescopes() {
	return telescopes;
    }

    /**
     * Sets the eyepieces for this observation
     * 
     * @param eyepieces
     */
    public void setEyepieces(String eyepieces) {
	this.eyepieces = eyepieces;
    }

    /**
     * Returns the eyepieces for this observation
     * 
     * @return eyepieces
     */
    public String getEyepieces() {
	return eyepieces;
    }

    /**
     * Sets the filters used in this observation
     * 
     * @param filters
     */
    public void setFilters(String filters) {
	this.filters = filters;
    }

    /**
     * Returns the filters used in this observation
     * 
     * @return filters
     */
    public String getFilters() {
	return filters;
    }

    /**
     * Add an observation target record
     * 
     * @param obsItem
     */
    public void addObservationItem(AJObservationItem obsItem) {
	observationItems.add(obsItem);
    }

    /**
     * Returns the list of observation target records.
     * 
     * @return observationItems
     */
    public ArrayList<AJObservationItem> getObservationItems() {
	return observationItems;
    }

}
