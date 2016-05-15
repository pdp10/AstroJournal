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
/*
 * Changelog:
 * - Piero Dalle Pezze: class creation.
 */
package org.astrojournal.generator;

import java.util.List;

import org.astrojournal.configuration.Configuration;
import org.astrojournal.generator.absgen.Exporter;
import org.astrojournal.generator.absgen.Importer;

/**
 * Interface of a generator.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public interface Generator {

    /**
     * Return the configuration.
     * 
     * @return config
     */
    public Configuration getConfiguration();

    /**
     * Set a new configuration
     * 
     * @param config
     */
    public void setConfiguration(Configuration config);

    /**
     * It generates the files for AstroJournal.
     * 
     * @return true if the journals have been generated.
     */
    public boolean generateJournals();

    /**
     * Returns the imported reports.
     * 
     * @return the reports
     */
    public List<Report> getReports();

    /**
     * Returns the list of Importer objects.
     * 
     * @return the Importers
     */
    public List<Importer> getImporters();

    /**
     * Returns the list of Exporter objects.
     * 
     * @return the Exporters
     */
    public List<Exporter> getExporters();

    /**
     * Reset the generator.
     */
    public void reset();

    /**
     * Reset the Importers.
     */
    public void resetImporters();

    /**
     * Reset the Exporters.
     */
    public void resetExporters();

    /**
     * Export the reports using the available Importers.
     * 
     * @return true if the reports have been imported.
     */
    public boolean ajImport();

    /**
     * Export the reports using the available Exporters.
     * 
     * @return true if the reports have been exported.
     */
    public boolean ajExport();

    /**
     * Runs the method postProcessing() for each available ajExporter.
     * 
     * @return true if the post processing succeeded.
     */
    public boolean postProcessing();
}
