package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartGame extends JPanel{
	
	private JButton singleGameBtn,multiGameBtn,exitBtn;
	private JLabel tetrisMainScreen,loginLabel;// ���� ���� �̹��� 
	private Communication communication;
	private GamePlay gamePlay;
	private LoginFrame loginFrame;
	private OpponentPanel oppPanel;
	private String serverAddress = "192.168.0.33";
	private int portNumber = 8877;
	private boolean gameOverThrow = false;
	private String myId = "";
	private String myPass = "";
	private String oppId = "";
	private int myWin=0;
	private int myLose =0;
	private int oppWin = 0;
	private int oppLose=0;
	
	private boolean tryLogin = false;
	private boolean trySign = false;
	private boolean isLogin = false;
	
	private ImageIcon mainImage;
	private JLabel mainImageLbl;
	
	private JLabel test;
	private RecordLabel myRecordLabel,oppRecordLabel;
	
	private JLabel winImageLabel,loseImageLabel;
	private final int myResultX = 0,myResultY = 0,oppResultX = 600,oppResultY = 0,resultXSize =200,resultYSize = 200;
	private ImageIcon winImage,loseImage;
	
	
	public StartGame() // ���� ���� ȭ�� ���� 
	{
		// ���� ��ư�� �ؽ�Ʈ�ʵ�,  ���̺��� �ʱ�ȭ�ϰ� �迭 , ������ //
		setLayout(null);
		setBounds(0,0,1000,600);
		singleGameBtn = new JButton("Single Play");
		multiGameBtn = new JButton("Multi Play");
		exitBtn = new JButton("Exit");
		tryLogin= false;
		trySign = false;
		isLogin = false;
		
		
		winImage = new ImageIcon("Win.png");
		loseImage = new ImageIcon("lose.png");
		winImageLabel = new JLabel(winImage);
		loseImageLabel = new JLabel(loseImage);
		add(winImageLabel);
		add(loseImageLabel);
		winImageLabel.setVisible(false);
		loseImageLabel.setVisible(false);

		
		myRecordLabel = new RecordLabel();
		myRecordLabel.setBounds(0,0,200,30);
		add(myRecordLabel);
		
		myRecordLabel.setVisible(false);
		oppRecordLabel = new RecordLabel();
		oppRecordLabel.setBounds(600,0,200,30);
		add(oppRecordLabel);
		oppRecordLabel.setVisible(false);
		
	
		mainImage =new ImageIcon("main.png");
		mainImageLbl = new JLabel(mainImage);
		mainImageLbl.setBounds(150,0,600,200);
		add(mainImageLbl);
		
		singleGameBtn.setBounds(300,430,100,50);
		multiGameBtn.setBounds(500,430,100,50);
		exitBtn.setBounds(700,430,100,50);
		exitBtn.setVisible(false);
		add(singleGameBtn);
		add(multiGameBtn);
		add(exitBtn);
		
		gamePlay = new GamePlay();
		
		gamePlay.setBounds(0,30,600,500);
		add(gamePlay);
		gamePlay.setVisible(false);
		
		loginFrame = new LoginFrame();
		loginFrame.setBounds(330,150,350,300);
		loginFrame.setVisible(false);
		
		loginLabel = new JLabel();
		loginLabel.setBounds(100,100,500,500);
		add(loginLabel);
		
		oppPanel = new OpponentPanel();
		oppPanel.setBounds(600,30,600,500);
		add(oppPanel);
		
		oppPanel.setVisible(false);
		
		
		

		
		singleGameBtn.addActionListener(new ActionListener() { // �̱� ���� ���� ��ư 
			public void actionPerformed(ActionEvent e) 
			{
				singleGameBtn.setVisible(false);
				multiGameBtn.setVisible(false);
				exitBtn.setVisible(true);
				gamePlay.setVisible(true);
				loginFrame.setVisible(false);
				mainImageLbl.setVisible(false);
			}
		});
		
		
		exitBtn.addActionListener(new ActionListener() { // ���� ���� ������ ��ư 
			public void actionPerformed(ActionEvent e)
			{		
				singleGameBtn.setVisible(true);
				multiGameBtn.setVisible(true);
				exitBtn.setVisible(false);
				gamePlay.setVisible(false);
				gamePlay.initGame();
				oppPanel.setVisible(false);
				oppRecordLabel.setVisible(false);
				myRecordLabel.setVisible(false);
				if(gamePlay.getNetworkChecker())
				{
					
					gamePlay.setNetworkChecker(false);
					try {
						communication.endCommunication();
					}catch(NullPointerException e1) {}
				}	
				isLogin = false;
				mainImageLbl.setVisible(true);
			}
		});
		
		multiGameBtn.addActionListener(new ActionListener() { // ��Ƽ ���� ���� ��ư 
			public void actionPerformed(ActionEvent e)
			{

				Communication communication = new Communication();
				System.out.println("���� ���� �õ�");
				loginFrame.setVisible(true);
			}
		});
		
		
	}
	
	public class LoginFrame extends JFrame // �α��� ȭ�� ������ 
	{
		private JTextField userIdField,userPasswordField,signIdField,signPasswordField,signPasswordConField;
		private JLabel idLabel,passwordLabel,passwordConLabel;
		private JButton loginBtn,signBtn,signConBtn,signCancelBtn;
		
		
		
		public LoginFrame()
		{
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	
			setTitle("�α���");
			setLayout(null);	
			idLabel = new JLabel("���̵�      :");
			idLabel.setBounds(60,20,100,30);
			add(idLabel);
			passwordLabel = new JLabel("��й�ȣ  :");
			passwordLabel.setBounds(60,60,100,30);
			add(passwordLabel);
			passwordConLabel = new JLabel("��й�ȣ Ȯ�� :");
			passwordConLabel.setBounds(60,100,100,30);
			add(passwordConLabel);
			passwordConLabel.setVisible(false);
			
			userIdField = new JTextField();
			userIdField.setBounds(160,20,100,30);
			add(userIdField);
			
			userPasswordField = new JTextField("");
			userPasswordField.setBounds(160,60,100,30);
			add(userPasswordField);
			
			signIdField = new JTextField();
			signPasswordField = new JTextField();
			signPasswordConField = new JTextField();
			
			signIdField.setBounds(160,20,100,30);
			signPasswordField.setBounds(160,60,100,30);
			signPasswordConField.setBounds(160,100,100,30);
			
			signIdField.setVisible(false);
			signPasswordField.setVisible(false);
			signPasswordConField.setVisible(false);
			
			add(signIdField);
			add(signPasswordField);
			add(signPasswordConField);
			
			loginBtn = new JButton("�α���");
			loginBtn.setBounds(60,200,90,30);
			add(loginBtn);
			signBtn = new JButton("ȸ������");
			signBtn.setBounds(180,200,90,30);
			add(signBtn);
			signConBtn = new JButton("���ԿϷ�");
			signConBtn.setBounds(60,200,90,30);
			signConBtn.setVisible(false);
			signCancelBtn = new JButton("�������");
			signCancelBtn.setBounds(180,200,90,30);
			signCancelBtn.setVisible(false);
			add(signCancelBtn);
			add(signConBtn);
			
			loginBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					if(!userIdField.getText().equals("") && !userPasswordField.getText().equals(""))
					{
						
						gameOverThrow= false;				
						myId = userIdField.getText();
						myPass = userPasswordField.getText();					
						tryLogin = true;		
					}				
				}
			});
			
			signBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					//idLabel.setVisible(false);
					//passwordLabel.setVisible(false);
					userIdField.setVisible(false);
					userIdField.setText("");
					userPasswordField.setVisible(false);
					userPasswordField.setText("");
					loginBtn.setVisible(false);
					signBtn.setVisible(false);
					signConBtn.setVisible(true);
					signCancelBtn.setVisible(true);
					passwordConLabel.setVisible(true);
					signIdField.setVisible(true);
					signPasswordField.setVisible(true);
					signPasswordConField.setVisible(true);
				
					
				}
			});
			
			signConBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					if(signPasswordField.getText().equals(signPasswordConField.getText()) && !signIdField.getText().equals("") && !signPasswordField.getText().equals(""))
					{
						myId = signIdField.getText();
						myPass = signPasswordField.getText();
						gameOverThrow= false;
						trySign = true;
					}
					else
					{
						System.out.println("�н����带 Ȯ�� �� �ּ���");
					}
				}
			});
			
			signCancelBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					//idLabel.setVisible(true);
					//passwordLabel.setVisible(true);
					userIdField.setVisible(true);
					userPasswordField.setVisible(true);
					signConBtn.setVisible(false);
					signCancelBtn.setVisible(false);
					loginBtn.setVisible(true);
					signBtn.setVisible(true);
					passwordConLabel.setVisible(false);
					signIdField.setVisible(false);
					signPasswordField.setVisible(false);
					signPasswordConField.setVisible(false);
				}
			});
	
		}


	
	}
	
	
	public class SendThread extends Thread
	{
		private Socket socket;
		private OutputStream outputStream;
		private DataOutputStream dataOutputStream;
		private String outputString;
		
		
		public SendThread()
		{
			
		}
		
		public SendThread(Socket in)
		{
			socket = in;
		}
		public void finalize() throws Throwable
		{
			socket.close();
			outputStream.close();
			dataOutputStream.close();
		}
		public void endSend()
		{
			
			try {
				dataOutputStream.close();
				outputStream.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void run()
		{
			
			try {
				outputStream = socket.getOutputStream();
				dataOutputStream = new DataOutputStream(outputStream);
				dataOutputStream.writeUTF("test1");
					
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			while(true)
			{
				try {
					Thread.sleep(0);
				}
				catch(Exception e)
				{}
				if(!isLogin) // �α��� ���� 
				{
					
					if(tryLogin)// �α��� �õ� 
					{
						
						outputString = "login " + myId + " " + myPass;
						System.out.println("�α��� �õ�" + " " + outputString);
						try {
							dataOutputStream.writeUTF(outputString);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.out.println("�۽��� �����߻�");
							e.printStackTrace();
						}
						tryLogin = false;
					}
					else if(trySign) // ȸ�� ���� �õ� 
					{
						System.out.println("���� �õ�");
						outputString = "sign " + myId + " " + myPass;
						
						try {
							dataOutputStream.writeUTF(outputString);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.out.println("�۽��� �����߻�");
							e.printStackTrace();
						}
						trySign = false;
					}
					
				}
				else // �α��� �Ŀ� 
				{
					
					if(gamePlay.getMessageChecker())  
					{
						if(gamePlay.getMessageType() == 2)// ���� ���� ��ư ������ �� 
						{
							
							outputString = "s";
							System.out.println("�޼��� ���� : " + outputString);
							gamePlay.setMessageType(0);
							exitBtn.setVisible(false);
							winImageLabel.setVisible(false);
							loseImageLabel.setVisible(false);
						}
						else if(gamePlay.getEndChecker()) // ���ӿ����� 
						{
							outputString = "g";
							System.out.println("�޼��� ���� : " + outputString);
							myLose++;
							oppWin++;
							myRecordLabel.setLose(myLose);
							oppRecordLabel.setWin(oppWin);
							gamePlay.setEndChecker(false);
							exitBtn.setVisible(true);
							winImageLabel.setVisible(true);
							loseImageLabel.setVisible(true);
							winImageLabel.setBounds(oppResultX,oppResultY,resultXSize,resultYSize);
							loseImageLabel.setBounds(myResultY,myResultY,resultXSize,resultYSize);
						}
						else if(gameOverThrow)
						{
							System.out.println("check");
						}
						else if(gamePlay.getMessageType() == 0) // ��� ���¸� �޾� ������ �� 
						{
							outputString = gamePlay.readBlock();
						}
						
						
						try {
							dataOutputStream.writeUTF(outputString);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.out.println("�۽��� �����߻�");
							e.printStackTrace();
						}
						gamePlay.setCrush(0);
						gamePlay.setMessageChecker(false);
						
					}
					
					
				}
				
			}
			
		}
	}
	
	public class ReceiveThread extends Thread
	{
		private Socket socket;
		private InputStream inputStream;
		
		private DataInputStream dataInputStream;
		private String inputString;
		private String[] tokenizing;
		
		public ReceiveThread(Socket in)
		{
			socket = in;

		}
		
		public void finalize() throws Throwable
		{
			socket.close();
			inputStream.close();
			dataInputStream.close();
		}
		
		public void endReceive()
		{
			
			try {
				dataInputStream.close();
				inputStream.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		public void run()
		{
			try
			{
				  inputStream = socket.getInputStream(); 
				  dataInputStream = new DataInputStream(inputStream);
				  
			}catch(Exception e)
			{
				
			}
			
			while(true)
			{
				try {
					inputString= dataInputStream.readUTF();
				}catch(SocketException e1) {
					break;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("������ ���� ����");
					e.printStackTrace();
				}
				System.out.println("�������� ���۵� ���� : " + inputString );
				tokenizing = inputString.split(" ");
				
				if(!isLogin) // �α��� ������ ���ư� 
				{
					if(tokenizing[0].equals("loginSuccess"))
					{
						tryLogin=false;
						myWin = Integer.parseInt(tokenizing[1]);
						myLose = Integer.parseInt(tokenizing[2]);
						isLogin = true;
						System.out.println("�α��� ����");
						loginFrame.setVisible(false);
						startMultiGame();
						mainImageLbl.setVisible(false);
						myRecordLabel.setVisible(true);
						myRecordLabel.setWin(myWin);
						myRecordLabel.setLose(myLose);
						myRecordLabel.setId(myId);
						
					}
					else if(tokenizing[0].equals("loginFailed"))
					{
						tryLogin=false;
						System.out.println("�α��� ����");
					}
					else if(tokenizing[0].equals("SignSuccess"))
					{
						loginFrame.setVisible(false);
						trySign = false;
						isLogin = true;
						myWin = 0;
						myLose = 0;
						myRecordLabel.setVisible(true);
						myRecordLabel.setWin(myWin);
						myRecordLabel.setLose(myLose);
						myRecordLabel.setId(myId);
						startMultiGame();
						System.out.println("ȸ������ ����");
						mainImageLbl.setVisible(false);
					}
					else if(tokenizing[0].equals("SignFailed"))
					{
						trySign = false;
						
						System.out.println("ȸ������ ����");
					}
				}
				else // �α��� �Ŀ��� ���ư� 
				{
					if(tokenizing[0].equals("s")) // ���� ���� �߻���
					{
						oppWin = Integer.parseInt(tokenizing[1]);
						oppLose = Integer.parseInt(tokenizing[2]);
						oppId = tokenizing[3];
						oppRecordLabel.setWin(oppWin);
						oppRecordLabel.setLose(oppLose);
						oppRecordLabel.setId(oppId);
						oppRecordLabel.setVisible(true);
						
						gamePlay.startButton();
						oppPanel.initBlocks();
					}
					else if(tokenizing[0].equals("g")) // ���� ���ӿ����� 
					{
						gamePlay.win();
						exitBtn.setVisible(true);
						myWin++;
						oppLose++;
						myRecordLabel.setWin(myWin);
						oppRecordLabel.setLose(oppLose);
						winImageLabel.setVisible(true);
						winImageLabel.setBounds(myResultX,myResultY,resultXSize,resultYSize);
						loseImageLabel.setBounds(oppResultX,oppResultY,resultXSize,resultYSize);	
						loseImageLabel.setVisible(true);
					}
					else if(tokenizing[0].equals("b")) // ���� ��� ������ 
					{
						inputString = inputString.substring(4,inputString.length());
						gamePlay.crush(Integer.parseInt(tokenizing[1]));
						oppPanel.setBlocks(inputString);
						System.out.println("��� ��� �б� : " + inputString );
					}
				}
				
				
				
				
				try {
					Thread.sleep(0);
				}catch(Exception e) {}
			}
		}
	}
	
	public class Communication // ��Ʈ������ �ν��� ��� �����͸� �޾� ������ ����ϴ� ������ 
	{
		private Socket socket;
		private SendThread sender;
		private ReceiveThread receiver;
		
		public boolean isConnected()
		{
			if(socket.isConnected() && sender.isAlive() && receiver.isAlive())
			{
				return true;
			}
			else 
			{
				return false;
			}
		}
		
		public Communication()
		{
			
			try {
				 socket = new Socket(serverAddress , portNumber) ;
			     
				 
				 if(socket.isConnected())
				 {
					 System.out.println("������ ������ �Ǿ����ϴ�.");
					 
				     sender = new SendThread(socket);
				     sender.start();
				     receiver = new ReceiveThread(socket);
				     receiver.start();
				 }
				
			     
			  
			}
			catch(IOException e)
			{
				
			}
		}
		
		public Socket getSocket()
		{
			return socket;
		}
		
		public void endCommunication()
		{
			try {
				socket.close();
				receiver.endReceive();
				sender.endSend();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sender.interrupt();
			receiver.interrupt();
		}
		
		public void finalize() throws Throwable
		{	
			sender.interrupt();
			receiver.interrupt();
		}
	}
	
	public Communication getCommunication()
	{
		return communication;
	}
	
	public void startMultiGame()
	{
		gamePlay.initGame();
		 gamePlay.setNetworkChecker(true);
		 oppPanel.setVisible(true);
		 singleGameBtn.setVisible(false);
		 multiGameBtn.setVisible(false);
		 exitBtn.setVisible(true);
		 gamePlay.setVisible(true);
		 loginFrame.setVisible(false);
	}

}


