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

import java.io.File;
import java.util.List;

import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ajconfiguration.AJConfiguration;
import org.astrojournal.configuration.ajconfiguration.AJPropertyConstants;
import org.astrojournal.generator.Report;
import org.astrojournal.generator.importer.ExtendedTSVImporter;
import org.astrojournal.generator.importer.Importer;
import org.astrojournal.generator.reportdata.ExtendedReportHeader;
import org.astrojournal.generator.reportdata.ExtendedReportItem;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Import a set of correct observations.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 15 Jan 2016
 */
public class ExtendedTSVImporterTest {

    /**
     * The imported reports.
     */
    private static List<Report> reports;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	System.setProperty(AJPropertyConstants.FILES_LOCATION.getKey(),
		System.getProperty("user.dir") + File.separator + "src"
			+ File.separator + "test" + File.separator
			+ "resources" + File.separator
			+ "aj_tab_separated_value_importer_test");

	Configuration config = new AJConfiguration();

	// Load the new properties
	config.loadSystemProperties();

	Importer importer = new ExtendedTSVImporter();

	// TODO: TEMPORARY IMPLEMENTATION. WITH DEPENDENCY INJECTION, THESE
	// PARAMETERS ARE PASSED BY THE INJECTOR
	// THEREFORE, THERE IS NO NEED TO SET THEM HERE!! :)
	importer.setFilesLocation(config
		.getProperty(AJPropertyConstants.FILES_LOCATION.getKey()));
	importer.setRawReportFolder(config
		.getProperty(AJPropertyConstants.RAW_REPORTS_FOLDER.getKey()));
	// TODO: END

