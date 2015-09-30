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
import org.astrojournal.observation.AJObservation;

/**
 * This class automatically generates astro journal documents.
 * 
 * @author Piero Dalle Pezze
 * @version 0.8
 * @since 12/04/2015
 */
public class AJGenerator {

  /** The log associated to this class */
  private static Logger log = Logger.getLogger(AJGenerator.class);

  /** The relative path containing the raw files (observation input folder). */
  private String rawReportsFolder = "raw_reports";
  /** The name of the folder containing the latex observation files by date (observation output folder). */
  private String latexReportsFolderByDate = "latex_reports_by_date";
  /** The name of the folder containing the latex observation files by target (observation output folder). */
  private String latexReportsFolderByTarget = "latex_reports_by_target";
  /** The name of the folder containing the latex observation files by constellation (observation output folder). */
  private String latexReportsFolderByConstellation = "latex_reports_by_constellation";
  /** The name of the folder containing the latex observation files by date (observation output folder). */
  private String sglReportsFolderByDate = "sgl_reports_by_date";
  
  /** The name of the main Latex file sorted by date. */
  private String latexMainByDate = "astrojournal_by_date.tex";
  /** The name of the main Latex file sorted by target. */
  private String latexMainByTarget = "astrojournal_by_target.tex";
  /** The name of the main Latex file sorted by constellation. */
  private String latexMainByConstellation = "astrojournal_by_constellation.tex";
  /** The name of the SGL main file sorted by date. */
  private String sglMainByDate = "astrojournal_by_date_sgl.txt";
  
  /** The Latex header with path for astrojournal by date. */
  private String latexHeaderByDate = "latex_header_footer/header_by_date.tex";
  /** The Latex footer with path for astrojournal by date. */
  private String latexFooterByDate = "latex_header_footer/footer_by_date.tex";
  /** The Latex header with path for astrojournal by target. */
  private String latexHeaderByTarget = "latex_header_footer/header_by_target.tex";
  /** The Latex footer with path for astrojournal by target. */
  private String latexFooterByTarget = "latex_header_footer/footer_by_target.tex";
  /** The Latex header with path for astrojournal by constellation. */
  private String latexHeaderByConstellation = "latex_header_footer/header_by_constellation.tex";
  /** The Latex footer with path for astrojournal by constellation. */
  private String latexFooterByConstellation = "latex_header_footer/footer_by_constellation.tex";

  /** The list of observations. */
  private ArrayList<AJObservation> observations = new ArrayList<AJObservation>(1000);

  /** True if the observations have been processed. */
  private boolean observationsProcessed = false;
  

  /** Default constructor */
  public AJGenerator() {}

  /** 
   * It generates the latex files for AstroJournal. 
   * @param rawObsDir the directory containing the raw observation files (input)
   * @param latexObsByDateDir the directory containing the single observations by date in latex format (output)
   * @param latexObsByTargetDir the directory containing the single observations by target in latex format (output)
   * @param latexObsByConstellationDir the directory containing the targets by constellation in latex format (output)
   * @param sglObsByDateDir the directory containing the single observations by date in txt format (output)
   * @return true if the observations sorted by date and by target have been exported to Latex correctly
   */
  public boolean generateJournals(String rawObsDir, String latexObsByDateDir, String latexObsByTargetDir, String latexObsByConstellationDir, String sglObsByDateDir) {
    rawReportsFolder = rawObsDir;
    latexReportsFolderByDate = latexObsByDateDir;
    latexReportsFolderByTarget = latexObsByTargetDir;
    latexReportsFolderByConstellation = latexObsByConstellationDir;    
    sglReportsFolderByDate = sglObsByDateDir;
    if (!importObservations()) {
      log.warn("raw observation file is not valid. Cannot generate Latex code for the observations.");
      return false;
    }
    boolean exportedByDate=true, exportedByTarget=true, exportedByConstellation=true, exportedByDateSGL = true;
    exportedByDate = generateJournalByDate(rawObsDir, latexObsByDateDir);
    exportedByTarget = generateJournalByTarget(rawObsDir, latexObsByTargetDir);
    exportedByConstellation = generateJournalByConstellation(rawObsDir, latexObsByConstellationDir);    
    exportedByDateSGL = generateJournalByDateSGL(rawObsDir, sglObsByDateDir);
    return exportedByDate && exportedByTarget && exportedByConstellation && exportedByDateSGL;
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
    AJExporter ajExporterByDate = new AJExporterByDate();    
    // export the imported observation by date to Latex
    System.out.println("\nExporting observation by date:");
    boolean resultByDate = ajExporterByDate.exportObservations(observations, latexReportsFolderByDate);
    ajExporterByDate.generateJournal(latexReportsFolderByDate, latexHeaderByDate, latexMainByDate, latexFooterByDate);
    return resultByDate;
  }

  
  /**
   * Generate the txt document for SGL reports sorted by date
   * @param rawObsDir the directory containing the raw observation files (input)
   * @param sglObsByDateDir the directory containing the single observations by date in txt format (output)
   * @return true if the observations sorted by date have been exported to Latex correctly
   */
  public boolean generateJournalByDateSGL(String rawObsDir, String sglObsByDateDir) {
    rawReportsFolder = rawObsDir;
    sglReportsFolderByDate = sglObsByDateDir;
    if (!importObservations()) {
      log.warn("raw observation file is not valid. Cannot generate txt code for the observations.");
      return false;
    }
    AJExporter ajExporterByDateSGL = new AJExporterByDateSGL();    
    // export the imported observation by date to txt
    System.out.println("\nExporting observation by date for SGL:");
    boolean resultByDateSGL = ajExporterByDateSGL.exportObservations(observations, sglReportsFolderByDate);
    ajExporterByDateSGL.generateJournal(sglReportsFolderByDate, "", sglMainByDate, "");
    return resultByDateSGL;
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
    AJExporter ajExporterByTarget = new AJExporterByTarget();    
    // export the imported observation by target to Latex
    System.out.println("\nExporting observation by target:");
    boolean resultByTarget = ajExporterByTarget.exportObservations(observations, latexReportsFolderByTarget);
    ajExporterByTarget.generateJournal(latexReportsFolderByTarget, latexHeaderByTarget, latexMainByTarget, latexFooterByTarget);
    return resultByTarget;
  }   
  
  
  /**
   * Generate the Latex document sorted by constellation.
   * @param rawObsDir the directory containing the raw observation files (input)
   * @param latexObsByConstellationDir the directory containing the targets by constellation in latex format (output)
   * @return true if the observations sorted by target have been exported to Latex correctly
   */
  public boolean generateJournalByConstellation(String rawObsDir, String latexObsByConstellationDir) {
    rawReportsFolder = rawObsDir;
    latexReportsFolderByConstellation = latexObsByConstellationDir;
    if (!importObservations()) {
      log.warn("raw observation file is not valid. Cannot generate Latex code for the observations.");
      return false;
    }
    AJExporter ajExporterByConstellation= new AJExporterByConstellation();    
    // export the imported observation by constellation to Latex
    System.out.println("\nExporting observation by constellation:");
    boolean resultByConstellation = ajExporterByConstellation.exportObservations(observations, latexReportsFolderByConstellation);
    ajExporterByConstellation.generateJournal(latexReportsFolderByConstellation, latexHeaderByConstellation, latexMainByConstellation, latexFooterByConstellation);
    return resultByConstellation;
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
      AJImporter ajImporter = new AJTabSeparatedValueImporter();
      observations.addAll(ajImporter.importObservations(files));
      observationsProcessed = true;
    }
    return observationsProcessed;
  }

}
