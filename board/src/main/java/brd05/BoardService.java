package brd05;

import java.util.List;

import brd05.ArticleVO;
import brd05.BoardDAO;

public class BoardService {
	BoardDAO boardDAO;
	
	public BoardService() {
		boardDAO = new BoardDAO();
	}
	
	public List<ArticleVO> listArticles(){  //반환형이 리스트제네릭ArticleVO형임
		List<ArticleVO> articlesList = boardDAO.selectAllArticles();
//      여기에 저장                 여기서 받은 값을
		return articlesList;  //다시 여기로 리턴
	}
		
	public int addArticle(ArticleVO article) { //Service가 없으면 select, delete, insert into 모두 기재해야함
		return boardDAO.insertNewArticle(article); 
		                     //9
	}
	
	public ArticleVO viewArticle(int articleNO) {
		ArticleVO article = null;
		article = boardDAO.selectArticle(articleNO);
		return article;
	}
	
	public void modArticle(ArticleVO article) {
		boardDAO.updateArticle(article);
	}
	
	public List<Integer> removeArticle(int articleNO){
		List<Integer> articleNOList = boardDAO.selectRemovedArticles(articleNO);
		boardDAO.deleteArticle(articleNO);
		return articleNOList;
	}
}
