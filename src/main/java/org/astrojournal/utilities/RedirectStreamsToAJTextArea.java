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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.astrojournal.gui.AJMainGUI;

/**
 * A simple class to redirect the console out and err streams to AJTextArea.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 22/12/2015
 */
public class RedirectStreamsToAJTextArea extends OutputStream {
    /**
     * The AJ Main GUI.
     */
    private AJMainGUI ajMainGUI;

    /**
     * Constructor.
     * 
     * @param ajMainGUI
     *            The AJ Main GUI
     */
    public RedirectStreamsToAJTextArea(AJMainGUI ajMainGUI) {
	this.ajMainGUI = ajMainGUI;
	System.setOut(new PrintStream(this, true));
	System.setErr(new PrintStream(this, true));
    }

    /**
     * Redirect to AJ Text Area.
     */
    @Override
    public void write(int b) throws IOException {
	ajMainGUI.appendTextToTextArea(String.valueOf((char) b));
    }

    /**
     * Redirect to AJ Text Area.
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
	ajMainGUI.appendTextToTextArea(new String(b, off, len));
    }

    /**
     * Redirect to AJ Text Area.
     */
    @Override
    public void write(byte[] b) throws IOException {
	write(b, 0, b.length);
    }
}
