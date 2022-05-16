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
	//statement : �ٸ� ������ statement�� ��Ȱ�� �Ͽ� ���� ����
	//prepareStatement : sql�� �̸� �غ��� ���� statement���� ������ ������ . (������ ���� ���� ������ �� ����) , ���ڰ� ���� ���ɴ�
	
	public DBConnection()
	{
	}
	
	public int getWin(String id)
	{
		ResultSet result;
		int win=0;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("����̹� �˻� ����");
			try {
				con = DriverManager.getConnection(url,adminId,adminPass);
				System.out.println("����̹� ���� ����"+con);
				sql = "select win from userinfo where id = \'"+id +"\'";
	
				statement = con.prepareStatement(sql);

				result = statement.executeQuery();
				
				while(result.next()) {
					win = result.getInt("win");
				}
				
				

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("����̹� ���� ����");
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("����̹� �˻� ����");
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
			System.out.println("����̹� �˻� ����");
			try {
				con = DriverManager.getConnection(url,adminId,adminPass);
				System.out.println("����̹� ���� ����"+con);
				sql = "select lose from userinfo where id = \'"+id + "\'";
	
				statement = con.prepareStatement(sql);

				result = statement.executeQuery();
				
				while(result.next()) {
					lose = result.getInt("lose");
				}
				
				

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("����̹� ���� ����");
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("����̹� �˻� ����");
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
			System.out.println("����̹� �˻� ����");
			try {
				con = DriverManager.getConnection(url,adminId,adminPass);
				System.out.println("����̹� ���� ����"+con);
				sql = "select * from userinfo where id = \'"+id+ "\' and password = " + password;
	
				statement = con.prepareStatement(sql);

				result = statement.executeQuery();
				
				while(result.next()) {
					counter ++;
				}
				
				return true ? counter>0 : false;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("����̹� ���� ����");
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("����̹� �˻� ����");
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
	
	public int updateRecord(String id,int type) // type 1 : �¸� , type 2: �й� 
	{
		int result = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("����̹� �˻� ����");
			try {
				con = DriverManager.getConnection(url,adminId,adminPass);
				System.out.println("����̹� ���� ����"+con);
				
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
				System.out.println("����̹� ���� ����");
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("����̹� �˻� ����");
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
			System.out.println("����̹� �˻� ����");
			try {
				con = DriverManager.getConnection(url,adminId,adminPass);
				System.out.println("����̹� ���� ����"+con);
				sql = "INSERT into userinfo VALUES(?,?,?,?)";
				statement = con.prepareStatement(sql);
				statement.setString(1, id);
				statement.setString(2, password);
				statement.setInt(3, 0);
				statement.setInt(4, 0);

				result = statement.executeUpdate(); 
				
				System.out.println(result + " ���� �� ���� ��");

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("����̹� ���� ����");
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("����̹� �˻� ����");
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
