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
package org.astrojournal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.astrojournal.catalogue.AJCatalogue;
import org.astrojournal.catalogue.AJCatalogueExporter;
import org.astrojournal.catalogue.AJCatalogueImporter;
import org.astrojournal.headerfooter.AJLatexHeader;
import org.astrojournal.headerfooter.AJLatexFooter;
import org.astrojournal.observation.AJObservation;
import org.astrojournal.observation.AJObservationExporter;
import org.astrojournal.observation.AJObservationImporter;

/**
 * This class automatically generates the Latex code for the AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version 0.7
 * @since 12/04/2015
 */
public class AJMain {

  /** The log associated to this class */
  private static Logger log = Logger.getLogger(AJMain.class);

  /** The relative path containing the tsv files (observation input folder). */
  private String tsvReportsFolder = "tsv_reports";
  /** The name of the folder containing the latex observation files (observation output folder). */
  private String latexReportsFolder = "latex_reports";

  /** The relative path containing the tsv catalogue files (catalogue input folder). */
  private String tsvCataloguesFolder = "tsv_catalogues";
  /** The name of the folder containing the latex catalogue files (catalogue output folder). */
  private String latexCataloguesFolder = "latex_catalogues";

  /** The name of the main Latex file. */
  private String mainLatex = "astrojournal.tex";
  /** The name of the folder of the Latex header file inclusive with relative path. */
  private String latexHeader = "latex_header_footer/header.tex";
  /** The name of the folder of the Latex footer file inclusive with relative path. */
  private String latexFooter = "latex_header_footer/footer.tex";


  /** Default constructor */
  public AJMain() {}

  /**
   * Generates a tex file (2 tables) per observation.
   * 
   * @return true if the procedure succeeds, false otherwise.
   */
  private boolean generateObservationsLatexCode() {
    // You need to create a reader reading the file tvs
    // Then parse the tvs file and for each table found (e.g. looking for the
    // word "Date",
    // write a latex table in a file. Date is the string date
    // for each file in the folder obs (sorted by observation increasing), add a
    // line
    // If this pathname does not denote a directory, then listFiles() returns
    // null.
    File[] files = new File(tsvReportsFolder).listFiles();
    if (files == null) {
      log.warn("Folder " + tsvReportsFolder + " not found");
      return false;
    }
    Arrays.sort(files);
    AJObservation obs;
    AJObservationImporter ajImporter = new AJObservationImporter();
    AJObservationExporter ajExporter = new AJObservationExporter();
    for (File file : files) {
      if (file.isFile() && file.getName().endsWith(".tsv")) {
        // Get the current file name.
        String tsvFilename = file.getName();
        System.out.println("Processing file " + tsvFilename);
        // Create a buffered reader to read the file
        BufferedReader reader = null;
        try {
          reader = new BufferedReader(new FileReader(new File(tsvReportsFolder,
            tsvFilename)));
          String line;
          // Read all lines
          while ((line = reader.readLine()) != null) {
            log.debug(line);
            if (line.indexOf(AJObservationImporter.getInitialKeyword()) > -1) {
              obs = new AJObservation();
              // this should receive (obs, tsvReportsFolder) as input instead of 
              // (obs, line, reader) and manage the reader thing internally.
              ajImporter.importObservation(obs, line, reader);
              ajExporter.exportObservation(obs, latexReportsFolder);
              System.out.println("\tExported observation " + obs.getDate());
            }
          } // end while
        } catch (IOException ex) {
          System.out.println(ex);
          //} catch (Exception ex) {
          //  System.out.println(ex);
        }
        finally {
          try {
            if (reader != null)
              reader.close();
          } catch (IOException ex) {
          }
        }
      } // end if
    } // end for
    return true;
  }


  /**
   * Generates a tex file (2 tables) per catalogue.
   * 
   * @return true if the procedure succeeds, false otherwise.
   */
  private boolean generateCataloguesLatexCode() {
    // You need to create a reader reading the file tvs
    // Then parse the tvs file and for each table found (e.g. looking for the
    // word "List:",
    // write a latex table in a file. Date is the string date
    // for each file in the folder obs, add a
    // line
    // If this pathname does not denote a directory, then listFiles() returns
    // null.
    File[] files = new File(tsvCataloguesFolder).listFiles();
    if (files == null) {
      log.warn("Folder " + tsvCataloguesFolder + " not found");
      return false;
    }
    Arrays.sort(files);
    AJCatalogue cat;
    AJCatalogueImporter ajImporter = new AJCatalogueImporter();
    AJCatalogueExporter ajExporter = new AJCatalogueExporter();
    for (File file : files) {
      if (file.isFile() && file.getName().endsWith(".tsv")) {
        // Get the current file name.
        String tsvFilename = file.getName();
        System.out.println("Processing file " + tsvFilename);
        // Create a buffered reader to read the file
        BufferedReader reader = null;
        try {
          reader = new BufferedReader(new FileReader(new File(tsvCataloguesFolder,
            tsvFilename)));
          String line;
          // Read all lines
          while ((line = reader.readLine()) != null) {
            log.debug(line);
            if (line.indexOf(AJCatalogueImporter.getInitialKeyword()) > -1) {
              cat = new AJCatalogue();
              // this should receive (cat, tsvCatalogueFolder) as input instead of 
              // (cat, line, reader) and manage the reader thing internally.
              ajImporter.importCatalogue(cat, line, reader);
              ajExporter.exportCatalogue(cat, latexCataloguesFolder);
              System.out.println("\tExported catalogue " + cat.getCatalogueName());
            }
          } // end while
        } catch (IOException ex) {
          System.out.println(ex);
          //} catch (Exception ex) {
          //  System.out.println(ex);
        }
        finally {
          try {
            if (reader != null)
              reader.close();
          } catch (IOException ex) {
          }
        }
      } // end if
    } // end for
    return true;
  }


