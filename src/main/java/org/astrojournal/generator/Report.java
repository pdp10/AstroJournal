package org.astrojournal.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic report structure containing a list of meta data fields and a table
 * of contents. This class serves for transferring data. All the entries are
 * Strings. Values are not checked for consistency here. The only control
 * performed by this class is that the size of the added metadata / data must be
 * greater or equal than the size defined via the constructor. Therefore, this
 * class can contain a data table with different row lengths, but all of these
 * must be greater or equal than the defined data column size passed to the
 * constructor.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 */
public class Report implements Comparable<Report> {

    /** The report meta data. */
    private String[] metaData = null;

    /** The report data. */
    private List<String[]> data = new ArrayList<String[]>();

    /** The minimum number of columns the data must have. */
    private int dataColumnNumber = 1;

    /**
     * Default constructor. A report of one meta data field and one data column
     * is generated.
     */
    public Report() {
	this(1, 1);
    }

    /**
     * Constructor. A report of with arbitrary meta data length and data column
     * number is generated.
     * 
     * @param metaDataLength
     *            The length of the meta data content
     * @param dataColumnNumber
     *            The minimum number of columns for the data table
     */
    public Report(int metaDataLength, int dataColumnNumber) {
	metaData = new String[metaDataLength];
	this.dataColumnNumber = dataColumnNumber;
    }

    /**
     * Add an array of String objects containing the report meta data.
     * 
     * @param metaData
     *            the meta data
     * @return true if metaData.length is greater or equal than the defined meta
     *         data length defined at constructor level
     */
    public boolean addMetaData(String[] metaData) {
	if (this.metaData.length <= metaData.length) {
	    this.metaData = metaData;
	    return true;
	}
	return false;
    }

    /**
     * Return the meta data String array.
     * 
     * @return the meta data
     */
    public String[] getMetaData() {
	return metaData;
    }

    /**
     * Add an array of String objects containing a report data row.
     * 
     * @param data
     *            a data row
     * @return true if data.length is greater or equal than the defined
     *         dataColumnNumber defined at constructor level
     */
    public boolean addData(String[] data) {
	if (dataColumnNumber <= data.length) {
	    this.data.add(data);
	    return true;
	}
	return false;
    }

    /**
     * Return the i-th data row to retrieve or null.
     * 
     * @param i
     *            the i-th data row to retrieve
     * @return the i-th data row or null if i is greater or equal than the
     *         number of data row inserted
     */
    public String[] getData(int i) {
	if (i < data.size()) {
	    return data.get(i);
	}
	return null;
    }

    /**
     * Return the table containing the report data.
     * 
     * @return the report data table
     */
    public List<String[]> getAllData() {
	return data;
    }

    /**
     * Return the meta data length.
     * 
     * @return the meta data length
     */
    public int getMetaDataLength() {
	return metaData.length;
    }

    /**
     * Return the number of data rows.
     * 
     * @return the number of data rows
     */
    public int getDataRowNumber() {
	return data.size();
    }

    /**
     * Return the number of data columns.
     * 
     * @return the number of data columns
     */
    public int getdataColumnNumber() {
	return dataColumnNumber;
    }

    @Override
    public int compareTo(Report report) {
	String thisStr = metaData[0].replaceAll("[^\\d.]", "");
	String thatStr = report.getMetaData()[0].replaceAll("[^\\d.]", "");
	if (thisStr.length() == 8 && thatStr.length() == 8) {
	    thisStr = thisStr.substring(4) + thisStr.substring(2, 4)
		    + thisStr.substring(0, 2);
	    thatStr = thatStr.substring(4) + thatStr.substring(2, 4)
		    + thatStr.substring(0, 2);
	    return thisStr.compareTo(thatStr);
	} else if (thisStr.length() < thatStr.length()) {
	    return -1;
	}
	return 1;
    }
}
