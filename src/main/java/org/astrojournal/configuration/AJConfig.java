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
 * A class containing the configuration of AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 12 Dec 2015
 */
public class AJConfig {
    /*
     * NOTE: To be a proper singleton and not a `global variable`, store here
     * the variables which can change over time: the actual configuration.
     * Therefore this object is always accessed via AJConfig.getInstance(). To
     * dynamically change the internal status of this singleton, one can either
     * invoke System.setProperty(AJProperty prop, value) and then call the
     * method AJConfig.getInstance().loadAJProperties() or change the
     * configuration file. This improves thread safety although a proper
     * thread-safe mechanism has not yet been implemented.
     * 
     * 
     * Global constants go in AJConstants. AJ Java properties go in
     * AJProperties.
     */

    /** The logger */
    private static Logger log = LogManager.getLogger(AJConfig.class);

    /**
     * The AJConfig instance to be used. Eager initialisation for this
     * singleton.
     */
    private static AJConfig singleton = new AJConfig();

    /** The configuration file name. */
    private String configFileName = "astrojournal.xml";

    /**
     * The configuration file (a reference to the real file in the file system).
     */
    private File configFile = null;

    /** True if the application should run quietly */
    private boolean quiet = false;

    /** True if latex output should be printed. */
    private boolean showLatexOutput = false;

    /** True if the license should be shown at start. */
    private boolean showLicenseAtStart = true;

    /** True if the version of pdflatex. */
    private boolean showPDFLatexVersionAtStart = true;

    /** True if the configuration should be shown at start. */
    private boolean showConfigurationAtStart = true;

    /** The absolute path containing AstroJournal input and output folders. */
    private File filesLocation = new File(System.getProperty("user.home")
	    + File.separator + "AstroJournal_files");

    /*
     * NOTE: These field MUST NOT have a file separator because Latex uses '/'
     * by default.
     */
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

    /** The bundle for internationalisation */
    private ResourceBundle localeBundle = ResourceBundle
	    .getBundle("locale/Bundle");

    /**
     * Reset AJConfig as at its initialisation. AstroJournal Java properties are
     * not scanned by this method.
     */
    public void reset() {
	quiet = false;
	showLatexOutput = false;
	showLicenseAtStart = true;
	showPDFLatexVersionAtStart = true;
	showConfigurationAtStart = true;
	filesLocation = new File(System.getProperty("user.home")
		+ File.separator + "AstroJournal_files");
	rawReportsFolder = "raw_reports";
	latexReportsFolderByDate = "latex_reports_by_date";
	latexReportsFolderByTarget = "latex_reports_by_target";
	latexReportsFolderByConstellation = "latex_reports_by_constellation";
	sglReportsFolderByDate = "sgl_reports_by_date";
	localeBundle = ResourceBundle.getBundle("locale/Bundle");
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
	loadAJProperties();
    }

    /**
     * Return the singleton instance of AJConfig.
     * 
     * @return the instance of AJConfig.
     */
    public static AJConfig getInstance() {
	return singleton;
    }

    /**
     * Setup the configuration file and load the preference for AstroJournal.
     */
    private void configurationInit() {

	if (SystemUtils.IS_OS_MAC_OSX) {
	    configFile = new File(System.getProperty("user.home")
		    + File.separator + "." + configFileName);
	} else if (SystemUtils.IS_OS_WINDOWS) {
	    configFile = new File(System.getProperty("user.home")
		    + File.separator + configFileName);
	} else if (SystemUtils.IS_OS_UNIX) {
	    configFile = new File(System.getProperty("user.home")
		    + File.separator + "." + configFileName);
	} else {
	    configFile = new File(System.getProperty("user.home")
		    + File.separator + configFileName);
	}

	if (configFile != null && configFile.exists()) {
	    loadConfiguration();
	} else {
	    saveConfiguration();
	}
    }

