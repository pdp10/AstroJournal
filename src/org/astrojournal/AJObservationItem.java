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


  /**
   * Creates an observation item
   */
  public AJObservationItem() {}

  // Getter and Setter
  /**
   * Sets the target name
   * @param target
   */
  public void setTarget(String target) {this.target = target; }
  
  /**
   * Returns the target name
   * @return target
   */
  public String getTarget() { return target; }

  /**
   * Sets the target constellation
   * @param constellation
   */
  public void setConstellation(String constellation) {this.constellation = constellation; }
  
  /**
   * Returns the target constellation
   * @return constellation
   */
  public String getConstellation() { return constellation; }

  /**
   * Sets the target type
   * @param type
   */
  public void setType(String type) {this.type = type; }
  
  /**
   * Returns the target type
   * @return type
   */
  public String getType() { return type; }

  /**
   * Sets the magnification used for this target
   * @param power
   */
  public void setPower(String power) {this.power = power; }
  
  /**
   * Returns the magnification used for this target
   * @return power
   */
  public String getPower() { return power; }

  /**
   * Sets the notes about this target
   * @param notes
   */
  public void setNotes(String notes) {this.notes = notes; }
  
  /**
   * Returns the notes about this target
   * @return notes
   */
  public String getNotes() { return notes; }

}
