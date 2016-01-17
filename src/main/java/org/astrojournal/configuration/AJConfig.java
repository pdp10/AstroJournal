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
package org.astrojournal.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * A simple class for configuring AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 12 Dec 2015
 */
public class AJConfig {

    /** The logger */
    private static Logger log = LogManager.getLogger(AJConfig.class);

    /** The AJConfig instance to be used. */
    private static AJConfig instance = new AJConfig();

    // THESE PARAMETERS ARE NOT CONFIGURABLE
    /** The bundle for internationalisation */
    public static final ResourceBundle BUNDLE = ResourceBundle
	    .getBundle("locale/Bundle");

    /** The configuration file name. */
    private static final String AJ_CONFIG_FILENAME = "astrojournal.xml";

    /** The AJ application name. */
    public static final String APPLICATION_NAME = "AstroJournal";

    /** The AJ application version. */
    public static final String APPLICATION_VERSION = "v0.10.14";

    /** The AJ website. */
    public static final String APPLICATION_WEBSITE = "http://pdp10.github.io/AstroJournal";

    /** The name of the main Latex file sorted by date. */
    public static final String REPORT_BY_DATE_FILENAME = "astrojournal_by_date.tex";

    /** The name of the main Latex file sorted by target. */
    public static final String REPORT_BY_TARGET_FILENAME = "astrojournal_by_target.tex";

    /** The name of the main Latex file sorted by constellation. */
    public static final String REPORT_BY_CONSTELLATION_FILENAME = "astrojournal_by_constellation.tex";

    /** The name of the SGL main file sorted by date. */
    public static final String SGL_REPORT_BY_DATE_FILENAME = "astrojournal_by_date_sgl.txt";

    private static final String LATEX_HEADER_FOOTER_FOLDER = "latex_header_footer";

    // NOTE: These fields require File.separator in order to be found by Java in
    // the file system.
    /** The Latex header with path for astrojournal by date. */
    public static final String HEADER_BY_DATE_FILENAME = LATEX_HEADER_FOOTER_FOLDER
	    + File.separator + "header_by_date.tex";

    /** The Latex footer with path for astrojournal by date. */
    public static final String FOOTER_BY_DATE_FILENAME = LATEX_HEADER_FOOTER_FOLDER
	    + File.separator + "footer_by_date.tex";

    /** The Latex header with path for astrojournal by target. */
    public static final String HEADER_BY_TARGET_FILENAME = LATEX_HEADER_FOOTER_FOLDER
	    + File.separator + "header_by_target.tex";

    /** The Latex footer with path for astrojournal by target. */
    public static final String FOOTER_BY_TARGET_FILENAME = LATEX_HEADER_FOOTER_FOLDER
	    + File.separator + "footer_by_target.tex";

    /** The Latex header with path for astrojournal by constellation. */
    public static final String HEADER_BY_CONSTELLATION_FILENAME = LATEX_HEADER_FOOTER_FOLDER
	    + File.separator + "header_by_constellation.tex";

    /** The Latex footer with path for astrojournal by constellation. */
    public static final String FOOTER_BY_CONSTELLATION_FILENAME = LATEX_HEADER_FOOTER_FOLDER
	    + File.separator + "footer_by_constellation.tex";

    /** True if the application should run quietly */
    public static final String QUIET_PROP = "aj.quiet";

    /** True if latex output should be printed. */
    public static final String LATEX_OUTPUT_PROP = "aj.latex_output";

    /** True if the license should be shown at start. */
    public static final String SHOW_LICENSE_AT_START_PROP = "aj.show_license_at_start";

    /** True if the version of pdflatex. */
    public static final String SHOW_PDFLATEX_VERSION_PROP = "aj.show_pdflatex_version";

    /** True if the configuration should be shown at start. */
    public static final String SHOW_CONFIGURATION_AT_START_PROP = "aj.show_configuration_at_start";

    /** The absolute path containing AstroJournal input and output folders. */
    public static final String AJ_FILES_LOCATION_PROP = "aj.aj_files_location";

    /** The relative path containing the raw files (observation input folder). */
    public static final String RAW_REPORTS_FOLDER_PROP = "aj.raw_reports_folder";

    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    public static final String LATEX_REPORTS_FOLDER_BY_DATE_PROP = "aj.latex_reports_folder_by_date";