	reports = importer.importReports();

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
     * Test the number of reports.
     */
    @Test
    public void testNumberReports() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals(9, reports.size());
    }

    /**
     * Test the number of total targets for all reports.
     */
    @Test
    public void testNumberTargets() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	int targets = 0;
	for (int i = 0; i < reports.size(); i++) {
	    targets = targets + reports.get(i).getReportItems().size();
	}
	assertEquals(28, targets);
    }

    /**
     * Test a report meta information.
     */
    @Test
    public void testReport1() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	ExtendedReportHeader extReportHeader = (ExtendedReportHeader) (reports
		.get(1).getReportHeader());

	assertEquals("22/03/2015", extReportHeader.getDate());
	assertEquals("19:00-22:00", extReportHeader.getTime());
	assertEquals("Cambridge, UK", extReportHeader.getLocation());
	assertEquals("12m", extReportHeader.getAltitude());
	assertEquals("3C (no wind)", extReportHeader.getTemperature());
	assertEquals("2 - Slight undulations", extReportHeader.getSeeing());
	assertEquals("3 - Somewhat clear", extReportHeader.getTransparency());
	assertEquals("20.4 mag", extReportHeader.getDarkness());
	assertEquals("Tele Vue 60 F6", extReportHeader.getTelescopes());
	assertEquals("TV Panoptic 24mm, Nagler 7mm, Powermate 2.5x",
		extReportHeader.getEyepieces());
	assertEquals("Astronomik OIII", extReportHeader.getFilters());
    }

    /**
     * Test the targets for a report.
     */
    @Test
    public void testReport1Target3() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	ExtendedReportItem extReportItem = (ExtendedReportItem) (reports.get(1)
		.getReportItems().get(2));

	assertEquals("Sigma", extReportItem.getTarget());
	assertEquals("Ori", extReportItem.getConstellation());
	assertEquals("Mlt star", extReportItem.getType());
	assertEquals("51x", extReportItem.getPower());
	assertEquals("Sufficient for seeing 5 stars", extReportItem.getNotes());
    }

    /**
     * Test the targets for a report.
     */
    @Test
    public void testReport0Targets() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	ExtendedReportHeader extReportHeader = (ExtendedReportHeader) (reports
		.get(0).getReportHeader());
	ExtendedReportItem extReportItem = (ExtendedReportItem) (reports.get(0)
		.getReportItems().get(0));

	assertEquals("23/02/2015", extReportHeader.getDate());

	extReportItem = (ExtendedReportItem) (reports.get(0).getReportItems()
		.get(0));
	assertEquals("M42", extReportItem.getTarget());
	extReportItem = (ExtendedReportItem) (reports.get(0).getReportItems()
		.get(1));
	assertEquals("NGC2244", extReportItem.getTarget());
	extReportItem = (ExtendedReportItem) (reports.get(0).getReportItems()
		.get(2));
	assertEquals("NGC2237", extReportItem.getTarget());
	extReportItem = (ExtendedReportItem) (reports.get(0).getReportItems()
		.get(3));
	assertEquals("Jupiter", extReportItem.getTarget());
    }

    /**
     * Test the last target of a report.
     */
    @Test
    public void testReport0LastTarget() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	ExtendedReportItem extReportItem = (ExtendedReportItem) (reports.get(0)
		.getReportItems().get(3));

	assertEquals("Jupiter", extReportItem.getTarget());
	assertEquals("Cnc", extReportItem.getConstellation());
	assertEquals("Planet", extReportItem.getType());
	assertEquals("129x", extReportItem.getPower());
	assertEquals(
		"A bit of wind, but the image stays crisp at high magnifications. No aberration.",
		extReportItem.getNotes());
    }

    /**
     * Test that all reports are read.
     */
    @Test
    public void testReports() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	ExtendedReportHeader extReportHeader = (ExtendedReportHeader) (reports
		.get(0).getReportHeader());
	assertEquals("23/02/2015", extReportHeader.getDate());
	extReportHeader = (ExtendedReportHeader) (reports.get(1)
		.getReportHeader());
	assertEquals("22/03/2015", extReportHeader.getDate());
	extReportHeader = (ExtendedReportHeader) (reports.get(2)
		.getReportHeader());
	assertEquals("03/06/2015", extReportHeader.getDate());
	extReportHeader = (ExtendedReportHeader) (reports.get(3)
		.getReportHeader());
	assertEquals("06/06/2015", extReportHeader.getDate());
    }

    /**
     * Test misspelling in report meta data.
     */
    @Test
    public void testMispeltTransparency() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	ExtendedReportHeader extReportHeader = (ExtendedReportHeader) (reports
		.get(4).getReportHeader());
	assertEquals("04/06/2015", extReportHeader.getDate());
	assertEquals("12C (wind: 0km/h)", extReportHeader.getTemperature());
	assertEquals("", extReportHeader.getTransparency());

	ExtendedReportItem extReportItem = (ExtendedReportItem) (reports.get(4)
		.getReportItems().get(0));
	assertEquals("Jupiter", extReportItem.getTarget());
	assertEquals("Planet", extReportItem.getType());
    }

    /**
     * Test lower case in report meta data.
     */
    @Test
    public void testLowerCaseAltitude() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	ExtendedReportHeader extReportHeader = (ExtendedReportHeader) (reports
		.get(4).getReportHeader());
	assertEquals("04/06/2015", extReportHeader.getDate());
	assertEquals("12m", extReportHeader.getAltitude());
    }

    /**
     * Test a skipped report.
     */
    @Test
    public void testSkippedReport() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	ExtendedReportHeader extReportHeader = (ExtendedReportHeader) (reports
		.get(4).getReportHeader());
	assertEquals("04/06/2015", extReportHeader.getDate());
	// Second report is 05/06/2015 and should be skipped because of
	// unrecognised Datte
	extReportHeader = (ExtendedReportHeader) (reports.get(5)
		.getReportHeader());
	assertEquals("07/06/2015a", extReportHeader.getDate());
    }

    /**
     * Test an incomplete but correct target entry.
     */
    @Test
    public void testIncompleteTargetEntry() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	ExtendedReportHeader extReportHeader = (ExtendedReportHeader) (reports
		.get(6).getReportHeader());
	assertEquals("07/06/2015b", extReportHeader.getDate());

	// NOTE: The field `constellation` for the Moon is empty but is there.
	// Therefore this is not a malformed entry.
	ExtendedReportItem extReportItem = (ExtendedReportItem) (reports.get(6)
		.getReportItems().get(1));
	assertEquals("Moon", extReportItem.getTarget());
	assertEquals("", extReportItem.getConstellation());
    }

    /**
     * Test an incomplete report.
     */
    @Test
    public void testIncompleteReport() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	ExtendedReportHeader extReportHeader = (ExtendedReportHeader) (reports
		.get(7).getReportHeader());
	assertEquals("11/06/2015", extReportHeader.getDate());
	assertEquals(" ", extReportHeader.getTemperature());
	assertEquals("5 - Clear", extReportHeader.getTransparency());
	assertEquals("", extReportHeader.getTelescopes());
    }

    /**
     * Test an incomplete report.
     */
    @Test
    public void testWrongFieldTelescopes() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());
	ExtendedReportHeader extReportHeader = (ExtendedReportHeader) (reports
		.get(7).getReportHeader());
	assertEquals("", extReportHeader.getTelescopes());
    }

    /**
     * Test a discarded target entry.
     */
    @Test
    public void testDiscardedTarget() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	ExtendedReportHeader extReportHeader = (ExtendedReportHeader) (reports
		.get(7).getReportHeader());
	assertEquals("11/06/2015", extReportHeader.getDate());

	assertEquals(1, reports.get(7).getReportItems().size());
    }

    /**
     * Test a report with all discarded target entries.
     */
    @Test
    public void testReportWithoutTargets() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());
	ExtendedReportHeader extReportHeader = (ExtendedReportHeader) (reports
		.get(8).getReportHeader());
	assertEquals("15/06/2015", extReportHeader.getDate());

	assertEquals(0, reports.get(8).getReportItems().size());
    }

}
