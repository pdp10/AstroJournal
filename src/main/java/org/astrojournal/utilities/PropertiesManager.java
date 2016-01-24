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
package org.astrojournal.utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Manager of Properties. This class wraps the functions functions for loading
 * and storing Java user properties and XML files containing Java user
 * properties.
 *
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 18 Jan 2016
 */
public class PropertiesManager {

    /**
     * Default constructor.
     */
    public PropertiesManager() {
    }

    // FILE .properties
    /**
     * Load a .properties file into an object Properties.
     * 
     * @param filename
     *            Absolute path to the .properties file
     * @return The properties contained in the file.
     * @throws IOException
     *             If the file cannot be loaded.
     */
    public static Properties load(String filename) throws IOException {
	return load(new Properties(), filename);
    }

    /**
     * Load a .properties file and store the read properties into an existing
     * set of properties. If a property already exists, it will be overridden by
     * the property defined in the new filename.
     * 
     * @param existingProperties
     *            an existing set of properties
     * @param filename
     *            Absolute path to the .properties file
     * @return An integrated set of properties.
     * @throws IOException
     *             If the file cannot be loaded.
     */
    public static Properties load(Properties existingProperties, String filename)
	    throws IOException {
	Properties properties = new Properties(existingProperties);
	FileInputStream in = null;
	try {
	    in = new FileInputStream(filename);
	    properties.load(in);
	} catch (IOException e) {
	    throw e;
	} finally {
	    try {
		if (in != null)
		    in.close();
	    } catch (IOException e) {
		throw e;
	    }
	}
	return properties;
    }

    /**
     * Store an object Properties into a .properties file.
     * 
     * @param properties
     *            an existing set of properties to store
     * @param filename
     *            Absolute path to the .properties file
     * @param comment
     *            An optional comment to put in the file or null.
     * @throws IOException
     *             If the file cannot be written.
     */
    public static void store(Properties properties, String filename,
	    String comment) throws IOException {
	FileOutputStream os = null;
	try {
	    os = new FileOutputStream(filename);
	    properties.store(os, comment);
	} catch (IOException e) {
	    throw e;
	} finally {
	    if (os != null) {
		try {
		    os.close();
		} catch (IOException e) {
		    throw e;
		}
	    }
	}
    }

    // FILE XML
    /**
     * Load a XML file into an object Properties.
     * 
     * @param filename
     *            Absolute path to the XML file
     * @return The properties contained in the file.
     * @throws IOException
     *             If the file cannot be loaded.
     */
    public static Properties loadFromXML(String filename) throws IOException {
	return loadFromXML(new Properties(), filename);
    }

    /**
     * Load a XML file and store the read properties into an existing set of
     * properties. If a property already exists, it will be overridden by the
     * property defined in the new filename.
     * 
     * @param existingProperties
     *            an existing set of properties
     * @param filename
     *            Absolute path to the XML file
     * @return An integrated set of properties.
     * @throws IOException
     *             If the file cannot be loaded.
     */
    public static Properties loadFromXML(Properties existingProperties,
	    String filename) throws IOException {
	Properties properties = new Properties(existingProperties);
	FileInputStream in = null;
	try {
	    in = new FileInputStream(filename);
	    properties.loadFromXML(in);
	} catch (IOException e) {
	    throw e;
	} finally {
	    try {
		if (in != null)
		    in.close();
	    } catch (IOException e) {
		throw e;
	    }
	}
	return properties;
    }

    /**
     * Store an object Properties into a XML file.
     * 
     * @param properties
     *            an existing set of properties to store
     * @param filename
     *            Absolute path to the XML file
     * @param comment
     *            An optional comment to put in the file or null.
     * @throws IOException
     *             If the file cannot be written.
     */
    public static void storeToXML(Properties properties, String filename,
	    String comment) throws IOException {
	FileOutputStream os = null;
	try {
	    os = new FileOutputStream(filename);
	    properties.storeToXML(os, comment);
	} catch (IOException e) {
	    throw e;
	} finally {
	    if (os != null) {
		try {
		    os.close();
		} catch (IOException e) {
		    throw e;
		}
	    }
	}
    }
}
