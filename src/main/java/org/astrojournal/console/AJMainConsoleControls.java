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
package org.astrojournal.console;

import org.astrojournal.AJMainControls;
import org.astrojournal.generator.Generator;

/**
 * A simple class containing the commands for AJMainConsole.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public class AJMainConsoleControls extends AJMainControls {

    /**
     * Constructor
     * 
     * @param generator
     *            The generator
     */
    public AJMainConsoleControls(Generator generator) {
	super(generator);
    }

    @Override
    public boolean createJournal() {
	if (!preProcessing()) {
	    return false;
	}

	if (!processing()) {
	    return false;
	}

	if (!postProcessing()) {
	    return false;
	}

	return true;
    }
}
