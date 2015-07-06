/*
 * Author: Piero Dalle Pezze
 * Version: 0.1
 * Created on: 05/06/2015
 */
package org.astrojournal;

import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Exports an AstroJournal catalogue to Latex code.
 */
public class AJCatalogueExporter {

  /** The log associated to this class */
  private static Logger log = Logger.getLogger(AJCatalogueExporter.class);
    
  
  /** Default constructor */
  public void AJCatalogueExporter() {} 

  /** Exports a catalogue record to Latex
   * @param catalogue the catalogue to export
   * @param latexReportsFolder the folder to write the catalogue in.
   */
  public void exportCatalogue(AJCatalogue catalogue, String latexReportsFolder) {
    Writer table = null;
    
    String filenameOut = catalogue.getCatalogueName();

    ArrayList<AJCatalogueItem> catalogueItems = catalogue.getCatalogueItems();
    try {
      table = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
        new File(latexReportsFolder, "cat" + filenameOut + ".tex")), "utf-8"));

      table.write("% Catalogue name\n");
      table.write("\\begin{tabular}{ p{0.9in} p{1.3in}}\n");
      table.write("{\\bf " + AJCatalogue.CATALOGUE_NAME + ":} & "
        + catalogue.getCatalogueName() + " \\\\ \n");
      table.write("\\end{tabular}\n");
      
      table.write("% Catalogue data\n");
      table.write("\\begin{longtable}{ p{0.7in}  p{1.0in}  p{0.6in}  p{0.9in}  p{5.1in} }\n");
      table.write("\\hline \n");
      table.write("{\\bf " + AJCatalogueItem.NAME + "} & {\\bf "
        + AJCatalogueItem.OTHER_NAME + "} & {\\bf "
        + AJCatalogueItem.TYPE_NAME + "} & {\\bf "
        + AJCatalogueItem.CONSTELLATION_NAME + "} & {\\bf "
        + AJCatalogueItem.COMMON_NAME + "} \\\\ \n");

      table.write("\\hline \n");
      for (AJCatalogueItem item : catalogueItems) {
	  table.write(item.getName() + " & " + item.getOtherName().replace("-", "") + " & "
          + item.getType().replace("-", "") + " & " + item.getConstellation().replace("-", "") + " & " + item.getCommonName().replace("-", "")
          + " \\\\ \n");
      }
      table.write("\\hline \n");
      table.write("\\end{longtable} \n");
    } catch (IOException ex) {
      System.out.println("Error when opening the file");
    } catch (Exception ex) {
      System.out.println(ex);
    }
    finally {
      try {
        if (table != null)
          table.close();
      } catch (Exception ex) {
      }
    }
  }

}
