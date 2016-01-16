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
	System.clearProperty("aj.latex_output");
	System.clearProperty("aj.quiet");
	System.clearProperty("aj.show_configuration_at_start");
	System.clearProperty("aj.show_license_at_start");
	System.clearProperty("aj.show_pdflatex_version");
	System.clearProperty("aj.aj_files_location");
	System.clearProperty("aj.raw_reports_folder");
	System.clearProperty("aj.latex_reports_folder_by_date");
	System.clearProperty("aj.latex_reports_folder_by_target");
	System.clearProperty("aj.latex_reports_folder_by_constellation");
	System.clearProperty("aj.sgl_reports_folder_by_date");
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

	System.setProperty("aj.latex_output", "true");
	System.setProperty("aj.quiet", "true");
	System.setProperty("aj.show_configuration_at_start", "true");
	System.setProperty("aj.show_license_at_start", "true");
	System.setProperty("aj.show_pdflatex_version", "true");

	AJConfig ajConfig = AJConfig.getInstance();

	// Notify AJConfig that the system properties have changed.
	ajConfig.loadSystemProperties();

	assertTrue(ajConfig.isLatexOutput());
	assertTrue(ajConfig.isQuiet());
	assertTrue(ajConfig.isShowConfigurationAtStart());
	assertTrue(ajConfig.isShowLicenseAtStart());
	assertTrue(ajConfig.isShowPDFLatexVersion());
    }

    /**
     * Test AstroJournal boolean Java properties set to false.
     */
    @Test
    public void testAJBooleanFalseProperties() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	System.setProperty("aj.latex_output", "false");
	System.setProperty("aj.quiet", "false");
	System.setProperty("aj.show_configuration_at_start", "false");
	System.setProperty("aj.show_license_at_start", "false");
	System.setProperty("aj.show_pdflatex_version", "false");

	AJConfig ajConfig = AJConfig.getInstance();

	// Notify AJConfig that the system properties have changed.
	ajConfig.loadSystemProperties();

	assertFalse(ajConfig.isLatexOutput());
	assertFalse(ajConfig.isQuiet());
	assertFalse(ajConfig.isShowConfigurationAtStart());
	assertFalse(ajConfig.isShowLicenseAtStart());
	assertFalse(ajConfig.isShowPDFLatexVersion());
    }

    /**
     * Test AstroJournal String Java properties.
     */
    @Test
    public void testAJStringProperties() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	System.setProperty("aj.aj_files_location",
		System.getProperty("java.io.tmpdir"));
	System.setProperty("aj.raw_reports_folder", "rr_test");
	System.setProperty("aj.latex_reports_folder_by_date", "lrdate_test");
	System.setProperty("aj.latex_reports_folder_by_target", "lrtarget_test");
	System.setProperty("aj.latex_reports_folder_by_constellation",
		"lrconst_test");
	System.setProperty("aj.sgl_reports_folder_by_date", "trdate_test");

	AJConfig ajConfig = AJConfig.getInstance();

	// Notify AJConfig that the system properties have changed.
	ajConfig.loadSystemProperties();

	assertEquals(ajConfig.getAJFilesLocation().getAbsolutePath(),
		System.getProperty("java.io.tmpdir"));
	assertEquals(ajConfig.getRawReportsFolder(), "rr_test");
	assertEquals(ajConfig.getLatexReportsFolderByDate(), "lrdate_test");
	assertEquals(ajConfig.getLatexReportsFolderByTarget(), "lrtarget_test");
	assertEquals(ajConfig.getLatexReportsFolderByConstellation(),
		"lrconst_test");
	assertEquals(ajConfig.getSglReportsFolderByDate(), "trdate_test");
    }
}
