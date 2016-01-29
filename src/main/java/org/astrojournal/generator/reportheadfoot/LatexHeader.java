/*
 * Copyright 2015 Piero Dalle Pezze
 *
 * This file is part of AstroJournal.
 *
 * AstroJournal is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
/*
 * Changelog:
 * - Piero Dalle Pezze: class creation.
 */
package org.astrojournal.generator.reportheadfoot;

import java.io.File;

/**
 * The header of the LaTeX main file.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 28/05/2015
 */
public class LatexHeader extends LatexHeaderFooter {

    /** The header of the Latex main file. */
    private String header = "\\documentclass[10pt,twoside,a4paper]{report}\n"
	    + "\\usepackage[a4paper,margin=1in,landscape]{geometry}\n"
	    + "\\usepackage[colorlinks=true,linkcolor={black},urlcolor={black}]{hyperref}\n"
	    + "\\usepackage{longtable}\n"
	    + "\\usepackage{pdflscape}\n"
	    + "\\title{Astronomy Observation Journal}\n"
	    + "\\author{Piero Dalle Pezze}\n"
	    + "\\date{\\today}\n"
	    + "\\begin{document}\n"
	    + "\\let\\thefootnote\\relax\\footnotetext{This document was generated using Java software tool {\\it AstroJournal} "
	    + "(\\href{http://pdp10.github.io/AstroJournal/}{http://pdp10.github.io/AstroJournal/}) "
	    + "and {\\it pdflatex} (\\href{http://www.tug.org/texlive/}{http://www.tug.org/texlive/}).} \n"
	    + "\\maketitle\n" + "\\footnotesize\n\n" + "\\noindent \n"
	    + "\\newpage\n\n";

    /** Default constructor. It initialises default header and footer. */
    public LatexHeader() {
	super();
    }

    /**
     * Constructor. It reads the header and footer from files.
     * 
     * @param path
     *            The path to the file
     * @param folder
     *            The folder containing the LaTeX header file, if any
     * @param latexHeader
     *            The LaTeX header file
     */
    public LatexHeader(String path, String folder, String latexHeader) {
	super();
	String fileString = buildPath(path, folder, latexHeader);
	header = importLatex(new File(fileString));
    }

    /**
     * Returns the header of the document.
     * 
     * @return header;
     */
    public String getHeader() {
	return header;
    }

}