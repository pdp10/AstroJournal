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

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

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
    public static final String APPLICATION_VERSION = "v0.10.12";

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

    // THESE PARAMETERS ARE CONFIGURABLE
    // All AstroJournal classes can only read these values. The only classes
    // which can edit these parameters must be positioned in the configuration
    // package. The setters have therefore "package visibility".
    /** True if latex output should be printed. */
    private boolean latexOutput = false;

    /** True if the application should run quietly */
    private boolean quiet = false;

    /** True if the license should be shown at start. */
    private boolean showLicenseAtStart = true;

    /** True if the configuration should be shown at start. */
    private boolean showConfigurationAtStart = true;

    // NOTE: These field MUST NOT have a file separator because Latex uses '/'
    // by default.
    /** The absolute path containing AstroJournal input and output folders. */
    private File ajFilesLocation;

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

	ajFilesLocation = new File(System.getProperty("user.home")
		+ File.separator + "AstroJournal_files");

	if (configFile != null && configFile.exists()) {
	    loadConfiguration();
	} else {
	    try {
		saveConfiguration();
	    } catch (IOException e) {
		log.error(e);
	    }
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
	    log.debug("Get an instance of document builder");
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    log.debug("Parse using builder to get DOM representation of the XML file");
	    Document dom = db.parse(configFile);

	    log.debug("Get the root element");
	    Element rootEle = dom.getDocumentElement();
	    log.debug("Retrieve elements by node list");
	    NodeList nodeList;
	    Element elem;

	    nodeList = rootEle.getElementsByTagName("latex_output");
	    elem = (Element) nodeList.item(0);
	    latexOutput = Boolean.parseBoolean(elem.getFirstChild()
		    .getNodeValue());
	    log.debug("latex_output:" + latexOutput);

	    nodeList = rootEle.getElementsByTagName("quiet");
	    elem = (Element) nodeList.item(0);
	    quiet = Boolean.parseBoolean(elem.getFirstChild().getNodeValue());
	    log.debug("quiet:" + quiet);

	    nodeList = rootEle
		    .getElementsByTagName("show_configuration_at_start");
	    elem = (Element) nodeList.item(0);
	    showConfigurationAtStart = Boolean.parseBoolean(elem
		    .getFirstChild().getNodeValue());
	    log.debug("show_configuration_at_start:" + showConfigurationAtStart);

	    nodeList = rootEle.getElementsByTagName("show_license_at_start");
	    elem = (Element) nodeList.item(0);
	    showLicenseAtStart = Boolean.parseBoolean(elem.getFirstChild()
		    .getNodeValue());
	    log.debug("show_license_at_start:" + showLicenseAtStart);

	    nodeList = rootEle.getElementsByTagName("aj_files_location");
	    elem = (Element) nodeList.item(0);
	    File oldAJFilesLocation = ajFilesLocation;
	    ajFilesLocation = new File(elem.getFirstChild().getNodeValue());
	    log.debug("aj_files_location:" + ajFilesLocation.getAbsolutePath());
	    if (ajFilesLocation == null || !ajFilesLocation.exists()
		    || !ajFilesLocation.canWrite()) {
		ajFilesLocation = oldAJFilesLocation;
		log.warn("The location for storing AJ Files does not exist.\n"
			+ "Check Edit > Preference or "
			+ configFile.getAbsolutePath()
			+ ".\nUsing default path: "
			+ ajFilesLocation.getAbsolutePath());
		correctLocation = false;
	    }

	    nodeList = rootEle.getElementsByTagName("raw_reports_folder");
	    elem = (Element) nodeList.item(0);
	    rawReportsFolder = elem.getFirstChild().getNodeValue();
	    log.debug("raw_reports_folder:" + rawReportsFolder);

	    nodeList = rootEle
		    .getElementsByTagName("latex_reports_folder_by_date");
	    elem = (Element) nodeList.item(0);
	    latexReportsFolderByDate = elem.getFirstChild().getNodeValue();
	    log.debug("latex_reports_folder_by_date:"
		    + latexReportsFolderByDate);

	    nodeList = rootEle
		    .getElementsByTagName("latex_reports_folder_by_target");
	    elem = (Element) nodeList.item(0);
	    latexReportsFolderByTarget = elem.getFirstChild().getNodeValue();
	    log.debug("latex_reports_folder_by_target:"
		    + latexReportsFolderByTarget);

	    nodeList = rootEle
		    .getElementsByTagName("latex_reports_folder_by_constellation");
	    elem = (Element) nodeList.item(0);
	    latexReportsFolderByConstellation = elem.getFirstChild()
		    .getNodeValue();
	    log.debug("latex_reports_folder_by_constellation:"
		    + latexReportsFolderByConstellation);

	    nodeList = rootEle
		    .getElementsByTagName("sgl_reports_folder_by_date");
	    elem = (Element) nodeList.item(0);
	    sglReportsFolderByDate = elem.getFirstChild().getNodeValue();
	    log.debug("sgl_reports_folder_by_date:" + sglReportsFolderByDate);

	    log.debug("Configuration file " + configFile.getAbsolutePath()
		    + " loaded");
	    if (!correctLocation) {
		saveConfiguration();
	    }
	} catch (ParserConfigurationException e) {
	    log.error(e, e);
	} catch (SAXException e) {
	    log.error(e, e);
	} catch (IOException e) {
	    log.error(e, e);
	}
    }

    /**
     * Save the configuration file.
     * 
     * @throws IOException
     */
    public void saveConfiguration() throws IOException {
	// get an instance of factory
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	try {
	    log.debug("Saving configuration file "
		    + configFile.getAbsolutePath());
	    log.debug("Initialise DocumentBuilder and DOM");
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document dom = db.newDocument();

	    log.debug("Create the root element");
	    Element rootElem = dom.createElement("astrojournal");
	    dom.appendChild(rootElem);

	    log.debug("Create elements and node and attach it to root astrojournal");
	    Element elem;
	    Text value;
	    elem = dom.createElement("latex_output");
	    value = dom.createTextNode(String.valueOf(latexOutput));
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement("quiet");
	    value = dom.createTextNode(String.valueOf(quiet));
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement("show_configuration_at_start");
	    value = dom
		    .createTextNode(String.valueOf(showConfigurationAtStart));
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement("show_license_at_start");
	    value = dom
		    .createTextNode(String.valueOf(showConfigurationAtStart));
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement("aj_files_location");
	    value = dom.createTextNode(ajFilesLocation.getAbsolutePath());
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement("raw_reports_folder");
	    value = dom.createTextNode(rawReportsFolder);
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement("latex_reports_folder_by_date");
	    value = dom.createTextNode(latexReportsFolderByDate);
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement("latex_reports_folder_by_target");
	    value = dom.createTextNode(latexReportsFolderByTarget);
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement("latex_reports_folder_by_constellation");
	    value = dom.createTextNode(latexReportsFolderByConstellation);
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    elem = dom.createElement("sgl_reports_folder_by_date");
	    value = dom.createTextNode(sglReportsFolderByDate);
	    elem.appendChild(value);
	    rootElem.appendChild(elem);

	    log.debug("Serialize DOM to FileOutputStream to generate the xml file");
	    OutputFormat format = new OutputFormat(dom);
	    format.setIndenting(true);
	    XMLSerializer serializer = new XMLSerializer(new FileOutputStream(
		    configFile), format);
	    serializer.serialize(dom);
	    log.debug("Configuration file " + configFile.getAbsolutePath()
		    + " saved");
	} catch (ParserConfigurationException pce) {
	    log.error("Error while trying to instantiate DocumentBuilder "
		    + pce, pce);
	} catch (IOException ie) {
	    throw ie;
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
	// Show version
	if (System.getProperty("aj.show_version") != null
		&& System.getProperty("aj.show_version").equals("true")) {
	    showLicenseAtStart = true;
	}

	// Quiet
	if (System.getProperty("aj.quiet") != null
		&& System.getProperty("aj.quiet").equals("true")) {
	    quiet = true;
	}

	// Latex output
	if (System.getProperty("aj.latex_output") != null
		&& System.getProperty("aj.latex_output").equals("true")) {
	    latexOutput = true;
	}

	// AJ files location
	if (System.getProperty("aj.aj_files_location") != null) {
	    ajFilesLocation = new File(
		    System.getProperty("aj.aj_files_location"));
	    if (!(ajFilesLocation.exists() && ajFilesLocation.canWrite())) {
		throw new IllegalArgumentException("AJ File Location "
			+ ajFilesLocation
			+ " does not exist or is not writeable");
	    }
	}

	// Raw reports folder
	if (System.getProperty("aj.raw_reports_folder") != null) {
	    rawReportsFolder = new String(
		    System.getProperty("aj.raw_reports_folder"));
	}

	// Latex reports folder by date
	if (System.getProperty("aj.latex_reports_folder_by_date") != null) {
	    latexReportsFolderByDate = new String(
		    System.getProperty("aj.latex_reports_folder_by_date"));
	}

	// Latex reports folder by target
	if (System.getProperty("aj.latex_reports_folder_by_target") != null) {
	    latexReportsFolderByTarget = new String(
		    System.getProperty("aj.latex_reports_folder_by_target"));
	}

	// Latex reports folder by constellation
	if (System.getProperty("aj.latex_reports_folder_by_constellation") != null) {
	    latexReportsFolderByConstellation = new String(
		    System.getProperty("aj.latex_reports_folder_by_constellation"));
	}

	// SGL reports folder by date
	if (System.getProperty("aj.sgl_reports_folder_by_date") != null) {
	    sglReportsFolderByDate = new String(
		    System.getProperty("aj.sgl_reports_folder_by_date"));
	}

    }

    /**
     * Prepare input and output folders for AstroJournal if these do not exist.
     */
    public void prepareAJFolders() {
	adjustFileSeparator();
	// Create the folders if these do not exist.

	ajFilesLocation.mkdir();

	// AJ folder
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
	if (!userHeaderFooterDir.exists()
		|| userHeaderFooterDir.listFiles(latexFilter).length < 1) {
	    try {
		FileUtils.copyDirectory(ajHeaderFooterDir, userHeaderFooterDir,
			true);
	    } catch (IOException e) {
		log.error(AJConfig.BUNDLE
			.getString("AJ.errCannotCopyHeaderFooterFolder.text"));
		e.getStackTrace();
	    }
	}

	new File(ajFilesLocation.getAbsolutePath() + File.separator
		+ rawReportsFolder).mkdir();
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
     * print the current configuration.
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