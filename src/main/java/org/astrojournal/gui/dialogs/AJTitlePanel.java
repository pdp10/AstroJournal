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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.astrojournal.configuration.AJConfig;

/**
 * A simple title panel to return minimal information about AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 13 Dec 2015
 */
public class AJTitlePanel extends JPanel {

    private static final long serialVersionUID = 8192425675694492246L;

    private JTextField website;

    /**
     * Provides a small panel which gives details of the AstroJournal version
     * and copyright. Used in both the welcome panel and the about dialog.
     */
    public AJTitlePanel() {
	initComponents();
    }

    /**
     * This method is called from within the constructor to initialise the form.
     */
    private void initComponents() {
	setLayout(new BorderLayout(5, 1));

	ImageIcon logo = new ImageIcon(
		ClassLoader.getSystemResource("graphics/aj_icon.png"));
	JPanel logoPanel = new JPanel();
	logoPanel.add(new JLabel("", logo, JLabel.CENTER));
	logoPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
	add(logoPanel, BorderLayout.WEST);

	JPanel c = new JPanel();
	c.setLayout(new GridBagLayout());

	GridBagConstraints constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 1;
	constraints.weightx = 1;
	constraints.weighty = 1;
	constraints.insets = new Insets(3, 3, 0, 0);
	constraints.fill = GridBagConstraints.NONE;

	JLabel program = new SmoothJLabel(AJConfig.APPLICATION_NAME + " "
		+ AJConfig.APPLICATION_VERSION, JLabel.CENTER);
	program.setFont(new Font("Dialog", Font.BOLD, 18));
	program.setForeground(new Color(0, 0, 200));
	c.add(program, constraints);

	constraints.gridy++;
	website = new JTextField("http://pdp10.github.io/AstroJournal/");
	website.setFont(new Font("Dialog", Font.PLAIN, 14));
	website.setEditable(false);
	website.setBorder(null);
	website.setOpaque(false);
	website.setHorizontalAlignment(JTextField.CENTER);
	c.add(website, constraints);
	constraints.gridy++;

	JLabel copyright = new JLabel(
		"<html>\u00a9 Piero Dalle Pezze 2015-16</html>", JLabel.CENTER);
	copyright.setFont(new Font("Dialog", Font.PLAIN, 14));
	c.add(copyright, constraints);
	constraints.gridy++;

	add(c, BorderLayout.CENTER);
    }

    /**
     * A JLabel with anti-aliasing enabled. Takes the same constructor arguments
     * as JLabel
     */
    private class SmoothJLabel extends JLabel {

	private static final long serialVersionUID = 4036739763854114821L;

	/**
	 * Creates a new label
	 * 
	 * @param text
	 *            The text
	 * @param position
	 *            The JLabel constant position for alignment
	 */
	public SmoothJLabel(String text, int position) {
	    super(text, position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
	    if (g instanceof Graphics2D) {
		((Graphics2D) g).setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
	    }
	    super.paintComponent(g);
	}

    }

}
