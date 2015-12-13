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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
     * Instantiates a new about dialog.
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
	setTitle("About AstroJournal");
	Container cont = getContentPane();
	cont.setLayout(new BorderLayout());

	add(new AJTitlePanel(), BorderLayout.CENTER);

	JPanel buttonPanel = new JPanel();

	JButton closeButton = new JButton("Close");
	getRootPane().setDefaultButton(closeButton);
	closeButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		setVisible(false);
		dispose();
	    }
	});
	buttonPanel.add(closeButton);

	cont.add(buttonPanel, BorderLayout.SOUTH);

	setSize(450, 200);
	setLocationRelativeTo(application);
	setResizable(false);
	setVisible(true);
    }
}
