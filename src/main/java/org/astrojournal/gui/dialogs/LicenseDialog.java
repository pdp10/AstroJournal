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
package org.astrojournal.gui.dialogs;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import org.astrojournal.gui.AJMainGUI;
import org.astrojournal.utilities.ReadFromJar;

/**
 * The Class LicenseDialog shows a text representation of the License used for
 * AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 13 Dec 2015
 */
public class LicenseDialog extends JDialog {

    /**
     * A temporary file containing the license file.
     */
    private File tempLicense = null;

    private static final long serialVersionUID = -8023870968821351252L;

    /** The html pane. */
    private JEditorPane htmlPane;

    /**
     * Instantiates a new license dialog.
     * 
     * @param application
     *            the application
     * @param license
     *            the license file
     * @throws FileNotFoundException
     *             if the license file does not exist.
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public LicenseDialog(AJMainGUI application, String license)
	    throws FileNotFoundException, IOException {
	super(application);
	initComponents(application, license);
    }

    /**
     * This method is called from within the constructor to initialise the form.
     * 
     * @throws FileNotFoundException
     *             if the license file does not exist.
     * @throws IOException
     */
    private void initComponents(AJMainGUI application, String license)
	    throws FileNotFoundException, IOException {
	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	setTitle("AstroJournal License");
	if (tempLicense == null) {
	    ReadFromJar rfj = new ReadFromJar();
	    tempLicense = rfj.getFileFromJARFile(license, "/" + license);
	}
	if (!tempLicense.exists()) {
	    throw new FileNotFoundException();
	}
	htmlPane = new JEditorPane(tempLicense.toURI().toURL());
	htmlPane.setEditable(false);
	htmlPane.setFont(new Font("Monospaced", Font.PLAIN, 12));
	setContentPane(new JScrollPane(htmlPane));
	setSize(580, 500);
	setLocationRelativeTo(application);
	setVisible(true);
    }
}
