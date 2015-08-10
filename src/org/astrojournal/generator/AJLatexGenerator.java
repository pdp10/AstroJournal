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
package org.astrojournal.generator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.apache.log4j.Logger;
import org.astrojournal.catalogue.AJCatalogue;
import org.astrojournal.generator.AJObservationExporterByDate;
import org.astrojournal.generator.AJObservationExporterByTarget;
import org.astrojournal.headerfooter.AJLatexHeader;
import org.astrojournal.headerfooter.AJLatexFooter;
import org.astrojournal.observation.AJObservation;

/**
 * This class automatically generates the Latex code for the AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version 0.8
 * @since 12/04/2015
 */
public class AJLatexGenerator {

  /** The log associated to this class */
  private static Logger log = Logger.getLogger(AJLatexGenerator.class);

  /** The relative path containing the tsv files (observation input folder). */
  private String tsvReportsFolder = "tsv_reports";
  /** The name of the folder containing the latex observation files by date (observation output folder). */
  private String latexReportsFolderByDate = "latex_reports_by_date";
  /** The name of the folder containing the latex observation files by target (observation output folder). */
  private String latexReportsFolderByTarget = "latex_reports_by_target";

  /** The relative path containing the tsv catalogue files (catalogue input folder). */
  private String tsvCataloguesFolder = "tsv_catalogues";
  /** The name of the folder containing the latex catalogue files (catalogue output folder). */
  private String latexCataloguesFolder = "latex_catalogues";

  /** The name of the main Latex file sorted by date. */
  private String mainLatexByDate = "astrojournal_by_date.tex";
  /** The name of the main Latex file sorted by target. */
  private String mainLatexByTarget = "astrojournal_by_target.tex";

  /** The Latex header with path for astrojournal by date. */
  private String latexHeaderByDate = "latex_header_footer/header_by_date.tex";
  /** The Latex footer with path for astrojournal by date. */
  private String latexFooterByDate = "latex_header_footer/footer_by_date.tex";
  /** The Latex header with path for astrojournal by target. */
  private String latexHeaderByTarget = "latex_header_footer/header_by_target.tex";
  /** The Latex footer with path for astrojournal by target. */
  private String latexFooterByTarget = "latex_header_footer/footer_by_target.tex";

  /** The list of observations. */
  private ArrayList<AJObservation> observations = new ArrayList<AJObservation>(1000);

  /** True if the observations have been processed. */
  private boolean observationsProcessed = false;
  /** True if the catalogues have been processed. */  
  private boolean cataloguesProcessed = false;

  /** A comparator for sorting catalogues */ 
  Comparator<String> catalogueItemComparator = new Comparator<String>() {
    @Override
    public int compare(String o1, String o2) {
      return extractInt(o1) - extractInt(o2);
    }
    int extractInt(String s) {
      String num = s.replaceAll("\\D", "");
      // return 0 if no digits found
      return num.isEmpty() ? 0 : Integer.parseInt(num);
    }
  };
  

  /** Default constructor */
  public AJLatexGenerator() {}

  /** 
   * It generates the latex files for AstroJournal. 
   * @param tsvObsDir the directory containing the tsv observation files (input)
   * @param latexObsByDateDir the directory containing the single observations by date in latex format (output)
   * @param latexObsByTargetDir the directory containing the single observations by target in latex format (output)
   * @param tsvCatDir the directory containing the tsv catalogue files (input)
   * @param latexCatDir the directory containing the catalogue in latex format (output)
   */
  public void generateLatexCode(String tsvObsDir, String latexObsByDateDir, String latexObsByTargetDir, String tsvCatDir, String latexCatDir) {
    generateLatexCodeByDate(tsvObsDir, latexObsByDateDir, tsvCatDir, latexCatDir);
    generateLatexCodeByTarget(tsvObsDir, latexObsByTargetDir);
  }


