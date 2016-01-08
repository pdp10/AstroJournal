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
import org.astrojournal.generator.AJGenerator;
import org.astrojournal.observation.AJObservation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Piero Dalle Pezze
 */
public class ImportObservationTest {

    /** The log associated to this class */
    private static Logger log = LogManager
	    .getLogger(ImportObservationTest.class);

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
    }

    @Test
    public void test() {

	log.info("Running junit test: " + getClass().getName());

	System.setProperty("aj.aj_files_location",
		System.getProperty("user.dir") + File.separator + "src"
			+ File.separator + "test" + File.separator
			+ "resources");
	AJGenerator ajLatexGenerator = new AJGenerator();
	ajLatexGenerator.importObservations();
	ArrayList<AJObservation> observations = ajLatexGenerator
		.getObservations();

	assertEquals("23/02/2015", observations.get(0).getDate());
	assertEquals("M42", observations.get(0).getObservationItems().get(0)
		.getTarget());
	assertEquals("NGC2244", observations.get(0).getObservationItems()
		.get(1).getTarget());
	assertEquals("NGC2237", observations.get(0).getObservationItems()
		.get(2).getTarget());
	assertEquals("Jupiter", observations.get(0).getObservationItems()
		.get(3).getTarget());

	assertEquals("22/03/2015", observations.get(1).getDate());
	assertEquals("03/06/2015", observations.get(2).getDate());
	assertEquals("06/06/2015", observations.get(3).getDate());
    }
}
