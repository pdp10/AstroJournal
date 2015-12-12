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


import org.apache.log4j.Logger;
import org.astrojournal.gui.AJMiniGUI;

/**
 * This class automatically generates the Latex code for the AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version 0.8
 * @since 12/04/2015
 */
public class AJMain {

  /** The log associated to this class */
  private static Logger log = Logger.getLogger(AJMain.class);


  /**
   * Start AJMiniGUI.
   */
  private static void startAJMiniGUI() {
    // enable anti-aliased text:
    System.setProperty("awt.useSystemAAFontSettings","gasp");
    System.setProperty("swing.aatext", "true");
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
          new AJMiniGUI().setVisible(true);
      }
    });
  }
  
  /** 
   * Main function. By default AstroJournal mini GUI is started. 
   * A list of 5 arguments representing the input (1) and output (4) folders can also be passed 
   * as input parameters.
   * @param args 
   */
  public static void main(String[] args) {
    
    String rawReportsFolder = "raw_reports_folder";
    String latexReportsFolderByDate = "latex_report_folder_by_date";    
    String latexReportsFolderByTarget = "latex_report_folder_by_target";
    String latexReportsFolderByConstellation = "latex_report_folder_by_constellation";    
    String sglReportsFolderByDate = "sgl_report_folder_by_date";    
    try {
      if(args.length == 0) {
        startAJMiniGUI();
      }
      else if(args.length == 5) {
        // set AJ properties
        System.setProperty("aj.raw_reports_folder", args[0]);
        System.setProperty("aj.latex_reports_folder_by_date", args[1]);
        System.setProperty("aj.latex_reports_folder_by_target", args[2]);
        System.setProperty("aj.latex_reports_folder_by_constellation", args[3]);
        System.setProperty("aj.sgl_reports_folder_by_date", args[4]);
        startAJMiniGUI();
      } else {
        throw new Exception("Please, specify the folders : " + 
                    rawReportsFolder + "/ " + 
                    latexReportsFolderByDate + "/ " + 
                    latexReportsFolderByTarget + "/ " + 
                    latexReportsFolderByConstellation + "and " +                     
                    sglReportsFolderByDate + " as arguments.");
      }
    } catch (Exception ex) {
        log.warn(ex);
    }
  }

}