  /**
   * Generate the Latex document sorted by date
   * @param tsvObsDir the directory containing the tsv observation files (input)
   * @param latexObsByDateDir the directory containing the single observations by date in latex format (output)
   * @param tsvCatDir the directory containing the tsv catalogue files (input)
   * @param latexCatDir the directory containing the catalogue in latex format (output)
   */
  public void generateLatexCodeByDate(String tsvObsDir, String latexObsByDateDir, String tsvCatDir, String latexCatDir) {
    tsvReportsFolder = tsvObsDir;
    latexReportsFolderByDate = latexObsByDateDir;
    tsvCataloguesFolder = tsvCatDir;
    latexCataloguesFolder = latexCatDir;      
    AJLatexHeader ajLatexHeaderByDate = new AJLatexHeader(latexHeaderByDate);
    AJLatexFooter ajLatexFooterByDate = new AJLatexFooter(latexFooterByDate);
    Writer writerByDate = null;
    try {
      writerByDate = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
        mainLatexByDate), "utf-8"));

      if (!generateObservationsLatexCode(true, false)) {
        log.warn("tsv observation file is not valid. Cannot generate Latex code for the observations.");
        return;
      }

      if (!generateCataloguesLatexCode()) {
        log.warn("tsv catalogue file is not valid. Cannot generate Latex code for the catalogues.");
        return;
      }     
      // write the Latex Header
      writerByDate.write(ajLatexHeaderByDate.getHeader());

      // write the Latex Body
      // Write the observation reports
      writerByDate.write("\\section{Observation reports}\n");
      writerByDate.write("\\vspace{4 mm}\n");
      writerByDate.write("\\hspace{4 mm}\n");
      // parse each file in the latex obs folder (sorted by observation increasing)
      File[] files = new File(latexReportsFolderByDate).listFiles();
      if (files == null) {
        log.warn("Folder " + latexReportsFolderByDate + " not found");
        return;
      }
      Arrays.sort(files, Collections.reverseOrder());    
      // If this pathname does not denote a directory, then listFiles() returns null.
      for (File file : files) {
        if (file.isFile() && file.getName().endsWith(".tex")) {
          // include the file removing the extension .tex
          writerByDate.write("\\input{" + latexReportsFolderByDate + "/"
              + file.getName().replaceFirst("[.][^.]+$", "") + "}\n");
          writerByDate.write("\\clearpage \n");
        }
      }

      // Write observed objects by catalogue
      writerByDate.write("\n\\small\n");
      writerByDate.write("\\section{Observed objects by catalogue}\n");
      writerByDate.write("\\vspace{4 mm}\n");
      writerByDate.write("\\hspace{4 mm}\n");
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
          writerByDate.write("\\input{" + latexCataloguesFolder + "/"
              + file.getName().replaceFirst("[.][^.]+$", "") + "}\n");
          writerByDate.write("\\clearpage \n");
        }
      }     
      // write the Latex Footer
      writerByDate.write(ajLatexFooterByDate.getFooter());

    } catch (IOException ex) {
      log.warn("Error when opening the file " + mainLatexByDate);
    } catch (Exception ex) {
      log.warn(ex);
    }
    finally {
      try {
        if (writerByDate != null)
          writerByDate.close();
      } catch (Exception ex) {
      }
    }    
  }


  /**
   * Generate the Latex document sorted by target.
   * @param tsvObsDir the directory containing the tsv observation files (input)
   * @param latexObsByTargetDir the directory containing the single observations by target in latex format (output)
   */
  public void generateLatexCodeByTarget(String tsvObsDir, String latexObsByTargetDir) {
    tsvReportsFolder = tsvObsDir;
    latexReportsFolderByTarget = latexObsByTargetDir;
    AJLatexHeader ajLatexHeaderByTarget = new AJLatexHeader(latexHeaderByTarget);
    AJLatexFooter ajLatexFooterByTarget = new AJLatexFooter(latexFooterByTarget);
    Writer writerByTarget = null;
    try {
      writerByTarget = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
        mainLatexByTarget), "utf-8"));

      if (!generateObservationsLatexCode(false, true)) {
        log.warn("tsv observation file is not valid. Cannot generate Latex code for the observations.");
        return;
      }

      // write the Latex Header
      writerByTarget.write(ajLatexHeaderByTarget.getHeader());

      // write the Latex Body
      // Write the observation reports
      // parse each file in the latex obs folder (sorted by observation increasing)
      File[] files = new File(latexReportsFolderByTarget).listFiles();
      if (files == null) {
        log.warn("Folder " + latexReportsFolderByTarget + " not found");
        return;
      }
      sortFilesByTarget(files);
      // If this pathname does not denote a directory, then listFiles() returns null.
      String target = null, type = "";
      for (File file : files) {
        target = file.getName();

        if (file.isFile() && target.endsWith(".tex")) {
          if(target.matches("^(sun|moon|mercury|venus|mars|jupiter|saturn|uranus|neptune|pluto|Sun|Moon|Mercury|Venus|Mars|Jupiter|Saturn|Uranus|Neptune|Pluto).*$")) {      
            if(!type.equals("Solar System")) {
              type = "Solar System";
              writerByTarget.write("\\clearpage\n");
              writerByTarget.write("\\section{" + type + "}\n");                  
            }
          } else if(target.matches("^[m|M][0-9].*$")) {
            if(!type.equals("Messier Catalogue")) {
              type = "Messier Catalogue";
              writerByTarget.write("\\clearpage\n");               
              writerByTarget.write("\\section{" + type + "}\n");
            }
          } else if(target.matches("^(ngc|NGC)[0-9].*$")) {
            if(!type.equals("New General Catalogue (NGC)")) {
              type = "New General Catalogue (NGC)";
              writerByTarget.write("\\clearpage\n");                  
              writerByTarget.write("\\section{" + type + "}\n");
            }
          } else if(target.matches("^(ic|IC)[0-9].*$")) {
            if(!type.equals("Index Catalogue (IC)")) {
              type = "Index Catalogue (IC)";
              writerByTarget.write("\\clearpage\n");                  
              writerByTarget.write("\\section{" + type + "}\n");
            }
          } else if(target.matches("^(stock|Stock)[0-9].*$")) {
            if(!type.equals("Stock Catalogue")) {
              type = "Stock Catalogue";
              writerByTarget.write("\\clearpage\n");                  
              writerByTarget.write("\\section{" + type + "}\n");
            }
          } else if(target.matches("^(mel|Mel)[0-9].*$")) {
            if(!type.equals("Melotte Catalogue")) {
              type = "Melotte Catalogue";
              writerByTarget.write("\\clearpage\n");                  
              writerByTarget.write("\\section{" + type + "}\n");
            }
          } else if(target.matches("^(cr|Cr)[0-9].*$")) {
            if(!type.equals("Collider Catalogue")) {
              type = "Collider Catalogue";
              writerByTarget.write("\\clearpage\n");                  
              writerByTarget.write("\\section{" + type + "}\n");
            }
          } else {
            if(!type.equals("Stars, Double Stars, Multiple Stars")) {
              type = "Stars, Double Stars, Multiple Stars";
              writerByTarget.write("\\clearpage\n");                  
              writerByTarget.write("\\section{" + type + "}\n");
            }
          }
          // include the file removing the extension .tex
          writerByTarget.write("\\input{" + latexReportsFolderByTarget + "/"
              + target.replaceFirst("[.][^.]+$", "") + "}\n");
          writerByTarget.write("\\vspace{4 mm}\n");
        }
      }
      // write the Latex Footer
      writerByTarget.write(ajLatexFooterByTarget.getFooter());

    } catch (IOException ex) {
      log.warn("Error when opening the file " + mainLatexByTarget);
    } catch (Exception ex) {
      log.warn(ex);
    }
    finally {
      try {
        if (writerByTarget != null)
          writerByTarget.close();
      } catch (Exception ex) {
      }
    }    
  }

  /**
   * Generates a Latex file (2 tables) per observation.
   * 
   * @return true if the procedure succeeds, false otherwise.
   */
  private boolean generateObservationsLatexCode(boolean observationsByDate, boolean observationsByTarget) {
    if(!observationsProcessed) {
      File[] files = new File(tsvReportsFolder).listFiles();
      if (files == null) {
        log.warn("Folder " + tsvReportsFolder + " not found");
        return false;
      }
      Arrays.sort(files);
      AJObservation obs;
      AJObservationImporter ajImporter = new AJObservationImporter();
      AJObservationExporterByDate ajExporterByDate = new AJObservationExporterByDate();
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
                // import a new observation
                ajImporter.importObservation(obs, line, reader);
                // Add the new observation to the list of observations
                observations.add(obs);
                // export the imported observation by date to Latex
                if(observationsByDate) {
                  ajExporterByDate.exportObservation(obs, latexReportsFolderByDate);
                  System.out.println("\tExported observation " + obs.getDate());
                }
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
      observationsProcessed = true;
    }
    if(observationsByTarget) {
      // All observations, if any, have been loaded
      // Now, export them by target to Latex
      System.out.println("\nExporting observation by targets:");
      AJObservationExporterByTarget ajExporterByTarget = new AJObservationExporterByTarget();
      return ajExporterByTarget.exportObservations(observations, latexReportsFolderByTarget);      
    }
    return true;
  }


  /**
   * Generates a tex file (2 tables) per catalogue.
   * 
   * @return true if the procedure succeeds, false otherwise.
   */
  private boolean generateCataloguesLatexCode() {
    if(!cataloguesProcessed) {
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
      cataloguesProcessed = true;
    }
    return true;
  }


  /** 
   * Sort the files by target.  
   * @param files the files to be sorted by target
   */
  private void sortFilesByTarget(File[] files) {
    // solar system in ArrayList instead of simple array, because we do not know
    // how many conjunctions there are.
    LinkedList<String> solarSystem = new LinkedList<String>();  
    ArrayList<String> messier = new ArrayList<String>(110);
    ArrayList<String> ngc = new ArrayList<String>(10000);
    ArrayList<String> ic = new ArrayList<String>(1000);
    ArrayList<String> stock = new ArrayList<String>(100);
    ArrayList<String> melotte = new ArrayList<String>(400);
    ArrayList<String> collider = new ArrayList<String>(300);
    ArrayList<String> stars = new ArrayList<String>(500);

    // Add empty data for the solar system. Conjuctions will be added in the end.
    solarSystem.add(""); solarSystem.add("");
    solarSystem.add(""); solarSystem.add("");
    solarSystem.add(""); solarSystem.add("");
    solarSystem.add(""); solarSystem.add(""); 
    solarSystem.add(""); solarSystem.add("");    
    String target = null;
    for(int i=0; i<files.length; i++) {
      target = files[i].getName();
      if(target.matches("^(sun|moon|mercury|venus|mars|jupiter|saturn|uranus|neptune|pluto|Sun|Moon|Mercury|Venus|Mars|Jupiter|Saturn|Uranus|Neptune|Pluto).*$")) {
        if(target.matches("^(sun|Sun)\\.tex$")) 
        { solarSystem.remove(0); solarSystem.add(0, files[i].toString()); }
        else if(target.matches("^(moon|Moon)\\.tex$")) 
        { solarSystem.remove(1); solarSystem.add(1, files[i].toString()); } 
        else if(target.matches("^(mercury|Mercury)\\.tex$"))
        { solarSystem.remove(2); solarSystem.add(2, files[i].toString()); }
        else if(target.matches("^(venus|Venus)\\.tex$")) 
        { solarSystem.remove(3); solarSystem.add(3, files[i].toString()); }
        else if(target.matches("^(mars|Mars)\\.tex$"))  
        { solarSystem.remove(4); solarSystem.add(4, files[i].toString()); }
        else if(target.matches("^(jupiter|Jupiter)\\.tex$")) 
        { solarSystem.remove(5); solarSystem.add(5, files[i].toString()); }
        else if(target.matches("^(saturn|Saturn)\\.tex$"))  
        { solarSystem.remove(6); solarSystem.add(6, files[i].toString()); }
        else if(target.matches("^(uranus|Uranus)\\.tex$"))   
        { solarSystem.remove(7); solarSystem.add(7, files[i].toString()); }
        else if(target.matches("^(neptune|Neptune)\\.tex$")) 
        { solarSystem.remove(8); solarSystem.add(8, files[i].toString()); }
        else if(target.matches("^(pluto|Pluto)\\.tex$"))     
        { solarSystem.remove(9); solarSystem.add(9, files[i].toString()); } 
        // conjunctions
        else { solarSystem.add(files[i].toString()); }
        log.debug(target);
      } else if(target.matches("^[m|M][0-9].*$")) {
        messier.add(files[i].toString());
        log.debug(target);
      } else if(target.matches("^(ngc|NGC)[0-9].*$")) {
        ngc.add(files[i].toString());
        log.debug(target);
      } else if(target.matches("^(ic|IC)[0-9].*$")) {
        ic.add(files[i].toString());
        log.debug(target);
      } else if(target.matches("^(stock|Stock)[0-9].*$")) {
        stock.add(files[i].toString());
        log.debug(target);  
      } else if(target.matches("^(mel|Mel)[0-9].*$")) {
        melotte.add(files[i].toString());
        log.debug(target);  
      } else if(target.matches("^(cr|Cr)[0-9].*$")) {
        collider.add(files[i].toString());
        log.debug(target);
      } else {
        stars.add(files[i].toString());
        log.debug(target);
      }
    }
    // note planets are manually sorted
    Collections.sort(messier, catalogueItemComparator);
    Collections.sort(ngc, catalogueItemComparator);
    Collections.sort(ic, catalogueItemComparator);
    Collections.sort(stock, catalogueItemComparator);
    Collections.sort(melotte, catalogueItemComparator);
    Collections.sort(collider, catalogueItemComparator);
    // normal lexico-graphical sorting for stars 
    Collections.sort(stars);

    int j=0;
    j = addSortedFiles(solarSystem, files, j);
    j = addSortedFiles(messier, files, j);
    j = addSortedFiles(ngc, files, j);
    j = addSortedFiles(ic, files, j);
    j = addSortedFiles(stock, files, j);
    j = addSortedFiles(melotte, files, j);
    j = addSortedFiles(collider, files, j);
    j = addSortedFiles(stars, files, j);
  }

  /**
   * Recreate the list of files as sorted by catalogue
   * @param list the sorted catalogue
   * @param files the full list of files
   * @param idx the current index for files
   * @return idx the new index for files
   */
  private int addSortedFiles(List<String> list, File[] files, int idx) {
    for(int i=0; i<list.size(); i++) {
      if(idx < files.length) {
        files[idx] = new File(list.get(i));
        idx++;
      }
    }
    return idx;
  }

}
