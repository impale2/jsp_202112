package dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import dto.Member;

public class MemberDaoImpl implements MemberDao {
	SqlSession session = MBConn.getSession();
	@Override
	public int insert(Member member) {
		return session.insert("MemberMapper.insert", member);
	}

	@Override
	public int update(Member member) {
		
		return session.update("MemberMapper.update", member);
	}

	@Override
	public int delete(String email) {
		
		return session.delete("MemberMapper.delete", email);
	}

	@Override
	public Member selectOne(String email) {

		return session.selectOne("MemberMapper.selectOne",email);
	}

	@Override
	public List<Member> selectList(Map<String, String> map) {
		
		return session.selectList("MemberMapper.selectList",map);
	}

}
