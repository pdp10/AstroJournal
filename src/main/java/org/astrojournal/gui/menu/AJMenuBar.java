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
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.gui.AJGUIActions;
import org.astrojournal.gui.AJMainGUI;
import org.astrojournal.gui.dialogs.AboutDialog;
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
    /** The resource bundle */
    private ResourceBundle resourceBundle;

    private JMenu fileMenu;
    private JMenuItem fileCreateJournal;
    private JMenuItem fileOpenJournal;
    private JMenuItem fileQuit;

    private JMenu editMenu;
    private JMenuItem editPreferences;

    private JMenu helpMenu;
    private JMenuItem helpLicense;
    private JMenuItem helpAbout;

    /**
     * Constructor for this menu bar.
     * 
     * @param application
     *            The application
     * @param resourceBundle
     *            The resource bundle
     */
    public AJMenuBar(AJMainGUI application, ResourceBundle resourceBundle) {
	this.application = application;
	this.resourceBundle = resourceBundle;
	initComponents();
    }

    /**
     * This method is called from within the constructor to initialise the form.
     */
    private void initComponents() {

	fileMenu = new JMenu(resourceBundle.getString("AJ.mnuFile.text"));
	fileMenu.setMnemonic(KeyEvent.VK_F);

	fileCreateJournal = new JMenuItem(
		resourceBundle.getString("AJ.cmdCreateJournal.text"));
	fileCreateJournal.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/create_journals_16.png")));
	fileCreateJournal.setMnemonic(KeyEvent.VK_J);
	fileCreateJournal.setAccelerator(KeyStroke.getKeyStroke('J', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	fileCreateJournal.setActionCommand(AJGUIActions.CREATE_JOURNAL.name());
	fileCreateJournal.addActionListener(this);
	fileMenu.add(fileCreateJournal);

	fileOpenJournal = new JMenuItem(
		resourceBundle.getString("AJ.cmdOpenJournal.text"));
	fileOpenJournal.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/open_journals_16.png")));
	fileOpenJournal.setMnemonic(KeyEvent.VK_O);
	fileOpenJournal.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	fileOpenJournal.setActionCommand(AJGUIActions.OPEN_JOURNAL.name());
	fileOpenJournal.addActionListener(this);
	fileOpenJournal.setEnabled(false);
	fileMenu.add(fileOpenJournal);

	fileMenu.addSeparator();

	fileQuit = new JMenuItem(resourceBundle.getString("AJ.mnuQuit.text"));
	fileQuit.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/quit_16.png")));
	fileQuit.setMnemonic(KeyEvent.VK_Q);
	fileQuit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	fileQuit.setActionCommand(AJGUIActions.QUIT.name());
	fileQuit.addActionListener(this);
	fileMenu.add(fileQuit);

	add(fileMenu);

	editMenu = new JMenu(resourceBundle.getString("AJ.mnuEdit.text"));
	editMenu.setMnemonic(KeyEvent.VK_E);
	editPreferences = new JMenuItem(
		resourceBundle.getString("AJ.mnuPreferences.text"));
	editPreferences.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/preferences_16.png")));
	editPreferences.setActionCommand(AJGUIActions.EDIT_PREFERENCES.name());
	editPreferences.setMnemonic(KeyEvent.VK_P);
	editPreferences.setAccelerator(KeyStroke.getKeyStroke('P', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	editPreferences.addActionListener(this);
	editMenu.add(editPreferences);
	add(editMenu);

	helpMenu = new JMenu(resourceBundle.getString("AJ.mnuHelp.text"));
	helpMenu.setMnemonic(KeyEvent.VK_H);

	helpLicense = new JMenuItem(
		resourceBundle.getString("AJ.mnuLicense.text"));
	helpLicense.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/license_16.png")));
	helpLicense.setActionCommand(AJGUIActions.HELP_LICENSE.name());
	helpLicense.setMnemonic(KeyEvent.VK_L);
	helpLicense.setAccelerator(KeyStroke.getKeyStroke('L', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	helpLicense.addActionListener(this);
	helpMenu.add(helpLicense);

	helpAbout = new JMenuItem(resourceBundle.getString("AJ.mnuAbout.text"));
	helpAbout.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/about_16.png")));
	helpAbout.setActionCommand(AJGUIActions.HELP_ABOUT.name());
	helpAbout.setMnemonic(KeyEvent.VK_A);
	helpAbout.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit
		.getDefaultToolkit().getMenuShortcutKeyMask()));
	helpAbout.addActionListener(this);
	helpMenu.add(helpAbout);

	add(helpMenu);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
	// Let's clean the status panel as something else is going to be
	// invoked.
	application.setStatusPanelText(" ");

	String action = ae.getActionCommand();

	if (action.equals(AJGUIActions.CREATE_JOURNAL.name())) {
	    application.createJournals();

	} else if (action.equals(AJGUIActions.OPEN_JOURNAL.name())) {
	    application.openJournals();

	} else if (action.equals(AJGUIActions.QUIT.name())) {
	    application.quit();

	} else if (action.equals(AJGUIActions.EDIT_PREFERENCES.name())) {
	    application.configure();

	} else if (action.equals(AJGUIActions.HELP_LICENSE.name())) {
	    String license = "LICENSE.txt";
	    try {
		LicenseDialog licenseDialog = new LicenseDialog(application,
			resourceBundle, license);
	    } catch (FileNotFoundException e) {
		log.error(
			resourceBundle.getString("AJ.errLicenseNotFound.text"),
			resourceBundle.getString("AJ.errFileNotFound.text"), e);
		JOptionPane.showMessageDialog(application,
			resourceBundle.getString("AJ.errLicenseNotFound.text"),
			resourceBundle.getString("AJ.errFileNotFound.text"),
			JOptionPane.ERROR_MESSAGE);
	    } catch (IOException e) {
		log.error(e, e);
	    }

	} else if (action.equals(AJGUIActions.HELP_ABOUT.name())) {
	    AboutDialog aboutDialog = new AboutDialog(application,
		    resourceBundle);

	} else {
	    log.error(resourceBundle.getString("AJ.errCommandNotFound.text")
		    + action);
	    JOptionPane.showMessageDialog(application,
		    resourceBundle.getString("AJ.errCommandNotFound.text")
			    + action,
		    resourceBundle.getString("AJ.errCommandNotFound.text"),
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
	if (action.equals(AJGUIActions.CREATE_JOURNAL.name())) {
	    fileCreateJournal.setEnabled(enabled);
	} else if (action.equals(AJGUIActions.OPEN_JOURNAL.name())) {
	    fileOpenJournal.setEnabled(enabled);
	} else if (action.equals(AJGUIActions.EDIT_PREFERENCES.name())) {
	    editPreferences.setEnabled(enabled);
	} else {
	    log.error("Unknown action : " + action);
	}
    }

}