    /**
     * The name of the folder containing the latex observation files by target
     * (observation output folder).
     */
    public static final String LATEX_REPORTS_FOLDER_BY_TARGET_PROP = "aj.latex_reports_folder_by_target";

    /**
     * The name of the folder containing the latex observation files by
     * constellation (observation output folder).
     */
    public static final String LATEX_REPORTS_FOLDER_BY_CONSTELLATION_PROP = "aj.latex_reports_folder_by_constellation";

    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    public static final String SGL_REPORTS_FOLDER_BY_DATE_PROP = "aj.sgl_reports_folder_by_date";

    // THESE PARAMETERS ARE CONFIGURABLE
    // All AstroJournal classes can only read these values. The only
    // classes
    // which can edit these parameters must be positioned in the
    // configuration
    // package. The setters have therefore "package visibility".

    /** True if the application should run quietly */
    private boolean quiet = false;

    /** True if latex output should be printed. */
    private boolean latexOutput = false;

    /** True if the license should be shown at start. */
    private boolean showLicenseAtStart = true;

    /** True if the version of pdflatex. */
    private boolean showPDFLatexVersion = true;

    /** True if the configuration should be shown at start. */
    private boolean showConfigurationAtStart = true;

    /** The absolute path containing AstroJournal input and output folders. */
    private File ajFilesLocation = new File(System.getProperty("user.home")
	    + File.separator + "AstroJournal_files");

    // NOTE: These field MUST NOT have a file separator because Latex uses '/'
    // by default.
    /**
     * The relative path containing the raw files (observation input folder).
     */
    private String rawReportsFolder = "raw_reports";

    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    private String latexReportsFolderByDate = "latex_reports_by_date";

    /**
     * The name of the folder containing the latex observation files by target
     * (observation output folder).
     */
    private String latexReportsFolderByTarget = "latex_reports_by_target";

    /**
     * The name of the folder containing the latex observation files by
     * constellation (observation output folder).
     */
    private String latexReportsFolderByConstellation = "latex_reports_by_constellation";

    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    private String sglReportsFolderByDate = "sgl_reports_by_date";

    /** The configuration file. */
    private File configFile = null;

    /**
     * Reset AJConfig as at its initialisation. AstroJournal Java properties are
     * not scanned by this method.
     */
    public void reset() {
	quiet = false;
	latexOutput = false;
	showLicenseAtStart = true;
	showPDFLatexVersion = true;
	showConfigurationAtStart = true;
	ajFilesLocation = new File(System.getProperty("user.home")
		+ File.separator + "AstroJournal_files");
	rawReportsFolder = "raw_reports";
	latexReportsFolderByDate = "latex_reports_by_date";
	latexReportsFolderByTarget = "latex_reports_by_target";
	latexReportsFolderByConstellation = "latex_reports_by_constellation";
	sglReportsFolderByDate = "sgl_reports_by_date";
	// Read the configuration file
	configurationInit();
    }

    /**
     * Private constructor for creating only one instance of AJConfig.
     */
    private AJConfig() {
	// Read the configuration file
	configurationInit();
	// Read the system properties (this may override the configuration
	// file)
	loadSystemProperties();
    }

    /**
     * Setup the configuration file and load the preference for AstroJournal.
     */
    private void configurationInit() {

	if (SystemUtils.IS_OS_MAC_OSX) {
	    configFile = new File(System.getProperty("user.home")
		    + File.separator + "." + AJ_CONFIG_FILENAME);
	} else if (SystemUtils.IS_OS_WINDOWS) {
	    configFile = new File(System.getProperty("user.home")
		    + File.separator + AJ_CONFIG_FILENAME);
	} else if (SystemUtils.IS_OS_UNIX) {
	    configFile = new File(System.getProperty("user.home")
		    + File.separator + "." + AJ_CONFIG_FILENAME);
	} else {
	    configFile = new File(System.getProperty("user.home")
		    + File.separator + AJ_CONFIG_FILENAME);
	}

	if (configFile != null && configFile.exists()) {
	    loadConfiguration();
	} else {
	    saveConfiguration();
	}
    }

