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
package org.astrojournal;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import org.astrojournal.gui.AJForm;


/** 
 * The main graphical user interface for AstroJournal
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 3 Sep 2015
 */
public class AJMainGUI extends JFrame {

  private static final long serialVersionUID = -7754655408591909633L;

  private static AJMainGUI application = null;
  
  public static final String VERSION = "0.1.0_devel";
  
  private JTabbedPane fileTabs = null;

  private AJForm form = null;
  
  public AJMainGUI() { }
  
  public void start() {
      setTitle("AstroJournal");
//    setIconImage(new ImageIcon(ClassLoader.getSystemResource("org/astrojournal/Resources/aj_icon.png")).getImage());
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(800,600);
      setLocationRelativeTo(null);
            
      fileTabs = new JTabbedPane(JTabbedPane.TOP);
      
//    setJMenuBar(new AJMenuBar(this));   
      // set the main AJ form
      form = new AJForm(); 
      getContentPane().add(form, "Center");
      application.setVisible(true);
  }

  public void close () {
    if (fileTabs.getSelectedIndex() >=0) {
      fileTabs.remove(fileTabs.getSelectedIndex());
    }
    if (fileTabs.getTabCount() == 0) {
      validate();
      repaint();
    }
  }
  
  public void closeAll () {
    fileTabs.removeAll();
    validate();
    repaint();
  }
  
  public void openFile () { }
  

  public static void main(String[] args) {
    // See if we just have to print out the version
    if (System.getProperty("aj.show_version") != null && System.getProperty("aj.show_version").equals("true")) {
      System.out.println("AstroJournal v"+VERSION);
      System.exit(0);
    }
    if (args.length > 0) {
      System.setProperty("java.awt.headless", "true");
      System.exit(0);
    }    
    else {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {}
    }
    application = new AJMainGUI(); 
    application.start();
  } 
  
}
