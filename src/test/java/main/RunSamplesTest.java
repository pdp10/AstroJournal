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

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.AJMain;
import org.astrojournal.configuration.AJConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This test executes astrojournal via command line on a simple sample set of
 * two observations.
 * 
 * @author Piero Dalle Pezze
 */
public class RunSamplesTest {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(RunSamplesTest.class);

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

	String[] args = new String[] { "--console" };
	System.setProperty("aj.aj_files_location",
		System.getProperty("user.dir") + File.separator + "src"
			+ File.separator + "test" + File.separator
			+ "resources");
	AJMain.main(args);
	try {
	    AJConfig.getInstance().cleanAJFolder();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	System.out.print("\nFile cleaning:");
	for (File f : AJConfig.getInstance().getAJFilesLocation().listFiles()) {
	    if (f.getName().endsWith(".tex") || f.getName().endsWith(".pdf")
		    || f.getName().endsWith(".txt")) {
		System.out.println("\tDeleting file " + f.getAbsolutePath());
		f.delete();
	    }
	}
    }
}
