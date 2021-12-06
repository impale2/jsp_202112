package service;

import java.util.List;
import java.util.Map;

import dto.Member;

public interface MemberService {
	String insert(Member member);
	String update(Member member,String changepw);
	String delete(String email);
	Member selectOne(String email);
	//map:검색조건
	List<Member> selectList(Map<String , String> map);
	Map<String, String> login(String email, String passwd);
}
