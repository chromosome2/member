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
			Context envContext=(Context)ctx.lookup("java:/comp/env");//데이터 베이스 환경조성 구성
			dataFactory=(DataSource)envContext.lookup("jdbc/oracle");//oracle말고 mySQL 연결해도 되고 등등... 
		}catch(Exception e) {
			System.out.println("DB 연결 오류");
		}
	}
	
	//회원 목록
	public List<MemberVO> listMembers() {
		List<MemberVO> membersList=new ArrayList();
		try {
			conn=dataFactory.getConnection();
			String query="select * from membertbl order by joinDate desc";
			System.out.println(query);
			pstmt=conn.prepareStatement(query);
			ResultSet rs=pstmt.executeQuery(); //sql디벨롭에서 초록색 화살표 누르는 작업
			while(rs.next()) {
				String id=rs.getString("id");
				String pwd=rs.getString("pwd");
				String name=rs.getString("name");
				String email=rs.getString("email");
				Date joinDate=rs.getDate("joinDate");
				//만약 키 같은 소수점을 하고 싶으면 보통은 double로 함.rs.getDouble로..
				MemberVO memberVO=new MemberVO(id, pwd, name, email, joinDate);
				membersList.add(memberVO);
			}
			rs.close();
			pstmt.close();
			conn.close();
		}catch(Exception e) {
			System.out.println("DB 조회 중 에러");
		}
		return membersList;
	}
	
	//회원 등록
	public void addMember(MemberVO mem) {//회원가입 html에서 submit했을 때 날라올 정보를 받는 MemberVO mem.
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
			System.out.println("DB 등록 중 에러");
		}
	}
	
	//수정할 회원정보 찾기
	public MemberVO findMember(String _id) {
		MemberVO memFindInfo=null;
		try {
			conn = dataFactory.getConnection();
			String query="select * from membertbl where id=?";
			pstmt=conn.prepareStatement(query);
			pstmt.setString(1, _id);
			System.out.println(query);
			ResultSet rs=pstmt.executeQuery();//쿼리문 실행
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
			System.out.println("수정할 자료 찾는 중 에러남.");
		}
		return memFindInfo;
	}
	
	//회원 정보 수정
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
			System.out.println("회원정보 수정 중 에러");
		}
	}
	
	//회원 삭제
	public void delMember(String id) {
		try {
			conn=dataFactory.getConnection();
			String query="delete from membertbl where id=?";
			System.out.println(query);
			pstmt=conn.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.executeUpdate();//sql실행
			pstmt.close();
			conn.close();
		}catch(Exception e) {
			System.out.println("회원정보 삭제 중 에러");
		}
	}
}
