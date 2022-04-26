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
	private JLabel tetrisMainScreen,loginLabel;// ���� ���� �̹��� 
	private Communication communication;
	private GamePlay gamePlay;
	private LoginFrame loginFrame;
	

	public StartGame() // ���� ���� ȭ�� ���� 
	{
		// ���� ��ư�� �ؽ�Ʈ�ʵ�,  ���̺��� �ʱ�ȭ�ϰ� �迭 , ������ //
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

		
		
		
		singleGameBtn.addActionListener(new ActionListener() { // �̱� ���� ���� ��ư 
			public void actionPerformed(ActionEvent e) 
			{
				singleGameBtn.setVisible(false);
				multiGameBtn.setVisible(false);
				exitBtn.setVisible(true);
				gamePlay.setVisible(true);
				loginFrame.setVisible(false);
			}
		});
		
		
		exitBtn.addActionListener(new ActionListener() { // �̱� ���� ���� ������ ��ư 
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
		
		multiGameBtn.addActionListener(new ActionListener() { // ��Ƽ ���� ���� ��ư 
			public void actionPerformed(ActionEvent e)
			{
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
					System.out.println(gamePlay.getCrush() + "�� �ν���");
					outputString = gamePlay.readBlock();
					
					try {
						dataOutputStream.writeUTF(outputString);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("�߽��� �����߻�");
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
				System.out.println("�������� ���۵� ���� : " + inputString );
			}
		}
	}
	
	public class Communication // ��Ʈ������ �ν��� ���� �����͸� �޾� ������ ����ϴ� ������ 
	{
		private Socket socket;
		private SendThread sender;
		private ReceiveThread receiver;
		
		public Communication()
		{
			
			try {
				 socket = new Socket("192.168.0.33" , 8877) ;
			     System.out.println("������ ������ �Ǿ����ϴ�.");
			         
			   
			     
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

