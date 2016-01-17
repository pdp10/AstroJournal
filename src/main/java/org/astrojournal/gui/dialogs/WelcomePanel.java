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
 * - Piero Dalle Pezze:  Code taken from the classes WelcomePanel.java in 
 * the software BamQC (GPL v3). Code merged and adapted for AstroJournal. Added internationalisation.
 */
package org.astrojournal.gui.dialogs;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.astrojournal.configuration.AJConfig;

/**
 * A panel for AstroJournal Welcome.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 9 Jan 2016
 */
public class WelcomePanel extends JPanel {

    private static final long serialVersionUID = 6524251080594027534L;

    /**
     * Constructor
     */
    public WelcomePanel() {
	super();
	initComponents();
    }

    /**
     * This method is called from within the constructor to initialise the form.
     */
    private void initComponents() {
	setLayout(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();

	gbc.gridx = 1;
	gbc.gridy = 1;
	gbc.weightx = 0.5;
	gbc.weighty = 0.5;

	add(new JPanel(), gbc);
	gbc.gridy++;
	gbc.weighty = 0.01;

	gbc.insets = new Insets(10, 10, 10, 10);
	gbc.fill = GridBagConstraints.NONE;

	add(new AJTitlePanel(), gbc);

	gbc.gridy++;
	gbc.weighty = 0.5;

	JLabel welcomeLabel = new JLabel(AJConfig.getInstance()
		.getLocaleBundle().getString("AJ.lblWelcomeLabel.text"));
	welcomeLabel.setFont(new Font("Arial", Font.BOLD, 12));
	add(welcomeLabel, gbc);

	gbc.gridy++;
	gbc.weighty = 0.99;
	add(new JPanel(), gbc);

    }
}