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
package org.astrojournal.gui.menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.astrojournal.configuration.PreferencesDialog;
import org.astrojournal.gui.AJMainGUI;
import org.astrojournal.gui.dialogs.AboutDialog;
import org.astrojournal.gui.dialogs.LicenseDialog;
import org.astrojournal.gui.dialogs.help.HelpDialog;

/**
 * Astro Journal Menu bar.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 13 Dec 2015
 */
public class AJMenuBar extends JMenuBar implements ActionListener {

    private static final long serialVersionUID = 7811240084595362788L;

    /** The main application */
    private AJMainGUI application;

    private JMenu fileMenu;
    private JMenuItem fileCreateJournal;
    private JMenuItem fileQuit;

    private JMenu editMenu;
    private JMenuItem editPreferences;

    private JMenu helpMenu;
    private JMenuItem helpContents;
    private JMenuItem helpLicense;
    private JMenuItem helpAbout;

    /**
     * Constructor for this menu bar.
     * 
     * @param application
     */
    public AJMenuBar(AJMainGUI application) {
	this.application = application;

	fileMenu = new JMenu("File");
	fileMenu.setMnemonic(KeyEvent.VK_F);

	fileCreateJournal = new JMenuItem("Create Journals");
	fileCreateJournal.setMnemonic(KeyEvent.VK_J);
	fileCreateJournal.setAccelerator(KeyStroke.getKeyStroke('J', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	fileCreateJournal.setActionCommand("create_journals");
	fileCreateJournal.addActionListener(this);
	fileMenu.add(fileCreateJournal);

	// fileMenu.addSeparator();

	fileQuit = new JMenuItem("Quit");
	fileQuit.setMnemonic(KeyEvent.VK_Q);
	fileQuit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	fileQuit.setActionCommand("quit");
	fileQuit.addActionListener(this);
	fileMenu.add(fileQuit);

	add(fileMenu);

	editMenu = new JMenu("Edit");
	editMenu.setMnemonic(KeyEvent.VK_E);
	editPreferences = new JMenuItem("Preferences...");
	editPreferences.setActionCommand("edit_preferences");
	editPreferences.setMnemonic(KeyEvent.VK_P);
	editPreferences.setAccelerator(KeyStroke.getKeyStroke('P', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	editPreferences.addActionListener(this);
	editMenu.add(editPreferences);
	add(editMenu);

	helpMenu = new JMenu("Help");
	helpMenu.setMnemonic(KeyEvent.VK_H);

	helpContents = new JMenuItem("Help Contents");
	helpContents.setActionCommand("help_contents");
	helpContents.setMnemonic(KeyEvent.VK_T);
	helpContents.setAccelerator(KeyStroke.getKeyStroke('T', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	helpContents.addActionListener(this);
	helpMenu.add(helpContents);

	helpLicense = new JMenuItem("License");
	helpLicense.setActionCommand("help_license");
	helpLicense.setMnemonic(KeyEvent.VK_L);
	helpLicense.setAccelerator(KeyStroke.getKeyStroke('L', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	helpLicense.addActionListener(this);
	helpMenu.add(helpLicense);

	helpAbout = new JMenuItem("About AstroJournal");
	helpAbout.setActionCommand("help_about");
	helpAbout.setMnemonic(KeyEvent.VK_A);
	helpAbout.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	helpAbout.addActionListener(this);
	helpMenu.add(helpAbout);

	add(helpMenu);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {

	String action = ae.getActionCommand();

	if (action.equals("create_journals")) {
	    application.createJournals();
	} else if (action.equals("quit")) {
	    application.closeApplication();
	} else if (action.equals("edit_preferences")) {
	    PreferencesDialog preferencesDialog = new PreferencesDialog(
		    application);
	} else if (action.equals("help_contents")) {
	    // FIXME
	    // JOptionPane
	    // .showMessageDialog(
	    // application,
	    // "This currently does not work..\n"
	    // + "THE PROBLEM IS that you need to scan a jar file and not a \n"
	    // + "normal file system structure. \n"
	    // +
	    // "Therefore, you need to work with URI and then in HelpIndexRoot, \n"
	    // + "scan the folder inside the jar file \n"
	    // +
	    // "using Uri instead of FILE. Therefore, extract the .html files and \n"
	    // + "show the help.", "WARNING",
	    // JOptionPane.ERROR_MESSAGE);
	    // end FIXME

	    // THIS ONLY WORKS IN ECLIPSE. ORIGINAL CODE
	    // try {
	    // HelpDialog helpDialog = new HelpDialog(application, new File(
	    // URLDecoder.decode(ClassLoader.getSystemResource("help")
	    // .getFile(), "UTF-8")));
	    // } catch (UnsupportedEncodingException e1) {
	    // e1.printStackTrace();
	    // }

	    try {
		HelpDialog helpDialog = new HelpDialog(application, new File(
			"help"));
	    } catch (FileNotFoundException e) {
		JOptionPane.showMessageDialog(application,
			"The Help Content was not found.", "File not found",
			JOptionPane.ERROR_MESSAGE);
	    }
	    // FIXME THE PROBLEM IS that you need to scan a jar file and not a
	    // normal file system structure.
	    // Therefore, you need to work with URI and then in HelpIndexRoot,
	    // scan the folder inside the jar file
	    // using Uri instead of FILE. Therefore, extract the .html files and
	    // show the help.

	    // THIS IS CORRECT
	    // HelpDialog helpDialog = new HelpDialog(application,
	    // ClassLoader.getSystemResource("help"));

	} else if (action.equals("help_license")) {
	    String license = "LICENSE.txt";
	    try {
		LicenseDialog licenseDialog = new LicenseDialog(application,
			license);
	    } catch (FileNotFoundException e) {
		JOptionPane.showMessageDialog(application, "The file "
			+ license + " was not found.", "File not found",
			JOptionPane.ERROR_MESSAGE);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} else if (action.equals("help_about")) {
	    AboutDialog aboutDialog = new AboutDialog(application);
	} else {
	    JOptionPane.showMessageDialog(application, "Unknown menu command "
		    + action, "Unknown command", JOptionPane.ERROR_MESSAGE);
	}
    }
}
