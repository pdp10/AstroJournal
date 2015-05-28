/* 
 * Author: Piero Dalle Pezze
 * Version: 0.2
 * Created on: 13/04/2015
 */
package org.astrojournal;




/** 
 * An Observation object contains the data of an observation.
 */
public class AJObservationItem {

    /** A string containing the target name. */
    public static final String TARGET_NAME = "Target";
    /** A string containing the constellation name. */
    public static final String CONSTELLATION_NAME = "Cons";
    /** A string containing the type name. */
    public static final String TYPE_NAME = "Type";
    /** A string containing the power name. */
    public static final String POWER_NAME = "Power";
    /** A string containing the notes name. */
    public static final String NOTES_NAME = "Notes";

    /** A string containing the target. */
    private String target = "";
    /** A string containing the constellation. */
    private String constellation = "";
    /** A string containing the type. */
    private String type = "";
    /** A string containing the power. */
    private String power = "";
    /** A string containing the notes. */
    private String notes = "";


    public AJObservationItem() {}

    // Getter and Setter
    public void setTarget(String target) {this.target = target; }
    public String getTarget() { return target; }

    public void setConstellation(String constellation) {this.constellation = constellation; }
    public String getConstellation() { return constellation; }

    public void setType(String type) {this.type = type; }
    public String getType() { return type; }

    public void setPower(String power) {this.power = power; }
    public String getPower() { return power; }

    public void setNotes(String notes) {this.notes = notes; }
    public String getNotes() { return notes; }

}
