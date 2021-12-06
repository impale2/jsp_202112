package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import dto.Member;
import service.MemberService;
import service.MemberServiceImpl;


@WebServlet("/member/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService mbs = new MemberServiceImpl();


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String uri = request.getRequestURI();
		System.out.println(uri);
		String path = request.getContextPath();
		if(uri.contains("add")) {
			//회원등록
//			String saveDirectory ="D:\\Dropbox\\savedir";
			
			String saveDirectory = getServletContext().getInitParameter("savedir");
			
			int size = 1024 * 1024 * 10;//10mb
			MultipartRequest multi = new MultipartRequest(request, saveDirectory, size,"utf-8",new DefaultFileRenamePolicy());
			String email = multi.getParameter("email");
			String passwd = multi.getParameter("passwd");
			String addr = multi.getParameter("addr");
			String addrdetail = multi.getParameter("addrdetail");	
			String zipcode = multi.getParameter("zipcode");
			String filename = multi.getFilesystemName("filename");
			if(filename==null) filename="";
			Member member = new Member();
			member.setAddr(addr);
			member.setAddrdetail(addrdetail);
			member.setEmail(email);
			member.setPasswd(passwd);
			member.setZipcode(zipcode);
			
			member.setFilename(filename);
			
			System.out.println(member);
			String msg = mbs.insert(member);
			System.out.println(email);
			
			response.sendRedirect(path+"/views/home.jsp?msg=" + URLEncoder.encode(msg,"utf-8"));
			
		}else if (uri.contains("login")) {
			//로그인
			String email = request.getParameter("email");
			String passwd = request.getParameter("passwd");
			System.out.println(email);
			System.out.println(passwd);
			Map<String,String> map=  mbs.login(email,passwd);
			String rcode = map.get("rcode");
			String msg = map.get("msg");
			//rcode가 0이면 home.jsp이동
			//rcode가 0이 아니면 login.jsp이동
			if(rcode.equals("0")) { //로그인성공
				//세션에 이메일 넣기
				HttpSession session = request.getSession();
				session.setAttribute("email", email);
				session.setMaxInactiveInterval(60*60*3);
				
				//쿠키에 이메일저장
				String idsave = request.getParameter("idsave");
				Cookie email_cookie = new Cookie("email",email);
				email_cookie.setPath(path);
				if(idsave==null) {
					email_cookie.setMaxAge(0);
				}
				response.addCookie(email_cookie);
				
				response.sendRedirect(path+"/views/home.jsp?msg=" + URLEncoder.encode(msg,"utf-8"));
			}else {
				response.sendRedirect(path+"/views/member/login.jsp?msg=" + URLEncoder.encode(msg,"utf-8"));
			}
		}else if (uri.contains("logout")) {
			//로그아웃
			HttpSession session = request.getSession();
			session.invalidate(); //모든 세션변수 삭제
			String msg = "로그아웃 되었습니다";
			response.sendRedirect(path+"/views/home.jsp?msg=" + URLEncoder.encode(msg,"utf-8"));
		}else if(uri.contains("myinfo")) {
			//내정보
			//이메일을 세션에서 가져오기
			HttpSession session = request.getSession();
			String email = (String)session.getAttribute("email");
			System.out.println(email);
			Member member = mbs.selectOne(email);
			System.out.println(member);
			//forward방식으로 myinfo.jsp이동
			request.setAttribute("member", member);
			request.getRequestDispatcher("/views/member/myinfo.jsp")
						.forward(request, response);
			
		}else if(uri.contains("modify")) {
			//수정
			//String saveDirectory = "D:\\Dropbox\\savedir";
			String saveDirectory = getServletContext().getInitParameter("savedir");
			int size = 1024 * 1024 * 10;//10m :업로드파일사이즈 제한
			//new DefaultFileRenamePolicy() : 같은이름의 파일이 있을때 파일이름 변경
			MultipartRequest multi = new MultipartRequest(request,saveDirectory, size,"utf-8", new DefaultFileRenamePolicy() );
			//MultipartRequest 객체를 이용해서 데이터 가져옴
			String email = multi.getParameter("email");
			String passwd = multi.getParameter("passwd");
			String changepw = multi.getParameter("changepw");
			String zipcode = multi.getParameter("zipcode");
			String addr = multi.getParameter("addr");
			String addrdetail = multi.getParameter("addrdetail");
			String filename = multi.getParameter("filename"); //파일이름
			String filedel = multi.getParameter("filedel");
			
			
			//실제저장된 파일이름가져오기
			String newfilename = multi.getFilesystemName("file"); 
			
			//파일을 변경하겠다라는 의미
			if (newfilename!=null) 
				filename = newfilename; //파일이 없을경우
			else if (filedel != null)
				filename = "";
			//객체 생성
			Member member = new Member();
			member.setEmail(email);
			member.setPasswd(passwd);
			member.setZipcode(zipcode);
			member.setAddr(addr);
			member.setAddrdetail(addrdetail);
			member.setFilename(filename);
			
			System.out.println(member);
			
			String msg = mbs.update(member, changepw);
			
			//내정보 서블릿 호출
			response.sendRedirect(path + "/views/member/myinfo.jsp?msg="+ URLEncoder.encode(msg, "utf-8"));
		}
//		}else if(uri.contains("filedown")) {
//			//파일 다운로드
//			//디렉토리, 파일이름
//			//String saveDirectory = "D:\\Dropbox\\savedir";
//			String saveDirectory = getServletContext().getInitParameter("savedir");
//			String filename = request.getParameter("filename");
//			
//			//마임타입:파일의 종류
//			String mimeType =getServletContext().getMimeType(filename);
//			if (mimeType == null) {
//				mimeType = "application/octet-stream;charset=utf-8";//모든 종류의 이진 데이터
//			}
//			response.setContentType(mimeType);
//			
//			//첨부파일로 파일을 보낼때
//			//한글파일이름 깨지지 않게 utf-8로 인코딩 
//			response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(filename,"utf-8"));
//			
//			
//			
//			//읽어올파일 경로명
//			String fileurl = saveDirectory +"/"+ filename;
//			System.out.println(fileurl);
//			
//			//입력스트림
//			FileInputStream fis =new FileInputStream(fileurl);
//			//출력스트림
//			ServletOutputStream outs =  response.getOutputStream();
//			
//			//한번에 읽어들일 바이트 배열
//			byte[] b = new byte[4096]; //4kbyte 크기의 byte배열
//			int numRead = 0; //읽어들인 바이트 수(-1이면 파일의 끝)
//			while((numRead = fis.read(b,0,b.length)) != -1) {
//				outs.write(b,0,numRead); //읽어들인 바이트수만큼 write
//			}
//			outs.flush(); //내보내기,버퍼를 비우기
//			outs.close();
//			fis.close();
//		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
