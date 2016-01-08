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
 * - Piero Dalle Pezze: Code taken from the classes AboutDialog.java and BamQCTitlePanel.java in 
 * the software BamQC (GPL v3). Code merged and adapted for AstroJournal. Added internationalisation.
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.astrojournal.configuration.AJConfig;
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

    private JTextField website;

    /**
     * Provides a small panel which gives details of the AstroJournal version
     * and copyright. Used in both the welcome panel and the about dialog.
     *
     * @param application
     */
    public AboutDialog(AJMainGUI application) {
	super(application);
	initComponents(application);
    }

    /**
     * This method is called from within the constructor to initialise the form.
     */
    private void initComponents(AJMainGUI application) {

	setTitle(AJConfig.BUNDLE.getString("AJ.mnuAbout.text"));
	setLayout(new BorderLayout());

	JPanel panelAbout = new JPanel();
	panelAbout.setLayout(new BorderLayout(7, 1));

	ImageIcon logo = new ImageIcon(
		ClassLoader.getSystemResource("graphics/logo/aj_icon_128.png"));
	JPanel logoPanel = new JPanel();
	logoPanel.add(new JLabel("", logo, JLabel.CENTER));
	logoPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
	add(logoPanel, BorderLayout.WEST);

	JPanel copyrights = new JPanel();
	copyrights.setLayout(new GridBagLayout());

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
	copyrights.add(program, constraints);

	constraints.gridy++;
	website = new JTextField("http://pdp10.github.io/AstroJournal/");
	website.setFont(new Font("Dialog", Font.PLAIN, 14));
	website.setEditable(false);
	website.setBorder(null);
	website.setOpaque(false);
	website.setHorizontalAlignment(JTextField.CENTER);
	copyrights.add(website, constraints);
	constraints.gridy++;

	JLabel copyright = new JLabel(
		"<html>\u00a9 Piero Dalle Pezze 2015-16</html>", JLabel.CENTER);
	copyright.setFont(new Font("Dialog", Font.PLAIN, 14));
	copyrights.add(copyright, constraints);
	constraints.gridy++;

	JLabel copyright2 = new JLabel(
		"Tango base icon theme \u00a9Public Domain", JLabel.CENTER);
	copyright2.setFont(new Font("Dialog", Font.PLAIN, 10));
	copyrights.add(copyright2, constraints);
	constraints.gridy++;

	JLabel copyright3 = new JLabel(
		"Apache Commons (io, lang3) \u00a9Apache Software Foundation, 2015",
		JLabel.CENTER);
	copyright3.setFont(new Font("Dialog", Font.PLAIN, 10));
	copyrights.add(copyright3, constraints);
	constraints.gridy++;

	JLabel copyright4 = new JLabel(
		"Apache log4j \u00a9Apache Software Foundation, 2012",
		JLabel.CENTER);
	copyright4.setFont(new Font("Dialog", Font.PLAIN, 10));
	copyrights.add(copyright4, constraints);
	constraints.gridy++;

	JLabel copyright5 = new JLabel("JUnit \u00a9 JUnit Team, 2015",
		JLabel.CENTER);
	copyright5.setFont(new Font("Dialog", Font.PLAIN, 10));
	copyrights.add(copyright5, constraints);

	panelAbout.add(copyrights, BorderLayout.CENTER);
	add(panelAbout, BorderLayout.CENTER);

	JPanel buttonPanel = new JPanel();
	JButton btnClose = new JButton(
		AJConfig.BUNDLE.getString("AJ.cmdClose.text"));
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

	setSize(500, 230);
	setLocationRelativeTo(application);
	setResizable(false);
	setVisible(true);
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
