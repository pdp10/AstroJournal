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
 * - Piero Dalle Pezze: Code taken from the class EditPreferenceDialogue.java in 
 * the software BamQC (GPL v3). Code adapted for AstroJournal. This class is no longer a Singleton 
 * and does not contain any configuration data for AstroJournal. It is only a 
 * class to change values in AJConfig using a GUI.
 */
package org.astrojournal.configuration;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.astrojournal.gui.AJMainGUI;

/**
 * A Dialog to allow the viewing and editing of all AstroJournal preferences.
 */
public class PreferencesDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 5832696837018920916L;

    /**
     * The instance of the configurator
     */
    private AJConfig ajConfig = AJConfig.getInstance();

    /**
     * The relative path containing the raw files (observation input folder).
     */
    private JTextField rawReportsFolder = new JTextField();
    /**
     * The AstroJournal files location (the path).
     */
    private JTextField ajFilesLocation = new JTextField();
    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    private JTextField latexReportsFolderByDate = new JTextField();
    /**
     * The name of the folder containing the latex observation files by target
     * (observation output folder).
     */
    private JTextField latexReportsFolderByTarget = new JTextField();
    /**
     * The name of the folder containing the latex observation files by
     * constellation (observation output folder).
     */
    private JTextField latexReportsFolderByConstellation = new JTextField();
    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    private JTextField sglReportsFolderByDate = new JTextField();

    /** True if the application should run quietly */
    private JCheckBox quiet = new JCheckBox();

    /** True if latex output should be printed. */
    private JCheckBox latexOutput = new JCheckBox();

    /** True if the license should be shown at start. */
    private JCheckBox showLicenseAtStart = new JCheckBox();

    /** True if the version of pdflatex. */
    private JCheckBox showPDFLatexVersion = new JCheckBox();

    /** True if the configuration should be shown at start. */
    private JCheckBox showConfigurationAtStart = new JCheckBox();

    /**
     * Instantiates a new edits the preferences dialog.
     * 
     * @param application
     *            the application
     */
    public PreferencesDialog(AJMainGUI application) {
	super(application, AJConfig.BUNDLE.getString("AJ.mnuEdit.text") + " "
		+ AJConfig.BUNDLE.getString("AJ.mnuPreferences.text"));
	initComponents(application);
    }

    /**
     * This method is called from within the constructor to initialise the form.
     * 
     * @param application
     *            the application
     */
    private void initComponents(final AJMainGUI application) {
	setSize(600, 280);
	setLocationRelativeTo(application);
	setModal(true);

	JPanel filePanel = new JPanel();
	filePanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
	filePanel.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();

	// Start text fields
	c.gridx = 0;
	c.gridy = 0;
	c.weightx = 0.1;
	c.weighty = 0.5;
	c.fill = GridBagConstraints.HORIZONTAL;
	JLabel ajFilesLocationLBL = new JLabel(
		AJConfig.BUNDLE.getString("AJ.lblAJFilesLocation.text"));
	ajFilesLocationLBL.setToolTipText(AJConfig.BUNDLE
		.getString("AJ.lblAJFilesLocation.toolTipText"));
	filePanel.add(ajFilesLocationLBL, c);
	c.gridx = 1;
	c.weightx = 0.5;
	ajFilesLocation
		.setText(ajConfig.getFilesLocation().getAbsolutePath());
	ajFilesLocation.setEditable(false);
	filePanel.add(ajFilesLocation, c);
	c.gridx = 2;
	c.weightx = 0.1;
	JButton btnBrowerFilesLocation = new JButton(
		AJConfig.BUNDLE.getString("AJ.cmdBrowse.text"));
	btnBrowerFilesLocation.setActionCommand("aj_files_location");
	btnBrowerFilesLocation.addActionListener(this);
	filePanel.add(btnBrowerFilesLocation, c);

	c.gridx = 0;
	c.gridy++;
	c.weightx = 0.1;
	JLabel inputDir = new JLabel(
		AJConfig.BUNDLE.getString("AJ.lblInpDir.text"));
	inputDir.setToolTipText(AJConfig.BUNDLE
		.getString("AJ.lblInpDir.toolTipText"));
	filePanel.add(inputDir, c);
	c.gridx = 1;
	c.weightx = 0.5;
	rawReportsFolder.setText(ajConfig.getRawReportsFolder());
	filePanel.add(rawReportsFolder, c);
	c.gridx = 2;
	c.weightx = 0.1;

	c.gridx = 0;
	c.gridy++;
	c.weightx = 0.1;
	JLabel outputDirByDate = new JLabel(
		AJConfig.BUNDLE.getString("AJ.lblOutByDateDir.text"));
	outputDirByDate.setToolTipText(AJConfig.BUNDLE
		.getString("AJ.lblOutByDateDir.toolTipText"));
	filePanel.add(outputDirByDate, c);
	c.gridx = 1;
	c.weightx = 0.5;
	latexReportsFolderByDate
		.setText(ajConfig.getLatexReportsFolderByDate());
	filePanel.add(latexReportsFolderByDate, c);
	c.gridx = 2;
	c.weightx = 0.1;

	c.gridx = 0;
	c.gridy++;
	c.weightx = 0.1;
	JLabel outputDirByTarget = new JLabel(
		AJConfig.BUNDLE.getString("AJ.lblOutByTargetDir.text"));
	outputDirByTarget.setToolTipText(AJConfig.BUNDLE
		.getString("AJ.lblOutByTargetDir.toolTipText"));
	filePanel.add(outputDirByTarget, c);
	c.gridx = 1;
	c.weightx = 0.5;
	latexReportsFolderByTarget.setText(ajConfig
		.getLatexReportsFolderByTarget());
	filePanel.add(latexReportsFolderByTarget, c);
	c.gridx = 2;
	c.weightx = 0.1;

	c.gridx = 0;
	c.gridy++;
	c.weightx = 0.1;
	JLabel outputDirByConstellation = new JLabel(
		AJConfig.BUNDLE.getString("AJ.lblOutByConstellationDir.text"));
	outputDirByConstellation.setToolTipText(AJConfig.BUNDLE
		.getString("AJ.lblOutByConstellationDir.toolTipText"));
	filePanel.add(outputDirByConstellation, c);
	c.gridx = 1;
	c.weightx = 0.5;
	latexReportsFolderByConstellation.setText(ajConfig
		.getLatexReportsFolderByConstellation());
	filePanel.add(latexReportsFolderByConstellation, c);
	c.gridx = 2;
	c.weightx = 0.1;

	c.gridx = 0;
	c.gridy++;
	c.weightx = 0.1;
	JLabel outputSGLDirByDate = new JLabel(
		AJConfig.BUNDLE.getString("AJ.lblSGLOutByDateDir.text"));
	outputSGLDirByDate.setToolTipText(AJConfig.BUNDLE
		.getString("AJ.lblSGLOutByDateDir.toolTipText"));
	filePanel.add(outputSGLDirByDate, c);
	c.gridx = 1;
	c.weightx = 0.5;
	sglReportsFolderByDate.setText(ajConfig.getSglReportsFolderByDate());
	filePanel.add(sglReportsFolderByDate, c);
	c.gridx = 2;
	c.weightx = 0.1;

	// End text fields

	// Start boolean checkboxes

	c.gridx = 0;
	c.gridy++;
	c.weightx = 0.1;
	JLabel lblQuiet = new JLabel(
		AJConfig.BUNDLE.getString("AJ.lblQuiet.text"));
	lblQuiet.setToolTipText(AJConfig.BUNDLE
		.getString("AJ.lblQuiet.toolTipText"));
	filePanel.add(lblQuiet, c);
	c.gridx = 1;
	c.weightx = 0.5;
	quiet.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (quiet.isSelected()) {
		    showLicenseAtStart.setEnabled(false);
		    latexOutput.setEnabled(false);
		    showConfigurationAtStart.setEnabled(false);
		    showPDFLatexVersion.setEnabled(false);
		} else {
		    showLicenseAtStart.setEnabled(true);
		    latexOutput.setEnabled(true);
		    showConfigurationAtStart.setEnabled(true);
		    showPDFLatexVersion.setEnabled(true);
		}
	    }
	});
	quiet.setSelected(ajConfig.isQuiet());
	// let's perform its action programmatically
	quiet.doClick();
	quiet.doClick();
	filePanel.add(quiet, c);
	c.gridx = 2;
	c.weightx = 0.1;

	c.gridx = 0;
	c.gridy++;
	c.weightx = 0.1;
	JLabel lblShowLatexOutput = new JLabel(
		AJConfig.BUNDLE.getString("AJ.lblShowLatexOutput.text"));
	lblShowLatexOutput.setToolTipText(AJConfig.BUNDLE
		.getString("AJ.lblShowLatexOutput.toolTipText"));
	latexOutput.setSelected(ajConfig.isShowLatexOutput());
	filePanel.add(lblShowLatexOutput, c);
	c.gridx = 1;
	c.weightx = 0.5;
	filePanel.add(latexOutput, c);
	c.gridx = 2;
	c.weightx = 0.1;

	c.gridx = 0;
	c.gridy++;
	c.weightx = 0.1;
	JLabel lblShowLicenseAtStart = new JLabel(
		AJConfig.BUNDLE.getString("AJ.lblShowLicenseAtStart.text"));
	lblShowLicenseAtStart.setToolTipText(AJConfig.BUNDLE
		.getString("AJ.lblShowLicenseAtStart.toolTipText"));
	showLicenseAtStart.setSelected(ajConfig.isShowLicenseAtStart());
	filePanel.add(lblShowLicenseAtStart, c);
	c.gridx = 1;
	c.weightx = 0.5;
	filePanel.add(showLicenseAtStart, c);
	c.gridx = 2;
	c.weightx = 0.1;

	c.gridx = 0;
	c.gridy++;
	c.weightx = 0.1;
	JLabel lblShowPDFLatexVersion = new JLabel(
		AJConfig.BUNDLE.getString("AJ.lblShowPDFLatexVersion.text"));
	lblShowPDFLatexVersion.setToolTipText(AJConfig.BUNDLE
		.getString("AJ.lblShowPDFLatexVersion.toolTipText"));
	showPDFLatexVersion.setSelected(ajConfig.isShowPDFLatexVersionAtStart());
	filePanel.add(lblShowPDFLatexVersion, c);
	c.gridx = 1;
	c.weightx = 0.5;
	filePanel.add(showPDFLatexVersion, c);
	c.gridx = 2;
	c.weightx = 0.1;

	c.gridx = 0;
	c.gridy++;
	c.weightx = 0.1;
	JLabel lblShowConfigurationAtStart = new JLabel(
		AJConfig.BUNDLE
			.getString("AJ.lblShowConfigurationAtStart.text"));
	lblShowConfigurationAtStart.setToolTipText(AJConfig.BUNDLE
		.getString("AJ.lblShowConfigurationAtStart.toolTipText"));
	showConfigurationAtStart.setSelected(ajConfig
		.isShowConfigurationAtStart());
	filePanel.add(lblShowConfigurationAtStart, c);
	c.gridx = 1;
	c.weightx = 0.5;
	filePanel.add(showConfigurationAtStart, c);
	c.gridx = 2;
	c.weightx = 0.1;

	// End boolean checkboxes

	getContentPane().setLayout(new BorderLayout());
	getContentPane().add(filePanel, BorderLayout.CENTER);

	JPanel buttonPanel = new JPanel();
	JButton btnCancel = new JButton(
		AJConfig.BUNDLE.getString("AJ.cmdCancel.text"));
	btnCancel.setActionCommand("cancel");
	// Set this button as default. :)
	getRootPane().setDefaultButton(btnCancel);
	btnCancel.addActionListener(this);
	buttonPanel.add(btnCancel);

	JButton btnSave = new JButton(
		AJConfig.BUNDLE.getString("AJ.cmdSave.text"));
	btnSave.setActionCommand("save");
	btnSave.addActionListener(this);
	buttonPanel.add(btnSave);

	getContentPane().add(buttonPanel, BorderLayout.SOUTH);

	setVisible(true);
    }

    /**
     * Launches a file browser to select a directory
     * 
     * @param textField
     *            the TextFild from which to take the starting directory
     */
    private void getDir(JTextField textField) {
	JFileChooser chooser = new JFileChooser();
	chooser.setCurrentDirectory(new File(textField.getText()));
	chooser.setDialogTitle(AJConfig.BUNDLE
		.getString("AJ.cmdSelectDirectory.text"));
	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	    textField.setText(chooser.getSelectedFile().getAbsolutePath());
	}
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

	if (action.equals("aj_files_location")) {
	    getDir(ajFilesLocation);
	} else if (action.equals("cancel")) {
	    setVisible(false);
	    dispose();
	} else if (action.equals("save")) {

	    File ajFilesLocationFile = new File(ajFilesLocation.getText());

	    // text fields
	    ajConfig.setFilesLocation(ajFilesLocationFile);
	    ajConfig.setRawReportsFolder(rawReportsFolder.getText());
	    ajConfig.setLatexReportsFolderByDate(latexReportsFolderByDate
		    .getText());
	    ajConfig.setLatexReportsFolderByTarget(latexReportsFolderByTarget
		    .getText());
	    ajConfig.setLatexReportsFolderByConstellation(latexReportsFolderByConstellation
		    .getText());
	    ajConfig.setSglReportsFolderByDate(sglReportsFolderByDate.getText());

	    // combobox fields
	    ajConfig.setQuiet(quiet.isSelected());
	    ajConfig.setShowLatexOutput(latexOutput.isSelected());
	    ajConfig.setShowLicenseAtStart(showLicenseAtStart.isSelected());
	    ajConfig.setPDFLatexVersionAtStart(showPDFLatexVersion.isSelected());
	    ajConfig.setShowConfigurationAtStart(showConfigurationAtStart
		    .isSelected());

	    // Save the configuration
	    ajConfig.saveConfiguration();
	    // prepare the folders for AJ.
	    ajConfig.prepareAJFolders();

	    setVisible(false);
	    dispose();
	}
    }

}
