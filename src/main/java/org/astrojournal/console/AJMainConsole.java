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

import org.astrojournal.AJMainControls;
import org.astrojournal.AJMetaInfo;
import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ajconfiguration.AJPropertyConstants;
import org.astrojournal.generator.Generator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A class for running AstroJournal via command line.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 22/12/2015
 */
public class AJMainConsole {

    private AJMainControls commandRunner;

    /**
     * Constructor.
     * 
     * @param generator
     *            The generator
     * 
     * @param config
     *            The configuration
     */
    public AJMainConsole(Generator generator, Configuration config) {
	generator.setConfiguration(config);
	initComponents(generator);
    }

    /**
     * Print the help for AstroJournal.
     * 
     * @return the help for AstroJournal Console.
     */
    public static String printHelp() {
	String help = new String(
		AJMetaInfo.NAME.getInfo()
			+ " "
			+ AJMetaInfo.VERSION.getInfo()
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
     * Initialise other fields.
     * 
     * @param generator
     *            The generator
     */
    private void initComponents(Generator generator) {
	commandRunner = new AJMainConsoleControls(generator);
    }

    /**
     * A simple main to start AJMainConsole.
     * 
     * @param args
     *            The command line arguments
     */
    public static void main(String args[]) {
	// Initialise dependency injection with Spring
	ApplicationContext context = new ClassPathXmlApplicationContext(
		"META-INF/aj_spring_default_context.xml");
	BeanFactory factory = context;
	Configuration config = (Configuration) factory.getBean("configuration");
	Generator generator = (Generator) factory.getBean("generator");

	AJMainConsole ajMainConsole = new AJMainConsole(generator, config);
	if (args.length > 1
		&& (args[1].equals("-l") || args[1].equals("--latex-output"))) {
	    if (config.getProperty(AJPropertyConstants.QUIET.getKey()).equals(
		    "true")) {
		// If the configuration was quiet, we switch every thing off,
		// except for LATEX_OUTPUT_PROP
		System.setProperty(AJPropertyConstants.QUIET.getKey(), "false");
		System.setProperty(
			AJPropertyConstants.SHOW_LICENSE_AT_START.getKey(),
			"false");
		System.setProperty(
			AJPropertyConstants.SHOW_PDFLATEX_VERSION_AT_START
				.getKey(), "false");
		System.setProperty(
			AJPropertyConstants.SHOW_CONFIGURATION_AT_START
				.getKey(), "false");
	    }
	    System.setProperty(AJPropertyConstants.SHOW_LATEX_OUTPUT.getKey(),
		    "true");
	} else {
	    System.setProperty(AJPropertyConstants.SHOW_LATEX_OUTPUT.getKey(),
		    "false");
	}
	config.loadSystemProperties();
	if (!ajMainConsole.createJournals()) {
	    System.exit(1);
	}
    }
}
