/*
 * Author: Piero Dalle Pezze
 * Version: 0.5
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
import java.util.Arrays;
import java.util.Collections;
import org.apache.log4j.Logger;

/**
 * This class automatically generates the Latex code for the AstroJournal.
 */
public class AJMain {

    /** The log associated to this class */
    private static Logger log = Logger.getLogger(AJMain.class);

    /** The relative path containing the tsv files (input folder). */
    private String tsvReportsFolder = "tsv_reports";
    /** The name of the folder containing the observation files (output folder). */
    private String latexReportsFolder = "latex_reports";
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
     * It generates the latex files for AstroJournal. 
     * @param tsvDir the directory containing the tsv files (input)
     * @param obsDir the directory containing the single observations in latex format (output)
     */
    public void generateLatexCode(String tsvDir, String obsDir) {
	tsvReportsFolder = tsvDir;
	latexReportsFolder = obsDir;
	//AJLatexHeaderFooter ajLatexHeaderFooter = new AJLatexHeaderFooter();
	AJLatexHeaderFooter ajLatexHeaderFooter = new AJLatexHeaderFooter(latexHeader, latexFooter);
	Writer writer = null;
	try {
	    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
   		  			    mainLatex), "utf-8"));
	    if(writer == null) {
		throw new IOException();
	    }

	    if (!generateObservationsLatexCode()) {
	      log.warn("tsv file is not valid. Cannot generate Latex code for the observations.");
	      return;
	    }
	    // write the Latex Header
	    writer.write(ajLatexHeaderFooter.getHeader());
	    // write the Latex Body
	    // for each file in the folder obs (sorted by observation increasing), add a
	    // line
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
		writer.write("\\include{" + latexReportsFolder + "/"
		  + file.getName().replaceFirst("[.][^.]+$", "") + "}\n");
	      }
	    }
	    // write the Latex Footer
	    writer.write(ajLatexHeaderFooter.getFooter());

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
	AJMain ajMain = new AJMain();
	try {
	    if(args.length == 2) {
		String tsvDir = args[0];
		String obsDir = args[1];
		ajMain.generateLatexCode(tsvDir, obsDir);
	    } else {
		throw new Exception("Please, specify the folders + " + ajMain.tsvReportsFolder + "/ and " + ajMain.latexReportsFolder + "/ as arguments.");
	    }
	} catch (Exception ex) {
	    log.warn(ex);
	}
    }

}
