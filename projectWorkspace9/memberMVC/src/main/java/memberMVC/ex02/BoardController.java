package memberMVC.ex02;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;


@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static String ART_IMAGE_REPO="C:\\board\\article_image";
	BoardService boardService;
	ArticleVO articleVO;
	
	public void init(ServletConfig config) throws ServletException {
		boardService=new BoardService();
		articleVO=new ArticleVO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nextPage="";
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String action=request.getPathInfo();//요청명을 가져온다.
		System.out.println("요청 이름 : "+action);
		try {
			List<ArticleVO> articleList=new ArrayList<ArticleVO>();
			if(action == null) {
				articleList=boardService.listArticles();
				request.setAttribute("articleList", articleList);
				nextPage="/boardinfo/listArticles.jsp";
			}else if(action.equals("/listArticles.do")){
				articleList=boardService.listArticles();
				request.setAttribute("articleList", articleList);//여기서 세팅해서 아래에서 forward하는거임.
				nextPage="/boardinfo/listArticles.jsp";
			}else if(action.equals("/articleForm.do")) {
				nextPage="/boardinfo/articleForm.jsp";
			}else if(action.equals("/addArticle.do")) {
				int articleNo=0;
				Map<String, String> articleMap=upload(request, response);
				String title=articleMap.get("title");
				String content=articleMap.get("content");
				String imageFileName=articleMap.get("imageFileName");
				articleVO.setParentNo(0);
				articleVO.setId("hong");
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				articleNo=boardService.addArticle(articleVO);
				//이미지를 첨부한 경우에만 수행
				if(imageFileName !=null && imageFileName.length() !=0) {
					//temp폴더에 임시로 업로드된 파일 객체를 생성
					File srcFile=new File(ART_IMAGE_REPO+"\\"+"temp\\"+imageFileName);
					//글 번호로 폴더를 생성
					File destDir=new File(ART_IMAGE_REPO+"\\"+articleNo);
					destDir.mkdir();//폴더 만들기
					//temp폴더의 파일을 글번호 이름으로 하는 폴더로 이동
					FileUtils.moveFileToDirectory(srcFile, destDir, true);//파일 옮기기.
					srcFile.delete();
				}
				PrintWriter pw=response.getWriter();
				pw.print("<script>"+"alert('새글을 추가했습니다.');"+"location.href='"+request.getContextPath()+"/board/listArticles.do';"+"</script>");
				return;
			}else if(action.equals("/viewArticle.do")) {
				String articleNo=request.getParameter("articleNo");
				System.out.println(articleNo);
				articleVO=boardService.viewArticle(Integer.parseInt(articleNo));
				//포워딩
				request.setAttribute("article", articleVO);//이 article이
				nextPage="/boardinfo/viewArticle.jsp";//여기로 넘어감
			}
			RequestDispatcher dispatcher=request.getRequestDispatcher(nextPage);//바인딩
			dispatcher.forward(request, response);//보여주기! //포워드
		}catch(Exception e) {
			System.out.println("요청 처리 중 에러"+ e.getMessage());
		}
	}//doHandle메서드 종료
	
	//이미지 파일 업로드
	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> articleMap=new HashMap<String, String>();
		String encoding="utf-8";//이미지파일 이름에 한글이 들어가면 깨져서 이렇게 해줘야함.
		File currentDirPath=new File(ART_IMAGE_REPO);
		DiskFileItemFactory factory=new DiskFileItemFactory();
		factory.setRepository(currentDirPath);
		factory.setSizeThreshold(1024*1024);
		ServletFileUpload upload=new ServletFileUpload(factory);
		try {
			List items=upload.parseRequest(request);//tomcat10에서는 안되는 작업
			for(int i=0; i<items.size(); i++) {
				FileItem fileItem=(FileItem)items.get(i);
				if(fileItem.isFormField()) {
					System.out.println(fileItem.getFieldName() + "=" + fileItem.getString(encoding));//encoding은 한글이 안깨지게
					//파일 업로드로 같이 전송된 새 글 관련(제목, 내용) 매개변수를 저장한 후 변환
					articleMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
				}else {
					System.out.println("파라미터이름 : "+fileItem.getFieldName());
					System.out.println("파일이름 : "+fileItem.getName());
					System.out.println("파일크기 : "+fileItem.getSize()+"bytes");
					if(fileItem.getSize() >0) {
						int idx=fileItem.getName().lastIndexOf("\\");//경로1
						if(idx== -1) {//-1이라는건 주소에서 "\\"이걸 못찾았다는거. 경로가 두가지가 있기때문에 다 고려해야함.
							idx=fileItem.getName().lastIndexOf("/");//경로2
						}
						String fileName=fileItem.getName().substring(idx+1);
						articleMap.put(fileItem.getFieldName(), fileName);
						//업로드한 이미지를 일단 temp에 저장
						File uploadFile=new File(currentDirPath+"\\temp\\"+fileName);//폴더에 이미지 넣기.
						fileItem.write(uploadFile);
					}
				}
			}
		}catch(Exception e) {
			System.out.println("파일 업로드 중 에러");
		}
		return articleMap;
	}

}
