/*
 * This file is part of AstroJournal.
 *
 * AstroJournal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AstroJournal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AstroJournal.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.astrojournal.configuration;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.astrojournal.gui.AJMainGUI;

/**
 * A Dialog to allow the viewing and editing of all BamQC preferences.
 */
public class PreferencesDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 5832696837018920916L;

    /** The relative path containing the raw files (observation input folder). */
    private JTextField rawReportsFolder;
    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    private JTextField latexReportsFolderByDate;
    /**
     * The name of the folder containing the latex observation files by target
     * (observation output folder).
     */
    private JTextField latexReportsFolderByTarget;
    /**
     * The name of the folder containing the latex observation files by
     * constellation (observation output folder).
     */
    private JTextField latexReportsFolderByConstellation;
    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    private JTextField sglReportsFolderByDate;

    /**
     * Instantiates a new edits the preferences dialog.
     * 
     * @param application
     *            the application
     */
    public PreferencesDialog(AJMainGUI application) {
	super(application, "Edit Preferences...");
	initComponents(application);
    }

    /**
     * This method is called from within the constructor to initialise the form.
     * 
     * @param application
     *            the application
     */
    private void initComponents(AJMainGUI application) {
	setSize(600, 280);
	setLocationRelativeTo(application);
	setModal(true);

	JPanel filePanel = new JPanel();
	filePanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
	filePanel.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 0;
	c.weightx = 0.1;
	c.weighty = 0.5;
	c.fill = GridBagConstraints.HORIZONTAL;
	filePanel.add(new JLabel("Input Folder: "), c);
	c.gridx = 1;
	c.weightx = 0.5;
	rawReportsFolder = new JTextField();
	rawReportsFolder.setText(AJConfig.getInstance().getRawReportsFolder());
	filePanel.add(rawReportsFolder, c);
	c.gridx = 2;
	c.weightx = 0.1;

	c.gridx = 0;
	c.gridy++;
	c.weightx = 0.1;
	filePanel.add(new JLabel("Folder for Latex Reports by Date:"), c);
	c.gridx = 1;
	c.weightx = 0.5;
	latexReportsFolderByDate = new JTextField();
	latexReportsFolderByDate.setText(AJConfig.getInstance()
		.getLatexReportsFolderByDate());
	filePanel.add(latexReportsFolderByDate, c);
	c.gridx = 2;
	c.weightx = 0.1;

	c.gridx = 0;
	c.gridy++;
	c.weightx = 0.1;
	filePanel.add(new JLabel("Folder for Latex Reports by Target:"), c);
	c.gridx = 1;
	c.weightx = 0.5;
	latexReportsFolderByTarget = new JTextField();
	latexReportsFolderByTarget.setText(AJConfig.getInstance()
		.getLatexReportsFolderByTarget());
	filePanel.add(latexReportsFolderByTarget, c);
	c.gridx = 2;
	c.weightx = 0.1;

	c.gridx = 0;
	c.gridy++;
	c.weightx = 0.1;
	filePanel.add(new JLabel("Folder for Latex Reports by Constellation:"),
		c);
	c.gridx = 1;
	c.weightx = 0.5;
	latexReportsFolderByConstellation = new JTextField();
	latexReportsFolderByConstellation.setText(AJConfig.getInstance()
		.getLatexReportsFolderByConstellation());
	filePanel.add(latexReportsFolderByConstellation, c);
	c.gridx = 2;
	c.weightx = 0.1;

	c.gridx = 0;
	c.gridy++;
	c.weightx = 0.1;
	filePanel.add(new JLabel("Folder for SGL Reports by Date:"), c);
	c.gridx = 1;
	c.weightx = 0.5;
	sglReportsFolderByDate = new JTextField();
	sglReportsFolderByDate.setText(AJConfig.getInstance()
		.getLatexReportsFolderByDate());
	filePanel.add(sglReportsFolderByDate, c);
	c.gridx = 2;
	c.weightx = 0.1;

	getContentPane().setLayout(new BorderLayout());
	getContentPane().add(filePanel, BorderLayout.CENTER);

	JPanel buttonPanel = new JPanel();
	JButton cancelButton = new JButton("Cancel");
	cancelButton.setActionCommand("cancel");
	cancelButton.addActionListener(this);
	buttonPanel.add(cancelButton);

	JButton okButton = new JButton("Save");
	okButton.setActionCommand("ok");
	okButton.addActionListener(this);
	buttonPanel.add(okButton);

	getContentPane().add(buttonPanel, BorderLayout.SOUTH);

	setVisible(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
	String action = ae.getActionCommand();

	if (action.equals("cancel")) {
	    setVisible(false);
	    dispose();
	} else if (action.equals("ok")) {
	    AJConfig config = AJConfig.getInstance();
	    config.setRawReportsFolder(rawReportsFolder.getText());
	    config.setLatexReportsFolderByDate(latexReportsFolderByDate
		    .getText());
	    config.setLatexReportsFolderByTarget(latexReportsFolderByTarget
		    .getText());
	    config.setLatexReportsFolderByConstellation(latexReportsFolderByConstellation
		    .getText());
	    config.setSglReportsFolderByDate(sglReportsFolderByDate.getText());
	    try {
		config.savePreferences();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    setVisible(false);
	    dispose();
	}
    }

}
