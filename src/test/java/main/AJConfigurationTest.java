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
import org.astrojournal.configuration.ajconfiguration.AJPropertyConstants;
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
	AJConfigurationTestUtils.removeAJPropertiesFromSystem();
    }

    /**
     * Test the passing from system properties to application properties.
     */
    @Test
    public void testSystemPropertiesPreliminary() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	Configuration config = new AJConfiguration();
	System.setProperty(AJPropertyConstants.QUIET.getKey(), "true");
	System.setProperty(AJPropertyConstants.RAW_REPORTS_FOLDER.getKey(),
		"raw_report_test1");
	System.setProperty("aj.fake_property", "this property does not exist!");

	config.loadSystemProperties();

	assertEquals(config.getProperty(AJPropertyConstants.QUIET.getKey()),
		"true");
	assertEquals(config.getProperty(AJPropertyConstants.RAW_REPORTS_FOLDER
		.getKey()), "raw_report_test1");
	assertEquals(config.getProperty("aj.fake_property"), null);

	// Let's set them again with different values as additional control
	System.setProperty(AJPropertyConstants.QUIET.getKey(), "false");
	System.setProperty(AJPropertyConstants.RAW_REPORTS_FOLDER.getKey(),
		"raw_report_test2");

	config.loadSystemProperties();

	assertEquals(config.getProperty(AJPropertyConstants.QUIET.getKey()),
		"false");
	assertEquals(config.getProperty(AJPropertyConstants.RAW_REPORTS_FOLDER
		.getKey()), "raw_report_test2");

	// let's remove this property from the system.
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

	System.setProperty(AJPropertyConstants.SHOW_LATEX_OUTPUT.getKey(),
		"true");
	System.setProperty(AJPropertyConstants.QUIET.getKey(), "true");
	System.setProperty(
		AJPropertyConstants.SHOW_CONFIGURATION_AT_START.getKey(),
		"true");
	System.setProperty(AJPropertyConstants.SHOW_LICENSE_AT_START.getKey(),
		"true");
	System.setProperty(
		AJPropertyConstants.SHOW_PDFLATEX_VERSION_AT_START.getKey(),
		"true");

	Configuration config = new AJConfiguration();

	// Load the new properties
	config.loadSystemProperties();

	assertEquals(config.getProperty(AJPropertyConstants.QUIET.getKey()),
		"true");
	assertEquals(config.getProperty(AJPropertyConstants.SHOW_LATEX_OUTPUT
		.getKey()), "true");
	assertEquals(
		config.getProperty(AJPropertyConstants.SHOW_LICENSE_AT_START
			.getKey()), "true");
	assertEquals(
		config.getProperty(AJPropertyConstants.SHOW_PDFLATEX_VERSION_AT_START
			.getKey()), "true");
	assertEquals(
		config.getProperty(AJPropertyConstants.SHOW_CONFIGURATION_AT_START
			.getKey()), "true");
    }

    /**
     * Test AstroJournal boolean Java properties set to false.
     */
    @Test
    public void testAJBooleanFalseProperties() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	System.setProperty(AJPropertyConstants.SHOW_LATEX_OUTPUT.getKey(),
		"false");
	System.setProperty(AJPropertyConstants.QUIET.getKey(), "false");
	System.setProperty(
		AJPropertyConstants.SHOW_CONFIGURATION_AT_START.getKey(),
		"false");
	System.setProperty(AJPropertyConstants.SHOW_LICENSE_AT_START.getKey(),
		"false");
	System.setProperty(
		AJPropertyConstants.SHOW_PDFLATEX_VERSION_AT_START.getKey(),
		"false");

	Configuration config = new AJConfiguration();

	// Load the new properties
	config.loadSystemProperties();

	assertEquals(config.getProperty(AJPropertyConstants.QUIET.getKey()),
		"false");
	assertEquals(config.getProperty(AJPropertyConstants.SHOW_LATEX_OUTPUT
		.getKey()), "false");
	assertEquals(
		config.getProperty(AJPropertyConstants.SHOW_LICENSE_AT_START
			.getKey()), "false");
	assertEquals(
		config.getProperty(AJPropertyConstants.SHOW_PDFLATEX_VERSION_AT_START
			.getKey()), "false");
	assertEquals(
		config.getProperty(AJPropertyConstants.SHOW_CONFIGURATION_AT_START
			.getKey()), "false");
    }

    /**
     * Test main folders.
     */
    @Test
    public void testMainFoldersProperties() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	System.setProperty(AJPropertyConstants.FILES_LOCATION.getKey(),
		System.getProperty("java.io.tmpdir"));
	System.setProperty(AJPropertyConstants.RAW_REPORTS_FOLDER.getKey(),
		"rr_test");
	System.setProperty(
		AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER.getKey(),
		"lhffolder_test");

	Configuration config = new AJConfiguration();
	// Load the new properties
	config.loadSystemProperties();

	assertEquals(
		config.getProperty(AJPropertyConstants.FILES_LOCATION.getKey()),
		System.getProperty("java.io.tmpdir"));
	assertEquals(config.getProperty(AJPropertyConstants.RAW_REPORTS_FOLDER
		.getKey()), "rr_test");
	assertEquals(
		config.getProperty(AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER
			.getKey()), "lhffolder_test");
    }

    /**
     * Test `Latex report by date` properties.
     */
    @Test
    public void testLatexReportByDateProperties() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	System.setProperty(
		AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_DATE.getKey(),
		"lrdate_test");
	System.setProperty(
		AJPropertyConstants.LATEX_REPORT_BY_DATE_FILENAME.getKey(),
		"lrdatefile_test");
	System.setProperty(
		AJPropertyConstants.LATEX_HEADER_BY_DATE_FILENAME.getKey(),
		"lrdateheader_test");
	System.setProperty(
		AJPropertyConstants.LATEX_FOOTER_BY_DATE_FILENAME.getKey(),
		"lrdatefooter_test");

	Configuration config = new AJConfiguration();
	// Load the new properties
	config.loadSystemProperties();

	assertEquals(
		config.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_DATE
			.getKey()), "lrdate_test");
	assertEquals(
		config.getProperty(AJPropertyConstants.LATEX_REPORT_BY_DATE_FILENAME
			.getKey()), "lrdatefile_test");
	assertEquals(
		config.getProperty(AJPropertyConstants.LATEX_HEADER_BY_DATE_FILENAME
			.getKey()), "lrdateheader_test");
	assertEquals(
		config.getProperty(AJPropertyConstants.LATEX_FOOTER_BY_DATE_FILENAME
			.getKey()), "lrdatefooter_test");

    }

    /**
     * Test `Latex report by target` properties.
     */
    @Test
    public void testLatexReportByTargetProperties() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	System.setProperty(
		AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_TARGET.getKey(),
		"lrtarget_test");
	System.setProperty(
		AJPropertyConstants.LATEX_REPORT_BY_TARGET_FILENAME.getKey(),
		"lrtargetfile_test");
	System.setProperty(
		AJPropertyConstants.LATEX_HEADER_BY_TARGET_FILENAME.getKey(),
		"lrtargetheader_test");
	System.setProperty(
		AJPropertyConstants.LATEX_FOOTER_BY_TARGET_FILENAME.getKey(),
		"lrtargetfooter_test");

	Configuration config = new AJConfiguration();
	// Load the new properties
	config.loadSystemProperties();

	assertEquals(
		config.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_TARGET
			.getKey()), "lrtarget_test");
	assertEquals(
		config.getProperty(AJPropertyConstants.LATEX_REPORT_BY_TARGET_FILENAME
			.getKey()), "lrtargetfile_test");
	assertEquals(
		config.getProperty(AJPropertyConstants.LATEX_HEADER_BY_TARGET_FILENAME
			.getKey()), "lrtargetheader_test");
	assertEquals(
		config.getProperty(AJPropertyConstants.LATEX_FOOTER_BY_TARGET_FILENAME
			.getKey()), "lrtargetfooter_test");

    }

    /**
     * Test `Latex report by constellation` properties.
     */
    @Test
    public void testLatexReportByConstellationProperties() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	System.setProperty(
		AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
			.getKey(), "lrconst_test");
	System.setProperty(
		AJPropertyConstants.LATEX_REPORT_BY_CONSTELLATION_FILENAME
			.getKey(), "lrconstfile_test");
	System.setProperty(
		AJPropertyConstants.LATEX_HEADER_BY_CONSTELLATION_FILENAME
			.getKey(), "lrconstheader_test");
	System.setProperty(
		AJPropertyConstants.LATEX_FOOTER_BY_CONSTELLATION_FILENAME
			.getKey(), "lrconstfooter_test");

	Configuration config = new AJConfiguration();
	// Load the new properties
	config.loadSystemProperties();

	assertEquals(
		config.getProperty(AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
			.getKey()), "lrconst_test");
	assertEquals(
		config.getProperty(AJPropertyConstants.LATEX_REPORT_BY_CONSTELLATION_FILENAME
			.getKey()), "lrconstfile_test");
	assertEquals(
		config.getProperty(AJPropertyConstants.LATEX_HEADER_BY_CONSTELLATION_FILENAME
			.getKey()), "lrconstheader_test");
	assertEquals(
		config.getProperty(AJPropertyConstants.LATEX_FOOTER_BY_CONSTELLATION_FILENAME
			.getKey()), "lrconstfooter_test");

    }

    /**
     * Test `Latex report by constellation` properties.
     */
    @Test
    public void testTXTReportByDateProperties() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	System.setProperty(
		AJPropertyConstants.TXT_REPORTS_FOLDER_BY_DATE.getKey(),
		"trdate_test");
	System.setProperty(
		AJPropertyConstants.TXT_REPORT_BY_DATE_FILENAME.getKey(),
		"trdatefile_test");

	Configuration config = new AJConfiguration();
	// Load the new properties
	config.loadSystemProperties();

	assertEquals(
		config.getProperty(AJPropertyConstants.TXT_REPORTS_FOLDER_BY_DATE
			.getKey()), "trdate_test");
	assertEquals(
		config.getProperty(AJPropertyConstants.TXT_REPORT_BY_DATE_FILENAME
			.getKey()), "trdatefile_test");

    }

}
