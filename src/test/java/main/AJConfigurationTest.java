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

import static org.junit.Assert.assertEquals;

import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ajconfiguration.AJConfiguration;
import org.astrojournal.configuration.ajconfiguration.AJProperties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test AstroJournal Java Properties.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 15 Jan 2016
 */
public class AJConfigurationTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
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
	AJConfigurationTestUtils.resetDefaultProperties();
    }

    /**
     * Test the passing from system properties to application properties.
     */
    @Test
    public void testSystemPropertiesPassing() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	Configuration config = new AJConfiguration();
	System.setProperty(AJProperties.QUIET.toString(), "true");
	System.setProperty(AJProperties.RAW_REPORTS_FOLDER.toString(),
		"raw_report_test1");
	System.setProperty("aj.fake_property", "this property does not exist!");

	config.loadSystemProperties();

	assertEquals(config.getProperty(AJProperties.QUIET.toString()), "true");
	assertEquals(
		config.getProperty(AJProperties.RAW_REPORTS_FOLDER.toString()),
		"raw_report_test1");
	assertEquals(config.getProperty("aj.fake_property"), null);

	// Let's set them again with different values as additional control
	System.setProperty(AJProperties.QUIET.toString(), "false");
	System.setProperty(AJProperties.RAW_REPORTS_FOLDER.toString(),
		"raw_report_test2");

	config.loadSystemProperties();

	assertEquals(config.getProperty(AJProperties.QUIET.toString()), "false");
	assertEquals(
		config.getProperty(AJProperties.RAW_REPORTS_FOLDER.toString()),
		"raw_report_test2");

	// let's remove these properties from the system.
	System.clearProperty(AJProperties.QUIET.toString());
	System.clearProperty(AJProperties.RAW_REPORTS_FOLDER.toString());
	System.clearProperty("aj.fake_property");
    }

    /**
     * Test AstroJournal boolean Java properties set to true.
     */
    @Test
    public void testAJBooleanTrueProperties() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	System.setProperty(AJProperties.SHOW_LATEX_OUTPUT.toString(), "true");
	System.setProperty(AJProperties.QUIET.toString(), "true");
	System.setProperty(AJProperties.SHOW_CONFIGURATION_AT_START.toString(),
		"true");
	System.setProperty(AJProperties.SHOW_LICENSE_AT_START.toString(),
		"true");
	System.setProperty(
		AJProperties.SHOW_PDFLATEX_VERSION_AT_START.toString(), "true");

	Configuration ajConfig = new AJConfiguration();

	// Load the new properties
	ajConfig.loadSystemProperties();

	assertEquals(ajConfig.getProperty(AJProperties.QUIET.toString()),
		"true");
	assertEquals(
		ajConfig.getProperty(AJProperties.SHOW_LATEX_OUTPUT.toString()),
		"true");
	assertEquals(ajConfig.getProperty(AJProperties.SHOW_LICENSE_AT_START
		.toString()), "true");
	assertEquals(
		ajConfig.getProperty(AJProperties.SHOW_PDFLATEX_VERSION_AT_START
			.toString()), "true");
	assertEquals(
		ajConfig.getProperty(AJProperties.SHOW_CONFIGURATION_AT_START
			.toString()), "true");
    }

    /**
     * Test AstroJournal boolean Java properties set to false.
     */
    @Test
    public void testAJBooleanFalseProperties() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	System.setProperty(AJProperties.SHOW_LATEX_OUTPUT.toString(), "false");
	System.setProperty(AJProperties.QUIET.toString(), "false");
	System.setProperty(AJProperties.SHOW_CONFIGURATION_AT_START.toString(),
		"false");
	System.setProperty(AJProperties.SHOW_LICENSE_AT_START.toString(),
		"false");
	System.setProperty(
		AJProperties.SHOW_PDFLATEX_VERSION_AT_START.toString(), "false");

	Configuration ajConfig = new AJConfiguration();

	// Load the new properties
	ajConfig.loadSystemProperties();

	assertEquals(ajConfig.getProperty(AJProperties.QUIET.toString()),
		"false");
	assertEquals(
		ajConfig.getProperty(AJProperties.SHOW_LATEX_OUTPUT.toString()),
		"false");
	assertEquals(ajConfig.getProperty(AJProperties.SHOW_LICENSE_AT_START
		.toString()), "false");
	assertEquals(
		ajConfig.getProperty(AJProperties.SHOW_PDFLATEX_VERSION_AT_START
			.toString()), "false");
	assertEquals(
		ajConfig.getProperty(AJProperties.SHOW_CONFIGURATION_AT_START
			.toString()), "false");
    }

    /**
     * Test AstroJournal String Java properties.
     */
    @Test
    public void testAJStringProperties() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	System.setProperty(AJProperties.FILES_LOCATION.toString(),
		System.getProperty("java.io.tmpdir"));
	System.setProperty(AJProperties.RAW_REPORTS_FOLDER.toString(),
		"rr_test");
	System.setProperty(
		AJProperties.LATEX_REPORTS_FOLDER_BY_DATE.toString(),
		"lrdate_test");
	System.setProperty(
		AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET.toString(),
		"lrtarget_test");
	System.setProperty(
		AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION.toString(),
		"lrconst_test");
	System.setProperty(AJProperties.SGL_REPORTS_FOLDER_BY_DATE.toString(),
		"trdate_test");

	Configuration ajConfig = new AJConfiguration();

	// Load the new properties
	ajConfig.loadSystemProperties();

	assertEquals(
		ajConfig.getProperty(AJProperties.FILES_LOCATION.toString()),
		System.getProperty("java.io.tmpdir"));
	assertEquals(ajConfig.getProperty(AJProperties.RAW_REPORTS_FOLDER
		.toString()), "rr_test");
	assertEquals(
		ajConfig.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE
			.toString()), "lrdate_test");
	assertEquals(
		ajConfig.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET
			.toString()), "lrtarget_test");
	assertEquals(
		ajConfig.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
			.toString()), "lrconst_test");
	assertEquals(
		ajConfig.getProperty(AJProperties.SGL_REPORTS_FOLDER_BY_DATE
			.toString()), "trdate_test");
    }
}
