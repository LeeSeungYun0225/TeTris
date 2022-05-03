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
	private int isGaming = 0; // ���� ���������� ���� Ȯ�� 
	private int readyPlayers = 0;
	public Server()
	{
		socket = new Socket[2];
		sender = new SendThread[2];
		receiver = new ReceiveThread[2];
		try {
			
			serverSocket = new ServerSocket(portNumber); // ��Ʈ �ѹ� ���� 
			serverSocket.setReuseAddress(true);//�ּ� ���� 
			System.out.println("��Ʈ�ѹ� ���� �Ϸ�");
			
			socket[0] = serverSocket.accept();
			System.out.println("���� ����1 �õ���");
			receiver[0] = new ReceiveThread(socket[0],0);
			sender[0] = new SendThread(socket[0],0);

			receiver[0].start();
			sender[0].start();
			
			socket[1] = serverSocket.accept();
			System.out.println("���� ����2 �õ���");
			receiver[1] = new ReceiveThread(socket[1],1);
			sender[1] = new SendThread(socket[1],1);

			receiver[1].start();
			sender[1].start();
			
			
			
			
		}catch(IOException e)
		{
			System.out.println("���� ����");
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
			}catch(SocketException e) // Ŭ���̾�Ʈ ���� ����� 
			{
				break;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("IOEX");
				e.printStackTrace();
			}
			System.out.println(receiveString);
			if(receiveString.equals("g"))// ���ӿ��� �޼��� ���� �ÿ� 
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
			else if(receiveString.equals("s")) // ���� �޼��� �����ÿ� 
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
			else if(isGaming==2) // ��� �޼��� ���� �ÿ� 
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
		
		sendString = "���� Ŭ���̾�Ʈ";
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




