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
package org.astrojournal.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.AJMainControls;
import org.astrojournal.AJMetaInfo;
import org.astrojournal.configuration.Configuration;
import org.astrojournal.configuration.ajconfiguration.AJPropertyConstants;
import org.astrojournal.configuration.ajconfiguration.PreferencesDialog;
import org.astrojournal.generator.Generator;
import org.astrojournal.gui.dialogs.StatusPanel;
import org.astrojournal.gui.dialogs.WelcomePanel;
import org.astrojournal.gui.menu.AJMenuBar;
import org.astrojournal.logging.JTextPaneAppender;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A very minimal graphical user interface for running AstroJournal without
 * scripts.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public class AJMainGUI extends JFrame implements ActionListener {

    /**
     * The log associated to this class.
     */
    private static Logger log = LogManager.getLogger(AJMainGUI.class);

    private static final long serialVersionUID = -7217707367091677434L;

    /**
     * The application configuration.
     */
    private Configuration config;
    private ResourceBundle resourceBundle;
    private Generator generator;

    private JButton btnCreateJournal;
    private JButton btnOpenJournal;
    private JButton btnQuit;
    private JTextPane textPane;
    private JPanel mainPanel;
    private WelcomePanel welcomePanel;
    private JPanel outputPanel;
    private JPanel controlPanel;
    private StatusPanel statusPanel;
    private AJMenuBar menu = null;

    /** The controller for this application */
    private AJMainControls ajMainControls;

    /**
     * Starts a GUI for AstroJournal.
     * 
     * @param generator
     *            The generator.
     * @param config
     *            The application configuration.
     */
    public AJMainGUI(Generator generator, Configuration config) {
	this.generator = generator;
	this.config = config;
	this.resourceBundle = config.getResourceBundle();
	this.generator.setConfiguration(this.config);
	ajMainControls = new AJMainGUIControls(this, generator);
	initComponents();
    }

    /**
     * Clean the text area.
     */
    // TODO QUESTION. This somehow / sometimes freeze the application..
    public void cleanJTextPane() {
	textPane.setText("");
    }

    /**
     * Set the text for the status panel.
     * 
     * @param str
     *            the text to set
     */
    public void setStatusPanelText(String str) {
	statusPanel.setText(str);
    }

    /**
     * Create the astro journals.
     */
    public void createJournals() {

	if (mainPanel.getComponent(0) instanceof WelcomePanel) {
	    // Replace the welcome panel with the output panel
	    remove(mainPanel);
	    mainPanel.remove(welcomePanel);
	    mainPanel.add(outputPanel, BorderLayout.CENTER);
	    add(mainPanel);
	}

	// define a SwingWorker to run in background
	// In this way the output is printed gradually as it is
	// generated.
	SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
	    @Override
	    public String doInBackground() {
		setStatusPanelText(resourceBundle
			.getString("AJ.lblFileGenerationinProgressLong.text"));
		cleanJTextPane();
		btnCreateJournal.setEnabled(false);
		btnOpenJournal.setEnabled(false);
		menu.setEnabled(AJGUIActions.CREATE_JOURNAL.name(), false);
		menu.setEnabled(AJGUIActions.OPEN_JOURNAL.name(), false);
		menu.setEnabled(AJGUIActions.EDIT_PREFERENCES.name(), false);
		if (!ajMainControls.createJournal()) {
		    setStatusPanelText(resourceBundle
			    .getString("AJ.errPDFLatexShort.text"));
		} else {
		    btnOpenJournal.setEnabled(true);
		    menu.setEnabled(AJGUIActions.OPEN_JOURNAL.name(), true);
		}
		btnCreateJournal.setEnabled(true);
		menu.setEnabled(AJGUIActions.CREATE_JOURNAL.name(), true);
		menu.setEnabled(AJGUIActions.EDIT_PREFERENCES.name(), true);
		return "";
	    }
	};
	// execute the background thread
	worker.execute();
    }

    /**
     * Visualise the astro journals.
     */
    public void openJournals() {
	ArrayList<File> files = new ArrayList<File>();
	File journalByTarget = new File(
		config.getProperty(AJPropertyConstants.FILES_LOCATION.getKey())
			+ File.separator
			+ config.getProperty(
				AJPropertyConstants.LATEX_REPORT_BY_TARGET_FILENAME
					.getKey()).replace(".tex", ".pdf"));
	files.add(journalByTarget);
	File journalByConstellation = new File(
		config.getProperty(AJPropertyConstants.FILES_LOCATION.getKey())
			+ File.separator
			+ config.getProperty(
				AJPropertyConstants.LATEX_REPORT_BY_CONSTELLATION_FILENAME
					.getKey()).replace(".tex", ".pdf"));
	files.add(journalByConstellation);
	File journalByDate = new File(
		config.getProperty(AJPropertyConstants.FILES_LOCATION.getKey())
			+ File.separator
			+ config.getProperty(
				AJPropertyConstants.LATEX_REPORT_BY_DATE_FILENAME
					.getKey()).replace(".tex", ".pdf"));
	files.add(journalByDate);
	File journalByDateTXT = new File(
		config.getProperty(AJPropertyConstants.FILES_LOCATION.getKey())
			+ File.separator
			+ config.getProperty(AJPropertyConstants.TXT_REPORT_BY_DATE_FILENAME
				.getKey()));
	files.add(journalByDateTXT);

	for (File file : files) {
	    if (file.exists()) {
		try {
		    if (Desktop.isDesktopSupported()) {
			Desktop.getDesktop().open(file);
			log.debug("Open file : " + file.getAbsolutePath());
		    } else {
			log.error("Awt Desktop is not supported!");
		    }
		} catch (Exception ex) {
		    log.warn(ex);
		    log.debug(ex, ex);
		}
	    } else {
		log.error("File is not exists!");
	    }
	}
    }

    /**
     * Quit the application.
     */
    public void quit() {
	dispose();
	System.exit(0);
    }

    /**
     * Configure the application.
     */
    public void configure() {
	PreferencesDialog preferencesDialog = new PreferencesDialog(this,
		config);
	config = preferencesDialog.getConfiguration();
	generator.setConfiguration(config);
	ajMainControls = new AJMainGUIControls(this, generator);
    }

    /**
     * Set AstroJournal window.
     */
    private void setAJWindow() {
	// Configure AJMainGUI with basic parameters
	setTitle(AJMetaInfo.NAME.getInfo() + " " + AJMetaInfo.VERSION.getInfo());
	setIconImage(new ImageIcon(
		ClassLoader.getSystemResource("graphics/logo/aj_icon_32.png"))
		.getImage());
	setSize(550, 580);
	setMinimumSize(new Dimension(520, 450));
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	setResizable(true);
	getContentPane().setLayout(new BorderLayout());
    }

    /**
     * Set AstroJournal jTextPane. This is the output panel.
     * 
     * @return the jScrollPane containing jTextPane.
     */
    private JScrollPane setJTextPane() {
	// Create the text area containing the program text output
	textPane = new JTextPane();
	// Move the JScrollPane to the bottom automatically.
	DefaultCaret caret = (DefaultCaret) textPane.getCaret();
	caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	textPane.setEditable(false);

	// Now attach the log appender necessary for redirecting the log
	// messages into the JTextPane
	JTextPaneAppender.addJTextPane(textPane);

	// Let's add a scroll pane
	return new JScrollPane(textPane);
    }

    /**
     * Set AstroJournal control buttons. A small keyboard.
     */
    private void setControlButtons() {

	// Create the button for creating the journals
	btnCreateJournal = new JButton();
	btnCreateJournal.setText(resourceBundle
		.getString("AJ.cmdCreateJournal.text"));
	btnCreateJournal.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/create_journals_16.png")));
	btnCreateJournal.setActionCommand(AJGUIActions.CREATE_JOURNAL.name());
	btnCreateJournal.addActionListener(this);
	// Set this button as default. :)
	getRootPane().setDefaultButton(btnCreateJournal);

	// Create the button for opening the journals
	btnOpenJournal = new JButton();
	btnOpenJournal.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/open_journals_16.png")));
	btnOpenJournal.setText(resourceBundle
		.getString("AJ.cmdOpenJournal.text"));
	btnOpenJournal.setActionCommand(AJGUIActions.OPEN_JOURNAL.name());
	btnOpenJournal.addActionListener(this);
	btnOpenJournal.setEnabled(false);

	// Create the button for closing the application
	btnQuit = new JButton();
	btnQuit.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/quit_16.png")));
	btnQuit.setText(resourceBundle.getString("AJ.cmdQuit.text"));
	btnQuit.setActionCommand(AJGUIActions.QUIT.name());
	btnQuit.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
	String action = ae.getActionCommand();
	if (action.equals(AJGUIActions.CREATE_JOURNAL.name())) {
	    createJournals();
	} else if (action.equals(AJGUIActions.OPEN_JOURNAL.name())) {
	    openJournals();
	} else if (action.equals(AJGUIActions.QUIT.name())) {
	    quit();
	} else {
	    log.error(resourceBundle.getString("AJ.errCommandNotFound.text")
		    + action);
	    JOptionPane.showMessageDialog(this,
		    resourceBundle.getString("AJ.errCommandNotFound.text")
			    + action,
		    resourceBundle.getString("AJ.errCommandNotFound.text"),
		    JOptionPane.ERROR_MESSAGE);
	}
    }

    /**
     * Set AstroJournal's panels.
     */
    private void setAJPanels() {
	setControlButtons();

	// Create the status bar
	statusPanel = new StatusPanel();

	JScrollPane scrollPane = setJTextPane();

	// Setup for the control panel
	// Create the control panel containing the button and the checkbox
	controlPanel = new JPanel();
	controlPanel.add(btnCreateJournal);
	controlPanel.add(btnOpenJournal);
	controlPanel.add(btnQuit);

	// Setup for the welcome panel
	welcomePanel = new WelcomePanel(resourceBundle);

	// Setup for the output panel
	outputPanel = new JPanel(new BorderLayout());
	outputPanel.add(
		new JLabel(resourceBundle.getString("AJ.lblOutput.text")),
		BorderLayout.NORTH);
	outputPanel.add(scrollPane, BorderLayout.CENTER);

	// Add welcome panel and control panel inside the main panel
	mainPanel = new JPanel(new BorderLayout());
	mainPanel.setPreferredSize(getContentPane().getPreferredSize());
	mainPanel.add(welcomePanel, BorderLayout.CENTER);
	mainPanel.add(controlPanel, BorderLayout.SOUTH);
    }

    /**
     * This method is called from within the constructor to initialise the form.
     */
    private void initComponents() {

	setAJWindow();

	// set the menu bar
	menu = new AJMenuBar(this, resourceBundle);
	setJMenuBar(menu);

	setAJPanels();

	// Add the main panel and the status panel to the frame.
	add(mainPanel, BorderLayout.CENTER);
	add(statusPanel, BorderLayout.SOUTH);
    }

    /**
     * A simple main to start AJMiniGUI.
     * 
     * @param args
     *            The command line arguments
     */
    public static void main(String args[]) {

	// Initialise dependency injection with Spring
	ApplicationContext context = new ClassPathXmlApplicationContext(
		"META-INF/aj_spring_default_context.xml");
	BeanFactory factory = context;
	final Configuration config = (Configuration) factory
		.getBean("configuration");
	final Generator generator = (Generator) factory.getBean("generator");

	// Note Nimbus does not seem to show the vertical scroll bar if there is
	// too much text..
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e) {
	    log.error(e, e);
	}

	// enable anti-aliased text:
	System.setProperty("awt.useSystemAAFontSettings", "gasp");
	System.setProperty("swing.aatext", "true");

	// invoke the application and make it visible
	java.awt.EventQueue.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		new AJMainGUI(generator, config).setVisible(true);
	    }
	});

    }

}
