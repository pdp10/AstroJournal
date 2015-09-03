/*
 * This file is part of AstroJournal.
 *
 * AstroJournal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AstroJournal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AstroJournal.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.astrojournal.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class AJForm extends JPanel {
  
  private JLabel lblDate = new JLabel("Date: ");
  private JTextField txtDate = new JTextField();
  
  private JLabel lblTime = new JLabel("Time: ");
  private JTextField txtTime = new JTextField();
  
  private JLabel lblLocation = new JLabel("Location: ");
  private JTextField txtLocation = new JTextField();
  
  private JLabel lblAltitude = new JLabel("Altitude: ");
  private JTextField txtAltitude = new JTextField();
  
  private JLabel lblTemperature = new JLabel("Temperature: ");
  private JTextField txtTemperature = new JTextField();
  
  private JLabel lblSeeing = new JLabel("Seeing: ");
  private JTextField txtSeeing = new JTextField();
  
  private JLabel lblTransparency = new JLabel("Transparency: ");
  private JTextField txtTransparency = new JTextField();
  
  private JLabel lblTelescopes = new JLabel("Telescopes: ");
  private JTextArea txtTelescopes = new JTextArea(2, 20);
  
  private JLabel lblEyepieces = new JLabel("Eyepieces: ");
  private JTextArea txtEyepieces = new JTextArea(2, 20);
  
  private JLabel lblFilters = new JLabel("Filters: ");
  private JTextArea txtFilters = new JTextArea(2, 20);
  
  private JLabel lblTarget = new JLabel("Target ");
  private JLabel lblConstellation = new JLabel("Constellation ");
  private JLabel lblType = new JLabel("Type ");
  private JLabel lblPower = new JLabel("Power ");
  private JLabel lblNotes = new JLabel("Notes ");

  private JButton btnAddObservation = new JButton("Add Observation");
  private JButton btnGenerateReport = new JButton("Generate Report");

  private JPanel buttonPanel = new JPanel();
  private JPanel formPanel = new JPanel();  
  private JPanel formEnvPanel = new JPanel();  
  private JPanel formEquipPanel = new JPanel();
  private JPanel formTablePanel = new JPanel();  
  
  
  public AJForm() {
    addButtonPanel();    
    addFormPanel();
    addTablePanel();
    
    setLayout(new BorderLayout());
    this.add(buttonPanel, "North");
    this.add(formPanel, "Center");
    this.add(formTablePanel, "South");    

  }


  private void addButtonPanel() {
    buttonPanel.setLayout(new FlowLayout());
    buttonPanel.add(btnAddObservation);
    buttonPanel.add(btnGenerateReport);
  }

  private void addFormPanel() {
    formEnvPanel.setLayout(new GridLayout(7, 2));
    formEnvPanel.add(lblDate);
    formEnvPanel.add(txtDate);
    formEnvPanel.add(lblTime);
    formEnvPanel.add(txtTime);
    formEnvPanel.add(lblLocation);
    formEnvPanel.add(txtLocation);
    formEnvPanel.add(lblAltitude);
    formEnvPanel.add(txtAltitude);
    formEnvPanel.add(lblTemperature);
    formEnvPanel.add(txtTemperature);
    formEnvPanel.add(lblSeeing);
    formEnvPanel.add(txtSeeing);
    formEnvPanel.add(lblTransparency);
    formEnvPanel.add(txtTransparency);
    
    formEquipPanel.setLayout(new GridLayout(3, 2));
    formEquipPanel.add(lblTelescopes);
    formEquipPanel.add(txtTelescopes);    
    formEquipPanel.add(lblEyepieces);
    formEquipPanel.add(txtEyepieces);
    formEquipPanel.add(lblFilters);
    formEquipPanel.add(txtFilters);   
    
    formPanel.setLayout(new FlowLayout());
    formPanel.add(formEnvPanel);
    formPanel.add(formEquipPanel);  
  }

  private void addTablePanel() {
    formTablePanel.setLayout(new FlowLayout());
    formTablePanel.add(lblTarget);
    formTablePanel.add(lblConstellation);
    formTablePanel.add(lblType);
    formTablePanel.add(lblPower);
    formTablePanel.add(lblNotes);
  }


  
  
  
}
