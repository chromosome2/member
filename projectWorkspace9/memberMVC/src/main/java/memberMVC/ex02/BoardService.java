package memberMVC.ex02;

import java.util.List;

public class BoardService {
	BoardDAO boardDAO;
	public BoardService() {
		boardDAO=new BoardDAO();
	}
	
	public List<ArticleVO> listArticles() {
		List<ArticleVO> articleList=boardDAO.selectAllArticles();
		return articleList;
	}
	
	public int addArticle(ArticleVO article) {
		return boardDAO.insertNewArticle(article);
	}
	
	public ArticleVO viewArticle(int articleNo) {
		ArticleVO article=null;
		article=boardDAO.selectArticle(articleNo);
		return article;
	}
}
//보드 서비스가 사실 없어도 됨. 근데 이게 있으면 눈에 확들어오고 복잡하걸 한눈에 알아볼 수 있음. 복잡할 수록 분리하는게 편함.