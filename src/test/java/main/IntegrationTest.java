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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.commons.io.FilenameUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.astrojournal.configuration.AJConfig;
import org.astrojournal.AJMain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * This test executes astrojournal via command line on a simple sample set of
 * two observations.
 * 
 * @author Piero Dalle Pezze
 */
public class IntegrationTest {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(IntegrationTest.class);
    
    /** 
     * The previous System.out / err
     */ 
    private static PrintStream previousOut, previousErr;  

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    
	log.info("Running integration test: " + IntegrationTest.class.getName());
	System.out.println("Running integration test: " + IntegrationTest.class.getName());

	// disable System.out / err
	previousOut = System.out;
	previousErr = System.err;
	System.setOut(new PrintStream(new OutputStream() {
	  @Override
	  public void write(int arg0) throws IOException { }
	}));
	System.setErr(new PrintStream(new OutputStream() {
	  @Override
	  public void write(int arg0) throws IOException { }
	}));
	
	System.setProperty("aj.aj_files_location",
		System.getProperty("user.dir") + File.separator + "src"
			+ File.separator + "test" + File.separator
			+ "resources" + File.separator
			+ "integration_test");
	AJConfig.getInstance().readSystemProperties();

	String[] args = new String[] { "--console" };
	AJMain.main(args);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {

	System.out.print("\nFile cleaning:");
	try {
	    AJConfig.getInstance().cleanAJFolder();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	File reportByDate = new File(AJConfig.getInstance().getAJFilesLocation().getAbsolutePath() + File.separator + 
	AJConfig.getInstance().getLatexReportsFolderByDate());
	reportByDate.delete();
	File reportByTarget = new File(AJConfig.getInstance().getAJFilesLocation().getAbsolutePath() + File.separator + 
	AJConfig.getInstance().getLatexReportsFolderByTarget());
	reportByTarget.delete();
	File reportByConstellation = new File(AJConfig.getInstance().getAJFilesLocation().getAbsolutePath() + File.separator + 
	AJConfig.getInstance().getLatexReportsFolderByConstellation());
	reportByConstellation.delete();
	File reportByDateSGL = new File(AJConfig.getInstance().getAJFilesLocation().getAbsolutePath() + File.separator + 
	AJConfig.getInstance().getSglReportsFolderByDate());
	reportByDateSGL.delete();
	File headerFooter = new File(AJConfig.getInstance().getAJFilesLocation().getAbsolutePath() + File.separator + 
	"latex_header_footer");
	for (File f : headerFooter.listFiles()) {
	    f.delete();
	}
	headerFooter.delete();
	
	for (File f : AJConfig.getInstance().getAJFilesLocation().listFiles()) {
	    if (f.getName().endsWith(".tex") || f.getName().endsWith(".pdf")
		    || f.getName().endsWith(".txt")) {
		System.out.println("\tDeleting file " + f.getAbsolutePath());
		f.delete();
	    }
	}
	
	// reset the previous stream.
	System.setOut(previousOut);
	System.setErr(previousErr); 
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
    public void testGeneratedReportByDate() {
	// tex + pdf
	assertTrue(new File(AJConfig.getInstance().getAJFilesLocation() + File.separator + AJConfig.REPORT_BY_DATE_FILENAME).exists());
	assertTrue(new File(AJConfig.getInstance().getAJFilesLocation() + File.separator + FilenameUtils.removeExtension(AJConfig.REPORT_BY_DATE_FILENAME) + ".pdf").exists());
    }
    
    @Test
    public void testGeneratedReportByTarget() {
	// tex + pdf
	assertTrue(new File(AJConfig.getInstance().getAJFilesLocation() + File.separator + AJConfig.REPORT_BY_TARGET_FILENAME).exists());
	assertTrue(new File(AJConfig.getInstance().getAJFilesLocation() + File.separator + FilenameUtils.removeExtension(AJConfig.REPORT_BY_TARGET_FILENAME) + ".pdf").exists());
    }
    
    @Test
    public void testGeneratedReportByConstellation() {
	// tex + pdf
	assertTrue(new File(AJConfig.getInstance().getAJFilesLocation() + File.separator + AJConfig.REPORT_BY_CONSTELLATION_FILENAME).exists());
	assertTrue(new File(AJConfig.getInstance().getAJFilesLocation() + File.separator + FilenameUtils.removeExtension(AJConfig.REPORT_BY_CONSTELLATION_FILENAME) + ".pdf").exists());
    }

    @Test
    public void testGeneratedSGLReportByDate() {
	// txt
	assertTrue(new File(AJConfig.getInstance().getAJFilesLocation() + File.separator + AJConfig.SGL_REPORT_BY_DATE_FILENAME).exists());
    }
}
