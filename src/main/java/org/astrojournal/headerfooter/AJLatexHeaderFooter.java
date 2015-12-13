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
package org.astrojournal.headerfooter;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * The Header Footer of the Latex main file.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 28/05/2015
 */
public abstract class AJLatexHeaderFooter {

  /** The log associated to this class */
  private static Logger log = Logger.getLogger(AJLatexHeaderFooter.class);
  
  /** 
   * Imports the latex file 
   * @param file The Latex file
   * @return the imported Latex file as a string
   */
  protected String importLatex(String file) {
    File f = new File(file);
    StringBuilder sb = new StringBuilder();
    if (f.isFile() && f.getName().endsWith(".tex")) {
      log.debug("Importing latex file " + file);
      // Create a buffered reader to read the file
      BufferedReader reader = null;
      try {
        reader = new BufferedReader(new FileReader(f));
        String line;
        // Read all lines
        while ((line = reader.readLine()) != null) {
          log.debug(line);
          sb.append(line).append(" \n");
        } // end while
      } catch (IOException ex) {
        System.out.println(ex);
      }
      finally {
        try {
          if (reader != null)
            reader.close();
        } catch (IOException ex) { 
          log.warn("File " + file + " was not closed successfully");
        }
      }
    }
    // No need to replace \ with \\ as this is not interpreted by Java at this stage
    return sb.toString();
  }


  /** Default constructor. It initialises default header and footer. */
  public AJLatexHeaderFooter() { }

}