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

  /** A cache of the visited targets. */
  private ArrayList<String> processedTargetCache = null;

  /** Default constructor */
  public AJObservationExporterByTarget() {} 

  /** 
   * Exports a list of observations by target to Latex
   * @param observations the list of observations to exportObservation
   * @param latexReportsByTargetFolder the folder to write the observation in.
   * @return true if the observations are exported
   */
  public boolean exportObservations(ArrayList<AJObservation> observations, String latexReportsByTargetFolder) {
    processedTargetCache = new ArrayList<String>(250);
    for(int i=0; i<observations.size(); i++) {
      AJObservation obs = observations.get(i);
      ArrayList<AJObservationItem> observationItems = obs.getObservationItems();
      for(int j=0; j<observationItems.size(); j++) {
        AJObservationItem obsItem = observationItems.get(j);
        String filenameOut = computeFileName(obsItem);
        Writer targetWriter = null;
        try {
          if(!wasTargetProcessed(obsItem.getTarget())) {
            processedTargetCache.add(obsItem.getTarget());
            targetWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
              new File(latexReportsByTargetFolder, filenameOut + ".tex")), "utf-8"));
            targetWriter.write("{\\bf " + obsItem.getTarget() + ", " 
                + obsItem.getConstellation() + ", " 
                + obsItem.getType() + "}:\n");
            targetWriter.write("\\begin{itemize}\n");
          } else {
            targetWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
              new File(latexReportsByTargetFolder, filenameOut + ".tex"), true), "utf-8"));
          }
          // if file already created skip the previous two lines
          targetWriter.write("\\item " + obs.getDate() + 
            " " + obs.getTime() + 
            ", " + obs.getLocation() + 
            ". " + obs.getSeeing() + 
            ", " + obs.getTransparency() + 
            ". " + obs.getTelescopes() + 
            ", " + obsItem.getPower() +
            ". " + obsItem.getNotes() + "\n");

          // do not close the Latex 'itemize' block now because nothing is known about other observations 
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
    processedTargetCache = new ArrayList<String>(250);
    for(int i=0; i<observations.size(); i++) {
      AJObservation obs = observations.get(i);
      ArrayList<AJObservationItem> observationItems = obs.getObservationItems();
      for(int j=0; j<observationItems.size(); j++) {
        AJObservationItem obsItem = observationItems.get(j);
        String filenameOut = computeFileName(obsItem);
        Writer targetWriter = null;
        try {
          if(!wasTargetProcessed(obsItem.getTarget())) {
            processedTargetCache.add(obsItem.getTarget());
            targetWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
              new File(latexReportsByTargetFolder, filenameOut + ".tex"), true), "utf-8"));
            targetWriter.write("\\end{itemize}\n");
            System.out.println("\tExported target " + filenameOut);
          }

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

  /**
   * Create the filename with different formats depending on whether this is a planet, 
   * a double/multiple star system, or everything else
   * @return the name of the file
   */
  private String computeFileName(AJObservationItem obsItem) {
    if(obsItem.getType().toLowerCase().equals("planet") ||
        obsItem.getTarget().toLowerCase().equals("moon") ||
        obsItem.getTarget().toLowerCase().equals("sun")) {
      return obsItem.getTarget().replaceAll("\\s+","").replaceAll("/","-");
    }
    if(obsItem.getType().toLowerCase().equals("star") || 
        obsItem.getType().toLowerCase().equals("dbl star") || 
        obsItem.getType().toLowerCase().equals("mlt star")) {
      return obsItem.getConstellation() + "_" + obsItem.getTarget().replaceAll("\\s+","").replaceAll("/","-");
    }
    return obsItem.getTarget().replaceAll("\\s+","").replaceAll("/","-") + "_" + obsItem.getConstellation();
  }

  /**
   * Search whether the target has already been processed or not.
   * @param target
   * @return true if the target was already processed
   */
  private boolean wasTargetProcessed(String target) {
    boolean processed = false;
    for(int i=0; i<processedTargetCache.size() && !processed; i++) {
      if(processedTargetCache.get(i).equals(target)) {
        processed = true;
      }
    }
    return processed;
  }



}