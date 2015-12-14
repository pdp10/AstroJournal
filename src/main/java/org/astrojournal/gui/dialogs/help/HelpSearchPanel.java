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
package org.astrojournal.gui.dialogs.help;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.astrojournal.configuration.AJConfig;

/**
 * The Class HelpSearchPanel.
 */
public class HelpSearchPanel extends JPanel implements ActionListener,
	ListSelectionListener, Runnable {

    private static final long serialVersionUID = 2861281767675063019L;

    /** The root. */
    private HelpIndexRoot root;

    /** The query field. */
    private JTextField queryField;

    /** The result list. */
    private JList resultList;

    /** The list model. */
    private DefaultListModel listModel;

    /** The search button. */
    private JButton searchButton;

    /** The dialog. */
    private HelpDialog dialog;

    /** The results scroll pane. */
    private JScrollPane resultsScrollPane;

    /**
     * Instantiates a new help search panel.
     * 
     * @param root
     *            the root
     * @param dialog
     *            the dialog
     */
    public HelpSearchPanel(HelpIndexRoot root, HelpDialog dialog) {
	this.root = root;
	this.dialog = dialog;
	initComponents();
    }

    /**
     * This method is called from within the constructor to initialise the form.
     */
    private void initComponents() {

	setLayout(new BorderLayout());

	JPanel queryPanel = new JPanel();
	queryPanel.setLayout(new BorderLayout());
	queryPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
	queryField = new JTextField();
	queryField.setActionCommand("search");
	queryField.addActionListener(this);
	queryPanel.add(queryField, BorderLayout.CENTER);
	searchButton = new JButton(
		AJConfig.BUNDLE.getString("AJ.lblSearch.text"));
	searchButton.setActionCommand("search");
	searchButton.addActionListener(this);
	queryPanel.add(searchButton, BorderLayout.EAST);
	add(queryPanel, BorderLayout.NORTH);

	listModel = new DefaultListModel();
	listModel.addElement(AJConfig.BUNDLE
		.getString("AJ.lblNoSearchResults.text"));
	resultList = new JList(listModel);
	resultList.addListSelectionListener(this);
	resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	resultsScrollPane = new JScrollPane(resultList);
	add(resultsScrollPane, BorderLayout.CENTER);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
	Thread t = new Thread(this);
	t.start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
     * .ListSelectionEvent)
     */
    @Override
    public void valueChanged(ListSelectionEvent lse) {
	Object o = resultList.getSelectedValue();
	if (o != null && o instanceof HelpPage) {
	    dialog.DisplayPage((HelpPage) o);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
	searchButton.setEnabled(false);
	listModel.removeAllElements();
	if (queryField.getText().trim().length() > 0) {
	    HelpPage[] results;
	    try {
		results = root.findPagesForTerm(queryField.getText().trim());
	    } catch (IOException e) {
		e.printStackTrace();
		searchButton.setEnabled(true);
		return;
	    }
	    if (results.length > 0) {
		for (int r = 0; r < results.length; r++) {
		    listModel.addElement(results[r]);
		}
	    } else {
		listModel.addElement(AJConfig.BUNDLE
			.getString("AJ.lblNoSearchResults.text"));
	    }
	}

	remove(resultsScrollPane);
	revalidate();
	resultList = new JList(listModel);
	resultList.addListSelectionListener(this);
	resultsScrollPane = new JScrollPane(resultList);
	add(resultsScrollPane, BorderLayout.CENTER);
	revalidate();
	repaint();

	searchButton.setEnabled(true);
    }
}
