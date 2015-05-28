/*
 * Author: Piero Dalle Pezze
 * Version: 0.4
 * Created on: 12/04/2015
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.apache.log4j.Logger;

/**
 * This class automatically generates the Latex code for the AstroJournal.
 */
public class AstroJournal {

  private static Logger log               = Logger.getLogger(AstroJournal.class);
  /** The relative path containing the tsv files. */
  private static String tsvFilesFolder    = "tsv_files";
  /** The name of the folder containing the observation files. */
  private static String observationsFolder = "observations";
  /** The name of the main Latex file. */
  private static String mainLatex         = "astrojournal.tex";

  /** It imports an observation record */
  private static void importObservation(Observation obs, String line,
					BufferedReader reader, String delimiter) throws IOException {
    log.debug(line);
    // copy the first line
    String[] values = line.split(delimiter);
    
    log.debug("Line length (A): " + values.length);
    if (values.length == 2 && values[0].equals(Observation.DATENAME)) {
      obs.setDate(values[1]);
      log.debug("values[0]" + values[0] + " values[1]=" + values[1]);
    }
    // Read the other lines for this observation
    while ((line = reader.readLine()) != null) {
      values = line.split(delimiter);
      log.debug("Line length (B): " + values.length);      

      if (values.length == 0 || values[0].equals("")) {
	  break;
      }

      if (values.length == 2) {

	  if (values[0].equals(Observation.TIMENAME)) {
	      obs.setTime(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);

	  } else if (values[0].equals(Observation.LOCATIONNAME)) {
	      obs.setLocation(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);

	  } else if (values[0].equals(Observation.ALTITUDENAME)) {
	      obs.setAltitude(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);

	  } else if (values[0].equals(Observation.TEMPERATURENAME)) {
	      obs.setTemperature(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);
	
	  } else if (values[0].equals(Observation.SEEINGNAME)) {
	      obs.setSeeing(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);

	  } else if (values[0].equals(Observation.TRANSPARENCYNAME)) {
	      obs.setTransparency(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);

	  } else if (values[0].equals(Observation.TELESCOPESNAME)) {
	      obs.setTelescopes(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);
	
	  } else if (values[0].equals(Observation.EYEPIECESNAME)) {
	      obs.setEyepieces(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);
	
	  } else if (values[0].equals(Observation.POWEREXITPUPILFOVNAME)) {
	      obs.setPowerExitPupilFOV(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);
	      
	  } else if (values[0].equals(Observation.FILTERSNAME)) {
	      obs.setFilters(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);
	  }
	  
      } else if (values.length == 5
		 && values[0].equals(ObservationItem.TARGETNAME)
		 && values[1].equals(ObservationItem.CONSTELLATIONNAME)
		 && values[2].equals(ObservationItem.TYPENAME)
		 && values[3].equals(ObservationItem.POWERNAME)
		 && values[4].equals(ObservationItem.NOTESNAME)) {
	  while ((line = reader.readLine()) != null) {
	      values = line.split(delimiter);
	      if (values.length != 5 ||	values[0].equals("")) {
		  break;
	      }
	      log.debug(line);
	      log.debug("Line length (C): " + values.length);
	      log.debug(values[4]);
	      ObservationItem item = new ObservationItem();
	      item.setTarget(values[0]);
	      item.setConstellation(values[1]);
	      item.setType(values[2]);
	      item.setPower(values[3]);
	      item.setNotes(values[4].replace("%", "\\%"));
	      obs.addObservationItem(item);
	  }
      }
    }
  }


