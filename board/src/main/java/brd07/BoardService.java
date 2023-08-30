package brd07;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardService {
BoardDAO boardDAO;
	
	
	public BoardService() {
		boardDAO = new BoardDAO();
	}
	
	public Map listArticles(Map<String,Integer> pagingMap) {
		Map articlesMap = new HashMap();
		List<ArticleVO> articlesList = boardDAO.selectAllArticles(pagingMap);
		int toArticles = boardDAO.selectToArticles();
		articlesMap.put("articlesList", articlesList);
		articlesMap.put("toArticles", toArticles);
		System.out.println(articlesMap.get(articlesList));
		return articlesMap;
	
	}
	
	public List<ArticleVO> listArticles(){  //��ȯ���� ����Ʈ���׸�ArticleVO����
		List<ArticleVO> articlesList = boardDAO.selectAllArticles();
//      ���⿡ ����                 ���⼭ ���� ����
		return articlesList;  //�ٽ� ����� ����
	}
		
	public int addArticle(ArticleVO article) { //Service�� ������ select, delete, insert into ��� �����ؾ���
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
	public int addReply(ArticleVO article) {
		return boardDAO.insertNewArticle(article);
	}
}
