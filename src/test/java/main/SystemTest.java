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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
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
 * three files. The third file contains errors.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 15 Jan 2016
 */
public class SystemTest {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(SystemTest.class);

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	System.setProperty("aj.aj_files_location",
		System.getProperty("user.dir") + File.separator + "src"
			+ File.separator + "test" + File.separator
			+ "resources" + File.separator + "system_test");
	AJConfig.getInstance().loadSystemProperties();

	String[] args = new String[] { "--console" };
	AJMain.main(args);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
	try {
	    AJConfig.getInstance().cleanAJFolder();
	} catch (IOException e) {
	    log.error(e, e);
	}
	File reportByDate = new File(AJConfig.getInstance()
		.getAJFilesLocation().getAbsolutePath()
		+ File.separator
		+ AJConfig.getInstance().getLatexReportsFolderByDate());
	reportByDate.delete();
	File reportByTarget = new File(AJConfig.getInstance()
		.getAJFilesLocation().getAbsolutePath()
		+ File.separator
		+ AJConfig.getInstance().getLatexReportsFolderByTarget());
	reportByTarget.delete();
	File reportByConstellation = new File(AJConfig.getInstance()
		.getAJFilesLocation().getAbsolutePath()
		+ File.separator
		+ AJConfig.getInstance().getLatexReportsFolderByConstellation());
	reportByConstellation.delete();
	File reportByDateSGL = new File(AJConfig.getInstance()
		.getAJFilesLocation().getAbsolutePath()
		+ File.separator
		+ AJConfig.getInstance().getSglReportsFolderByDate());
	reportByDateSGL.delete();
	File headerFooter = new File(AJConfig.getInstance()
		.getAJFilesLocation().getAbsolutePath()
		+ File.separator + "latex_header_footer");
	for (File f : headerFooter.listFiles()) {
	    f.delete();
	}
	headerFooter.delete();

	for (File f : AJConfig.getInstance().getAJFilesLocation().listFiles()) {
	    if (f.getName().endsWith(".tex") || f.getName().endsWith(".pdf")
		    || f.getName().endsWith(".txt")) {
		log.info("\tDeleting file " + f.getAbsolutePath());
		f.delete();
	    }
	}

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
     * Test the generation of report by date.
     */
    @Test
    public void testGeneratedReportByDate() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	// tex + pdf
	assertTrue(new File(AJConfig.getInstance().getAJFilesLocation()
		+ File.separator + AJConfig.REPORT_BY_DATE_FILENAME).exists());
	assertTrue(new File(
		AJConfig.getInstance().getAJFilesLocation()
			+ File.separator
			+ FilenameUtils
				.removeExtension(AJConfig.REPORT_BY_DATE_FILENAME)
			+ ".pdf").exists());
    }

    /**
     * Test the generation of report by target.
     */
    @Test
    public void testGeneratedReportByTarget() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	// tex + pdf
	assertTrue(new File(AJConfig.getInstance().getAJFilesLocation()
		+ File.separator + AJConfig.REPORT_BY_TARGET_FILENAME).exists());
	assertTrue(new File(
		AJConfig.getInstance().getAJFilesLocation()
			+ File.separator
			+ FilenameUtils
				.removeExtension(AJConfig.REPORT_BY_TARGET_FILENAME)
			+ ".pdf").exists());
    }

    /**
     * Test the generation of report by constellation.
     */
    @Test
    public void testGeneratedReportByConstellation() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	// tex + pdf
	assertTrue(new File(AJConfig.getInstance().getAJFilesLocation()
		+ File.separator + AJConfig.REPORT_BY_CONSTELLATION_FILENAME)
		.exists());
	assertTrue(new File(
		AJConfig.getInstance().getAJFilesLocation()
			+ File.separator
			+ FilenameUtils
				.removeExtension(AJConfig.REPORT_BY_CONSTELLATION_FILENAME)
			+ ".pdf").exists());
    }

    /**
     * Test the generation of SGL report by date.
     */
    @Test
    public void testGeneratedSGLReportByDate() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	// txt
	assertTrue(new File(AJConfig.getInstance().getAJFilesLocation()
		+ File.separator + AJConfig.SGL_REPORT_BY_DATE_FILENAME)
		.exists());
    }
}
