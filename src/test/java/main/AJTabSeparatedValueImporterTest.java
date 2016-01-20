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
import java.util.ArrayList;

import org.astrojournal.configuration.AJConfig;
import org.astrojournal.configuration.AJProperties;
import org.astrojournal.generator.ajimporter.AJImporter;
import org.astrojournal.generator.ajimporter.AJTabSeparatedValueImporter;
import org.astrojournal.generator.observation.AJObservation;
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
public class AJTabSeparatedValueImporterTest {

    /**
     * The imported observations.
     */
    private static ArrayList<AJObservation> observations;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	System.setProperty(AJProperties.FILES_LOCATION,
		System.getProperty("user.dir") + File.separator + "src"
			+ File.separator + "test" + File.separator
			+ "resources" + File.separator
			+ "aj_tab_separated_value_importer_test");

	// Load the new properties
	AJConfig.getInstance().loadSystemProperties();

	AJImporter ajImporter = new AJTabSeparatedValueImporter();

	// TODO: TEMPORARY IMPLEMENTATION. WITH DEPENDENCY INJECTION, THESE
	// PARAMETERS ARE PASSED BY THE INJECTOR
	// THEREFORE, THERE IS NO NEED TO SET THEM HERE!! :)
	ajImporter.setFilesLocation(AJConfig.getInstance().getProperty(
		AJProperties.FILES_LOCATION));
	ajImporter.setRawReportFolder(AJConfig.getInstance().getProperty(
		AJProperties.RAW_REPORTS_FOLDER));
	// TODO: END

	observations = ajImporter.importObservations();
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
    }

    /**
     * Test the number of reports.
     */
    @Test
    public void testNumberReports() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals(9, observations.size());
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
	for (int i = 0; i < observations.size(); i++) {
	    targets = targets
		    + observations.get(i).getObservationItems().size();
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

	assertEquals("22/03/2015", observations.get(1).getDate());
	assertEquals("19:00-22:00", observations.get(1).getTime());
	assertEquals("Cambridge, UK", observations.get(1).getLocation());
	assertEquals("12m", observations.get(1).getAltitude());
	assertEquals("3C (no wind)", observations.get(1).getTemperature());
	assertEquals("2 - Slight undulations", observations.get(1).getSeeing());
	assertEquals("3 - Somewhat clear", observations.get(1)
		.getTransparency());
	assertEquals("20.4 mag", observations.get(1).getDarkness());
	assertEquals("Tele Vue 60 F6", observations.get(1).getTelescopes());
	assertEquals("TV Panoptic 24mm, Nagler 7mm, Powermate 2.5x",
		observations.get(1).getEyepieces());
	assertEquals("Astronomik OIII", observations.get(1).getFilters());
    }

    /**
     * Test the targets for a report.
     */
    @Test
    public void testReport1Target3() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("Sigma", observations.get(1).getObservationItems().get(2)
		.getTarget());
	assertEquals("Ori", observations.get(1).getObservationItems().get(2)
		.getConstellation());
	assertEquals("Mlt star",
		observations.get(1).getObservationItems().get(2).getType());
	assertEquals("51x", observations.get(1).getObservationItems().get(2)
		.getPower());
	assertEquals("Sufficient for seeing 5 stars", observations.get(1)
		.getObservationItems().get(2).getNotes());
    }

    /**
     * Test the targets for a report.
     */
    @Test
    public void testReport0Targets() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("23/02/2015", observations.get(0).getDate());
	assertEquals("M42", observations.get(0).getObservationItems().get(0)
		.getTarget());
	assertEquals("NGC2244", observations.get(0).getObservationItems()
		.get(1).getTarget());
	assertEquals("NGC2237", observations.get(0).getObservationItems()
		.get(2).getTarget());
	assertEquals("Jupiter", observations.get(0).getObservationItems()
		.get(3).getTarget());
    }

    /**
     * Test the last target of a report.
     */
    @Test
    public void testReport0LastTarget() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("Jupiter", observations.get(0).getObservationItems()
		.get(3).getTarget());
	assertEquals("Cnc", observations.get(0).getObservationItems().get(3)
		.getConstellation());
	assertEquals("Planet", observations.get(0).getObservationItems().get(3)
		.getType());
	assertEquals("129x", observations.get(0).getObservationItems().get(3)
		.getPower());
	assertEquals(
		"A bit of wind, but the image stays crisp at high magnifications. No aberration.",
		observations.get(0).getObservationItems().get(3).getNotes());
    }

    /**
     * Test that all reports are read.
     */
    @Test
    public void testReports() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("23/02/2015", observations.get(0).getDate());
	assertEquals("22/03/2015", observations.get(1).getDate());
	assertEquals("03/06/2015", observations.get(2).getDate());
	assertEquals("06/06/2015", observations.get(3).getDate());
    }

    /**
     * Test mispelling in report meta data.
     */
    @Test
    public void testMispeltTransparency() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("04/06/2015", observations.get(4).getDate());
	assertEquals("12C (wind: 0km/h)", observations.get(4).getTemperature());
	assertEquals("", observations.get(4).getTransparency());
	assertEquals("Jupiter", observations.get(4).getObservationItems()
		.get(0).getTarget());
	assertEquals("Planet", observations.get(4).getObservationItems().get(0)
		.getType());
    }

    /**
     * Test lower case in report meta data.
     */
    @Test
    public void testLowerCaseAltitude() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("04/06/2015", observations.get(4).getDate());
	assertEquals("12m", observations.get(4).getAltitude());
    }

    /**
     * Test a skipped report.
     */
    @Test
    public void testSkippedReport() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("04/06/2015", observations.get(4).getDate());
	// Second report is 05/06/2015 and should be skipped because of
	// unrecognised Datte
	assertEquals("07/06/2015a", observations.get(5).getDate());
    }

    /**
     * Test an incomplete but correct target entry.
     */
    @Test
    public void testIncompleteTargetEntry() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("07/06/2015b", observations.get(6).getDate());
	// NOTE: The field `constellation` for the Moon is empty but is there.
	// Therefore this is not a malformed entry.
	assertEquals("Moon", observations.get(6).getObservationItems().get(1)
		.getTarget());
	assertEquals("", observations.get(6).getObservationItems().get(1)
		.getConstellation());
    }

    /**
     * Test an incomplete report.
     */
    @Test
    public void testIncompleteReport() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("11/06/2015", observations.get(7).getDate());
	assertEquals(" ", observations.get(7).getTemperature());
	assertEquals("5 - Clear", observations.get(7).getTransparency());
	assertEquals("", observations.get(7).getTelescopes());
    }

    /**
     * Test an incomplete report.
     */
    @Test
    public void testWrongFieldTelescopes() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("", observations.get(7).getTelescopes());
    }

    /**
     * Test a discarded target entry.
     */
    @Test
    public void testDiscardedTarget() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("11/06/2015", observations.get(7).getDate());
	assertEquals(1, observations.get(7).getObservationItems().size());
    }

    /**
     * Test a report with all discarded target entries.
     */
    @Test
    public void testReportWithoutTargets() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("15/06/2015", observations.get(8).getDate());
	assertEquals(0, observations.get(8).getObservationItems().size());
    }

}
