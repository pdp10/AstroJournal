/* 
 * Author: Piero Dalle Pezze
 * Version: 0.1
 * Created on: 05/06/2015
 */
package org.astrojournal;

import java.util.ArrayList;


/** 
 * An identifier for a catalogue.
 */
public class AJCatalogue {

    /** A string containing the catalogue name identifier. */
    public static final String CATALOGUE_NAME = "Catalogue";

    /** A string containing the catalogue name. */
    private String catalogueName = "";

    private ArrayList<AJCatalogueItem> catalogueItems = new ArrayList<AJCatalogueItem>();


    /** Default constructor */
    public AJCatalogue() {}

    // Getter and Setter
    public void setCatalogueName(String catalogueName) {this.catalogueName = catalogueName; }
    public String getCatalogueName() { return catalogueName; }

    public void addCatalogueItem(AJCatalogueItem catalogueItem) { catalogueItems.add(catalogueItem); }
    public ArrayList<AJCatalogueItem> getCatalogueItems() { return catalogueItems; }

}
