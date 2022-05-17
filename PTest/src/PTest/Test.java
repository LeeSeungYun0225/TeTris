package PTest;
import java.awt.*;
import javax.swing.*;

public class Test extends JPanel
{
	public static void main(String[] args)
	{
		KeyTest frame = new KeyTest();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}