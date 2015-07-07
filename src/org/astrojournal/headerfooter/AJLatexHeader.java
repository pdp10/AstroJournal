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
package org.astrojournal.headerfooter;

/**
 * The header of the Latex main file.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 28/05/2015
 */
public class AJLatexHeader extends AJLatexHeaderFooter {

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


  /** Default constructor. It initialises default header and footer. */
  public AJLatexHeader() { 
    super();
  }

  /** 
   * Constructor. It reads the header and footer from files. 
   * @param latexHeader The Latex header file 
   */
  public AJLatexHeader(String latexHeader) { 
    super();
    header = importLatex(latexHeader);
  }

  /**
   * Returns the header of the document.
   * @return header;
   */
  public String getHeader() { return header; }

}