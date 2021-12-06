package dao;

import static org.junit.jupiter.api.Assertions.*;


import java.util.List;


import org.junit.jupiter.api.Test;

import dto.Board;
import dto.BoardFile;

class test {
	BoardDao bdao = new BoardDaoImpl();
	BoardFileDao bfd = new BoardFileDaoImpl();
	@Test
	void testinsert() {
		Board board = new Board();
		board.setEmail("kang@gmail.com");
		board.setContent("내용");
		board.setSubject("제목");
		board.setIp("192.168.0.1");
		int cnt = bdao.insert(board);
		System.out.println(cnt);
		
	}
	@Test
	void testdelete() {


		int cnt = bdao.delete(2);
		System.out.println(cnt);
		
	}
	@Test
	void testselectOne() {
		Board board = bdao.selectOne(3);
		System.out.println(board);
		assertNotEquals(null, board);
		
	}

	@Test
	void testBFile() {
		BoardFile boardFile = new BoardFile();
		boardFile.setBnum(3);
		boardFile.setFilename("dd.jpg");
		System.out.println(boardFile);
		int cnt = bfd.insert(boardFile);
		System.out.println(cnt);

	}
}
