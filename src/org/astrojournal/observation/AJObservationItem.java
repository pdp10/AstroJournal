/*
 * This file is part of AstroJournal.
 *
 * AstroJournal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AstroJournal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AstroJournal.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.astrojournal.observation;


/** 
 * An Observation object contains the data of an observation.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 13/04/2015
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
