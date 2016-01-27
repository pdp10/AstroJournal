package org.astrojournal.generator.reportdata;

public interface ReportItemNotes extends ReportItem {

    /** A string containing the notes name. */
    public static final String NOTES_NAME = "Notes";

    /**
     * Sets the notes about this target
     * 
     * @param notes
     */
    public void setNotes(String notes);

    /**
     * Returns the notes about this target
     * 
     * @return notes
     */
    public String getNotes();
}
