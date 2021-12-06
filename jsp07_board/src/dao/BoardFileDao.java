package dao;

import java.util.List;

import dto.BoardFile;

public interface BoardFileDao {
	int insert(BoardFile boardFile);
	int update(BoardFile boardFile);
	int delete(int fnum);
	int delete_bnum(int bnum);
	BoardFile selectOne(int fnum);
	List<BoardFile> selectList(int fnum);
	
}
