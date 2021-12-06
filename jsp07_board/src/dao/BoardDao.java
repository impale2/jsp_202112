package dao;

import java.util.List;


import dto.Board;
import dto.Page;

public interface BoardDao {
	void cntplus(int bnum);
	int insert(Board board);
	int update(Board board);
	int delete(int bnum);
	Board selectOne(int bnum);
	List<Board> selectList(Page page);
	//전체게시물수
	int select_totcnt(Page page);
	//댓글순서 변경
	void restepplus(Board board);
	//댓글 조회
	List<Board> select_reply(int ref);
}
