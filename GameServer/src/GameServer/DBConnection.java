package GameServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBConnection {
	private String url  = "jdbc:mysql://localhost/tetris";
	private String adminId = "root";
	private String adminPass = "!Ekdma0607";
	private Connection con = null;
	private PreparedStatement statement;
	private String sql;
	//statement : 다른 쿼리를 statement를 재활용 하여 실행 가능
	//prepareStatement : sql을 미리 준비해 놓아 statement보다 실행이 빠르다 . (동일한 많은 쿼리 실행할 때 유용) , 인자값 전달 가능능
	
	public DBConnection()
	{
	}
	
	public int getWin(String id)
	{
		ResultSet result;
		int win=0;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("드라이버 검색 성공");
			try {
				con = DriverManager.getConnection(url,adminId,adminPass);
				System.out.println("드라이버 연결 성공"+con);
				sql = "select win from userinfo where id = \'"+id +"\'";
	
				statement = con.prepareStatement(sql);

				result = statement.executeQuery();
				
				while(result.next()) {
					win = result.getInt("win");
				}
				
				

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("드라이버 연결 실패");
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("드라이버 검색 실패");
			e.printStackTrace();
		}
		
		try {
			con.close();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return win;
	}
	public int getLose(String id)
	{
		ResultSet result;
		int lose=0;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("드라이버 검색 성공");
			try {
				con = DriverManager.getConnection(url,adminId,adminPass);
				System.out.println("드라이버 연결 성공"+con);
				sql = "select lose from userinfo where id = \'"+id + "\'";
	
				statement = con.prepareStatement(sql);

				result = statement.executeQuery();
				
				while(result.next()) {
					lose = result.getInt("lose");
				}
				
				

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("드라이버 연결 실패");
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("드라이버 검색 실패");
			e.printStackTrace();
		}
		
		try {
			con.close();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lose;
	}
	public boolean loginCheck(String id,String password)
	{
		ResultSet result;
		int counter = 0;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("드라이버 검색 성공");
			try {
				con = DriverManager.getConnection(url,adminId,adminPass);
				System.out.println("드라이버 연결 성공"+con);
				sql = "select * from userinfo where id = \'"+id+ "\' and password = " + password;
	
				statement = con.prepareStatement(sql);

				result = statement.executeQuery();
				
				while(result.next()) {
					counter ++;
				}
				
				return true ? counter>0 : false;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("드라이버 연결 실패");
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("드라이버 검색 실패");
			e.printStackTrace();
		}
		
		try {
			con.close();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	public int updateRecord(String id,int type) // type 1 : 승리 , type 2: 패배 
	{
		int result = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("드라이버 검색 성공");
			try {
				con = DriverManager.getConnection(url,adminId,adminPass);
				System.out.println("드라이버 연결 성공"+con);
				
				switch(type)
				{
					case 1:
						sql = "update userinfo set win = win+1 where id = \'"+id + "\'";
						break;
					case 2:
						sql = "update userinfo set lose = lose+1 where id = \'"+id + "\'";
						break;
				}
				
				
				statement = con.prepareStatement(sql);

				result = statement.executeUpdate(); 
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("드라이버 연결 실패");
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("드라이버 검색 실패");
			e.printStackTrace();
		}
		
		try {
			con.close();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public int signNewUser(String id,String password)
	{
		int result = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("드라이버 검색 성공");
			try {
				con = DriverManager.getConnection(url,adminId,adminPass);
				System.out.println("드라이버 연결 성공"+con);
				sql = "INSERT into userinfo VALUES(?,?,?,?)";
				statement = con.prepareStatement(sql);
				statement.setString(1, id);
				statement.setString(2, password);
				statement.setInt(3, 0);
				statement.setInt(4, 0);

				result = statement.executeUpdate(); 
				
				System.out.println(result + " 개의 행 삽입 됨");

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("드라이버 연결 실패");
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("드라이버 검색 실패");
			e.printStackTrace();
		}
		
		try {
			con.close();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void finallize() {
		try {
			con.close();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
