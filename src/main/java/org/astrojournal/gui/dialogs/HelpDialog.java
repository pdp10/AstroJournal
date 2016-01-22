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
package org.astrojournal.gui.dialogs;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.help.HelpSet;
import javax.help.JHelp;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.gui.AJMainGUI;

/**
 * A simple Help Dialog.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 9 Jan 2016
 */
public class HelpDialog {

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(HelpDialog.class);

    private JHelp helpViewer = null;
    private JFrame frame;

    /**
     * A simple HelpDialog
     * 
     * @param application
     *            The application
     * @param resourceBundle
     *            The resource bundle
     */
    public HelpDialog(AJMainGUI application, ResourceBundle resourceBundle) {
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
	try {
	    // Initialisation of JavaHelp
	    final String helpHS = "help/helpset.hs";
	    ClassLoader classLoader = this.getClass().getClassLoader();
	    URL hsURL = HelpSet.findHelpSet(classLoader, helpHS);
	    helpViewer = new JHelp(new HelpSet(classLoader, hsURL));
	    helpViewer.setCurrentID("Index");
	} catch (Exception e) {
	    log.error(resourceBundle.getString("AJ.errHelpIndexNotFound.text"),
		    e);
	    JOptionPane.showMessageDialog(application,
		    resourceBundle.getString("AJ.errHelpIndexNotFound.text"),
		    resourceBundle.getString("AJ.errFileNotFound.text"),
		    JOptionPane.ERROR_MESSAGE);
	    return;
	}

	frame = new JFrame();
	frame.setSize(550, 450);
	frame.setTitle(resourceBundle.getString("AJ.mnuHelpContents.text"));
	frame.setIconImage(new ImageIcon(ClassLoader
		.getSystemResource("graphics/logo/aj_icon_32.png")).getImage());
	frame.getContentPane().add(helpViewer);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.setVisible(true);

    }

    /**
     * A simple menu for testing this help.
     * 
     * @param args
     */
    public static void main(String args[]) {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle(
		"locale.aj", new Locale("en", "GB"));
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception ex) {
	    log.warn(ex, ex);
	}

	java.awt.EventQueue.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		HelpDialog hd = new HelpDialog(null, resourceBundle);
	    }
	});
    }

}
