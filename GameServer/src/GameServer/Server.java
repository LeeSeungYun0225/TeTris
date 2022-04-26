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
			serverSocket = new ServerSocket(portNumber); // 포트 넘버 설정 
			System.out.println("포트넘버 설정 완료");
			socket = serverSocket.accept(); // 클라이언트 접속시 실행 
			System.out.println("서버 접속 시도됨");

			receiver = new ReceiveThread(socket);
			sender = new SendThread(socket);
			receiver.start();
			sender.start();
			
		}catch(IOException e)
		{
			System.out.println("서버 오류");
		}
		
	}
	
	
}
