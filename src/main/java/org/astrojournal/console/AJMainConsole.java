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
	String help = new String(
		AJConfig.APPLICATION_NAME
			+ " "
			+ AJConfig.APPLICATION_VERSION
			+ "\n"
			+ "USAGE: run_astrojournal.sh [options]\n"
			+ "Options are:\n"
			+ "\t-f [--config] :\t\tPrint the current configuration on a shell.\n"
			+ "\t-c [--console] :\tRun AstroJournal via command line instead of via GUI.\n"
			+ "\t-l [--latex-output] :\tIn combination with the option -c, it prints the LaTeX output.\n"
			+ "\t-h [--help] :\t\tShow this help on a shell.\n"
			+ "\t--license :\t\tShow the license for AstroJournal on a shell.\n"
			+ "\t-t [--test-latex] :\tTest the installation of pdflatex.\n");
	return help;
    }

    /**
     * Create the astro journals.
     * 
     * @return true if the observations sorted by date and by target have been
     *         exported to Latex correctly
     */
    public boolean createJournals() {
	return commandRunner.createJournal();
    }

    /**
     * This method is called from within the constructor to initialise the form.
     */
    private void initComponents() {
	commandRunner = new AJMainConsoleControls();
    }

    /**
     * A simple main to start AJMainConsole.
     * 
     * @param args
     *            The command line arguments
     */
    public static void main(String args[]) {
	AJConfig ajConfig = AJConfig.getInstance();
	AJMainConsole ajMainConsole = new AJMainConsole();
	if (args.length > 1
		&& (args[1].equals("-l") || args[1].equals("--latex-output"))) {
	    if (ajConfig.isQuiet()) {
		// If the configuration was quiet, we switch every thing off,
		// except for LATEX_OUTPUT_PROP
		System.setProperty(AJConfig.QUIET_PROP, "false");
		System.setProperty(AJConfig.SHOW_LICENSE_AT_START_PROP, "false");
		System.setProperty(AJConfig.SHOW_PDFLATEX_VERSION_AT_START_PROP, "false");
		System.setProperty(AJConfig.SHOW_CONFIGURATION_AT_START_PROP,
			"false");
	    }
	    System.setProperty(AJConfig.SHOW_LATEX_OUTPUT_PROP, "true");
	} else {
	    System.setProperty(AJConfig.SHOW_LATEX_OUTPUT_PROP, "false");
	}
	ajConfig.loadSystemProperties();
	if (!ajMainConsole.createJournals()) {
	    System.exit(1);
	}
    }
}
