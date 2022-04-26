package GameServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ReceiveThread extends Thread {
	private Socket socket;
	private InputStream inputStream;
	private String receiveString;
	private DataInputStream dataInputStream;

	public ReceiveThread()
	{
		
	}
	
	public ReceiveThread(Socket in)
	{
		socket = in;
	}
	
	public void run()
	{
		try {
			inputStream = socket.getInputStream();
			dataInputStream = new DataInputStream(inputStream);
			
			
		}catch(Exception e)
		{
			System.out.println("SomeTingWrong");	
		}
		
		while(true)
		{
			try {
	
				receiveString = dataInputStream.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("SomeTingWrong");
				e.printStackTrace();
			}
			System.out.println(receiveString);
			
			
		}
	}
}
