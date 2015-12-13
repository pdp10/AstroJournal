/*
 * This file is part of AstroJournal.
 * AstroJournal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * AstroJournal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with AstroJournal. If not, see <http://www.gnu.org/licenses/>.
 */
package org.astrojournal.configuration;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * A simple class for configuring AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 12 Dec 2015
 */
public class AJConfig {

    /** The AJConfig instance to be used. */
    private static AJConfig instance = new AJConfig();

    // THESE PARAMETERS ARE NOT CONFIGURABLE
    /** The AJ application name. */
    public static final String APPLICATION_NAME = "AstroJournal";
    /** The AJ application version. */
    public static final String APPLICATION_VERSION = "v0.9";

    /** The name of the main Latex file sorted by date. */
    public static final String REPORT_BY_DATE_FILENAME = "astrojournal_by_date.tex";
    /** The name of the main Latex file sorted by target. */
    public static final String REPORT_BY_TARGET_FILENAME = "astrojournal_by_target.tex";
    /** The name of the main Latex file sorted by constellation. */
    public static final String REPORT_BY_CONSTELLATION_FILENAME = "astrojournal_by_constellation.tex";
    /** The name of the SGL main file sorted by date. */
    public static final String SGL_REPORT_BY_DATE_FILENAME = "astrojournal_by_date_sgl.txt";

    private static final String LATEX_HEADER_FOOTER_FOLDER = "latex_header_footer";
    /** The Latex header with path for astrojournal by date. */
    public static final String HEADER_BY_DATE_FILENAME = LATEX_HEADER_FOOTER_FOLDER
	    + "/header_by_date.tex";
    /** The Latex footer with path for astrojournal by date. */
    public static final String FOOTER_BY_DATE_FILENAME = LATEX_HEADER_FOOTER_FOLDER
	    + "/footer_by_date.tex";
    /** The Latex header with path for astrojournal by target. */
    public static final String HEADER_BY_TARGET_FILENAME = LATEX_HEADER_FOOTER_FOLDER
	    + "/header_by_target.tex";
    /** The Latex footer with path for astrojournal by target. */
    public static final String FOOTER_BY_TARGET_FILENAME = LATEX_HEADER_FOOTER_FOLDER
	    + "/footer_by_target.tex";
    /** The Latex header with path for astrojournal by constellation. */
    public static final String HEADER_BY_CONSTELLATION_FILENAME = LATEX_HEADER_FOOTER_FOLDER
	    + "/header_by_constellation.tex";
    /** The Latex footer with path for astrojournal by constellation. */
    public static final String FOOTER_BY_CONSTELLATION_FILENAME = LATEX_HEADER_FOOTER_FOLDER
	    + "/footer_by_constellation.tex";

    // THESE PARAMETERS ARE CONFIGURABLE
    // All AstroJournal classes can only read these values. The only classes
    // which can edit these parameters must be positioned in the configuration
    // package. The setters have therefore "package visibility".
    /** True if latex output should be printed. */
    private boolean latexOutput = false;
    /** True if the application should run quietly */
    private boolean quiet = false;
    /** True if the version should be shown. */
    private boolean showVersion = true;

    /** The relative path containing the raw files (observation input folder). */
    private String rawReportsFolder = "raw_reports/";
    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    private String latexReportsFolderByDate = "latex_reports_by_date/";
    /**
     * The name of the folder containing the latex observation files by target
     * (observation output folder).
     */
    private String latexReportsFolderByTarget = "latex_reports_by_target/";
    /**
     * The name of the folder containing the latex observation files by
     * constellation (observation output folder).
     */
    private String latexReportsFolderByConstellation = "latex_reports_by_constellation/";
    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    private String sglReportsFolderByDate = "sgl_reports_by_date/";

    /**
     * Private constructor for creating only one instance of AJConfig.
     */
    private AJConfig() {

	// Show version
	if (System.getProperty("aj.show_version") != null
		&& System.getProperty("aj.show_version").equals("true")) {
	    showVersion = true;
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

	File headerFooter = new File(LATEX_HEADER_FOOTER_FOLDER);
	File inp = new File(rawReportsFolder);
	File out1 = new File(latexReportsFolderByDate);
	File out2 = new File(latexReportsFolderByTarget);
	File out3 = new File(latexReportsFolderByConstellation);
	File out4 = new File(sglReportsFolderByDate);
	// Create the folders if these do not exist.
	headerFooter.mkdir();
	inp.mkdir();
	out1.mkdir();
	out2.mkdir();
	out3.mkdir();
	out4.mkdir();

	// Delete previous content if this was present.
	try {
	    FileUtils.cleanDirectory(out1);
	    FileUtils.cleanDirectory(out2);
	    FileUtils.cleanDirectory(out3);
	    FileUtils.cleanDirectory(out4);
	} catch (IOException e) {
	    System.out
		    .println("Impossible to clean the output directories. Does the program have right permission?");
	    e.printStackTrace();
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
     * Create a string containing details for AstroJournal.
     * 
     * @return a string
     */
    public String printVersion() {
	String version = APPLICATION_NAME
		+ " "
		+ APPLICATION_VERSION
		+ " is free software: you can redistribute it and/or modify \n"
		+ "it under the terms of the GNU General Public License as published by \n"
		+ "the Free Software Foundation, either version 3 of the License, or \n"
		+ "(at your option) any later version. \n"
		+ "AstroJournal is distributed in the hope that it will be useful, \n"
		+ "but WITHOUT ANY WARRANTY; without even the implied warranty of \n"
		+ "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the \n"
		+ "GNU General Public License for more details. \n"
		+ "You should have received a copy of the GNU General Public License \n"
		+ "along with AstroJournal. If not, see <http://www.gnu.org/licenses/>. \n"
		+ "\n"
		+ "AstroJournal Web Site: <https://github.com/pdp10/AstroJournal>\n\n"
		+ "\n" + printConfiguration();
	return version;
    }

    /**
     * print the current configuration.
     * 
     * @return the current configuration
     */
    public String printConfiguration() {
	String configuration = "AstroJournal is running with the following configuration:\n"
		+ "raw_reports_folder: "
		+ rawReportsFolder
		+ "\n"
		+ "latex_reports_by_date: "
		+ latexReportsFolderByDate
		+ "\n"
		+ "latex_reports_by_target: "
		+ latexReportsFolderByTarget
		+ "\n"
		+ "latex_reports_by_constellation: "
		+ latexReportsFolderByConstellation
		+ "\n"
		+ "sgl_reports_by_date: " + sglReportsFolderByDate + "\n\n";
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
     * @return the showVersion
     */
    public boolean isShowVersion() {
	return showVersion;
    }

    /**
     * @param showVersion
     *            the showVersion to set
     */
    void setShowVersion(boolean showVersion) {
	this.showVersion = showVersion;
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