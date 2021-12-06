package service;

import dto.Board;

public interface ReplyService {
	String insert(Board board);
	String update(Board board);
	Board seletOne(int bnum);
	
}
