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
package org.astrojournal.headerfooter;

/**
 * The footer of the Latex main file.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 28/05/2015
 */
public class AJLatexFooter extends AJLatexHeaderFooter {

    /** The footer of the Latex document. */
    private String footer = "\\end{document}\n";

    /** Default constructor. It initialises default header and footer. */
    public AJLatexFooter() {
	super();
    }

    /**
     * Constructor. It reads the header and footer from files.
     * 
     * @param path
     *            The path to the file
     * @param latexFooter
     *            The Latex footer file
     */
    public AJLatexFooter(String path, String latexFooter) {
	super();
	footer = importLatex(path, latexFooter);
    }

    /**
     * Returns the footer of the document.
     * 
     * @return footer;
     */
    public String getFooter() {
	return footer;
    }

}
