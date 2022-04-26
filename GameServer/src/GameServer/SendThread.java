package GameServer;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SendThread extends Thread {
	
	
	private OutputStream outputStream;
	private DataOutputStream dataOutputStream;
	private String sendString;
	private Socket socket;
	public SendThread()
	{
		
	}
	
	public SendThread(Socket in)
	{
		socket = in;
	}
	
	public void run()
	{
		try
		{
			outputStream = socket.getOutputStream();
			
			dataOutputStream = new DataOutputStream(outputStream);
			
			sendString = "하이 클라이언트";
			dataOutputStream.writeUTF(sendString);
		}catch(Exception e) {}
		
		while(true)
		{
			if(!socket.isConnected())
			{
				break;
			}
	
		}
	}
}

