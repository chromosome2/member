package memberMVC.ex01;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/member/*") //�����ϱⴭ����, ȸ�������ϱ� ������ �� �� ��Ʈ�ѷ��� �����Ű��.
public class MemberController extends HttpServlet {
	MemberDAO memberDAO;
	
	@Override
	public void init() throws ServletException {
		memberDAO=new MemberDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nextPage=null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		//���� printWrite ����. �������� Ŭ���̾�Ʈ ��û�� �޾Ƽ� ���� ȣ���ϴ� ���Ҹ� �Ұ���.
		String action=request.getPathInfo();//��û�� ���������� �� �� ����.
		System.out.println("��û�����̸� : "+action);
		if(action == null || action.equals("/listMembers.do")) { //action�� ���̰ų� ����Ʈ�����.�� ����
			List<MemberVO> membersList=memberDAO.listMembers();//�ʱ�ȭ�۾����� ����� ��. 
			request.setAttribute("membersList", membersList);//memberDAO���� ���� �� ���� �ֱ�
			nextPage="/meminfo/listMembers.jsp";
		}else if(action.equals("/addMember.do")) {
			String id=request.getParameter("id");
			String pwd=request.getParameter("pwd");
			String name=request.getParameter("name");
			String email=request.getParameter("email");
			MemberVO memberVO=new MemberVO(id, pwd, name, email);
			memberDAO.addMember(memberVO);//ȸ����� �޼��� ����
			request.setAttribute("msg", "addMember");
			nextPage="/member/listMembers.do";
		}else if(action.equals("/memberForm.do")) {
			nextPage="/meminfo/memberForm.jsp";
		}else if(action.equals("/modMemberForm.do")){
			String id=request.getParameter("id");
			MemberVO memFindInfo=memberDAO.findMember(id);
			request.setAttribute("memFindInfo", memFindInfo);
			nextPage="/meminfo/modMemberForm.jsp";
		}else if (action.equals("/modMember.do")) {
			String id=request.getParameter("id");
			String pwd=request.getParameter("pwd");
			String name=request.getParameter("name");
			String email=request.getParameter("email");
			MemberVO memberVO=new MemberVO(id, pwd, name, email); //id�� where ���ǿ� �����Ŷ� �����°Ű� ���Գ�¥�� ������ �ʿ�����ϱ� �Ȱ�����.
			memberDAO.modMember(memberVO);
			request.setAttribute("msg", "modified");
			nextPage="/member/listMembers.do";
		}else if (action.equals("/delMember.do")) {
			String id=request.getParameter("id");
			memberDAO.delMember(id);
			request.setAttribute("msg", "deleted");
			nextPage="/member/listMember.do";
		}else {
			List<MemberVO> membersList=memberDAO.listMembers(); 
			request.setAttribute("membersList", membersList);
			nextPage="/meminfo/listMembers.jsp";
		}
		RequestDispatcher dispatcher=request.getRequestDispatcher(nextPage);//listMembers.jsp�� ���ε�
		dispatcher.forward(request, response);
	}

}