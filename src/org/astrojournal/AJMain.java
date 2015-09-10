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
import org.astrojournal.generator.AJLatexGenerator;
import org.astrojournal.gui.AJMainGUI;
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
   * Main function. If no arguments are passed, the graphical user interface is started. 
   * Otherwise, if a list of 3 arguments representing the input and output folders is passed, 
   * then it runs as command line. 
   * @param args 
   */
  public static void main(String[] args) {
    
    // Depending on args, this class invokes AJMainGUI or runs as a Batch program.
    // if no args is passed, run AJMainGUI.
    
    String tsvReportsFolder = null;
    String latexReportsFolderByDate = null;
    String latexReportsFolderByTarget = null;
    try {
      if(args.length == 0) {
        java.awt.EventQueue.invokeLater(new Runnable() {
          @Override
          public void run() {
          // TODO: for now we use the mini GUI. 
//              new AJMainGUI().setVisible(true);
              new AJMiniGUI().setVisible(true);
          }
      });
      }
      else if(args.length == 3) {
        AJLatexGenerator ajLatexGenerator = new AJLatexGenerator();
        tsvReportsFolder = args[0];
        latexReportsFolderByDate = args[1];
        latexReportsFolderByTarget = args[2];
        ajLatexGenerator.generateLatexCode(tsvReportsFolder, latexReportsFolderByDate, latexReportsFolderByTarget);
        //ajLatexGenerator.generateLatexCodeByDate(tsvReportsFolder, latexReportsFolderByDate);
        //ajLatexGenerator.generateLatexCodeByTarget(tsvReportsFolder, latexReportsFolderByTarget);
      } else {
        throw new Exception("Please, specify the folders : " + 
                    tsvReportsFolder + "/ " + 
                    latexReportsFolderByDate + "/ and " + 
                    latexReportsFolderByTarget + "/ as arguments.");
      }
    } catch (Exception ex) {
        log.warn(ex);
    }
  }

}
