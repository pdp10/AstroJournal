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
package org.astrojournal.gui.menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.AJConfigurator;
import org.astrojournal.configuration.PreferencesDialog;
import org.astrojournal.gui.AJMainGUI;
import org.astrojournal.gui.dialogs.AboutDialog;
import org.astrojournal.gui.dialogs.HelpDialog;
import org.astrojournal.gui.dialogs.LicenseDialog;

/**
 * Astro Journal Menu bar.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 13 Dec 2015
 */
public class AJMenuBar extends JMenuBar implements ActionListener {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(AJMenuBar.class);

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
	initComponents();
    }

    /**
     * This method is called from within the constructor to initialise the form.
     */
    private void initComponents() {

	fileMenu = new JMenu(AJConfigurator.getInstance().getLocaleBundle()
		.getString("AJ.mnuFile.text"));
	fileMenu.setMnemonic(KeyEvent.VK_F);

	fileCreateJournal = new JMenuItem(AJConfigurator.getInstance()
		.getLocaleBundle().getString("AJ.cmdCreateJournal.text"));
	fileCreateJournal.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/create_journals_16.png")));
	fileCreateJournal.setMnemonic(KeyEvent.VK_J);
	fileCreateJournal.setAccelerator(KeyStroke.getKeyStroke('J', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	fileCreateJournal.setActionCommand("create_journals");
	fileCreateJournal.addActionListener(this);
	fileMenu.add(fileCreateJournal);

	// fileMenu.addSeparator();

	fileQuit = new JMenuItem(AJConfigurator.getInstance().getLocaleBundle()
		.getString("AJ.mnuQuit.text"));
	fileQuit.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/quit_16.png")));
	fileQuit.setMnemonic(KeyEvent.VK_Q);
	fileQuit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	fileQuit.setActionCommand("quit");
	fileQuit.addActionListener(this);
	fileMenu.add(fileQuit);

	add(fileMenu);

	editMenu = new JMenu(AJConfigurator.getInstance().getLocaleBundle()
		.getString("AJ.mnuEdit.text"));
	editMenu.setMnemonic(KeyEvent.VK_E);
	editPreferences = new JMenuItem(AJConfigurator.getInstance()
		.getLocaleBundle().getString("AJ.mnuPreferences.text"));
	editPreferences.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/preferences_16.png")));
	editPreferences.setActionCommand("edit_preferences");
	editPreferences.setMnemonic(KeyEvent.VK_P);
	editPreferences.setAccelerator(KeyStroke.getKeyStroke('P', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	editPreferences.addActionListener(this);
	editMenu.add(editPreferences);
	add(editMenu);

	helpMenu = new JMenu(AJConfigurator.getInstance().getLocaleBundle()
		.getString("AJ.mnuHelp.text"));
	helpMenu.setMnemonic(KeyEvent.VK_H);

	helpContents = new JMenuItem(AJConfigurator.getInstance().getLocaleBundle()
		.getString("AJ.mnuHelpContents.text"));
	helpContents.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/help_16.png")));
	helpContents.setActionCommand("help_contents");
	helpContents.setMnemonic(KeyEvent.VK_T);
	helpContents.setAccelerator(KeyStroke.getKeyStroke('T', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	helpContents.addActionListener(this);
	helpMenu.add(helpContents);

	helpLicense = new JMenuItem(AJConfigurator.getInstance().getLocaleBundle()
		.getString("AJ.mnuLicense.text"));
	helpLicense.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/license_16.png")));
	helpLicense.setActionCommand("help_license");
	helpLicense.setMnemonic(KeyEvent.VK_L);
	helpLicense.setAccelerator(KeyStroke.getKeyStroke('L', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	helpLicense.addActionListener(this);
	helpMenu.add(helpLicense);

	helpAbout = new JMenuItem(AJConfigurator.getInstance().getLocaleBundle()
		.getString("AJ.mnuAbout.text"));
	helpAbout.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/about_16.png")));
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
	    HelpDialog helpDialog = new HelpDialog(application);

	} else if (action.equals("help_license")) {
	    String license = "LICENSE.txt";
	    try {
		LicenseDialog licenseDialog = new LicenseDialog(application,
			license);
	    } catch (FileNotFoundException e) {
		log.error(
			AJConfigurator.getInstance().getLocaleBundle()
				.getString("AJ.errLicenseNotFound.text"),
			AJConfigurator.getInstance().getLocaleBundle()
				.getString("AJ.errFileNotFound.text"), e);
		JOptionPane.showMessageDialog(
			application,
			AJConfigurator.getInstance().getLocaleBundle()
				.getString("AJ.errLicenseNotFound.text"),
			AJConfigurator.getInstance().getLocaleBundle()
				.getString("AJ.errFileNotFound.text"),
			JOptionPane.ERROR_MESSAGE);
	    } catch (IOException e) {
		log.error(e, e);
	    }

	} else if (action.equals("help_about")) {
	    AboutDialog aboutDialog = new AboutDialog(application);

	} else {
	    log.error(AJConfigurator.getInstance().getLocaleBundle()
		    .getString("AJ.errCommandNotFound.text")
		    + action);
	    JOptionPane.showMessageDialog(application, AJConfigurator.getInstance()
		    .getLocaleBundle().getString("AJ.errCommandNotFound.text")
		    + action, AJConfigurator.getInstance().getLocaleBundle()
		    .getString("AJ.errCommandNotFound.text"),
		    JOptionPane.ERROR_MESSAGE);
	}
    }

    /**
     * Enable or disable the menu items.
     * 
     * @param action
     * @param enabled
     */
    public void setEnabled(String action, boolean enabled) {
	if (action.equals("create_journal")) {
	    fileCreateJournal.setEnabled(enabled);
	} else if (action.equals("preferences")) {
	    editPreferences.setEnabled(enabled);
	} else {
	    log.error("Unknown action : " + action);
	}
    }

}
