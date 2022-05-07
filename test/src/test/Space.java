package test;

import java.awt.*;
import javax.swing.*;

public class Space extends JLabel // 각각의 공간(블록1개 사이즈)을 표현하기 위한 클래스
{
	private int type;
	private ImageIcon img[];
	private boolean check;
	
	public Space()
	{
		check = false;
		type = 0;
		img = new ImageIcon[16];
		img[0] = new ImageIcon("");
		img[1] = new ImageIcon("block.png");
		img[2] = new ImageIcon("Block1.png");
		img[3] = new ImageIcon("Block2.png");
		img[4] = new ImageIcon("Block3.png");
		img[5] = new ImageIcon("Block4.png");
		img[6] = new ImageIcon("Block5.png");
		img[7] = new ImageIcon("Block6.png");
		img[8] = new ImageIcon("Block7.png");
		img[9] = new ImageIcon("Block8.png");
		img[10] = new ImageIcon("Block9.png");
		img[11] = new ImageIcon("Block10.png");
		img[12] = new ImageIcon("Block11.png");
		img[13] = new ImageIcon("Block12.png");
		img[14] = new ImageIcon("Block13.png");
		img[15] = new ImageIcon("Die.png");
		setIcon(img[type]);
	}
	
	
	public boolean getState() // boolean 
	{
		return check;
	}
	
	public void setState(boolean x)
	{
		check = x;
	}
	public void setImg()
	{
		setIcon(img[type]);	
	}
	
	public int getType()
	{
		return type;
	}
	public void setType(int x)
	{
		type = x;
	}
	
}