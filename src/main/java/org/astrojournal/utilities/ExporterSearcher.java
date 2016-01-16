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
import org.astrojournal.generator.ajexporter.AJExporter;

/**
 * This class retrieves the list of available exporters.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 12/04/2015
 */
public class ExporterSearcher {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(ExporterSearcher.class);

    /** The super package containing the exporters. */
    private static String exporterSuperPackage = "org.astrojournal.generator.ajexporter";

    /**
     * Return the container package of the exporters.
     * 
     * @return the container package of the exporters
     */
    public static String getExporterSuperPackage() {
	return exporterSuperPackage;
    }

    /**
     * Check whether className is an exporter
     * 
     * @param className
     *            a fully qualified class name including package
     * @return true if className is an exporter
     */
    private static boolean isExporter(String className) {
	try {
	    Class<?> cls = Class.forName(className);
	    Object clsInstance = cls.newInstance();
	    if (clsInstance instanceof AJExporter) {
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
     * Return the list of exporter packages.
     * 
     * @return the list of exporter packages
     */
    public static ArrayList<String> getExporterPackageList() {
	List<String> classes = ClassSearchUtils
		.searchClassPath(exporterSuperPackage);
	ArrayList<String> classPackages = new ArrayList<String>();
	for (int i = 0; i < classes.size(); i++) {
	    String cls = classes.get(i);
	    if (isExporter(cls)) {
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
     * Return the list of exporters including their package.
     * 
     * @return the list of exporters including their package
     */
    public static ArrayList<String> getExporterFullNameList() {
	List<String> classes = ClassSearchUtils
		.searchClassPath(exporterSuperPackage);
	ArrayList<String> classPackages = new ArrayList<String>();
	for (int i = 0; i < classes.size(); i++) {
	    String cls = classes.get(i);
	    if (isExporter(cls)) {
		classPackages.add(cls);
	    }
	}
	Collections.sort(classPackages);
	return classPackages;
    }

    /**
     * Return the list of exporter names.
     * 
     * @return the list of exporter names
     */
    public static ArrayList<String> getExporterNameList() {
	List<String> classes = ClassSearchUtils
		.searchClassPath(exporterSuperPackage);
	ArrayList<String> classNames = new ArrayList<String>();
	for (int i = 0; i < classes.size(); i++) {
	    String cls = classes.get(i);
	    if (isExporter(cls)) {
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
	ArrayList<String> exporterNames = getExporterNameList();
	for (int i = 0; i < exporterNames.size(); i++) {
	    System.out.println(exporterNames.get(i));
	    log.info(exporterNames.get(i));
	}

	ArrayList<String> exporterFullNames = getExporterFullNameList();
	for (int i = 0; i < exporterFullNames.size(); i++) {
	    System.out.println(exporterFullNames.get(i));
	    log.info(exporterFullNames.get(i));
	}

	ArrayList<String> exporterPackageNames = getExporterPackageList();
	for (int i = 0; i < exporterPackageNames.size(); i++) {
	    System.out.println(exporterPackageNames.get(i));
	    log.info(exporterPackageNames.get(i));
	}

    }

}
