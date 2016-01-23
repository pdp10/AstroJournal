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
 * The set of AstroJournal constants which do not change.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 17 Jan 2016
 */
public class AJConstants {

    /**
     * The default application configuration file name. This is in resources/.
     */
    public static final String DEFAULT_CONFIGURATION_PROPERTIES_FILE_NAME = "application_properties.xml";

    /** The user configuration file name. */
    public static final String USER_CONFIGURATION_PROPERTIES_FILE_NAME = "astrojournal.xml";

    /** The comment to be inserted in the user configuration file. */
    public static final String USER_CONFIGURATION_PROPERTIES_FILE_COMMENT = "User setting for the application Config. Edit at your own risk.";

    /** The short text for the application license. */
    public static final String SHORT_LICENSE = AppMetaInfo.NAME
	    + " "
	    + AppMetaInfo.VERSION
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
