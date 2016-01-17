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

/**
 * The set of Java Properties for AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 17 Jan 2016
 */
public final class AJProperties {

    /** True if the application should run quietly */
    public static final String QUIET = "aj.quiet";

    /** True if latex output should be printed. */
    public static final String SHOW_LATEX_OUTPUT = "aj.show_latex_output";

    /** True if the license should be shown at start. */
    public static final String SHOW_LICENSE_AT_START = "aj.show_license_at_start";

    /** True if the version of pdflatex. */
    public static final String SHOW_PDFLATEX_VERSION_AT_START = "aj.show_pdflatex_version_at_start";

    /** True if the configuration should be shown at start. */
    public static final String SHOW_CONFIGURATION_AT_START = "aj.show_configuration_at_start";

    /** The absolute path containing AstroJournal input and output folders. */
    public static final String FILES_LOCATION = "aj.files_location";

    /** The relative path containing the raw files (observation input folder). */
    public static final String RAW_REPORTS_FOLDER = "aj.raw_reports_folder";

    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    public static final String LATEX_REPORTS_FOLDER_BY_DATE = "aj.latex_reports_folder_by_date";

    /**
     * The name of the folder containing the latex observation files by target
     * (observation output folder).
     */
    public static final String LATEX_REPORTS_FOLDER_BY_TARGET = "aj.latex_reports_folder_by_target";

    /**
     * The name of the folder containing the latex observation files by
     * constellation (observation output folder).
     */
    public static final String LATEX_REPORTS_FOLDER_BY_CONSTELLATION = "aj.latex_reports_folder_by_constellation";

    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    public static final String SGL_REPORTS_FOLDER_BY_DATE = "aj.sgl_reports_folder_by_date";

    /**
     * The locale for the internationalisation.
     */
    public static final String LOCALE = "aj.locale";
}
