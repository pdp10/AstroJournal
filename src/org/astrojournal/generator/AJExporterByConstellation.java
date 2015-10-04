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
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.astrojournal.headerfooter.AJLatexFooter;
import org.astrojournal.headerfooter.AJLatexHeader;
import org.astrojournal.observation.AJObservation;
import org.astrojournal.observation.AJObservationItem;

/**
 * Exports the observed targets by constellation to Latex code.
 * 
 * @author Piero Dalle Pezze
 * @version 0.2
 * @since 28/05/2015
 */
public class AJExporterByConstellation implements AJExporter {

  /** The log associated to this class */
  private static Logger log = Logger.getLogger(AJExporterByConstellation.class);

  private HashMap<String, HashSet<String>> constellations = new HashMap<String, HashSet<String>>();
  
  /** A comparator for sorting items */ 
  private Comparator<String> itemComparator = new Comparator<String>() {
    @Override
    public int compare(String o1, String o2) {
      return extractInt(o1) - extractInt(o2);
    }
    int extractInt(String s) {
      String num = s.replaceAll("\\D", "");
      // return 0 if no digits found
      return num.isEmpty() ? 0 : Integer.parseInt(num);
    }
  };
  

  /** Default constructor */
  public AJExporterByConstellation() { } 
  
  
  /**
   * Generate the Latex document by constellation
   * @param latexReportsFolderByConst the directory containing the single observations by date in latex format (output)
   * @param latexHeaderByConst the latex header code (by date)
   * @param latexMainByConst the latex main body code (by date)
   * @param latexFooterByConst the latex footer code (by date)
   */
  @Override
  public void generateJournal(String latexReportsFolderByConst, String latexHeaderByConst, String latexMainByConst, String latexFooterByConst) {
    AJLatexHeader ajLatexHeaderByConst = new AJLatexHeader(latexHeaderByConst);
    AJLatexFooter ajLatexFooterByConst = new AJLatexFooter(latexFooterByConst);
    Writer writerByConst = null;
    try {
      writerByConst = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
        latexMainByConst), "utf-8"));
      // write the Latex Header
      writerByConst.write(ajLatexHeaderByConst.getHeader());

      // write the Latex Body
      // parse each file in the latex obs folder
      File[] files = new File(latexReportsFolderByConst).listFiles();
      if (files == null) {
        log.warn("Folder " + latexReportsFolderByConst + " not found");
        return;
      }
      // sort the constellations when we parse the files
      Arrays.sort(files);
      
      String currConst = "", filename = "";
      // If this pathname does not denote a directory, then listFiles() returns null.
      for (File file : files) {
        filename = file.getName();
        if (file.isFile() && filename.endsWith(".tex")) {
          if(!currConst.equals(filename.substring(filename.indexOf("_")+1, filename.indexOf(".")))) {
            currConst = filename.substring(filename.indexOf("_")+1, filename.indexOf("."));
            writerByConst.write("\\section{"+currConst+"}\n");
          }
          // include the file removing the extension .tex
          writerByConst.write("\\input{" + latexReportsFolderByConst + "/"
              + filename.replaceFirst("[.][^.]+$", "") + "}\n");
          //writerByConst.write("\\clearpage \n");
        }
      }

      // write the Latex Footer
      writerByConst.write(ajLatexFooterByConst.getFooter());

    } catch (IOException ex) {
      log.warn("Error when opening the file " + latexMainByConst);
    } catch (Exception ex) {
      log.warn(ex);
    }
    finally {
      try {
        if (writerByConst != null)
          writerByConst.close();
      } catch (Exception ex) {
      }
    }    
  }
  
  

  /**
   * Exports an observation record to Latex
   * 
   * @param observations
   *        the list of observations to exportObservation
   * @param latexReportsByConstFolder
   *        the folder to write the observation in.
   * @return true if the observations are exported
   */
  @Override
  public boolean exportObservations(ArrayList<AJObservation> observations,
    String latexReportsByConstFolder) {
    boolean result = true;
    if (constellations.size() == 0) {
      organiseTargetsByConstellation(observations);
    }
    String[] keys = constellations.keySet().toArray(new String[0]);
    for (int i = 0; i < keys.length; i++) {
      Writer list = null;
      String filenameOut = keys[i];
      try {
        list = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
          new File(latexReportsByConstFolder, "const_" + filenameOut + ".tex")),
          "utf-8"));
        String[] targets = constellations.get(keys[i]).toArray(new String[0]);
        // sort the targets here, before writing them in the file
        Arrays.sort(targets);
        StringBuilder listOfTargets = new StringBuilder();
        for (int j = 0; j<targets.length; j++) {
          if(j==targets.length-1) 
            listOfTargets.append(targets[j]);
          else
            listOfTargets.append(targets[j] + ", ");
        }
        list.write(listOfTargets.toString() + "\n\n");
        System.out.println("\tExported constellation " + filenameOut);
      } catch (IOException ex) {
        System.out.println("Error when opening the file");
        result = false;
      } catch (Exception ex) {
        System.out.println(ex);
        result = false;
      }
      finally {
        try {
          if (list != null)
            list.close();
        } catch (Exception ex) {
        }
      }
    }
    return result;
  }


  /**
   * Organise the targets by constellation in a suitable data structure.
   * 
   * @param observations
   *        the list of observations to exportObservation
   */
  private void organiseTargetsByConstellation(ArrayList<AJObservation> observations) {
    AJObservation obs = null;
    int nObservations = observations.size();
    for (int i = 0; i < nObservations; i++) {
      obs = observations.get(i);
      ArrayList<AJObservationItem> items = obs.getObservationItems();
      for (int j = 0; j < items.size(); j++) {
        AJObservationItem item = items.get(j);
        // skip solar system targets. We only consider DSOs.
        if(item.getType().toLowerCase().equals("planet") ||
           item.getTarget().toLowerCase().equals("sun") || 
           item.getTarget().toLowerCase().equals("moon")) {
          continue;
        }
        if (!constellations.containsKey(item.getConstellation())) {
          constellations.put(item.getConstellation(), new HashSet<String>());
        }
        log.debug(item.getConstellation() + " " + item.getTarget() + " (" + item.getType() + ")");
        constellations.get(item.getConstellation()).add(item.getTarget() + " (" + item.getType() + ")");
      }
    }
  }
}
