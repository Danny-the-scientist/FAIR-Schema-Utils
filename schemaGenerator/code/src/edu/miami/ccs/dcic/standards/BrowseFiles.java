package edu.miami.ccs.dcic.standards;


//Java program to create open or 
//save dialog using JFileChooser 
import java.io.*;
import java.util.Scanner;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.*; 
import javax.swing.filechooser.*; 
public class BrowseFiles extends JFrame implements ActionListener { 


Input in = new Input();
	

	
	public  BrowseFiles() 
	{ 
		// frame to contains GUI elements 
		 in.f = new JFrame("Schema generator"); 

		
		// set the size of the frame 
		 in.f.setSize(400, 400); 

		// set the frame's visibility 
		 in.f.setVisible(true); 

		 in.f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

		// button to open save dialog 

		// button to open open dialog 
		JButton button2 = new JButton(new AbstractAction("open"){
	        @Override
	        public void actionPerformed( ActionEvent e ) {
	            // add Action
	        	String com = e.getActionCommand(); 
	        	// if the user presses the open dialog show the open dialog 
	   		 
				// create an object of JFileChooser class 
				JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); 

				// invoke the showsOpenDialog function to show the save dialog 
				int r = j.showOpenDialog(null); 

				// if the user selects a file 
				if (r == JFileChooser.APPROVE_OPTION) 

				{ 
					
					// set the label to the path of the selected file 
					 in.l.setText(j.getSelectedFile().getAbsolutePath()); 
					
					
					 in.setFile(j.getSelectedFile().getAbsolutePath());
					JLabel lbl = new JLabel("Are you creating a new template");
				    lbl.setVisible(true);
				
				    JComboBox comboBox = new JComboBox();
				    comboBox.addItem("YES");
			        comboBox.addItem("NO");
			        comboBox.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							String s = (String) comboBox.getSelectedItem();
							if(s.equalsIgnoreCase("Yes")){
								System.out.println("I am here");
								JLabel lbl2 = new JLabel("Please enter your api key <ex: apiKey d2e0213b....>");
								   lbl2.setVisible(true);
								   in.p.add(lbl);
								   in.f.add( in.p); 
								  
							}
						}
			        	
			        });
			       
				    in.p.add(lbl);
				    in.p.add(comboBox);
				    in.f.add( in.p); 
				    
					
				} 
				// if the user cancelled the operation 
				else
					 in.l.setText("the user cancelled the operation"); 
			} 

	       
	 
		});

	
		// make an object of the class filechooser 

		


		

		// make a panel to add the buttons and labels 
		 in.p = new JPanel(); 

		// add buttons to the frame 
		 in.p.add(button2); 

		// set the label to its initial value 
		 in.l = new JLabel("Select a csv file"); 

		// add panel to the frame 
		 in.p.add( in.l);

		 in.f.add( in.p); 

		 in.f.show(); 
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	} 
	

	public static void main(String[] args) {
		BrowseFiles ex = new BrowseFiles();
	    
	    }

	
}