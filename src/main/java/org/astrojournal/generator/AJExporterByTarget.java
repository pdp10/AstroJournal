/*
 * Copyright 2015 Piero Dalle Pezze
 *
 * This file is part of AstroJournal.
 *
 * AstroJournal is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.astrojournal.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.astrojournal.headerfooter.AJLatexFooter;
import org.astrojournal.headerfooter.AJLatexHeader;
import org.astrojournal.observation.AJObservation;
import org.astrojournal.observation.AJObservationItem;

/**
 * Exports an AstroJournal set of observations by target to Latex code.
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 22/07/2015
 */
public class AJExporterByTarget extends AJExporter {

    /** The log associated to this class */
    private static Logger log = Logger.getLogger(AJExporterByTarget.class);

    /** A cache of the visited targets. */
    private HashSet<String> processedTargetCache = new HashSet<String>(1000);

    /** A comparator for sorting catalogues */
    private Comparator<String> catalogueItemComparator = new Comparator<String>() {
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

    /**
     * Default constructor
     * 
     * @param ajFilesLocation
     */
    public AJExporterByTarget(File ajFilesLocation) {
	super(ajFilesLocation);
    }

    /**
     * Generate the Latex document sorting the observation by decreasing target
     * 
     * @param latexReportsFolderByTarget
     *            the directory containing the single observations by target in
     *            latex format (output)
     * @param latexHeaderByTarget
     *            the latex header code (by target)
     * @param latexMainByTarget
     *            the latex main body code (by target)
     * @param latexFooterByTarget
     *            the latex footer code (by target)
     */
    @Override
    public void generateJournal(String latexReportsFolderByTarget,
	    String latexHeaderByTarget, String latexMainByTarget,
	    String latexFooterByTarget) {
	AJLatexHeader ajLatexHeaderByTarget = new AJLatexHeader(
		ajFilesLocation.getAbsolutePath(), latexHeaderByTarget);
	AJLatexFooter ajLatexFooterByTarget = new AJLatexFooter(
		ajFilesLocation.getAbsolutePath(), latexFooterByTarget);
	Writer writerByTarget = null;
	try {
	    writerByTarget = new BufferedWriter(new OutputStreamWriter(
		    new FileOutputStream(ajFilesLocation.getAbsolutePath()
			    + File.separator + latexMainByTarget), "utf-8"));
	    // write the Latex Header
	    writerByTarget.write(ajLatexHeaderByTarget.getHeader());
	    // write the Latex Body
	    // Write the observation reports
	    // parse each file in the latex obs folder (sorted by observation
	    // increasing)
	    File[] files = new File(ajFilesLocation.getAbsolutePath()
		    + File.separator + latexReportsFolderByTarget).listFiles();
	    if (files == null) {
		log.warn("Folder " + ajFilesLocation.getAbsolutePath()
			+ File.separator + latexReportsFolderByTarget
			+ " not found");
		return;
	    }
	    sortFilesByTarget(files);
	    // If this pathname does not denote a directory, then listFiles()
	    // returns
	    // null.
	    String target = null, type = "";
	    for (File file : files) {
		target = file.getName();
		if (file.isFile() && target.endsWith(".tex")) {
		    if (target
			    .matches("^(sun|moon|mercury|venus|mars|asteroid|jupiter|saturn|uranus|neptune|pluto|comet|Sun|Moon|Mercury|Venus|Mars|Asteroid|Jupiter|Saturn|Uranus|Neptune|Pluto|Comet).*$")) {
			if (!type.equals("Solar System")) {
			    type = "Solar System";
			    writerByTarget.write("\\clearpage\n");
			    writerByTarget.write("\\section{" + type + "}\n");
			}
		    } else if (target
			    .matches("^(milkyway|MilkyWay|MILKYWAY).*$")) {
			if (!type.equals("Milky Way")) {
			    type = "Milky Way";
			    writerByTarget.write("\\clearpage\n");
			    writerByTarget.write("\\section{" + type + "}\n");
			}
		    } else if (target.matches("^(m|M)[0-9].*$")) {
			if (!type.equals("Messier Catalogue")) {
			    type = "Messier Catalogue";
			    writerByTarget.write("\\clearpage\n");
			    writerByTarget.write("\\section{" + type + "}\n");
			}
		    } else if (target.matches("^(ngc|NGC)[0-9].*$")) {
			if (!type.equals("New General Catalogue (NGC)")) {
			    type = "New General Catalogue (NGC)";
			    writerByTarget.write("\\clearpage\n");
			    writerByTarget.write("\\section{" + type + "}\n");
			}
		    } else if (target.matches("^(ic|IC)[0-9].*$")) {
			if (!type.equals("Index Catalogue (IC)")) {
			    type = "Index Catalogue (IC)";
			    writerByTarget.write("\\clearpage\n");
			    writerByTarget.write("\\section{" + type + "}\n");
			}
		    } else if (target.matches("^(stock|Stock|STOCK)[0-9].*$")) {
			if (!type.equals("Stock Catalogue")) {
			    type = "Stock Catalogue";
			    writerByTarget.write("\\clearpage\n");
			    writerByTarget.write("\\section{" + type + "}\n");
			}
		    } else if (target.matches("^(mel|Mel|MEL)[0-9].*$")) {
			if (!type.equals("Melotte Catalogue")) {
			    type = "Melotte Catalogue";
			    writerByTarget.write("\\clearpage\n");
			    writerByTarget.write("\\section{" + type + "}\n");
			}
		    } else if (target.matches("^(cr|Cr|CR)[0-9].*$")) {
			if (!type.equals("Collider Catalogue")) {
			    type = "Collider Catalogue";
			    writerByTarget.write("\\clearpage\n");
			    writerByTarget.write("\\section{" + type + "}\n");
			}
		    } else if (target.matches("^(pk|PK)[0-9].*$")) {
			if (!type.equals("Perek-Kohoutek Catalogue")) {
			    type = "Perek-Kohoutek Catalogue";
			    writerByTarget.write("\\clearpage\n");
			    writerByTarget.write("\\section{" + type + "}\n");
			}
		    } else if (target.matches("^(b|B|Barnard|BARNARD)[0-9].*$")) {
			if (!type.equals("Barnard Catalogue")) {
			    type = "Barnard Catalogue";
			    writerByTarget.write("\\clearpage\n");
			    writerByTarget.write("\\section{" + type + "}\n");
			}
		    } else if (target
			    .matches("^(hcg|HCG|Hickson Compact Group)[0-9].*$")) {
			if (!type.equals("Hickson Compact Group Catalogue")) {
			    type = "Hickson Compact Group Catalogue";
			    writerByTarget.write("\\clearpage\n");
			    writerByTarget.write("\\section{" + type + "}\n");
			}
		    } else if (target.matches("^(ugc|UGC)[0-9].*$")) {
			if (!type.equals("Uppsala General Catalogue")) {
			    type = "Uppsala General Catalogue";
			    writerByTarget.write("\\clearpage\n");
			    writerByTarget.write("\\section{" + type + "}\n");
			}
		    } else if (target.matches("^(Steph)[0-9].*$")) {
			if (!type.equals("Other Catalogues")) {
			    type = "Other Catalogues";
			    writerByTarget.write("\\clearpage\n");
			    writerByTarget.write("\\section{" + type + "}\n");
			}
		    } else {
			if (!type.equals("Stars, Double Stars, Multiple Stars")) {
			    type = "Stars, Double Stars, Multiple Stars";
			    writerByTarget.write("\\clearpage\n");
			    writerByTarget.write("\\section{" + type + "}\n");
			}
		    }
		    // include the file removing the extension .tex
		    writerByTarget.write("\\input{"
			    + latexReportsFolderByTarget + "/"
			    + target.replaceFirst("[.][^.]+$", "") + "}\n");
		    writerByTarget.write("\\vspace{4 mm}\n");
		}
	    }
	    // write the Latex Footer
	    writerByTarget.write(ajLatexFooterByTarget.getFooter());
	} catch (IOException ex) {
	    log.warn("Error when opening the file "
		    + ajFilesLocation.getAbsolutePath() + File.separator
		    + latexMainByTarget);
	} catch (Exception ex) {
	    log.warn(ex);
	} finally {
	    try {
		if (writerByTarget != null)
		    writerByTarget.close();
	    } catch (Exception ex) {
	    }
	}
    }

    /**
     * Exports a list of observations by target to Latex
     * 
     * @param observations
     *            the list of observations to exportObservation
     * @param latexReportsByTargetFolder
     *            the folder to write the observation in.
     * @return true if the observations are exported
     */
    @Override
    public boolean exportObservations(ArrayList<AJObservation> observations,
	    String latexReportsByTargetFolder) {
	processedTargetCache.clear();
	for (int i = 0; i < observations.size(); i++) {
	    AJObservation obs = observations.get(i);
	    ArrayList<AJObservationItem> observationItems = obs
		    .getObservationItems();
	    for (int j = 0; j < observationItems.size(); j++) {
		AJObservationItem obsItem = observationItems.get(j);
		String filenameOut = computeFileName(obsItem);
		log.debug("filename: " + filenameOut);
		Writer targetWriter = null;
		try {
		    if (!processedTargetCache.contains(filenameOut)) {
			processedTargetCache.add(filenameOut);
			targetWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(
					new File(ajFilesLocation
						.getAbsolutePath()
						+ File.separator
						+ latexReportsByTargetFolder,
						filenameOut + ".tex")), "utf-8"));
			if (obsItem.getType().toLowerCase().equals("planet")
				|| obsItem.getTarget().toLowerCase()
					.equals("moon")
				|| obsItem.getTarget().toLowerCase()
					.equals("sun")
				|| obsItem.getType().toLowerCase()
					.equals("asteroid")
				|| obsItem.getType().toLowerCase()
					.equals("comet")) {
			    targetWriter.write("\\subsection{"
				    + obsItem.getTarget());
			} else if (obsItem.getType().toLowerCase()
				.equals("star")
				|| obsItem.getType().toLowerCase()
					.equals("dbl star")
				|| obsItem.getType().toLowerCase()
					.equals("mlt star")) {
			    targetWriter.write("\\subsection{"
				    + obsItem.getConstellation());
			    targetWriter.write(", " + obsItem.getTarget());
			} else if (obsItem.getType().toLowerCase()
				.equals("galaxy")
				&& obsItem.getTarget().toLowerCase()
					.equals("milky way")) {
			    // Don't print the constellation if we are
			    // processing the milky way!
			    targetWriter.write("\\subsection{"
				    + obsItem.getTarget());
			} else {
			    targetWriter.write("\\subsection{"
				    + obsItem.getTarget());
			    targetWriter.write(", "
				    + obsItem.getConstellation());
			}
			targetWriter.write(", " + obsItem.getType() + "}\n");
			targetWriter.write("\\begin{itemize}\n");
		    } else {
			// if file was already created skip the previous two
			// lines
			targetWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(
					new File(ajFilesLocation
						.getAbsolutePath()
						+ File.separator
						+ latexReportsByTargetFolder,
						filenameOut + ".tex"), true),
					"utf-8"));
		    }
		    targetWriter.write("\\item " + obs.getDate() + " "
			    + obs.getTime() + ", " + obs.getLocation() + ". "
			    + obs.getSeeing() + ", " + obs.getTransparency()
			    + ". " + obs.getTelescopes() + ", "
			    + obsItem.getPower() + ". " + obsItem.getNotes()
			    + "\n");

		    // do not close the Latex 'itemize' block now because
		    // nothing is known about other observations
		    // for this target.

		} catch (IOException ex) {
		    log.warn("Error when opening the file");
		    return false;
		} catch (Exception ex) {
		    log.warn(ex);
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
     * 
     * @param observations
     *            the list of observations to exportObservation
     * @param latexReportsByTargetFolder
     *            the folder to write the observation in.
     * @return true if the lists are closed
     */
    private boolean closeLists(ArrayList<AJObservation> observations,
	    String latexReportsByTargetFolder) {
	processedTargetCache.clear();
	for (int i = 0; i < observations.size(); i++) {
	    AJObservation obs = observations.get(i);
	    ArrayList<AJObservationItem> observationItems = obs
		    .getObservationItems();
	    for (int j = 0; j < observationItems.size(); j++) {
		AJObservationItem obsItem = observationItems.get(j);
		String filenameOut = computeFileName(obsItem);
		Writer targetWriter = null;
		try {
		    if (!processedTargetCache.contains(filenameOut)) {
			processedTargetCache.add(filenameOut);
			targetWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(
					new File(ajFilesLocation
						.getAbsolutePath()
						+ File.separator
						+ latexReportsByTargetFolder,
						filenameOut + ".tex"), true),
					"utf-8"));
			targetWriter.write("\\end{itemize}\n");
			System.out.println("\tExported target " + filenameOut);
		    }

		} catch (IOException ex) {
		    log.warn("Error when opening the file");
		    return false;
		} catch (Exception ex) {
		    log.warn(ex);
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
     * Create the filename with different formats depending on whether this is a
     * planet, a double/multiple star system, or everything else
     * 
     * @return the name of the file
     */
    private String computeFileName(AJObservationItem obsItem) {
	if (obsItem.getType().toLowerCase().equals("planet")
		|| obsItem.getTarget().toLowerCase().equals("moon")
		|| obsItem.getTarget().toLowerCase().equals("sun")) {
	    return obsItem.getTarget().replaceAll("\\s+", "")
		    .replaceAll("/", "-");
	}
	if (obsItem.getType().toLowerCase().equals("asteroid")
		|| obsItem.getType().toLowerCase().equals("comet")) {
	    return obsItem.getType().replaceAll("\\s+", "")
		    .replaceAll("/", "-");
	}
	if (obsItem.getType().toLowerCase().equals("star")
		|| obsItem.getType().toLowerCase().equals("dbl star")
		|| obsItem.getType().toLowerCase().equals("mlt star")) {
	    return obsItem.getConstellation()
		    + "_"
		    + obsItem.getTarget().replaceAll("\\s+", "")
			    .replaceAll("/", "-");
	}
	if (obsItem.getType().toLowerCase().equals("galaxy")
		&& obsItem.getTarget().toLowerCase().equals("milky way")) {
	    // Don't print the constellation if we are processing the milky way!
	    return obsItem.getTarget().replaceAll("\\s+", "")
		    .replaceAll("/", "-");
	}
	return obsItem.getTarget().replaceAll("\\s+", "").replaceAll("/", "-")
		+ "_" + obsItem.getConstellation();
    }

    /**
     * Recreate the list of files as sorted by catalogue
     * 
     * @param list
     *            the sorted catalogue
     * @param files
     *            the full list of files
     * @param idx
     *            the current index for files
     * @return idx the new index for files
     */
    private int addSortedFiles(List<String> list, File[] files, int idx) {
	for (int i = 0; i < list.size(); i++) {
	    if (idx < files.length) {
		files[idx] = new File(list.get(i));
		idx++;
	    }
	}
	return idx;
    }

    /**
     * Sort the files by target.
     * 
     * @param files
     *            the files to be sorted by target
     */
    private void sortFilesByTarget(File[] files) {
	// solar system in ArrayList instead of simple array, because we do not
	// know
	// how many conjunctions there are.
	LinkedList<String> solarSystem = new LinkedList<String>();
	ArrayList<String> milkyWay = new ArrayList<String>(1);
	ArrayList<String> messier = new ArrayList<String>(110);
	ArrayList<String> ngc = new ArrayList<String>(10000);
	ArrayList<String> ic = new ArrayList<String>(1000);
	ArrayList<String> stock = new ArrayList<String>(100);
	ArrayList<String> melotte = new ArrayList<String>(400);
	ArrayList<String> collider = new ArrayList<String>(300);
	ArrayList<String> pk = new ArrayList<String>(1143);
	ArrayList<String> barnard = new ArrayList<String>(366);
	ArrayList<String> hickson = new ArrayList<String>(100);
	ArrayList<String> abell = new ArrayList<String>(4073);
	ArrayList<String> ugc = new ArrayList<String>(12921);
	ArrayList<String> others = new ArrayList<String>(500);
	ArrayList<String> stars = new ArrayList<String>(500);

	// Add empty data for the solar system. Conjunctions will be added in
	// the end.
	solarSystem.add("");
	solarSystem.add("");
	solarSystem.add("");
	solarSystem.add("");
	solarSystem.add("");
	solarSystem.add("");
	solarSystem.add("");
	solarSystem.add("");
	solarSystem.add("");
	solarSystem.add("");
	String target = null;
	for (int i = 0; i < files.length; i++) {
	    target = files[i].getName();
	    if (target
		    .matches("^(sun|moon|mercury|venus|mars|asteroid|jupiter|saturn|uranus|neptune|pluto|comet|Sun|Moon|Mercury|Venus|Mars|Asteroid|Jupiter|Saturn|Uranus|Neptune|Pluto|Comet).*$")) {
		if (target.matches("^(sun|Sun)\\.tex$")) {
		    solarSystem.remove(0);
		    solarSystem.add(0, files[i].toString());
		} else if (target.matches("^(moon|Moon)\\.tex$")) {
		    solarSystem.remove(1);
		    solarSystem.add(1, files[i].toString());
		} else if (target.matches("^(mercury|Mercury)\\.tex$")) {
		    solarSystem.remove(2);
		    solarSystem.add(2, files[i].toString());
		} else if (target.matches("^(venus|Venus)\\.tex$")) {
		    solarSystem.remove(3);
		    solarSystem.add(3, files[i].toString());
		} else if (target.matches("^(mars|Mars)\\.tex$")) {
		    solarSystem.remove(4);
		    solarSystem.add(4, files[i].toString());
		} else if (target.matches("^(asteroid|Asteroid)\\.tex$")) {
		    solarSystem.remove(5);
		    solarSystem.add(5, files[i].toString());
		} else if (target.matches("^(jupiter|Jupiter)\\.tex$")) {
		    solarSystem.remove(6);
		    solarSystem.add(6, files[i].toString());
		} else if (target.matches("^(saturn|Saturn)\\.tex$")) {
		    solarSystem.remove(7);
		    solarSystem.add(7, files[i].toString());
		} else if (target.matches("^(uranus|Uranus)\\.tex$")) {
		    solarSystem.remove(8);
		    solarSystem.add(8, files[i].toString());
		} else if (target.matches("^(neptune|Neptune)\\.tex$")) {
		    solarSystem.remove(9);
		    solarSystem.add(9, files[i].toString());
		} else if (target.matches("^(pluto|Pluto)\\.tex$")) {
		    solarSystem.remove(10);
		    solarSystem.add(10, files[i].toString());
		} else if (target.matches("^(comet|Comet)\\.tex$")) {
		    solarSystem.remove(11);
		    solarSystem.add(11, files[i].toString());
		}
		// conjunctions
		else {
		    solarSystem.add(files[i].toString());
		}
		log.debug(target);
	    } else if (target.matches("^(milkyway|MilkyWay|MILKYWAY).*$")) {
		milkyWay.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(m|M)[0-9].*$")) {
		messier.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(ngc|NGC)[0-9].*$")) {
		ngc.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(ic|IC)[0-9].*$")) {
		ic.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(stock|Stock|STOCK)[0-9].*$")) {
		stock.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(mel|Mel|MEL)[0-9].*$")) {
		melotte.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(cr|Cr|CR)[0-9].*$")) {
		collider.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(b|B|Barnard|BARNARD)[0-9].*$")) {
		barnard.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(pk|PK)[0-9].*$")) {
		pk.add(files[i].toString());
		log.debug(target);
	    } else if (target
		    .matches("^(hcg|HCG|Hickson Compact Group)[0-9].*$")) {
		hickson.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(Abell|ABELL)[0-9].*$")) {
		abell.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(ugc|UGC)[0-9].*$")) {
		ugc.add(files[i].toString());
		log.debug(target);
	    } else if (target.matches("^(Steph)[0-9].*$")) {
		others.add(files[i].toString());
		log.debug(target);
	    } else {
		stars.add(files[i].toString());
		log.debug(target);
	    }
	}
	// note Planets and Milky Way are manually sorted
	Collections.sort(messier, catalogueItemComparator);
	Collections.sort(ngc, catalogueItemComparator);
	Collections.sort(ic, catalogueItemComparator);
	Collections.sort(stock, catalogueItemComparator);
	Collections.sort(melotte, catalogueItemComparator);
	Collections.sort(collider, catalogueItemComparator);
	Collections.sort(pk, catalogueItemComparator);
	Collections.sort(barnard, catalogueItemComparator);
	Collections.sort(hickson, catalogueItemComparator);
	Collections.sort(abell, catalogueItemComparator);
	Collections.sort(ugc, catalogueItemComparator);
	Collections.sort(others, catalogueItemComparator);
	// normal lexico-graphical sorting for stars
	Collections.sort(stars);

	int j = 0;
	j = addSortedFiles(solarSystem, files, j);
	j = addSortedFiles(milkyWay, files, j);
	j = addSortedFiles(messier, files, j);
	j = addSortedFiles(ngc, files, j);
	j = addSortedFiles(ic, files, j);
	j = addSortedFiles(stock, files, j);
	j = addSortedFiles(melotte, files, j);
	j = addSortedFiles(collider, files, j);
	j = addSortedFiles(pk, files, j);
	j = addSortedFiles(barnard, files, j);
	j = addSortedFiles(hickson, files, j);
	j = addSortedFiles(abell, files, j);
	j = addSortedFiles(ugc, files, j);
	j = addSortedFiles(others, files, j);
	j = addSortedFiles(stars, files, j);

    }

}
