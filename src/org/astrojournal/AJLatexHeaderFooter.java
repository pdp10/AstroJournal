/*
 * Author: Piero Dalle Pezze
 * Version: 0.2
 * Created on: 28/05/2015
 */
package org.astrojournal;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * The Header of the Latex main file.
 */
public class AJLatexHeaderFooter {

    /** The log associated to this class */
    private static Logger log = Logger.getLogger(AJLatexHeaderFooter.class);    


    /** The header of the Latex main file. */
    private String header =
	"\\documentclass[10pt,twoside,a4paper]{report}\n" +
	"\\usepackage[a4paper,margin=1in,landscape]{geometry}\n" +
	"\\usepackage[colorlinks=true,linkcolor={black},urlcolor={black}]{hyperref}\n" +
	"\\usepackage{longtable}\n" +
	"\\usepackage{pdflscape}\n" +
	"\\title{Astronomy Observation Journal}\n" +
	"\\author{Piero Dalle Pezze}\n" +
	"\\date{\\today}\n" +
	"\\begin{document}\n" +
	"\\let\\thefootnote\\relax\\footnotetext{This document was generated using Java software tool {\\it AstroJournal} " + 
	"(\\href{https://pdp10@bitbucket.org/pdp10/astrojournal.git}{https://pdp10@bitbucket.org/pdp10/astrojournal.git}) " +
        "and {\\it pdflatex} (\\href{http://www.tug.org/texlive/}{http://www.tug.org/texlive/}).} \n" +
	"\\maketitle\n" +
	"\\footnotesize\n\n" +
	"\\noindent \n" +
	"\\newpage\n\n";


    /** The footer of the Latex document. */
    private String footer = "\\end{document}\n";
    

    /** Imports the latex file 
     *@param file The Latex file
     *@return the imported Latex file as a string
     */
    private String importLatex(String file) {
      File f = new File(file);
      StringBuilder sb = new StringBuilder();
      if (f.isFile() && f.getName().endsWith(".tex")) {
	  log.debug("Importing latex file " + file);
	  // Create a buffered reader to read the file
	  BufferedReader reader = null;
	  try {
	    reader = new BufferedReader(new FileReader(f));
	    String line;
	    // Read all lines
	    while ((line = reader.readLine()) != null) {
	      log.debug(line);
	      sb.append(line).append(" \n");
	    } // end while
	  } catch (IOException ex) {
	    System.out.println(ex);
	  }
	  finally {
	    try {
	      if (reader != null)
		reader.close();
	    } catch (IOException ex) { 
	      log.warn("File " + file + " was not closed successfully");
	    }
          }
       }
       // No need to replace \ with \\ as this is not interpreted by Java at this stage
       return sb.toString();
    }


    /** Default constructor. It initialises default header and footer. */
    public AJLatexHeaderFooter() { }

    /** Default constructor. It reads the header and footer from files. 
     *@param latexHeader The Latex header file 
     *@param latexFooter The Latex footer file
     */
    public AJLatexHeaderFooter(String latexHeader, String latexFooter) { 
	header = importLatex(latexHeader);
	footer = importLatex(latexFooter);
    }

    /**
     * Returns the header of the document.
     * @return header;
     */
    public String getHeader() { return header; }

    /**
     * Returns the footer of the document.
     * @return footer;
     */
    public String getFooter() { return footer; }
   
}
