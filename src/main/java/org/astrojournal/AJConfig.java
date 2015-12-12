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
package org.astrojournal;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;



/**
 * A simple class for configuring AstroJournal.
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 12 Dec 2015
 */
public class AJConfig {
  
  /** The AJConfig instance to be used. */
  private static AJConfig instance = new AJConfig();

  // THESE PARAMETERS ARE NOT CONFIGURABLE
  /** The AJ application name. */
  public final String applicationName = "AstroJournal";
  /** The AJ application version. */
  public final String applicationVersion = "v0.9";

  /** The name of the main Latex file sorted by date. */
  public final String latexMainByDate = "astrojournal_by_date.tex";
  /** The name of the main Latex file sorted by target. */
  public final String latexMainByTarget = "astrojournal_by_target.tex";
  /** The name of the main Latex file sorted by constellation. */
  public final String latexMainByConstellation = "astrojournal_by_constellation.tex";
  /** The name of the SGL main file sorted by date. */
  public final String sglMainByDate = "astrojournal_by_date_sgl.txt";
  
  private final String latexHeaderFooterFolder = "latex_header_footer";
  /** The Latex header with path for astrojournal by date. */
  public final String latexHeaderByDate = latexHeaderFooterFolder+"/header_by_date.tex";
  /** The Latex footer with path for astrojournal by date. */
  public final String latexFooterByDate = latexHeaderFooterFolder+"/footer_by_date.tex";
  /** The Latex header with path for astrojournal by target. */
  public final String latexHeaderByTarget = latexHeaderFooterFolder+"/header_by_target.tex";
  /** The Latex footer with path for astrojournal by target. */
  public final String latexFooterByTarget = latexHeaderFooterFolder+"/footer_by_target.tex";
  /** The Latex header with path for astrojournal by constellation. */
  public final String latexHeaderByConstellation = latexHeaderFooterFolder+"/header_by_constellation.tex";
  /** The Latex footer with path for astrojournal by constellation. */
  public final String latexFooterByConstellation = latexHeaderFooterFolder+"/footer_by_constellation.tex";
  
  
  // THESE PARAMETERS ARE CONFIGURABLE 
  /** True if latex output should be printed. */  
  public boolean latexOutput = false;  
  /** True if the application should run quietly */
  public boolean quiet = false;
  /** True if the version should be shown. */
  public boolean showVersion = true;

  /** The relative path containing the raw files (observation input folder). */
  public String rawReportsFolder = "raw_reports/";
  /** The name of the folder containing the latex observation files by date (observation output folder). */
  public String latexReportsFolderByDate = "latex_reports_by_date/";
  /** The name of the folder containing the latex observation files by target (observation output folder). */
  public String latexReportsFolderByTarget = "latex_reports_by_target/";
  /** The name of the folder containing the latex observation files by constellation (observation output folder). */
  public String latexReportsFolderByConstellation = "latex_reports_by_constellation/";
  /** The name of the folder containing the latex observation files by date (observation output folder). */
  public String sglReportsFolderByDate = "sgl_reports_by_date/";

  private AJConfig() {
    
    // Show version
    if (System.getProperty("aj.show_version") != null && 
        System.getProperty("aj.show_version").equals("true")) {
      showVersion = true;
    }
        
    // Quiet
    if (System.getProperty("aj.quiet") != null && 
        System.getProperty("aj.quiet").equals("true")) {
      quiet = true;
    }

    // Latex output
    if (System.getProperty("aj.latex_output") != null && 
        System.getProperty("aj.latex_output").equals("true")) {
      latexOutput = true;
    }

    
    // Raw reports folder
    if (System.getProperty("aj.raw_reports_folder") != null) {
      rawReportsFolder = new String(System.getProperty("aj.raw_reports_folder"));
    }
    
    // Latex reports folder by date
    if (System.getProperty("aj.latex_reports_folder_by_date") != null) {
      latexReportsFolderByDate = new String(System.getProperty("aj.latex_reports_folder_by_date"));
    }
    
    // Latex reports folder by target
    if (System.getProperty("aj.latex_reports_folder_by_target") != null) {
      latexReportsFolderByTarget = new String(System.getProperty("aj.latex_reports_folder_by_target"));
    }
    
    // Latex reports folder by constellation
    if (System.getProperty("aj.latex_reports_folder_by_constellation") != null) {
      latexReportsFolderByConstellation = new String(System.getProperty("aj.latex_reports_folder_by_constellation"));
    }

    // SGL reports folder by date
    if (System.getProperty("aj.sgl_reports_folder_by_date") != null) {
      sglReportsFolderByDate = new String(System.getProperty("aj.sgl_reports_folder_by_date"));
    }
    
    File inp = new File(rawReportsFolder);
    File out1 = new File(latexReportsFolderByDate);
    File out2 = new File(latexReportsFolderByTarget);
    File out3 = new File(latexReportsFolderByConstellation);
    File out4 = new File(sglReportsFolderByDate);
    // Create the folders if these do not exist.
    inp.mkdir();
    out1.mkdir();
    out2.mkdir();
    out3.mkdir();
    out4.mkdir();
    
    // Delete previous content if this was present.
    try {
      FileUtils.cleanDirectory(out1);
      FileUtils.cleanDirectory(out2);
      FileUtils.cleanDirectory(out3);
      FileUtils.cleanDirectory(out4);
    } catch (IOException e) {
      System.out.println("Impossible to clean the output directories. Does the program have right permission?");
      e.printStackTrace();
    }
    
  }

  /**
   * Return the instance of AJConfig.
   * @return the instance of AJConfig.
   */
  public static AJConfig getInstance() {
    return instance;
  }

  /**
   * Create a string containing details for AstroJournal.
   * @return a string
   */
  public String printVersion() {
    String version = 
        applicationName + " " + applicationVersion + 
        " is free software: you can redistribute it and/or modify \n" +
        "it under the terms of the GNU General Public License as published by \n" +
        "the Free Software Foundation, either version 3 of the License, or \n" +
        "(at your option) any later version. \n" +
        "AstroJournal is distributed in the hope that it will be useful, \n" +
        "but WITHOUT ANY WARRANTY; without even the implied warranty of \n" +
        "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the \n" +
        "GNU General Public License for more details. \n" +
        "You should have received a copy of the GNU General Public License \n" +
        "along with AstroJournal. If not, see <http://www.gnu.org/licenses/>. \n" + 
        "\n" + 
        "AstroJournal Web Site: <https://github.com/pdp10/AstroJournal>\n\n" +
        "\n" +
        printConfiguration();
    return version;
  }

  /**
   * print the current configuration.
   * @return the current configuration
   */
  public String printConfiguration() {
    String configuration = 
            "AstroJournal is running with the following configuration:\n" +
            "raw_reports_folder: " + rawReportsFolder + "\n" +
            "latex_reports_by_date: " + latexReportsFolderByDate + "\n" +
            "latex_reports_by_target: " + latexReportsFolderByTarget + "\n" +
            "latex_reports_by_constellation: " + latexReportsFolderByConstellation + "\n" +
            "sgl_reports_by_date: " + sglReportsFolderByDate + "\n\n";
    return configuration;
  }
  
}