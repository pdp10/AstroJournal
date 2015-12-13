/*
 * This file is part of AstroJournal.
 * AstroJournal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * AstroJournal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with AstroJournal. If not, see <http://www.gnu.org/licenses/>.
 */
package org.astrojournal.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.lang3.SystemUtils;

public class AJPreferences {

    /** The single instantiated instance of preferences */
    private static AJPreferences p = new AJPreferences();

    /** The default save location. */
    private File saveLocation = null;

    /** The last used save location. */
    private File lastUsedSaveLocation = null;

    /** The preferences file. */
    private File preferencesFile = null;

    /** The recently opened files list */
    private LinkedList<String> recentlyOpenedFiles = new LinkedList<String>();

    /**
     * Instantiates a preferences object. Only ever called once from inside this
     * class. External access is via the getInstnace() method.
     */
    private AJPreferences() {

	if (SystemUtils.IS_OS_MAC_OSX) {
	    // let's store these files inside a folder AstroJournal
	    preferencesFile = new File(System.getProperty("user.home")
		    + File.separator + "Library" + File.separator
		    + "AstroJournal" + File.separator + "aj_prefs.txt");
	    saveLocation = new File(System.getProperty("user.home")
		    + File.separator + "Library" + File.separator
		    + "AstroJournal" + File.separator + "AstroJournal_files");
	} else if (SystemUtils.IS_OS_WINDOWS) {
	    // let's store these files inside a folder AstroJournal
	    preferencesFile = new File(System.getProperty("user.home")
		    + File.separator + "astrojournal" + File.separator
		    + "config.txt");
	    saveLocation = new File(System.getProperty("user.home")
		    + File.separator + "astrojournal" + File.separator
		    + "AstroJournal_files");
	} else if (SystemUtils.IS_OS_UNIX) {
	    // let's store these files as hidden files inside a folder
	    // .astrojournal
	    preferencesFile = new File(System.getProperty("user.home")
		    + File.separator + ".astrojournal" + File.separator
		    + "config.txt");
	    saveLocation = new File(System.getProperty("user.home")
		    + File.separator + ".astrojournal" + File.separator
		    + "AstroJournal_files");
	} else {
	    // let's store these files explicitly inside a folder astrojournal
	    preferencesFile = new File(System.getProperty("user.home")
		    + File.separator + "astrojournal" + File.separator
		    + "config.txt");
	    saveLocation = new File(System.getProperty("user.home")
		    + File.separator + "astrojournal" + File.separator
		    + "AstroJournal_files");
	}

	new File(saveLocation.getAbsolutePath()).mkdirs();

	if (preferencesFile != null && preferencesFile.exists()) {
	    loadPreferences();
	} else {
	    try {
		savePreferences();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * Load preferences from a saved file
     */
    private void loadPreferences() {

	BufferedReader br = null;
	try {
	    br = new BufferedReader(new FileReader(preferencesFile));

	    String line;
	    String[] sections;
	    while ((line = br.readLine()) != null) {
		if (line.startsWith("#"))
		    continue; // It's a comment
		sections = line.split("\\t", -1);
		if (sections[0].equals("SaveLocation")) {
		    saveLocation = new File(sections[1]);
		} else if (sections[0].equals("RecentFile")) {
		    File f = new File(sections[1]);
		    if (f.exists()) {
			recentlyOpenedFiles.add(sections[1]);
		    }
		} else {
		    System.err.println("Unknown preference '" + sections[0]
			    + "'");
		}

	    }
	    br.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    /**
     * Save preferences.
     * 
     * @throws IOException
     */
    public void savePreferences() throws IOException {
	PrintWriter p = new PrintWriter(new FileWriter(preferencesFile));

	p.println("# AstroJournal Preferences file.  Do not edit by hand.");

	// Then the saveLocation
	p.println("SaveLocation\t" + saveLocation.getAbsolutePath());

	// Save the recently opened file list
	Iterator<String> rof = recentlyOpenedFiles.iterator();
	while (rof.hasNext()) {
	    p.println("RecentFile\t" + rof.next());
	}
	p.close();
    }

    /**
     * Gets the single instance of AstroJournalPreferences.
     * 
     * @return single instance of AstroJournalPreferences
     */
    public static AJPreferences getInstance() {
	return p;
    }

    /**
     * Gets the list of recently opened files.
     * 
     * @return the recently opened files
     */
    public String[] getRecentlyOpenedFiles() {
	return recentlyOpenedFiles.toArray(new String[0]);
    }

    /**
     * Adds a path to the recently opened files list. We store up to 5 recently
     * used files on a rotating basis. Adding a new one pushes out the oldest
     * one.
     * 
     * @param filePath
     *            The new file location to add
     */
    public void addRecentlyOpenedFile(String filePath) {
	// I know this is inefficient in a linked list but
	// it's only going to contain 5 elements so who cares
	if (recentlyOpenedFiles.contains(filePath)) {
	    recentlyOpenedFiles.remove(filePath);
	}
	recentlyOpenedFiles.add(0, filePath);

	// Only keep 9 items
	while (recentlyOpenedFiles.size() > 9) {
	    recentlyOpenedFiles.remove(9);
	}
	try {
	    savePreferences();
	} catch (IOException e) {
	    // In this case we don't report this error since
	    // the user isn't explicitly asking us to save.
	}
    }

    /**
     * Gets the default save location for projects / images / reports etc. This
     * will initially be the location in the preferences file but will be
     * updated during use to reflect the last actually used location.
     * 
     * @return The default save location.
     */
    public File getSaveLocation() {
	if (lastUsedSaveLocation != null)
	    return lastUsedSaveLocation;
	return saveLocation;
    }

    /**
     * Gets the default save location from the preferences file. This value will
     * always match the preferences file and will not update to reflect actual
     * usage within the current session.
     * 
     * @return The default save location
     */
    public File getSaveLocationPreference() {
	/**
	 * Always returns the save location saved in the preferences file. Used
	 * by the preferences editing dialog. Everywhere else should use
	 * getSaveLocation()
	 */
	return saveLocation;
    }

    /**
     * Sets the save location to record in the preferences file
     * 
     * @param f
     *            The new save location
     */
    public void setSaveLocation(File f) {
	saveLocation = f;
    }

    /**
     * Sets the last used save location. This is a temporary setting and will
     * not be recorded in the preferences file.
     * 
     * @param f
     *            The new last used save location
     */
    public void setLastUsedSaveLocation(File f) {
	if (f.isDirectory()) {
	    lastUsedSaveLocation = f;
	} else {
	    lastUsedSaveLocation = f.getParentFile();
	}
    }

}
