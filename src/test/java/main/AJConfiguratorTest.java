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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.astrojournal.configuration.AJConfig;
import org.astrojournal.configuration.AJProperties;
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
public class AJConfiguratorTest {

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
	AJConfiguratorTestUtils.resetDefaultProperties();
    }

    /**
     * Test the passing from system properties to application properties.
     */
    @Test
    public void testSystemPropertiesPassing() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	AJConfig ajConfig = AJConfig.getInstance();
	System.setProperty(AJProperties.QUIET, "true");
	System.setProperty(AJProperties.RAW_REPORTS_FOLDER, "raw_report_test1");
	System.setProperty("aj.fake_property", "this property does not exist!");

	ajConfig.loadSystemProperties();

	assertEquals(ajConfig.getProperty(AJProperties.QUIET), "true");
	assertEquals(ajConfig.getProperty(AJProperties.RAW_REPORTS_FOLDER),
		"raw_report_test1");
	assertEquals(ajConfig.getProperty("aj.fake_property"), null);

	// Let's set them again with different values as additional control
	System.setProperty(AJProperties.QUIET, "false");
	System.setProperty(AJProperties.RAW_REPORTS_FOLDER, "raw_report_test2");

	ajConfig.loadSystemProperties();

	assertEquals(ajConfig.getProperty(AJProperties.QUIET), "false");
	assertEquals(ajConfig.getProperty(AJProperties.RAW_REPORTS_FOLDER),
		"raw_report_test2");

	// let's remove these properties from the system.
	System.clearProperty(AJProperties.QUIET);
	System.clearProperty(AJProperties.RAW_REPORTS_FOLDER);
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

	System.setProperty(AJProperties.SHOW_LATEX_OUTPUT, "true");
	System.setProperty(AJProperties.QUIET, "true");
	System.setProperty(AJProperties.SHOW_CONFIGURATION_AT_START, "true");
	System.setProperty(AJProperties.SHOW_LICENSE_AT_START, "true");
	System.setProperty(AJProperties.SHOW_PDFLATEX_VERSION_AT_START, "true");

	AJConfig ajConfig = AJConfig.getInstance();

	// Load the new properties
	ajConfig.loadSystemProperties();

	assertTrue(ajConfig.isShowLatexOutput());
	assertTrue(ajConfig.isQuiet());
	assertTrue(ajConfig.isShowConfigurationAtStart());
	assertTrue(ajConfig.isShowLicenseAtStart());
	assertTrue(ajConfig.isShowPDFLatexVersionAtStart());
    }

    /**
     * Test AstroJournal boolean Java properties set to false.
     */
    @Test
    public void testAJBooleanFalseProperties() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	System.setProperty(AJProperties.SHOW_LATEX_OUTPUT, "false");
	System.setProperty(AJProperties.QUIET, "false");
	System.setProperty(AJProperties.SHOW_CONFIGURATION_AT_START, "false");
	System.setProperty(AJProperties.SHOW_LICENSE_AT_START, "false");
	System.setProperty(AJProperties.SHOW_PDFLATEX_VERSION_AT_START, "false");

	AJConfig ajConfig = AJConfig.getInstance();

	// Load the new properties
	ajConfig.loadSystemProperties();

	assertFalse(ajConfig.isShowLatexOutput());
	assertFalse(ajConfig.isQuiet());
	assertFalse(ajConfig.isShowConfigurationAtStart());
	assertFalse(ajConfig.isShowLicenseAtStart());
	assertFalse(ajConfig.isShowPDFLatexVersionAtStart());
    }

    /**
     * Test AstroJournal String Java properties.
     */
    @Test
    public void testAJStringProperties() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	System.setProperty(AJProperties.FILES_LOCATION,
		System.getProperty("java.io.tmpdir"));
	System.setProperty(AJProperties.RAW_REPORTS_FOLDER, "rr_test");
	System.setProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE,
		"lrdate_test");
	System.setProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET,
		"lrtarget_test");
	System.setProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION,
		"lrconst_test");
	System.setProperty(AJProperties.SGL_REPORTS_FOLDER_BY_DATE,
		"trdate_test");

	AJConfig ajConfig = AJConfig.getInstance();

	// Load the new properties
	ajConfig.loadSystemProperties();

	assertEquals(ajConfig.getFilesLocation().getAbsolutePath(),
		System.getProperty("java.io.tmpdir"));
	assertEquals(ajConfig.getRawReportsFolder(), "rr_test");
	assertEquals(ajConfig.getLatexReportsFolderByDate(), "lrdate_test");
	assertEquals(ajConfig.getLatexReportsFolderByTarget(), "lrtarget_test");
	assertEquals(ajConfig.getLatexReportsFolderByConstellation(),
		"lrconst_test");
	assertEquals(ajConfig.getSglReportsFolderByDate(), "trdate_test");
    }
}
