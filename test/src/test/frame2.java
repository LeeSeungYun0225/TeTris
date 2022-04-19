package test;


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class frame2 extends JPanel implements KeyListener
{
	private Space lbl[][];//이차원 배열의 선언 
	private int block[];
	private Block blc;
	private JButton btn;
	private JLabel Background;
	private int i,hold;
	public frame2()
	{	
		hold = -1;
		setLayout(null);
		setPreferredSize(new Dimension(IntClass.BLOCKSIZE*IntClass.xNumber,IntClass.BLOCKSIZE*IntClass.yNumber));
		btn = new JButton("Btn");
		btn.setBackground(Color.black);
		add(btn);
		btn.setBounds(250,100,100,100);
		btn.addKeyListener(this);
		btn.setVisible(true);
		blc = new Block();
		blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);//이미지를 이동하고나서 사이즈가 다르면 움직이지 않음 ! 
		add(blc);
		setVisible(true);
		setPreferredSize(new Dimension(IntClass.BLOCKSIZE,IntClass.BLOCKSIZE));
		lbl = new Space[IntClass.xNumber][IntClass.yNumber+4];
		for(int i=0;i<IntClass.xNumber;i++) // 레이블을 초기화 하는 작업 
		{
			for(int j=0;j<IntClass.yNumber+4;j++)
			{
				
				lbl[i][j] = new Space();
			}
		}
		for(int i=0;i<IntClass.xNumber;i++) // 레이블을 배치하는 작업 
		{
			for(int j=0;j<IntClass.yNumber+4;j++)
			{
				this.add(lbl[i][j]);
				lbl[i][j].setBounds(IntClass.BLOCKSIZE*i,IntClass.BLOCKSIZE*IntClass.yNumber-IntClass.BLOCKSIZE*(j+1),IntClass.BLOCKSIZE,IntClass.BLOCKSIZE);
			}
		}
		Background = new JLabel(new ImageIcon("Back.png"));
		add(Background);
		Background.setBounds(0,0,200,400);
		
		Shadow();
		i = blc.getPt().y/IntClass.BLOCKSIZE+3;
	}
	
	public void keyPressed(KeyEvent event)
	{
		switch(event.getKeyCode())
		{
			case KeyEvent.VK_ENTER:
			{
				Right();
				break;
			}
			case KeyEvent.VK_SHIFT:
			{
				Left();
				break;
			}

		}
	}
	public void keyTyped(KeyEvent event){}
	public void keyReleased(KeyEvent event){}
	
	
	
	public void addKl(JButton btn)
	{
		btn.addKeyListener(this);
	}
	
	
	
	
	
	
	
	
	
	
public void Nblock()
{
	blc.setState((int)(Math.random()*7));
	blc.setx(IntClass.BLOCKSIZE*3);
	blc.sety(-IntClass.BLOCKSIZE*2);
	blc.setBlock();
	blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
	hold = -1;
	Shadow();
}
	
	
	
