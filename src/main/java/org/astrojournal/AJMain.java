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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.AJConfig;
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
     */
    private static void startAJMainGUI() {
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
		new AJMainGUI().setVisible(true);
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

	try {
	    if (args.length == 0) {
		startAJMainGUI();
	    } else if (args[0].equals("-f") || args[0].equals("--config")) {
		log.info(AJConfig.getInstance().printConfiguration());
		System.exit(0);
	    } else if (args[0].equals("-c") || args[0].equals("--console")) {
		AJMainConsole.main(args);
		System.exit(0);
	    } else if (args[0].equals("-h") || args[0].equals("--help")) {
		log.info(AJMainConsole.printHelp());
		System.exit(0);
	    } else if (args[0].equals("--license")) {
		log.info(AJConfig.getInstance().printLicense());
		System.exit(0);
	    } else if (args[0].equals("-t") || args[0].equals("--test-latex")) {
		log.info(AJConfig.getInstance().printPDFLatexVersion());
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
