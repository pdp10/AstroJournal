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
package org.astrojournal;

import javax.swing.UIManager;

import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ConfigurationUtils;
import org.astrojournal.configuration.ajconfiguration.AJConfiguration;
import org.astrojournal.configuration.ajconfiguration.AJConfigurationUtils;
import org.astrojournal.configuration.ajconfiguration.AJConstants;
import org.astrojournal.console.AJMainConsole;
import org.astrojournal.gui.AJMainGUI;

/**
 * This class automatically generates the Latex code for the AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version 0.9
 * @since 12/04/2015
 */
public class AJMain {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(AJMain.class);

    /**
     * Start AJMiniGUI.
     * 
     * @param config
     *            The configuration
     */
    private static void startAJMainGUI(final Configuration config) {
	// Note Nimbus does not seem to show the vertical scroll bar if there is
	// too much text..
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception ex) {
	    log.warn(ex, ex);
	}

	// enable anti-aliased text:
	System.setProperty("awt.useSystemAAFontSettings", "gasp");
	System.setProperty("swing.aatext", "true");
	java.awt.EventQueue.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		new AJMainGUI(config).setVisible(true);
	    }
	});
    }

    /**
     * Main function. By default AstroJournal Main GUI is started. Arguments can
     * be passed. Please see AJMainConsole.printHelp() for options.
     * 
     * @param args
     */
    public static void main(String[] args) {
	Configuration config = new AJConfiguration();
	ConfigurationUtils configUtils = config.getConfigurationUtils();

	// Get some information for debugging
	log.debug("Application: " + AJConstants.APPLICATION_NAME + " "
		+ AJConstants.APPLICATION_VERSION);
	log.debug("Operating System: " + SystemUtils.OS_ARCH + " "
		+ SystemUtils.OS_NAME + " " + SystemUtils.OS_VERSION);
	log.debug("Java: " + SystemUtils.JAVA_VENDOR + " "
		+ SystemUtils.JAVA_VERSION);

	try {
	    if (args.length == 0) {
		startAJMainGUI(config);
	    } else if (args[0].equals("-f") || args[0].equals("--config")) {
		log.info(configUtils.printConfiguration(config));
		System.exit(0);
	    } else if (args[0].equals("-c") || args[0].equals("--console")) {
		AJMainConsole.main(args);
		System.exit(0);
	    } else if (args[0].equals("-h") || args[0].equals("--help")) {
		log.info(AJMainConsole.printHelp());
		System.exit(0);
	    } else if (args[0].equals("--license")) {
		log.info(AJConstants.SHORT_LICENSE);
		System.exit(0);
	    } else if (args[0].equals("-t") || args[0].equals("--test-latex")) {
		if (configUtils instanceof AJConfigurationUtils) {
		    log.info(((AJConfigurationUtils) configUtils)
			    .printPDFLatexVersion(config));
		} else {
		    log.error("Cannot test LaTeX with this configuration.");
		}
		System.exit(0);
	    } else {
		log.error("Unrecognised option. Please, run AstroJournal with the option -h [--help] for suggestions.");
		System.exit(0);
	    }
	} catch (Exception ex) {
	    log.error(ex, ex);
	}
    }
}
