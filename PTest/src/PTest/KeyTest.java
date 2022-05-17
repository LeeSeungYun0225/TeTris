package PTest;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class KeyTest extends JFrame
{
	private ImageIcon img1,img2,img3,img4;
	private ImageIcon img1_1,img1_2,img2_1,img2_2,img3_1,img3_2,img4_1,img4_2;
	private int moveMode;
	private Point pt;
	private final int CHARACTER_XSIZE = 40;
	private final int CHARACTER_YSIZE = 40;
	private final int PANEL_XSIZE = 800;
	private final int PANEL_YSIZE = 670;
	private int moveMode2;
	private StartPanel sp;
	private JButton btnmove;
	
	public KeyTest() 
	{
		setLayout(null);
		setPreferredSize(new Dimension(PANEL_XSIZE,PANEL_YSIZE));
		sp = new StartPanel();
		add(sp);
		sp.setBounds(0,0,PANEL_XSIZE,PANEL_YSIZE);
		

	}
	

	
	
	public class StartPanel extends JPanel
	{
		private JButton startbtn,settingbtn;
		private Mapping mp;
		private UCharacter uc;
		private actionListener al;
		
		public StartPanel()
		{
			setPreferredSize(new Dimension(PANEL_XSIZE,PANEL_YSIZE));
			setBackground(Color.white);
			setLayout(null);
			mp = new Mapping("map1.txt");
			uc = new UCharacter();
			
			startbtn = new JButton(new ImageIcon("start.png"));
			settingbtn = new JButton(new ImageIcon("setting.png"));
			startbtn.setBounds(350,400,100,30);
			settingbtn.setBounds(350,450,100,30);
			add(startbtn);
			add(settingbtn);
			add(uc);
			add(mp);
			al = new actionListener();
			startbtn.addActionListener(al);
			settingbtn.addActionListener(al);
			mp.setBounds(0,0,PANEL_XSIZE,PANEL_YSIZE);
		}
		
		
		private class actionListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				Object obj = event.getSource();
				if(obj == startbtn)
				{
					startbtn.setVisible(false);
					settingbtn.setVisible(false);
					uc.setVisible(true);
					mp.setVisible(true);
				}
				else if(obj == settingbtn)
				{
					startbtn.setVisible(false);
					settingbtn.setVisible(false);
					mp.setVisible(true);
					mp.getmappingbtn().setVisible(true);
				}
			}
		}
	}
	
	private class UCharacter extends JButton implements Runnable,KeyListener
	{
		private Thread td;
		
		public UCharacter()//캐릭터를 라벨로 처리하였음 
		{

			img1 = new ImageIcon("back.png");
			img2 = new ImageIcon("front.png");
			img3 = new ImageIcon("left.png");
			img4 = new ImageIcon("right.png");
			img1_1 = new ImageIcon("back_move1.png");
			img1_2 = new ImageIcon("back_move2.png");
			img2_1 = new ImageIcon("front_move1.png");
			img2_2 = new ImageIcon("front_move2.png");
			img3_1 = new ImageIcon("left_move1.png");
			img3_2 = new ImageIcon("left_move2.png");
			img4_1 = new ImageIcon("right_move1.png");
			img4_2 = new ImageIcon("right_move2.png");
			setBackground(null);
			setBorderPainted(false);
			moveMode2 = 0;
			this.setIcon(img2);
			pt = new Point();
			pt.x=0;
			pt.y=0;
			moveMode = 0;
			setBounds(pt.x,pt.y,CHARACTER_XSIZE,CHARACTER_YSIZE);
			addKeyListener(this);
			td = new Thread(this);
			td.start();
			setVisible(false);
			
		}
		public void keyPressed(KeyEvent event)
		{
		switch(event.getKeyCode())
		{
			case KeyEvent.VK_LEFT:
			{	
				if(pt.x-CHARACTER_XSIZE >=0)
				{
					moveMode = 3;
				}
				break;
			}
			case KeyEvent.VK_RIGHT:
			{
				if(pt.x+CHARACTER_XSIZE <= PANEL_XSIZE-40)
				{
					moveMode = 4;
				}
				break;
			}
			case KeyEvent.VK_DOWN:
			{
				if(pt.y+CHARACTER_YSIZE <= PANEL_YSIZE- 80)
				{
					moveMode = 2;
				}
				break;
			}
			case KeyEvent.VK_UP:
			{
				if(pt.y-CHARACTER_YSIZE >=0)
				{
					moveMode = 1;
					
				}
				break;
			}
			
		}
	}
	public void keyReleased(KeyEvent event)
	{
	}
	public void keyTyped(KeyEvent event)
	{
	}
		
		public void run()
		{
			while(true)
			{
			if(moveMode == 1)
			{
				
				for(int i=0;i<10;i++)
				{
					if(moveMode2 == 0)
					{
						if(i<=4)
						{
							setIcon(img1_1);
						}
						else
						{
							setIcon(img1);
						}
					}
					else
					{
						if(i<=4)
						{
							setIcon(img1_2);
						}
						else
						{
							setIcon(img1);
						}
					}
			
					pt.y-= (CHARACTER_YSIZE/10);
					setBounds(pt.x,pt.y,CHARACTER_XSIZE,CHARACTER_YSIZE);
					try{
						Thread.sleep(20);
					}catch(InterruptedException e){}
				}
				if(moveMode2 == 1)
				{
					moveMode2 =0;
				}
				else
				{
					moveMode2 = 1;
				}
				moveMode = 0;
			}
			
			else if(moveMode == 2)
			{
				for(int i=0;i<10;i++)
				{
					if(moveMode2 == 0)
					{
						if(i<=4)
						{
							setIcon(img2_1);
						}
						else
						{
							setIcon(img2);
						}
					}
					else
					{
						if(i<=4)
						{
							setIcon(img2_2);
						}
						else
						{
							setIcon(img2);
						}
					}

					pt.y+= (CHARACTER_YSIZE/10);
					setBounds(pt.x,pt.y,CHARACTER_XSIZE,CHARACTER_YSIZE);
					try{
						Thread.sleep(20);
					}catch(InterruptedException e){}
				}
				if(moveMode2 == 1)
				{
					moveMode2 =0;
				}
				else
				{
					moveMode2 = 1;
				}
				moveMode = 0;
			}
			else if(moveMode == 3)
			{
				for(int i=0;i<10;i++)
				{
					if(moveMode2 == 0)
					{
						if(i<=4)
						{
							setIcon(img3_1);
						}
						else
						{
							setIcon(img3);
						}
					}
					else
					{
						if(i<=4)
						{
							setIcon(img3_2);
						}
						else
						{
							setIcon(img3);
						}
					}
					pt.x-= (CHARACTER_XSIZE/10);
					setBounds(pt.x,pt.y,CHARACTER_XSIZE,CHARACTER_YSIZE);
					try{
						Thread.sleep(20);
					}catch(InterruptedException e){}
				}
				if(moveMode2 == 1)
				{
					moveMode2 =0;
				}
				else
				{
					moveMode2 = 1;
				}
				moveMode = 0;
			}
			else if(moveMode == 4)
			{
				for(int i=0;i<10;i++)
				{
					if(moveMode2 == 0)
					{
						if(i<=4)
						{
							setIcon(img4_1);
						}
						else
						{
							setIcon(img4);
						}
					}
					else
					{
						if(i<=4)
						{
							setIcon(img4_2);
						}
						else
						{
							setIcon(img4);
						}
					}
					pt.x+= (CHARACTER_XSIZE/10);
					setBounds(pt.x,pt.y,CHARACTER_XSIZE,CHARACTER_YSIZE);
					try{
						Thread.sleep(20);
					}catch(InterruptedException e){}
				}
				if(moveMode2 == 1)
				{
					moveMode2 =0;
				}
				else
				{
					moveMode2 = 1;
				}
				moveMode = 0;
			}

		}		
		}
	}

}
