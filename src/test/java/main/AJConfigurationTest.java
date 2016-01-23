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
import org.astrojournal.configuration.ajconfiguration.AJPropertyNames;
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
    public void testSystemPropertiesPreliminary() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	Configuration config = new AJConfiguration();
	System.setProperty(AJPropertyNames.QUIET.toString(), "true");
	System.setProperty(AJPropertyNames.RAW_REPORTS_FOLDER.toString(),
		"raw_report_test1");
	System.setProperty("aj.fake_property", "this property does not exist!");

	config.loadSystemProperties();

	assertEquals(config.getProperty(AJPropertyNames.QUIET.toString()),
		"true");
	assertEquals(config.getProperty(AJPropertyNames.RAW_REPORTS_FOLDER
		.toString()), "raw_report_test1");
	assertEquals(config.getProperty("aj.fake_property"), null);

	// Let's set them again with different values as additional control
	System.setProperty(AJPropertyNames.QUIET.toString(), "false");
	System.setProperty(AJPropertyNames.RAW_REPORTS_FOLDER.toString(),
		"raw_report_test2");

	config.loadSystemProperties();

	assertEquals(config.getProperty(AJPropertyNames.QUIET.toString()),
		"false");
	assertEquals(config.getProperty(AJPropertyNames.RAW_REPORTS_FOLDER
		.toString()), "raw_report_test2");

	// let's remove these properties from the system.
	System.clearProperty(AJPropertyNames.QUIET.toString());
	System.clearProperty(AJPropertyNames.RAW_REPORTS_FOLDER.toString());
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

	System.setProperty(AJPropertyNames.SHOW_LATEX_OUTPUT.toString(), "true");
	System.setProperty(AJPropertyNames.QUIET.toString(), "true");
	System.setProperty(
		AJPropertyNames.SHOW_CONFIGURATION_AT_START.toString(), "true");
	System.setProperty(AJPropertyNames.SHOW_LICENSE_AT_START.toString(),
		"true");
	System.setProperty(
		AJPropertyNames.SHOW_PDFLATEX_VERSION_AT_START.toString(),
		"true");

	Configuration config = new AJConfiguration();

	// Load the new properties
	config.loadSystemProperties();

	assertEquals(config.getProperty(AJPropertyNames.QUIET.toString()),
		"true");
	assertEquals(config.getProperty(AJPropertyNames.SHOW_LATEX_OUTPUT
		.toString()), "true");
	assertEquals(config.getProperty(AJPropertyNames.SHOW_LICENSE_AT_START
		.toString()), "true");
	assertEquals(
		config.getProperty(AJPropertyNames.SHOW_PDFLATEX_VERSION_AT_START
			.toString()), "true");
	assertEquals(
		config.getProperty(AJPropertyNames.SHOW_CONFIGURATION_AT_START
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

	System.setProperty(AJPropertyNames.SHOW_LATEX_OUTPUT.toString(),
		"false");
	System.setProperty(AJPropertyNames.QUIET.toString(), "false");
	System.setProperty(
		AJPropertyNames.SHOW_CONFIGURATION_AT_START.toString(), "false");
	System.setProperty(AJPropertyNames.SHOW_LICENSE_AT_START.toString(),
		"false");
	System.setProperty(
		AJPropertyNames.SHOW_PDFLATEX_VERSION_AT_START.toString(),
		"false");

	Configuration config = new AJConfiguration();

	// Load the new properties
	config.loadSystemProperties();

	assertEquals(config.getProperty(AJPropertyNames.QUIET.toString()),
		"false");
	assertEquals(config.getProperty(AJPropertyNames.SHOW_LATEX_OUTPUT
		.toString()), "false");
	assertEquals(config.getProperty(AJPropertyNames.SHOW_LICENSE_AT_START
		.toString()), "false");
	assertEquals(
		config.getProperty(AJPropertyNames.SHOW_PDFLATEX_VERSION_AT_START
			.toString()), "false");
	assertEquals(
		config.getProperty(AJPropertyNames.SHOW_CONFIGURATION_AT_START
			.toString()), "false");
    }

    /**
     * Test main folders.
     */
    @Test
    public void testMainFoldersProperties() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	System.setProperty(AJPropertyNames.FILES_LOCATION.toString(),
		System.getProperty("java.io.tmpdir"));
	System.setProperty(AJPropertyNames.RAW_REPORTS_FOLDER.toString(),
		"rr_test");
	System.setProperty(
		AJPropertyNames.LATEX_HEADER_FOOTER_FOLDER.toString(),
		"lhffolder_test");

	Configuration config = new AJConfiguration();
	// Load the new properties
	config.loadSystemProperties();

	assertEquals(
		config.getProperty(AJPropertyNames.FILES_LOCATION.toString()),
		System.getProperty("java.io.tmpdir"));
	assertEquals(config.getProperty(AJPropertyNames.RAW_REPORTS_FOLDER
		.toString()), "rr_test");
	assertEquals(
		config.getProperty(AJPropertyNames.LATEX_HEADER_FOOTER_FOLDER
			.toString()), "lhffolder_test");

	// let's remove these properties from the system.
	System.clearProperty(AJPropertyNames.FILES_LOCATION.toString());
	System.clearProperty(AJPropertyNames.RAW_REPORTS_FOLDER.toString());
	System.clearProperty(AJPropertyNames.LATEX_HEADER_FOOTER_FOLDER
		.toString());
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
		AJPropertyNames.LATEX_REPORTS_FOLDER_BY_DATE.toString(),
		"lrdate_test");
	System.setProperty(
		AJPropertyNames.LATEX_REPORT_BY_DATE_FILENAME.toString(),
		"lrdatefile_test");
	System.setProperty(
		AJPropertyNames.LATEX_HEADER_BY_DATE_FILENAME.toString(),
		"lrdateheader_test");
	System.setProperty(
		AJPropertyNames.LATEX_FOOTER_BY_DATE_FILENAME.toString(),
		"lrdatefooter_test");

	Configuration config = new AJConfiguration();
	// Load the new properties
	config.loadSystemProperties();

	assertEquals(
		config.getProperty(AJPropertyNames.LATEX_REPORTS_FOLDER_BY_DATE
			.toString()), "lrdate_test");
	assertEquals(
		config.getProperty(AJPropertyNames.LATEX_REPORT_BY_DATE_FILENAME
			.toString()), "lrdatefile_test");
	assertEquals(
		config.getProperty(AJPropertyNames.LATEX_HEADER_BY_DATE_FILENAME
			.toString()), "lrdateheader_test");
	assertEquals(
		config.getProperty(AJPropertyNames.LATEX_FOOTER_BY_DATE_FILENAME
			.toString()), "lrdatefooter_test");

	// let's remove these properties from the system.
	System.clearProperty(AJPropertyNames.LATEX_REPORTS_FOLDER_BY_DATE
		.toString());
	System.clearProperty(AJPropertyNames.LATEX_REPORT_BY_DATE_FILENAME
		.toString());
	System.clearProperty(AJPropertyNames.LATEX_HEADER_BY_DATE_FILENAME
		.toString());
	System.clearProperty(AJPropertyNames.LATEX_FOOTER_BY_DATE_FILENAME
		.toString());
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
		AJPropertyNames.LATEX_REPORTS_FOLDER_BY_TARGET.toString(),
		"lrtarget_test");
	System.setProperty(
		AJPropertyNames.LATEX_REPORT_BY_TARGET_FILENAME.toString(),
		"lrtargetfile_test");
	System.setProperty(
		AJPropertyNames.LATEX_HEADER_BY_TARGET_FILENAME.toString(),
		"lrtargetheader_test");
	System.setProperty(
		AJPropertyNames.LATEX_FOOTER_BY_TARGET_FILENAME.toString(),
		"lrtargetfooter_test");

	Configuration config = new AJConfiguration();
	// Load the new properties
	config.loadSystemProperties();

	assertEquals(
		config.getProperty(AJPropertyNames.LATEX_REPORTS_FOLDER_BY_TARGET
			.toString()), "lrtarget_test");
	assertEquals(
		config.getProperty(AJPropertyNames.LATEX_REPORT_BY_TARGET_FILENAME
			.toString()), "lrtargetfile_test");
	assertEquals(
		config.getProperty(AJPropertyNames.LATEX_HEADER_BY_TARGET_FILENAME
			.toString()), "lrtargetheader_test");
	assertEquals(
		config.getProperty(AJPropertyNames.LATEX_FOOTER_BY_TARGET_FILENAME
			.toString()), "lrtargetfooter_test");

	// let's remove these properties from the system.
	System.clearProperty(AJPropertyNames.LATEX_REPORTS_FOLDER_BY_TARGET
		.toString());
	System.clearProperty(AJPropertyNames.LATEX_REPORT_BY_TARGET_FILENAME
		.toString());
	System.clearProperty(AJPropertyNames.LATEX_HEADER_BY_TARGET_FILENAME
		.toString());
	System.clearProperty(AJPropertyNames.LATEX_FOOTER_BY_TARGET_FILENAME
		.toString());
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
		AJPropertyNames.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
			.toString(), "lrconst_test");
	System.setProperty(
		AJPropertyNames.LATEX_REPORT_BY_CONSTELLATION_FILENAME
			.toString(), "lrconstfile_test");
	System.setProperty(
		AJPropertyNames.LATEX_HEADER_BY_CONSTELLATION_FILENAME
			.toString(), "lrconstheader_test");
	System.setProperty(
		AJPropertyNames.LATEX_FOOTER_BY_CONSTELLATION_FILENAME
			.toString(), "lrconstfooter_test");

	Configuration config = new AJConfiguration();
	// Load the new properties
	config.loadSystemProperties();

	assertEquals(
		config.getProperty(AJPropertyNames.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
			.toString()), "lrconst_test");
	assertEquals(
		config.getProperty(AJPropertyNames.LATEX_REPORT_BY_CONSTELLATION_FILENAME
			.toString()), "lrconstfile_test");
	assertEquals(
		config.getProperty(AJPropertyNames.LATEX_HEADER_BY_CONSTELLATION_FILENAME
			.toString()), "lrconstheader_test");
	assertEquals(
		config.getProperty(AJPropertyNames.LATEX_FOOTER_BY_CONSTELLATION_FILENAME
			.toString()), "lrconstfooter_test");

	// let's remove these properties from the system.
	System.clearProperty(AJPropertyNames.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
		.toString());
	System.clearProperty(AJPropertyNames.LATEX_REPORT_BY_CONSTELLATION_FILENAME
		.toString());
	System.clearProperty(AJPropertyNames.LATEX_HEADER_BY_CONSTELLATION_FILENAME
		.toString());
	System.clearProperty(AJPropertyNames.LATEX_FOOTER_BY_CONSTELLATION_FILENAME
		.toString());
    }

    /**
     * Test `Latex report by constellation` properties.
     */
    @Test
    public void testSGLReportByDateProperties() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	System.setProperty(
		AJPropertyNames.SGL_REPORTS_FOLDER_BY_DATE.toString(),
		"trdate_test");
	System.setProperty(
		AJPropertyNames.SGL_REPORT_BY_DATE_FILENAME.toString(),
		"trdatefile_test");

	Configuration config = new AJConfiguration();
	// Load the new properties
	config.loadSystemProperties();

	assertEquals(
		config.getProperty(AJPropertyNames.SGL_REPORTS_FOLDER_BY_DATE
			.toString()), "trdate_test");
	assertEquals(
		config.getProperty(AJPropertyNames.SGL_REPORT_BY_DATE_FILENAME
			.toString()), "trdatefile_test");

	// let's remove these properties from the system.
	System.clearProperty(AJPropertyNames.SGL_REPORTS_FOLDER_BY_DATE
		.toString());
	System.clearProperty(AJPropertyNames.SGL_REPORT_BY_DATE_FILENAME
		.toString());

    }

}
