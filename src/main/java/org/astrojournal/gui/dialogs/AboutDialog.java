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
 * - Piero Dalle Pezze: Class creation.
 */
package org.astrojournal.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.astrojournal.gui.AJMainGUI;

/**
 * Shows the generic about dialog giving details of the current version and
 * copyright assignments. This is just a thin shell around the AJTitlePanel
 * which actually holds the relevant information and which is also used on the
 * welcome screen.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 13 Dec 2015
 */
public class AboutDialog extends JDialog {

    private static final long serialVersionUID = -3893572577575366797L;

    /**
     * Provides a small panel which gives details of the AstroJournal version
     * and copyright. Used in both the welcome panel and the about dialog.
     *
     * @param application
     *            The application
     * @param resourceBundle
     *            The resource bundle
     */
    public AboutDialog(AJMainGUI application, ResourceBundle resourceBundle) {
	super(application);
	initComponents(application, resourceBundle);
    }

    /**
     * This method is called from within the constructor to initialise the form.
     * 
     * @param application
     *            The application
     * @param resourceBundle
     *            The resource bundle
     */
    private void initComponents(AJMainGUI application,
	    ResourceBundle resourceBundle) {

	setTitle(resourceBundle.getString("AJ.mnuAbout.text"));
	setLayout(new BorderLayout());

	AJTitlePanel welcomePanel = new AJTitlePanel();
	add(welcomePanel, BorderLayout.CENTER);

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

	setSize(500, 340);
	setLocationRelativeTo(application);
	setResizable(false);
	setVisible(true);
    }

}
