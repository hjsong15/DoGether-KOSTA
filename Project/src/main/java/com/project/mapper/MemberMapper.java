package com.project.mapper;
import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.model.Member;

@Mapper
public interface MemberMapper {
	
public void MemberAdd(Member m);
	
	public void oAuthAdd(Member m);
	public Member FindID(String m_email); /* Member 와 string의 차이. */
	
	public String FindAddr(String m_email);
	
	public void editMember(Member m);
	
	public void delete(Member m);
	
//	public void changePwd(Member m);
	public void changePwd(Member m);

	public String FindPwd(@Param("m_email")String m_email, @Param("m_name")String m_name);
	/***** 이메일 인증 ******/
	public void EmailNum(@Param("m_code")String m_code,@Param("m_email")String email);
	
	public String EmailCode(@Param("m_code")String m_code,@Param("m_email")String email);
	
	public ArrayList<Member> AllMember();
	
	public int emailCheck(String m_email);
	
	public int nickCheck(String m_nickname);
}
