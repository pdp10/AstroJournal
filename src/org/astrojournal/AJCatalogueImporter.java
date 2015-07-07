/*
 * Author: Piero Dalle Pezze
 * Version: 0.1
 * Created on: 05/06/2015
 */
package org.astrojournal;

import java.io.BufferedReader;
import java.io.IOException;
import org.apache.log4j.Logger;


/**
 * The parser for catalogues in AstroJournal. It imports tab separated value (tsv) files 
 * containing the catalogue items.
 */ 
public class AJCatalogueImporter {

  /** The log associated to this class */
  private static Logger log = Logger.getLogger(AJCatalogueImporter.class);  

  /** The keyword denoting the first line of the catalogue records */
  protected static String initialKeyword = AJCatalogue.CATALOGUE_NAME;


  /** Default constructor */
  public AJCatalogueImporter() {} 

  /** 
   * Imports a catalogue record 
   * @param catalogue the catalogue containing the records to import
   * @param line the current line parsed in the file (the first line of the record)
   * @param reader the buffered reader associated to the file
   * @throws IOException if reader cannot read the observation
   */
  public void importCatalogue(AJCatalogue catalogue, String line,
    BufferedReader reader) throws IOException {
    String delimiter = "\t";  
    log.debug(line);
    // copy the first line
    String[] values = line.split(delimiter);

    log.debug("Line length (A): " + values.length);
    if (values.length == 2 && values[0].equals(AJCatalogue.CATALOGUE_NAME)) {
      catalogue.setCatalogueName(values[1]);
      log.debug("values[0]" + values[0] + " values[1]=" + values[1]);
    }
    // Read the other lines for this observation
    while ((line = reader.readLine()) != null) {
      values = line.split(delimiter);
      log.debug("Line length (B): " + values.length);      

      if (values.length == 0 || values[0].equals("")) {
        break;
      }

      if (values.length == 5
          && values[0].equals(AJCatalogueItem.NAME)
          && values[1].equals(AJCatalogueItem.OTHER_NAME)
          && values[2].equals(AJCatalogueItem.TYPE_NAME)
          && values[3].equals(AJCatalogueItem.CONSTELLATION_NAME)
          && values[4].equals(AJCatalogueItem.COMMON_NAME)) {
        while ((line = reader.readLine()) != null) {
          values = line.split(delimiter);
          if (values.length != 5 ||	values[0].equals("")) {
            break;
          }
          log.debug(line);	      
          log.debug("Line length (C): " + values.length);	      
          log.debug(values[4]);
          AJCatalogueItem item = new AJCatalogueItem();
          item.setName(values[0]);
          item.setOtherName(values[1]);
          item.setType(values[2]);
          item.setConstellation(values[3]);
          item.setCommonName(values[4].replace("%", "\\%"));
          catalogue.addCatalogueItem(item);
        }
      }
    }
  }

  /** Returns the initial keyword denoting the beginning of the catalogue record 
   * @return initialKeyword
   */
  public static String getInitialKeyword() { return initialKeyword; }

}
