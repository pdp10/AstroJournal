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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

/**
 * A class for capturing the console output (e.g.
 * System.out.println("some text") ).
 * http://stackoverflow.com/questions/8708342/redirect-console-output-to-string-
 * in-java
 * 
 * @author Manasjyoti Sharma
 * @version $Rev$
 * @since 1.0
 * @date 12 Dec 2015
 */
public class ConsoleOutputCapturer {
    private ByteArrayOutputStream baos;
    private PrintStream previous;
    private boolean capturing;

    /**
     * Start capturing the console output.
     */
    public void start() {
	if (capturing) {
	    return;
	}

	capturing = true;
	previous = System.out;
	baos = new ByteArrayOutputStream();

	OutputStream outputStreamCombiner = new OutputStreamCombiner(
		Arrays.asList(previous, baos));
	PrintStream custom = new PrintStream(outputStreamCombiner);

	System.setOut(custom);
    }

    /**
     * Stop capturing the console output.
     * 
     * @return the string containing the console output
     */
    public String stop() {
	if (!capturing) {
	    return "";
	}

	System.setOut(previous);

	String capturedValue = baos.toString();

	baos = null;
	previous = null;
	capturing = false;

	return capturedValue;
    }

    private static class OutputStreamCombiner extends OutputStream {
	private List<OutputStream> outputStreams;

	public OutputStreamCombiner(List<OutputStream> outputStreams) {
	    this.outputStreams = outputStreams;
	}

	@Override
	public void write(int b) throws IOException {
	    for (OutputStream os : outputStreams) {
		os.write(b);
	    }
	}

	@Override
	public void flush() throws IOException {
	    for (OutputStream os : outputStreams) {
		os.flush();
	    }
	}

	@Override
	public void close() throws IOException {
	    for (OutputStream os : outputStreams) {
		os.close();
	    }
	}
    }
}
