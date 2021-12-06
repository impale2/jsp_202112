package service;

import java.util.List;
import java.util.Map;

import dto.Board;
import dto.Page;

public interface BoardService {
	String update(Board board, String[] filedels, List<String> filenames);
	String delete(int bnum);
	
	List<Board> selectList(Page page);
	String insert(Board board, List<String> filename);
	Map<String, Object> selectOne(int bnum);
	//조회수 +1
	void cntplus(int bnum);
}
