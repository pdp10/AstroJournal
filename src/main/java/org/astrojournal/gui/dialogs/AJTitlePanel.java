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
 * - Piero Dalle Pezze: Class creation.
 */
package org.astrojournal.gui.dialogs;

import java.awt.BorderLayout;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astrojournal.configuration.ajconfiguration.AJMetaInfo;
import org.astrojournal.utilities.DesktopBrowse;

/**
 * A panel containing a title for AstroJournal.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 9 Jan 2016
 */
public class AJTitlePanel extends JPanel {

    private static final long serialVersionUID = -6666510405157515963L;

    /** The log associated to this class */
    private static Logger log = LogManager.getLogger(AJTitlePanel.class);

    /**
     * Constructor.
     */
    public AJTitlePanel() {
	super();
	initComponents();
    }

    /**
     * This method is called from within the constructor to initialise the form.
     */
    private void initComponents() {

	setLayout(new BorderLayout());

	ImageIcon logo = new ImageIcon(
		ClassLoader.getSystemResource("graphics/logo/aj_icon_150.png"));
	JPanel logoPanel = new JPanel();
	logoPanel.add(new JLabel("", logo, JLabel.CENTER));
	logoPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
	add(logoPanel, BorderLayout.WEST);

	JTextPane textPane = new JTextPane();
	textPane.setEditable(false);
	textPane.setOpaque(false);
	textPane.setEditorKit(JEditorPane
		.createEditorKitForContentType("text/html"));
	textPane.setText("<a href='" + AJMetaInfo.WEBSITE.getInfo() + "'>"
		+ AJMetaInfo.WEBSITE.getInfo() + "</a>");

	textPane.addHyperlinkListener(new HyperlinkListener() {
	    @Override
	    public void hyperlinkUpdate(HyperlinkEvent hle) {
		if (HyperlinkEvent.EventType.ACTIVATED.equals(hle
			.getEventType())) {
		    try {
			DesktopBrowse.browse(hle.getURL().toURI());
		    } catch (URISyntaxException e) {
			log.error(e, e);
		    }
		}
	    }
	});

	add(textPane, BorderLayout.CENTER);
	try {
	    Document doc = textPane.getDocument();
	    Style style = textPane.addStyle("Title", null);
	    StyleConstants.setFontSize(style, 12);
	    doc.insertString(
		    doc.getLength(),
		    "\n\n\u00a9 Piero Dalle Pezze, 2015-16\n\n"
			    + "AstroJournal is a free software distributed under "
			    + "the terms of the GNU General Public License (GNU GPL v3).\n\n"
			    + "AstroJournal uses the following third-party software libraries:\n"
			    + "Tango base icon theme \u00a9 Public Domain; "
			    + "Apache Commons (io, lang3) \u00a9 Apache Software Foundation, 2015; "
			    + "Apache Logging Services (log4j2) \u00a9 Apache Software Foundation, 2014; "
			    + "JUnit \u00a9 JUnit Team, 2015; "
			    + "Hamcrest (hamcrest-junit) \u00a9 BSD 3-Clause License, 2015.",
		    style);

	} catch (BadLocationException e) {
	    log.error(e, e);
	}

    }

}