/* 
 * Author: Piero Dalle Pezze
 * Version: 0.1
 * Created on: 05/06/2015
 */
package org.astrojournal;


/** 
 * The item of a catalogue.
 */
public class AJCatalogueItem {

  /** A string containing the target name. */
  public static final String NAME = "Name";
  /** A string containing the other name. */
  public static final String OTHER_NAME = "Other Name";
  /** A string containing the type name. */
  public static final String TYPE_NAME = "Type";
  /** A string containing the constellation name. */
  public static final String CONSTELLATION_NAME = "Cons";
  /** A string containing the common name. */
  public static final String COMMON_NAME = "Common Name";

  /** A string containing the target name. */
  private String name = "";
  /** A string containing the other name. */
  private String otherName = "";
  /** A string containing the type. */
  private String type = "";
  /** A string containing the constellation. */
  private String constellation = "";
  /** A string containing the common name. */
  private String commonName = "";


  /**
   * Creates a catalogue record
   */
  public AJCatalogueItem() {}

  // Getter and Setter
  /**
   * Sets the target name
   * @param name
   */
  public void setName(String name) {this.name = name; }

  /**
   * Returns the target name
   * @return name
   */
  public String getName() { return name; }

  /**
   * Sets the constellation containing the target
   * @param constellation
   */
  public void setConstellation(String constellation) {this.constellation = constellation; }

  /**
   * Returns the constellation containing the target
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
   * Sets the other name for the target
   * @param otherName
   */
  public void setOtherName(String otherName) {this.otherName = otherName; }

  /**
   * Returns the other name for the target
   * @return otherName
   */
  public String getOtherName() { return otherName; }

  /**
   * Sets the common name for the target
   * @param commonName
   */
  public void setCommonName(String commonName) {this.commonName = commonName; }

  /**
   * Returns the common name for the target
   * @return commonName
   */
  public String getCommonName() { return commonName; }

}
