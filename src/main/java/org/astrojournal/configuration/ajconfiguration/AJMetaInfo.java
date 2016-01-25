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
 * The meta information for this application.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 22 Jan 2016
 */
public enum AJMetaInfo {

    /** The application name. */
    NAME("AstroJournal"),

    /** The version. */
    VERSION("(devel)"),

    /** The website. */
    WEBSITE("http://pdp10.github.io/AstroJournal/"),

    /**
     * The default application configuration file name. This is in resources/.
     */
    DEFAULT_CONFIGURATION_PROPERTIES_FILE_NAME("application_properties.xml"),

    /** The user configuration file name. */
    USER_CONFIGURATION_PROPERTIES_FILE_NAME("astrojournal.xml"),

    /** The comment to be inserted in the user configuration file. */
    USER_CONFIGURATION_PROPERTIES_FILE_COMMENT(
	    "User setting for the application Config. Edit at your own risk."),

    /** The short text for the application license. */
    SHORT_LICENSE(
	    AJMetaInfo.NAME.getInfo()
		    + " "
		    + AJMetaInfo.VERSION.getInfo()
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
		    + "AstroJournal Web Site: <https://github.com/pdp10/AstroJournal>\n\n");

    /** The information value. */
    private final String info;

    /** Constructor */
    private AJMetaInfo(String info) {
	this.info = info;
    }

    /**
     * Return the info
     * 
     * @return the info
     */
    public String getInfo() {
	return this.info;
    }
}