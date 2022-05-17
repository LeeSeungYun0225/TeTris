package PTest;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class Floor extends JLabel
{
	private ImageIcon[] floorimg;//이미지 저장 어레이 리스트 
	private int floorType;//0:: 그냥 바닥 1::부쉬 2:: 벽 3::물가
	private Point floorPt;//이 바닥 타일의 위치 
	private int movable;//이동할 수 있는 지역인지 :: 1이면 이동가능 0이면 이동 불가능 
	private int dialog;//1이면 z누르면 내용 나오게, 0이면 이벤트 x
	private int battle;//1이면 배틀이 생성될 수 있는 지역, 0이면 배틀이 생성되지 않는 지역 
	private int current;//현재 있는 지형
	private boolean isFull;
/////////1이면 불가능 2면 가능 
	
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
	
	public Floor(int type,Point pt)//curType는 현재 물인지 땅인지 구분 
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
			
			case 0://보통 
			{
				setIcon(floorimg[0]);
				dialog = 1;
				movable = 2;
				battle = 1;
				break;
			}
			case 1://부쉬 
			{
				setIcon(floorimg[1]);
				dialog = 1;
				movable = 2;
				battle = 2;
				break;
			}
			case 2://벽 
			{
				setIcon(floorimg[2]);
				dialog = 1;
				movable = 1;
				battle = 1;
				break;}
			case 3://물가 
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