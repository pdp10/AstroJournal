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
package main;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ConfigurationUtils;
import org.astrojournal.configuration.ajconfiguration.AJConfiguration;
import org.astrojournal.configuration.ajconfiguration.AJConstants;
import org.astrojournal.configuration.ajconfiguration.AJProperties;
import org.astrojournal.console.AJMainConsole;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This test executes astrojournal via command line on a simple sample set of
 * three files. The third file contains errors.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 15 Jan 2016
 */
public class SystemTest {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(SystemTest.class);

    /** The configuration. */
    private static Configuration config;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	System.setProperty(AJProperties.FILES_LOCATION,
		System.getProperty("user.dir") + File.separator + "src"
			+ File.separator + "test" + File.separator
			+ "resources" + File.separator + "system_test");

	config = new AJConfiguration();

	String[] args = new String[] { "--console" };
	AJMainConsole.main(args);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
	try {
	    ConfigurationUtils configUtils = config.getConfigurationUtils();
	    configUtils.cleanFolder(config);
	} catch (IOException e) {
	    log.error(e, e);
	}

	File reportByTarget = new File(
		config.getProperty(AJProperties.FILES_LOCATION)
			+ File.separator
			+ config.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET));
	reportByTarget.delete();
	File reportByConstellation = new File(
		config.getProperty(AJProperties.FILES_LOCATION)
			+ File.separator
			+ config.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION));
	reportByConstellation.delete();
	File reportByDateSGL = new File(
		config.getProperty(AJProperties.FILES_LOCATION)
			+ File.separator
			+ config.getProperty(AJProperties.SGL_REPORTS_FOLDER_BY_DATE));
	reportByDateSGL.delete();
	File headerFooter = new File(
		config.getProperty(AJProperties.FILES_LOCATION)
			+ File.separator + "latex_header_footer");
	for (File f : headerFooter.listFiles()) {
	    f.delete();
	}
	headerFooter.delete();

	File[] files = new File(config.getProperty(AJProperties.FILES_LOCATION))
		.listFiles();
	for (File f : files) {
	    if (f.getName().endsWith(".tex") || f.getName().endsWith(".pdf")
		    || f.getName().endsWith(".txt")) {
		log.info("\tDeleting file " + f.getAbsolutePath());
		f.delete();
	    }
	}

    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test the generation of report by date.
     */
    @Test
    public void testGeneratedReportByDate() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	// tex + pdf
	assertTrue(new File(config.getProperty(AJProperties.FILES_LOCATION)
		+ File.separator + AJConstants.REPORT_BY_DATE_FILENAME)
		.exists());
	assertTrue(new File(
		config.getProperty(AJProperties.FILES_LOCATION)
			+ File.separator
			+ FilenameUtils
				.removeExtension(AJConstants.REPORT_BY_DATE_FILENAME)
			+ ".pdf").exists());
    }

    /**
     * Test the generation of report by target.
     */
    @Test
    public void testGeneratedReportByTarget() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	// tex + pdf
	assertTrue(new File(config.getProperty(AJProperties.FILES_LOCATION)
		+ File.separator + AJConstants.REPORT_BY_TARGET_FILENAME)
		.exists());
	assertTrue(new File(
		config.getProperty(AJProperties.FILES_LOCATION)
			+ File.separator
			+ FilenameUtils
				.removeExtension(AJConstants.REPORT_BY_TARGET_FILENAME)
			+ ".pdf").exists());
    }

    /**
     * Test the generation of report by constellation.
     */
    @Test
    public void testGeneratedReportByConstellation() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	// tex + pdf
	assertTrue(new File(config.getProperty(AJProperties.FILES_LOCATION)
		+ File.separator + AJConstants.REPORT_BY_CONSTELLATION_FILENAME)
		.exists());
	assertTrue(new File(
		config.getProperty(AJProperties.FILES_LOCATION)
			+ File.separator
			+ FilenameUtils
				.removeExtension(AJConstants.REPORT_BY_CONSTELLATION_FILENAME)
			+ ".pdf").exists());
    }

    /**
     * Test the generation of SGL report by date.
     */
    @Test
    public void testGeneratedSGLReportByDate() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	// txt
	assertTrue(new File(config.getProperty(AJProperties.FILES_LOCATION)
		+ File.separator + AJConstants.SGL_REPORT_BY_DATE_FILENAME)
		.exists());
    }
}
