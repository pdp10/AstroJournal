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
package org.astrojournal.gui;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.astrojournal.AJConfig;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * A very minimal graphical user interface for running astrojournal without
 * scripts.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 10/09/2015
 */
public class AJMiniGUI extends JFrame {

  private static final long serialVersionUID = -7217707367091677434L;
  
  private JCheckBox cbxLatexOutput;
  private JButton btnCreateJournal;
  private JButton btnClose;
  private JTextArea txtArea;
  private StatusPanel statusPanel;
  private boolean latexOutput = AJConfig.getInstance().latexOutput;
  private AJMiniGUIControls commandRunner;
  
  /**
   * Creates new form NewJFrame
   */
  public AJMiniGUI() {
    initComponents();
  }

  /**
   * Append text to the text area
   * @param str the text to append
   */
  public void appendText(String str) {
    txtArea.append(str);
  }

  /**
   * Set the text for the status panel.
   * @param str the text to set
   */
  public void setStatusPanelText(String str) {
    statusPanel.setText(str);
  }

  
  /**
   * This method is called from within the constructor to initialise the form.
   */
  private void initComponents() {
    
    commandRunner = new AJMiniGUIControls(this);
    
    // Configure AJMiniGUI with basic parameters
    setTitle(AJConfig.getInstance().applicationName + " " + AJConfig.getInstance().applicationVersion);
    setSize(600,600);
    setMinimumSize(new Dimension(480, 300));
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setResizable(true);
    getContentPane().setLayout(new BorderLayout());

    // Create the status bar
    statusPanel = new StatusPanel();
    
    // Create the text area containing the program text output
    txtArea = new JTextArea();
    txtArea.setEditable(false);
    txtArea.setLineWrap(true);
    txtArea.setWrapStyleWord(true);
    JScrollPane scrollPane = new JScrollPane(txtArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    
    
    // Create the checkbox for printing the Latex output
    cbxLatexOutput = new JCheckBox();
    if(latexOutput) {
      cbxLatexOutput.setSelected(true);
    }
    cbxLatexOutput.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(cbxLatexOutput.isSelected()) {
          latexOutput = true;          
        } else {
          latexOutput = false;
        }
      }
    });
    
    // Create the button for creating the journals
    btnCreateJournal = new JButton();
    btnCreateJournal.setText("Create Journals");
    btnCreateJournal.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
          txtArea.setText("");
          commandRunner.createJournal(latexOutput);
      }
    });
    
    // Create the button for closing the application
    btnClose = new JButton();
    btnClose.setText("Close");
    btnClose.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {      
          dispose();
          System.exit(0);
      }
    });

    // Create the control panel containing the button and the checkbox
    JPanel controlPanel = new JPanel();
    controlPanel.add(new JLabel("Show Latex Output"));
    controlPanel.add(cbxLatexOutput);
    controlPanel.add(btnCreateJournal);
    controlPanel.add(btnClose);
    
    // Create the main panel containing the text area and the control panel
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setPreferredSize(getContentPane().getPreferredSize());
    mainPanel.add(new JLabel("Output:"), BorderLayout.NORTH);    
    mainPanel.add(scrollPane, BorderLayout.CENTER);    
    mainPanel.add(controlPanel, BorderLayout.SOUTH);
    
    // Add the main panel and the status panel to the frame.
    add(mainPanel, BorderLayout.CENTER);
    add(statusPanel, BorderLayout.SOUTH);
    
  } 


  /**
   * A simple main to start AJMiniGUI.
   * @param args The command line arguments
   */
  public static void main(String args[]) {

    // Note Nimbus does not seem to show the vertical scroll bar if there is too much text..
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {}

    // enable anti-aliased text:
    System.setProperty("awt.useSystemAAFontSettings","gasp");
    System.setProperty("swing.aatext", "true");
    
    // invoke the application and make it visible
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        new AJMiniGUI().setVisible(true);
      }
    });
    
  }
  
}