    /**
     * Load the configuration file.
     */
    private void loadConfiguration() {
	boolean correctLocation = true;
	log.debug("Loading the configuration file "
		+ configFile.getAbsolutePath());
	log.debug("Get the factory");
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	try {
	    log.debug("Get an instance of DocumentBuilder to parse the XML file");
	    DocumentBuilder parser = dbf.newDocumentBuilder();
	    log.debug("Parse using builder to get a DOM representation for the XML file");
	    Document dom = parser.parse(configFile);

	    log.debug("Get the root element");
	    Element rootEle = dom.getDocumentElement();
	    log.debug("Retrieve elements by node list");
	    NodeList nodeList;
	    Element elem;

	    nodeList = rootEle.getElementsByTagName(QUIET_PROP);
	    elem = (Element) nodeList.item(0);
	    quiet = Boolean.parseBoolean(elem.getFirstChild().getNodeValue());
	    log.debug(QUIET_PROP + ":" + quiet);

	    nodeList = rootEle.getElementsByTagName(LATEX_OUTPUT_PROP);
	    elem = (Element) nodeList.item(0);
	    latexOutput = Boolean.parseBoolean(elem.getFirstChild()
		    .getNodeValue());
	    log.debug(LATEX_OUTPUT_PROP + ":" + latexOutput);

	    nodeList = rootEle.getElementsByTagName(SHOW_LICENSE_AT_START_PROP);
	    elem = (Element) nodeList.item(0);
	    showLicenseAtStart = Boolean.parseBoolean(elem.getFirstChild()
		    .getNodeValue());
	    log.debug(SHOW_LICENSE_AT_START_PROP + ":" + showLicenseAtStart);

	    nodeList = rootEle.getElementsByTagName(SHOW_PDFLATEX_VERSION_PROP);
	    elem = (Element) nodeList.item(0);
	    showPDFLatexVersion = Boolean.parseBoolean(elem.getFirstChild()
		    .getNodeValue());
	    log.debug(SHOW_PDFLATEX_VERSION_PROP + ":" + showPDFLatexVersion);

	    nodeList = rootEle
		    .getElementsByTagName(SHOW_CONFIGURATION_AT_START_PROP);
	    elem = (Element) nodeList.item(0);
	    showConfigurationAtStart = Boolean.parseBoolean(elem
		    .getFirstChild().getNodeValue());
	    log.debug(SHOW_CONFIGURATION_AT_START_PROP + ":"
		    + showConfigurationAtStart);

	    nodeList = rootEle.getElementsByTagName(AJ_FILES_LOCATION_PROP);
	    elem = (Element) nodeList.item(0);
	    File oldAJFilesLocation = ajFilesLocation;
	    ajFilesLocation = new File(elem.getFirstChild().getNodeValue());
	    log.debug(AJ_FILES_LOCATION_PROP + ":"
		    + ajFilesLocation.getAbsolutePath());
	    if (ajFilesLocation == null || !ajFilesLocation.exists()
		    || !ajFilesLocation.canWrite()) {
		log.warn("The location for storing AJ Files does not exist.\n"
			+ "Check Edit > Preference or "
			+ configFile.getAbsolutePath()
			+ ".\nUsing default path: "
			+ oldAJFilesLocation.getAbsolutePath());
		ajFilesLocation = oldAJFilesLocation;
		correctLocation = false;
	    }

	    nodeList = rootEle.getElementsByTagName(RAW_REPORTS_FOLDER_PROP);
	    elem = (Element) nodeList.item(0);
	    rawReportsFolder = elem.getFirstChild().getNodeValue();
	    log.debug(RAW_REPORTS_FOLDER_PROP + ":" + rawReportsFolder);

	    nodeList = rootEle
		    .getElementsByTagName(LATEX_REPORTS_FOLDER_BY_DATE_PROP);
	    elem = (Element) nodeList.item(0);
	    latexReportsFolderByDate = elem.getFirstChild().getNodeValue();
	    log.debug(LATEX_REPORTS_FOLDER_BY_DATE_PROP + ":"
		    + latexReportsFolderByDate);

	    nodeList = rootEle
		    .getElementsByTagName(LATEX_REPORTS_FOLDER_BY_TARGET_PROP);
	    elem = (Element) nodeList.item(0);
	    latexReportsFolderByTarget = elem.getFirstChild().getNodeValue();
	    log.debug(LATEX_REPORTS_FOLDER_BY_TARGET_PROP + ":"
		    + latexReportsFolderByTarget);

	    nodeList = rootEle
		    .getElementsByTagName(LATEX_REPORTS_FOLDER_BY_CONSTELLATION_PROP);
	    elem = (Element) nodeList.item(0);
	    latexReportsFolderByConstellation = elem.getFirstChild()
		    .getNodeValue();
	    log.debug(LATEX_REPORTS_FOLDER_BY_CONSTELLATION_PROP + ":"
		    + latexReportsFolderByConstellation);

	    nodeList = rootEle
		    .getElementsByTagName(SGL_REPORTS_FOLDER_BY_DATE_PROP);
	    elem = (Element) nodeList.item(0);
	    sglReportsFolderByDate = elem.getFirstChild().getNodeValue();
	    log.debug(SGL_REPORTS_FOLDER_BY_DATE_PROP + ":"
		    + sglReportsFolderByDate);

	    log.debug("Configuration file " + configFile.getAbsolutePath()
		    + " loaded");
	    if (!correctLocation) {
		saveConfiguration();
	    }
	} catch (ParserConfigurationException e) {
	    log.error("The configuration file " + configFile.getAbsolutePath()
		    + " was not read correctly. A suggestion is to delete it.",
		    e);
	} catch (SAXException e) {
	    log.error("The configuration file " + configFile.getAbsolutePath()
		    + " was not read correctly. A suggestion is to delete it.",
		    e);
	} catch (IOException e) {
	    log.error("The configuration file " + configFile.getAbsolutePath()
		    + " was not read correctly. A suggestion is to delete it.",
		    e);
	}
    }

