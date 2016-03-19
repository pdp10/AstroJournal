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
import org.astrojournal.configuration.ajconfiguration.AJMetaInfo;
import org.astrojournal.console.AJMainConsole;
import org.astrojournal.generator.Generator;
import org.astrojournal.gui.AJMainGUI;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
    private static void startAJMainGUI(final Generator generator,
	    final Configuration config) {
	// Note Nimbus does not seem to show the vertical scroll bar if there is
	// too much text..
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception ex) {
	    log.error(ex, ex);
	}

	// enable anti-aliased text:
	System.setProperty("awt.useSystemAAFontSettings", "gasp");
	System.setProperty("swing.aatext", "true");
	java.awt.EventQueue.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		new AJMainGUI(generator, config).setVisible(true);
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
	// Get some information for debugging
	log.debug("Application: " + AJMetaInfo.NAME.getInfo() + " "
		+ AJMetaInfo.VERSION.getInfo());
	log.debug("Operating System: " + SystemUtils.OS_ARCH + " "
		+ SystemUtils.OS_NAME + " " + SystemUtils.OS_VERSION);
	log.debug("Java: " + SystemUtils.JAVA_VENDOR + " "
		+ SystemUtils.JAVA_VERSION);

	// Initialise dependency injection with Spring
	ApplicationContext context = new ClassPathXmlApplicationContext(
		"META-INF/aj_spring_default_context.xml");
	BeanFactory factory = context;
	Configuration config = (Configuration) factory.getBean("configuration");
	Generator generator = (Generator) factory.getBean("generator");

	ConfigurationUtils configUtils = config.getConfigurationUtils();

	try {
	    if (args.length == 0) {
		startAJMainGUI(generator, config);
	    } else if (args[0].equals("-f") || args[0].equals("--config")) {
		log.info(configUtils.printConfiguration(config));
	    } else if (args[0].equals("-c") || args[0].equals("--console")) {
		AJMainConsole.main(args);
	    } else if (args[0].equals("-h") || args[0].equals("--help")) {
		log.info(AJMainConsole.printHelp());
	    } else if (args[0].equals("--license")) {
		log.info(AJMetaInfo.SHORT_LICENSE.getInfo());
	    } else if (args[0].equals("-t") || args[0].equals("--test-latex")) {
		log.info(configUtils.printPDFLatexVersion(config));
	    } else {
		log.fatal("Unrecognised option. Please, run AstroJournal with the option -h [--help] for suggestions.");
	    }
	} catch (Exception ex) {
	    log.error(ex, ex);
	}
    }
}
