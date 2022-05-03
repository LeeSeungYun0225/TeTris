package test;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.*;

public class TTMain 
{	
	public static void main(String[] args)
	{
		
		StartGame test = new StartGame();
		

		JFrame frame = new JFrame("테트리스");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(1000,600));
		frame.pack();
		frame.getContentPane();
		frame.setVisible(true);	
		frame.setLayout(null);
		frame.add(test);
		
		test.setBounds(50,50,1000,600);
		

	}
}	