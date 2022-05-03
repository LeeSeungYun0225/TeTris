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
	public Server()
	{
		socket = new Socket[2];
		sender = new SendThread[2];
		receiver = new ReceiveThread[2];
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
			if(receiveString.equals("g"))// 게임오버 메세지 받을 시에 
			{
				if(receiveClientNum == 0)
				{
					sender[1].sendMessage("w");
				}
				else if(receiveClientNum == 1)
				{
					sender[0].sendMessage("w");		
				}
			}
			else if(receiveString.equals("s")) // 시작 메세지 받을시에 
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
			else if(isGaming==2) // 블록 메세지 받을 시에 
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
		
		sendString = "하이 클라이언트";
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
				}catch(Exception e) {}
				finally
				{
					sendOn =false;
				}
			}
			
			if(!socket.isConnected())
			{
				break;
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




