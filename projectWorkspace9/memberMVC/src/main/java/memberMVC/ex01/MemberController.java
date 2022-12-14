package memberMVC.ex01;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/member/*") //가입하기눌러도, 회원가입하기 눌러도 다 이 컨트롤러를 실행시키게.
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
		//이제 printWrite 안함. 서블릿은 클라이언트 요청만 받아서 모델을 호출하는 역할만 할거임.
		String action=request.getPathInfo();//요청한 매핑정보를 알 수 있음.
		System.out.println("요청매핑이름 : "+action);
		if(action == null || action.equals("/listMembers.do")) { //action이 널이거나 리스트멤버스.두 인지
			List<MemberVO> membersList=memberDAO.listMembers();//초기화작업에서 선언된 것. 
			request.setAttribute("membersList", membersList);//memberDAO에서 리턴 된 값을 넣기
			nextPage="/meminfo/listMembers.jsp";
		}else if(action.equals("/addMember.do")) {
			String id=request.getParameter("id");
			String pwd=request.getParameter("pwd");
			String name=request.getParameter("name");
			String email=request.getParameter("email");
			MemberVO memberVO=new MemberVO(id, pwd, name, email);
			memberDAO.addMember(memberVO);//회원등록 메서드 실행
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
			MemberVO memberVO=new MemberVO(id, pwd, name, email); //id는 where 조건에 넣을거라서 갖고온거고 가입날짜는 수정할 필요없으니까 안갖고옴.
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
		RequestDispatcher dispatcher=request.getRequestDispatcher(nextPage);//listMembers.jsp로 바인딩
		dispatcher.forward(request, response);
	}

}
