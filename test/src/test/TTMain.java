package test;

import java.awt.*;
import javax.swing.*;

public class TTMain extends JPanel
{	
	public static void main(String[] args)
	{
		frame test = new frame();

	
		JFrame frame = new JFrame("Tetris!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(1000,(IntClass.yNumber+5)*IntClass.BLOCKSIZE));
		frame.pack();
		frame.getContentPane();
		frame.setVisible(true);
		frame.setLayout(null);
		frame.add(test);
		test.setBounds(50,50,600,IntClass.BLOCKSIZE*IntClass.yNumber);
	}
}