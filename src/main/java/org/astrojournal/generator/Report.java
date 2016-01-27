package org.astrojournal.generator;

import java.util.ArrayList;
import java.util.List;

import org.astrojournal.generator.reportdata.ReportHeader;
import org.astrojournal.generator.reportdata.ReportItem;

public class Report {

    private ReportHeader dataHeader = null;
    private List<ReportItem> reportItems = new ArrayList<ReportItem>();

    public Report() {
    }

    public void setDataHeader(ReportHeader dataHeader) {
	this.dataHeader = dataHeader;
    }

    public ReportHeader getReportHeader() {
	return dataHeader;
    }

    public void addReportItem(ReportItem reportItem) {
	reportItems.add(reportItem);
    }

    public List<ReportItem> getReportItems() {
	return reportItems;
    }
}