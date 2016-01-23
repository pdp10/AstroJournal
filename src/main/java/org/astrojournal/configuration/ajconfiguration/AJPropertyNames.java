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
 * @date 17 Jan 2016
 */
public enum AJPropertyNames {

    // LOCALE
    /**
     * The locale for the internationalisation.
     */
    LOCALE("aj.locale"),

    // FLAGS
    /** True if the application should run quietly */
    QUIET("aj.quiet"),
    /** True if latex output should be printed. */
    SHOW_LATEX_OUTPUT("aj.show_latex_output"),
    /** True if the license should be shown at start. */
    SHOW_LICENSE_AT_START("aj.show_license_at_start"),
    /** True if the version of pdflatex. */
    SHOW_PDFLATEX_VERSION_AT_START("aj.show_pdflatex_version_at_start"),
    /** True if the configuration should be shown at start. */
    SHOW_CONFIGURATION_AT_START("aj.show_configuration_at_start"),

    // MAIN LOCATION
    /** The absolute path containing AstroJournal input and output folders. */
    FILES_LOCATION("aj.files_location"),

    // INPUT FOLDER
    /** The relative path containing the raw files (observation input folder). */
    RAW_REPORTS_FOLDER("aj.raw_reports_folder"),

    // LATEX HEADER FOOTER
    /** The folder containing the latex header and footer. */
    LATEX_HEADER_FOOTER_FOLDER("aj.latex_header_footer_folder"),

    // LATEX REPORT BY DATE
    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    LATEX_REPORTS_FOLDER_BY_DATE("aj.latex_reports_folder_by_date"),

    /** The name of the main Latex file sorted by date. */
    LATEX_REPORT_BY_DATE_FILENAME("aj.latex_report_by_date_filename"),

    /** The Latex header with path for astrojournal by date. */
    LATEX_HEADER_BY_DATE_FILENAME("aj.latex_header_by_date_filename"),

    /** The Latex footer with path for astrojournal by date. */
    LATEX_FOOTER_BY_DATE_FILENAME("aj.latex_footer_by_date_filename"),

    // LATEX REPORT BY TARGET
    /**
     * The name of the folder containing the latex observation files by target
     * (observation output folder).
     */
    LATEX_REPORTS_FOLDER_BY_TARGET("aj.latex_reports_folder_by_target"),

    /** The name of the main Latex file sorted by target. */
    LATEX_REPORT_BY_TARGET_FILENAME("aj.latex_report_by_target_filename"),

    /** The Latex header with path for astrojournal by target. */
    LATEX_HEADER_BY_TARGET_FILENAME("aj.latex_header_by_target_filename"),

    /** The Latex footer with path for astrojournal by target. */
    LATEX_FOOTER_BY_TARGET_FILENAME("aj.latex_footer_by_target_filename"),

    // LATEX REPORT BY CONSTELLATION
    /**
     * The name of the folder containing the latex observation files by
     * constellation (observation output folder).
     */
    LATEX_REPORTS_FOLDER_BY_CONSTELLATION(
	    "aj.latex_reports_folder_by_constellation"),

    /** The name of the main Latex file sorted by constellation. */
    LATEX_REPORT_BY_CONSTELLATION_FILENAME(
	    "aj.latex_report_by_constellation_filename"),

    /** The Latex header with path for astrojournal by constellation. */
    LATEX_HEADER_BY_CONSTELLATION_FILENAME(
	    "aj.latex_header_by_constellation_filename"),

    /** The Latex footer with path for astrojournal by constellation. */
    LATEX_FOOTER_BY_CONSTELLATION_FILENAME(
	    "aj.latex_footer_by_constellation_filename"),

    // SGL REPORT BY DATE
    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    SGL_REPORTS_FOLDER_BY_DATE("aj.sgl_reports_folder_by_date"),

    /** The name of the SGL main file sorted by date. */
    SGL_REPORT_BY_DATE_FILENAME("aj.sgl_report_by_date_filename"),

    ;

    /** The property key name. */
    private final String name;

    /**
     * Constructor.
     *
     * @param key
     *            the key name
     */
    private AJPropertyNames(String key) {
	this.name = key;
    }

    @Override
    public String toString() {
	return this.name;
    }

    /**
     * Return the property key name
     * 
     * @return the property key name
     */
    public String getName() {
	return this.name;
    }

}
