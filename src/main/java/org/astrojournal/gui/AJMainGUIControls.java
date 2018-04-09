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

import org.astrojournal.AJMainControls;
import org.astrojournal.generator.Generator;

/**
 * A simple class containing the commands for AJMainGUI.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public class AJMainGUIControls extends AJMainControls {

    /**
     * A reference to AJ Main GUI.
     */
    private AJMainGUI ajMainGUI = null;

    /**
     * Constructor
     * 
     * @param ajMainGUI
     *            The GUI
     * @param generator
     *            The generator
     */
    public AJMainGUIControls(AJMainGUI ajMainGUI, Generator generator) {
	super(generator);
	this.ajMainGUI = ajMainGUI;
    }

    @Override
    public boolean createJournal() {
	if (!preProcessing()) {
	    ajMainGUI.setStatusPanelText(config.getResourceBundle().getString(
		    "AJ.errUnconfiguredPreferences.text"));
	    return false;
	}

	if (!processing()) {
	    ajMainGUI.setStatusPanelText(config.getResourceBundle().getString(
		    "AJ.errJournalNotExportedShort.text"));
	    return false;
	}

	if (!postProcessing()) {
	    ajMainGUI.setStatusPanelText(config.getResourceBundle().getString(
		    "AJ.errPDFLatexShort.text"));
	    return false;
	}

	ajMainGUI.setStatusPanelText(config.getResourceBundle().getString(
		"AJ.lblCreatedReportsLong.text"));
	return true;
    }
}
