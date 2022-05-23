package test;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class RecordLabel extends JPanel{
	
	private JLabel idLabel;
	private JLabel winLabel;
	private JLabel loseLabel;
	
	public RecordLabel()
	{
		idLabel = new JLabel("id : ");
		winLabel = new JLabel("win : ");
		loseLabel = new JLabel("lose: ");
		
		setLayout(null);
		
		idLabel.setBounds(0,0,100,30);
		winLabel.setBounds(80,0,70,30);
		loseLabel.setBounds(150,0,70,30);
		
		add(idLabel);
		add(winLabel);
		add(loseLabel);
		
	}
	
	public void setId(String in)
	{
		idLabel.setText("ID : " + in);
	}
	
	public void setWin(int in)
	{
		winLabel.setText("Win : " + in);
	}
	
	public void setLose(int in)
	{
		loseLabel.setText("Lose : " + in);
	}

}
