package GameServer;

import java.net.ServerSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public class Server {

	private int portNumber = 8877;
	private ServerSocket serverSocket;
	private Socket socket;
	private ReceiveThread receiver;
	private SendThread sender;
	public Server()
	{
		try {
			serverSocket = new ServerSocket(portNumber); // ��Ʈ �ѹ� ���� 
			System.out.println("��Ʈ�ѹ� ���� �Ϸ�");
			socket = serverSocket.accept(); // Ŭ���̾�Ʈ ���ӽ� ���� 
			System.out.println("���� ���� �õ���");

			receiver = new ReceiveThread(socket);
			sender = new SendThread(socket);
			receiver.start();
			sender.start();
			
		}catch(IOException e)
		{
			System.out.println("���� ����");
		}
		
	}
	
	
}
