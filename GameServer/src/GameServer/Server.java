package GameServer;

import java.net.ServerSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public class Server {

	private int portNumber = 8877;
	private ServerSocket serverSocket;
	private Socket socket[];
	private ReceiveThread receiver[];
	private SendThread sender[];
	private int isGaming = 0; // 게임 진행중인지 여부 확인 
	private int readyPlayers = 0;
	private DBConnection db;
	private String userId[];
	private int playerLimit=2;
	private int win[];
	private int lose[];
	public Server()
	{
		db = new DBConnection();
		socket = new Socket[playerLimit];
		sender = new SendThread[playerLimit];
		receiver = new ReceiveThread[playerLimit];
		userId = new String[playerLimit];
		win = new int[playerLimit];
		lose = new int[playerLimit];
		win[0]=win[1]=0;
		lose[0]=lose[1]=0;
		try {
			
			serverSocket = new ServerSocket(portNumber); // 포트 넘버 설정 
			serverSocket.setReuseAddress(true);//주소 재사용 
			System.out.println("포트넘버 설정 완료");
			
			socket[0] = serverSocket.accept();
			System.out.println("서버 접속1 시도됨");
			receiver[0] = new ReceiveThread(socket[0],0);
			sender[0] = new SendThread(socket[0],0);

			receiver[0].start();
			sender[0].start();
			
			socket[1] = serverSocket.accept();
			System.out.println("서버 접속2 시도됨");
			receiver[1] = new ReceiveThread(socket[1],1);
			sender[1] = new SendThread(socket[1],1);

			receiver[1].start();
			sender[1].start();
			
			
			
			
		}catch(IOException e)
		{
			System.out.println("서버 오류");
		}
		
		
	}
	

public class ReceiveThread extends Thread {
	private Socket socket;
	private InputStream inputStream;
	private String receiveString;
	private DataInputStream dataInputStream;
	private int receiveClientNum;
	private String[] tokens;
	

	public ReceiveThread()
	{
		
	}
	
	public ReceiveThread(Socket in,int clientNumber)
	{
		socket = in;
		receiveClientNum = clientNumber;
	}
	
	public void run()
	{
		while(true)
		{
		
			
			
			if(!socket.isConnected())
			{
				break;
			}
			try {
				inputStream = socket.getInputStream();
				dataInputStream = new DataInputStream(inputStream);
				receiveString = dataInputStream.readUTF();
			}catch(EOFException e)
			{
				System.out.println("EOF");
			}catch(SocketException e) // 클라이언트 강제 종료시 
			{
				break;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("IOEX");
				e.printStackTrace();
			}
			
			System.out.println(receiveString);
			tokens = receiveString.split(" ");
			
			
			if (tokens[0].equals("login")) // 로그인 시도시 
			{
				if(db.loginCheck(tokens[1],tokens[2]))//로그인 성공시 
				{
					if(receiveClientNum == 0)
					{
						win[0] = db.getWin(tokens[1]);
						lose[0] = db.getLose(tokens[1]);
						sender[0].sendMessage("loginSuccess " + win[0]+" " +lose[0]);
						userId[0]=tokens[1];
					}
					else 
					{
						win[1] = db.getWin(tokens[1]);
						lose[1] = db.getLose(tokens[1]);
						sender[1].sendMessage("loginSuccess " + win[1]+" " +lose[1]);
						userId[1]=tokens[1];
					}
				}
				else // 로그인 실패시 
				{
					if(receiveClientNum == 0) 
					{
						sender[0].sendMessage("loginFailed");
					}
					else 
					{
						sender[1].sendMessage("loginFailed");
					}
				}
			}
			
			else if(tokens[0].equals("sign")) // 회원가입 시도시 
			{
				if(db.signNewUser(tokens[1], tokens[2]) == 1) // 회원가입 성공시 
				{
					if(receiveClientNum == 0)
					{
						sender[0].sendMessage("SignSuccess");
						userId[0]=tokens[1];
						win[0] = lose[0] =0;
					}
					else 
					{
						sender[1].sendMessage("SignSuccess");
						userId[1] = tokens[1];
						win[1] = lose[1] = 0;
					}
				}
				else // 회원가입 실패시 
				{
					if(receiveClientNum == 0)
					{
						sender[0].sendMessage("SignFailed");
					}
					else 
					{
						sender[1].sendMessage("SignFailed");
					}
				}
			}
			
			if(receiveString.equals("s")) // 시작 메세지 받을시에 
			{
				readyPlayers++;
				if(readyPlayers == 2)
				{
					readyPlayers=0;
					try {
						Thread.sleep(2000);
					}catch(Exception e)
					{}
					
					sender[1].sendMessage("s");
					sender[0].sendMessage("s");	
				}
			}
			
			else if(receiveString.equals("g") || receiveString.equals("exit")) // 게임 오버 메세지 받을 시에 
			{
				if(receiveClientNum == 0) // 1번이 패배시 
				{
					sender[1].sendMessage(receiveString); 
					db.updateRecord(userId[1], 1); // win
					db.updateRecord(userId[0], 2); // lose 
					win[1] ++;
					lose[0] ++;
				}
				else // 2번이 패배시 
				{
					sender[0].sendMessage(receiveString);	
					db.updateRecord(userId[0], 1); // win
					db.updateRecord(userId[1], 2); // lose 
					win[0] ++;
					lose[1]++;
					
				}
			}
			
			else if(isGaming==2) // 게임중에 
			{

					if(receiveClientNum == 0)
					{
						sender[1].sendMessage(receiveString);
					}
					else
					{
						sender[0].sendMessage(receiveString);		
					}
			}
			
			
			
			
		}
	}
	
	public void finalize() throws Throwable
	{
		socket.close();
		inputStream.close();
		dataInputStream.close();
	}
}


public class SendThread extends Thread {
	
	
	private OutputStream outputStream;
	private DataOutputStream dataOutputStream;
	private String sendString;
	private Socket socket;
	private int sendClientNum;
	
	private boolean sendOn = false;
	
	public SendThread()
	{
		
		
	}
	
	public SendThread(Socket in,int clientNumber)
	{
		socket = in;
		try {
			outputStream = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dataOutputStream = new DataOutputStream(outputStream);
		
		sendString ="";
		isGaming++;
		sendOn= true;
		sendClientNum = clientNumber;
	}
	
	public void run()
	{	
		while(true)
		{
			if(sendOn)
			{
				try
				{
					dataOutputStream.writeUTF(sendString);
					System.out.println("데이터 전송 완료 : " + sendString);
				}catch(Exception e) {
					System.out.println("데이터 전송 실패 : " + sendString);
				}
				finally
				{
					sendOn =false;
				}
			}
			
			
			try {
				Thread.sleep(0);
			}catch(Exception e) {}
		}
	}
	
	public void finalize() throws Throwable
	{
		socket.close();
		dataOutputStream.close();
		outputStream.close();
	}
	
	public void sendMessage(String message)
	{
		sendString = message;
		sendOn=  true;
	}
}


	
}




