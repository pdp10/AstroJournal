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
public class AJPropertiesTest {

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
	System.clearProperty(AJConfig.SHOW_LATEX_OUTPUT_PROP);
	System.clearProperty(AJConfig.QUIET_PROP);
	System.clearProperty(AJConfig.SHOW_CONFIGURATION_AT_START_PROP);
	System.clearProperty(AJConfig.SHOW_LICENSE_AT_START_PROP);
	System.clearProperty(AJConfig.SHOW_PDFLATEX_VERSION_AT_START_PROP);
	System.clearProperty(AJConfig.FILES_LOCATION_PROP);
	System.clearProperty(AJConfig.RAW_REPORTS_FOLDER_PROP);
	System.clearProperty(AJConfig.LATEX_REPORTS_FOLDER_BY_DATE_PROP);
	System.clearProperty(AJConfig.LATEX_REPORTS_FOLDER_BY_TARGET_PROP);
	System.clearProperty(AJConfig.LATEX_REPORTS_FOLDER_BY_CONSTELLATION_PROP);
	System.clearProperty(AJConfig.SGL_REPORTS_FOLDER_BY_DATE_PROP);
	// Notify AJConfig that the system properties have changed.
	AJConfig.getInstance().reset();
    }

    /**
     * Test AstroJournal boolean Java properties set to true.
     */
    @Test
    public void testAJBooleanTrueProperties() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	System.setProperty(AJConfig.SHOW_LATEX_OUTPUT_PROP, "true");
	System.setProperty(AJConfig.QUIET_PROP, "true");
	System.setProperty(AJConfig.SHOW_CONFIGURATION_AT_START_PROP, "true");
	System.setProperty(AJConfig.SHOW_LICENSE_AT_START_PROP, "true");
	System.setProperty(AJConfig.SHOW_PDFLATEX_VERSION_AT_START_PROP, "true");

	AJConfig ajConfig = AJConfig.getInstance();

	// Notify AJConfig that the system properties have changed.
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

	System.setProperty(AJConfig.SHOW_LATEX_OUTPUT_PROP, "false");
	System.setProperty(AJConfig.QUIET_PROP, "false");
	System.setProperty(AJConfig.SHOW_CONFIGURATION_AT_START_PROP, "false");
	System.setProperty(AJConfig.SHOW_LICENSE_AT_START_PROP, "false");
	System.setProperty(AJConfig.SHOW_PDFLATEX_VERSION_AT_START_PROP, "false");

	AJConfig ajConfig = AJConfig.getInstance();

	// Notify AJConfig that the system properties have changed.
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

	System.setProperty(AJConfig.FILES_LOCATION_PROP,
		System.getProperty("java.io.tmpdir"));
	System.setProperty(AJConfig.RAW_REPORTS_FOLDER_PROP, "rr_test");
	System.setProperty(AJConfig.LATEX_REPORTS_FOLDER_BY_DATE_PROP,
		"lrdate_test");
	System.setProperty(AJConfig.LATEX_REPORTS_FOLDER_BY_TARGET_PROP,
		"lrtarget_test");
	System.setProperty(AJConfig.LATEX_REPORTS_FOLDER_BY_CONSTELLATION_PROP,
		"lrconst_test");
	System.setProperty(AJConfig.SGL_REPORTS_FOLDER_BY_DATE_PROP,
		"trdate_test");

	AJConfig ajConfig = AJConfig.getInstance();

	// Notify AJConfig that the system properties have changed.
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
