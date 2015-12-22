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
package org.astrojournal.console;

import org.astrojournal.configuration.AJConfig;

/**
 * A class for running AstroJournal via command line.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 22/12/2015
 */
public class AJMainConsole {

    private boolean latexOutput = AJConfig.getInstance().isLatexOutput();
    private AJMainConsoleControls commandRunner;

    /**
     * Creates new form NewJFrame
     */
    public AJMainConsole() {
	initComponents();
    }

    /**
     * Print the help for AstroJournal.
     * 
     * @return the help for AstroJournal Console.
     */
    public static String printHelp() {
	return "Apologise, this help is not yet written! Please report this "
		+ "issue at the website https://github.com/pdp10/AstroJournal/issues";
    }

    /**
     * Set whether the LaTeXOutput should be printed.
     * 
     * @param value
     */
    public void printLaTeXOutput(boolean value) {
	latexOutput = value;
    }

    /**
     * Create the astro journals.
     */
    public void createJournals() {
	commandRunner.createJournal(latexOutput);
    }

    /**
     * This method is called from within the constructor to initialise the form.
     */
    private void initComponents() {
	commandRunner = new AJMainConsoleControls();
	System.out.println(
		AJConfig.APPLICATION_NAME + " " + AJConfig.APPLICATION_VERSION);
    }

    /**
     * A simple main to start AJMainConsole.
     * 
     * @param args
     *            The command line arguments
     */
    public static void main(String args[]) {
	AJMainConsole ajMainConsole = new AJMainConsole();
	if (args.length > 1 && args[1].equals("-l"))
	    ajMainConsole.printLaTeXOutput(true);
	ajMainConsole.createJournals();
    }

}