public void CheckBreak(int a)//1줄 체크 
{
	int check = -1;
	for(int i=0;i<10;i++)
	{
		if(lbl[i][a].getState() ==1)
		{
			check = 0;
		}
		else
		{
			check = -1;
		}
		
		if(check == -1)
		{
			i = 10;
		}
	}
	
	if(check != -1)//터짐  
	{
		for(int i=a;i<21;i++)
		{
			for(int j=0;j<10;j++)
			{
				lbl[j][i].setState(lbl[j][i+1].getState());
				lbl[j][i].setImg();
			}
		}
	}
}

	
public void Down()
{
	int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE);
	switch(blc.getState())
	{
		case 0:
		{
			if(i>0 && (lbl[(blc.getPt().x/IntClass.BLOCKSIZE)+1][i-1].getState() == 1 || lbl[(blc.getPt().x/IntClass.BLOCKSIZE)+2][i-1].getState() == 1))
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);
				Nblock();
				
			}
			else if(i==0)
			{
			
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				Nblock();
			}
			
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
						
		case 1:
		{
			if((i>0 && (lbl[(blc.getPt().x/IntClass.BLOCKSIZE)+1][i-1].getState() == 1 || lbl[(blc.getPt().x/IntClass.BLOCKSIZE)+2][i-1].getState() == 1)) || lbl[(blc.getPt().x/IntClass.BLOCKSIZE)+3][i].getState() == 1)
			{	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);
				
				Nblock();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				Nblock();
			}
			
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
						
		case 2:
		{ 
			if( i> 0 && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState() == 1 ) || lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState() == 1 )
			{	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);
				
				Nblock();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				Nblock();
			}
		
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
						
		case 3:
		{
			if(i>0 && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].getState() == 1))			
			{			
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);
				

				Nblock();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				Nblock();
			}
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
		
		case 4:
		{		
			if(i>0 && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].getState() == 1))
			{		
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);
		
				Nblock();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				Nblock();
			}

			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
		
		case 5:
		{
			if(i>0 &&(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].getState() == 1))
			{	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);

				Nblock();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				Nblock();
			}
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
		
		case 6:
		{
			if((i>1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-2].getState() == 1))
			{	
				if(i>0)
				{
					lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setState(1);
					lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setImg();
					CheckBreak(i-1);
					CheckBreak(i-1);
					CheckBreak(i-1);
					CheckBreak(i-1);
				}
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();	
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);
				CheckBreak(i+1);
				CheckBreak(i+2);

				Nblock();
			}
			else if(i == 1)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				CheckBreak(1);
				CheckBreak(1);
				CheckBreak(2);
				CheckBreak(2);
				CheckBreak(3);
				Nblock();
			}
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}

			break;
		}
		
		case 7:
		{
			if((i>0 &&lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState() == 1 || (i>1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-2].getState() == 1)))
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].setImg();

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();		
				CheckBreak(i-1);
				CheckBreak(i-1);
				CheckBreak(i-1);
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);
				Nblock();
			}
			else if(i == 1)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				CheckBreak(1);
				CheckBreak(2);
				Nblock();
			}
		
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
		
		case 8:
		{				
			if((i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1 || (i>1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-2].getState() == 1)))
			{
				if(i>0)
				{
					lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].setState(1);
					lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].setImg();
					CheckBreak(i-1);
					CheckBreak(i-1);
					CheckBreak(i-1);
				}
				
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);
				Nblock();
			}
			else if(i == 1)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				CheckBreak(1);
				CheckBreak(2);
				Nblock();
			}
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
		
		case 9:
		{
			if((i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1 || (i<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].getState() == 1)))
			{	

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
					
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);
				CheckBreak(i+1);
				CheckBreak(i+2);
				Nblock();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				CheckBreak(1);
				CheckBreak(2);
				Nblock();
			}
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
		case 10:
		{		
			if(( lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1|| (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].getState() == 1) || (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1)))
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setImg();
				
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);

				Nblock();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				Nblock();
			}
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
		
		case 11:
		{	
			if( i>0 && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1) || i>0 && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState() == 1))
			{	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
							
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);
				CheckBreak(i+1);
				CheckBreak(i+2);
				
				Nblock();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				CheckBreak(1);
				CheckBreak(2);
				Nblock();
			}
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
		
		case 12:
		{	
			if((i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState() == 1))
			{	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();	
							

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);
				CheckBreak(i+1);
				CheckBreak(i+2);	
				Nblock();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				CheckBreak(1);
				CheckBreak(2);
				Nblock();
			}
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
		
		case 13:
		{
			if((i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1) || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState() == 1 )
			{	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();

				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);

				Nblock();
			}
			
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				Nblock();
			}
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
		
		case 14:
		{	
			if( (i<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].getState() == 1 ) || ( i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState() == 1))
			{		
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);
				CheckBreak(i+1);
				CheckBreak(i+2);
				Nblock();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				CheckBreak(1);
				CheckBreak(2);
				Nblock();
			}
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
		
		case 15:
		{	
			if((i>1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-2].getState() == 1) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState() == 1))
			{		
				if(i>0)
				{
					lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setState(1);
					lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setImg();
					CheckBreak(i-1);
					CheckBreak(i-1);
					CheckBreak(i-1);
				}	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);

				
				Nblock();
			}
			else if(i == 1)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				CheckBreak(1);
				CheckBreak(2);
				Nblock();
			}
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
		
		case 16:
		{			
			if((i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].getState() == 1 ) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState() == 1) || (i>1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-2].getState() == 1))
			{
				if(i>0)
				{
					lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setState(1);
					lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setImg();
					CheckBreak(i-1);
					CheckBreak(i-1);
				}

				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				
				CheckBreak(i);

				
				Nblock();
			}
			else if(i == 1)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				Nblock();
			}
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
		
		case 17:
		{
						
			if((i>0  && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].getState() == 1) || i>1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-2].getState() == 1)
			{
				if(i>0)
				{
					lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setState(1);
					lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setImg();
					CheckBreak(i-1);
					CheckBreak(i-1);
					CheckBreak(i-1);
				}
					
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				
				CheckBreak(i);
				CheckBreak(i);
				CheckBreak(i+1);
				Nblock();
			}
			else if(i == 1)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(0);
				CheckBreak(1);
				CheckBreak(1);
				CheckBreak(2);
				Nblock();
			}
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
		
		case 18:
		{	
			if((i<0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].getState() == 1) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState() == 1) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].getState() == 1))
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setImg();
				CheckBreak(i);
				Nblock();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
				CheckBreak(0);
				Nblock();
			}
			else
			{
				blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
				blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
			}
			break;
		}
	}
}
	
	
	
	
	
	

	
	public void Right()
	{
		int x = 17 - blc.getPt().y/IntClass.BLOCKSIZE;
		
		switch(blc.getState())
				{
					case 0:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-3))
						{
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x+1].getState() != 1) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() != 1))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 1:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-4))
						{
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x+1].getState() != 1) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() != 1))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 2:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-3))
						{
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() != 1) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() != 1))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 3:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-4))
						{
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() != 1) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x].getState() != 1))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 4:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-3))
						{
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x+1].getState() != 1) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() != 1))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 5:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-3))
						{
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() != 1) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() != 1))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 6: // else if / left 
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-2))
						{
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x-1].getState() != 1) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x].getState() != 1) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() != 1) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+2].getState() != 1))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						
						}
						break;
					}
					case 7:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-4))
						{
							if((x>0 &&lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x-1].getState() != 1) && lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x+1].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
					
						break;
					}
					case 8:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-2))
						{
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][x-1].getState() != 1) && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 9:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-3))
						{
							if( lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x+2].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 10:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-4))
						{
							if( lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x+1].getState() != 1 )
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 11:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-3))
						{
							if( lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x+1].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x+2].getState() != 1 )
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 12:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-3))
						{
							if( lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+2].getState() != 1 )
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 13:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-4))
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x+1].getState() != 1 )
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 14:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-3))
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x+1].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x+2].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 15:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-3))
						{
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x-1].getState() != 1) && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 16:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-3))
						{
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x-1].getState() != 1) && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 17:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-2))
						{
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x-1].getState() != 1) && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 18:
					{
						if(blc.getPt().x<IntClass.BLOCKSIZE*(IntClass.xNumber-4))
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					
				}
			}
			
			
	public void Left()
	{
		int x = 17 - blc.getPt().y/IntClass.BLOCKSIZE;
		
		switch(blc.getState())
				{
					case 0:
					{
						if(blc.getPt().x>-IntClass.BLOCKSIZE)
						{
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() != 1) && ( lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() != 1))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 1:
					{
						if(blc.getPt().x>-IntClass.BLOCKSIZE)
						{
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][x+1].getState() != 1) && ( lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() != 1))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 2:
					{
						if(blc.getPt().x>=IntClass.BLOCKSIZE)
						{
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x+1].getState() != 1) && ( lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() != 1))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 3:
					{
						if(blc.getPt().x>-IntClass.BLOCKSIZE)
						{
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() != 1) && ( lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() != 1))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 4:
					{
						if(blc.getPt().x>0)
						{
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() != 1) && ( lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x].getState() != 1))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 5:
					{
						if(blc.getPt().x>0)
						{
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() != 1) && ( lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x].getState() != 1))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 6:
					{
						if(blc.getPt().x>-IntClass.BLOCKSIZE)
						{
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x-1].getState() != 1) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() != 1) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() != 1) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+2].getState() != 1))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 7:
					{
						if(blc.getPt().x>-IntClass.BLOCKSIZE*2)
						{
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x-1].getState() != 1) && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][x+1].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 8:
					{
						if(blc.getPt().x>=IntClass.BLOCKSIZE)
						{
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x-1].getState() != 1) && lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 9:
					{
						if(blc.getPt().x>-IntClass.BLOCKSIZE)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+2].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 10:
					{
						if(blc.getPt().x>-IntClass.BLOCKSIZE)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 11:
					{
						if(blc.getPt().x>-IntClass.BLOCKSIZE)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][x+1].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][x+2].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 12:
					{
						if(blc.getPt().x>-IntClass.BLOCKSIZE)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+2].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 13:
					{
						if(blc.getPt().x>-IntClass.BLOCKSIZE)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 14:
					{
						if(blc.getPt().x>-IntClass.BLOCKSIZE)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][x+1].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+2].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 15:
					{
						if(blc.getPt().x>-IntClass.BLOCKSIZE)
						{
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x-1].getState() != 1) && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 16:
					{
						if(blc.getPt().x>0)
						{
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x-1].getState() != 1) && lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x].getState() != 1 )
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 17:
					{
						if(blc.getPt().x>0)
						{
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x-1].getState() != 1) && lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x].getState() != 1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						break;
					}
					case 18:
					{
						if(blc.getPt().x>0)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x].getState() != 1)
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
						}
						
						break;
					}
					
				}
				
	}
	
	
	public void Change()
	{
		int x = blc.getPt().x/IntClass.BLOCKSIZE;
		int y = 17-blc.getPt().y/IntClass.BLOCKSIZE;
		switch(blc.getState())
				{
					case 0:
					{
						break;
					}
					case 1:
					{
						if(y>0 && lbl[x+3][y-1].getState() != 1 && lbl[x+3][y].getState() != 1)
						{
							blc.setState(7);
							blc.setBlock();
						}
						
						else if(lbl[x+1][y+1].getState() != 1 && lbl[x+1][y+2].getState() != 1)
						{
							blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
							blc.setState(7);
							blc.setBlock();
							blc.sety(blc.getPt().y-IntClass.BLOCKSIZE);
							blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
						}
						break;
					}
					case 2:
					{
						if(y>0 && lbl[x][y-1].getState() != 1 && lbl[x][y].getState() != 1)
						{
							blc.setState(8);
							blc.setBlock();
						}
						else if(lbl[x+2][y+1].getState() != 1 && lbl[x+2][y+2].getState() != 1)
						{
							blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
							blc.setState(8);
							blc.setBlock();
							blc.sety(blc.getPt().y-IntClass.BLOCKSIZE);
							blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
						}
						break;
					}
					case 3:
					{
						if(lbl[x+1][y+1].getState() != 1 && lbl[x+2][y+2].getState() != 1)
						{
							blc.setState(9);
							blc.setBlock();
						}
						break;
					}
					case 4:
					{
						if(lbl[x+1][y+1].getState() != 1 && lbl[x+1][y+2].getState() != 1)
						{
							blc.setState(12);
							blc.setBlock();
						}
						break;
					}
					case 5:
					{	
						if(y>0 && lbl[x+1][y-1].getState() != 1)
						{
							blc.setState(15);
							blc.setBlock();
						}
						else
						{
							blc.sety(blc.getPt().y-IntClass.BLOCKSIZE);
							blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
							blc.setState(15);
							blc.setBlock();
						}
						break;
					}
					case 6:
					{
						if(blc.getPt().x == (IntClass.xNumber-2)*IntClass.BLOCKSIZE)//우측에 붙을 시  
						{
							if(lbl[x-2][y].getState() != 1 && lbl[x-1][y].getState() != 1 && lbl[x][y].getState() != 1)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(18);
								blc.setBlock();
							}
						}
						else if(blc.getPt().x == (IntClass.xNumber-3)*IntClass.BLOCKSIZE)//우측 -1에 부ㅡㅌ을 시 
						{
							if(lbl[x-1][y].getState() != 1 && lbl[x][y].getState() != 1 && lbl[x+2][y].getState() != 1)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(18);
								blc.setBlock();
							}
						}
						else if(blc.getPt().x == -IntClass.BLOCKSIZE)//좌측 
						{
							if(lbl[x+4][y].getState() != 1 && lbl[x+2][y].getState() != 1 && lbl[x+3][y].getState() != 1)
							{
								blc.setx(blc.getPt().x + IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(18);
								blc.setBlock();
							}
						}
						else if(lbl[x][y].getState() != 1 && lbl[x+2][y].getState() != 1 && lbl[x+3][y].getState() != 1)
						{
							blc.setState(18);
							blc.setBlock();
						}
						

						
						break;
					}
					case 7:
					{
						if(blc.getPt().x == -IntClass.BLOCKSIZE*2)
						{
							if(lbl[x+4][y+1].getState() != 1)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(1);
								blc.setBlock();
							}
						}
						else if(lbl[x+1][y].getState() != 1 && lbl[x+3][y+1].getState() != 1)
						{
							blc.setState(1);
							blc.setBlock();
				
						}
						break;
					}
					case 8:
					{
						if(blc.getPt().x == (IntClass.xNumber-2)*IntClass.BLOCKSIZE)
						{
							if(lbl[x-1][y+1].getState() != 1)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(2);
								blc.setBlock();
							}
						}
						else if(lbl[x][y+1].getState() != 1 && lbl[x+2][y].getState() != 1)
						{
							blc.setState(2);
							blc.setBlock();
						}
						break;
					}
					case 9:
					{
						if(blc.getPt().x == (IntClass.xNumber-3)*IntClass.BLOCKSIZE)
						{
							if(lbl[x][y+1].getState() != 1 && lbl[x+2][y+1].getState() != 1 && lbl[x+2][y].getState() != 1)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(10);
								blc.setBlock();
							}
						}
						else if(lbl[x+2][y+1].getState() != 1 && lbl[x+3][y+1].getState() != 1 && lbl[x+3][y].getState() != 1)
						{
							blc.setState(10);
							blc.setBlock();
						}
						break;
					}
					case 10:
					{
						if(lbl[x+1][y].getState() != 1 && lbl[x+2][y].getState() != 1 && lbl[x+2][y+2].getState() != 1)
						{
							blc.setState(11);
							blc.setBlock();
						}
						else if(lbl[x+2][y].getState() != 1 && lbl[x+3][y+2].getState() != 1)
						{
							blc.setState(11);
							blc.setBlock();
							blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
							blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
						}
						break;
					}
					case 11:
					{
						if(blc.getPt().x == (IntClass.xNumber-3)*IntClass.BLOCKSIZE)
						{
							if(lbl[x][y+1].getState() != 1 && lbl[x][y].getState() != 1 )
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(3);
								blc.setBlock();
							}	
						}
						else if(lbl[x+1][y+1].getState() != 1 && lbl[x+3][y+1].getState() != 1 && lbl[x+3][y].getState() != 1)
						{
							blc.setState(3);
							blc.setBlock();	
						}
						break;
					}
					case 12:
					{
						if(blc.getPt().x == (IntClass.xNumber-3)*IntClass.BLOCKSIZE)
						{
							if(lbl[x][y+1].getState() != 1 && lbl[x][y].getState() != 1 && lbl[x+2][y+1].getState() != 1)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(13);
								blc.setBlock();
							}
						}
						else if(lbl[x+2][y+1].getState() != 1  && lbl[x+3][y+1].getState() != 1)
						{
							blc.setState(13);
							blc.setBlock();	
						}
						break;
					}
					case 13:
					{
						if(lbl[x+1][y+2].getState() != 1 && lbl[x+2][y+2].getState() != 1 && lbl[x+2][y].getState() != 1)
						{
							blc.setState(14);
							blc.setBlock();
						}
						break;
					}
					case 14:
					{
						if(blc.getPt().x == -IntClass.BLOCKSIZE)
						{
							if(lbl[x+3][y+1].getState() != 1 && lbl[x+3][y].getState() != 1 && lbl[x+1][y].getState() != 1)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(4);
								blc.setBlock();
							}
						}
						else if(lbl[x][y].getState() != 1 && lbl[x+1][y].getState() != 1)
						{
							blc.setState(4);
							blc.setBlock();
						}
						break;
					}
					case 15:
					{
						if(blc.getPt().x == -IntClass.BLOCKSIZE)
						{
							if(y>0 && lbl[x+2][y-1].getState() != 1 && lbl[x+3][y].getState() != 1)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(16);
								blc.setBlock();
							}
						}
						else if(lbl[x][y].getState() != 1)
						{
							blc.setState(16);
							blc.setBlock();
						}

						break;
					}
					case 16://시작 
					{
						if(lbl[x+1][y+1].getState() != 1)
						{
							blc.setState(17);
							blc.setBlock();
						}
						break;
					}
					case 17:
					{
						if(blc.getPt().x == (IntClass.xNumber-2)*IntClass.BLOCKSIZE)
						{
							if(lbl[x-1][y].getState() != 1 && lbl[x][y+1].getState() != 1)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);		
								if(y == 1 && lbl[x][y-1].getState() != 1 && lbl[x+2][y-1].getState() != 1 )
								{
									blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
									blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								}
								blc.setState(5);
								blc.setBlock();
							}
						}
						else if(lbl[x+2][y].getState() != 1)
						{
							if(y==1 && lbl[x][y-1].getState() != 1 && lbl[x+2][y-1].getState() != 1 )
							{
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
							}
							blc.setState(5);
							blc.setBlock();
						}
						
						break;
					}
					case 18:
					{
						if(y == 0)
						{
							if(lbl[x+1][y+1].getState() != 1 && lbl[x+1][y+2].getState() != 1 && lbl[x+1][y+3].getState() != 1)
							{
								blc.sety(blc.getPt().y-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
						}
						else if(lbl[x+1][y-1].getState() != 1 && lbl[x+1][y+1].getState() != 1 && lbl[x+1][y+2].getState() != 1)
						{
							blc.setState(6);
							blc.setBlock();
						}
						else if(y == 1)
						{
							if(lbl[x][y-1].getState() != 1 && lbl[x][y+1].getState() != 1 && lbl[x][y+2].getState() != 1)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
							else if(lbl[x+2][y-1].getState() != 1 && lbl[x+2][y+1].getState() != 1 && lbl[x+2][y+2].getState() != 1)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
							else if(lbl[x+3][y-1].getState() != 1 && lbl[x+3][y+1].getState() != 1 && lbl[x+3][y+2].getState() != 1)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
						}
						else if(y == 2)
						{
							if(lbl[x][y-2].getState() != 1 && lbl[x][y-1].getState() != 1 && lbl[x][y+1].getState() != 1)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
							else if(lbl[x+2][y-1].getState() != 1 && lbl[x+2][y-2].getState() != 1 && lbl[x+2][y+1].getState() != 1)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
							else if(lbl[x+3][y-1].getState() != 1 && lbl[x+3][y-2].getState() != 1 && lbl[x+3][y+1].getState() != 1)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE*2);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
						}
						else if(y == 3)
						{
							if(lbl[x][y-3].getState() != 1 && lbl[x][y-2].getState() != 1 && lbl[x][y-1].getState() != 1)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
							else if(lbl[x+2][y-1].getState() != 1 && lbl[x+2][y-2].getState() != 1 && lbl[x+2][y-3].getState() != 1)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
							else if(lbl[x+3][y-1].getState() != 1 && lbl[x+3][y-2].getState() != 1 && lbl[x+3][y-3].getState() != 1)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE*2);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
						}
						else if(y>3)
						{
							if(lbl[x][y-3].getState() != 1 && lbl[x][y-2].getState() != 1 && lbl[x][y-1].getState() != 1)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
							else if(lbl[x+2][y-1].getState() != 1 && lbl[x+2][y-2].getState() != 1 && lbl[x+2][y-3].getState() != 1)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
							else if(lbl[x+3][y-1].getState() != 1 && lbl[x+3][y-2].getState() != 1 && lbl[x+3][y-3].getState() != 1)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE*2);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
							else if(lbl[x][y-2].getState() != 1 && lbl[x][y-1].getState() != 1 && lbl[x][y+1].getState() != 1)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
							else if(lbl[x+2][y-1].getState() != 1 && lbl[x+2][y-2].getState() != 1 && lbl[x+2][y+1].getState() != 1)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
							else if(lbl[x+3][y-1].getState() != 1 && lbl[x+3][y-2].getState() != 1 && lbl[x+3][y+1].getState() != 1)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE*2);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
							else if(lbl[x][y-1].getState() != 1 && lbl[x][y+1].getState() != 1 && lbl[x][y+2].getState() != 1)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
							else if(lbl[x+2][y-1].getState() != 1 && lbl[x+2][y+1].getState() != 1 && lbl[x+2][y+2].getState() != 1)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
							else if(lbl[x+3][y-1].getState() != 1 && lbl[x+3][y+1].getState() != 1 && lbl[x+3][y+2].getState() != 1)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.setBlock();
							}
						}
						break;
					}
					
				}
		}
	
	
	
	public void Filllbl()
	{
		switch(blc.getState())
				{
					case 0:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 )
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(1);
							}
								
						}
						break;
					}
					case 1:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || (i<18 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].getState() == 1))
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(1);
							}
								
						}
						break;
					}
					case 2:
					{ 
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || (blc.getPt().x/IntClass.BLOCKSIZE>=0 && i<18 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].getState() == 1))
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+2].setImg();
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(1);
							}
								
						}
						break;
					}
					case 3:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState() == 1)
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(1);
							}
						}
						break;
					}
					case 4:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState() == 1)
							{
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(1);
							}
						}
						break;
					}
					case 5:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState() == 1)
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();
						
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(1);
							}
						}
						break;
					}
					case 6:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1 )
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();	
								CheckBreak(i);
								CheckBreak(i);
								CheckBreak(i);
								CheckBreak(i);
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);
								CheckBreak(i+2);
								CheckBreak(i+3);
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setImg();
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(1);
								CheckBreak(1);
								CheckBreak(1);
								CheckBreak(2);
								CheckBreak(2);
								CheckBreak(3);
							}
						}
						break;
					}
					case 7:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].getState() == 1) || i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();	
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								
								CheckBreak(i);
								CheckBreak(i);
								CheckBreak(i);
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);

								i = -1;
							}
						}
						break;
					}
					case 8:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].getState() == 1) || i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								CheckBreak(i);
								CheckBreak(i);
								CheckBreak(i);
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);

								i = -1;
							}
						}
						break;
					}
					case 9:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || (i<18 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].getState() == 1))
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
								
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);
								CheckBreak(i+2);
								CheckBreak(i+3);

								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(1);
								CheckBreak(1);
								CheckBreak(2);

							}
						}
						break;
					}
					case 10:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if((i<19 &&lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].getState() == 1)|| (i<18 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState() == 1) || (i<19 &&lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].getState() == 1))
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(1);
							}
						}
						break;
					}
					case 11:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 )
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);
								CheckBreak(i+2);
								CheckBreak(i+3);
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(1);
								CheckBreak(1);
								CheckBreak(2);
							}
								
						}
						break;
					}
					case 12:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 )
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
								
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);
								CheckBreak(i+2);
								CheckBreak(i+3);
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(1);
								CheckBreak(1);
								CheckBreak(2);
							}
								
						}
						break;
					}
					case 13:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || (i<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].getState() == 1) || (i<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].getState() == 1) )
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();		
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(1);
							}
								
						}
						break;
					}
					case 14:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if( (i< 18 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].getState() == 1 ) || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1)
							{
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(1);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(1);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(1);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);
								CheckBreak(i+2);
								CheckBreak(i+3);
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(1);
								CheckBreak(1);
								CheckBreak(2);
							}
								
						}
						break;
					}
					case 15:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1) || ( lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1))
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								CheckBreak(i);
								CheckBreak(i);
								CheckBreak(i);
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);
								i = -1;
							}
							else if(i == 1)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(1);
								CheckBreak(1);
								CheckBreak(2);
							}
								
						}
						break;
					}
					case 16:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState() == 1 ) || (i < 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1))
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								CheckBreak(i);
								CheckBreak(i);
								CheckBreak(i+1);
								
								i = -1;
							}
							else if(i == 1)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(1);
							}
						
								
						}
						break;
					}
					case 17:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((i < 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState() == 1 )|| (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1))
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								CheckBreak(i);
								CheckBreak(i);
								CheckBreak(i);
								CheckBreak(i+1);
								CheckBreak(i+1);
								CheckBreak(i+2);
								i = -1;
							}
							else if(i == 1)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(0);
								CheckBreak(1);
								CheckBreak(1);
								CheckBreak(2);
							}
								
						}
						break;
					}
					case 18:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState() == 1)
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								CheckBreak(i+1);
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
								CheckBreak(0);
							}
								
						}
						break;
					}
				}
				Nblock();
	}
	public void Initlbl()
	{
		switch(blc.getState())
				{
					case 0:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 )
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
								
						}
						break;
					}
					case 1:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || (i<18 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].getState() == 1))
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
								
						}
						break;
					}
					case 2:
					{ 
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || (blc.getPt().x/IntClass.BLOCKSIZE>=0 && i<18 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].getState() == 1))
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
								
						}
						break;
					}
					case 3:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState() == 1)
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
						}
						break;
					}
					case 4:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState() == 1)
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
						}
						break;
					}
					case 5:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState() == 1)
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
						}
						break;
					}
					case 6:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1 )
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								}
								if(i<17)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();	
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setImg();
							}
						}
						break;
					}
					case 7:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].getState() == 1) || i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setImg();
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();	
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								}
								
								
								i = -1;
							}
						}
						break;
					}
					case 8:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].getState() == 1) || i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								}
								i = -1;
							}
						}
						break;
					}
					case 9:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || (i<18 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].getState() == 1))
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								}
								if(i<17)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
							}
						}
						break;
					}
					case 10:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if((i<19 &&lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].getState() == 1)|| (i<18 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState() == 1) || (i<19 &&lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].getState() == 1))
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
								}
									
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
							}
						}
						break;
					}
					case 11:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 )
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								}
								if(i<17)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
								
						}
						break;
					}
					case 12:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 )
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								}	
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								}
								if(i<17)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
							}
								
						}
						break;
					}
					case 13:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || (i<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].getState() == 1) || (i<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].getState() == 1) )
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
							}
								
						}
						break;
					}
					case 14:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if( (i< 18 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].getState() == 1 ) || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1)
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								}
								if(i<17)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
								
						}
						break;
					}
					case 15:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1) || (i < 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1))
							{

								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
								
						}
						break;
					}
					case 16:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(( lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState() == 1 ) || ( lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1))
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								}
								i = -1;
							}
							else if(i == 1)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
						
								
						}
						break;
					}
					case 17:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState() == 1 )|| (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1))
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();

								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
							}
								
						}
						break;
					}
					case 18:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState() == 1)
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
							}
								
						}
						break;
					}
				}
	}
	
	
	public void Shadow()
	{
		switch(blc.getState())
				{
					case 0:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 )
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
								
						}
						break;
					}
					case 1:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || (i<18 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].getState() == 1))
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
								
						}
						break;
					}
					case 2:
					{ 
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || (blc.getPt().x/IntClass.BLOCKSIZE>=0 && i<18 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].getState() == 1))
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
								
						}
						break;
					}
					case 3:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState() == 1)
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
						}
						break;
					}
					case 4:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState() == 1)
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
						}
						break;
					}
					case 5:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState() == 1)
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
						}
						break;
					}
					case 6:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1 )
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								}
								if(i<17)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();	
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setImg();
							}
						}
						break;
					}
					case 7:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].getState() == 1) || i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setImg();
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();	
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								}
								
								
								i = -1;
							}
						}
						break;
					}
					case 8:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].getState() == 1) || i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								}
								i = -1;
							}
						}
						break;
					}
					case 9:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || (i<18 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].getState() == 1))
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								}
								if(i<17)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
							}
						}
						break;
					}
					case 10:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if((i<19 &&lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].getState() == 1)|| (i<18 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState() == 1) || (i<19 &&lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].getState() == 1))
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
								}
									
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
							}
						}
						break;
					}
					case 11:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 )
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								}
								if(i<17)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
								
						}
						break;
					}
					case 12:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 )
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								}	
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								}
								if(i<17)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
							}
								
						}
						break;
					}
					case 13:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || (i<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].getState() == 1) || (i<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].getState() == 1) )
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
							}
								
						}
						break;
					}
					case 14:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if( (i< 18 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].getState() == 1 ) || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1)
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								}
								if(i<17)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
								
						}
						break;
					}
					case 15:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1) || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
								
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
								
						}
						break;
					}
					case 16:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState() == 1 ) || ( lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1))
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
								
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								}
								i = -1;
							}
							else if(i == 1)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
						
								
						}
						break;
					}
					case 17:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState() == 1 )|| (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() == 1))
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();

								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								}
								if(i<18)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								}
								i = -1;
							}
							else if(i == 1)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
							}
								
						}
						break;
					}
					case 18:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() == 1 || lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState() == 1)
							{
								if(i<19)
								{
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								}
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
							}
								
						}
						break;
					}
				}
	}
}
