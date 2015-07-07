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
package org.astrojournal.catalogue;

import java.util.ArrayList;


/** 
 * An identifier for a catalogue.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 05/06/2015
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
