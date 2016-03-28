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
package org.astrojournal.generator.statistics;

import java.util.HashMap;

/**
 * A collector of statistics
 * 
 * @author Piero Dalle Pezze
 * @version 0.1
 * @since 28/03/2016
 */
public class TargetStatistics {

    private HashMap<String, MutableInt> counting = new HashMap<String, MutableInt>();

    /** Constructor. */
    public TargetStatistics() {
	reset();
    }

    /**
     * Reset the statistics.
     */
    public void reset() {
	counting.put("solar system", new MutableInt());
	counting.put("cl+neb", new MutableInt());
	counting.put("opn cl", new MutableInt());
	counting.put("glob cl", new MutableInt());
	counting.put("galaxy", new MutableInt());
	counting.put("pln neb", new MutableInt());
	counting.put("sn rem", new MutableInt());
	counting.put("neb", new MutableInt());
	counting.put("quasar", new MutableInt());
	counting.put("star", new MutableInt());
    }

    /**
     * Increment the counting for this target type.
     * 
     * @param type
     *            The type to increment.
     */
    public void increment(String type) {
	if (counting.containsKey(type)) {
	    counting.put(type, counting.get(type).increment());
	}
    }

    /**
     * Return the counting for this target type.
     * 
     * @param type
     *            the target type
     * @return the counting
     */
    public int getCount(String type) {
	if (counting.containsKey(type)) {
	    return counting.get(type).value;
	}
	return 0;
    }

    /** A class wrapping an int */
    class MutableInt {
	/** The wrapped variable */
	private int value = 0;

	/**
	 * Increment the MutableInt wrapper.
	 * 
	 * @return the incremented wrapper
	 */
	public MutableInt increment() {
	    value++;
	    return this;
	}

	/**
	 * Return the wrapped variable.
	 * 
	 * @return the wrapped variable.
	 */
	public int get() {
	    return value;
	}
    }
}
