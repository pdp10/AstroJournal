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
package org.astrojournal.generator;

import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.astrojournal.observation.AJObservation;
import org.astrojournal.observation.AJObservationItem;

/**
 * Exports an AstroJournal observation to Latex code.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 28/05/2015
 */
public class AJObservationExporterByDate {

  /** The log associated to this class */
  private static Logger log = Logger.getLogger(AJObservationExporterByDate.class);


  /** Default constructor */
  public AJObservationExporterByDate() {} 

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
    try {
      table = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
        new File(latexReportsFolder, "obs" + filenameOut + ".tex")), "utf-8"));
      
      // debugging
      log.debug("writing observation " + obs.getDate());
      table.write("% General observation data\n");
      table.write("\\begin{tabular}{ p{0.9in} p{1.3in} p{1.2in} p{5.2in}}\n");
      table.write("{\\bf " + AJObservation.DATE_NAME + ":} & "
          + obs.getDate() + " & {\\bf " + AJObservation.TELESCOPES_NAME
          + ":} & " + obs.getTelescopes() + " \\\\ \n");
      table.write("{\\bf " + AJObservation.TIME_NAME + ":} & "
          + obs.getTime() + " & {\\bf " + AJObservation.EYEPIECES_NAME
          + ":} & " + obs.getEyepieces() + " \\\\ \n");
      table.write("{\\bf " + AJObservation.LOCATION_NAME + ":} & "
          + obs.getLocation() + " & {\\bf "
          + AJObservation.FILTERS_NAME
          + ":} & " + obs.getFilters() + " \\\\ \n");
      table.write("{\\bf " + AJObservation.ALTITUDE_NAME + ":} & "
          + obs.getAltitude() + " & & \\\\ \n");      
      table.write("{\\bf " + AJObservation.TEMPERATURE_NAME + ":} & "
          + obs.getTemperature() + " & & \\\\ \n");
      table.write("{\\bf " + AJObservation.SEEING_NAME + ":} & "
          + obs.getSeeing() + " & & \\\\ \n");
      table.write("{\\bf " + AJObservation.TRANSPARENCY_NAME + ":} & "
          + obs.getTransparency() + " & & \\\\ \n");
      table.write("\\end{tabular}\n");


      table.write("% Detailed observation data\n");
      table.write("\\begin{longtable}{ p{0.7in}  p{0.3in}  p{0.6in}  p{0.9in}  p{5.8in} }\n");
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
