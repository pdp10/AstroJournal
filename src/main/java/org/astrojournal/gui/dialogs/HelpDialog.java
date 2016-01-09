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

import javax.help.HelpSet;
import javax.help.JHelp;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.astrojournal.configuration.AJConfig;
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

    private JHelp helpViewer = null;
    private JFrame frame;

    /**
     * A simple HelpDialog
     * 
     * @param application
     */
    public HelpDialog(AJMainGUI application) {
	try {
	    // Initialisation of JavaHelp
	    final String helpHS = "help/helpset.hs";
	    ClassLoader classLoader = this.getClass().getClassLoader();
	    URL hsURL = HelpSet.findHelpSet(classLoader, helpHS);
	    helpViewer = new JHelp(new HelpSet(classLoader, hsURL));
	    helpViewer.setCurrentID("Index");
	} catch (Exception e) {
	    JOptionPane.showMessageDialog(application,
		    AJConfig.BUNDLE.getString("AJ.errHelpIndexNotFound.text"),
		    AJConfig.BUNDLE.getString("AJ.errFileNotFound.text"),
		    JOptionPane.ERROR_MESSAGE);
	    return;
	}

	frame = new JFrame();
	frame.setSize(550, 450);
	frame.setIconImage(new ImageIcon(ClassLoader
		.getSystemResource("graphics/logo/aj_icon_32.png")).getImage());
	frame.getContentPane().add(helpViewer);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.setVisible(true);

    }

    public static void main(String args[]) {
	java.awt.EventQueue.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		HelpDialog hd = new HelpDialog(null);
	    }
	});
    }

}
