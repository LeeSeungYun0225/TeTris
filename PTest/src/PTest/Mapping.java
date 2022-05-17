package PTest;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;


public class Mapping extends JPanel implements MouseListener,MouseMotionListener //맵 제작 및 사용할 때 필요한 패널 
{
	private Floor[][] arfl;
	private int drawMode,setting;
	private JButton mappingbtn;
	private JButton bushbtn;
	private actionListener al;
	private FileWriter writer;
	private FileReader reader;
	private int[] arr;
	private JButton savebtn;
	
	public Mapping()
	{
		arfl = new Floor[20][16];
		Initfl();
		setPreferredSize(new Dimension(800,670));
		setBackground(Color.white);
		addMouseListener(this);
		addMouseMotionListener(this);
		setLayout(null);
		mappingbtn = new JButton("Map");
		bushbtn = new JButton("Bush");
		mappingbtn.setBounds(720,10,70,30);
		bushbtn.setBounds(720,50,70,30);
		add(mappingbtn);
		add(bushbtn);
		mappingbtn.setBackground(Color.black);
		mappingbtn.setForeground(Color.white);
		bushbtn.setBackground(Color.black);
		bushbtn.setForeground(Color.white);
		mappingbtn.setVisible(false);
		bushbtn.setVisible(false);
		savebtn = new JButton("Save");
		savebtn.setBounds(720,90,70,30);
		savebtn.setBackground(Color.black);
		savebtn.setForeground(Color.white);
		add(savebtn);
		savebtn.setVisible(false);
		drawMode = -1;
		setting = -1;
		
		al = new actionListener();
		
		mappingbtn.addActionListener(al);
		bushbtn.addActionListener(al);
		savebtn.addActionListener(al);
		setVisible(false);	
	}
	
	public Mapping(String filename)
	{
		arfl = new Floor[20][16];
		Initfl();
		setPreferredSize(new Dimension(800,670));
		setBackground(Color.white);
		addMouseListener(this);
		addMouseMotionListener(this);
		setLayout(null);
		mappingbtn = new JButton("Map");
		bushbtn = new JButton("Bush");
		mappingbtn.setBounds(720,10,70,30);
		bushbtn.setBounds(720,50,70,30);
		add(mappingbtn);
		add(bushbtn);
		mappingbtn.setBackground(Color.black);
		mappingbtn.setForeground(Color.white);
		bushbtn.setBackground(Color.black);
		bushbtn.setForeground(Color.white);
		mappingbtn.setVisible(false);
		bushbtn.setVisible(false);
		savebtn = new JButton("Save");
		savebtn.setBounds(720,90,70,30);
		savebtn.setBackground(Color.black);
		savebtn.setForeground(Color.white);
		add(savebtn);
		savebtn.setVisible(false);
		drawMode = -1;
		setting = -1;
		
		al = new actionListener();
		
		mappingbtn.addActionListener(al);
		bushbtn.addActionListener(al);
		savebtn.addActionListener(al);
		setVisible(false);	
		
		try{
			reader = new FileReader(filename);

			for(int i =0; i<20;i++)
			{
				for(int j=0;j<16;j++)
				{
					for(int k=0;k<6;k++)
					{
						switch(k)
						{
							case 0:
							{	
								int readed;
								Point pttemp = new Point();
								pttemp.x = i*40;
								pttemp.y = j*40;
								readed = reader.read();
								arfl[i][j] = new Floor(readed,pttemp);
								break;
							}
							case 1:
							{
								arfl[i][j].setmovable(reader.read());
								break;
							}
							case 2:
							{
								arfl[i][j].setdialog(reader.read());
								break;
							}
							case 3:
							{
								arfl[i][j].setbattle(reader.read());
								break;
							}
							case 4:
							{
								arfl[i][j].setcurrent(reader.read());
								break;
							}
							case 5:
							{
								arfl[i][j].setisFull(reader.read());
								break;
							}
						}//floorType,movable,dialog,battle,current,isFull(int)
					}
					add(arfl[i][j]);
				}
			}
			
		}catch(FileNotFoundException e){System.out.println("파일을 찾을 수 없습니다");}
		catch(IOException e){}
		finally
		{
			try{
				reader.close();
			}catch(Exception e){System.out.println("파일을 닫는데 실패했습니다");}
		}
		repaint();
	}
	public void Initfl()
	{
		for(int i=0;i<20;i++)
		{
			for(int j=0;j<16;j++)
			{
				arfl[i][j] = new Floor();
			}
		}
	}

	public JButton getmappingbtn()
	{
		return mappingbtn;
	}
	public JButton getbushbtn()
	{
		return bushbtn;
	}
	private class actionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			Object obj = event.getSource();
			if(obj == mappingbtn)
			{
				if(setting == -1)
				{
					bushbtn.setVisible(true);
					savebtn.setVisible(true);
					setting = 0;
				}
				else
				{
					bushbtn.setVisible(false);
					savebtn.setVisible(false);
					drawMode = -1;
					setting = -1;
				}
			}
			else if(obj == bushbtn)
			{
				drawMode = 1;
			}
			else if(obj == savebtn)
			{
				//io//
				try{
					writer = new FileWriter("map1.txt");
					for(int i=0;i<20;i++)
					{
						for(int j=0;j<16;j++)
						{
							writer.write(arfl[i][j].getfloorType());
							writer.write(arfl[i][j].getmovable());
							writer.write(arfl[i][j].getdialog());
							writer.write(arfl[i][j].getbattle());
							writer.write(arfl[i][j].getcurrent());
							writer.write(arfl[i][j].getisFull());
							//floorType,movable,dialog,battle,current,isFull(int)
						}
					}
				}catch(IOException e){System.out.println("파일을 출력하는데 실패하였습니다");}
				finally
				{
					try{
						writer.close();
					}catch(Exception e){System.out.println("파일을 닫는데 실패하였습니다");}
				}
			}
		}
	}
	public void mouseClicked(MouseEvent event){}
	public void mousePressed(MouseEvent event)
	{
		Point temp = event.getPoint();
		int index1,index2;
		index1 = temp.x/40;
		index2 = temp.y/40;
		temp.x = index1*40;
		temp.y = index2*40;

		if(event.getPoint().x>=0 && event.getPoint().x <=799 && event.getPoint().y >=0 && event.getPoint().y <=639 && drawMode != -1)
		{
			if(!arfl[index1][index2].isFull())//해당 인덱스가 비어있다면 
			{
				switch(drawMode)
				{
					case 1:
					{
						arfl[index1][index2] = new Floor(1,temp);
						break;
					}
					default :
					{
						break;
					}	
				}
				add(arfl[index1][index2]);
				repaint();
			}
		}
	}
	public void mouseReleased(MouseEvent event){}
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void mouseMoved(MouseEvent event){}
	public void mouseDragged(MouseEvent event)
	{
		Point temp = event.getPoint();
		int index1,index2;
		index1 = temp.x/40;
		index2 = temp.y/40;
		temp.x = index1*40;
		temp.y = index2*40;
		
		if(event.getPoint().x>=0 && event.getPoint().x <=799 && event.getPoint().y >=0 && event.getPoint().y <=639 && drawMode != -1)
		{
			if(!arfl[index1][index2].isFull())//해당 인덱스가 비어있다면 
			{
				switch(drawMode)
				{
					case 1:
					{
						arfl[index1][index2] = new Floor(1,temp);
						break;
					}
					default :
					{
						break;
					}
				}
				add(arfl[index1][index2]);
					repaint();
			}
		}
	}
	
} 