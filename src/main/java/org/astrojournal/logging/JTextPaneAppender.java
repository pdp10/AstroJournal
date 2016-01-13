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
package org.astrojournal.logging;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.astrojournal.configuration.AJConfig;

/**
 * A log4j2 appender to a JTextPane.
 * 
 * @author Piero Dalle Pezze
 * @version $Rev$
 * @since 1.0
 * @date 12 Jan 2016
 */
@Plugin(name = "JTextPaneAppender", category = "Core", elementType = "appender", printObject = true)
public class JTextPaneAppender extends AbstractAppender {

    private static final long serialVersionUID = -1616989641502748537L;

    private static JTextPane jTextPane = new JTextPane();

    private static Style styleRegular;
    private static Style styleItalic;
    private static Style styleBold;
    private static Style styleSmall;
    private static Style styleSmallItalic;
    private static Style styleRed;
    private static Style styleBlue;

    private int maxLines = 0;

    /**
     * The constructor for this appender.
     * 
     * @param name
     * @param layout
     * @param filter
     * @param maxLines
     * @param ignoreExceptions
     */
    protected JTextPaneAppender(String name, Layout<?> layout, Filter filter,
	    int maxLines, boolean ignoreExceptions) {
	super(name, filter, layout, ignoreExceptions);
	this.maxLines = maxLines;
    }

    /**
     * A method for initialising this appender statically.
     * 
     * @param name
     * @param maxLines
     * @param ignoreExceptions
     * @param layout
     * @param filter
     * @return the appender.
     */
    @PluginFactory
    public static JTextPaneAppender createAppender(
	    @PluginAttribute("name") String name,
	    @PluginAttribute("maxLines") int maxLines,
	    @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
	    @PluginElement("Layout") Layout<?> layout,
	    @PluginElement("Filters") Filter filter) {

	if (name == null) {
	    LOGGER.error("Please, report a name for JTextPaneAppender");
	    return null;
	}

	if (layout == null) {
	    layout = PatternLayout.createDefaultLayout();
	}

	return new JTextPaneAppender(name, layout, filter, maxLines,
		ignoreExceptions);
    }

    /**
     * Add the graphical JTextPane to capture the logging information.
     * 
     * @param textPane
     */
    public static void addJTextPane(final JTextPane textPane) {
	// The application graphical output
	jTextPane = textPane;

	// Define JTextPane styles
	StyledDocument doc = jTextPane.getStyledDocument();
	styleRegular = doc.addStyle("regular", null);
	StyleConstants.setForeground(styleRegular, Color.BLACK);
	StyleConstants.setFontSize(styleRegular, 12);
	StyleConstants.setFontFamily(styleRegular, "Arial");

	styleBold = doc.addStyle("bold", styleRegular);
	StyleConstants.setBold(styleBold, true);

	styleItalic = doc.addStyle("italic", styleRegular);
	StyleConstants.setItalic(styleItalic, true);

	styleRed = doc.addStyle("red", styleBold);
	StyleConstants.setForeground(styleRed, Color.RED);

	styleBlue = doc.addStyle("blue", styleBold);
	StyleConstants.setForeground(styleBlue, Color.BLUE);

	styleSmall = doc.addStyle("small", styleRegular);
	StyleConstants.setFontSize(styleSmall, 11);

	styleSmallItalic = doc.addStyle("smallItalic", styleSmall);
	StyleConstants.setItalic(styleSmallItalic, true);

	// Remove the Appender Console as the GUI is being initialised and
	// therefore having this information twice is not desirable.
	Logger logger = LogManager.getLogger("Console");
	org.apache.logging.log4j.core.Logger coreLogger = (org.apache.logging.log4j.core.Logger) logger;
	LoggerContext context = coreLogger.getContext();
	coreLogger.removeAppender(context.getConfiguration().getAppender(
		"Console"));
    }

    @Override
    public void append(final LogEvent event) {
	final String message = new String(this.getLayout().toByteArray(event));
	try {
	    SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run() {
		    try {
			if (jTextPane != null) {
			    try {
				Document doc = jTextPane.getDocument();
				if (event.getLevel().equals(Level.DEBUG)) {
				    doc.insertString(doc.getLength(), message,
					    styleSmall);
				} else if (event.getLevel().equals(Level.INFO)) {
				    if (message
					    .startsWith(AJConfig.APPLICATION_NAME
						    + " "
						    + AJConfig.APPLICATION_VERSION)) {
					doc.insertString(doc.getLength(),
						message, styleSmallItalic);
				    } else if (StringUtils.countMatches(
					    message, "\n") > 1) {
					doc.insertString(doc.getLength(),
						message, styleSmall);
				    } else if (!message.startsWith("\t")) {
					doc.insertString(doc.getLength(),
						message, styleBold);
				    } else {
					doc.insertString(doc.getLength(),
						message, styleRegular);
				    }
				} else if (event.getLevel().equals(Level.WARN)) {
				    doc.insertString(doc.getLength(), message,
					    styleRed);
				} else {
				    doc.insertString(doc.getLength(), message,
					    styleBlue);
				}
			    } catch (BadLocationException e) {
				LOGGER.fatal(e);
			    }
			}
		    } catch (final Throwable t) {
			System.err
				.println("Unable to append log to text pane: "
					+ t.getMessage()
					+ ". Please see help menu for reporting this issue.");
		    }
		}
	    });
	} catch (final IllegalStateException e) {
	    System.err.println("Unable to append log to text pane: "
		    + e.getMessage()
		    + ". Please see help menu for reporting this issue.");
	}
    }
}