package controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import dto.Board;
import dto.Page;
import service.BoardService;
import service.BoardServiceImpl;



@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService bs = new BoardServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		System.out.println(uri);
		//context path
		String path = request.getContextPath();
		if(uri.contains("list")) {
			String findkey= request.getParameter("findkey");
			String findvalue = request.getParameter("findvalue");
			String curpage_s = request.getParameter("curpage");
			int curpage=1; //디폴트 페이지
			if (curpage_s != null) //현재 페이지를 넘겼다면
				curpage =Integer.parseInt(curpage_s);
			
			Page page = new Page();
			page.setFindkey(findkey);
			page.setFindvalue(findvalue);
			page.setCurpage(curpage);
			System.out.println("controller:" + page);
			
			List<Board> blist = bs.selectList(page);
			System.out.println(blist);
			
			//forward방식, list.jsp
			request.setAttribute("blist", blist);
			request.setAttribute("page", page);
			request.getRequestDispatcher("/views/Board/list.jsp")
					.forward(request, response);
		}else if (uri.contains("add")) {		
			//등록
			String saveDirectory = getServletContext().getInitParameter("savedir") ;
			int size = 1024 * 1024 * 10; //10m
			//객체를 생성과 동시에 파일을 saveDirectory에 저장
			MultipartRequest multi = new MultipartRequest(request,saveDirectory , size,"utf-8", new DefaultFileRenamePolicy());
			
			//db에 insert할 정보들
			String email = multi.getParameter("email");
			String subject = multi.getParameter("subject");
			String content = multi.getParameter("content");
			
			//객체생성
			Board board = new Board();
			board.setEmail(email);
			board.setSubject(subject);
			board.setContent(content);
			board.setIp(request.getRemoteAddr()); //요청ip주소
			System.out.println(board);
			
			//신규 파일이름 처리
			List<String> filenames = new ArrayList<>();
			//파일의 이름의 모음
			Enumeration<String> files =  multi.getFileNames();
			while(files.hasMoreElements()) { //다음자료가 있으면 true
				String name = files.nextElement();
				System.out.println(name);
				//실제로 저장된 파일이름
				String filename = multi.getFilesystemName(name);
				if (filename != null) filenames.add(filename);
			}
			
			System.out.println(filenames);

			String msg = bs.insert(board,filenames);
			
			//redirect방식, /board/list 호출
			response.sendRedirect(path+"/board/list?msg=" + URLEncoder.encode(msg, "utf-8"));
		}else if (uri.contains("detail")) {
			//상세조회
			int bnum = Integer.parseInt(request.getParameter("bnum"));
			//조회수 +1
			String cntplus =  request.getParameter("cntplus");
			if (cntplus!=null) {
				bs.cntplus(bnum);
			}
			//게시물과 파일 조회
			Map<String, Object> map =bs.selectOne(bnum);

			
			//forward방식, detail.jsp이동
			request.setAttribute("map", map);
			request.getRequestDispatcher("/views/Board/detail.jsp").forward(request, response);
		}else if (uri.contains("remove")) {
			//삭제
			int bnum = Integer.parseInt(request.getParameter("bnum"));
			String msg = bs.delete(bnum);
			System.out.println(msg);
			//리스트로 이동
			response.sendRedirect(path+"/board/list?msg=" + URLEncoder.encode(msg, "utf-8"));
		}else if (uri.contains("modiform")) {
			//상세조회
			int bnum = Integer.parseInt(request.getParameter("bnum"));
			//게시물과 파일 조회
			Map<String, Object> map =bs.selectOne(bnum);
			//forward방식, modify.jsp이동
			request.setAttribute("map", map);
			request.getRequestDispatcher("/views/Board/modify.jsp").forward(request, response);
		}else if(uri.contains("modify")) {
			//수정
			String saveDirectory = getServletContext().getInitParameter("savedir") ;
			int size = 1024 * 1024 * 10; //10m
			//객체를 생성과 동시에 파일을 saveDirectory에 저장
			MultipartRequest multi = new MultipartRequest(request,saveDirectory , size,"utf-8", new DefaultFileRenamePolicy());
			
			//db에 update정보 읽기
			int bnum = Integer.parseInt(multi.getParameter("bnum"));
			String email = multi.getParameter("email");
			String subject = multi.getParameter("subject");
			String content = multi.getParameter("content");
			Board board = new Board();
			board.setBnum(bnum);
			board.setEmail(email);
			board.setSubject(subject);
			board.setContent(content);
			board.setIp( request.getRemoteAddr());
			System.out.println("수정할 board:" + board);
			
			String[] filedels =  multi.getParameterValues("filedel");
			System.out.println("기존파일 삭제리스트:" + Arrays.toString(filedels) );
			
			//신규 파일이름 처리
			List<String> filenames = new ArrayList<>();
			//파일의 이름의 모음
			Enumeration<String> files =  multi.getFileNames();
			while(files.hasMoreElements()) { //다음자료가 있으면 true
				String name = files.nextElement();
				//실제로 저장된 파일이름
				String filename = multi.getFilesystemName(name);
				if (filename != null) filenames.add(filename);
			}
			
			System.out.println("신규파일 추가 리스트:" + filenames);
			
			//서비스에 update요청
			String msg = bs.update(board,filedels,filenames);
			
			//redirect방식, /board/detail로 이동
			response.sendRedirect(
					path+"/board/detail?bnum="+bnum+"&msg="+ URLEncoder.encode(msg, "utf-8"));
			
		}
			
		
 	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
