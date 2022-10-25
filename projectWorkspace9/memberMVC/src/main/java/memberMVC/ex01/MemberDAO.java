package memberMVC.ex01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {
	private DataSource dataFactory;
	private Connection conn;
	private PreparedStatement pstmt;
	
	public MemberDAO() {
		try {
			Context ctx=new InitialContext();
			Context envContext=(Context)ctx.lookup("java:/comp/env");//������ ���̽� ȯ������ ����
			dataFactory=(DataSource)envContext.lookup("jdbc/oracle");//oracle���� mySQL �����ص� �ǰ� ���... 
		}catch(Exception e) {
			System.out.println("DB ���� ����");
		}
	}
	
	//ȸ�� ���
	public List<MemberVO> listMembers() {
		List<MemberVO> membersList=new ArrayList();
		try {
			conn=dataFactory.getConnection();
			String query="select * from membertbl order by joinDate desc";
			System.out.println(query);
			pstmt=conn.prepareStatement(query);
			ResultSet rs=pstmt.executeQuery(); //sql�𺧷ӿ��� �ʷϻ� ȭ��ǥ ������ �۾�
			while(rs.next()) {
				String id=rs.getString("id");
				String pwd=rs.getString("pwd");
				String name=rs.getString("name");
				String email=rs.getString("email");
				Date joinDate=rs.getDate("joinDate");
				//���� Ű ���� �Ҽ����� �ϰ� ������ ������ double�� ��.rs.getDouble��..
				MemberVO memberVO=new MemberVO(id, pwd, name, email, joinDate);
				membersList.add(memberVO);
			}
			rs.close();
			pstmt.close();
			conn.close();
		}catch(Exception e) {
			System.out.println("DB ��ȸ �� ����");
		}
		return membersList;
	}
	
	//ȸ�� ���
	public void addMember(MemberVO mem) {//ȸ������ html���� submit���� �� ����� ������ �޴� MemberVO mem.
		try {
			conn=dataFactory.getConnection();
			String id=mem.getId();
			String pwd=mem.getPwd();
			String name=mem.getName();
			String email=mem.getEmail();
			String query="insert into membertbl (id, pwd, name, email) values(?,?,?,?)";
			System.out.println(query);
			pstmt=conn.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			pstmt.setString(3, name);
			pstmt.setString(4, email);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		}catch (Exception e) {
			System.out.println("DB ��� �� ����");
		}
	}
	
	//������ ȸ������ ã��
	public MemberVO findMember(String _id) {
		MemberVO memFindInfo=null;
		try {
			conn = dataFactory.getConnection();
			String query="select * from membertbl where id=?";
			pstmt=conn.prepareStatement(query);
			pstmt.setString(1, _id);
			System.out.println(query);
			ResultSet rs=pstmt.executeQuery();//������ ����
			rs.next();
			String id=rs.getString("id");
			String pwd=rs.getString("pwd");
			String name=rs.getString("name");
			String email=rs.getString("email");
			Date joinDate=rs.getDate("joinDate");
			memFindInfo=new MemberVO(id,pwd, name, email, joinDate);
			pstmt.close();
			conn.close();
			rs.close();
		}catch(Exception e) {
			System.out.println("������ �ڷ� ã�� �� ������.");
		}
		return memFindInfo;
	}
	
	//ȸ�� ���� ����
	public void modMember(MemberVO memberVO) {
		String id=memberVO.getId();
		String pwd=memberVO.getPwd();
		String name=memberVO.getName();
		String email=memberVO.getEmail();
		try {
			conn=dataFactory.getConnection();
			String query="update membertbl set pwd=?, name=?, email=? where id=?";
			System.out.println(query);
			pstmt=conn.prepareStatement(query);
			pstmt.setString(1, pwd);
			pstmt.setString(2, name);
			pstmt.setString(3, email);
			pstmt.setString(4, id);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		}catch(Exception e) {
			System.out.println("ȸ������ ���� �� ����");
		}
	}
	
	//ȸ�� ����
	public void delMember(String id) {
		try {
			conn=dataFactory.getConnection();
			String query="delete from membertbl where id=?";
			System.out.println(query);
			pstmt=conn.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.executeUpdate();//sql����
			pstmt.close();
			conn.close();
		}catch(Exception e) {
			System.out.println("ȸ������ ���� �� ����");
		}
	}
}
