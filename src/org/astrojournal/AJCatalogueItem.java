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


    public AJCatalogueItem() {}

    // Getter and Setter
    public void setName(String name) {this.name = name; }
    public String getName() { return name; }

    public void setConstellation(String constellation) {this.constellation = constellation; }
    public String getConstellation() { return constellation; }

    public void setType(String type) {this.type = type; }
    public String getType() { return type; }

    public void setOtherName(String otherName) {this.otherName = otherName; }
    public String getOtherName() { return otherName; }

    public void setCommonName(String commonName) {this.commonName = commonName; }
    public String getCommonName() { return commonName; }

}
