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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
 * 
 * @author Piero Dalle Pezze
 */
public class ImportCorrectObservationsTest {

    /**
     * The log associated to this class.
     */
    private static Logger log = LogManager
	    .getLogger(ImportCorrectObservationsTest.class);

    /**
     * The imported observations.
     */
    private static ArrayList<AJObservation> observations;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

	log.info("Running unit test: "
		+ ImportCorrectObservationsTest.class.getName());
	System.out.println("Running unit test: "
		+ ImportCorrectObservationsTest.class.getName());

	System.setProperty("aj.aj_files_location",
		System.getProperty("user.dir") + File.separator + "src"
			+ File.separator + "test" + File.separator
			+ "resources" + File.separator + "correct_observations");
	AJConfig.getInstance().readSystemProperties();

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
	assertEquals(4, observations.size());
    }

    /**
     * Test the number of total targets for all reports.
     */
    @Test
    public void testNumberTargets() {
	int targets = 0;
	for (int i = 0; i < observations.size(); i++) {
	    targets = targets
		    + observations.get(i).getObservationItems().size();
	}
	assertEquals(23, targets);
    }

    /**
     * Test a report meta information.
     */
    @Test
    public void testReport1() {
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
	assertEquals("23/02/2015", observations.get(0).getDate());
	assertEquals("22/03/2015", observations.get(1).getDate());
	assertEquals("03/06/2015", observations.get(2).getDate());
	assertEquals("06/06/2015", observations.get(3).getDate());
    }

}
