/*
 * $Id: ClassSearchUtils.java,v 1.1 2009/03/01 12:01:11 rah003 Exp $
 *
 * Copyright 2009 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
/*
 * Changelog:
 * - Piero Dalle Pezze: Original source code from: http://www.java2s.com/Tutorial/Java/
 * 0180__File/SearchclassinclasspathandJarfiles.htm (GNU LGPL v2). Code adapted in order 
 * to just print the classes instead of loading the them. Use of log4j2 logging system.
 */
package org.astrojournal.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Original source code from: http://www.java2s.com/Tutorial/Java/
 * 0180__File/SearchclassinclasspathandJarfiles.htm . Code adapted by Piero
 * Dalle Pezze in order to just print the classes instead of loading the them.
 */
public class ClassSearchUtils {

    private static final Logger log = LogManager
	    .getLogger(ClassSearchUtils.class);

    /**
     * List of the resource found in the classpath.
     */
    private ArrayList<String> listPackages;

    /**
     * Extension of the resource to be found in the classpath.
     */
    private String extension;

    private String prefix;

    /**
     * Search for the resource with the extension in the classpath. Method
     * self-instantiate factory for every call to ensure thread safety.
     * 
     * @param prefix
     *            The package prefix to search in.
     * @return List of all resources with specified extension.
     */
    public static List<String> searchClassPath(String prefix) {
	return searchClassPath(prefix, ".class");
    }

    /**
     * Search for the resource with the extension in the classpath. Method
     * self-instantiate factory for every call to ensure thread safety.
     * 
     * @param prefix
     *            The package prefix to search in.
     * @param extension
     *            Mandatory extension of the resource. If all resources are
     *            required extension should be empty string. Null extension is
     *            not allowed and will cause method to fail.
     * @return List of all resources with specified extension.
     */
    public static List<String> searchClassPath(String prefix, String extension) {
	ClassSearchUtils factory = new ClassSearchUtils();
	factory.prefix = prefix;
	return factory.find(extension);
    }

    /**
     * Search for the resource with the extension in the classpath.
     * 
     * @param ext
     *            Mandatory extension of the resource. If all resources are
     *            required extension should be empty string. Null extension is
     *            not allowed and will cause method to fail.
     * @return List of all resources with specified extension.
     */
    private List<String> find(String ext) {
	this.extension = ext;
	this.listPackages = new ArrayList<String>();
	String classpath = System.getProperty("java.class.path");

	if (classpath == null) {
	    classpath = System.getProperty("java.class.path");
	}

	StringTokenizer tokenizer = new StringTokenizer(classpath,
		File.pathSeparator);
	String token;
	File dir;
	String name;
	while (tokenizer.hasMoreTokens()) {
	    token = tokenizer.nextToken();
	    dir = new File(token);
	    if (dir.isDirectory()) {
		lookInDirectory("", dir);
	    }
	    if (dir.isFile()) {
		name = dir.getName().toLowerCase();
		if (name.endsWith(".zip") || name.endsWith(".jar")) {
		    this.lookInArchive(dir);
		}
	    }
	}
	return this.listPackages;
    }

    /**
     * @param name
     *            Name of to parent directories in java class notation (dot
     *            separator)
     * @param dir
     *            Directory to be searched for classes.
     */
    private void lookInDirectory(String name, File dir) {
	log.debug("Looking in directory [" + dir.getName() + "].");
	File[] files = dir.listFiles();
	File file;
	String fileName;
	final int size = files.length;
	for (int i = 0; i < size; i++) {
	    file = files[i];
	    fileName = file.getName();
	    if (file.isFile()
		    && fileName.toLowerCase().endsWith(this.extension)) {
		if (this.extension.equalsIgnoreCase(".class")) {
		    fileName = fileName.substring(0, fileName.length() - 6);
		    // filter ignored resources
		    if (!(name + fileName).startsWith(this.prefix)) {
			continue;
		    }

		    log.debug("Found class: [" + name + fileName + "].");
		    this.listPackages.add(name + fileName);
		} else {
		    this.listPackages.add(name.replace('.', File.separatorChar)
			    + fileName);
		}
	    }
	    // search recursively.
	    // I don't like that but we will see how it will work.
	    if (file.isDirectory()) {
		lookInDirectory(name + fileName + ".", file);
	    }
	}

    }

    /**
     * Search archive files for required resource.
     * 
     * @param archive
     *            Jar or zip to be searched for classes or other resources.
     */
    private void lookInArchive(File archive) {
	log.debug("Looking in archive [" + archive.getName()
		+ "] for extension [" + this.extension + "].");
	JarFile jarFile = null;
	Enumeration<?> entries = null;
	try {
	    jarFile = new JarFile(archive);
	    entries = jarFile.entries();
	} catch (IOException e) {
	    log.error("Unable to read jar item.", e);
	    return;
	}
	// Enumeration entries = jarFile.entries();
	JarEntry entry;
	String entryName;
	while (entries.hasMoreElements()) {
	    entry = (JarEntry) entries.nextElement();
	    entryName = entry.getName();
	    if (entryName.toLowerCase().endsWith(this.extension)) {
		if (this.extension.equalsIgnoreCase(".class")) {
		    // convert name into java classloader notation
		    entryName = entryName.substring(0, entryName.length() - 6);
		    entryName = entryName.replace('/', '.');

		    // filter ignored resources
		    if (!entryName.startsWith(this.prefix)) {
			continue;
		    }

		    log.debug("Found class: [" + entryName + "]. ");
		    this.listPackages.add(entryName);
		} else {
		    this.listPackages.add(entryName);
		}
	    }
	}
	try {
	    if (jarFile != null) {
		jarFile.close();
	    }
	} catch (IOException e) {
	    log.error(e, e);
	}
    }

}
