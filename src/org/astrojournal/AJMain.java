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
   * Main function 
   * @param args a list of 5 arguments representing the input and output folders
   */
  public static void main(String[] args) {
    AJLatexGenerator ajLatexGenerator = new AJLatexGenerator();
    String tsvReportsFolder = null;
    String latexReportsFolderByDate = null;
    String latexReportsFolderByTarget = null;
    try {
      if(args.length == 3) {
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