    /**
     * Test a XML element.
     * 
     * @param nodeList
     * @param elem
     * @throws IOException
     */
    private void testXMLElement(NodeList nodeList, Element elem)
	    throws IOException {
	if (elem == null) {
	    throw new IOException("NodeList: " + nodeList.item(0)
		    + " returned null element");
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
	    log.debug("Retrieving elements by tag name and first child by node list");
	    NodeList nodeList;
	    Element elem;

	    nodeList = rootEle.getElementsByTagName(AJProperties.QUIET);
	    elem = (Element) nodeList.item(0);
	    testXMLElement(nodeList, elem);
	    quiet = Boolean.parseBoolean(elem.getFirstChild().getNodeValue());
	    log.debug(AJProperties.QUIET + ":" + quiet);

	    nodeList = rootEle
		    .getElementsByTagName(AJProperties.SHOW_LATEX_OUTPUT);
	    elem = (Element) nodeList.item(0);
	    testXMLElement(nodeList, elem);
	    showLatexOutput = Boolean.parseBoolean(elem.getFirstChild()
		    .getNodeValue());
	    log.debug(AJProperties.SHOW_LATEX_OUTPUT + ":" + showLatexOutput);

	    nodeList = rootEle
		    .getElementsByTagName(AJProperties.SHOW_LICENSE_AT_START);
	    elem = (Element) nodeList.item(0);
	    testXMLElement(nodeList, elem);
	    showLicenseAtStart = Boolean.parseBoolean(elem.getFirstChild()
		    .getNodeValue());
	    log.debug(AJProperties.SHOW_LICENSE_AT_START + ":"
		    + showLicenseAtStart);

	    nodeList = rootEle
		    .getElementsByTagName(AJProperties.SHOW_PDFLATEX_VERSION_AT_START);
	    elem = (Element) nodeList.item(0);
	    testXMLElement(nodeList, elem);
	    showPDFLatexVersionAtStart = Boolean.parseBoolean(elem
		    .getFirstChild().getNodeValue());
	    log.debug(AJProperties.SHOW_PDFLATEX_VERSION_AT_START + ":"
		    + showPDFLatexVersionAtStart);

	    nodeList = rootEle
		    .getElementsByTagName(AJProperties.SHOW_CONFIGURATION_AT_START);
	    elem = (Element) nodeList.item(0);
	    testXMLElement(nodeList, elem);
	    showConfigurationAtStart = Boolean.parseBoolean(elem
		    .getFirstChild().getNodeValue());
	    log.debug(AJProperties.SHOW_CONFIGURATION_AT_START + ":"
		    + showConfigurationAtStart);

	    nodeList = rootEle
		    .getElementsByTagName(AJProperties.FILES_LOCATION);
	    elem = (Element) nodeList.item(0);
	    testXMLElement(nodeList, elem);
	    File oldFilesLocation = filesLocation;
	    filesLocation = new File(elem.getFirstChild().getNodeValue());
	    log.debug(AJProperties.FILES_LOCATION + ":"
		    + filesLocation.getAbsolutePath());
	    if (filesLocation == null || !filesLocation.exists()
		    || !filesLocation.canWrite()) {
		log.warn("The location for storing AJ Files does not exist.\n"
			+ "Check Edit > Preference or "
			+ configFile.getAbsolutePath()
			+ ".\nUsing default path: "
			+ oldFilesLocation.getAbsolutePath());
		filesLocation = oldFilesLocation;
		correctLocation = false;
	    }

	    nodeList = rootEle
		    .getElementsByTagName(AJProperties.RAW_REPORTS_FOLDER);
	    elem = (Element) nodeList.item(0);
	    testXMLElement(nodeList, elem);
	    rawReportsFolder = elem.getFirstChild().getNodeValue();
	    log.debug(AJProperties.RAW_REPORTS_FOLDER + ":" + rawReportsFolder);

	    nodeList = rootEle
		    .getElementsByTagName(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE);
	    elem = (Element) nodeList.item(0);
	    latexReportsFolderByDate = elem.getFirstChild().getNodeValue();
	    log.debug(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE + ":"
		    + latexReportsFolderByDate);

	    nodeList = rootEle
		    .getElementsByTagName(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET);
	    elem = (Element) nodeList.item(0);
	    testXMLElement(nodeList, elem);
	    latexReportsFolderByTarget = elem.getFirstChild().getNodeValue();
	    log.debug(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET + ":"
		    + latexReportsFolderByTarget);

	    nodeList = rootEle
		    .getElementsByTagName(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION);
	    elem = (Element) nodeList.item(0);
	    testXMLElement(nodeList, elem);
	    latexReportsFolderByConstellation = elem.getFirstChild()
		    .getNodeValue();
	    log.debug(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION + ":"
		    + latexReportsFolderByConstellation);

	    nodeList = rootEle
		    .getElementsByTagName(AJProperties.SGL_REPORTS_FOLDER_BY_DATE);
	    elem = (Element) nodeList.item(0);
	    testXMLElement(nodeList, elem);
	    sglReportsFolderByDate = elem.getFirstChild().getNodeValue();
	    log.debug(AJProperties.SGL_REPORTS_FOLDER_BY_DATE + ":"
		    + sglReportsFolderByDate);

	    log.debug("Configuration file " + configFile.getAbsolutePath()
		    + " loaded");
	    if (!correctLocation) {
		saveConfiguration();
	    }
	} catch (ParserConfigurationException e) {
	    log.debug(e, e);
	    log.error("The configuration file "
		    + configFile.getAbsolutePath()
		    + " was not read correctly. A new configuration file will be generated.");
	    saveConfiguration();
	} catch (SAXException e) {
	    log.debug(e, e);
	    log.error("The configuration file "
		    + configFile.getAbsolutePath()
		    + " was not read correctly. A new configuration file will be generated.");
	    saveConfiguration();
	} catch (IOException e) {
	    log.debug(e, e);
	    log.error("The configuration file "
		    + configFile.getAbsolutePath()
		    + " was not read correctly. A new configuration file will be generated.");
	    saveConfiguration();
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
	    Element rootElem = dom.createElement(AJConstants.APPLICATION_NAME);
	    dom.appendChild(rootElem);

	    log.debug("Create elements and node and attach it to root astrojournal");
	    Element elem;
	    Text value;
	    elem = dom.createElement(AJProperties.QUIET);
	    value = dom.createTextNode(String.valueOf(quiet));
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement(AJProperties.SHOW_LATEX_OUTPUT);
	    value = dom.createTextNode(String.valueOf(showLatexOutput));
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement(AJProperties.SHOW_LICENSE_AT_START);
	    value = dom.createTextNode(String.valueOf(showLicenseAtStart));
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom
		    .createElement(AJProperties.SHOW_PDFLATEX_VERSION_AT_START);
	    value = dom.createTextNode(String
		    .valueOf(showPDFLatexVersionAtStart));
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement(AJProperties.SHOW_CONFIGURATION_AT_START);
	    value = dom
		    .createTextNode(String.valueOf(showConfigurationAtStart));
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement(AJProperties.FILES_LOCATION);
	    value = dom.createTextNode(filesLocation.getAbsolutePath());
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement(AJProperties.RAW_REPORTS_FOLDER);
	    value = dom.createTextNode(rawReportsFolder);
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE);
	    value = dom.createTextNode(latexReportsFolderByDate);
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom
		    .createElement(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET);
	    value = dom.createTextNode(latexReportsFolderByTarget);
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom
		    .createElement(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION);
	    value = dom.createTextNode(latexReportsFolderByConstellation);
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement(AJProperties.SGL_REPORTS_FOLDER_BY_DATE);
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
     * Load the Java System Properties for AstroJournal dynamically.
     */
    public void loadAJProperties() {

	// Quiet
	if (System.getProperty(AJProperties.QUIET) != null) {
	    log.debug("Setting AJ Property QUIET="
		    + System.getProperty(AJProperties.QUIET));
	    if (System.getProperty(AJProperties.QUIET).equals("true")) {
		quiet = true;
	    } else {
		quiet = false;
	    }
	}

	// Latex output
	if (System.getProperty(AJProperties.SHOW_LATEX_OUTPUT) != null) {
	    log.debug("Setting AJ Property LATEX_OUTPUT="
		    + System.getProperty(AJProperties.SHOW_LATEX_OUTPUT));
	    if (System.getProperty(AJProperties.SHOW_LATEX_OUTPUT).equals(
		    "true")) {
		showLatexOutput = true;
	    } else {
		showLatexOutput = false;
	    }
	}

	// Show license at start
	if (System.getProperty(AJProperties.SHOW_LICENSE_AT_START) != null) {
	    log.debug("Setting AJ Property SHOW_LICENSE_AT_START="
		    + System.getProperty(AJProperties.SHOW_LICENSE_AT_START));
	    if (System.getProperty(AJProperties.SHOW_LICENSE_AT_START).equals(
		    "true")) {
		showLicenseAtStart = true;
	    } else {
		showLicenseAtStart = false;
	    }
	}

	// Show the pdflatex version at start
	if (System.getProperty(AJProperties.SHOW_PDFLATEX_VERSION_AT_START) != null) {
	    log.debug("Setting AJ Property SHOW_PDFLATEX_VERSION="
		    + System.getProperty(AJProperties.SHOW_PDFLATEX_VERSION_AT_START));
	    if (System.getProperty(AJProperties.SHOW_PDFLATEX_VERSION_AT_START)
		    .equals("true")) {
		showPDFLatexVersionAtStart = true;
	    } else {
		showPDFLatexVersionAtStart = false;
	    }
	}

	// Show configuration at start
	if (System.getProperty(AJProperties.SHOW_CONFIGURATION_AT_START) != null) {
	    log.debug("Setting AJ Property SHOW_CONFIGURATION_AT_START="
		    + System.getProperty(AJProperties.SHOW_CONFIGURATION_AT_START));
	    if (System.getProperty(AJProperties.SHOW_CONFIGURATION_AT_START)
		    .equals("true")) {
		showConfigurationAtStart = true;
	    } else {
		showConfigurationAtStart = false;
	    }
	}

	// AJ files location
	if (System.getProperty(AJProperties.FILES_LOCATION) != null) {
	    log.debug("Setting AJ Property FILES_LOCATION="
		    + System.getProperty(AJProperties.FILES_LOCATION));
	    File newFilesLocation = new File(
		    System.getProperty(AJProperties.FILES_LOCATION));
	    if (!(newFilesLocation != null && newFilesLocation.exists() && newFilesLocation
		    .canWrite())) {
		log.error("The location for storing AJ Files set as Java Property does not exist or is not writeable.\n"
			+ ".\nUsing previous path: "
			+ getFilesLocation().getAbsolutePath());
	    } else {
		filesLocation = newFilesLocation;
	    }
	}

	// Raw reports folder
	if (System.getProperty(AJProperties.RAW_REPORTS_FOLDER) != null) {
	    log.debug("Setting AJ Property RAW_REPORTS_FOLDER="
		    + System.getProperty(AJProperties.RAW_REPORTS_FOLDER));
	    rawReportsFolder = System
		    .getProperty(AJProperties.RAW_REPORTS_FOLDER);
	}

	// Latex reports folder by date
	if (System.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE) != null) {
	    log.debug("Setting AJ Property LATEX_REPORTS_FOLDER_BY_DATE="
		    + System.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE));
	    latexReportsFolderByDate = System
		    .getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_DATE);
	}

	// Latex reports folder by target
	if (System.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET) != null) {
	    log.debug("Setting AJ Property LATEX_REPORTS_FOLDER_BY_TARGET="
		    + System.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET));
	    latexReportsFolderByTarget = System
		    .getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_TARGET);
	}

	// Latex reports folder by constellation
	if (System
		.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION) != null) {
	    log.debug("Setting AJ Property LATEX_REPORTS_FOLDER_BY_CONSTELLATION="
		    + System.getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION));
	    latexReportsFolderByConstellation = System
		    .getProperty(AJProperties.LATEX_REPORTS_FOLDER_BY_CONSTELLATION);
	}

	// SGL reports folder by date
	if (System.getProperty(AJProperties.SGL_REPORTS_FOLDER_BY_DATE) != null) {
	    log.debug("Setting AJ Property SGL_REPORTS_FOLDER_BY_DATE="
		    + System.getProperty(AJProperties.SGL_REPORTS_FOLDER_BY_DATE));
	    sglReportsFolderByDate = System
		    .getProperty(AJProperties.SGL_REPORTS_FOLDER_BY_DATE);
	}

	// The locale
	if (System.getProperty(AJProperties.LOCALE) != null) {
	    log.debug("Setting AJ Property LOCALE="
		    + System.getProperty(AJProperties.LOCALE));
	    ResourceBundle newLocale = null;
	    try {
		newLocale = ResourceBundle.getBundle(System
			.getProperty(AJProperties.LOCALE));
	    } catch (Exception e) {
		log.error("The locale : "
			+ System.getProperty(AJProperties.LOCALE)
			+ " does not exist. Using previous Locale.");
		newLocale = null;
	    }
	    if (newLocale != null) {
		localeBundle = newLocale;
	    }
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
     * Prepare input and output folders for AstroJournal if these do not exist.
     */
    public void prepareAJFolders() {
	adjustFileSeparator();
	// Create the folders if these do not exist.

	filesLocation.mkdir();

	// AJ header footer folder
	File ajHeaderFooterDir = new File(
		AJConstants.LATEX_HEADER_FOOTER_FOLDER);
	ajHeaderFooterDir.mkdir();
	// Create a local folder for header_footer and copy the content from
	// the AJ folder to here
	File userHeaderFooterDir = new File(filesLocation.getAbsolutePath()
		+ File.separator + AJConstants.LATEX_HEADER_FOOTER_FOLDER);

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
		log.error(localeBundle
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
	File userRawReportsDir = new File(filesLocation.getAbsolutePath()
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
		log.error(localeBundle
			.getString("AJ.errCannotCopyRawReportsFolder.text"), e);
	    }
	}

	new File(filesLocation.getAbsolutePath() + File.separator
		+ latexReportsFolderByDate).mkdir();
	new File(filesLocation.getAbsolutePath() + File.separator
		+ latexReportsFolderByTarget).mkdir();
	new File(filesLocation.getAbsolutePath() + File.separator
		+ latexReportsFolderByConstellation).mkdir();
	new File(filesLocation.getAbsolutePath() + File.separator
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
	    if (!(filesLocation.exists() && filesLocation.canWrite())) {
		throw new FileNotFoundException();
	    }
	    FileUtils.cleanDirectory(new File(filesLocation.getAbsolutePath()
		    + File.separator + latexReportsFolderByDate));
	    FileUtils.cleanDirectory(new File(filesLocation.getAbsolutePath()
		    + File.separator + latexReportsFolderByTarget));
	    FileUtils.cleanDirectory(new File(filesLocation.getAbsolutePath()
		    + File.separator + latexReportsFolderByConstellation));
	    FileUtils.cleanDirectory(new File(filesLocation.getAbsolutePath()
		    + File.separator + sglReportsFolderByDate));
	} catch (IOException e) {
	    throw e;
	}
    }

    /**
     * Create a string containing the license for AstroJournal.
     * 
     * @return a string
     */
    public String printLicense() {
	String license = AJConstants.APPLICATION_NAME
		+ " "
		+ AJConstants.APPLICATION_VERSION
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
	    sb.append(localeBundle
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
		    + localeBundle
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
	String configuration = "AstroJournal current configuration:\n" + "\t"
		+ localeBundle.getString("AJ.lblAJFilesLocation.text") + " "
		+ filesLocation.getAbsolutePath() + "\n\t"
		+ localeBundle.getString("AJ.lblInpDir.text") + " "
		+ rawReportsFolder + "\n\t"
		+ localeBundle.getString("AJ.lblOutByDateDir.text") + " "
		+ latexReportsFolderByDate + "\n\t"
		+ localeBundle.getString("AJ.lblOutByTargetDir.text") + " "
		+ latexReportsFolderByTarget + "\n\t"
		+ localeBundle.getString("AJ.lblOutByConstellationDir.text")
		+ " " + latexReportsFolderByConstellation + "\n\t"
		+ localeBundle.getString("AJ.lblSGLOutByDateDir.text") + " "
		+ sglReportsFolderByDate + "\n\t"
		+ localeBundle.getString("AJ.lblQuiet.text") + " " + quiet
		+ "\n\t" + localeBundle.getString("AJ.lblShowLatexOutput.text")
		+ " " + showLatexOutput + "\n\t"
		+ localeBundle.getString("AJ.lblShowLicenseAtStart.text") + " "
		+ showLicenseAtStart + "\n\t"
		+ localeBundle.getString("AJ.lblShowPDFLatexVersion.text")
		+ " " + showPDFLatexVersionAtStart + "\n\t"
		+ localeBundle.getString("AJ.lblShowConfigurationAtStart.text")
		+ " " + showConfigurationAtStart + "\n" + "\n\n";
	return configuration;
    }

    /**
     * @return the configFileName
     */
    public String getConfigFileName() {
	return configFileName;
    }

    /**
     * @return the localeBundle
     */
    public ResourceBundle getLocaleBundle() {
	return localeBundle;
    }

    /**
     * @return the quiet
     */
    public boolean isQuiet() {
	return quiet;
    }

    /**
     * @return the showLatexOutput
     */
    public boolean isShowLatexOutput() {
	return showLatexOutput;
    }

    /**
     * @return the showConfigurationAtStart
     */
    public boolean isShowConfigurationAtStart() {
	return showConfigurationAtStart;
    }

    /**
     * @return the showLicenseAtStart
     */
    public boolean isShowLicenseAtStart() {
	return showLicenseAtStart;
    }

    /**
     * @return the showPDFLatexVersionAtStart
     */
    public boolean isShowPDFLatexVersionAtStart() {
	return showPDFLatexVersionAtStart;
    }

    /**
     * Return the file containing all input and output files.
     * 
     * @return the AstroJournal Files Location.
     */
    public File getFilesLocation() {
	return filesLocation;
    }

    /**
     * @return the rawReportsFolder
     */
    public String getRawReportsFolder() {
	return rawReportsFolder;
    }

    /**
     * @return the latexReportsFolderByDate
     */
    public String getLatexReportsFolderByDate() {
	return latexReportsFolderByDate;
    }

    /**
     * @return the latexReportsFolderByTarget
     */
    public String getLatexReportsFolderByTarget() {
	return latexReportsFolderByTarget;
    }

    /**
     * @return the latexReportsFolderByConstellation
     */
    public String getLatexReportsFolderByConstellation() {
	return latexReportsFolderByConstellation;
    }

    /**
     * @return the sglReportsFolderByDate
     */
    public String getSglReportsFolderByDate() {
	return sglReportsFolderByDate;
    }

}