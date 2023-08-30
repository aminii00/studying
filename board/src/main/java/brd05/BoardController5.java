package brd05;

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
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;


@WebServlet("/board005/*")
public class BoardController5 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	   // 상수                     이미지 저장 폴더 경로
	BoardService boardService;
	ArticleVO articleVO;

	public void init(ServletConfig config) throws ServletException {//init에서 생성 후 선언해준 것에 값이 들어감
		boardService = new BoardService(); 
		articleVO = new ArticleVO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nextPage = "";  //" "스페이스바 치면 나중에 오류발생확률 높음
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String action = request.getPathInfo(); //2번째 매핑 / 앞쪽에서 보내준 액션을 받음
		System.out.println("action:"+action);
		try {
			List<ArticleVO> articlesList = new ArrayList<ArticleVO>(); //배열형태사용아니고 리스트형 사용하려고 하는것임
			if(action == null) { //1차 매핑 후 2차 매핑을 살피는데 1차 매핑이 없으면 클래스가 실행안됨
				articlesList = boardService.listArticles();
				request.setAttribute("articlesList", articlesList);
				nextPage = "/board05/listArticles.jsp";
			}else if(action.equals("/listArticles.do")) {
				articlesList = boardService.listArticles();
				request.setAttribute("articlesList", articlesList);
//		        속성저장          키                값
				nextPage = "/board05/listArticles.jsp";
				// 넥스트페이지는 디스패처로 넘겨줌(리스트 주소를 넣어서 포워드)

			}else if(action.equals("/articleForm.do")) { //얘는
				nextPage = "/board05/articleForm.jsp"; //얘를 열어줌

			}else if(action.equals("/addArticle.do")) {
				int articleNO = 0; //부모가 0이라서
				Map<String, String> articleMap = upload(request, response);
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");

				articleVO.setParentNO(0);
				articleVO.setId("aminii"); //내 db에 있는 것으로
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				articleNO = boardService.addArticle(articleVO); //저장소객체에 저장한 후 그 주소를 넣어서 호출
				//9 /바로 int로 가져오면 이값만 가져올 수 있음 때문에 Service에 넣어서 가져오는게 좋음
				if(imageFileName != null && imageFileName.length() !=0) {
					//temp
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
                                               //9
					destDir.mkdirs(); //articleNO는 이것을 꼭 사용해줘야함 파일생성
					FileUtils.moveFileToDirectory(srcFile, destDir, true);  //temp안에 있는 파일이 articleNO 파일로 넘어감
				}
				PrintWriter pw = response.getWriter();
				pw.print("<script>" + "alert('새글을 추가했습니다.');"
						+ " location.href='"
						+ request.getContextPath()
						+ "/board005/listArticles.do';" + "</script>");
				return;		
			
			}else if(action.equals("/viewArticle.do")){
				String articleNO = request.getParameter("articleNO");
				articleVO = boardService.viewArticle(Integer.parseInt(articleNO));
				request.setAttribute("article", articleVO);
				nextPage = "/board05/viewArticle.jsp";
			
			}else if(action.equals("/modArticle.do")) {
				Map<String, String> articleMap = upload(request, response);
				int articleNO = Integer.parseInt(articleMap.get("articleNO"));
				articleVO.setArticleNO(articleNO);
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				articleVO.setParentNO(0);
				articleVO.setId("aminii");  //내 db에 있는 것으로
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				boardService.modArticle(articleVO);
				if(imageFileName != null && imageFileName.length() !=0) { //기존이미지를 다른이미지로 수정
					//temp
					String originalFileName = articleMap.get("originalFileName");
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
                                               //9
					destDir.mkdirs(); //articleNO는 이것을 꼭 사용해줘야함 파일생성
					FileUtils.moveFileToDirectory(srcFile, destDir, true); //temp안에 있는 파일이 articleNO 파일로 넘어감
					File oldFile = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + originalFileName);
					oldFile.delete(); //기존이미지 삭제
				}
				PrintWriter pw = response.getWriter();
				pw.print("<script>" + "alert('글을 수정했습니다.');"
						+ " location.href='"
						+ request.getContextPath()
						+ "/board005/viewArticle.do?articleNO=" + articleNO + "';" + "</script>");
				return;
			
			}else if(action.equals("/removeArticle.do")){
				int articleNO = Integer.parseInt(request.getParameter("articleNO"));
				List<Integer> articleNOList = boardService.removeArticle(articleNO);
				for(int _articleNO : articleNOList) {
					File imgDir = new File(ARTICLE_IMAGE_REPO + "\\" + _articleNO);
					if(imgDir.exists()) {
						FileUtils.deleteDirectory(imgDir);
					}
				}
				PrintWriter pw = response.getWriter();
				pw.print("<script>" + "alert('글을 삭제했습니다.');"
						+ " location.href='"
						+ request.getContextPath()
						+ "/board005/listArticles.do';" + "</script>");
				return;
			
			}else {
				nextPage = "/board05/listArticles.jsp";
			}
			
			RequestDispatcher dispatch = request.getRequestDispatcher(nextPage); //페이지 이동(포워드)
			dispatch.forward(request, response);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 리퀘스트에 받아서 리퀘스트 객체를 준 것임
		Map<String, String> articleMap = new HashMap<String, String>();
		//Map의 스트링,스트링 제네릭형                                    
		String encoding = "utf-8";
		File currentDirPath = new File(ARTICLE_IMAGE_REPO);
		//파일객체는 파일형
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//setRepository 실질적 저장소 사용 setSizeThreshold 최대용량처리->두개 메소드 사용하려고 DiskFileItemFactory 사용
		factory.setRepository(currentDirPath);
		factory.setSizeThreshold(1024 * 1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List items = upload.parseRequest(request);
			//ServletFileUpload의 parseRequest메소드 호출
			//parseRequest가 잘라서 각각의 키의 값을 배열형태로 저장함. 때문에 리스트형
			for(int i=0; i<items.size(); i++) {
				FileItem fileItem = (FileItem) items.get(i); //0인덱스부터 for문 돔
				//items는 request객체 이므로 FileItem형으로 캐스트변환
				if(fileItem.isFormField()) { //텍스트인지 물어봄
					System.out.println(fileItem.getFieldName() + "=" + fileItem.getString(encoding));
					articleMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
					//Map에는 put으로 저장         title                  temp
				}else {
					System.out.println("파라미터이름:"+fileItem.getFieldName()); //imagaFileName
					System.out.println("파일이름:"+fileItem.getName()); //temp.jpg
					System.out.println("파일크기:"+fileItem.getSize()+"bytes"); //파일사이즈(40)
					if(fileItem.getSize() > 0) {
						int idx = fileItem.getName().lastIndexOf("\\");
						//C:\Users\Administrator\Desktop/temp.jpg 없으니까 내려옴
						if(idx == -1) {
							idx = fileItem.getName().lastIndexOf("/");
							//C:\Users\Administrator\Desktop/temp.jpg /앞에 자르고 temp.jpg만 남김
						}
						String fileName = fileItem.getName().substring(idx+1);
						articleMap.put(fileItem.getFieldName(), fileName);
			             //imageFileName, temp.jpg
						File uploadFile = new File((currentDirPath)+"\\temp\\"+fileName);
						//이미지저장경로 넣어서 파일객체 생성
						fileItem.write(uploadFile); //uploadFile 넣어서 써주기
					} //end if
				} //end if
			} //end for
		}catch(Exception e) {
			e.printStackTrace();
		}
		return articleMap; //upload호출한 곳으로 반환
	}
}
