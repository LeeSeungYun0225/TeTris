package test;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Block extends JLabel // ����� �����ϴ� Ŭ����
{

	private Space lbl[];
	private int state;
	private Point pt;
	public Block()
	{
		setPreferredSize(new Dimension(IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4));
		setLayout(null);
						
		lbl = new Space[4];
		for(int i =0;i<4;i++)
		{
			lbl[i] = new Space();
			add(lbl[i]);
		}
		state =(int)(Math.random()*7); // ���� ��� ���� 
		BlockColor();
		setBlock();
		
		pt = new Point();
		setBlockPosition();

	}
	
	public void blockInit()
	{
		state =(int)(Math.random()*7); // ���� ��� ���� 
		BlockColor();
		setBlock();
		
		pt = new Point();
		setBlockPosition();
	}
	
	public Space[] getlbl()
	{
		return lbl;
	}
	
	public void setBlockPosition() // ����� ��ġ�� �����Ѵ�.  
	{
	
		switch(state)
		{
			case 0:
			{
				pt.x=IntClass.BLOCKSIZE*3;
				pt.y=-IntClass.BLOCKSIZE*3;
				
				break;
			}
			case 1:
			{
				pt.x=IntClass.BLOCKSIZE*3;
				pt.y=-IntClass.BLOCKSIZE*3;
		
				break;
			}
			case 2:
			{
				pt.x=IntClass.BLOCKSIZE*4;
				pt.y=-IntClass.BLOCKSIZE*3;
				break;
			}
			case 3:
			{
				pt.x=IntClass.BLOCKSIZE*3;
				pt.y=-IntClass.BLOCKSIZE*3;
				break;
			}
			case 4:
			{
				pt.x=IntClass.BLOCKSIZE*4;
				pt.y=-IntClass.BLOCKSIZE*3;
				break;
			}
			case 5:
			{
				pt.x=IntClass.BLOCKSIZE*4;
				pt.y=-IntClass.BLOCKSIZE*3;
				break;
			}			
			case 6:
			{
				pt.x=IntClass.BLOCKSIZE*4;
				pt.y=-IntClass.BLOCKSIZE*3;
				break;
			}	
		}
		this.setBounds(pt.x,pt.y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
	}
	

	
	public void BlockColor() // ����� ���� �����ϴ� �޼ҵ� 
	{
		switch(state)//case 0~6�� ���� ������ ���, case7 �̻��� �� ����Ű�� ���� ���� �� ����� �ǹ��Ѵ�. 
		{
			case 999:
			{
				for(int i =0;i<4;i++)
				{
					lbl[i].setType(0);
					lbl[i].setImg();
				}
				break;
			}
			case 0:
			{
				for(int i =0;i<4;i++)
				{
					lbl[i].setType(1);
					lbl[i].setImg();
				}
				break;
			}
			case 1:{}
			case 7:
			{
				for(int i =0;i<4;i++)
				{
					lbl[i].setType(2);
					lbl[i].setImg();
				}
				break;
			}
			case 2:{}
			case 8:
			{
				for(int i =0;i<4;i++)
				{
					lbl[i].setType(3);
					lbl[i].setImg();
				}
				break;
			}
			case 3:{}
			case 9:{}
			case 10:{}
			case 11:
			{
				for(int i =0;i<4;i++)
				{
					lbl[i].setType(4);
					lbl[i].setImg();
				}
				break;
			}
			case 4:{}
			case 12:{}
			case 13:{}
			case 14:
			{
				for(int i =0;i<4;i++)
				{
					lbl[i].setType(5);
					lbl[i].setImg();
				}
				break;
			}
			case 5:{}			
			case 15:{}
			case 16:{}
			case 17:
			{
				for(int i =0;i<4;i++)
				{
					lbl[i].setType(6);
					lbl[i].setImg();
				}
				break;
			}
			case 6:{}
			case 18:
			{
				for(int i =0;i<4;i++)
				{
					lbl[i].setType(1);
					lbl[i].setImg();
				}
				break;
			}
		}
	}
		
	public int getState()
	{
		return state;
	}
	public void setState(int x)
	{
		state = x;
	}
	public void setx(int x)
	{
		pt.x = x;
	}
	public void sety(int y)
	{
		pt.y = y;
	}
	public Point getPt()
	{
		return pt;
	}
	
	public void setBlock()//state�� ���� ��� ����, ����� ����� ������ִ� �޼ҵ� 
	{
		switch(state) // case 7�̻��� �͵��� 0~6�� ������ �������¸� �ǹ��Ѵ� . 
		{
			case 0://�簢�� 
			{
				lbl[0].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			case 1://_-�� 
			{
				lbl[0].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE*3,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			case 2:// -_ �� 
			{
				lbl[0].setBounds(0,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			case 3:// ���� 
			{
				lbl[0].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE*3,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			case 4:// 7�� 
			{
				lbl[0].setBounds(0,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			case 5:// �� 
			{
				lbl[0].setBounds(0,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			case 6: // I 
			{
				lbl[0].setBounds(IntClass.BLOCKSIZE,0,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*3,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			case 7:
			{
				lbl[0].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE*3,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE*3,IntClass.BLOCKSIZE*3,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			case 8:
			{
				lbl[0].setBounds(0,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(0,IntClass.BLOCKSIZE*3,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}	
			case 9:
			{
				lbl[0].setBounds(IntClass.BLOCKSIZE,0,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE*2,0,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			case 10:
			{
				lbl[0].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE*3,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE*3,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			case 11:
			{
				lbl[0].setBounds(IntClass.BLOCKSIZE*2,0,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			case 12:
			{
				lbl[0].setBounds(IntClass.BLOCKSIZE,0,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			case 13:
			{
				lbl[0].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE*3,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			case 14:
			{
				lbl[0].setBounds(IntClass.BLOCKSIZE,0,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE*2,0,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			case 15:
			{
				lbl[0].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*3,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			case 16:
			{
				lbl[0].setBounds(0,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*3,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			case 17:
			{
				lbl[0].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(0,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*3,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
			
			case 18:
			{
				lbl[0].setBounds(0,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[1].setBounds(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[2].setBounds(IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				lbl[3].setBounds(IntClass.BLOCKSIZE*3,IntClass.BLOCKSIZE*2,IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
				break;
			}
		}
	}
}