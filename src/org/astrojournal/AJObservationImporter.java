/*
 * Author: Piero Dalle Pezze
 * Version: 0.1
 * Created on: 28/05/2015
 */
package org.astrojournal;

import java.io.BufferedReader;
import java.io.IOException;
import org.apache.log4j.Logger;


/**
 * The parser for AstroJournal. It imports tab separated value (tsv) files 
 * containing the observations.
 */ 
public class AJObservationImporter {

  /** The log associated to this class */
  private static Logger log = Logger.getLogger(AJObservationImporter.class);  

  /** The keyword denoting the first line of the observation record */
  protected static String initialKeyword = AJObservation.DATE_NAME;
    

  /** Default constructor */
  public void AJObservationImporter() {} 

  /** 
   * Imports an observation record 
   * @param obs the object containing the observation to import
   * @param line the current line parsed in the file (the first line of the record)
   * @param reader the buffered reader associated to the file
   * @throws IOException if reader cannot read the observation
   */
  public void importObservation(AJObservation obs, String line,
					BufferedReader reader) throws IOException {
    String delimiter = "\t";  
    log.debug(line);
    // copy the first line
    String[] values = line.split(delimiter);
    
    log.debug("Line length (A): " + values.length);
    if (values.length == 2 && values[0].equals(AJObservation.DATE_NAME)) {
      obs.setDate(values[1]);
      log.debug("values[0]" + values[0] + " values[1]=" + values[1]);
    }
    // Read the other lines for this observation
    while ((line = reader.readLine()) != null) {
      values = line.split(delimiter);
      log.debug("Line length (B): " + values.length);      

      if (values.length == 0 || values[0].equals("")) {
	  break;
      }

      if (values.length == 2) {

	  if (values[0].equals(AJObservation.TIME_NAME)) {
	      obs.setTime(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);

	  } else if (values[0].equals(AJObservation.LOCATION_NAME)) {
	      obs.setLocation(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);

	  } else if (values[0].equals(AJObservation.ALTITUDE_NAME)) {
	      obs.setAltitude(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);

	  } else if (values[0].equals(AJObservation.TEMPERATURE_NAME)) {
	      obs.setTemperature(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);
	
	  } else if (values[0].equals(AJObservation.SEEING_NAME)) {
	      obs.setSeeing(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);

	  } else if (values[0].equals(AJObservation.TRANSPARENCY_NAME)) {
	      obs.setTransparency(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);

	  } else if (values[0].equals(AJObservation.TELESCOPES_NAME)) {
	      obs.setTelescopes(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);
	
	  } else if (values[0].equals(AJObservation.EYEPIECES_NAME)) {
	      obs.setEyepieces(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);
	
	  } else if (values[0].equals(AJObservation.POWER_EXIT_PUPIL_FOV_NAME)) {
	      obs.setPowerExitPupilFOV(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);
	      
	  } else if (values[0].equals(AJObservation.FILTERS_NAME)) {
	      obs.setFilters(values[1]);
	      log.debug("values[0]==" + values[0] + " values[1]=" + values[1]);
	  }
	  
      } else if (values.length == 5
		 && values[0].equals(AJObservationItem.TARGET_NAME)
		 && values[1].equals(AJObservationItem.CONSTELLATION_NAME)
		 && values[2].equals(AJObservationItem.TYPE_NAME)
		 && values[3].equals(AJObservationItem.POWER_NAME)
		 && values[4].equals(AJObservationItem.NOTES_NAME)) {
	  while ((line = reader.readLine()) != null) {
	      values = line.split(delimiter);
	      if (values.length != 5 ||	values[0].equals("")) {
		  break;
	      }
	      log.debug(line);
	      log.debug("Line length (C): " + values.length);
	      log.debug(values[4]);
	      AJObservationItem item = new AJObservationItem();
	      item.setTarget(values[0]);
	      item.setConstellation(values[1]);
	      item.setType(values[2]);
	      item.setPower(values[3]);
	      item.setNotes(values[4].replace("%", "\\%"));
	      obs.addObservationItem(item);
	  }
      }
    }
  }

  /** Returns the initial keyword denoting the beginning of the observation record 
   * @return initialKeyword
   */
  public static String getInitialKeyword() { return initialKeyword; }

}
