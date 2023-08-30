package brd06;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	private DataSource dataFactory;
	Connection conn;
	PreparedStatement pstmt;
	
	public BoardDAO() {
		try {  //db�뿰�룞
			Context ctx = new InitialContext();
			Context envContext = (Context)ctx.lookup("java:/comp/env");
			dataFactory = (DataSource)envContext.lookup("jdbc/servletex");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List selectAllArticles() { //반환형은 리스트형
		List articlesList = new ArrayList();
		try {
			conn= dataFactory.getConnection();
			String query =  /*"select * from t_board";*/
					 "select case when level -1 > 0 then concat(concat(repeat(' ', level -1), '<'), board.title ) else board.title end as title, board.articleNO, board.parentNO, result.level, board.content, board.id, board.writeDate "
					//레벨이 0보다 크면 (2이상일땐 자식) 앞과 뒤를 concat으로 연결함   repeat은 반복문자열                                                                                   레벨은 생성한 것이기때문에 result에 넣음
					 + " from" +
					 " (select function_hierarchical() as articleNO, @level as level " +
					 //                                            @의 사용영역은 전역임
					 " from (select @start_with:=0, @articleNO:=@start_with, @level:=0) tbl join t_board) result "
					 + " join t_board board on board.articleNO = result.articleNO";
					 
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int level = rs.getInt("level");
				int articleNO = rs.getInt("articleNO");
				int parentNO = rs.getInt("parentNO");
				String title = rs.getString("title");
				String content = rs.getString("content");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				ArticleVO article = new ArticleVO();
				article.setLevel(level);
				article.setArticleNO(articleNO);
				article.setParentNO(parentNO);
				article.setTitle(title);
				article.setContent(content);
				article.setId(id);
				article.setWriteDate(writeDate);
				articlesList.add(article); //인덱스로 추가
			}
			rs.close();
			pstmt.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return articlesList;
	}
	
	private int getNewArticleNO() {
		try {
			conn= dataFactory.getConnection(); //커넥션정보가져옴(컨텍스트에 있는 서버 리소스 정보를 가져옴)
			String query = "select max(articleNO) from t_board ";
			//t_board의 articleNO 최대값을 가져옴
			System.out.println(query);
			pstmt = conn.prepareStatement(query); //sql紐낅졊�뼱瑜� 援ы쁽�떆�궎�뒗 媛앹껜瑜� �깮�꽦�빐�꽌 �꽔�뼱以�
			ResultSet rs = pstmt.executeQuery(query); //prepareSttement�뿉�꽌 吏��썝�빐二쇰뒗 executeQuery瑜� �떎�뻾�떆�궡
//		    커서시작위치         실직적 처리 실행
			if(rs.next())
				return(rs.getInt(1)+1); 
			 //첫번째열의 articleNO가 8이면+1해서 9가됨 9를 리턴(db는 0인덱스가 없음)
            //articleNO가 int형임
			rs.close();
			pstmt.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int insertNewArticle(ArticleVO article) { //db에 연결한 후 crud 요들단위로 처리
		int articleNO = getNewArticleNO(); //9를 받음
		try {
			conn= dataFactory.getConnection();
			int parentNO = article.getParentNO();
			                          //0
			String title = article.getTitle();
			String content = article.getContent();
			String id = article.getId();
			                    //lee
			String imageFileName = article.getImageFileName();
			String query = "insert into t_board (articleNO, parentNO, title, content, imageFileName, id) "+"  values(?,?,?,?,?,?)";
			                                   //   9                                                                  받은값을 ?를 이용해서 변수조정
			System.out.println(query);
			pstmt=conn.prepareStatement(query);//Statement라는 중간매개체가 있는것임 여기에 넣었다가 다시 db에 넣어줌
			pstmt.setInt(1, articleNO);
			pstmt.setInt(2, parentNO);
			pstmt.setString(3, title);
			pstmt.setString(4, content);
			pstmt.setString(5, imageFileName);
			pstmt.setString(6, id);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return articleNO; //9瑜� 由ы꽩
	}
	
	public ArticleVO selectArticle(int articleNO) {
		ArticleVO article = new ArticleVO();
		try {
			conn= dataFactory.getConnection();
			String query = "select articleNO,parentNO,title,content, ifnull(imageFileName, 'null') as imageFileName, id, writeDate"
							+ " from t_board"
							+ " where articleNO=?";		                                                                                                
			System.out.println(query);
			pstmt=conn.prepareStatement(query); //Statement라는 중간매개체가 있는것임 여기에 넣었다가 다시 db에 넣어줌
			pstmt.setInt(1, articleNO);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			int _articleNO = rs.getInt("articleNO");
			int parentNO = rs.getInt("parentNO");
			String title = rs.getString("title");
			String content = rs.getString("content");
			String imageFileName = URLEncoder.encode(rs.getString("imageFileName"), "UTF-8");
			if(imageFileName.equals("null")) {
				imageFileName = null;
			}
			String id = rs.getString("id");
			Date writeDate = rs.getDate("writeDate");
			
			article.setArticleNO(_articleNO);
			article.setParentNO(parentNO);
			article.setTitle(title);
			article.setContent(content);
			article.setImageFileName(imageFileName);
			article.setId(id);
			article.setWriteDate(writeDate);
			rs.close();
			pstmt.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return article;
	}
	
	public void updateArticle(ArticleVO article) {
		int articleNO = article.getArticleNO();
		String title = article.getTitle();
		String content = article.getTitle();
		String imageFileName = article.getImageFileName();
		try {
			conn= dataFactory.getConnection();
			String query = "update t_board set title=?,content=?";
			if(imageFileName != null && imageFileName.length() !=0) {
				query += ",imageFileName=?";
			}
			query += " where articleNo=?";
			
			System.out.println(query);
			pstmt=conn.prepareStatement(query); //Statement라는 중간매개체가 있는것임 여기에 넣었다가 다시 db에 넣어줌
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			if(imageFileName != null && imageFileName.length() !=0) {
				pstmt.setString(3, imageFileName);
				pstmt.setInt(4, articleNO);
			}else {
				pstmt.setInt(3, articleNO);
			}
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteArticle(int articleNO) {
		try {
			conn= dataFactory.getConnection();
			String query = "delete from t_board where parentNO=? or articleNO=?";		                                                                                                
			System.out.println(query);
			pstmt=conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			pstmt.setInt(2, articleNO);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Integer> selectRemovedArticles(int articleNO){
		List<Integer> articleNOList = new ArrayList<Integer>();
		try {
			conn= dataFactory.getConnection();
			String query ="select articleNO from t_board where articleNO = (select articleNO from t_board where parentNO = ?)";
			System.out.println(query);
			pstmt=conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				articleNO = rs.getInt("articleNO");
				articleNOList.add(articleNO);
			}
			pstmt.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return articleNOList;
	}
}