  /** 
   * It generates the latex files for AstroJournal. 
   * @param tsvObsDir the directory containing the tsv observation files (input)
   * @param latexObsDir the directory containing the single observations in latex format (output)
   * @param tsvCatDir the directory containing the tsv catalogue files (input)
   * @param latexCatDir the directory containing the catalogue in latex format (output)
   */
  public void generateLatexCode(String tsvObsDir, String latexObsDir, String tsvCatDir, String latexCatDir) {
    tsvReportsFolder = tsvObsDir;
    latexReportsFolder = latexObsDir;
    tsvCataloguesFolder = tsvCatDir;
    latexCataloguesFolder = latexCatDir;	
    AJLatexHeader ajLatexHeader = new AJLatexHeader(latexHeader);
    AJLatexFooter ajLatexFooter = new AJLatexFooter(latexFooter);
    Writer writer = null;
    try {
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
        mainLatex), "utf-8"));

      if (!generateObservationsLatexCode()) {
        log.warn("tsv observation file is not valid. Cannot generate Latex code for the observations.");
        return;
      }

      if (!generateCataloguesLatexCode()) {
        log.warn("tsv catalogue file is not valid. Cannot generate Latex code for the catalogues.");
        return;
      }	    
      // write the Latex Header
      writer.write(ajLatexHeader.getHeader());

      // write the Latex Body
      // Write the observation reports
      writer.write("\\section{Observation reports}\n");
      writer.write("\\vspace{4 mm}\n");
      writer.write("\\hspace{4 mm}\n");
      // parse each file in the latex obs folder (sorted by observation increasing)
      File[] files = new File(latexReportsFolder).listFiles();
      if (files == null) {
        log.warn("Folder " + latexReportsFolder + " not found");
        return;
      }
      Arrays.sort(files, Collections.reverseOrder());    
      // If this pathname does not denote a directory, then listFiles() returns null.
      for (File file : files) {
        if (file.isFile() && file.getName().endsWith(".tex")) {
          // include the file removing the extension .tex
          writer.write("\\input{" + latexReportsFolder + "/"
              + file.getName().replaceFirst("[.][^.]+$", "") + "}\n");
          writer.write("\\clearpage \n");
        }
      }
      
      // Write observed objects by catalogue
      writer.write("\n\\small\n");
      writer.write("\\section{Observed objects by catalogue}\n");
      writer.write("\\vspace{4 mm}\n");
      writer.write("\\hspace{4 mm}\n");
      // parse each file in the latex catalogue folder (sorted by catalogue id increasing)
      files = new File(latexCataloguesFolder).listFiles();
      if (files == null) {
        log.warn("Folder " + latexCataloguesFolder + " not found");
        return;
      }
      Arrays.sort(files);
      // If this pathname does not denote a directory, then listFiles() returns null.
      for (File file : files) {
        if (file.isFile() && file.getName().endsWith(".tex")) {
          // include the file removing the extension .tex
          writer.write("\\input{" + latexCataloguesFolder + "/"
              + file.getName().replaceFirst("[.][^.]+$", "") + "}\n");
          writer.write("\\clearpage \n");
        }
      }	    
      // write the Latex Footer
      writer.write(ajLatexFooter.getFooter());

    } catch (IOException ex) {
      log.warn("Error when opening the file " + mainLatex);
    } catch (Exception ex) {
      log.warn(ex);
    }
    finally {
      try {
        if (writer != null)
          writer.close();
      } catch (Exception ex) {
      }
    }
  }


  /** 
   * Main function 
   * @param args a list of 4 arguments representing the input and output folders
   */
  public static void main(String[] args) {
    AJMain ajMain = new AJMain();
    try {
      if(args.length == 4) {
        String tsvObsDir = args[0];
        String latexObsDir = args[1];
        String tsvCatDir = args[2];
        String latexCatDir = args[3];
        ajMain.generateLatexCode(tsvObsDir, latexObsDir, tsvCatDir, latexCatDir);
      } else {
        throw new Exception("Please, specify the folders : " + ajMain.tsvReportsFolder + "/ " + ajMain.latexReportsFolder + "/ " + ajMain.tsvReportsFolder + "/ and " + ajMain.latexReportsFolder + "/ as arguments.");
      }
    } catch (Exception ex) {
      log.warn(ex);
    }
  }

}
