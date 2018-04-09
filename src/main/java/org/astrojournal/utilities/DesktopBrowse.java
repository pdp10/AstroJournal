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
 * - Piero Dalle Pezze: Class creation.
 */
package org.astrojournal.utilities;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A simple class to solve the problem caused by the exception
 * "java.lang.UnsupportedOperationException: The BROWSE action is not supported
 * on the current platform!" thrown by
 * Desktop.getDesktop().browse(hle.getURL().toURI())
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public class DesktopBrowse {

    /** The log associated to this class. */
    private static Logger log = LogManager.getLogger(DesktopBrowse.class);

    private static String osInfo = SystemUtils.OS_ARCH + " "
	    + SystemUtils.OS_NAME + " " + SystemUtils.OS_VERSION;

    /**
     * Browse an URI using a platform specific browser if possible.
     * Alternatively, attempt to use Desktop.getDesktop().browse(uri).
     * 
     * @param uri
     *            the URI to browser
     * @return true if the command succeeded, false otherwise.
     */
    public static boolean browse(URI uri) {
	if (browsePlatformSpecific(uri)) {
	    return true;
	}
	if (browseJavaDesktop(uri)) {
	    log.debug("Java Desktop SUCCEEDED");
	    return true;
	}
	log.error("Failed to browse the URI " + uri.toString()
		+ " on this platform (" + osInfo + ").");
	return false;
    }

    /**
     * Browse an URI using a platform specific browser.
     * 
     * @param uri
     *            the URI to browser
     * @return true if the command succeeded, false otherwise.
     */
    public static boolean browsePlatformSpecific(URI uri) {
	if (SystemUtils.IS_OS_MAC_OSX) {
	    if (browseByPlatform("open", uri)) {
		log.debug("The command 'open' SUCCEEDED");
		return true;
	    }
	}
	if (SystemUtils.IS_OS_WINDOWS) {
	    if (browseByPlatform("explorer", uri)) {
		log.debug("The command 'explorer' SUCCEEDED");
		return true;
	    }
	}
	if (SystemUtils.IS_OS_UNIX) {
	    if (browseByPlatform("kde-open", uri)) {
		log.debug("The command 'kde-open' SUCCEEDED");
		return true;
	    }
	    if (browseByPlatform("gnome-open", uri)) {
		log.debug("The command 'gnome-open' SUCCEEDED");
		return true;
	    }
	    if (browseByPlatform("xdg-open", uri)) {
		log.debug("The command 'xdg-open' SUCCEEDED");
		return true;
	    }
	}
	return false;
    }

    /**
     * Browse an URI using a platform specific browser.
     * 
     * @param command
     *            the platform specific command to execute
     * @param uri
     *            the URI to browser
     * @return true if the command succeeded, false otherwise.
     */
    private static boolean browseByPlatform(String command, URI uri) {
	String[] browseCommand = new String[] { command, uri.toString() };
	log.debug("Trying to run " + browseCommand[0] + " " + browseCommand[1]);
	try {
	    Process p = Runtime.getRuntime().exec(browseCommand);
	    if (p == null || p.exitValue() == 0) {
		log.debug("The command '" + browseCommand[0] + " "
			+ browseCommand[1]
			+ "' did not start or terminated immediately.");
		return false;
	    }
	    log.debug("The command '" + browseCommand[0] + " "
		    + browseCommand[1] + "' crashed for some reason.");
	} catch (IllegalThreadStateException e) {
	    log.debug("The command '" + browseCommand[0] + " "
		    + browseCommand[1] + "' is running!");
	    return true;
	} catch (IOException e) {
	    // Let's not report the stack trace as we are still trying.
	    log.debug(e);
	}
	return false;
    }

    /**
     * Browse an URI using Java Desktop (not recommended).
     * 
     * @param uri
     *            the URI to browser
     * @return true if the command succeeded, false otherwise.
     */
    public static boolean browseJavaDesktop(URI uri) {
	log.debug("Trying to run Desktop.getDesktop().browse(" + uri.toString()
		+ ")");
	try {
	    if (!Desktop.isDesktopSupported()) {
		log.debug("Java Desktop is not supported on this platform ("
			+ osInfo + ").\n" + "Impossible to browse the URI: "
			+ uri.toString());
		return false;
	    }
	    if (!Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
		log.debug("Java Desktop does not support the action BROWSER on this platform ("
			+ osInfo + ").\n");
		return false;
	    }
	    Desktop.getDesktop().browse(uri);
	} catch (IOException e) {
	    // Report the stack trace to make visible that something went wrong.
	    log.debug("Java Desktop did not work on this platform (" + osInfo
		    + ")", e);
	    return false;
	}
	return true;
    }
}
