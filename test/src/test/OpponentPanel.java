package test;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OpponentPanel extends JPanel{
	private JLabel backgroundLabel;
	private Space blockSpace[][];
	private String[] tokenStr;
	
	
	public OpponentPanel()
	{
		setLayout(null);
		blockSpace = new Space[IntClass.xNumber][IntClass.yNumber + 4];
		for (int i = 0; i < IntClass.xNumber; i++) // 레이블을 초기화 하는 작업
		{
			for (int j = 0; j < IntClass.yNumber + 4; j++) {

				blockSpace[i][j] = new Space();
			}
		}
		for (int i = 0; i < IntClass.xNumber; i++) // 레이블을 배치하는 작업
		{
			for (int j = 0; j < IntClass.yNumber + 4; j++) {
				this.add(blockSpace[i][j]);
				blockSpace[i][j].setBounds(IntClass.BLOCKSIZE * i,
						IntClass.BLOCKSIZE * IntClass.yNumber - IntClass.BLOCKSIZE * (j + 1), IntClass.BLOCKSIZE,
						IntClass.BLOCKSIZE);
			}
		}
		
		backgroundLabel = new JLabel(new ImageIcon("Back.png"));
		add(backgroundLabel);
		backgroundLabel.setBounds(0, 0, 200, 400);

	}
	
	
	public void setBlocks(String in) // 문자열로 받은 블록 내역을 읽어 화면에 보여주기 
	{
		tokenStr = in.split(" ");
		int j=0;
		
		
		for(int i=0;i< tokenStr.length;i++)
		{
			j= i/10;
			
			blockSpace[i%10][j].setType(Integer.parseInt(tokenStr[i]));
			blockSpace[i%10][j].setImg();
		}
		
		
	}
	
	public void initBlocks() {
		for (int i = 0; i < 10; i++) // 레이블을 초기화 하는 작업
		{
			for (int j = 0; j < 20; j++) {

				blockSpace[i][j].setType(0);
				blockSpace[i][j].setImg();
			}
		}
	}
	
}
