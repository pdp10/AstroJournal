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
import org.astrojournal.generator.absgen.Importer;
import org.astrojournal.generator.extgen.ExtDataCols;
import org.astrojournal.generator.extgen.ExtMetaDataCols;
import org.astrojournal.generator.extgen.ExtendedTSVImporter;
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
public class ExtTSVImporterTest {

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
	    targets = targets + reports.get(i).getDataRowNumber();
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

	String[] metaData = reports.get(1).getMetaData();

	assertEquals("22/03/2015", metaData[ExtMetaDataCols.DATE_NAME.ordinal()]);
	assertEquals("19:00-22:00", metaData[ExtMetaDataCols.TIME_NAME.ordinal()]);
	assertEquals("Cambridge, UK",
		metaData[ExtMetaDataCols.LOCATION_NAME.ordinal()]);
	assertEquals("12m", metaData[ExtMetaDataCols.ALTITUDE_NAME.ordinal()]);
	assertEquals("3C (no wind)",
		metaData[ExtMetaDataCols.TEMPERATURE_NAME.ordinal()]);
	assertEquals("2 - Slight undulations",
		metaData[ExtMetaDataCols.SEEING_NAME.ordinal()]);
	assertEquals("3 - Somewhat clear",
		metaData[ExtMetaDataCols.TRANSPARENCY_NAME.ordinal()]);
	assertEquals("20.4 mag", metaData[ExtMetaDataCols.DARKNESS_NAME.ordinal()]);
	assertEquals("Tele Vue 60 F6",
		metaData[ExtMetaDataCols.TELESCOPES_NAME.ordinal()]);
	assertEquals("TV Panoptic 24mm, Nagler 7mm, Powermate 2.5x",
		metaData[ExtMetaDataCols.EYEPIECES_NAME.ordinal()]);
	assertEquals("Astronomik OIII",
		metaData[ExtMetaDataCols.FILTERS_NAME.ordinal()]);
    }

    /**
     * Test the targets for a report.
     */
    @Test
    public void testReport1Target3() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	String[] targetEntry = reports.get(1).getData(2);

	assertEquals("Sigma", targetEntry[ExtDataCols.TARGET_NAME.ordinal()]);
	assertEquals("Ori", targetEntry[ExtDataCols.CONSTELLATION_NAME.ordinal()]);
	assertEquals("Mlt star", targetEntry[ExtDataCols.TYPE_NAME.ordinal()]);
	assertEquals("51x", targetEntry[ExtDataCols.POWER_NAME.ordinal()]);
	assertEquals("Sufficient for seeing 5 stars",
		targetEntry[ExtDataCols.NOTES_NAME.ordinal()]);
    }

    /**
     * Test the targets for a report.
     */
    @Test
    public void testReport0Targets() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	String[] metaData = reports.get(0).getMetaData();
	String[] targetEntry;

	assertEquals("23/02/2015", metaData[ExtMetaDataCols.DATE_NAME.ordinal()]);
	targetEntry = reports.get(0).getData(0);
	assertEquals("M42", targetEntry[ExtDataCols.TARGET_NAME.ordinal()]);
	targetEntry = reports.get(0).getData(1);
	assertEquals("NGC2244", targetEntry[ExtDataCols.TARGET_NAME.ordinal()]);
	targetEntry = reports.get(0).getData(2);
	assertEquals("NGC2237", targetEntry[ExtDataCols.TARGET_NAME.ordinal()]);
	targetEntry = reports.get(0).getData(3);
	assertEquals("Jupiter", targetEntry[ExtDataCols.TARGET_NAME.ordinal()]);
    }

    /**
     * Test the last target of a report.
     */
    @Test
    public void testReport0LastTarget() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	String[] targetEntry = reports.get(0).getData(3);

	assertEquals("Jupiter", targetEntry[ExtDataCols.TARGET_NAME.ordinal()]);
	assertEquals("Cnc", targetEntry[ExtDataCols.CONSTELLATION_NAME.ordinal()]);
	assertEquals("Planet", targetEntry[ExtDataCols.TYPE_NAME.ordinal()]);
	assertEquals("129x", targetEntry[ExtDataCols.POWER_NAME.ordinal()]);
	assertEquals(
		"A bit of wind, but the image stays crisp at high magnifications. No aberration.",
		targetEntry[ExtDataCols.NOTES_NAME.ordinal()]);
    }

    /**
     * Test that all reports are read.
     */
    @Test
    public void testReports() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	String[] metaData = reports.get(0).getMetaData();

	assertEquals("23/02/2015", metaData[ExtMetaDataCols.DATE_NAME.ordinal()]);
	metaData = reports.get(1).getMetaData();
	assertEquals("22/03/2015", metaData[ExtMetaDataCols.DATE_NAME.ordinal()]);
	metaData = reports.get(2).getMetaData();
	assertEquals("03/06/2015", metaData[ExtMetaDataCols.DATE_NAME.ordinal()]);
	metaData = reports.get(3).getMetaData();
	assertEquals("06/06/2015", metaData[ExtMetaDataCols.DATE_NAME.ordinal()]);
    }

    /**
     * Test misspelling in report meta data.
     */
    @Test
    public void testMispeltTransparency() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	String[] metaData = reports.get(4).getMetaData();
	String[] targetEntry = reports.get(4).getData(0);

	assertEquals("04/06/2015", metaData[ExtMetaDataCols.DATE_NAME.ordinal()]);
	assertEquals("12C (wind: 0km/h)",
		metaData[ExtMetaDataCols.TEMPERATURE_NAME.ordinal()]);
	assertEquals("", metaData[ExtMetaDataCols.TRANSPARENCY_NAME.ordinal()]);

	assertEquals("Jupiter", targetEntry[ExtDataCols.TARGET_NAME.ordinal()]);
	assertEquals("Planet", targetEntry[ExtDataCols.TYPE_NAME.ordinal()]);
    }

    /**
     * Test lower case in report meta data.
     */
    @Test
    public void testLowerCaseAltitude() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	String[] metaData = reports.get(4).getMetaData();

	assertEquals("04/06/2015", metaData[ExtMetaDataCols.DATE_NAME.ordinal()]);
	assertEquals("12m", metaData[ExtMetaDataCols.ALTITUDE_NAME.ordinal()]);
    }

    /**
     * Test a skipped report.
     */
    @Test
    public void testSkippedReport() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	String[] metaData = reports.get(4).getMetaData();

	assertEquals("04/06/2015", metaData[ExtMetaDataCols.DATE_NAME.ordinal()]);
	// Second report is 05/06/2015 and should be skipped because of
	// unrecognised Datte
	metaData = reports.get(5).getMetaData();
	assertEquals("07/06/2015a", metaData[ExtMetaDataCols.DATE_NAME.ordinal()]);
    }

    /**
     * Test an incomplete but correct target entry.
     */
    @Test
    public void testIncompleteTargetEntry() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	String[] metaData = reports.get(6).getMetaData();
	assertEquals("07/06/2015b", metaData[ExtMetaDataCols.DATE_NAME.ordinal()]);

	// NOTE: The field `constellation` for the Moon is empty but is there.
	// Therefore this is not a malformed entry.
	String[] targetEntry = reports.get(6).getData(1);
	assertEquals("Moon", targetEntry[ExtDataCols.TARGET_NAME.ordinal()]);
	assertEquals("", targetEntry[ExtDataCols.CONSTELLATION_NAME.ordinal()]);
    }

    /**
     * Test an incomplete report.
     */
    @Test
    public void testIncompleteReport() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	String[] metaData = reports.get(7).getMetaData();

	assertEquals("11/06/2015", metaData[ExtMetaDataCols.DATE_NAME.ordinal()]);
	assertEquals(" ", metaData[ExtMetaDataCols.TEMPERATURE_NAME.ordinal()]);
	assertEquals("5 - Clear",
		metaData[ExtMetaDataCols.TRANSPARENCY_NAME.ordinal()]);
	assertEquals("", metaData[ExtMetaDataCols.TELESCOPES_NAME.ordinal()]);
    }

    /**
     * Test an incomplete report.
     */
    @Test
    public void testWrongFieldTelescopes() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	String[] metaData = reports.get(7).getMetaData();
	assertEquals("", metaData[ExtMetaDataCols.TELESCOPES_NAME.ordinal()]);
    }

    /**
     * Test a discarded target entry.
     */
    @Test
    public void testDiscardedTarget() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	String[] metaData = reports.get(7).getMetaData();

	assertEquals("11/06/2015", metaData[ExtMetaDataCols.DATE_NAME.ordinal()]);
	assertEquals(1, reports.get(7).getDataRowNumber());
    }

    /**
     * Test a report with all discarded target entries.
     */
    @Test
    public void testReportWithoutTargets() {
	System.out.println("Running test " + this.getClass().getSimpleName()
		+ "." + new Object() {
		}.getClass().getEnclosingMethod().getName());

	String[] metaData = reports.get(8).getMetaData();

	assertEquals("15/06/2015", metaData[ExtMetaDataCols.DATE_NAME.ordinal()]);
	assertEquals(0, reports.get(8).getDataRowNumber());
    }
}
