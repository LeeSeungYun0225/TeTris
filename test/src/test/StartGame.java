package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartGame extends JPanel{
	
	private JButton singleGameBtn,multiGameBtn,exitBtn;
	private JLabel tetrisMainScreen,loginLabel;// 게임 시작 이미지 
	private Communication communication;
	private GamePlay gamePlay;
	private LoginFrame loginFrame;
	private OpponentPanel oppPanel;
	

	public StartGame() // 게임 시작 화면 구성 
	{
		// 각종 버튼과 텍스트필드,  레이블을 초기화하고 배열 , 세팅함 //
		setLayout(null);
		setBounds(0,0,1000,600);
		singleGameBtn = new JButton("Single Play");
		multiGameBtn = new JButton("Multi Play");
		exitBtn = new JButton("Exit");
		
		
		singleGameBtn.setBounds(300,400,100,50);
		multiGameBtn.setBounds(500,400,100,50);
		exitBtn.setBounds(700,400,100,50);
		exitBtn.setVisible(false);
		add(singleGameBtn);
		add(multiGameBtn);
		add(exitBtn);
		
		gamePlay = new GamePlay();
		
		gamePlay.setBounds(0,0,600,500);
		add(gamePlay);
		gamePlay.setVisible(false);
		
		loginFrame = new LoginFrame();
		loginFrame.setBounds(330,150,350,300);
		loginFrame.setVisible(false);
		
		loginLabel = new JLabel();
		loginLabel.setBounds(100,100,500,500);
		add(loginLabel);
		
		oppPanel = new OpponentPanel();
		oppPanel.setBounds(600,0,600,500);
		add(oppPanel);
		
		oppPanel.setVisible(false);
		
		
		
		singleGameBtn.addActionListener(new ActionListener() { // 싱글 게임 진입 버튼 
			public void actionPerformed(ActionEvent e) 
			{
				singleGameBtn.setVisible(false);
				multiGameBtn.setVisible(false);
				exitBtn.setVisible(true);
				gamePlay.setVisible(true);
				loginFrame.setVisible(false);
			}
		});
		
		
		exitBtn.addActionListener(new ActionListener() { // 싱글 게임 도중 나가기 버튼 
			public void actionPerformed(ActionEvent e)
			{
				singleGameBtn.setVisible(true);
				multiGameBtn.setVisible(true);
				exitBtn.setVisible(false);
				gamePlay.setVisible(false);
				gamePlay.initGame();
				gamePlay.setNetworkChecker(false);
				try {
					communication.endCommunication();
				}catch(NullPointerException e1) {}
				
			}
		});
		
		multiGameBtn.addActionListener(new ActionListener() { // 멀티 게임 진입 버튼 
			public void actionPerformed(ActionEvent e)
			{
				loginFrame.setVisible(true);
			}
		});
		
		
	}
	
	public class LoginFrame extends JFrame // 로그인 화면 프레임 
	{
		private JTextField userIdField,userPasswordField,signIdField,signPasswordField,signPasswordConField;
		private JLabel idLabel,passwordLabel,passwordConLabel;
		private JButton loginBtn,signBtn,signConBtn,signCancelBtn;
		
		
		
		public LoginFrame()
		{
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	
			setTitle("로그인");
			setLayout(null);	
			idLabel = new JLabel("아이디      :");
			idLabel.setBounds(60,20,100,30);
			add(idLabel);
			passwordLabel = new JLabel("비밀번호  :");
			passwordLabel.setBounds(60,60,100,30);
			add(passwordLabel);
			passwordConLabel = new JLabel("비밀번호 확인 :");
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
			
			loginBtn = new JButton("로그인");
			loginBtn.setBounds(60,200,90,30);
			add(loginBtn);
			signBtn = new JButton("회원가입");
			signBtn.setBounds(180,200,90,30);
			add(signBtn);
			signConBtn = new JButton("가입완료");
			signConBtn.setBounds(60,200,90,30);
			signConBtn.setVisible(false);
			signCancelBtn = new JButton("가입취소");
			signCancelBtn.setBounds(180,200,90,30);
			signCancelBtn.setVisible(false);
			add(signCancelBtn);
			add(signConBtn);
			
			loginBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					Communication communication = new Communication();
					
					
					setVisible(false);
					
					
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
				if(gamePlay.getEndChecker()) // 게임오버시 
				{
					outputString = "g";
					System.out.println("메세지 전송 : " + outputString);
					try {
						dataOutputStream.writeUTF(outputString);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("발신중 문제발생");
						e.printStackTrace();
					}
					
					gamePlay.setEndChecker(false);
				}
				
				if(gamePlay.getMessageChecker())  
				{
					if(gamePlay.getMessageType() == 2)// 게임 시작 버튼 눌렀을 때 
					{
						outputString = "s";
						System.out.println("메세지 전송 : " + outputString);
						gamePlay.setMessageType(0);
					}
					
					else if(gamePlay.getMessageType() == 0) // 블록 상태를 받아 전송할 때 
					{
						outputString = gamePlay.readBlock();
					}
					
					try {
						dataOutputStream.writeUTF(outputString);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("발신중 문제발생");
						e.printStackTrace();
					}
					gamePlay.setCrush(0);
					gamePlay.setMessageChecker(false);
				}
				
				try {
					Thread.sleep(0);
				}
				catch(Exception e)
				{}
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("서버에서 전송된 문자 : " + inputString );
				tokenizing = inputString.split(" ");
				
				if(tokenizing[0].equals("b")) // 상대방 블록 받을시 
				{
					inputString = inputString.substring(4,inputString.length());
					gamePlay.crush(Integer.parseInt(tokenizing[1]));
					oppPanel.setBlocks(inputString);
					System.out.println("상대 블록 읽기 : " + inputString );
				}
				else if(tokenizing[0].equals("w")) // 상대방 게임오버시 
				{
					gamePlay.win();
				}
				else if(tokenizing[0].equals("s")) // 시작 문자 발생시
				{
					gamePlay.startButton();
					oppPanel.initBlocks();
				}
			}
		}
	}
	
	public class Communication // 테트리스의 부숴진 블록 데이터를 받아 서버와 통신하는 스레드 
	{
		private Socket socket;
		private SendThread sender;
		private ReceiveThread receiver;
		
		public Communication()
		{
			
			try {
				 socket = new Socket("192.168.0.33" , 8877) ;
			     
				 
				 if(socket.isConnected())
				 {
					 System.out.println("서버와 접속이 되었습니다.");
					 gamePlay.initGame();
					 gamePlay.setNetworkChecker(true);
					 oppPanel.setVisible(true);
					 singleGameBtn.setVisible(false);
					 multiGameBtn.setVisible(false);
					 exitBtn.setVisible(true);
					 gamePlay.setVisible(true);
					 loginFrame.setVisible(false);
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

}


