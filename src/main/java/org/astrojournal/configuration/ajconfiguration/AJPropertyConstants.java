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
package org.astrojournal.configuration.ajconfiguration;

/**
 * The set of Java Properties for AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public enum AJPropertyConstants {

    // LOCALE
    /**
     * The locale for the internationalisation.
     */
    LOCALE("aj.locale", "en_GB"),

    // FLAGS
    /** True if the application should run quietly */
    QUIET("aj.quiet", "false"),
    /** True if latex output should be printed. */
    SHOW_LATEX_OUTPUT("aj.show_latex_output", "false"),
    /** True if the license should be shown at start. */
    SHOW_LICENSE_AT_START("aj.show_license_at_start", "true"),
    /** True if the version of pdflatex. */
    SHOW_PDFLATEX_VERSION_AT_START("aj.show_pdflatex_version_at_start", "true"),
    /** True if the configuration should be shown at start. */
    SHOW_CONFIGURATION_AT_START("aj.show_configuration_at_start", "true"),

    // MAIN LOCATION
    /** The absolute path containing AstroJournal input and output folders. */
    FILES_LOCATION("aj.files_location", "AstroJournal_files"),

    // GENERATOR
    /** The generator name. */
    GENERATOR_NAME("aj.generator", "extgen"),

    // INPUT FOLDER
    /** The relative path containing the raw files (observation input folder). */
    RAW_REPORTS_FOLDER("aj.raw_reports_folder", "raw_reports"),

    // LATEX HEADER FOOTER
    /** The folder containing the latex header and footer. */
    LATEX_HEADER_FOOTER_FOLDER("aj.latex_header_footer_folder",
	    "latex_header_footer"),

    // LATEX REPORT BY DATE
    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    LATEX_REPORTS_FOLDER_BY_DATE("aj.latex_reports_folder_by_date",
	    "latex_reports_by_date"),

    /** The name of the main Latex file sorted by date. */
    LATEX_REPORT_BY_DATE_FILENAME("aj.latex_report_by_date_filename",
	    "astrojournal_by_date.tex"),

    /** The Latex header with path for astrojournal by date. */
    LATEX_HEADER_BY_DATE_FILENAME("aj.latex_header_by_date_filename",
	    "header_by_date.tex"),

    /** The Latex footer with path for astrojournal by date. */
    LATEX_FOOTER_BY_DATE_FILENAME("aj.latex_footer_by_date_filename",
	    "footer_by_date.tex"),

    // LATEX REPORT BY TARGET
    /**
     * The name of the folder containing the latex observation files by target
     * (observation output folder).
     */
    LATEX_REPORTS_FOLDER_BY_TARGET("aj.latex_reports_folder_by_target",
	    "latex_reports_by_target"),

    /** The name of the main Latex file sorted by target. */
    LATEX_REPORT_BY_TARGET_FILENAME("aj.latex_report_by_target_filename",
	    "astrojournal_by_target.tex"),

    /** The Latex header with path for astrojournal by target. */
    LATEX_HEADER_BY_TARGET_FILENAME("aj.latex_header_by_target_filename",
	    "header_by_target.tex"),

    /** The Latex footer with path for astrojournal by target. */
    LATEX_FOOTER_BY_TARGET_FILENAME("aj.latex_footer_by_target_filename",
	    "footer_by_target.tex"),

    // LATEX REPORT BY CONSTELLATION
    /**
     * The name of the folder containing the latex observation files by
     * constellation (observation output folder).
     */
    LATEX_REPORTS_FOLDER_BY_CONSTELLATION(
	    "aj.latex_reports_folder_by_constellation",
	    "latex_reports_by_constellation"),

    /** The name of the main Latex file sorted by constellation. */
    LATEX_REPORT_BY_CONSTELLATION_FILENAME(
	    "aj.latex_report_by_constellation_filename",
	    "astrojournal_by_constellation.tex"),

    /** The Latex header with path for astrojournal by constellation. */
    LATEX_HEADER_BY_CONSTELLATION_FILENAME(
	    "aj.latex_header_by_constellation_filename",
	    "header_by_constellation.tex"),

    /** The Latex footer with path for astrojournal by constellation. */
    LATEX_FOOTER_BY_CONSTELLATION_FILENAME(
	    "aj.latex_footer_by_constellation_filename",
	    "footer_by_constellation.tex"),

    // TXT REPORT BY DATE
    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    TXT_REPORTS_FOLDER_BY_DATE("aj.txt_reports_folder_by_date",
	    "txt_reports_by_date"),

    /** The name of the TXT main file sorted by date. */
    TXT_REPORT_BY_DATE_FILENAME("aj.txt_report_by_date_filename",
	    "astrojournal_by_date.txt"),

    ;

    /** The property key name. */
    private final String key;

    /** The property value. */
    private final String value;

    /**
     * Constructor.
     *
     * @param key
     *            the key name
     * @param value
     *            the value
     */
    private AJPropertyConstants(String key, String value) {
	this.key = key;
	this.value = value;
    }

    /**
     * Return the property key
     * 
     * @return the property key
     */
    public String getKey() {
	return this.key;
    }

    /**
     * Return the property value
     * 
     * @return the property value
     */
    public String getValue() {
	return this.value;
    }

    /**
     * Return a string containing a list of properties.
     * 
     * @return the list or properties.
     */
    public static String printAllProperties() {
	AJPropertyConstants[] properties = values();
	StringBuilder sb = new StringBuilder();
	for (AJPropertyConstants property : properties) {
	    sb.append(property.getKey() + "=" + property.getValue() + "\n");
	}
	return sb.toString();
    }

}
