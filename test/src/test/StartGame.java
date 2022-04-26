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
					
					gamePlay.setNetworkChecker(true);
					setVisible(false);
					singleGameBtn.setVisible(false);
					multiGameBtn.setVisible(false);
					exitBtn.setVisible(true);
					gamePlay.setVisible(true);
					loginFrame.setVisible(false);
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
				if(gamePlay.getDownChecker())  
				{
					System.out.println(gamePlay.getCrush() + "개 부숴짐");
					outputString = gamePlay.readBlock();
					
					try {
						dataOutputStream.writeUTF(outputString);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("발신중 문제발생");
						e.printStackTrace();
					}
					gamePlay.setCrush(0);
					gamePlay.setDownChecker(false);
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
		
		public ReceiveThread(Socket in)
		{
			socket = in;
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
			     System.out.println("서버와 접속이 되었습니다.");
			         
			   
			     
			     sender = new SendThread(socket);
			     sender.start();
			     receiver = new ReceiveThread(socket);
			     receiver.start();
			     
			  
			}
			catch(IOException e)
			{
				
			}
		}
		
		public Socket getSocket()
		{
			return socket;
		}
		
		
	}
	
	public Communication getCommunication()
	{
		return communication;
	}

}


