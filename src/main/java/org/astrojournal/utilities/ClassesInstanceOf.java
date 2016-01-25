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

/**
 * This class retrieves the name list of available classes which are instances
 * of a specific class type. A super package is used for increasing the search
 * time.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 12/04/2015
 */
public class ClassesInstanceOf {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(ClassesInstanceOf.class);

    /**
     * Check whether className is an instance of the super class type
     * 
     * @param className
     *            a fully qualified class name including package
     * @param superClassType
     *            The super type.
     * @return true if className is an exporter
     */
    private static boolean isInstanceOf(String className,
	    Class<?> superClassType) {
	try {
	    Class<?> cls = Class.forName(className);
	    Object clsInstance = cls.newInstance();
	    if (superClassType.isInstance(clsInstance)) {
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
	} catch (IllegalArgumentException e) {
	    log.debug(e);
	} catch (SecurityException e) {
	    log.debug(e);
	}
	return false;
    }

    /**
     * Return the list of packages whose contained classes are instance of a
     * super class Type.
     * 
     * @param superPackage
     * @param superClassType
     * @return the list of packages
     */
    public static ArrayList<String> getClassPackageInstanceOf(
	    String superPackage, Class<?> superClassType) {
	List<String> classes = ClassSearchUtils.searchClassPath(superPackage);
	ArrayList<String> classPackages = new ArrayList<String>();
	for (int i = 0; i < classes.size(); i++) {
	    String cls = classes.get(i);
	    if (isInstanceOf(cls, superClassType)) {
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
     * Return the list of object class names which are instance of a super class
     * Type. The names include the package.
     * 
     * @param superPackage
     * @param superClassType
     * @return the list of exporters including their package
     */
    public static ArrayList<String> getClassFullNamesInstanceOf(
	    String superPackage, Class<?> superClassType) {
	List<String> classes = ClassSearchUtils.searchClassPath(superPackage);
	ArrayList<String> classPackages = new ArrayList<String>();
	for (int i = 0; i < classes.size(); i++) {
	    String cls = classes.get(i);
	    if (isInstanceOf(cls, superClassType)) {
		classPackages.add(cls);
	    }
	}
	Collections.sort(classPackages);
	return classPackages;
    }

    /**
     * Return the list of object class names which are instance of a super class
     * Type.
     * 
     * @param superPackage
     * @param superClassType
     * @return the list of object class names
     */
    public static ArrayList<String> getClassNamesInstanceOf(
	    String superPackage, Class<?> superClassType) {
	List<String> classes = ClassSearchUtils.searchClassPath(superPackage);
	ArrayList<String> classNames = new ArrayList<String>();
	for (int i = 0; i < classes.size(); i++) {
	    String cls = classes.get(i);
	    if (isInstanceOf(cls, superClassType)) {
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
     * Prints the list of available objects in org.astrojournal (for testing).
     * 
     * @param args
     */
    public static void main(String[] args) {
	ArrayList<String> exporterNames = getClassNamesInstanceOf(
		"org.astrojournal", Object.class);
	for (int i = 0; i < exporterNames.size(); i++) {
	    System.out.println(exporterNames.get(i));
	    log.info(exporterNames.get(i));
	}

	ArrayList<String> exporterFullNames = getClassFullNamesInstanceOf(
		"org.astrojournal", Object.class);
	for (int i = 0; i < exporterFullNames.size(); i++) {
	    System.out.println(exporterFullNames.get(i));
	    log.info(exporterFullNames.get(i));
	}

	ArrayList<String> exporterPackageNames = getClassPackageInstanceOf(
		"org.astrojournal", Object.class);
	for (int i = 0; i < exporterPackageNames.size(); i++) {
	    System.out.println(exporterPackageNames.get(i));
	    log.info(exporterPackageNames.get(i));
	}

    }
}
