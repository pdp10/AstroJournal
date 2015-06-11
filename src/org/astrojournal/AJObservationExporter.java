/*
 * Author: Piero Dalle Pezze
 * Version: 0.1
 * Created on: 28/05/2015
 */
package org.astrojournal;

import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Exports an AstroJournal observation to Latex code.
 */
public class AJObservationExporter {

  /** The log associated to this class */
  private static Logger log = Logger.getLogger(AJObservationExporter.class);
    
  
  /** Default constructor */
  public void AJObservationExporter() {} 

  /** Exports an observation record to Latex
   * @param obs the observation to exportObservation
   * @param latexReportsFolder the folder to write the observation in.
   */
  public void exportObservation(AJObservation obs, String latexReportsFolder) {
    Writer table = null;
    
    String filenameOut = obs.getDate();
    filenameOut = filenameOut.substring(6, 10) + filenameOut.substring(3, 5)
      + filenameOut.substring(0, 2);
    // Add an additional char if this is present. This is the case in which
    // more than one observation per day is done.
    if(obs.getDate().length() == 11) {
	filenameOut = filenameOut + obs.getDate().charAt(10);
    }

    ArrayList<AJObservationItem> observationItems = obs.getObservationItems();
    // debugging
    log.debug(obs.getEyepieces());
    try {
      table = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
        new File(latexReportsFolder, "obs" + filenameOut + ".tex")), "utf-8"));

      table.write("% General observation data\n");
      table.write("\\begin{tabular}{ p{1.7in} p{1.2in} p{1.5in} p{4.2in}}\n");
      table.write("{\\bf " + AJObservation.DATE_NAME + ":} & "
        + obs.getDate() + " & {\\bf " + AJObservation.TELESCOPES_NAME
        + ":} & " + obs.getTelescopes() + " \\\\ \n");
      table.write("{\\bf " + AJObservation.TIME_NAME + ":} & "
        + obs.getTime() + " & {\\bf " + AJObservation.EYEPIECES_NAME
        + ":} & " + obs.getEyepieces() + " \\\\ \n");
      table.write("{\\bf " + AJObservation.LOCATION_NAME + ":} & "
        + obs.getLocation() + " & {\\bf "
        + AJObservation.POWER_EXIT_PUPIL_FOV_NAME + ":} & "
        + obs.getPowerExitPupilFOV() + " \\\\ \n");
      table.write("{\\bf " + AJObservation.ALTITUDE_NAME + ":} & "
        + obs.getAltitude() + " & {\\bf " + AJObservation.FILTERS_NAME
        + ":} & " + obs.getFilters() + " \\\\ \n");
      table.write("{\\bf " + AJObservation.TEMPERATURE_NAME + ":} & "
        + obs.getTemperature() + " & & \\\\ \n");
      table.write("{\\bf " + AJObservation.SEEING_NAME + ":} & "
        + obs.getSeeing() + " & & \\\\ \n");
      table.write("{\\bf " + AJObservation.TRANSPARENCY_NAME + ":} & "
        + obs.getTransparency() + " & & \\\\ \n");
      table.write("\\end{tabular}\n");
      

      table.write("% Detailed observation data\n");
      table.write("\\centering \n");
      table.write("\\begin{longtable}{ p{0.8in}  p{0.3in}  p{0.5in}  p{0.9in}  p{5.8in} }\n");
      table.write("\\hline \n");
      table.write("{\\bf " + AJObservationItem.TARGET_NAME + "} & {\\bf "
        + AJObservationItem.CONSTELLATION_NAME + "} & {\\bf "
        + AJObservationItem.TYPE_NAME + "} & {\\bf "
        + AJObservationItem.POWER_NAME + "} & {\\bf "
        + AJObservationItem.NOTES_NAME + "} \\\\ \n");

      table.write("\\hline \n");
      for (AJObservationItem item : observationItems) {
        table.write(item.getTarget() + " & " + item.getConstellation() + " & "
          + item.getType() + " & " + item.getPower() + " & " + item.getNotes()
          + " \\\\ \n");
      }
      table.write("\\hline \n");
      table.write("\\end{longtable} \n");
    } catch (IOException ex) {
      System.out.println("Error when opening the file");
    } catch (Exception ex) {
      System.out.println(ex);
    }
    finally {
      try {
        if (table != null)
          table.close();
      } catch (Exception ex) {
      }
    }
  }

}