  /** Exports an observation record to Latex */
  private static void exportObservation(Observation obs) {
    Writer table = null;
    
    String filenameOut = obs.getDate();
    filenameOut = filenameOut.substring(6, 10) + filenameOut.substring(3, 5)
      + filenameOut.substring(0, 2);
    // Add an additional char if this is present. This is the case in which
    // more than one observation per day is done.
    if(obs.getDate().length() == 11) {
	filenameOut = filenameOut + obs.getDate().charAt(10);
    }
    
    ArrayList<ObservationItem> observationItems = obs.getObservationItems();
    // debugging
    log.debug(obs.getEyepieces());
    try {
      table = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
        new File(observationsFolder, "obs" + filenameOut + ".tex")), "utf-8"));
      table.write("% General observation data\n");
      table.write("\\begin{tabular}{ p{1.7in} p{1.2in} p{1.5in} p{4.2in}}\n");
      table.write("{\\bf " + Observation.DATENAME + ":} & "
        + obs.getDate() + " & {\\bf " + Observation.TELESCOPESNAME
        + ":} & " + obs.getTelescopes() + " \\\\ \n");
      table.write("{\\bf " + Observation.TIMENAME + ":} & "
        + obs.getTime() + " & {\\bf " + Observation.EYEPIECESNAME
        + ":} & " + obs.getEyepieces() + " \\\\ \n");
      table.write("{\\bf " + Observation.LOCATIONNAME + ":} & "
        + obs.getLocation() + " & {\\bf "
        + Observation.POWEREXITPUPILFOVNAME + ":} & "
        + obs.getPowerExitPupilFOV() + " \\\\ \n");
      table.write("{\\bf " + Observation.ALTITUDENAME + ":} & "
        + obs.getAltitude() + " & {\\bf " + Observation.FILTERSNAME
        + ":} & " + obs.getFilters() + " \\\\ \n");
      table.write("{\\bf " + Observation.TEMPERATURENAME + ":} & "
        + obs.getTemperature() + " & & \\\\ \n");
      table.write("{\\bf " + Observation.SEEINGNAME + ":} & "
        + obs.getSeeing() + " & & \\\\ \n");
      table.write("{\\bf " + Observation.TRANSPARENCYNAME + ":} & "
        + obs.getTransparency() + " & & \\\\ \n");
      table.write("\\end{tabular}\n");

      table.write("% Detailed observation data\n");
      table.write("\\centering \n");
      table.write("\\begin{longtable}{ p{0.8in}  p{0.3in}  p{0.5in}  p{0.9in}  p{5.8in} }\n");
      table.write("\\hline \n");
      table.write("{\\bf " + ObservationItem.TARGETNAME + "} & {\\bf "
        + ObservationItem.CONSTELLATIONNAME + "} & {\\bf "
        + ObservationItem.TYPENAME + "} & {\\bf "
        + ObservationItem.POWERNAME + "} & {\\bf "
        + ObservationItem.NOTESNAME + "} \\\\ \n");

      table.write("\\hline \n");
      for (ObservationItem item : observationItems) {
        table.write(item.getTarget() + " & " + item.getConstellation() + " & "
          + item.getType() + " & " + item.getPower() + " & " + item.getNotes()
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


  /**
   * Generates a tex file (2 tables) per observation.
   * 
   * @return true if the procedure succeeds.
   */
  private static boolean generateObsLatex() {
    // You need to create a reader reading the file tvs
    // Then parse the tvs file and for each table found (e.g. looking for the
    // word "Date",
    // write a latex table in a file. Date is the string date
    // for each file in the folder obs (sorted by observation increasing), add a
    // line
    File[] files = new File(tsvFilesFolder).listFiles();
    Arrays.sort(files);
    String delimiter = "\t";
    if (files == null) {
      log.warn("folder " + tsvFilesFolder + " not found");
      return false;
    }
    // If this pathname does not denote a directory, then listFiles() returns
    // null.
    for (File file : files) {
      if (file.isFile() && file.getName().endsWith(".tsv")) {
        // Get the current file name.
        String tsvFilename = file.getName();
        log.info("Processing the file ... " + tsvFilename + "\n");
        // Create a buffered reader to read the file
        BufferedReader reader = null;
        try {
          reader = new BufferedReader(new FileReader(new File(tsvFilesFolder,
            tsvFilename)));
          String line;
          // Read all lines
          while ((line = reader.readLine()) != null) {
            log.debug(line);
            if (line.indexOf(Observation.DATENAME) > -1) {
              Observation obs = new Observation();
              importObservation(obs, line, reader, delimiter);
              exportObservation(obs);
              log.info("\t- Exported observation: " + obs.getDate()
                + " ... DONE\n");
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


    /** Generates the Latex code for the Astro Journal
     *
     * @param writer the writer object
     * @return true if the Latex code is generated
     * @throws IOException
     */
  private static boolean generateLatexCode(Writer writer) throws IOException {
    if (!generateObsLatex()) {
      return false;
    }
    writer.write("\\documentclass[10pt,twoside,a4paper]{article}\n");
    writer.write("\\usepackage[a4paper,margin=1in,landscape]{geometry}\n");
    writer.write("\\usepackage{color}\n");
    writer.write("\\usepackage{graphicx}\n");
    writer.write("\\usepackage{longtable}\n");
    writer.write("\\usepackage{pdflscape}\n");
    writer.write("% DO NOT EDIT THIS FILE AS IT IS AUTO-GENERATED by the program AstroJournal. \n");
    writer.write("\\title{Astronomy Observation Journal}\n");
    writer.write("\\author{Piero Dalle Pezze}\n");
    writer.write("\\date{\\today}\n");
    writer.write("\\begin{document}\n");
    writer.write("\\maketitle\n");
    writer.write("\\begin{abstract}\n");
    writer.write("This document contains a collection of observation reports. It is auto-generated by running Java program "
      + "AstroJournal (https://pdp10@bitbucket.org/pdp10/astrojournal.git) and the utility pdflatex (or texi2pdf). AstroJournal generates Latex code from tab-separated value (tsv) files " 
      + "exported using a SpreadSheet (e.g. Google Drive SpreadSheet). This allows users to: a) edit their observation reports using a spreadsheet easily, b) obtain a complete and formatted report in Latex, and c) benefit from versioning (if using a versioning system such as Git). ");
    writer.write("\\end{abstract}\n");

    writer.write("\n\n% Preamble. \n");
    writer.write("\\newpage\n");

    //writer.write("\\scriptsize\n\n");
    writer.write("\\footnotesize\n\n");

    writer.write("\\noindent \n");

    writer.write("{\\bf Atlases:}\n");
    writer.write("\\begin{enumerate}  \n");
    writer.write("\\item Deep Sky Hunter Star Atlas v2 (by Michael Vlasov)\n");
    writer.write("\\item Sky \\& Telescope's Pocket Sky Atlas (by Roger W. Sinnott)\n");
    writer.write("\\item Carte du Ciel (software)\n");
    writer.write("\\item Stellarium on tablet (software)\n");
    writer.write("\\end{enumerate}\n");

    writer.write("\\bigskip \n");
    writer.write("{\\bf Observing techniques for DSO:}\n");
    writer.write("\\begin{enumerate}\n");
    writer.write("\\item Eye adaptation at the eyepiece for 10min at least \n");
    writer.write("\\item Averted vision \n");
    writer.write("\\item Cover the other eye to relax the observing eye nerve \n");
    writer.write("\\item Know exact target position (precise star hopping) \n");
    writer.write("\\end{enumerate} \n");

    writer.write("\\bigskip \n");
    writer.write("{\\bf Antoniadi Scale:}\n");
    writer.write("\\begin{enumerate}\n");
    writer.write("\\item Perfect seeing, without a quiver.\n");
    writer.write("\\item Slight undulations, with moments of calm lasting several seconds.\n");
    writer.write("\\item Moderate seeing, with larger air tremors.\n");
    writer.write("\\item Poor seeing, with constant troublesome undulations.\n");
    writer.write("\\item Very bad seeing, scarcely allowing the makings of a rough sketch.\n");
    writer.write("\\end{enumerate}\n");

    writer.write("\\bigskip \n");    
    writer.write("{\\bf Transparency Scale (American Association of Amateur Astronomers):}\n");
    writer.write("\\begin{enumerate}\n");
    writer.write("\\item Do Not Observe - Completely cloudy or precipitating. (Why are you out?)\n");
    writer.write("\\item Very Poor - Mostly Cloudy. \n");
    writer.write("\\item Poor - Partly cloudy or heavy haze. 1 or 2 Little Dipper stars visible. \n");
    writer.write("\\item Somewhat Clear - Cirrus or moderate haze. 3 or 4 Little Dipper stars visible. \n");
    writer.write("\\item Partly Clear - Slight haze. 4 or 5 Little Dipper stars visible. \n");
    writer.write("\\item Clear - No clouds. Milky Way visible with averted vision. 6 Little Dipper stars visible. \n");
    writer.write("\\item Very Clear - Milky Way and M31 visible. Stars fainter than mag 6.0 are just seen and fainter parts of the Milky Way are more obvious \n");
    writer.write("\\item Extremely Clear - overwhelming profusion of stars, Zodiacal light and the gegenschein form continuous band across the sky, the Milky Way is very wide and bright throughout\n");
    writer.write("\\end{enumerate}\n");
    writer.write("\\newpage\n\n");
    
    // for each file in the folder obs (sorted by observation increasing), add a
    // line
    File[] files = new File(observationsFolder).listFiles();
    Arrays.sort(files, Collections.reverseOrder());
    if (files == null) {
      log.warn("Folder " + observationsFolder
        + " not found");
      return false;
    }
    // If this pathname does not denote a directory, then listFiles() returns
    // null.
    for (File file : files) {
      if (file.isFile() && file.getName().endsWith(".tex")) {
        // include the file removing the extension .tex
        writer.write("\\include{" + observationsFolder + "/"
          + file.getName().replaceFirst("[.][^.]+$", "") + "}\n");
        //writer.write("\\newpage\n");
      }
    }
    writer.write("\\end{document}\n");
    return true;
  }


    /** 
     * It generates the latex files of the AstroJournal. 
     * @param tsvDir the directory containing the tsv files (input)
     * @param obsDir the directory containing the single observations in latex format (output)
     */
    public static void generate(String tsvDir, String obsDir) {
	tsvFilesFolder = tsvDir;
	observationsFolder = obsDir;
	Writer writer = null;
	try {
	    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
   		  			    mainLatex), "utf-8"));
	    if(writer == null) {
		throw new IOException();
	    }
	    generateLatexCode(writer);
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
     * @param args a list of two arguments representing the input and output folders
     */
    public static void main(String[] args) {
	try {
	    if(args.length == 2) {
		String tsvDir = args[0];
		String obsDir = args[1];
		AstroJournal.generate(tsvDir, obsDir);
	    } else {
		throw new Exception("Please, specify the folders + " + tsvFilesFolder + "/ and " + observationsFolder + "/ as arguments.");
	    }
	} catch (Exception ex) {
	    log.warn(ex);
	}
    }
}
