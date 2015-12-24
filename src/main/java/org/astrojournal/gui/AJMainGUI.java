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
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;

import org.astrojournal.configuration.AJConfig;
import org.astrojournal.gui.dialogs.StatusPanel;
import org.astrojournal.gui.menu.AJMenuBar;
import org.astrojournal.utilities.RedirectStreamsToAJTextArea;

/**
 * A very minimal graphical user interface for running AstroJournal without
 * scripts.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 10/09/2015
 */
public class AJMainGUI extends JFrame {

    private static final long serialVersionUID = -7217707367091677434L;

    private JCheckBox cbxLatexOutput;
    private JButton btnCreateJournal;
    private JButton btnClose;
    private JTextArea textArea;
    private StatusPanel statusPanel;
    private boolean latexOutput = AJConfig.getInstance().isLatexOutput();
    private AJMainGUIControls commandRunner;
    private AJMenuBar menu = null;
    // redirect the console streams to AJTextArea
    private RedirectStreamsToAJTextArea redirect2TextArea;

    /**
     * Creates new form NewJFrame
     */
    public AJMainGUI() {
	initComponents();
    }

    /**
     * Clean the text area.
     */
    public void cleanTextArea() {
	textArea.setText(" ");
    }

    /**
     * Append text to the text area.
     * 
     * @param str
     *            the text to append
     */
    public void appendTextToTextArea(final String str) {
	textArea.append(str);
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
	// define a SwingWorker to run in background
	SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
	    @Override
	    public String doInBackground() {
		// In this way the output is printed gradually as it is
		// generated.
		cleanTextArea();
		commandRunner.createJournal(latexOutput);
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
	try {
	    redirect2TextArea.close();
	} catch (IOException e) {
	}
	dispose();
	System.exit(0);
    }

    /**
     * This method is called from within the constructor to initialise the form.
     */
    private void initComponents() {

	redirect2TextArea = new RedirectStreamsToAJTextArea(this);

	commandRunner = new AJMainGUIControls(this);

	// Configure AJMiniGUI with basic parameters
	setTitle(
		AJConfig.APPLICATION_NAME + " " + AJConfig.APPLICATION_VERSION);
	setIconImage(new ImageIcon(
		ClassLoader.getSystemResource("graphics/aj_icon_32.png"))
			.getImage());
	setSize(600, 600);
	setMinimumSize(new Dimension(480, 300));
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	setResizable(true);
	getContentPane().setLayout(new BorderLayout());

	// set the menu bar
	menu = new AJMenuBar(this);
	setJMenuBar(menu);

	// Create the status bar
	statusPanel = new StatusPanel();

	// Create the text area containing the program text output
	textArea = new JTextArea();
	// Move the JScrollPane to the bottom automatically.
	DefaultCaret caret = (DefaultCaret) textArea.getCaret();
	caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	textArea.setEditable(false);
	textArea.setLineWrap(true);
	textArea.setWrapStyleWord(true);
	JScrollPane scrollPane = new JScrollPane(textArea);

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
	btnCreateJournal
		.setText(AJConfig.BUNDLE.getString("AJ.cmdCreateJournal.text"));
	btnCreateJournal.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		createJournals();
	    }
	});
	// Set this button as default. :)
	getRootPane().setDefaultButton(btnCreateJournal);

	// Create the button for closing the application
	btnClose = new JButton();
	btnClose.setText(AJConfig.BUNDLE.getString("AJ.cmdClose.text"));
	btnClose.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		closeApplication();
	    }
	});

	// Create the control panel containing the button and the checkbox
	JPanel controlPanel = new JPanel();
	controlPanel.add(new JLabel(
		AJConfig.BUNDLE.getString("AJ.lblShowLatexOutput.text")));
	controlPanel.add(cbxLatexOutput);
	controlPanel.add(btnCreateJournal);
	controlPanel.add(btnClose);

	// Create the main panel containing the text area and the control panel
	JPanel mainPanel = new JPanel(new BorderLayout());
	mainPanel.setPreferredSize(getContentPane().getPreferredSize());
	mainPanel.add(
		new JLabel(AJConfig.BUNDLE.getString("AJ.lblOutput.text")),
		BorderLayout.NORTH);
	mainPanel.add(scrollPane, BorderLayout.CENTER);
	mainPanel.add(controlPanel, BorderLayout.SOUTH);

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
	// try {
	// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	// } catch (Exception e) {
	// }

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
