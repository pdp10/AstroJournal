/*
 * This file is part of AstroJournal.
 * AstroJournal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * AstroJournal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with AstroJournal. If not, see <http://www.gnu.org/licenses/>.
 */
package org.astrojournal.gui.dialogs;

import java.awt.Font;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import org.astrojournal.gui.AJMiniGUI;

/**
 * The Class LicenseDialog shows a text representation of the License used for
 * AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 13 Dec 2015
 */
public class LicenseDialog extends JDialog {

    private static final long serialVersionUID = -8023870968821351252L;

    /** The html pane. */
    private JEditorPane htmlPane;

    /**
     * Instantiates a new license dialog.
     * 
     * @param application
     *            the a
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public LicenseDialog(AJMiniGUI application) throws IOException {
	super(application);
	initComponents(application);
    }

    /**
     * This method is called from within the constructor to initialise the form.
     * 
     * @throws IOException
     */
    private void initComponents(AJMiniGUI application) throws IOException {
	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	setTitle("AstroJournal License...");
	htmlPane = new JEditorPane(new File("LICENSE.txt").toURI().toURL());
	htmlPane.setEditable(false);
	htmlPane.setFont(new Font("Monospaced", Font.PLAIN, 12));
	setContentPane(new JScrollPane(htmlPane));
	setSize(580, 500);
	setLocationRelativeTo(application);
	setVisible(true);
    }
}
