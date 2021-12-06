package service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.MemberDao;
import dao.MemberDaoImpl;
import dto.Member;

public class MemberServiceImpl implements MemberService {
	private MemberDao mdao = new MemberDaoImpl();
	
	@Override
	public String insert(Member member) {
		//비밀번호를 암호화 해서 저장하기
		String salt = saltmake(); //솔트생성
		String secretpw = sha256(member.getPasswd(), salt);
		member.setPasswd(secretpw);
		member.setSalt(salt);
		int cnt = mdao.insert(member);
		if(cnt>0) {
			return "추가 완료";
		}else {
			return "추가 실패";
		}

	}
	//salt를 램덤하게 만들기
	public String saltmake() {
		String salt = null;
		try {
			//난수를생성해준다
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			byte[] bytes = new byte[16]; //빈배열
			sr.nextBytes(bytes); //랜덤한 값을 bytes에 만든다 
			//byte데이터를 String형으로 
			salt = new String(Base64.getEncoder().encode(bytes));
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return salt;
	}
	//평문을 암호문으로 변경
	public String sha256(String passwd, String salt) {
		StringBuffer sb = new StringBuffer();
		
		try {
			//SHA-256 : 단방향 암호기법, 복호화불가능 256bit(16진수 64자리)
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(passwd.getBytes()); //문자열을 바이트 배열로 변경해서 전달
			md.update(salt.getBytes()); //솔트 추가
			
			byte[] data= md.digest(); //암호화된 바이트 배열(32byte)
			//16진수 문자열로 변경 sb변수에 추가
			System.out.println("암호화된 바이트 배열:" + Arrays.toString(data));
			for(byte b : data) {
				sb.append(String.format("%02x", b)) ;
			}
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	@Override
	public String update(Member member, String changepw) {
		//한건조회
		String msg = null;
		
		Member dbmember = mdao.selectOne(member.getEmail());
		String dbpw = dbmember.getPasswd(); //암호화된 db비빌번호
		
		//입력한 비밀번호를 암호화(권한체크)
		String secretpw = sha256(member.getPasswd(), dbmember.getSalt());
		
		if (dbpw.equals(secretpw)) {//변경권한생김
			//비밀번호가 변경됬다면 
			if (!changepw.equals("")) {
				//솔트구하기
				String salt = saltmake();
				//새로운 암호비밀번호를 얻기위해서
				secretpw =sha256(changepw, salt);
				
				member.setPasswd(secretpw);
				member.setSalt(salt);
			}else { //비밀번호가 변경이 안됐다면 기존정보 다시 세팅
				member.setPasswd(dbpw);
				member.setSalt(dbmember.getSalt());
			}
			System.out.println("service:" + member);
			mdao.update(member);
			msg = "수정완료";
		}else {
			msg = "비밀번호가 일치하지 않습니다";
		}
		return msg;
	}

	@Override
	public String delete(String email) {
		// TODO Auto-generated method stub
		int cnt = mdao.delete(email);
		if(cnt>0) {
			return "추가 완료";
		}else {
			return "추가 실패";
		}
	}

	@Override
	public Member selectOne(String email) {
		// TODO Auto-generated method stub
		return mdao.selectOne(email);
	}

	@Override
	public List<Member> selectList(Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Map<String, String> login(String email, String passwd) {
		Map<String, String>map = new HashMap<>();
		//0:로그인성공
		//1:이메일불일치
		//2:비밀번호불일치
		String msg= null; //메세지
		String rcode= null;//결과코드값
		Member member = mdao.selectOne(email);
		if(member == null) {
			msg = "회원정보가 없습니다";
			rcode ="1";
		}else {
			String pw = member.getPasswd();
			String salt = member.getSalt();
			//암호화된 비밀번호
			String secretpw = sha256(passwd,salt);
			//db에 저장된 비밀번호와 암호화된 비밀번호가 일치한다면
			if(pw.equals(secretpw)) {
				msg = "로그인 성공";
				rcode = "0";
			}else {
				msg = "비밀번호가 일치하지 않습니다";
				rcode = "2";
			}
			
		}
		map.put("msg", msg);
		map.put("rcode", rcode);
		System.out.println(map);
		return map;
		
	
	}

}
