package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.BoardDao;
import dao.BoardDaoImpl;
import dao.BoardFileDao;
import dao.BoardFileDaoImpl;
import dto.Board;
import dto.BoardFile;
import dto.Page;

public class BoardServiceImpl implements BoardService{

	private BoardDao bdao = new BoardDaoImpl();
	private BoardFileDao bfdao = new BoardFileDaoImpl();
	@Override
	public List<Board> selectList(Page page) {
		//페이징처리
		int curpage = page.getCurpage(); //현재페이지
		int perpage = page.getPerpage(); //한페이지당 게시물수
		int startnum = (curpage-1)*perpage + 1;//시작번호
		int endnum = startnum + perpage -1;
		
		page.setStartnum(startnum);
		page.setEndnum(endnum);
		//전체페이지수 구하기
		int totcnt = bdao.select_totcnt(page); //전체건수
		int totpage = totcnt/perpage; //전체페이지수
		if (totcnt%perpage>0) totpage ++; //나머지가 있으면 +1
		System.out.println("전체건수:" + totcnt);
		System.out.println("전체페이지수:" + totpage);
		page.setTotpage(totpage);
		
		
		//페이지블럭 구하기
		int perblock = page.getPerblock(); //페이지블럭의수
		int startpage = curpage - ((curpage-1)%perblock);
		int endpage = startpage +(perblock-1);
		//전체페이지가 페이지블럭의 마지막페이지보다 작다면
		//마지막페이지를 전체페이지로 설정
		if(totpage<endpage ) endpage = totpage;
		page.setStartpage(startpage);
		page.setEndpage(endpage);
		

		
		System.out.println(totpage);
		
		System.out.println(page);
		return bdao.selectList(page);
	}
	@Override
	public String insert(Board board, List<String> filenames) {
		int cnt = bdao.insert(board);
		System.out.println("board " + cnt +"건 추가");
		System.out.println("service:" + board);
		//파일이름 배열 처리
		for(String filename:filenames) {
			if (filename==null) continue;
			BoardFile boardFile = new BoardFile();
			boardFile.setBnum(board.getBnum());
			boardFile.setFilename(filename);
			bfdao.insert(boardFile);
		}
		if(cnt>0) {
			return "추가 성공";
		}else {
			return "추가 실패";
		}
	}
	@Override
	public Map<String, Object> selectOne(int bnum) {
		//1.게시물 한건 조회
		Board board = bdao.selectOne(bnum);
		//2.게시물의 파일 조회
		List<BoardFile> bflist = bfdao.selectList(bnum);
		//3.댓글조회
		List<Board> rlist = bdao.select_reply(bnum);
		Map<String, Object> map = new HashMap<>();
		map.put("board", board);
		map.put("bflist", bflist);
		map.put("rlist", rlist);
		return map;
	}
	@Override
	public String delete(int bnum) {
		//주의 : fk때문에 자식데이터부터 삭제 후 부모데이터 삭제
		//게시물의 파일들 삭제
		bfdao.delete_bnum(bnum);
		//게시물 삭제
		int cnt = bdao.delete(bnum);
		if(cnt>0) {
			return "삭제 성공";
		}else {
			return "삭제 실패";
		}
		
	}
	@Override
	public String update(Board board, String[] filedels, List<String> filenames) {
		//게시물 수정
		int cnt = bdao.update(board);
		
		//파일들 삭제
		if(filedels != null) {
			for(String fnum : filedels) {
				bfdao.delete(Integer.parseInt(fnum));
			}
		}

		
		//파일들 추가
	

		for(String filename:filenames) {
			BoardFile boardFile = new BoardFile();
			boardFile.setBnum(board.getBnum());
			boardFile.setFilename(filename);
			System.out.println(boardFile);
			bfdao.insert(boardFile);
		}
		
		if(cnt>0) {
			return "수정 성공";
		}else {
			return "수정 실패";
		}
	
		
	}
	@Override
	public void cntplus(int bnum) {
		
		bdao.cntplus(bnum);
	}


}