    /**
     * Save the configuration file.
     */
    public void saveConfiguration() {

	log.debug("Saving the configuration file "
		+ configFile.getAbsolutePath());
	log.debug("Get the factory");
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	try {
	    log.debug("Get an instance of DocumentBuilder");
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    log.debug("Parse using builder to get a new DOM representation for the XML file");
	    Document dom = db.newDocument();

	    log.debug("Create the root element");
	    Element rootElem = dom.createElement(APPLICATION_NAME);
	    dom.appendChild(rootElem);

	    log.debug("Create elements and node and attach it to root astrojournal");
	    Element elem;
	    Text value;
	    elem = dom.createElement(QUIET_PROP);
	    value = dom.createTextNode(String.valueOf(quiet));
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement(LATEX_OUTPUT_PROP);
	    value = dom.createTextNode(String.valueOf(latexOutput));
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement(SHOW_LICENSE_AT_START_PROP);
	    value = dom.createTextNode(String.valueOf(showLicenseAtStart));
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement(SHOW_PDFLATEX_VERSION_PROP);
	    value = dom.createTextNode(String.valueOf(showPDFLatexVersion));
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement(SHOW_CONFIGURATION_AT_START_PROP);
	    value = dom
		    .createTextNode(String.valueOf(showConfigurationAtStart));
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement(AJ_FILES_LOCATION_PROP);
	    value = dom.createTextNode(ajFilesLocation.getAbsolutePath());
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement(RAW_REPORTS_FOLDER_PROP);
	    value = dom.createTextNode(rawReportsFolder);
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement(LATEX_REPORTS_FOLDER_BY_DATE_PROP);
	    value = dom.createTextNode(latexReportsFolderByDate);
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement(LATEX_REPORTS_FOLDER_BY_TARGET_PROP);
	    value = dom.createTextNode(latexReportsFolderByTarget);
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom
		    .createElement(LATEX_REPORTS_FOLDER_BY_CONSTELLATION_PROP);
	    value = dom.createTextNode(latexReportsFolderByConstellation);
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement(SGL_REPORTS_FOLDER_BY_DATE_PROP);
	    value = dom.createTextNode(sglReportsFolderByDate);
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    log.debug("Saving XML document using JAXP");
	    TransformerFactory transFactory = TransformerFactory.newInstance();
	    log.debug("TransformerFactory: "
		    + transFactory.getClass().getName());
	    // transFactory.setAttribute("indent-number", 2);
	    Transformer idTransform = transFactory.newTransformer();
	    idTransform.setOutputProperty(OutputKeys.METHOD, "xml");
	    idTransform.setOutputProperty(OutputKeys.INDENT, "yes");
	    idTransform.setOutputProperty(
		    "{http://xml.apache.org/xslt}indent-amount", "2");
	    Source input = new DOMSource(dom);
	    Result output = new StreamResult(new FileOutputStream(configFile));
	    idTransform.transform(input, output);

	    log.debug("Configuration file " + configFile.getAbsolutePath()
		    + " saved");

	} catch (ParserConfigurationException e) {
	    log.error("Error while trying to instantiate DocumentBuilder " + e,
		    e);
	} catch (TransformerConfigurationException e) {
	    log.error(
		    "The configuration file "
			    + configFile.getAbsolutePath()
			    + " was not saved correctly. A suggestion is to delete it if this exists.",
		    e);
	} catch (TransformerException e) {
	    log.error(
		    "The configuration file "
			    + configFile.getAbsolutePath()
			    + " was not saved correctly. A suggestion is to delete it if this exists.",
		    e);
	} catch (FileNotFoundException e) {
	    log.error(
		    "The configuration file "
			    + configFile.getAbsolutePath()
			    + " was not saved correctly. A suggestion is to delete it if this exists.",
		    e);
	}
    }

