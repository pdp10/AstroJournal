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
import org.astrojournal.generator.ajimporter.AJImporter;
import org.astrojournal.generator.ajimporter.AJTabSeparatedValueImporter;
import org.astrojournal.generator.observation.AJObservation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Import a set of incorrect observations.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 15 Jan 2016
 */
public class ImportWrongObservationsTest {

    /**
     * The imported observations.
     */
    private static ArrayList<AJObservation> observations;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	System.setProperty(AJConfig.AJ_FILES_LOCATION_PROP,
		System.getProperty("user.dir") + File.separator + "src"
			+ File.separator + "test" + File.separator
			+ "resources" + File.separator + "wrong_observations");
	AJConfig.getInstance().loadSystemProperties();

	AJImporter ajTabSeparatedValueImporter = new AJTabSeparatedValueImporter();
	observations = ajTabSeparatedValueImporter.importObservations();
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

	assertEquals(5, observations.size());
    }

    /**
     * Test the number of targets for all reports.
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
	assertEquals(5, targets);
    }

    /**
     * Test mispelling in report meta data.
     */
    @Test
    public void testMispeltTransparency() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("04/06/2015", observations.get(0).getDate());
	assertEquals("12C (wind: 0km/h)", observations.get(0).getTemperature());
	assertEquals("", observations.get(0).getTransparency());
	assertEquals("Jupiter", observations.get(0).getObservationItems()
		.get(0).getTarget());
	assertEquals("Planet", observations.get(0).getObservationItems().get(0)
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

	assertEquals("04/06/2015", observations.get(0).getDate());
	assertEquals("12m", observations.get(0).getAltitude());
    }

    /**
     * Test a skipped report.
     */
    @Test
    public void testSkippedReport() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("04/06/2015", observations.get(0).getDate());
	// Second report is 05/06/2015 and should be skipped because of
	// unrecognised Datte
	assertEquals("07/06/2015a", observations.get(1).getDate());
    }

    /**
     * Test an incomplete but correct target entry.
     */
    @Test
    public void testIncompleteTargetEntry() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("07/06/2015b", observations.get(2).getDate());
	// NOTE: The field `constellation` for the Moon is empty but is there.
	// Therefore this is not a malformed entry.
	assertEquals("Moon", observations.get(2).getObservationItems().get(1)
		.getTarget());
	assertEquals("", observations.get(2).getObservationItems().get(1)
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

	assertEquals("11/06/2015", observations.get(3).getDate());
	assertEquals(" ", observations.get(3).getTemperature());
	assertEquals("5 - Clear", observations.get(3).getTransparency());
	assertEquals("", observations.get(3).getTelescopes());
    }

    /**
     * Test an incomplete report.
     */
    @Test
    public void testWrongFieldTelescopes() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("", observations.get(3).getTelescopes());
    }

    /**
     * Test a discarded target entry.
     */
    @Test
    public void testDiscardedTarget() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("11/06/2015", observations.get(3).getDate());
	assertEquals(1, observations.get(3).getObservationItems().size());
    }

    /**
     * Test a report with all discarded target entries.
     */
    @Test
    public void testReportWithoutTargets() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	assertEquals("15/06/2015", observations.get(4).getDate());
	assertEquals(0, observations.get(4).getObservationItems().size());
    }

}
