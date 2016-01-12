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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.AJConfig;
import org.astrojournal.gui.dialogs.StatusPanel;
import org.astrojournal.gui.dialogs.WelcomePanel;
import org.astrojournal.gui.menu.AJMenuBar;
import org.astrojournal.logging.JTextPaneAppender;

/**
 * A very minimal graphical user interface for running AstroJournal without
 * scripts.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 10/09/2015
 */
public class AJMainGUI extends JFrame {

    /**
     * The log associated to this class. Note: This should be put in a class
     * AJLogger.
     */
    private static Logger log = LogManager.getLogger(AJMainGUI.class);

    private static final long serialVersionUID = -7217707367091677434L;

    private JCheckBox cbxLatexOutput;
    private JButton btnCreateJournal;
    private JButton btnQuit;
    private JTextPane textPane;
    private JPanel mainPanel;
    private WelcomePanel welcomePanel;
    private JPanel outputPanel;
    private JPanel controlPanel;
    private StatusPanel statusPanel;
    private boolean latexOutput = AJConfig.getInstance().isLatexOutput();
    private AJMainGUIControls commandRunner;
    private AJMenuBar menu = null;

    /**
     * Creates new form NewJFrame
     */
    public AJMainGUI() {
	initComponents();
    }

    /**
     * Clean the text area.
     */
    public void cleanJTextPane() {
	textPane.setText(" ");
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
		setStatusPanelText(AJConfig.BUNDLE
			.getString("AJ.lblFileGenerationinProgressLong.text"));
		cleanJTextPane();
		btnCreateJournal.setEnabled(false);
		menu.setEnabled("create_journal", false);
		menu.setEnabled("preferences", false);
		commandRunner.createJournal(latexOutput);
		btnCreateJournal.setEnabled(true);
		menu.setEnabled("create_journal", true);
		menu.setEnabled("preferences", true);
		return "";
	    }
	};
	// execute the background thread
	worker.execute();
    }

    /**
     * Dispose this application.
     */
    public void closeApplication() {
	dispose();
	System.exit(0);
    }

    /**
     * Set AstroJournal window.
     */
    private void setAJWindow() {
	commandRunner = new AJMainGUIControls(this);
	// Configure AJMainGUI with basic parameters
	setTitle(AJConfig.APPLICATION_NAME + " " + AJConfig.APPLICATION_VERSION);
	setIconImage(new ImageIcon(
		ClassLoader.getSystemResource("graphics/logo/aj_icon_32.png"))
		.getImage());
	setSize(520, 550);
	setMinimumSize(new Dimension(520, 450));
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	setResizable(true);
	getContentPane().setLayout(new BorderLayout());
    }

    /**
     * Set AstroJournal jTextPane. This is the output panel.
     * 
     * @return the jScrolPane containing jTextPane.
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
	// Create the checkbox for printing the Latex output
	cbxLatexOutput = new JCheckBox();
	if (latexOutput) {
	    cbxLatexOutput.setSelected(true);
	}
	cbxLatexOutput.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (cbxLatexOutput.isSelected()) {
		    latexOutput = true;
		} else {
		    latexOutput = false;
		}
	    }
	});

	// Create the button for creating the journals
	btnCreateJournal = new JButton();
	btnCreateJournal.setText(AJConfig.BUNDLE
		.getString("AJ.cmdCreateJournal.text"));
	btnCreateJournal.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/create_journals_16.png")));
	btnCreateJournal.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		createJournals();
	    }
	});
	// Set this button as default. :)
	getRootPane().setDefaultButton(btnCreateJournal);

	// Create the button for closing the application
	btnQuit = new JButton();
	btnQuit.setIcon(new ImageIcon(ClassLoader
		.getSystemResource("graphics/icons/quit_16.png")));
	btnQuit.setText(AJConfig.BUNDLE.getString("AJ.cmdQuit.text"));
	btnQuit.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		closeApplication();
	    }
	});

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
	controlPanel.add(new JLabel(AJConfig.BUNDLE
		.getString("AJ.lblShowLatexOutput.text")));
	controlPanel.add(cbxLatexOutput);
	controlPanel.add(btnCreateJournal);
	controlPanel.add(btnQuit);

	// Setup for the welcome panel
	welcomePanel = new WelcomePanel();

	// Setup for the output panel
	outputPanel = new JPanel(new BorderLayout());
	outputPanel.add(
		new JLabel(AJConfig.BUNDLE.getString("AJ.lblOutput.text")),
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
	menu = new AJMenuBar(this);
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

	// Note Nimbus does not seem to show the vertical scroll bar if there is
	// too much text..
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e) {
	    log.error(e);
	}

	// enable anti-aliased text:
	System.setProperty("awt.useSystemAAFontSettings", "gasp");
	System.setProperty("swing.aatext", "true");

	// invoke the application and make it visible
	java.awt.EventQueue.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		new AJMainGUI().setVisible(true);
	    }
	});

    }

}