    /**
     * Adjust the file separator if needed.
     */
    private void adjustFileSeparator() {
	// File separator must be '/' as this is the default file separator in
	// LaTeX. Therefore, let's replace eventual '\' with '/'.
	rawReportsFolder = rawReportsFolder.replace("\\", "/");
	latexReportsFolderByDate = latexReportsFolderByDate.replace("\\", "/");
	latexReportsFolderByTarget = latexReportsFolderByTarget.replace("\\",
		"/");
	latexReportsFolderByConstellation = latexReportsFolderByConstellation
		.replace("\\", "/");
	sglReportsFolderByDate = sglReportsFolderByDate.replace("\\", "/");
    }

    /**
     * Load the Java System Properties for AstroJournal dynamically.
     */
    public void loadSystemProperties() {

	// Latex output
	if (System.getProperty(LATEX_OUTPUT_PROP) != null) {
	    if (System.getProperty(LATEX_OUTPUT_PROP).equals("true")) {
		latexOutput = true;
	    } else {
		latexOutput = false;
	    }
	}

	// Quiet
	if (System.getProperty(QUIET_PROP) != null) {
	    if (System.getProperty(QUIET_PROP).equals("true")) {
		quiet = true;
	    } else {
		quiet = false;
	    }
	}

	// Show configuration at start
	if (System.getProperty(SHOW_CONFIGURATION_AT_START_PROP) != null) {
	    if (System.getProperty(SHOW_CONFIGURATION_AT_START_PROP).equals(
		    "true")) {
		showConfigurationAtStart = true;
	    } else {
		showConfigurationAtStart = false;
	    }
	}

	// Show license at start
	if (System.getProperty(SHOW_LICENSE_AT_START_PROP) != null) {
	    if (System.getProperty(SHOW_LICENSE_AT_START_PROP).equals("true")) {
		showLicenseAtStart = true;
	    } else {
		showLicenseAtStart = false;
	    }
	}

	// Show the pdflatex version at start
	if (System.getProperty(SHOW_PDFLATEX_VERSION_PROP) != null) {
	    if (System.getProperty(SHOW_PDFLATEX_VERSION_PROP).equals("true")) {
		showPDFLatexVersion = true;
	    } else {
		showPDFLatexVersion = false;
	    }
	}

	// AJ files location
	if (System.getProperty(AJ_FILES_LOCATION_PROP) != null) {
	    File oldAJFilesLocation = ajFilesLocation;
	    ajFilesLocation = new File(
		    System.getProperty(AJ_FILES_LOCATION_PROP));
	    if (!(ajFilesLocation != null && ajFilesLocation.exists() && ajFilesLocation
		    .canWrite())) {
		log.warn("The location for storing AJ Files set as Java Property does not exist or is not writeable.\n"
			+ ".\nUsing default path: "
			+ oldAJFilesLocation.getAbsolutePath());
		ajFilesLocation = oldAJFilesLocation;
		throw new IllegalArgumentException("AJ File Location "
			+ ajFilesLocation.getAbsolutePath()
			+ " does not exist or is not writeable");
	    }
	}

	// Raw reports folder
	if (System.getProperty(RAW_REPORTS_FOLDER_PROP) != null) {
	    rawReportsFolder = new String(
		    System.getProperty(RAW_REPORTS_FOLDER_PROP));
	}

	// Latex reports folder by date
	if (System.getProperty(LATEX_REPORTS_FOLDER_BY_DATE_PROP) != null) {
	    latexReportsFolderByDate = new String(
		    System.getProperty(LATEX_REPORTS_FOLDER_BY_DATE_PROP));
	}

	// Latex reports folder by target
	if (System.getProperty(LATEX_REPORTS_FOLDER_BY_TARGET_PROP) != null) {
	    latexReportsFolderByTarget = new String(
		    System.getProperty(LATEX_REPORTS_FOLDER_BY_TARGET_PROP));
	}

	// Latex reports folder by constellation
	if (System.getProperty(LATEX_REPORTS_FOLDER_BY_CONSTELLATION_PROP) != null) {
	    latexReportsFolderByConstellation = new String(
		    System.getProperty(LATEX_REPORTS_FOLDER_BY_CONSTELLATION_PROP));
	}

	// SGL reports folder by date
	if (System.getProperty(SGL_REPORTS_FOLDER_BY_DATE_PROP) != null) {
	    sglReportsFolderByDate = new String(
		    System.getProperty(SGL_REPORTS_FOLDER_BY_DATE_PROP));
	}

    }

