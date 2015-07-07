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
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Exports an AstroJournal catalogue to Latex code.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 05/06/2015
 */
public class AJCatalogueExporter {

  /** The log associated to this class */
  private static Logger log = Logger.getLogger(AJCatalogueExporter.class);


  /** Default constructor */
  public AJCatalogueExporter() {} 

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
        log.debug("writing catalogue item " + item.getName());
        table.write(item.getName() + " & " 
            + item.getOtherName().replace("-", "") + " & "
            + item.getType().replace("-", "") + " & " 
            + item.getConstellation().replace("-", "") + " & " 
            + item.getCommonName().replace("-", "")
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
