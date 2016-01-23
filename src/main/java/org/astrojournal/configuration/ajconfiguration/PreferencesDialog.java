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
 * - Piero Dalle Pezze: Class creation.
 */
package org.astrojournal.configuration.ajconfiguration;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ConfigurationUtils;
import org.astrojournal.gui.AJMainGUI;

/**
 * A Dialog to allow the viewing and editing of all AstroJournal preferences.
 */
public class PreferencesDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 5832696837018920916L;

    /**
     * The log associated to this class.
     */
    private static Logger log = LogManager.getLogger(PreferencesDialog.class);

    /**
     * The configuration.
     */
    private Configuration config;

    /**
     * The resource bundle.
     */
    private ResourceBundle resourceBundle;

    /**
     * The AstroJournal files location (the path).
     */
    private JTextField ajFilesLocation = new JTextField();

    /**
     * The relative path containing the raw files (observation input folder).
     */
    private JTextField rawReportsFolder = new JTextField();

    /**
     * The folder containing the LaTeX header and footer files.
     */
    private JTextField latexHeaderFooterFolder = new JTextField();

    /**
     * The name of the folder containing the latex observation files by date
     * (observation output folder).
     */
    private JTextField latexReportsFolderByDate = new JTextField();

    /** The name of the main Latex file sorted by date. */
    private JTextField latexReportByDateFilename = new JTextField();

    /** The Latex header with path for astrojournal by date. */
    private JTextField latexHeaderByDateFilename = new JTextField();

    /** The Latex footer with path for astrojournal by date. */
    private JTextField latexFooterByDateFilename = new JTextField();

    /**
     * The name of the folder containing the latex observation files by target
     * (observation output folder).
     */
    private JTextField latexReportsFolderByTarget = new JTextField();

    /** The name of the main Latex file sorted by target. */
    private JTextField latexReportByTargetFilename = new JTextField();

    /** The Latex header with path for astrojournal by target. */
    private JTextField latexHeaderByTargetFilename = new JTextField();

    /** The Latex footer with path for astrojournal by target. */
    private JTextField latexFooterByTargetFilename = new JTextField();

    /**
     * The name of the folder containing the latex observation files by
     * constellation (observation output folder).
     */
    private JTextField latexReportsFolderByConstellation = new JTextField();

    /** The name of the main Latex file sorted by Constellation. */
    private JTextField latexReportByConstellationFilename = new JTextField();

    /** The Latex header with path for astrojournal by constellation. */
    private JTextField latexHeaderByConstellationFilename = new JTextField();

    /** The Latex footer with path for astrojournal by constellation. */
    private JTextField latexFooterByConstellationFilename = new JTextField();

    /**
     * The name of the folder containing the text observation files by date
     * (observation output folder).
     */
    private JTextField sglReportsFolderByDate = new JTextField();

    /** The name of the main SGL file sorted by date. */
    private JTextField sglReportByDateFilename = new JTextField();

    // FLAGS
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
     *            The application
     * @param config
     *            The configuration
     */
    public PreferencesDialog(AJMainGUI application, Configuration config) {
	super(application, config.getResourceBundle().getString(
		"AJ.mnuEdit.text")
		+ " "
		+ config.getResourceBundle()
			.getString("AJ.mnuPreferences.text"));
	this.config = config;
	this.resourceBundle = this.config.getResourceBundle();
	initComponents(application);
    }

    /**
     * This method is called from within the constructor to initialise the form.
     * 
     * @param application
     *            the application
     */
    private void initComponents(final AJMainGUI application) {
	log.debug("Setting of new parameter values.");
	setSize(600, 600);
	setMinimumSize(new Dimension(600, 550));
	setLocationRelativeTo(application);
	setModal(true);

	JPanel panel = new JPanel();
	panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
	panel.setLayout(new GridBagLayout());
	GridBagConstraints constraints = new GridBagConstraints();

	// Start text fields
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.weightx = 0.1;
	constraints.weighty = 0.5;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	JLabel ajFilesLocationLBL = new JLabel(
		resourceBundle.getString("AJ.lblAJFilesLocation.text"));
	ajFilesLocationLBL.setToolTipText(resourceBundle
		.getString("AJ.lblAJFilesLocation.toolTipText"));
	panel.add(ajFilesLocationLBL, constraints);
	constraints.gridx = 1;
	constraints.weightx = 0.5;
	ajFilesLocation.setText(config
		.getProperty(AJPropertyConstants.FILES_LOCATION.getKey()));
	ajFilesLocation.setEditable(false);
	panel.add(ajFilesLocation, constraints);
	constraints.gridx = 2;
	constraints.weightx = 0.1;
	JButton btnBrowerFilesLocation = new JButton(
		resourceBundle.getString("AJ.cmdBrowse.text"));
	btnBrowerFilesLocation.setActionCommand("aj_files_location");
	btnBrowerFilesLocation.addActionListener(this);
	panel.add(btnBrowerFilesLocation, constraints);

	// start text fields
	makeTextFields(panel, constraints);
	// end text fields

	// Start boolean check boxes
	makeCheckBoxes(panel, constraints);
	// Set up the listeners
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
	// let's perform its action programmatically
	quiet.doClick();
	quiet.doClick();
	// End boolean check boxes

	getContentPane().setLayout(new BorderLayout());
	getContentPane().add(panel, BorderLayout.CENTER);

	JPanel buttonPanel = new JPanel();
	JButton btnCancel = new JButton(
		resourceBundle.getString("AJ.cmdCancel.text"));
	btnCancel.setActionCommand("cancel");
	// Set this button as default. :)
	getRootPane().setDefaultButton(btnCancel);
	btnCancel.addActionListener(this);
	buttonPanel.add(btnCancel);

	JButton btnSave = new JButton(
		resourceBundle.getString("AJ.cmdSave.text"));
	btnSave.setActionCommand("save");
	btnSave.addActionListener(this);
	buttonPanel.add(btnSave);

	getContentPane().add(buttonPanel, BorderLayout.SOUTH);

	setVisible(true);
	log.debug("New parameter values set.");
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

	    log.debug("Saving a new configuration.");
	    File ajFilesLocationFile = new File(ajFilesLocation.getText());

	    // text fields
	    System.setProperty(AJPropertyConstants.FILES_LOCATION.getKey(),
		    ajFilesLocationFile.getAbsolutePath());
	    System.setProperty(AJPropertyConstants.RAW_REPORTS_FOLDER.getKey(),
		    rawReportsFolder.getText());
	    System.setProperty(
		    AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER.getKey(),
		    latexHeaderFooterFolder.getText());
	    System.setProperty(
		    AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_DATE.getKey(),
		    latexReportsFolderByDate.getText());
	    System.setProperty(
		    AJPropertyConstants.LATEX_REPORT_BY_DATE_FILENAME.getKey(),
		    latexReportByDateFilename.getText());
	    System.setProperty(
		    AJPropertyConstants.LATEX_HEADER_BY_DATE_FILENAME.getKey(),
		    latexHeaderByDateFilename.getText());
	    System.setProperty(
		    AJPropertyConstants.LATEX_FOOTER_BY_DATE_FILENAME.getKey(),
		    latexFooterByDateFilename.getText());
	    System.setProperty(
		    AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_TARGET.getKey(),
		    latexReportsFolderByTarget.getText());
	    System.setProperty(
		    AJPropertyConstants.LATEX_REPORT_BY_TARGET_FILENAME
			    .getKey(), latexReportByTargetFilename.getText());
	    System.setProperty(
		    AJPropertyConstants.LATEX_HEADER_BY_TARGET_FILENAME
			    .getKey(), latexHeaderByTargetFilename.getText());
	    System.setProperty(
		    AJPropertyConstants.LATEX_FOOTER_BY_TARGET_FILENAME
			    .getKey(), latexFooterByTargetFilename.getText());

	    System.setProperty(
		    AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
			    .getKey(), latexReportsFolderByConstellation
			    .getText());
	    System.setProperty(
		    AJPropertyConstants.LATEX_REPORT_BY_CONSTELLATION_FILENAME
			    .getKey(), latexReportByConstellationFilename
			    .getText());
	    System.setProperty(
		    AJPropertyConstants.LATEX_HEADER_BY_CONSTELLATION_FILENAME
			    .getKey(), latexHeaderByConstellationFilename
			    .getText());
	    System.setProperty(
		    AJPropertyConstants.LATEX_FOOTER_BY_CONSTELLATION_FILENAME
			    .getKey(), latexFooterByConstellationFilename
			    .getText());

	    System.setProperty(
		    AJPropertyConstants.SGL_REPORTS_FOLDER_BY_DATE.getKey(),
		    sglReportsFolderByDate.getText());
	    System.setProperty(
		    AJPropertyConstants.SGL_REPORT_BY_DATE_FILENAME.getKey(),
		    sglReportByDateFilename.getText());

	    // combobox fields
	    System.setProperty(AJPropertyConstants.QUIET.getKey(),
		    String.valueOf(quiet.isSelected()));
	    System.setProperty(AJPropertyConstants.SHOW_LATEX_OUTPUT.getKey(),
		    String.valueOf(latexOutput.isSelected()));
	    System.setProperty(
		    AJPropertyConstants.SHOW_LICENSE_AT_START.getKey(),
		    String.valueOf(showLicenseAtStart.isSelected()));
	    System.setProperty(
		    AJPropertyConstants.SHOW_PDFLATEX_VERSION_AT_START.getKey(),
		    String.valueOf(showPDFLatexVersion.isSelected()));
	    System.setProperty(
		    AJPropertyConstants.SHOW_CONFIGURATION_AT_START.getKey(),
		    String.valueOf(showConfigurationAtStart.isSelected()));

	    // Load the properties
	    config.loadSystemProperties();

	    if (config instanceof AJConfiguration) {
		// Save the configuration
		((AJConfiguration) config).saveProperties();
	    }

	    // prepare the folders.
	    ConfigurationUtils configUtils = config.getConfigurationUtils();
	    configUtils.prepareFolders(config);

	    log.debug("New configuration saved.");

	    setVisible(false);
	    dispose();
	}
    }

    /**
     * Return the configuration
     * 
     * @return the configuration
     */
    public Configuration getConfiguration() {
	return config;
    }

    /**
     * Set a new configuration
     * 
     * @param config
     *            the config to set
     */
    public void setConfiguration(Configuration config) {
	this.config = config;
    }

    /**
     * Add a set of labels and jtextfields to the panel.
     * 
     * @param panel
     * @param constraints
     */
    private void makeTextFields(JPanel panel, GridBagConstraints constraints) {
	addStringEntry(panel, constraints, "AJ.lblInpDir.text",
		"AJ.lblInpDir.toolTipText", rawReportsFolder,
		AJPropertyConstants.RAW_REPORTS_FOLDER.getKey());
	addStringEntry(panel, constraints, "AJ.lblLatexHeaderFolderDir.text",
		"AJ.lblLatexHeaderFolderDir.toolTipText",
		latexHeaderFooterFolder,
		AJPropertyConstants.LATEX_HEADER_FOOTER_FOLDER.getKey());

	// LATEX REPORT BY DATE
	addStringEntry(panel, constraints, "AJ.lblOutByDateDir.text",
		"AJ.lblOutByDateDir.toolTipText", latexReportsFolderByDate,
		AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_DATE.getKey());
	addStringEntry(panel, constraints, "AJ.lblOutByDateFile.text",
		"AJ.lblOutByDateFile.toolTipText", latexReportByDateFilename,
		AJPropertyConstants.LATEX_REPORT_BY_DATE_FILENAME.getKey());
	addStringEntry(panel, constraints, "AJ.lblHeaderByDateFile.text",
		"AJ.lblHeaderByDateFile.toolTipText",
		latexHeaderByDateFilename,
		AJPropertyConstants.LATEX_HEADER_BY_DATE_FILENAME.getKey());
	addStringEntry(panel, constraints, "AJ.lblFooterByDateFile.text",
		"AJ.lblFooterByDateFile.toolTipText",
		latexFooterByDateFilename,
		AJPropertyConstants.LATEX_FOOTER_BY_DATE_FILENAME.getKey());

	// LATEX REPORT BY TARGET
	addStringEntry(panel, constraints, "AJ.lblOutByTargetDir.text",
		"AJ.lblOutByTargetDir.toolTipText", latexReportsFolderByTarget,
		AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_TARGET.getKey());
	addStringEntry(panel, constraints, "AJ.lblOutByTargetFile.text",
		"AJ.lblOutByTargetFile.toolTipText",
		latexReportByTargetFilename,
		AJPropertyConstants.LATEX_REPORT_BY_TARGET_FILENAME.getKey());
	addStringEntry(panel, constraints, "AJ.lblHeaderByTargetFile.text",
		"AJ.lblHeaderByTargetFile.toolTipText",
		latexHeaderByTargetFilename,
		AJPropertyConstants.LATEX_HEADER_BY_TARGET_FILENAME.getKey());
	addStringEntry(panel, constraints, "AJ.lblFooterByTargetFile.text",
		"AJ.lblFooterByTargetFile.toolTipText",
		latexFooterByTargetFilename,
		AJPropertyConstants.LATEX_FOOTER_BY_TARGET_FILENAME.getKey());

	// LATEX REPORT BY CONSTELLATION
	addStringEntry(panel, constraints, "AJ.lblOutByConstellationDir.text",
		"AJ.lblOutByConstellationDir.toolTipText",
		latexReportsFolderByConstellation,
		AJPropertyConstants.LATEX_REPORTS_FOLDER_BY_CONSTELLATION
			.getKey());
	addStringEntry(panel, constraints, "AJ.lblOutByConstellationFile.text",
		"AJ.lblOutByConstellationFile.toolTipText",
		latexReportByConstellationFilename,
		AJPropertyConstants.LATEX_REPORT_BY_CONSTELLATION_FILENAME
			.getKey());
	addStringEntry(panel, constraints,
		"AJ.lblHeaderByConstellationFile.text",
		"AJ.lblHeaderByConstellationFile.toolTipText",
		latexHeaderByConstellationFilename,
		AJPropertyConstants.LATEX_HEADER_BY_CONSTELLATION_FILENAME
			.getKey());
	addStringEntry(panel, constraints,
		"AJ.lblFooterByConstellationFile.text",
		"AJ.lblFooterByConstellationFile.toolTipText",
		latexFooterByConstellationFilename,
		AJPropertyConstants.LATEX_FOOTER_BY_CONSTELLATION_FILENAME
			.getKey());

	// SGL REPORT BY DATE
	addStringEntry(panel, constraints, "AJ.lblSGLOutByDateDir.text",
		"AJ.lblSGLOutByDateDir.toolTipText", sglReportsFolderByDate,
		AJPropertyConstants.SGL_REPORTS_FOLDER_BY_DATE.getKey());
	addStringEntry(panel, constraints, "AJ.lblSGLOutByDateFile.text",
		"AJ.lblSGLOutByDateFile.toolTipText", sglReportByDateFilename,
		AJPropertyConstants.SGL_REPORT_BY_DATE_FILENAME.getKey());
    }

    /**
     * Add a label and a jtextfield to the panel.
     * 
     * @param panel
     * @param constraints
     * @param label
     * @param labelTooltip
     * @param jTextField
     * @param jTextFieldText
     */
    private void addStringEntry(JPanel panel, GridBagConstraints constraints,
	    String label, String labelTooltip, JTextField jTextField,
	    String jTextFieldText) {
	constraints.gridx = 0;
	constraints.gridy++;
	constraints.weightx = 0.1;
	JLabel lbl = new JLabel(resourceBundle.getString(label));
	lbl.setToolTipText(resourceBundle.getString(labelTooltip));
	panel.add(lbl, constraints);
	constraints.gridx = 1;
	constraints.weightx = 0.5;
	jTextField.setText(config.getProperty(jTextFieldText));
	panel.add(jTextField, constraints);
	constraints.gridx = 2;
	constraints.weightx = 0.1;
    }

    /**
     * Add a set of labels and checkboxes to the panel.
     * 
     * @param panel
     * @param constraints
     */
    private void makeCheckBoxes(JPanel panel, GridBagConstraints constraints) {

	addCheckBoxEntry(panel, constraints, "AJ.lblQuiet.text",
		"AJ.lblQuiet.toolTipText", quiet,
		AJPropertyConstants.QUIET.getKey());

	addCheckBoxEntry(panel, constraints, "AJ.lblShowLatexOutput.text",
		"AJ.lblShowLatexOutput.toolTipText", latexOutput,
		AJPropertyConstants.SHOW_LATEX_OUTPUT.getKey());

	addCheckBoxEntry(panel, constraints, "AJ.lblShowLicenseAtStart.text",
		"AJ.lblShowLicenseAtStart.toolTipText", showLicenseAtStart,
		AJPropertyConstants.SHOW_LICENSE_AT_START.getKey());

	addCheckBoxEntry(panel, constraints, "AJ.lblShowPDFLatexVersion.text",
		"AJ.lblShowPDFLatexVersion.toolTipText", showPDFLatexVersion,
		AJPropertyConstants.SHOW_PDFLATEX_VERSION_AT_START.getKey());

	addCheckBoxEntry(panel, constraints,
		"AJ.lblShowConfigurationAtStart.text",
		"AJ.lblShowConfigurationAtStart.toolTipText",
		showConfigurationAtStart,
		AJPropertyConstants.SHOW_CONFIGURATION_AT_START.getKey());
    }

    /**
     * Add a label and a jcheckbox to the panel.
     * 
     * @param panel
     * @param constraints
     * @param label
     * @param labelTooltip
     * @param jCheckBox
     * @param selected
     */
    private void addCheckBoxEntry(JPanel panel, GridBagConstraints constraints,
	    String label, String labelTooltip, JCheckBox jCheckBox,
	    String selected) {
	constraints.gridx = 0;
	constraints.gridy++;
	constraints.weightx = 0.1;
	JLabel lbl = new JLabel(resourceBundle.getString(label));
	lbl.setToolTipText(resourceBundle.getString(labelTooltip));
	jCheckBox
		.setSelected(Boolean.parseBoolean(config.getProperty(selected)));
	panel.add(lbl, constraints);
	constraints.gridx = 1;
	constraints.weightx = 0.5;
	panel.add(jCheckBox, constraints);
	constraints.gridx = 2;
	constraints.weightx = 0.1;
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
	chooser.setDialogTitle(resourceBundle
		.getString("AJ.cmdSelectDirectory.text"));
	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	    textField.setText(chooser.getSelectedFile().getAbsolutePath());
	}
    }

}