    /**
     * Prepare input and output folders for AstroJournal if these do not exist.
     */
    public void prepareAJFolders() {
	adjustFileSeparator();
	// Create the folders if these do not exist.

	ajFilesLocation.mkdir();

	// AJ header footer folder
	File ajHeaderFooterDir = new File(LATEX_HEADER_FOOTER_FOLDER);
	ajHeaderFooterDir.mkdir();
	// Create a local folder for header_footer and copy the content from
	// the AJ folder to here
	File userHeaderFooterDir = new File(ajFilesLocation.getAbsolutePath()
		+ File.separator + LATEX_HEADER_FOOTER_FOLDER);

	FileFilter latexFilter = new FileFilter() {
	    @Override
	    public boolean accept(File pathname) {
		return pathname.getName().endsWith(".tex");
	    }
	};

	// if the header footer folder does not exist, let's copy the default
	// one.
	if (!userHeaderFooterDir.exists()
		|| userHeaderFooterDir.listFiles(latexFilter).length < 1) {
	    try {
		FileUtils.copyDirectory(ajHeaderFooterDir, userHeaderFooterDir,
			true);
	    } catch (IOException e) {
		log.error(AJConfig.BUNDLE
			.getString("AJ.errCannotCopyHeaderFooterFolder.text"),
			e);
	    }
	}

	// Let's do the same for the raw_reports folder
	// AJ raw reports folder
	File ajRawReportsDir = new File(rawReportsFolder);
	ajRawReportsDir.mkdir();
	// Create a local folder for ajRawReports and copy the content from
	// the AJ folder to here
	File userRawReportsDir = new File(ajFilesLocation.getAbsolutePath()
		+ File.separator + rawReportsFolder);

	FileFilter rawReportsFilter = new FileFilter() {
	    @Override
	    public boolean accept(File pathname) {
		return pathname.getName().endsWith(".csv")
			|| pathname.getName().endsWith(".tsv");
	    }
	};

	// if the raw reports folder does not exist, let's copy the default one.
	// This is convenient for testing.
	if (!userRawReportsDir.exists()
		|| userRawReportsDir.listFiles(rawReportsFilter).length < 1) {
	    try {
		FileUtils.copyDirectory(ajRawReportsDir, userRawReportsDir,
			true);
	    } catch (IOException e) {
		log.error(AJConfig.BUNDLE
			.getString("AJ.errCannotCopyRawReportsFolder.text"), e);
	    }
	}

	new File(ajFilesLocation.getAbsolutePath() + File.separator
		+ latexReportsFolderByDate).mkdir();
	new File(ajFilesLocation.getAbsolutePath() + File.separator
		+ latexReportsFolderByTarget).mkdir();
	new File(ajFilesLocation.getAbsolutePath() + File.separator
		+ latexReportsFolderByConstellation).mkdir();
	new File(ajFilesLocation.getAbsolutePath() + File.separator
		+ sglReportsFolderByDate).mkdir();
    }

