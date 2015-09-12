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

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.astrojournal.generator.AJExporterByDate;
import org.astrojournal.generator.AJExporterByTarget;
import org.astrojournal.observation.AJObservation;

/**
 * This class automatically generates the Latex code for the AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version 0.8
 * @since 12/04/2015
 */
public class AJLatexGenerator {

  /** The log associated to this class */
  private static Logger log = Logger.getLogger(AJLatexGenerator.class);

  /** The relative path containing the raw files (observation input folder). */
  private String rawReportsFolder = "raw_reports";
  /** The name of the folder containing the latex observation files by date (observation output folder). */
  private String latexReportsFolderByDate = "latex_reports_by_date";
  /** The name of the folder containing the latex observation files by target (observation output folder). */
  private String latexReportsFolderByTarget = "latex_reports_by_target";

  /** The name of the main Latex file sorted by date. */
  private String latexMainByDate = "astrojournal_by_date.tex";
  /** The name of the main Latex file sorted by target. */
  private String latexMainByTarget = "astrojournal_by_target.tex";

  /** The Latex header with path for astrojournal by date. */
  private String latexHeaderByDate = "latex_header_footer/header_by_date.tex";
  /** The Latex footer with path for astrojournal by date. */
  private String latexFooterByDate = "latex_header_footer/footer_by_date.tex";
  /** The Latex header with path for astrojournal by target. */
  private String latexHeaderByTarget = "latex_header_footer/header_by_target.tex";
  /** The Latex footer with path for astrojournal by target. */
  private String latexFooterByTarget = "latex_header_footer/footer_by_target.tex";

  /** The list of observations. */
  private ArrayList<AJObservation> observations = new ArrayList<AJObservation>(1000);

  /** True if the observations have been processed. */
  private boolean observationsProcessed = false;
  

  /** Default constructor */
  public AJLatexGenerator() {}

  /** 
   * It generates the latex files for AstroJournal. 
   * @param rawObsDir the directory containing the raw observation files (input)
   * @param latexObsByDateDir the directory containing the single observations by date in latex format (output)
   * @param latexObsByTargetDir the directory containing the single observations by target in latex format (output)
   * @return true if the observations sorted by date and by target have been exported to Latex correctly
   */
  public boolean generateJournals(String rawObsDir, String latexObsByDateDir, String latexObsByTargetDir) {
    rawReportsFolder = rawObsDir;
    latexReportsFolderByDate = latexObsByDateDir;
    latexReportsFolderByTarget = latexObsByTargetDir;
    if (!importObservations()) {
      log.warn("raw observation file is not valid. Cannot generate Latex code for the observations.");
      return false;
    }
    boolean exportedByDate=true, exportedByTarget=true;
    exportedByDate = generateJournalByDate(rawObsDir, latexObsByDateDir);
    exportedByTarget = generateJournalByTarget(rawObsDir, latexObsByTargetDir);
    return exportedByDate && exportedByTarget;
  }

  /**
   * Generate the Latex document sorted by date
   * @param rawObsDir the directory containing the raw observation files (input)
   * @param latexObsByDateDir the directory containing the single observations by date in latex format (output)
   * @return true if the observations sorted by date have been exported to Latex correctly
   */
  public boolean generateJournalByDate(String rawObsDir, String latexObsByDateDir) {
    rawReportsFolder = rawObsDir;
    latexReportsFolderByDate = latexObsByDateDir;
    if (!importObservations()) {
      log.warn("raw observation file is not valid. Cannot generate Latex code for the observations.");
      return false;
    }
    AJExporterByDate ajExporterByDate = new AJExporterByDate();    
    // export the imported observation by date to Latex
    System.out.println("\nExporting observation by date:");
    boolean resultByDate = ajExporterByDate.exportObservations(observations, latexReportsFolderByDate);
    ajExporterByDate.generateJournalByDate(latexReportsFolderByDate, latexHeaderByDate, latexMainByDate, latexFooterByDate);
    return resultByDate;
  }


  /**
   * Generate the Latex document sorted by target.
   * @param rawObsDir the directory containing the raw observation files (input)
   * @param latexObsByTargetDir the directory containing the single observations by target in latex format (output)
   * @return true if the observations sorted by target have been exported to Latex correctly
   */
  public boolean generateJournalByTarget(String rawObsDir, String latexObsByTargetDir) {
    rawReportsFolder = rawObsDir;
    latexReportsFolderByTarget = latexObsByTargetDir;
    if (!importObservations()) {
      log.warn("raw observation file is not valid. Cannot generate Latex code for the observations.");
      return false;
    }
    AJExporterByTarget ajExporterByTarget = new AJExporterByTarget();    
    // export the imported observation by target to Latex
    System.out.println("\nExporting observation by target:");
    boolean resultByTarget = ajExporterByTarget.exportObservations(observations, latexReportsFolderByTarget);
    ajExporterByTarget.generateJournalByTarget(latexReportsFolderByTarget, latexHeaderByTarget, latexMainByTarget, latexFooterByTarget);
    return resultByTarget;
  }    
    

  /**
   * Import the observations.
   * 
   * @return true if the procedure succeeds, false otherwise.
   */
  private boolean importObservations() {
    if(!observationsProcessed) {
      File[] files = new File(rawReportsFolder).listFiles();
      if (files == null) {
        log.warn("Folder " + rawReportsFolder + " not found");
        return false;
      }
      AJDataImporter ajImporter = new AJDataImporter();
      observations.addAll(ajImporter.importObservations(files));
      observationsProcessed = true;
    }
    return observationsProcessed;
  }

}
