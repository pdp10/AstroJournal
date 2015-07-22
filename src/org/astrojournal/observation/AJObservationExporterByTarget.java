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
package org.astrojournal.observation;

import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Exports an AstroJournal set of observations by target to Latex code.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 22/07/2015
 */
public class AJObservationExporterByTarget {

  /** The log associated to this class */
  private static Logger log = Logger.getLogger(AJObservationExporterByTarget.class);


  /** Default constructor */
  public AJObservationExporterByTarget() {} 

  /** 
   * Exports a list of observations by target to Latex
   * @param observations the list of observations to exportObservation
   * @param latexReportsByTargetFolder the folder to write the observation in.
   * @return true if the observations are exported
   */
  public boolean exportObservations(ArrayList<AJObservation> observations, String latexReportsByTargetFolder) {

    for(int i=0; i<observations.size(); i++) {
      AJObservation obs = observations.get(i);
      ArrayList<AJObservationItem> observationItems = obs.getObservationItems();
      for(int j=0; j<observationItems.size(); j++) {
        AJObservationItem obsItem = observationItems.get(j);
        String filenameOut = obsItem.getTarget() + "_" + obsItem.getConstellation();
        Writer targetWriter = null;
        try {
          targetWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
            new File(latexReportsByTargetFolder, filenameOut + ".tex")), "utf-8"));
          
          // TODO
          // check if the file has already been created. If not, add target header
          targetWriter.write("{\\bf " + obsItem.getTarget() + ", " 
              + obsItem.getConstellation() + ", " 
              + obsItem.getType() + "}:\n");
          targetWriter.write("\\begin{itemize}\n");
          // if file already created skip the previous two lines
          targetWriter.write("\\item " + obs.getDate() + 
            " " + obs.getTime() + 
            ", " + obs.getLocation() + 
            ". " + obs.getSeeing() + 
            ", " + obs.getTransparency() + 
            ". " + obs.getTelescopes() + 
            ", " + obsItem.getPower() +
            ". " + obsItem.getNotes() + "\n");

          // At this stage do not close the block itemize because nothing is known about other observations 
          // for this target.
        
        } catch (IOException ex) {
          System.out.println("Error when opening the file");
          return false;
        } catch (Exception ex) {
          System.out.println(ex);
          return false;
        } finally {
          try {
            if (targetWriter != null)
              targetWriter.close();
          } catch (Exception ex) {
            return false;
          }
        }
      }
    }
    return closeLists(observations, latexReportsByTargetFolder);
  }


  /** 
   * It closes the latex lists opened by the function exportObservations
   * @param observations the list of observations to exportObservation
   * @param latexReportsByTargetFolder the folder to write the observation in.
   * @return true if the lists are closed
   */
  private boolean closeLists(ArrayList<AJObservation> observations, String latexReportsByTargetFolder) {

    for(int i=0; i<observations.size(); i++) {
      AJObservation obs = observations.get(i);
      ArrayList<AJObservationItem> observationItems = obs.getObservationItems();
      for(int j=0; j<observationItems.size(); j++) {
        AJObservationItem obsItem = observationItems.get(j);
        String filenameOut = obsItem.getTarget() + "_" + obsItem.getConstellation();
        Writer targetWriter = null;
        try {
          
          // TODO
          // append only
          targetWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
            new File(latexReportsByTargetFolder, filenameOut + ".tex")), "utf-8"));
          targetWriter.write("\\end{itemize}\n");

        } catch (IOException ex) {
          System.out.println("Error when opening the file");
          return false;
        } catch (Exception ex) {
          System.out.println(ex);
          return false;
        } finally {
          try {
            if (targetWriter != null)
              targetWriter.close();
          } catch (Exception ex) {
            return false;
          }
        }
      }
    }
    return true;
  }
}