    /**
     * Delete the previous output folder content if this is present.
     * 
     * @throws IOException
     *             if the folder could not be cleaned.
     */
    public void cleanAJFolder() throws IOException {
	try {
	    if (!(ajFilesLocation.exists() && ajFilesLocation.canWrite())) {
		throw new FileNotFoundException();
	    }
	    FileUtils.cleanDirectory(new File(ajFilesLocation.getAbsolutePath()
		    + File.separator + latexReportsFolderByDate));
	    FileUtils.cleanDirectory(new File(ajFilesLocation.getAbsolutePath()
		    + File.separator + latexReportsFolderByTarget));
	    FileUtils.cleanDirectory(new File(ajFilesLocation.getAbsolutePath()
		    + File.separator + latexReportsFolderByConstellation));
	    FileUtils.cleanDirectory(new File(ajFilesLocation.getAbsolutePath()
		    + File.separator + sglReportsFolderByDate));
	} catch (IOException e) {
	    throw e;
	}
    }

    /**
     * Return the instance of AJConfig.
     * 
     * @return the instance of AJConfig.
     */
    public static AJConfig getInstance() {
	return instance;
    }

    /**
     * Create a string containing the license for AstroJournal.
     * 
     * @return a string
     */
    public String printLicense() {
	String license = APPLICATION_NAME
		+ " "
		+ APPLICATION_VERSION
		+ " is free software: you can redistribute it and/or modify \n"
		+ "it under the terms of the GNU General Public License as published by \n"
		+ "the Free Software Foundation, either version 3 of the License, or \n"
		+ "(at your option) any later version. \n\n"
		+ "This program is distributed in the hope that it will be useful, \n"
		+ "but WITHOUT ANY WARRANTY; without even the implied warranty of \n"
		+ "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the \n"
		+ "GNU General Public License for more details. \n\n"
		+ "You should have received a copy of the GNU General Public License \n"
		+ "along with this program; if not, see <http://www.gnu.org/licenses/>. \n"
		+ "\n"
		+ "AstroJournal Web Site: <https://github.com/pdp10/AstroJournal>\n\n";
	return license;
    }

    /**
     * Create a string containing the output of the command `pdflatex -version`.
     * 
     * @return the current configuration
     */
    public String printPDFLatexVersion() {
	StringBuilder sb = new StringBuilder();
	String command = "pdflatex";
	String argument = "-version";
	Process p;
	try {
	    p = Runtime.getRuntime().exec(command + " " + argument);
	    // read the output messages from the command
	    BufferedReader stdInput = new BufferedReader(new InputStreamReader(
		    p.getInputStream()));
	    sb.append(AJConfig.BUNDLE
		    .getString("AJ.lblOutputForPDFLatexVersion.text")
		    + " `"
		    + command + " " + argument + "`:\n\n");
	    String temp;
	    while ((temp = stdInput.readLine()) != null) {
		sb.append(temp).append("\n");
	    }
	    stdInput.close();
	    // read the error messages from the command
	    BufferedReader stdError = new BufferedReader(new InputStreamReader(
		    p.getErrorStream()));
	    sb.append("\n"
		    + AJConfig.BUNDLE
			    .getString("AJ.lblErrorForPDFLatexVersion.text")
		    + " `" + command + " " + argument + "`:\n\n");
	    while ((temp = stdError.readLine()) != null) {
		sb.append(temp).append("\n");
	    }
	    stdError.close();
	} catch (IOException e) {
	    // Don't report this exception stack trace.
	    log.fatal("The command "
		    + command
		    + " was not found. \n"
		    + command
		    + " is required for generating PDF documents from \n"
		    + "LaTeX code. \n\n"
		    + "Please install:\n"
		    + " - TeX Live (http://www.tug.org/texlive/) (GNU/Linux Users); \n"
		    + " - MikTeX (http://miktex.org/download) (Windows Users). \n\n"
		    + "See http://pdp10.github.io/AstroJournal/ for information \n"
		    + "about AstroJournal requirements.\n\n" + "Abort.");
	    return "";
	}

	return sb.toString();
    }

    /**
     * Print the current configuration.
     * 
     * @return the current configuration
     */
    public String printConfiguration() {
	String configuration = "AstroJournal current configuration:\n"
		+ "\taj_files_location: " + ajFilesLocation.getAbsolutePath()
		+ "\n" + "\traw_reports_folder: " + rawReportsFolder + "\n"
		+ "\tlatex_reports_by_date: " + latexReportsFolderByDate + "\n"
		+ "\tlatex_reports_by_target: " + latexReportsFolderByTarget
		+ "\n" + "\tlatex_reports_by_constellation: "
		+ latexReportsFolderByConstellation + "\n"
		+ "\tsgl_reports_by_date: " + sglReportsFolderByDate + "\n\n";
	return configuration;
    }

