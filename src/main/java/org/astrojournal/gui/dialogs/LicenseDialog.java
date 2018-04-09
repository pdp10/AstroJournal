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
 * - Piero Dalle Pezze: Class creation
 */
package org.astrojournal.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.astrojournal.AJMetaInfo;
import org.astrojournal.gui.AJMainGUI;
import org.astrojournal.utilities.ReadFromJar;

/**
 * The Class LicenseDialog shows a text representation of the License used for
 * AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
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
     * @param resourceBundle
     *            the resource bundle
     * @param license
     *            the license file
     * @throws FileNotFoundException
     *             if the license file does not exist.
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public LicenseDialog(AJMainGUI application, ResourceBundle resourceBundle,
	    String license) throws FileNotFoundException, IOException {
	super(application);
	initComponents(application, resourceBundle, license);
    }

    /**
     * This method is called from within the constructor to initialise the form.
     * 
     * @param application
     *            the application
     * @param resourceBundle
     *            the resource bundle
     * @param license
     *            the license file
     * @throws FileNotFoundException
     *             if the license file does not exist.
     * @throws IOException
     */
    private void initComponents(AJMainGUI application,
	    ResourceBundle resourceBundle, String license)
	    throws FileNotFoundException, IOException {

	setTitle(AJMetaInfo.NAME.getInfo() + " "
		+ resourceBundle.getString("AJ.lblLicense.text"));
	setLayout(new BorderLayout());

	if (tempLicense == null) {
	    ReadFromJar rfj = new ReadFromJar();
	    tempLicense = rfj.getFileFromJARFile(license, "/" + license);
	}
	if (!tempLicense.exists()) {
	    throw new FileNotFoundException();
	}
	htmlPane = new JEditorPane(tempLicense.toURI().toURL());
	// The following two settings are required for setting the default
	// button
	htmlPane.setEditable(false);
	htmlPane.setFocusable(false);
	htmlPane.setFont(new Font("Monospaced", Font.PLAIN, 12));

	JScrollPane scrollPane = new JScrollPane(htmlPane);
	// The following setting is required for setting the default
	// button
	scrollPane.setFocusable(true);
	add(scrollPane, BorderLayout.CENTER);

	JPanel buttonPanel = new JPanel();
	JButton btnClose = new JButton(
		resourceBundle.getString("AJ.cmdClose.text"));
	getRootPane().setDefaultButton(btnClose);
	btnClose.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent ae) {
		setVisible(false);
		dispose();
	    }
	});
	buttonPanel.add(btnClose);

	add(buttonPanel, BorderLayout.SOUTH);

	setSize(580, 500);
	setLocationRelativeTo(application);
	setVisible(true);
    }
}
