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
package org.astrojournal.gui;

/**
 * The approved actions for AstroJournal GUI.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public enum AJGUIActions {

    /** Event: Discard a choice */
    CANCEL,
    /** Event: Create the astronomy journals */
    CREATE_JOURNAL,
    /** Event: Open the astronomy journals */
    OPEN_JOURNAL,
    /** Event: Edit the application preferences */
    EDIT_PREFERENCES,
    /** Event: Retrieve the files location Event */
    FILES_LOCATION,
    /** Event: Show a summary for this application */
    HELP_ABOUT,
    /** Event: Show the application license */
    HELP_LICENSE,
    // /** Event: Approve a choice */
    // OK,
    /** Event: Quit the application */
    QUIT,
    /** Event: Save something */
    SAVE,

    ;

    /** Constructor */
    private AJGUIActions() {
    }

}
