package PTest;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class Floor extends JLabel
{
	private ImageIcon[] floorimg;//�̹��� ���� ��� ����Ʈ 
	private int floorType;//0:: �׳� �ٴ� 1::�ν� 2:: �� 3::����
	private Point floorPt;//�� �ٴ� Ÿ���� ��ġ 
	private int movable;//�̵��� �� �ִ� �������� :: 1�̸� �̵����� 0�̸� �̵� �Ұ��� 
	private int dialog;//1�̸� z������ ���� ������, 0�̸� �̺�Ʈ x
	private int battle;//1�̸� ��Ʋ�� ������ �� �ִ� ����, 0�̸� ��Ʋ�� �������� �ʴ� ���� 
	private int current;//���� �ִ� ����
	private boolean isFull;
/////////1�̸� �Ұ��� 2�� ���� 
	
	public Floor()
	{
		setIcon(null);
		
		floorType = 0;
		dialog = 0;
		movable = 0;
		battle = 0;
		current = 0;
		isFull = false;
	}
	
	public Floor(int type,Point pt)//curType�� ���� ������ ������ ���� 
	{
		floorimg = new ImageIcon[4];
		floorimg[0] = new ImageIcon("nomal.png");
		floorimg[1] = new ImageIcon("bush.png");
		floorimg[2] = new ImageIcon("byuk.png");
		floorimg[3] = new ImageIcon("byuk.png");
		floorPt = new Point();
		floorPt.x = pt.x;
		floorPt.y = pt.y;
		floorType = type;
		isFull = true;
		switch(floorType)
		{
			
			case 0://���� 
			{
				setIcon(floorimg[0]);
				dialog = 1;
				movable = 2;
				battle = 1;
				break;
			}
			case 1://�ν� 
			{
				setIcon(floorimg[1]);
				dialog = 1;
				movable = 2;
				battle = 2;
				break;
			}
			case 2://�� 
			{
				setIcon(floorimg[2]);
				dialog = 1;
				movable = 1;
				battle = 1;
				break;}
			case 3://���� 
			{
				setIcon(floorimg[3]);
				dialog = 1;
				movable = 1;
				battle = 2;
				break;}
			default:
			{break;}
		}
		setBounds(floorPt.x,floorPt.y,40,40);
	}
	
	public int getfloorType()
	{
		return floorType;
	}
	public int getmovable()
	{
		return movable;
	}
	public Point getpt()
	{
		return floorPt;
	}
	public boolean isFull()
	{
		return isFull;
	}
	public int getdialog()
	{
		return dialog;
	}
	public int getcurrent()
	{
		return current;
	}
	public int getbattle()
	{
		return battle;
	}
	public int getisFull()
	{
		if(isFull)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	public void setisFull(int x)
	{
		if(x == 1)
		{
			isFull = true;
		}
		else 
		{
			isFull = false;
		}
	}
	public void setcurrent(int x)
	{
		current = x;
	}
	public void setbattle(int x)
	{
		battle = x;
	}
	public void setdialog(int x)
	{
		dialog = x;
	}
	public void setmovable(int x)
	{
		movable = x;
	}
	public void setfloorType(int x)
	{
		floorType = x;
	}
	//floorType,movable,dialog,battle,current,isFull(int)
}