    /**
     * @return the latexOutput
     */
    public boolean isLatexOutput() {
	return latexOutput;
    }

    /**
     * @param latexOutput
     *            the latexOutput to set
     */
    void setLatexOutput(boolean latexOutput) {
	this.latexOutput = latexOutput;
    }

    /**
     * @return the quiet
     */
    public boolean isQuiet() {
	return quiet;
    }

    /**
     * @param quiet
     *            the quiet to set
     */
    void setQuiet(boolean quiet) {
	this.quiet = quiet;
    }

    /**
     * @return the showConfigurationAtStart
     */
    public boolean isShowConfigurationAtStart() {
	return showConfigurationAtStart;
    }

    /**
     * @param showConfigurationAtStart
     *            the showConfigurationAtStart to set
     */
    void setShowConfigurationAtStart(boolean showConfigurationAtStart) {
	this.showConfigurationAtStart = showConfigurationAtStart;
    }

    /**
     * @return the showLicenseAtStart
     */
    public boolean isShowLicenseAtStart() {
	return showLicenseAtStart;
    }

    /**
     * @param showLicenseAtStart
     *            the showLicenseAtStart to set
     */
    void setShowLicenseAtStart(boolean showLicenseAtStart) {
	this.showLicenseAtStart = showLicenseAtStart;
    }

    /**
     * @return the showPDFLatexVersion
     */
    public boolean isShowPDFLatexVersion() {
	return showPDFLatexVersion;
    }

    /**
     * @param showPDFLatexVersion
     *            the showPDFLatexVersion to set
     */
    void setPDFLatexVersion(boolean showPDFLatexVersion) {
	this.showPDFLatexVersion = showPDFLatexVersion;
    }

    /**
     * Return the file containing all input and output files.
     * 
     * @return the AstroJournal Files Location.
     */
    public File getAJFilesLocation() {
	return ajFilesLocation;
    }

    /**
     * @param ajFilesLocation
     *            the ajFilesLocation to set
     */
    void setAJFilesLocation(File ajFilesLocation) {
	this.ajFilesLocation = ajFilesLocation;
    }

    /**
     * @return the rawReportsFolder
     */
    public String getRawReportsFolder() {
	return rawReportsFolder;
    }

    /**
     * @param rawReportsFolder
     *            the rawReportsFolder to set
     */
    void setRawReportsFolder(String rawReportsFolder) {
	this.rawReportsFolder = rawReportsFolder;
    }

    /**
     * @return the latexReportsFolderByDate
     */
    public String getLatexReportsFolderByDate() {
	return latexReportsFolderByDate;
    }

    /**
     * @param latexReportsFolderByDate
     *            the latexReportsFolderByDate to set
     */
    void setLatexReportsFolderByDate(String latexReportsFolderByDate) {
	this.latexReportsFolderByDate = latexReportsFolderByDate;
    }

    /**
     * @return the latexReportsFolderByTarget
     */
    public String getLatexReportsFolderByTarget() {
	return latexReportsFolderByTarget;
    }

    /**
     * @param latexReportsFolderByTarget
     *            the latexReportsFolderByTarget to set
     */
    void setLatexReportsFolderByTarget(String latexReportsFolderByTarget) {
	this.latexReportsFolderByTarget = latexReportsFolderByTarget;
    }

    /**
     * @return the latexReportsFolderByConstellation
     */
    public String getLatexReportsFolderByConstellation() {
	return latexReportsFolderByConstellation;
    }

    /**
     * @param latexReportsFolderByConstellation
     *            the latexReportsFolderByConstellation to set
     */
    void setLatexReportsFolderByConstellation(
	    String latexReportsFolderByConstellation) {
	this.latexReportsFolderByConstellation = latexReportsFolderByConstellation;
    }

    /**
     * @return the sglReportsFolderByDate
     */
    public String getSglReportsFolderByDate() {
	return sglReportsFolderByDate;
    }

    /**
     * @param sglReportsFolderByDate
     *            the sglReportsFolderByDate to set
     */
    void setSglReportsFolderByDate(String sglReportsFolderByDate) {
	this.sglReportsFolderByDate = sglReportsFolderByDate;
    }

}