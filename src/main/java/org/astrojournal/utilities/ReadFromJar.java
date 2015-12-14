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
package org.astrojournal.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A list of utilities for reading files from a jar file.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 13 Dec 2015
 */
public class ReadFromJar {

    /**
     * Read a filename resource from the application jar file.
     * 
     * @param fileNameInJar
     * @return The string content of the file in the Jar
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String getStringFileFromJARFile(String fileNameInJar)
	    throws FileNotFoundException, IOException {
	// We cannot load the file with File, because this file is inside a Jar
	// file and is not part of the usual file system. We need to use
	// a method like getClass().getResource()
	InputStream is = getClass().getResourceAsStream(fileNameInJar);
	if (is == null) {
	    throw new FileNotFoundException();
	}
	InputStreamReader isr = new InputStreamReader(is);
	BufferedReader br = new BufferedReader(isr);
	StringBuffer sb = new StringBuffer();
	String line;
	while ((line = br.readLine()) != null) {
	    sb.append(line).append("\n");
	}
	br.close();
	isr.close();
	is.close();
	return sb.toString();
    }

    /**
     * Read a temporary file containing resource from the application jar file.
     * A call to the method deleteOnExit() for this temporary file is
     * guaranteed.
     * 
     * @param tempFileName
     *            The temporary filename
     * @param fileNameInJar
     *            The filename in the jar file.
     * @return a temporary file containing the content of the extracted file in
     *         the Jar.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public File getFileFromJARFile(String tempFileName, String fileNameInJar)
	    throws FileNotFoundException, IOException {
	String fileString = getStringFileFromJARFile(fileNameInJar);
	File temp = File.createTempFile(tempFileName, ".tmp");
	BufferedWriter writer = null;
	try {
	    writer = new BufferedWriter(new FileWriter(temp));
	    writer.write(fileString);
	} finally {
	    if (writer != null)
		writer.close();
	}
	temp.deleteOnExit();
	return temp;
    }

}
