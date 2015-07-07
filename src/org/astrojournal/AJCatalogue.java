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
  /**
   * Sets the catalogueName
   * @param catalogueName
   */
  public void setCatalogueName(String catalogueName) {
    this.catalogueName = catalogueName; 
  }
  /**
   * Returns the catalogue name
   * @return the catalogue name
   */
  public String getCatalogueName() { 
    return catalogueName; 
  }

  /**
   * Adds an item to the catalogue
   * @param catalogueItem
   */
  public void addCatalogueItem(AJCatalogueItem catalogueItem) { 
    catalogueItems.add(catalogueItem); 
  }
  
  /**
   * Returns the list of catalogue items
   * @return the catalogue items
   */
  public ArrayList<AJCatalogueItem> getCatalogueItems() { 
    return catalogueItems; 
  }

}
