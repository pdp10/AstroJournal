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
package org.astrojournal.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.astrojournal.observation.AJObservation;

/**
 * The parser for AstroJournal. It imports files containing the observations.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 28/05/2015
 */
public abstract class AJImporter {

  /** The keyword denoting the first line of the observation record */
  protected static String initialKeyword = AJObservation.DATE_NAME;
  /** The values contained in an imported string. */
  protected String[]      values         = null;


  /** Default constructor */
  public AJImporter() { }


  /**
   * Returns the initial keyword denoting the beginning of the observation
   * record
   * 
   * @return initialKeyword
   */
  public static String getInitialKeyword() {
    return initialKeyword;
  }

  /**
   * Imports the observation data stored in multiple files.
   * @param files An array of files to parse (either CSV or TSV file, separated by TAB delimiter).
   * @return a list of AJObservation objects
   */
  public ArrayList<AJObservation> importObservations(File[] files) {
    Arrays.sort(files);
    ArrayList<AJObservation> observations = new ArrayList<AJObservation>();
    for (File file : files) {
      observations.addAll(importObservations(file));
    } // end for
    return observations;
  }
  
  
  /**
   * Imports the observation data stored in a file.
   * @param file The file to parse
   * @return a list of AJObservation objects
   */
  public abstract ArrayList<AJObservation> importObservations(File file);


  /**
   * Imports an observation record
   * 
   * @param reader
   *        the buffered reader associated to the file
   * @param obs
   *        the object containing the observation to import
   * @param line
   *        the current line parsed in the file (the first line of the record)
   * @param delimiter
   *        the field delimiter
   * @throws IOException
   *         if reader cannot read the observation
   */
  protected abstract void importObservation(BufferedReader reader, AJObservation obs, String line, String delimiter) throws IOException;
  
}
