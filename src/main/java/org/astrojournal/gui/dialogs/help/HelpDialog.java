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

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.astrojournal.configuration.AJConfig;

/**
 * The Class HelpDialog is the root window of the help system.
 */
public class HelpDialog extends JDialog implements TreeSelectionListener {

    private static final long serialVersionUID = -5895752006807164521L;

    /** The tree. */
    private JTree tree;

    /** The current page. */
    private HelpPageDisplay currentPage = null;

    /** The main split. */
    private JSplitPane mainSplit;

    /**
     * Instantiates a new help dialog.
     * 
     * @param parent
     *            the parent
     * @param startingLocation
     *            the starting location
     * @throws FileNotFoundException
     *             if startingLocation does not exist.
     */
    public HelpDialog(JFrame parent, File startingLocation)
	    throws FileNotFoundException {
	super(parent, AJConfig.BUNDLE.getString("AJ.mnuHelpContents.text"));
	if (!startingLocation.exists()) {
	    throw new FileNotFoundException();
	}
	initComponents(parent, startingLocation);
    }

    /**
     * This method is called from within the constructor to initialise the form.
     */
    private void initComponents(JFrame parent, File startingLocation) {

	HelpIndexRoot root = new HelpIndexRoot(startingLocation);

	mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	setContentPane(mainSplit);

	tree = new JTree(new DefaultTreeModel(root));

	JSplitPane leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	leftSplit.setTopComponent(new JScrollPane(tree));
	leftSplit.setBottomComponent(new HelpSearchPanel(root, this));

	mainSplit.setLeftComponent(leftSplit);
	currentPage = new HelpPageDisplay(null);
	mainSplit.setRightComponent(currentPage);

	tree.addTreeSelectionListener(this);

	setSize(580, 500);
	setLocationRelativeTo(parent);
	setVisible(true);

	leftSplit.setDividerLocation(0.7);
	mainSplit.setDividerLocation(0.3);
	findStartingPage();
    }

    /**
     * Find starting page.
     */
    private void findStartingPage() {
	DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tree
		.getModel().getRoot();

	DisplayPage((HelpPage) currentNode.getFirstLeaf());
    }

    /**
     * Display page.
     * 
     * @param page
     *            the page
     */
    public void DisplayPage(HelpPage page) {
	if (currentPage != null) {
	    int d = mainSplit.getDividerLocation();
	    mainSplit.remove(currentPage);
	    currentPage = new HelpPageDisplay(page);
	    mainSplit.setRightComponent(currentPage);
	    mainSplit.setDividerLocation(d);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event
     * .TreeSelectionEvent)
     */
    @Override
    public void valueChanged(TreeSelectionEvent tse) {

	if (tse.getNewLeadSelectionPath() == null)
	    return;

	Object o = tse.getNewLeadSelectionPath().getLastPathComponent();
	if (o instanceof HelpPage && ((HelpPage) o).isLeaf()) {
	    DisplayPage((HelpPage) o);
	}
    }

}
