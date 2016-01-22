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

import java.io.File;

/**
 * The set of AstroJournal constants which do not change.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 17 Jan 2016
 */
public class AJConstants {

    /** The AJ application name. */
    public static final String APPLICATION_NAME = "AstroJournal";

    /** The AJ application version. */
    public static final String APPLICATION_VERSION = "v1.1.0";

    /** The AJ website. */
    public static final String APPLICATION_WEBSITE = "http://pdp10.github.io/AstroJournal";

    /**
     * The default application configuration file name. This is in resources/.
     */
    public static final String DEFAULT_CONFIGURATION_PROPERTIES_FILE_NAME = "application_properties.xml";

    /** The user configuration file name. */
    public static final String USER_CONFIGURATION_PROPERTIES_FILE_NAME = "astrojournal.xml";

    /** The comment to be inserted in the user configuration file. */
    public static final String USER_CONFIGURATION_PROPERTIES_FILE_COMMENT = "User setting for the application Config. Edit at your own risk.";

    /** The name of the main Latex file sorted by date. */
    public static final String REPORT_BY_DATE_FILENAME = "astrojournal_by_date.tex";

    /** The name of the main Latex file sorted by target. */
    public static final String REPORT_BY_TARGET_FILENAME = "astrojournal_by_target.tex";

    /** The name of the main Latex file sorted by constellation. */
    public static final String REPORT_BY_CONSTELLATION_FILENAME = "astrojournal_by_constellation.tex";

    /** The name of the SGL main file sorted by date. */
    public static final String SGL_REPORT_BY_DATE_FILENAME = "astrojournal_by_date_sgl.txt";

    /** The folder containing the latex header and footer. */
    public static final String LATEX_HEADER_FOOTER_FOLDER = "latex_header_footer";

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

    /** The short text for the application license. */
    public static final String SHORT_LICENSE = AJConstants.APPLICATION_NAME
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

}
