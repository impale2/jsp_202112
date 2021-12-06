package service;

import dao.BoardDao;
import dao.BoardDaoImpl;
import dto.Board;

public class ReplyServiceImpl implements ReplyService{
	private BoardDao bdao = new BoardDaoImpl();
	@Override
	public String insert(Board board) {
		//부모의 글순서(resetp)+1
		board.setRestep(board.getRestep()+1);
		//부모의 글레벨(relevel)+1
		board.setRelevel(board.getRelevel()+1);
		System.out.println("service:"+board);
		//
		bdao.restepplus(board);
		int cnt = bdao.insert(board);
		System.out.println(cnt + "건 댓글 추가");
		if(cnt>0) {
			return "추가 성공";
		}else {
			return"추가 실패";
		}
	}
	@Override
	public Board seletOne(int bnum) {
		Board board = bdao.selectOne(bnum);
		return board;
	}
	@Override
	public String update(Board board) {
		int cnt = bdao.update(board);
		if(cnt>0) {
			return "추가 성공";
		}else {
			return "추가 실패";
		}
	}

}
