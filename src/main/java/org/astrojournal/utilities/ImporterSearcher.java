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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.generator.ajimporter.AJImporter;

/**
 * This class retrieves the list of available importers.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 12/04/2015
 */
public class ImporterSearcher {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(ImporterSearcher.class);

    /** The super package containing the importers. */
    private static String importerSuperPackage = "org.astrojournal.generator";

    /**
     * Return the container package of the importers.
     * 
     * @return the container package of the importers
     */
    public static String getImporterSuperPackage() {
	return importerSuperPackage;
    }

    /**
     * Check whether className is an importer
     * 
     * @param className
     *            a fully qualified class name including package
     * @return true if className is an importer
     */
    private static boolean isImporter(String className) {
	try {
	    Class<?> cls = Class.forName(className);
	    Object clsInstance = cls.newInstance();
	    if (clsInstance instanceof AJImporter) {
		return true;
	    }
	} catch (UnsatisfiedLinkError e) {
	    log.debug(e);
	} catch (ClassNotFoundException e) {
	    log.debug(e);
	} catch (InstantiationException e) {
	    log.debug(e);
	} catch (IllegalAccessException e) {
	    log.debug(e);
	}
	return false;
    }

    /**
     * Return the list of importer packages.
     * 
     * @return the list of importer packages
     */
    public static ArrayList<String> getImporterPackageList() {
	List<String> classes = ClassSearchUtils
		.searchClassPath(importerSuperPackage);
	ArrayList<String> classPackages = new ArrayList<String>();
	for (int i = 0; i < classes.size(); i++) {
	    String cls = classes.get(i);
	    if (isImporter(cls)) {
		String clsPackage = cls.substring(0, cls.lastIndexOf("."));
		if (!classPackages.contains(clsPackage)) {
		    classPackages.add(clsPackage);
		}
	    }
	}
	Collections.sort(classPackages);
	return classPackages;
    }

    /**
     * Return the list of importers including their package.
     * 
     * @return the list of importers including their package
     */
    public static ArrayList<String> getImporterFullNameList() {
	List<String> classes = ClassSearchUtils
		.searchClassPath(importerSuperPackage);
	ArrayList<String> classPackages = new ArrayList<String>();
	for (int i = 0; i < classes.size(); i++) {
	    String cls = classes.get(i);
	    if (isImporter(cls)) {
		classPackages.add(cls);
	    }
	}
	Collections.sort(classPackages);
	return classPackages;
    }

    /**
     * Return the list of importer names.
     * 
     * @return the list of importer names
     */
    public static ArrayList<String> getImporterNameList() {
	List<String> classes = ClassSearchUtils
		.searchClassPath(importerSuperPackage);
	ArrayList<String> classNames = new ArrayList<String>();
	for (int i = 0; i < classes.size(); i++) {
	    String cls = classes.get(i);
	    if (isImporter(cls)) {
		String className = cls.substring(cls.lastIndexOf(".") + 1);
		if (!classNames.contains(className)) {
		    classNames.add(className);
		}
	    }
	}
	Collections.sort(classNames);
	return classNames;
    }

    /**
     * Prints the list of available exporters.
     * 
     * @param args
     */
    public static void main(String[] args) {
	ArrayList<String> importerNames = getImporterNameList();
	for (int i = 0; i < importerNames.size(); i++) {
	    System.out.println(importerNames.get(i));
	    log.info(importerNames.get(i));
	}

	ArrayList<String> importerFullNames = getImporterFullNameList();
	for (int i = 0; i < importerFullNames.size(); i++) {
	    System.out.println(importerFullNames.get(i));
	    log.info(importerFullNames.get(i));
	}

	ArrayList<String> importerPackageNames = getImporterPackageList();
	for (int i = 0; i < importerPackageNames.size(); i++) {
	    System.out.println(importerPackageNames.get(i));
	    log.info(importerPackageNames.get(i));
	}

    }

}
