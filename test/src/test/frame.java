package test;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class frame extends JPanel implements KeyListener,Runnable
{
	private Space lbl[][];//이차원 배열의 선언 
	private int block[];
	private Block blc,holdblc,next1,next2,next3; // 현재 내려오고 있는 블록blc /  hlodblc는 쉬프트 키로 홀드칸에 저장되는 블록 , next123은 다음에 나올 블록들 
	private JButton btn;
	private JLabel Background,next,holdlbl;
	private int i,hold,shold;
	private boolean over,set,down,canstart;
	private Thread play,cru;
	private int crush,cry[],checkTime;
	private Td td1;
	private JLabel startlbl;
	
	
	
	public frame()
	{	
		canstart = true;
		startlbl = new JLabel(new ImageIcon("Start.png")); // 시작 알림 레이블 
		add(startlbl);
		startlbl.setBounds(50,150,100,50);
		
		checkTime = 1;
		td1 = new Td();
		crush = -1;
		cry = new int[4];
		for(int i=0;i<4;i++)
		{
			cry[i] = -1;
		}
		set = false;
		over = false;
		down = true;
		shold = -1;
		hold = -1;
		setLayout(null);
		setPreferredSize(new Dimension(IntClass.BLOCKSIZE*IntClass.xNumber,IntClass.BLOCKSIZE*IntClass.yNumber));
		next = new JLabel(new ImageIcon("next.png"));
		
		
		//넥스트,홀드 블록 , 레이블 초기화, 자리 지정 
		holdblc = new Block();
		holdlbl = new JLabel(new ImageIcon("Hold.png"));
		holdlbl.add(holdblc);
		holdlbl.setBounds(350,20,100,100);
		next.setBounds(200,0,150,400);
		add(next);
		add(holdlbl);
		btn = new JButton("Btn");
		btn.setBackground(Color.black);
		add(btn);
		btn.setBounds(5000,100,0,0);
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
	}
	
	public void keyPressed(KeyEvent event) // 키 이벤트 처리  
	{
		switch(event.getKeyCode())
		{
			case KeyEvent.VK_RIGHT:
			{
				if(over)
				{
					Right();
				}
				break;
			}
			case KeyEvent.VK_LEFT:
			{
				if(over)
				{
					Left();
				}
				break;
			}
			case KeyEvent.VK_UP:
			{
				if(over)
				{
					Initlbl();
					Change();
					Shadow();
				}
				break;
			}
			case KeyEvent.VK_DOWN:
			{
				if(over)
				{
					Down();
				}
				
				break;
			}
			case KeyEvent.VK_SPACE:
			{
				if(over)
				{
					Initlbl();
					Filllbl();
					Nblock();
					hold = -1;
					setHoldlbl();
					Shadow();
				}
				else
				{
					if(canstart) // 게임 시작 전일때,
					{
						startbtn();
					}
				}
				break;
			}
			case KeyEvent.VK_SHIFT:
			{
				if(hold == -1)
				{
					if(over)
					{
						Initlbl();
						Hold();
						Shadow();
					}
				}
				break;
			}
			}
		}
	
	public void keyTyped(KeyEvent event)
	{}
	public void keyReleased(KeyEvent event){}
	  
	
	public void startbtn() // 게임 시작 메소드 
	{
		canstart = false;
		clear();
		over = true;
		startlbl.setVisible(false);
		Down();
		play = new Thread(this);
		play.start();
		cru = new Thread(td1);
		cru.start();
		set = true;
		blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
		setNext();
		setNextPosition();
		holdblc.setBounds(10,15,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
		holdblc.setState(999);
		holdblc.BlockColor();
		Shadow();
		i = blc.getPt().y/IntClass.BLOCKSIZE+3;
		next1.setVisible(true);
		next2.setVisible(true);
		next3.setVisible(true);
	}
	
	public void clear() // 블록 모두 지우기 
	{
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<22;j++)
			{
				lbl[i][j].setState(false);
				lbl[i][j].setType(0);
				lbl[i][j].setImg();
			}
		}
	}
	
	
	public void setHoldPosition() // 홀드에 저장된 블록 설정 
	{
		switch(holdblc.getState())
		{
			case 0:
			{
				holdblc.setBounds(10,15,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 1:
			{
				holdblc.setBounds(0,15,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 2:
			{
				holdblc.setBounds(20,15,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 3:
			{
				holdblc.setBounds(0,15,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 4:
			{
				holdblc.setBounds(20,15,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 5:
			{
				holdblc.setBounds(20,15,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 6:
			{
				holdblc.setBounds(20,10,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
		}
	}
	
	public void setHoldlbl() // 홀드 버튼 사용 가능여부 표현 
	{
		switch(hold)
		{
			case -1:
			{
				holdlbl.setIcon(new ImageIcon("Hold.png"));
				break;
			}
			case 1:
			{
				holdlbl.setIcon(new ImageIcon("Hold2.png"));
				break;
			}
		}
	}
	
	public void setNext() // 다음에 나올 블록들 설정 
	{
		next1 = new Block();
		next1.setBounds(35,30,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
		next1.setState((int)(Math.random()*7));
		next1.BlockColor();
		next1.setBlock();
		next.add(next1);
		next2 = new Block();
		next2.setBounds(35,150,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
		next2.setState((int)(Math.random()*7));
		next2.BlockColor();
		next2.setBlock();
		next.add(next2);
		next3 = new Block();
		next3.setBounds(35,270,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
		next3.setState((int)(Math.random()*7));
		next3.BlockColor();
		next3.setBlock();
		next.add(next3);
	}
	
	public void setNextPosition()
	{
		switch(next1.getState())
		{
			case 0:
			{
				next1.setBounds(35,30,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 1:
			{
				next1.setBounds(25,30,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 2:
			{
				next1.setBounds(45,30,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 3:
			{
				next1.setBounds(25,30,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 4:
			{
				next1.setBounds(45,30,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 5:
			{
				next1.setBounds(45,30,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 6:
			{
				next1.setBounds(45,30,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
		}
		switch(next2.getState())
		{
			case 0:
			{
				next2.setBounds(35,150,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 1:
			{
				next2.setBounds(25,150,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 2:
			{
				next2.setBounds(45,150,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 3:
			{
				next2.setBounds(25,150,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 4:
			{
				next2.setBounds(45,150,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 5:
			{
				next2.setBounds(45,150,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 6:
			{
				next2.setBounds(45,150,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
		}
		switch(next3.getState())
		{
			case 0:
			{
				next3.setBounds(35,270,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 1:
			{
				next3.setBounds(25,270,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 2:
			{
				next3.setBounds(45,270,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 3:
			{
				next3.setBounds(25,270,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 4:
			{
				next3.setBounds(45,270,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 5:
			{
				next3.setBounds(45,270,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
			case 6:
			{
				next3.setBounds(45,270,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				break;
			}
		}
	}
	
	
	public void Gameover()
	{
		for(int i=0;i<10;i++)
		{
			if(lbl[i][20].getState())
			{
				set = false;
				over = false;
				blc.setState(-100);
				startlbl.setVisible(true);
				blc.setBounds(-200,-200,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
				next1.setVisible(false);
				next2.setVisible(false);
				next3.setVisible(false);
			}
		}
	}
	
	public void run() // 시간에 따라 블록이 내려오는 속도 조절 
	{
		while(set)
		{
			Down();
			if(checkTime<100)
			{
				try{
								Thread.sleep(1000);
				}catch(InterruptedException e){}
			}
			else if(checkTime>=100 && checkTime<300)
			{
				try{
								Thread.sleep(800);
				}catch(InterruptedException e){}
			}
			else if(checkTime>=300 && checkTime<700)
			{
				try{
								Thread.sleep(600);
				}catch(InterruptedException e){}
			}
			else if(checkTime>=700 && checkTime<1000)
			{
				try{
								Thread.sleep(300);
				}catch(InterruptedException e){}
			}
			else if(checkTime>=1000 && checkTime<1500)
			{
				try{
								Thread.sleep(120);
				}catch(InterruptedException e){}
			}
			else if(checkTime>=1500)
			{
				try{
								Thread.sleep(10);
				}catch(InterruptedException e){}
			}
			checkTime++;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
public void Nblock() //다음 블록 읽어오기 
{
	if(over)
	{
		blc.setState(next1.getState());
		next1.setState(next2.getState());
		next2.setState(next3.getState());
		next3.setState((int)(Math.random()*7));
		next1.BlockColor();
		next1.setBlock();
		next2.BlockColor();
		next2.setBlock();
		next3.BlockColor();
		next3.setBlock();
		setNextPosition();
		blc.BlockColor();
		blc.setx(IntClass.BLOCKSIZE*3);
		blc.sety(-IntClass.BLOCKSIZE*2);
		blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
		blc.setBlockPosition();
		Shadow();
		blc.setBlock();
		Down();
		checkTime++;
	}
	
}


public void Hold()
{
	if(shold !=-1)
	{
		Initlbl();
		int x=-1;
		hold = 1;
		setHoldlbl();
		switch(blc.getState())
		{
			case 0:
			{
			}
			case 1:
			{
			}
			case 2:
			{
			}
			case 3:
			{
			}
			case 4:
			{
			}
			case 5:
			{
			}
			case 6:
			{
				x = blc.getState();
				break;
			}
			case 7:
			{
				x = 1;
				break;
			}
			case 8:
			{
				x = 2;
				break;
			}
			case 9:{}
			case 10:{}
			case 11:
			{
				x = 3;
				break;
			}
			case 12:{}
			case 13:{}
			case 14:
			{
				x = 4;
				break;
			}
			case 15:{}
			case 16:{}
			case 17:
			{
				x = 5;
				break;
			}
			case 18:
			{
				x = 6;
				break;
			}
		}

		blc.setState(shold);
		blc.BlockColor();
		holdblc.setState(x);
		setHoldPosition();
		holdblc.setBlock();
		holdblc.BlockColor();
		shold = x;
		blc.setx(IntClass.BLOCKSIZE*3);
		blc.sety(-IntClass.BLOCKSIZE*2);
		blc.setBlock();
		blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
		Shadow();
	}
	else
	{
		Initlbl();
		hold = 1;
		setHoldlbl();
		switch(blc.getState())
		{
			case 0:
			{
			}
			case 1:
			{
			}
			case 2:
			{
			}
			case 3:
			{
			}
			case 4:
			{
			}
			case 5:
			{
			}
			case 6:
			{
				shold = blc.getState();
				break;
			}
			case 7:
			{
				shold = 1;
				break;
			}
			case 8:
			{
				shold = 2;
				break;
			}
			case 9:{}
			case 10:{}
			case 11:
			{
				shold = 3;
				break;
			}
			case 12:{}
			case 13:{}
			case 14:
			{
				shold = 4;
				break;
			}
			case 15:{}
			case 16:{}
			case 17:
			{
				shold = 5;
				break;
			}
			case 18:
			{
				shold = 6;
				break;
			}
		}
		holdblc.setState(shold);
		holdblc.setBlock();
		setHoldPosition();
		holdblc.BlockColor();
		Nblock();
		blc.BlockColor();
		Shadow();
	}
}
	
public void CheckBreak(int a)//1줄 체크 
{
	for(int i=0;i<10;i++)
	{
		if(!lbl[i][a].getState())
		{
			return;
		}
	}
		

	for(int i=a;i<21;i++)
	{
		for(int j=0;j<10;j++)
		{
			lbl[j][i].setState(lbl[j][i+1].getState());
			lbl[j][i].setType(lbl[j][i+1].getType());
			lbl[j][i].setImg();
		}
	}
	down = true;
	
}

	
public void Down()
{
	int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE);
	int y=0;
	switch(blc.getState())
	{
		case 0:
		{
			if(i>0 && (lbl[(blc.getPt().x/IntClass.BLOCKSIZE)+1][i-1].getState()|| lbl[(blc.getPt().x/IntClass.BLOCKSIZE)+2][i-1].getState()))
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(3);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setType(3);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(3);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(3);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
		
				down = false;
				crush = 2;
				cry[0] = i;
				cry[1] = i+1;
				Nblock();
				hold = -1;
				setHoldlbl();
				
			}
			else if(i==0)
			{
			
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(3);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(3);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(3);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(3);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
			
				down = false;
				crush = 2;
				cry[0] = 0;
				cry[1] = 1;
				Nblock();
				hold = -1;
				setHoldlbl();
				
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
			if((i>0 && (lbl[(blc.getPt().x/IntClass.BLOCKSIZE)+1][i-1].getState()|| lbl[(blc.getPt().x/IntClass.BLOCKSIZE)+2][i-1].getState())) || lbl[(blc.getPt().x/IntClass.BLOCKSIZE)+3][i].getState())
			{	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(5);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setType(5);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setType(5);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(5);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
			
				down = false;
				crush = 2;
				cry[0] = i;
				cry[1] = i+1;
				Nblock();
				hold = -1;
				setHoldlbl();

			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(5);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setType(5);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(5);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(5);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
				
				down = false;
				crush = 2;
				cry[0] = 0;
				cry[1] = 1;
				Nblock();
				hold = -1;
				setHoldlbl();
			
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
			if( i> 0 && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState()) || lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState())
			{	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(7);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setType(7);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(7);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(7);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
		
				down = false;
				crush = 2;
				cry[0] = i;
				cry[1] = i+1;
				hold = -1;
				setHoldlbl();
				Nblock();

			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(7);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setType(7);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(7);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(7);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
				
				down = false;
				crush = 2;
				cry[0] = 0;
				cry[1] = 1;
				hold = -1;
				setHoldlbl();
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
			if(i>0 && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].getState()))			
			{			
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
				
				down = false;
				crush = 2;
				cry[0] = i;
				cry[1] = i+1;
				hold = -1;
				setHoldlbl();
				Nblock();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
				
				down = false;
				crush = 2;
				cry[0] = 0;
				cry[1] = 1;
				Nblock();
				hold = -1;

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
			if(i>0 && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].getState()))
			{		
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
				
				down = false;
				crush = 2;
				cry[0] = i;
				cry[1] = i+1;
				hold = -1;
				setHoldlbl();
				Nblock();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
				
				down = false;
				crush = 2;
				cry[0] = 0;
				cry[1] = 1;
				Nblock();
				hold = -1;
				setHoldlbl();

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
			if(i>0 &&(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].getState()))
			{	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
			
				down = false;
				crush = 2;
				cry[0] = i;
				cry[1] = i+1;
				hold = -1;
				setHoldlbl();

				Nblock();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
			
				down = false;
				crush = 2;
				cry[0] = 0;
				cry[1] = 1;
				Nblock();
				hold = -1;
				setHoldlbl();

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
			if((i>1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-2].getState()))
			{	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setType(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
				y=i+2;
				CheckBreak(y);
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
				y=i-1;
				CheckBreak(y);
			
				down = false;
				crush = 4;
				cry[0] = i-1;
				cry[1] = i;
				cry[2] = i+1;
				cry[3] = i+2;
				hold = -1;
				setHoldlbl();
				Nblock();

			}
			else if(i == 1)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setType(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setImg();
				y=3;
				CheckBreak(y);
				y=2;
				CheckBreak(y);
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
				
				down = false;
				crush = 4;
				cry[0] = 0;
				cry[1] = 1;
				cry[2] = 2;
				cry[3] = 3;
				Nblock();
				hold = -1;
				setHoldlbl();
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
			if((i>0 &&lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState() || (i>1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-2].getState())))
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].setType(5);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].setImg();

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setType(5);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setType(5);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(5);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
				y=i-1;
				CheckBreak(y);
				
				down = false;
				crush = 3;
				cry[0] = i-1;
				cry[1] = i;
				cry[2] = i+1;

				Nblock();
				hold = -1;
				setHoldlbl();
			}
			else if(i == 1)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(5);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setType(5);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setType(5);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setType(5);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
				y=2;
				CheckBreak(y);
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
				down = false;
				crush = 3;
				cry[0] = 0;
				cry[1] = 1;
				cry[2] = 2;
				Nblock();
				hold = -1;
				setHoldlbl();
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
			if((i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState() || (i>1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-2].getState())))
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].setType(7);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].setImg();
				
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(7);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setType(7);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(7);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
				y=i-1;
				CheckBreak(y);
				down = false;
				crush = 3;
				cry[0] = i-1;
				cry[1] = i;
				cry[2] = i+1;
				Nblock();
				hold = -1;
				setHoldlbl();
			}
			else if(i == 1)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setType(7);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setType(7);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(7);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(7);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
				y=2;
				CheckBreak(y);
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
				down = false;
				crush = 3;
				cry[0] = 0;
				cry[1] = 1;
				cry[2] = 2;
				Nblock();
				hold = -1;
				setHoldlbl();
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
			if((i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()|| (i<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].getState())))
			{	

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
					
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
				y=i+2;
				CheckBreak(y);
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
				down = false;
				crush = 3;
				cry[0] = i;
				cry[1] = i+1;
				cry[2] = i+2;
				Nblock();
				hold = -1;
				setHoldlbl();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
				y=2;
				CheckBreak(y);
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
				down = false;
				crush = 3;
				cry[0] = 0;
				cry[1] = 1;
				cry[2] = 2;
				Nblock();
				hold = -1;
				setHoldlbl();
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
			if(( lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].getState()) || (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState())))
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setImg();
				
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
				down = false;
				crush = 2;
				cry[0] = i;
				cry[1] = i+1;

				Nblock();
				hold = -1;
				setHoldlbl();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
				down = false;
				crush = 2;
				cry[0] = i;
				cry[1] = i+1;
				Nblock();
				hold = -1;
				setHoldlbl();
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
			if( i>0 && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()) || i>0 && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState()))
			{	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
							
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
				y=i+2;
				CheckBreak(y);
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
				down = false;
				crush = 3;
				cry[0] = i;
				cry[1] = i+1;
				cry[2] = i+2;
				hold = -1;
				setHoldlbl();
				Nblock();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(9);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				y=2;
				CheckBreak(y);
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
				down = false;
				crush = 3;
				cry[0] = 0;
				cry[1] = 1;
				cry[2] = 2;
				Nblock();
				hold = -1;
				setHoldlbl();
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
			if((i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState()))
			{	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();	
							

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
				y=i+2;
				CheckBreak(y);
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
				down = false;
				crush = 3;
				cry[0] = i;
				cry[1] = i+1;
				cry[2] = i+2;	
				Nblock();
				hold = -1;
				setHoldlbl();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				y=2;
				CheckBreak(y);
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
				down = false;
				crush = 3;
				cry[0] = 0;
				cry[1] = 1;
				cry[2] = 2;
				Nblock();
				hold = -1;
				setHoldlbl();
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
			if((i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()) || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() || lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState())
			{	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
				down = false;
				crush = 2;
				cry[0] = i;
				cry[1] = i+1;

				Nblock();
				hold = -1;
				setHoldlbl();
				
			}
			
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
				down = false;
				crush = 2;
				cry[0] = 0;
				cry[1] = 1;
				Nblock();
				hold = -1;
				setHoldlbl();
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
			if( (i<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].getState()) || ( i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState()))
			{		
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
				y=i+2;
				CheckBreak(y);
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
				down = false;
				crush = 3;
				cry[0] = i;
				cry[1] = i+1;
				cry[2] = i+2;
				Nblock();
				hold = -1;
				setHoldlbl();
			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(11);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				y=2;
				CheckBreak(y);
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
				down = false;
				crush = 3;
				cry[0] = 0;
				cry[1] = 1;
				cry[2] = 2;
				Nblock();
				hold = -1;
				setHoldlbl();
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
			if((i>1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-2].getState()) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState()))
			{		

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setImg();

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
				y=i-1;
				CheckBreak(y);
				down = false;
				crush = 3;
				cry[0] = i-1;
				cry[1] = i;
				cry[2] = i+1;
				hold = -1;
				setHoldlbl();
				
				Nblock();
			}
			else if(i == 1)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				y=2;
				CheckBreak(y);
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
				down = false;
				crush = 3;
				cry[0] = 0;
				cry[1] = 1;
				cry[2] = 2;
				Nblock();
				hold = -1;
				setHoldlbl();

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
			if((i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].getState()) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState()) || (i>1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-2].getState()))
			{

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setImg();


				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				y=i;
				CheckBreak(y);
				y=i-1;
				CheckBreak(y);
				
				down = false;
				crush = 2;
				cry[0] = i-1;
				cry[1] = i;
				hold = -1;
				setHoldlbl();
				
				Nblock();
			}
			else if(i == 1)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
				down = false;
				crush = 2;
				cry[0] = 0;
				cry[1] = 1;
				Nblock();
				hold = -1;
				setHoldlbl();
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
						
			if((i>0  && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].getState()) || i>1 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-2].getState())
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].setImg();

				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();

				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
				y=i+1;
				CheckBreak(y);
				y=i;
				CheckBreak(y);
				y=i-1;
				CheckBreak(y);
				hold = -1;
				setHoldlbl();
				down = false;
				crush = 3;
				cry[0] = i-1;
				cry[1] = i;
				cry[2] = i+1;
				Nblock();
			}
			else if(i == 1)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(13);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
				y=2;
				CheckBreak(y);
				y=1;
				CheckBreak(y);
				y=0;
				CheckBreak(y);
				down = false;
				crush = 3;
				cry[0] = 0;
				cry[1] = 1;
				cry[2] = 2;
				Nblock();
				hold = -1;
				setHoldlbl();

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
			if((i<0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].getState()) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i-1].getState()) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].getState()))
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setType(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setType(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setType(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setImg();
				y=i;
				CheckBreak(y);
				down = false;
				crush = 1;
				cry[0] = i;
				Nblock();
				hold = -1;
				setHoldlbl();

			}
			else if(i == 0)
			{
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setType(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();	
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(true);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setType(1);
				lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
				y=0;
				CheckBreak(y);
				down = false;
				crush = 1;
				cry[0] = 0;

				Nblock();
				hold = -1;
				setHoldlbl();

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
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x+1].getState() ==false) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() ==false))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() ==false)
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
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x+1].getState() ==false) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() ==false))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() ==false)
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
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() ==false) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() ==false))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() ==false)
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
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() ==false) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x].getState() ==false))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x].getState() ==false)
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
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x+1].getState() ==false) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() ==false))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() ==false)
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
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() ==false) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() ==false))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x+IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() ==false)
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
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x-1].getState() ==false) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x].getState() ==false) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() ==false) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+2].getState() ==false))
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
							if((x>0 &&lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x-1].getState() ==false) && lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x+1].getState() ==false)
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
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][x-1].getState() ==false) && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() ==false)
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
							if( lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x+2].getState() ==false)
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
							if( lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x+1].getState() ==false )
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
							if( lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x+1].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x+2].getState() ==false )
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
							if( lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+2].getState() ==false )
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
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x+1].getState() ==false )
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
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x+1].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x+2].getState() ==false)
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
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x-1].getState() ==false) && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() ==false)
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
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x-1].getState() ==false) && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][x].getState() ==false)
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
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x-1].getState() ==false) && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() ==false)
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
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+4][x].getState() ==false)
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
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() ==false) && ( lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() ==false))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() ==false)
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
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][x+1].getState() ==false) && ( lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() ==false))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() ==false)
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
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x+1].getState() ==false) && ( lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() ==false))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() ==false)
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
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() ==false) && ( lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() ==false))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() ==false)
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
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x+1].getState() ==false) && ( lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x].getState() ==false))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x].getState() ==false)
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
							if((x<19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() ==false) && ( lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x].getState() ==false))
							{
								Initlbl();
								blc.setBounds(blc.getPt().x-IntClass.BLOCKSIZE,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								Shadow();
							}
							else if(x == 19 && lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x].getState() ==false)
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
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x-1].getState() ==false) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() ==false) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() ==false) && (lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+2].getState() ==false))
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
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x-1].getState() ==false) && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][x+1].getState() ==false)
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
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x-1].getState() ==false) && lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() ==false)
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
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+2].getState() ==false)
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
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() ==false)
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
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][x+1].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][x+2].getState() ==false)
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
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+2].getState() ==false)
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
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() ==false)
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
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][x+1].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+2].getState() ==false)
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
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x-1].getState() ==false) && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() ==false)
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
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x-1].getState() ==false) && lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x].getState() ==false )
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
							if((x>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x-1].getState() ==false) && lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x].getState() ==false && lbl[blc.getPt().x/IntClass.BLOCKSIZE][x+1].getState() ==false)
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
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE-1][x].getState() ==false)
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
						if(y>0 && lbl[x+3][y-1].getState() ==false && lbl[x+3][y].getState() ==false)
						{
							blc.setState(7);
							blc.BlockColor();
							blc.setBlock();
						}
						
						else if(lbl[x+1][y+1].getState() ==false && lbl[x+1][y+2].getState() ==false)
						{
							blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
							blc.setState(7);
							blc.setBlock();
							blc.BlockColor();
							blc.sety(blc.getPt().y-IntClass.BLOCKSIZE);
							blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
						}
						break;
					}
					case 2:
					{
						if(y>0 && lbl[x][y-1].getState() ==false && lbl[x][y].getState() ==false)
						{
							blc.setState(8);
							blc.BlockColor();
							blc.setBlock();
						}
						else if(lbl[x+2][y+1].getState() ==false && lbl[x+2][y+2].getState() ==false)
						{
							blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
							blc.setState(8);
							blc.BlockColor();
							blc.setBlock();
							blc.sety(blc.getPt().y-IntClass.BLOCKSIZE);
							blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
						}
						break;
					}
					case 3:
					{
						if(lbl[x+1][y+1].getState() ==false && lbl[x+2][y+2].getState() ==false)
						{
							blc.setState(9);
							blc.BlockColor();
							blc.setBlock();
						}
						break;
					}
					case 4:
					{
						if(lbl[x+1][y+1].getState() ==false && lbl[x+1][y+2].getState() ==false)
						{
							blc.setState(12);
							blc.BlockColor();
							blc.setBlock();
						}
						break;
					}
					case 5:
					{	
						if(y>0 && lbl[x+1][y-1].getState() ==false)
						{
							blc.setState(15);
							blc.BlockColor();
							blc.setBlock();
						}
						else
						{
							blc.sety(blc.getPt().y-IntClass.BLOCKSIZE);
							blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
							blc.setState(15);
							blc.BlockColor();
							blc.setBlock();
						}
						break;
					}
					case 6:
					{
						if(blc.getPt().x == (IntClass.xNumber-2)*IntClass.BLOCKSIZE)//우측에 붙을 시  
						{
							if(lbl[x-2][y].getState() ==false && lbl[x-1][y].getState() ==false && lbl[x][y].getState() ==false)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(18);
								blc.BlockColor();
								blc.setBlock();
							}
						}
						else if(blc.getPt().x == (IntClass.xNumber-3)*IntClass.BLOCKSIZE)//우측 -1에 부ㅡㅌ을 시 
						{
							if(lbl[x-1][y].getState() ==false && lbl[x][y].getState() ==false && lbl[x+2][y].getState() ==false)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(18);
								blc.BlockColor();
								blc.setBlock();
							}
						}
						else if(blc.getPt().x == -IntClass.BLOCKSIZE)//좌측 
						{
							if(lbl[x+4][y].getState() ==false && lbl[x+2][y].getState() ==false && lbl[x+3][y].getState() ==false)
							{
								blc.setx(blc.getPt().x + IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(18);
								blc.BlockColor();
								blc.setBlock();
							}
						}
						else if(lbl[x][y].getState() ==false && lbl[x+2][y].getState() ==false && lbl[x+3][y].getState() ==false)
						{
							blc.setState(18);
							blc.BlockColor();
							blc.setBlock();
						}
						

						
						break;
					}
					case 7:
					{
						if(blc.getPt().x == -IntClass.BLOCKSIZE*2)
						{
							if(lbl[x+4][y+1].getState() ==false)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(1);
								blc.BlockColor();
								blc.setBlock();
							}
						}
						else if(lbl[x+1][y].getState() ==false && lbl[x+3][y+1].getState() ==false)
						{
							blc.setState(1);
							blc.BlockColor();
							blc.setBlock();
				
						}
						break;
					}
					case 8:
					{
						if(blc.getPt().x == (IntClass.xNumber-2)*IntClass.BLOCKSIZE)
						{
							if(lbl[x-1][y+1].getState() ==false)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(2);
								blc.BlockColor();
								blc.setBlock();
							}
						}
						else if(lbl[x][y+1].getState() ==false && lbl[x+2][y].getState() ==false)
						{
							blc.setState(2);
							blc.BlockColor();
							blc.setBlock();
						}
						break;
					}
					case 9:
					{
						if(blc.getPt().x == (IntClass.xNumber-3)*IntClass.BLOCKSIZE)
						{
							if(lbl[x][y+1].getState() ==false && lbl[x+2][y+1].getState() ==false && lbl[x+2][y].getState() ==false)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(10);
								blc.BlockColor();
								blc.setBlock();
							}
						}
						else if(lbl[x+2][y+1].getState() ==false && lbl[x+3][y+1].getState() ==false && lbl[x+3][y].getState() ==false)
						{
							blc.setState(10);
							blc.BlockColor();
							blc.setBlock();
						}
						break;
					}
					case 10:
					{
						if(lbl[x+1][y].getState() ==false && lbl[x+2][y].getState() ==false && lbl[x+2][y+2].getState() ==false)
						{
							blc.setState(11);
							blc.BlockColor();
							blc.setBlock();
						}
						else if(lbl[x+2][y].getState() ==false && lbl[x+3][y+2].getState() ==false)
						{
							blc.setState(11);
							blc.BlockColor();
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
							if(lbl[x][y+1].getState() ==false && lbl[x][y].getState() ==false )
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(3);
								blc.BlockColor();
								blc.setBlock();
							}	
						}
						else if(lbl[x+1][y+1].getState() ==false && lbl[x+3][y+1].getState() ==false && lbl[x+3][y].getState() ==false)
						{
							blc.setState(3);
							blc.BlockColor();
							blc.setBlock();	
						}
						break;
					}
					case 12:
					{
						if(blc.getPt().x == (IntClass.xNumber-3)*IntClass.BLOCKSIZE)
						{
							if(lbl[x][y+1].getState() ==false && lbl[x][y].getState() ==false && lbl[x+2][y+1].getState() ==false)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(13);
								blc.BlockColor();
								blc.setBlock();
							}
						}
						else if(lbl[x+2][y+1].getState() ==false  && lbl[x+3][y+1].getState() ==false)
						{
							blc.setState(13);
							blc.BlockColor();
							blc.setBlock();	
						}
						break;
					}
					case 13:
					{
						if(lbl[x+1][y+2].getState() ==false && lbl[x+2][y+2].getState() ==false && lbl[x+2][y].getState() ==false)
						{
							blc.setState(14);
							blc.BlockColor();
							blc.setBlock();
						}
						break;
					}
					case 14:
					{
						if(blc.getPt().x == -IntClass.BLOCKSIZE)
						{
							if(lbl[x+3][y+1].getState() ==false && lbl[x+3][y].getState() ==false && lbl[x+1][y].getState() ==false)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(4);
								blc.BlockColor();
								blc.setBlock();
							}
						}
						else if(lbl[x][y].getState() ==false && lbl[x+1][y].getState() ==false)
						{
							blc.setState(4);
							blc.BlockColor();
							blc.setBlock();
						}
						break;
					}
					case 15:
					{
						if(blc.getPt().x == -IntClass.BLOCKSIZE)
						{
							if(y>0 && lbl[x+2][y-1].getState() ==false && lbl[x+3][y].getState() ==false)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(16);
								blc.BlockColor();
								blc.setBlock();
							}
						}
						else if(lbl[x][y].getState() ==false)
						{
							blc.setState(16);
							blc.BlockColor();
							blc.setBlock();
						}

						break;
					}
					case 16://시작 
					{
						if(lbl[x+1][y+1].getState() ==false)
						{
							blc.setState(17);
							blc.BlockColor();
							blc.setBlock();
						}
						break;
					}
					case 17:
					{
						if(blc.getPt().x == (IntClass.xNumber-2)*IntClass.BLOCKSIZE)
						{
							if(lbl[x-1][y].getState() ==false && lbl[x][y+1].getState() ==false)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);		
								if(y == 1 && lbl[x][y-1].getState() ==false && lbl[x+2][y-1].getState() ==false )
								{
									blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
									blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								}
								blc.setState(5);
								blc.BlockColor();
								blc.setBlock();
							}
						}
						else if(lbl[x+2][y].getState() ==false)
						{
							if(y==1 && lbl[x][y-1].getState() ==false && lbl[x+2][y-1].getState() ==false )
							{
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
							}
							blc.setState(5);
							blc.BlockColor();
							blc.setBlock();
						}
						
						break;
					}
					case 18:
					{
						if(y == 0)
						{
							if(lbl[x+1][y+1].getState() ==false && lbl[x+1][y+2].getState() ==false && lbl[x+1][y+3].getState() ==false)
							{
								blc.sety(blc.getPt().y-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
						}
						else if(lbl[x+1][y-1].getState() ==false && lbl[x+1][y+1].getState() ==false && lbl[x+1][y+2].getState() ==false)
						{
							blc.setState(6);
							blc.BlockColor();
							blc.setBlock();
						}
						else if(y == 1)
						{
							if(lbl[x][y-1].getState() ==false && lbl[x][y+1].getState() ==false && lbl[x][y+2].getState() ==false)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
							else if(lbl[x+2][y-1].getState() ==false && lbl[x+2][y+1].getState() ==false && lbl[x+2][y+2].getState() ==false)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
							else if(lbl[x+3][y-1].getState() ==false && lbl[x+3][y+1].getState() ==false && lbl[x+3][y+2].getState() ==false)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
						}
						else if(y == 2)
						{
							if(lbl[x][y-2].getState() ==false && lbl[x][y-1].getState() ==false && lbl[x][y+1].getState() ==false)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
							else if(lbl[x+2][y-1].getState() ==false && lbl[x+2][y-2].getState() ==false && lbl[x+2][y+1].getState() ==false)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
							else if(lbl[x+3][y-1].getState() ==false && lbl[x+3][y-2].getState() ==false && lbl[x+3][y+1].getState() ==false)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE*2);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
						}
						else if(y == 3)
						{
							if(lbl[x][y-3].getState() ==false && lbl[x][y-2].getState() ==false && lbl[x][y-1].getState() ==false)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
							else if(lbl[x+2][y-1].getState() ==false && lbl[x+2][y-2].getState() ==false && lbl[x+2][y-3].getState() ==false)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
							else if(lbl[x+3][y-1].getState() ==false && lbl[x+3][y-2].getState() ==false && lbl[x+3][y-3].getState() ==false)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE*2);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
						}
						else if(y>3)
						{
							if(lbl[x][y-3].getState() ==false && lbl[x][y-2].getState() ==false && lbl[x][y-1].getState() ==false)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
							else if(lbl[x+2][y-1].getState() ==false && lbl[x+2][y-2].getState() ==false && lbl[x+2][y-3].getState() ==false)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
							else if(lbl[x+3][y-1].getState() ==false && lbl[x+3][y-2].getState() ==false && lbl[x+3][y-3].getState() ==false)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE*2);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
							else if(lbl[x][y-2].getState() ==false && lbl[x][y-1].getState() ==false && lbl[x][y+1].getState() ==false)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
							else if(lbl[x+2][y-1].getState() ==false && lbl[x+2][y-2].getState() ==false && lbl[x+2][y+1].getState() ==false)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
								
							}
							else if(lbl[x+3][y-1].getState() ==false && lbl[x+3][y-2].getState() ==false && lbl[x+3][y+1].getState() ==false)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE*2);
								blc.sety(blc.getPt().y+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
							else if(lbl[x][y-1].getState() ==false && lbl[x][y+1].getState() ==false && lbl[x][y+2].getState() ==false)
							{
								blc.setx(blc.getPt().x-IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
							else if(lbl[x+2][y-1].getState() ==false && lbl[x+2][y+1].getState() ==false && lbl[x+2][y+2].getState() ==false)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
							else if(lbl[x+3][y-1].getState() ==false && lbl[x+3][y+1].getState() ==false && lbl[x+3][y+2].getState() ==false)
							{
								blc.setx(blc.getPt().x+IntClass.BLOCKSIZE*2);
								blc.setBounds(blc.getPt().x,blc.getPt().y,IntClass.BLOCKSIZE*4,IntClass.BLOCKSIZE*4);
								blc.setState(6);
								blc.BlockColor();
								blc.setBlock();
							}
						}
						break;
					}
					
				}
		}
	
	
	
	public void Filllbl()
	{
		int y=0;
		switch(blc.getState())
				{
					case 0:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() )
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(3);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(3);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(3);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(3);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								y=i+2;
								CheckBreak(y);
								y=i+1;
								CheckBreak(y);
								crush = 2;
								cry[0] = i+1;
								cry[1] = i+2;
								down = false;
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(3);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(3);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(3);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(3);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								y=1;
								CheckBreak(y);
								y=0;
								CheckBreak(y);
								crush = 2;
								cry[0] = 0;
								cry[1] = 1;
								down = false;
							}
								
						}
						break;
					}
					case 1:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].getState()))
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(5);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(5);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setType(5);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(5);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								
								y=i+2;
								CheckBreak(y);
								y=i+1;
								CheckBreak(y);
								down = false;
								crush = 2;
								cry[0] = i+1;
								cry[1] = i+2;
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(5);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setType(5);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(5);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(5);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								y=1;
								CheckBreak(y);
								y=0;
								CheckBreak(y);
								down = false;
								crush = 2;
								cry[0] = 0;
								cry[1] = 1;
							}
								
						}
						break;
					}
					case 2:
					{ 
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() || (blc.getPt().x/IntClass.BLOCKSIZE>=0 &&  lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].getState()))
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(7);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(7);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(7);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+2].setType(7);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+2].setImg();
								y=i+2;
								CheckBreak(y);
								y=i+1;
								CheckBreak(y);
								down = false;
								crush = 2;
								cry[0] = i+1;
								cry[1] = i+2;
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(7);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setType(7);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(7);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(7);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								y=1;
								CheckBreak(y);
								y=0;
								CheckBreak(y);
								down = false;
								crush = 2;
								cry[0] = 0;
								cry[1] = 1;
							}
								
						}
						break;
					}
					case 3:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState())
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								y=i+2;
								CheckBreak(y);
								y=i+1;
								CheckBreak(y);
								down = false;
								crush = 2;
								cry[0] = i+1;
								cry[1] = i+2;
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								y=1;
								CheckBreak(y);
								y=0;
								CheckBreak(y);
								down = false;
								crush = 2;
								cry[0] = 0;
								cry[1] = 1;
							}
						}
						break;
					}
					case 4:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState())
							{
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								y=i+2;
								CheckBreak(y);
								y=i+1;
								CheckBreak(y);
								down = false;
								crush = 2;
								cry[0] = i+1;
								cry[1] = i+2;
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								y=1;
								CheckBreak(y);
								y=0;
								CheckBreak(y);
								down = false;
								crush = 2;
								cry[0] = 0;
								cry[1] = 1;
							}
						}
						break;
					}
					case 5:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState())
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();
						
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								y=i+2;
								CheckBreak(y);
								y=i+1;
								CheckBreak(y);
								down = false;
								crush = 2;
								cry[0] = i+1;
								cry[1] = i+2;
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								y=1;
								CheckBreak(y);
								y=0;
								CheckBreak(y);
								down = false;
								crush = 2;
								cry[0] = 0;
								cry[1] = 0;
							}
						}
						break;
					}
					case 6:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState())
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setType(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();	
								y=i+3;
								CheckBreak(y);
								repaint();
								y=i+2;
								CheckBreak(y);
								repaint();
								y=i+1;
								CheckBreak(y);
								repaint();
								y=i;
								CheckBreak(y);
								repaint();
								down = false;
								crush = 4;
								cry[0] = i;
								cry[1] = i+1;
								cry[2] = i+2;
								cry[3] = i+3;
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setType(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setImg();
								y=3;
								CheckBreak(y);
								y=2;
								CheckBreak(y);
								y=1;
								CheckBreak(y);
								y=0;
								CheckBreak(y);
								down = false;
								crush = 4;
								cry[0] = 0;
								cry[1] = 1;
								cry[2] = 2;
								cry[3] = 3;
							}
						}
						break;
					}
					case 7:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].getState()) || i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setType(5);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setType(5);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(5);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();	
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(5);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								y=i+2;
								CheckBreak(y);
								y=i+1;
								CheckBreak(y);
								y=i;
								CheckBreak(y);
								
								down = false;
								crush = 3;
								cry[0] = i;
								cry[1] = i+1;
								cry[2] = i+2;
								i = -1;
							}
						}
						break;
					}
					case 8:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].getState()) || i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setType(7);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(7);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(7);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(7);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								y=i+2;
								CheckBreak(y);
								y=i+1;
								CheckBreak(y);
								y=i;
								CheckBreak(y);
								down = false;
								crush = 3;
								cry[0] = i;
								cry[1] = i+1;
								cry[2] = i+2;

								i = -1;
							}
						}
						break;
					}
					case 9:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].getState()))
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
								y=i+3;
								CheckBreak(y);
								y=i+2;
								CheckBreak(y);
								y=i+1;
								CheckBreak(y);
								
								
								down = false;
								crush = 3;
								cry[0] = i+1;
								cry[1] = i+2;
								cry[2] = i+3;

								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
								y=2;
								CheckBreak(y);
								y=1;
								CheckBreak(y);
								y=0;
								CheckBreak(y);
								down = false;
								crush = 3;
								cry[0] = 0;
								cry[1] = 1;
								cry[2] = 2;

							}
						}
						break;
					}
					case 10:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if((lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].getState())|| (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState()) || (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].getState()))
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
								y=i+2;
								CheckBreak(y);
								y=i+1;
								CheckBreak(y);
								
								down = false;
								crush = 2;
								cry[0] = i+1;
								cry[1] = i+2;
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
								y=1;
								CheckBreak(y);
								y=0;
								CheckBreak(y);
								down = false;
								crush = 2;
								cry[0] = 0;
								cry[1] = 1;
							}
						}
						break;
					}
					case 11:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState())
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
								y=i+3;
								CheckBreak(y);
								y=i+2;
								CheckBreak(y);
								y=i+1;
								CheckBreak(y);
								down = false;
								crush = 3;
								cry[0] = i+1;
								cry[1] = i+2;
								cry[2] = i+3;
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(9);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								y=2;
								CheckBreak(y);
								y=1;
								CheckBreak(y);
								y=0;
								CheckBreak(y);
								down = false;
								crush = 3;
								cry[0] = 0;
								cry[1] = 1;
								cry[2] = 2                                                                                                                                                                         ;
							}
								
						}
						break;
					}
					case 12:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState())
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
								y=i+3;
								CheckBreak(y);
								y=i+2;
								CheckBreak(y);
								y=i+1;
								CheckBreak(y);
								
								down = false;
								crush = 3;
								cry[0] = i+1;
								cry[1] = i+2;
								cry[2] = i+3;
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								y=2;
								CheckBreak(y);
								y=1;
								CheckBreak(y);
								y=0;
								CheckBreak(y);
								down = false;
								crush = 3;
								cry[0] = 0;
								cry[1] = 1;
								cry[2] = 2;
							}
								
						}
						break;
					}
					case 13:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].getState()) || (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].getState()) )
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();		
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
								y=i+2;
								CheckBreak(y);
								y=i+1;
								CheckBreak(y);
								down = false;
								crush = 2;
								cry[0] = i+1;
								cry[1] = i+2;
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
								y=1;
								CheckBreak(y);
								y=0;
								CheckBreak(y);
								down = false;
								crush = 2;
								cry[0] = 0;
								cry[1] = 1;
							}
								
						}
						break;
					}
					case 14:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if( (lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].getState()) || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState())
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
								y=i+3;
								CheckBreak(y);
								y=i+2;
								CheckBreak(y);
								y=i+1;
								CheckBreak(y);
								down = false;
								crush = 3;
								cry[0] = i+1;
								cry[1] = i+2;
								cry[2] = i+3;
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(11);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								y=2;
								CheckBreak(y);
								y=1;
								CheckBreak(y);
								y=0;
								CheckBreak(y);
								down = false;
								crush = 3;
								cry[0] =0;
								cry[1] =1;
								cry[2] =2;
							}
								
						}
						break;
					}
					case 15:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()) || ( lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()))
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								y=i+2;
								CheckBreak(y);
								y=i+1;
								CheckBreak(y);
								y=i;
								CheckBreak(y);
								down = false;
								crush = 3;
								cry[0] = i;
								cry[1] = i+1;
								cry[2] = i+2;
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								y=2;
								CheckBreak(y);
								y=1;
								CheckBreak(y);
								y=0;
								CheckBreak(y);
								down = false;
								crush = 3;
								cry[0] = 0;
								cry[1] = 1;
								cry[2] = 2;
							}
								
						}
						break;
					}
					case 16:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState()) || (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()))
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								y=i+1;
								CheckBreak(y);
								y=i;
								CheckBreak(y);
								down = false;
								crush = 2;
								cry[0] = i;
								cry[1] = i+1;				
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								y=1;
								CheckBreak(y);
								y=0;
								CheckBreak(y);
								down = false;
								crush = 2;
								cry[0] = 0;
								cry[1] = 1;
							}
						
								
						}
						break;
					}
					case 17:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState())|| (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()))
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								y=i+2;
								CheckBreak(y);
								y=i+1;
								CheckBreak(y);
								y=i;
								CheckBreak(y);
								down = false;
								crush = 3;
								cry[0] = i;
								cry[1] = i+1;
								cry[2] = i+2;
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(13);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								y=2;
								CheckBreak(y);
								y=1;
								CheckBreak(y);
								y=0;
								CheckBreak(y);
								down = false;
								crush = 3;
								cry[0] = 0;
								cry[1] = 1;
								cry[2] = 2;
							}
								
						}
						break;
					}
					case 18:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState())
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setType(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								y=i+1;
								CheckBreak(y);
								down = false;
								crush = 1;
								cry[0] = i+1;						
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setType(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(true);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setType(1);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
								y=0;
								CheckBreak(y);
								down = false;
								crush = 1;
								cry[0] = i+1;
							}
								
						}
						break;
					}
				}
	}
	public void Initlbl()
	{
		switch(blc.getState())
				{
					case 0:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState())
							{

									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
								
						}
						break;
					}
					case 1:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].getState()))
							{

									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();

									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
								
						}
						break;
					}
					case 2:
					{ 
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState() || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| (blc.getPt().x/IntClass.BLOCKSIZE>=0 &&lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].getState()))
							{

									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
								
						}
						break;
					}
					case 3:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState())
							{
			
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
						}
						break;
					}
					case 4:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState())
							{
							
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
							
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
						}
						break;
					}
					case 5:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState())
							{
							
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
						}
						break;
					}
					case 6:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState())
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();	
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setImg();
							}
						}
						break;
					}
					case 7:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState() || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].getState()) || i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();	
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								
								
								
								i = -1;
							}
						}
						break;
					}
					case 8:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].getState()) || i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								i = -1;
							}
						}
						break;
					}
					case 9:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].getState()))
							{
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
							}
						}
						break;
					}
					case 10:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if((lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].getState())|| (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState()) || (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].getState()))
							{
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
								
									
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
							}
						}
						break;
					}
					case 11:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState())
							{
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
								
						}
						break;
					}
					case 12:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState())
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
							}
								
						}
						break;
					}
					case 13:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].getState()) || (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].getState()) )
							{
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
							}
								
						}
						break;
					}
					case 14:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].getState()) || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState())
							{
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
								
						}
						break;
					}
					case 15:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()) || (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()))
							{

								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
								
						}
						break;
					}
					case 16:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState()) || (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()))
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								
								i = -1;
							}
							else if(i == 1)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
						
								
						}
						break;
					}
					case 17:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState())|| (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()))
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();

							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
							}
								
						}
						break;
					}
					case 18:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState())
							{
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setType(0);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(0);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setType(0);
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
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState())
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(4);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(4);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(4);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(4);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(4);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(4);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(4);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(4);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
								
						}
						break;
					}
					case 1:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].getState()))
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(6);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(6);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setType(6);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(6);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(6);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setType(6);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(6);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(6);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
								
						}
						break;
					}
					case 2:
					{ 
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| (blc.getPt().x/IntClass.BLOCKSIZE>=0 &&lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].getState()))
							{
							
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(8);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(8);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(8);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+2].setType(8);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(8);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setType(8);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(8);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(8);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
								
						}
						break;
					}
					case 3:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState())
							{
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(10);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(10);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setType(10);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(10);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
						}
						break;
					}
					case 4:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState())
							{
							
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(12);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(12);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(12);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(12);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(12);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setType(12);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(12);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(12);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
						}
						break;
					}
					case 5:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState())
							{
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(14);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(14);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(14);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(14);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
						}
						break;
					}
					case 6:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState())
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setType(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();	
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setType(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][3].setImg();
							}
						}
						break;
					}
					case 7:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i-1].getState()) || i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setType(6);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setType(6);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(6);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();	
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(6);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								
								
								
								i = -1;
							}
						}
						break;
					}
					case 8:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE][i-1].getState()) || i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setType(8);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(8);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(8);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(8);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								i = -1;
							}
						}
						break;
					}
					case 9:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].getState()))
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
							}
						}
						break;
					}
					case 10:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
							if((lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].getState())|| (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState()) || (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].getState()))
							{
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
								
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
							}
						}
						break;
					}
					case 11:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState())
							{
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(10);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(10);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(10);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setType(10);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(10);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
								
						}
						break;
					}
					case 12:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState())
							{
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(12);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(12);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(12);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
							
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setType(12);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(12);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(12);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(12);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(12);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
							}
								
						}
						break;
					}
					case 13:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].getState()) || (lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].getState()) )
							{
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(12);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
							
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(12);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(12);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setType(12);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(12);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(12);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(12);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setType(12);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][1].setImg();
							}
								
						}
						break;
					}
					case 14:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].getState()) || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState())
							{
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(12);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setType(12);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+2].setImg();
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setType(12);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+3].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setType(12);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+3].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(12);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(12);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setType(12);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(12);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
							}
								
						}
						break;
					}
					case 15:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()) || lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState())
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
							
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
								
						}
						break;
					}
					case 16:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState()) || (lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()) || (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()))
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
								
								i = -1;
							}
							else if(i == 1)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][1].setImg();
							}
						
								
						}
						break;
					}
					case 17:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if((lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState())|| (i>0 && lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i-1].getState()))
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+2].setImg();
								
								i = -1;
							}
							else if(i == 1)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][1].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][1].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setType(14);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][2].setImg();
							}
								
						}
						break;
					}
					case 18:
					{
						for(int i = 17 - (blc.getPt().y/IntClass.BLOCKSIZE) ; i >= 0 ; i --)
						{
						
							if(lbl[blc.getPt().x/IntClass.BLOCKSIZE][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i].getState()|| lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i].getState())
							{
								
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setType(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE][i+1].setImg();	
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setType(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setType(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][i+1].setImg();
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setState(false);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setType(2);
									lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][i+1].setImg();
								
								i = -1;
							}
							else if(i == 0)
							{
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setType(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE][0].setImg();	
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setType(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+1][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setType(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+2][0].setImg();
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setState(false);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setType(2);
								lbl[blc.getPt().x/IntClass.BLOCKSIZE+3][0].setImg();
							}
								
						}
						break;
					}
				}
	}
	
	
	
	public class Td extends Thread
	{
		public Td()
		{
			
		}
		
		public void run()
		{
			while(set)
			{
				if(!down)
				{
					try{
								Thread.sleep(100);
							}catch(InterruptedException e){}
					switch(crush)
					{
						case 1:
						{
							CheckBreak(cry[0]);
							Gameover();
							break;
						}
						case 2:
						{
							CheckBreak(cry[0]);
							CheckBreak(cry[0]);
							CheckBreak(cry[1]);
							Gameover();
							break;
						}
						case 3:
						{
							CheckBreak(cry[0]);
							CheckBreak(cry[0]);
							CheckBreak(cry[0]);
							CheckBreak(cry[1]);
							CheckBreak(cry[1]);
							CheckBreak(cry[2]);
							Gameover();
							break;
						}
						case 4:
						{
							CheckBreak(cry[0]);
							CheckBreak(cry[0]);
							CheckBreak(cry[0]);
							CheckBreak(cry[0]);
							CheckBreak(cry[1]);
							CheckBreak(cry[1]);
							CheckBreak(cry[1]);
							CheckBreak(cry[2]);
							CheckBreak(cry[2]);
							CheckBreak(cry[3]);
							Gameover();
							break;
						}
					}
					crush = -1;
					
				}
				if(!over)
				{
					for(int i=0;i<20;i++)
					{
						for(int j=0;j<10;j++)
						{
							lbl[j][i].setType(15);
							lbl[j][i].setImg();
						}
						try{
								Thread.sleep(30);
							}catch(InterruptedException e){}
					}
					canstart = true;
				}
				
			}
		}
	}
	
}


